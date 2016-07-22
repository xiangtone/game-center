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
    public class OperateRecordDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(OperateRecordEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@Id", entity.Id));
            paramsList.Add(new MySqlParameter("@ElemId", entity.ElemId));
            paramsList.Add(new MySqlParameter("@OperateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UserName", entity.UserName));
            paramsList.Add(new MySqlParameter("@OperateType", entity.OperateType));
            paramsList.Add(new MySqlParameter("@OperateFlag", entity.OperateFlag));
            paramsList.Add(new MySqlParameter("@SourcePage", entity.SourcePage));
            paramsList.Add(new MySqlParameter("@OperateExplain", entity.OperateExplain));
            paramsList.Add(new MySqlParameter("@OperateContent", entity.OperateContent));
            paramsList.Add(new MySqlParameter("@reason", entity.reason));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            return paramsList;
        }

        #endregion
        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, OperateRecordEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }

        #endregion

        /// <summary>
        /// 
        /// </summary>
        /// <param name="ElemId"></param>
        /// <returns></returns>
        public List<OperateRecordEntity> GetListByElemId(int ElemId)
        {
            #region CommandText

            string commandText = @"select * from OperateRecord WHERE Status =1 and ElemId=" + ElemId + " order by OperateTime desc ";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<OperateRecordEntity>() as List<OperateRecordEntity>;
            }

        }
         /// <summary>
        /// 
        /// </summary>
        /// <param name="ElemId"></param>
        /// <returns></returns>
        public List<OperateRecordEntity> GetReasonByElemId(int ElemId)
        {
            #region CommandText

            string commandText = @"select * from OperateRecord WHERE Status =1 and ElemId=" + ElemId + " order by OperateTime  LIMIT 0, 1";

            #endregion
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<OperateRecordEntity>() as List<OperateRecordEntity>;
            }

        }
       
        /// <summary>
        /// 新增信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(OperateRecordEntity entity)
        {

            #region CommandText

            string commandText = @" INSERT INTO OperateRecord (
                                                        ElemId,
                                                        UserName,
                                                        OperateType,
                                                        OperateFlag,
                                                         SourcePage,
                                                        OperateExplain,
                                                        OperateContent,
                                                        reason,
                                                        Status
                                                    ) 
                                                    VALUES
                                                        (
                                                           @ElemId,
                                                           @UserName,
                                                           @OperateType,
                                                           @OperateFlag,
                                                            @SourcePage,
                                                           @OperateExplain,
                                                           @OperateContent,
                                                           @reason,
                                                           @Status
                                                        ); SELECT LAST_INSERT_ID(); ";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result;
        }

        public List<OperateRecordEntity> GetDataList(int StartIndex, int EndIndex, int type, ref int totalCount)
        {
            #region CommandText

            string commandText = string.Format("select * from OperateRecord WHERE Status =1 and SourcePage='{0}' order by OperateTime desc LIMIT {1},{2}", type, StartIndex, EndIndex);

            string commandTextCount = string.Format("select count(*) from OperateRecord WHERE Status =1 and SourcePage='{0}' order by OperateTime desc", type);
            #endregion

            totalCount = MySqlHelper.ExecuteScalar(this.ConnectionString, commandTextCount).Convert<int>();
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<OperateRecordEntity>() as List<OperateRecordEntity>;
            }
        }

        public DataSet GetDataList(int type)
        {
            string commandText = string.Format("select UserName as '操作人',OperateExplain as '操作类型',OperateContent as '操作内容',OperateTime as '操作时间' from OperateRecord WHERE Status =1 and SourcePage='{0}' order by OperateTime desc", type);
            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText, null);
        }
    }
}
