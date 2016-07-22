using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Model;
using MySql.Data.MySqlClient;

namespace AppStore.DAL
{
    public class SourceUrlSyncDAL : BaseDAL
    {

        private bool ExecuteNonQuery(string commandText, List<MySqlParameter> paramsList)
        {
            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }

        /// <summary>
        /// 更新AppInfo下的ThumbPicUrl信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppInfoThumbPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE AppInfo SET ThumbPicUrl = @ThumbPicUrl WHERE ThumbPicUrl =@OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@ThumbPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }


        /// <summary>
        /// 更新AppInfo下的MainIconPicUrl信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppInfoMainIconPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE AppInfo SET MainIconPicUrl = @MainIconPicUrl WHERE MainIconPicUrl =@OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@MainIconPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        /// <summary>
        /// 更新AppPicList下的PicUrl信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateAppPicListPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE AppPicList SET PicUrl = @PicUrl WHERE PicUrl =@OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@PicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdateGroupElemsRecommPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE GroupElems SET RecommPicUrl = @RecommPicUrl WHERE RecommPicUrl =@OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@RecommPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdateGroupInfoGroupPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE GroupInfo SET GroupPicUrl = @GroupPicUrl WHERE GroupPicUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@GroupPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdateGroupTypeTypePicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE GroupType SET TypePicUrl = @TypePicUrl WHERE TypePicUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@TypePicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdateLinkInfoThumbPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE LinkInfo SET ThumbPicUrl = @ThumbPicUrl WHERE ThumbPicUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@ThumbPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdateLinkInfoIconPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE LinkInfo SET IconPicUrl = @IconPicUrl WHERE IconPicUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@IconPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdatePackInfoIconPicUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE PackInfo SET IconPicUrl = @IconPicUrl WHERE IconPicUrl = @OldResUrl; UPDATE AppInfo SET MainIconPicUrl = @IconPicUrl WHERE MainIconPicUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@IconPicUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }

        public bool UpdatePackInfoPackUrl(SourceEntity.SourceItems entity)
        {
            string commandText = @"UPDATE PackInfo SET PackUrl = @PackUrl WHERE PackUrl = @OldResUrl";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@OldResUrl", entity.oldResUrl));
            paramsList.Add(new MySqlParameter("@PackUrl", entity.newResUrl));

            return this.ExecuteNonQuery(commandText, paramsList);
        }
    }
}
