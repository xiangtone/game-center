using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AppStore.Web
{
    /// <summary>
    /// 模块菜单
    /// </summary>
    public class ModuleMenu : ModuleInfo
    {
        /// <summary>
        /// 模块菜单
        /// </summary>
        public ModuleMenu(ModuleInfo module)
        {
            ModuleId = module.ModuleId;
            ParentId = module.ParentId;
            ModuleName = module.ModuleName;
            ModuleFlag = module.ModuleFlag;
            ModulePath = module.ModulePath;
            ModuleType = module.ModuleType;
            OrderNo = module.OrderNo;
            PermType = module.PermType;
            PermDefine = module.PermDefine;
            ModuleUrl = module.ModuleUrl;
            ActionValue = module.ActionValue;
            ModuleDesc = module.ModuleDesc;
            CreateTime = module.CreateTime;
            Status = module.Status;
        }

        public ModuleMenu()
        { }

        public List<ModuleMenu> ChileMenu { get; set; }

        private enum RelationShip { Brother, Child, Childson, Parent, Other };

        private RelationShip GetRelationShip(ModuleInfo module)
        {
            if (this.ModulePath == module.ModulePath && this.ParentId == module.ParentId)
            {
                return RelationShip.Brother;
            }
            else if (this.ModuleId == module.ParentId)
            {
                return RelationShip.Child;
            }
            else if (this.ParentId == module.ModuleId)
            {
                return RelationShip.Parent;
            }
            else if (module.ModulePath.Contains(this.ModulePath))
            {
                return RelationShip.Childson;
            }
            else
                return RelationShip.Other;
        }

        public static ModuleMenu BuildTree(Dictionary<int, ModuleInfo> moduleList)
        {
            if (moduleList != null)
            {
                ModuleMenu baseMenu = new ModuleMenu();
                baseMenu.ModuleId = moduleList[1].ModuleId;
                baseMenu.ModulePath = moduleList[1].ModulePath;
                baseMenu.ParentId = 0;

                var childModuleList = moduleList.Values.Where(s => s.ModuleId != 1);

                BuildTree(baseMenu, childModuleList);

                return baseMenu;
            }
            else
                return null;
        }

        public static ModuleMenu BuildTree(Dictionary<int, ModuleInfo> moduleList, IEnumerable<int> moduleIdFilter)
        {
            if (moduleList != null && moduleIdFilter.Count() > 0)
            {
                var modulePaths = moduleList.Where(s => moduleIdFilter.Contains(s.Key)).Select(e => e.Value.ModulePath);
                string modulePathSring = string.Join("", modulePaths);
                modulePathSring += "," + string.Join(",", moduleIdFilter);
                var eachIdPart = modulePathSring.Split(',');
                var toTest = eachIdPart.Where(es => !string.IsNullOrEmpty(es)).Select(s => int.Parse(s));

                var newModuleList = moduleList.Where(s => eachIdPart.Where(es => !string.IsNullOrEmpty(es)).Select(ess => int.Parse(ess)).Contains(s.Key)).ToDictionary(bs => bs.Key, bs => bs.Value);
                return BuildTree(newModuleList);
            }
            else
                return null;
            //return BuildTree(moduleList);
        }

        private static void BuildTree(ModuleMenu menu, IEnumerable<ModuleInfo> moduleList)
        {
            var sonList = moduleList.Where(s => menu.GetRelationShip(s) == RelationShip.Child);
            var childRelationShip = new RelationShip[] { RelationShip.Child, RelationShip.Childson };

            if (sonList != null)
            {
                menu.ChileMenu = new List<ModuleMenu>();
                foreach (var eachSon in sonList)
                {
                    var newSon = new ModuleMenu(eachSon);
                    var hisChild = moduleList.Where(s => childRelationShip.Any(e => e == newSon.GetRelationShip(s)));
                    if (hisChild != null)
                        BuildTree(newSon, hisChild);
                    menu.ChileMenu.Add(newSon);
                }
                menu.ChileMenu = menu.ChileMenu.OrderBy(s => s.OrderNo).ToList<ModuleMenu>();
            }
        }
    }
}