using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using MySql.Data.MySqlClient;

using AppStore.Model;
using AppStore.Common;


namespace AppStore.DAL
{
    /// <summary>
    /// 与分组相关的DAL
    /// </summary>
    public class AppCommentsDAL : BaseDAL
    {
        #region 评论列表
        /// <summary>
        /// 获取评论列表
        /// </summary>
        /// <returns></returns>
        public List<AppCommentsEntity> GetDataList()
        {
            return GetDataList();
        }

        #endregion

        #region 评论列表
        /// <summary>
        /// 获取评论列表
        /// </summary>
        /// <returns></returns>
        public List<AppCommentsEntity> GetDataList(string searchType = null, string searchKey = null, string orderType = null
            , int pageIndex = -1, int pageSize = -1)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"select CommentID,
                                        UserName,
                                        UserEI,
                                        UserID,
                                        AppID,
                                        UserScore,
                                        LocalVerCode,
                                        LocalVerName,
                                        Comments,
                                        CommentTime,
                                        AuditStatus,
                                        Status
                                        from AppComments Where Status = 1 ");
            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            BuildConditions(commandText, searchType, searchKey, orderType, pageIndex, pageSize);

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), null))
            {
                return objReader.ReaderToList<AppCommentsEntity>() as List<AppCommentsEntity>;
            }
        }

        #endregion

        public int GetTotalCount(string searchType = null, string searchKey = null, string orderType = null
            , int pageIndex = 0, int pageSize = 0)
        {
            StringBuilder commandText = new StringBuilder();
            commandText.Append(@"select Count(*) from AppComments Where Status = 1 ");

            BuildConditions(commandText, searchType, searchKey, orderType, pageIndex, pageSize);

            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), null).Convert<int>();

        }

        private void BuildConditions(StringBuilder commandText, 
            string searchType, string searchKey, string orderType, int pageIndex, int pageSize)
        {
            if (!String.IsNullOrEmpty(searchKey))
            {
                string searchStringFmt = "";
                switch (searchType)
                {
                    case "appname":
                        searchStringFmt = "And ( Select Count(0) From AppInfo info Where info.AppID = AppComments.AppID And info.AppName like '%{0}%'  ) > 0 ";
                        break;
                    case "comments":
                        searchStringFmt = "And Comments like '%{0}%' ";
                        break;
                    default:
                        searchStringFmt = " And ( Comments like '%{0}%' or ( Select Count(0) From AppInfo info Where info.AppID = AppComments.AppID And info.AppName like '%{0}%'  ) > 0 ) ";
                        break;
                }
                commandText.AppendFormat(searchStringFmt, searchKey);
            }
            if (!String.IsNullOrEmpty(orderType))
            {
                commandText.AppendFormat("Order By CommentTime {0} ", orderType);
            }
            if (pageIndex > 0 && pageSize > 0)
            {
                commandText.AppendFormat("Limit {0} Offset {1}", pageSize, (pageIndex - 1) * pageSize);
            }
        }
    }
}
