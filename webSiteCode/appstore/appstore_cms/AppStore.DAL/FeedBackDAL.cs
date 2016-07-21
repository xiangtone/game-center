using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data;
using MySql.Data.MySqlClient;

using System.Data.SqlClient;
using System.Data;
using AppStore.Common;
using nwbase_utils;

namespace AppStore.DAL
{
    public class FeedBackDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(FeedBackEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@OpenId", entity.OpenId));
            paramsList.Add(new MySqlParameter("@Content", entity.Content));
            paramsList.Add(new MySqlParameter("@UserContact", entity.UserContact));
            paramsList.Add(new MySqlParameter("@ClientId", entity.ClientId));
            paramsList.Add(new MySqlParameter("@ChannelNo", entity.ChannelNo));
            paramsList.Add(new MySqlParameter("@Version", entity.Version));
            paramsList.Add(new MySqlParameter("@CreateTime", entity.CreateTime));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            paramsList.Add(new MySqlParameter("@UpdateTime", entity.UpdateTime));
            return paramsList;
        }

        #endregion


        #region 执行非查询操作
        private bool ExecuteNonQuery(string commandText, FeedBackEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }
        #endregion


        /// <summary>
        /// 列表信息
        /// </summary>
        /// <returns></returns>
        public List<FeedBackEntity> GetFeedBackList(int ClientId,int pageindex,int pagesize, ref int totalCount)
        {
            #region CommandText

            string commandText = string.Format("select * from feedback where ClientId={0} order by CreateTime  desc LIMIT {1},{2}", ClientId, pageindex,pagesize);

            #endregion

            #region CommandText2

            string commandText2 = @"select count(*) from feedback where ClientId=" + ClientId + "";

            #endregion
            totalCount =  MySqlHelper.ExecuteScalar(this.ConnectionString, commandText2.ToString()).Convert<int>();

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<FeedBackEntity>() as List<FeedBackEntity>;
            }
        }

        public int UpdateRemarks(int id, string r)
        {
            #region CommandText

            string commandText = @"update feedback set Remarks ='" + r + "',Status =2 where FBId = " + id;

            #endregion
            return MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText);
        }
    }
}
