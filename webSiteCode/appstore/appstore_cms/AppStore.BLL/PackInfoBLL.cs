using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class PackInfoBLL
    {

        /// <summary>
        /// 新增安装包信息;默认新增安装包为主安装包版本
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(PackInfoEntity entity)
        {
            return new PackInfoDAL().Insert(entity);
        }
        public int InsertByAuto(PackInfoEntity entity)
        {
            return new PackInfoDAL().InsertByAuto(entity);
        }

        /// <summary>
        /// 删除安装包信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            return new PackInfoDAL().Delete(ID);
        }

        /// <summary>
        /// 更新一条安装包信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(PackInfoEntity entity)
        {
            return new PackInfoDAL().Update(entity);
        }

        /// <summary>
        /// 获取安装包列表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<PackInfoEntity> GetDataList(PackInfoEntity entity)
        {
            return new PackInfoDAL().GetDataList(entity);
        }

        /// <summary>
        /// 获取一条安装包信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public PackInfoEntity GetSingle(int ID)
        {
            return new PackInfoDAL().GetSingle(ID);
        }

        /// <summary>
        /// 获取最新一条安装包信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public PackInfoEntity GetNewSingle(int AppID)
        {
            return new PackInfoDAL().GetNewSingle(AppID);
        }
        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCount(PackInfoEntity entity)
        {
            return new PackInfoDAL().GetTotalCount(entity);
        }

        /// <summary>
        /// 更新安装包主版本
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateMainVersion(PackInfoEntity entity)
        {
            return new PackInfoDAL().UpdateMainVersion(entity);
        }

        /// <summary>
        /// 获取应用下的安装包个数
        /// </summary>
        /// <param name="AppID"></param>
        /// <returns></returns>
        public int GetAppToPackCount(int AppID)
        {
            return new PackInfoDAL().GetAppToPackCount(AppID);
        }
        public bool UpdateMainVer(PackInfoEntity entity)
        {
            return new PackInfoDAL().UpdateMainVer(entity);
        }

       
    }
}
