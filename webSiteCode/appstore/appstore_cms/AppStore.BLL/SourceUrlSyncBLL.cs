using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AppStore.Model;

using AppStore.DAL;

namespace AppStore.BLL
{
    public class SourceUrlSyncBLL
    {
        /// <summary>
        /// 更新AppInfo下的缩略图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppInfoThumbPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateAppInfoThumbPicUrl(entity);
        }

        /// <summary>
        /// 更新AppInfo下的主安装包的Icon
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppInfoMainIconPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateAppInfoMainIconPicUrl(entity);
        }

        /// <summary>
        /// 更新应用截图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppPicListPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateAppPicListPicUrl(entity);
        }

        /// <summary>
        /// 更新分组元素中的推荐图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateGroupElemsRecommPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateGroupElemsRecommPicUrl(entity);
        }

        /// <summary>
        /// 更新分组信息中的分组图片地址
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateGroupInfoGroupPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateGroupInfoGroupPicUrl(entity);
        }

        /// <summary>
        /// 更新分组类型中的图片
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateGroupTypeTypePicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateGroupTypeTypePicUrl(entity);
        }

        /// <summary>
        /// 更新LinkInfo中的缩略图
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateLinkInfoThumbPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateLinkInfoThumbPicUrl(entity);
        }

        /// <summary>
        /// 更新LinkInfo中的Icon地址
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateLinkInfoIconPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdateLinkInfoIconPicUrl(entity);
        }

        /// <summary>
        /// 更新安装包中的Icon信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdatePackInfoIconPicUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdatePackInfoIconPicUrl(entity);
        }

        /// <summary>
        /// 更新安装包中的安装包地址
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdatePackInfoPackUrl(SourceEntity.SourceItems entity)
        {
            return new SourceUrlSyncDAL().UpdatePackInfoPackUrl(entity);
        }
    }
}
