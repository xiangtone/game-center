using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppInfoBLL
    {
        /// <summary>
        /// 新增应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(AppInfoEntity entity)
        {
            return new AppInfoDAL().Insert(entity);
        }
        /// <summary>
        /// 更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(AppInfoEntity entity)
        {
            return new AppInfoDAL().Update(entity);
        }
        /// <summary>
        /// 更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdatePackCount(AppInfoEntity entity)
        {
            return new AppInfoDAL().UpdatePackCount(entity);
        }
        /// <summary>
        /// 删除应用信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            return new AppInfoDAL().Delete(ID);
        }

        public bool UpdateStatus(int ID, int Status)
        {
            return new AppInfoDAL().UpdateStatus(ID, Status);
        }
        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoEntity> GetDataList(AppInfoEntity entity, ref int totalCount, int SchemeID = 0)
        {
            string channel = "";

            if (SchemeID == 104)
            {
                channel = ",70,";

            }
            else if (SchemeID == 101)
            {
                channel = ",20,";
            }
            string status = "1,2";
            if (SchemeID == 0)
            {
                status = "1,2,3,4,5,6,7"; 
            }
            totalCount = new AppInfoDAL().GetTotalCountNew(entity, status, channel);
            return new AppInfoDAL().GetDataListNew(entity, status, channel);

        }
        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoEntity> GetDataListNew(AppInfoEntity entity, string status, string channel, ref int totalCount)
        {
            totalCount = new AppInfoDAL().GetTotalCountNew(entity, status, channel);
            return new AppInfoDAL().GetDataListNew(entity, status, channel);

        }
        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoEntity GetSingle(int ID)
        {
            return new AppInfoDAL().GetSingle(ID);
        }
        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoEntity GetSingle2(int ID)
        {
            return new AppInfoDAL().GetSingle2(ID);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public PackInfoEntity GetNewInfo(int appid)
        {
            return new AppInfoDAL().GetNewInfo(appid);
        }

        /// <summary>
        /// 判断是否已经推荐过一条应用信息
        /// </summary>
        /// <param name="appID"></param>
        /// <returns></returns>
        public bool IsExistRecommApp(int appID)
        {
            return new AppInfoDAL().IsExistRecommApp(appID);
        }

        /// <summary>
        /// 判断已经存在同一款应用
        /// </summary>
        /// <param name="showName"></param>
        /// <returns></returns>
        public bool IsExistAppInfo(string showName)
        {
            return new AppInfoDAL().IsExistAppInfo(showName);
        }

        /// <summary>
        /// 上传安装包后更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpDatePackInfo(AppInfoEntity entity)
        {
            return new AppInfoDAL().UpDatePackInfo(entity);
        }

        /// <summary>
        /// 判断在GroupElems中是否存在
        /// </summary>
        /// <param name="elemID"></param>
        /// <returns></returns>
        public bool IsExistGroupElems(int elemID)
        {
            return new AppInfoDAL().IsExistGroupElems(elemID);
        }
        public bool DelGroupElems(int elemID)
        {
            return new AppInfoDAL().DelGroupElems(elemID);
        }
        /// <summary>
        /// 从统计数据库中获取联运游戏列表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoEntity> GetUAppInfoList(AppInfoEntity entity, ref int totalCount)
        {
            totalCount = new AppInfoDAL().GetUAppInfoCount(entity);
            return new AppInfoDAL().GetUAppInfoList(entity);
        }

        /// <summary>
        /// 获取当前AppID下的安装包数量
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, int> GetPacksCount()
        {
            return new AppInfoDAL().GetPacksCount();
        }

        /// <summary>
        /// 获取当前AppID下的安装包数量
        /// </summary>
        /// <returns></returns>
        public int GetPacksCount(int Appid)
        {
            return new AppInfoDAL().GetPacksCount(Appid);
        }
        /// <summary>
        /// 判断应用安装包是否重复
        /// </summary>
        /// <param name="AppID">应用ID，当为0时，则判断其他应用是否存在此包，否则判断添加的包是否一致</param>
        /// <param name="PackName"></param>
        /// <param name="PackSign"></param>
        /// <returns></returns>
        public int CheckAppRepeat(int AppID, string PackName, string PackSign)
        {
            return new AppInfoDAL().CheckAppRepeat(AppID, PackName, PackSign);
        }


        /// <summary>
        /// 通过安装包PackName + PackSign获取应用信息
        /// </summary>
        /// <param name="PackName">包名</param>
        /// <param name="PackSign">签名验证码</param>
        /// <returns></returns>
        public AppInfoEntity GetAppByPackNameAndPackSign(string PackName, string PackSign)
        {
            return new AppInfoDAL().GetAppByPackNameAndPackSign(PackName, PackSign);
        }

        /// <summary>
        /// 修改更新时间
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool ChangeUpdateTime(AppInfoEntity entity)
        {
            return new AppInfoDAL().ChangeUpdateTime(entity);
        }

        public List<AppInfoEntity> SelectListByStatus(int p)
        {
            return new AppInfoDAL().SelectListByStatus(p);
        }

        public bool DeleteByID(AppInfoEntity entity)
        {
            return new AppInfoDAL().DeleteByID(entity);
        }

        public int GetCountById(int ID)
        {
            return new AppInfoDAL().GetCountById(ID);
        }

        public DataSet GetExportDataList(AppInfoEntity entity, string status, string channel)
        {
            return new AppInfoDAL().GetExportDataList(entity, status, channel);
        }
        public DataSet GetExportDataList2(AppInfoEntity entity, string status, string channel)
        {
            return new AppInfoDAL().GetExportDataList2(entity, status, channel);
        }

        public bool UpdateArch(int id, int arch)
        {
            return new AppInfoDAL().UpdateArch(id, arch);
        }
    }
}
