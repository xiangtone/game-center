using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppTypeBLL
    {
        /// <summary>
        /// 根据分类获取应用类型
        /// </summary>
        /// <param name="AppClass">应用分类，当为0时，表示所有分类</param>
        /// <returns></returns>
        public List<AppTypeEntity> GetAPPTypeList(int AppClass)
        {
            return new AppTypeDAL().GetAPPTypeList(AppClass);
        }


        public AppTypeEntity GetSingle(int id)
        {
            return new AppTypeDAL().GetSingle(id);
        }
    }
}
