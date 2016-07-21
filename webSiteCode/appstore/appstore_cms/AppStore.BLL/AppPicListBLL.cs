using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppPicListBLL
    {
        /// <summary>
        /// 新增应用截图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Insert(AppPicListEntity entity)
        {
            return new AppPicListDAL().Insert(entity);
        }

        /// <summary>
        /// 更新应用截图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(AppPicListEntity entity)
        {
            return new AppPicListDAL().Update(entity);
        }

        /// <summary>
        /// 获取指定安装包下所有应用截图
        /// </summary>
        /// <param name="packID"></param>
        /// <returns></returns>
        public List<AppPicListEntity> GetDataList(int packID)
        {
            return new AppPicListDAL().GetDataList(packID);
        }

        public bool DeleteByPackID(int packID)
        {
            return new AppPicListDAL().DeleteByPackID(packID);
        }

        public bool DeleteByIDs(string IDs)
        {
            return new AppPicListDAL().DeleteByIDs(IDs);
        }

        public bool UpdataByID(AppPicListEntity currentEntity)
        {
            return new AppPicListDAL().UpdataByID(currentEntity);
        }
    }
}
