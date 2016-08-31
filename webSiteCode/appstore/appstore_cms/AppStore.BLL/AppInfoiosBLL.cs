using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppInfoiosBLL
    {
        /// <summary>
        /// 新增应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(AppInfoiosEntity entity)
        {
            return new AppInfoiosDAL().Insert(entity);
        }
        /// <summary>
        /// 更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(AppInfoiosEntity entity)
        {
            return new AppInfoiosDAL().Update(entity);
        }
     
        /// <summary>
        /// 删除应用信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            return new AppInfoiosDAL().Delete(ID);
        }

        public bool UpdateStatus(int ID, int Status)
        {
            return new AppInfoiosDAL().UpdateStatus(ID, Status);
        }

        /// <summary>
        /// 获取应用总数及所有应用数据
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoiosEntity> GetDataList(AppInfoiosEntity entity, ref int totalCount)
        {
            totalCount = new AppInfoiosDAL().GetTotalCount(entity);
            return new AppInfoiosDAL().GetDataListNew(entity);

        }
        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoiosEntity> GetDataListNew(AppInfoiosEntity entity,ref int totalCount)
        {
            totalCount = new AppInfoiosDAL().GetTotalCount(entity);
            return new AppInfoiosDAL().GetDataListNew(entity);

        }
        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoiosEntity GetSingle(int ID)
        {
            return new AppInfoiosDAL().GetSingle(ID);
        }
      
    
        /// <summary>
        /// 判断已经存在同一款应用
        /// </summary>
        /// <param name="showName"></param>
        /// <returns></returns>
        public bool IsExistAppInfo(string AppName)
        {
            return new AppInfoiosDAL().IsExistAppInfo(AppName);
        }


        /// <summary>
        /// 修改更新时间
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        
        public bool ChangeUpdateTime(AppInfoiosEntity entity)
        {
            return new AppInfoiosDAL().ChangeUpdateTime(entity);
        }

        /// <summary>
        /// 根据状态取得应用数据
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoiosEntity> SelectListByStatus(int p)
        {
            return new AppInfoiosDAL().SelectListByStatus(p);
        }

        public bool DeleteByID(AppInfoiosEntity entity)
        {
            return new AppInfoiosDAL().DeleteByID(entity);
        }

        public int GetCountById(int ID)
        {
            return new AppInfoiosDAL().GetCountById(ID);
        }

        public DataSet GetExportDataList(AppInfoiosEntity entity, string status)
        {
            return new AppInfoiosDAL().GetExportDataList(entity, status);
        }
       
    }
}
