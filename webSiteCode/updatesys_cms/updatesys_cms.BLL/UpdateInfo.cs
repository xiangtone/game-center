using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace updatesys_cms.BLL
{
    public class UpdateInfo
    {
        /// <summary>
        /// 获取所有更新信息
        /// </summary>
        /// <param name="appid"></param>
        /// <returns></returns>
        public List<Model.UpdateInfo> GetAll(int appid, int schemeId)
        {
            var result = new DAL.UpdateInfo().GetAll(appid, schemeId);
            if (result != null)
            {
                var appList = new AppInfo().GetDictAll();
                if (appList != null)
                {
                    foreach (var eachItem in result)
                    {
                        if (appList.ContainsKey(eachItem.AppId))
                        {
                            eachItem.AppName = appList[eachItem.AppId].AppName;
                        }
                    }
                }
            }
            return result;
        }

        /// <summary>
        /// 新增更新信息
        /// </summary>
        /// <param name="updateInfo"></param>
        /// <returns></returns>
        public bool Add(Model.UpdateInfo updateInfo)
        {
            return new DAL.UpdateInfo().Add(updateInfo);
        }

        /// <summary>
        /// 修改更新信息
        /// </summary>
        /// <param name="updateInfo"></param>
        /// <returns></returns>
        public bool Update(Model.UpdateInfo updateInfo)
        {
            return new DAL.UpdateInfo().Update(updateInfo);
        }

        /// <summary>
        /// 删除更新信息
        /// </summary>
        /// <param name="updateId"></param>
        /// <returns></returns>
        public bool Del(int updateId)
        {
            return new DAL.UpdateInfo().Del(updateId);
        }

         /// <summary>
        /// 获取更新信息
        /// </summary>
        /// <param name="appid"></param>
        /// <returns></returns>
        public Model.UpdateInfo GetOne(int updateId)
        {
            return new DAL.UpdateInfo().GetOne(updateId);
        }
        /// <summary>
        /// 获取最新的安装包信息（通用方案
        /// </summary>
        /// <returns></returns>
        public Dictionary<string, Model.UpdateInfo> GetNewList()
        {
            var updateList = new DAL.UpdateInfo().GetNewList();
            if (updateList != null)
            {
                Dictionary<string, Model.UpdateInfo> result = new Dictionary<string, Model.UpdateInfo>();
                foreach (var eachItem in updateList)
                {
                    string keyName = string.Format("{0}_{1}_{2}", eachItem.SchemeId, eachItem.PackName, eachItem.ChannelNo);
                    if (result.ContainsKey(keyName))
                    {
                        var oldItem = result[keyName];
                        if (oldItem.VerCode < eachItem.VerCode)
                            result[keyName] = eachItem;
                    }
                    else
                        result.Add(keyName, eachItem);


                }
                return result;
            }
            else
                return null;
        }

        
    }
}
