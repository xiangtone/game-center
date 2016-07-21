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
    public class AppCommentSummaryDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(AppCommentSummaryEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>() { 
                    new MySqlParameter("@CommentTimes",entity.CommentTimes),
					new MySqlParameter("@ScoreTimes",entity.ScoreTimes),
					new MySqlParameter("@ScoreSum",entity.ScoreSum),
					new MySqlParameter("@ScoreAvg",entity.ScoreAvg),
					new MySqlParameter("@ScoreTimes1",entity.ScoreTimes1),
					new MySqlParameter("@ScoreTimes2",entity.ScoreTimes2),
					new MySqlParameter("@ScoreTimes3",entity.ScoreTimes3),
					new MySqlParameter("@ScoreTimes4",entity.ScoreTimes4),
					new MySqlParameter("@ScoreTimes5",entity.ScoreTimes5),
					new MySqlParameter("@AppId",entity.AppID)
            };
            return paramsList;
        }

        #endregion
        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, AppCommentSummaryEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }


        #endregion
        public int Exists(int AppId)
        {
            StringBuilder strSql = new StringBuilder();
            string commandText = @"Select count(*) from appcommentsummary  WHERE AppId= " + AppId;
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText).Convert<int>();
        }
        public bool Delete(int AppId)
        {
            #region CommandText

            string commandText = @"Delete from appcommentsummary   WHERE AppId= @AppID";

            #endregion
            if (Exists(AppId) > 0)
            {
              
                return ExecuteNonQuery(commandText, new AppCommentSummaryEntity() { AppID = AppId });
            }
            else
            {
              
                return true;
            }

        }
        public bool Insert(AppCommentSummaryEntity entity)
        {
            #region CommandText

            string commandText = @" INSERT INTO appcommentsummary (
                                                        AppId,
                                                        CommentTimes,
                                                        ScoreTimes,
                                                        ScoreSum,
                                                        ScoreAvg,
                                                        ScoreTimes1,
                                                        ScoreTimes2,
                                                        ScoreTimes3,
                                                        ScoreTimes4,
                                                        ScoreTimes5
                                                    ) 
                                                    VALUES
                                                        (
                                                            @AppId,
                                                            @CommentTimes,
                                                            @ScoreTimes,
                                                            @ScoreSum,
                                                            @ScoreAvg,
                                                            @ScoreTimes1,
                                                            @ScoreTimes2,
                                                            @ScoreTimes3,
                                                            @ScoreTimes4,
                                                            @ScoreTimes5                                                          
                                                        );";

            #endregion
            return ExecuteNonQuery(commandText, entity);

        }
    }
}
