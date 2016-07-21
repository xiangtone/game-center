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
    public class ChannelDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(ChannelEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@ChannelNO", entity.ChannelNO));
            paramsList.Add(new MySqlParameter("@ChannelName", entity.ChannelName));
            paramsList.Add(new MySqlParameter("@ChannelFlag", entity.ChannelFlag));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
     

            return paramsList;
        }

        #endregion


        #region 执行非查询操作
        private bool ExecuteNonQuery(string commandText, ChannelEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }
        #endregion

        /// <summary>
        /// 新增渠道信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(ChannelEntity entity)
        {
            #region CommandText

            string commandText = @" INSERT INTO Channel (
                                                        ChannelNO,
                                                        ChannelName,
                                                        ChannelFlag,
                                                        Remarks,
                                                        CreateTime,
                                                        UpdateTime,
                                                        Status
                                                    ) 
                                                    VALUES
                                                        (
                                                           @ChannelNO,
                                                           @ChannelName,
                                                           @ChannelFlag,
                                                           @Remarks,
                                                           @CreateTime,
                                                           @UpdateTime,
                                                           @Status
                                                        ); SELECT LAST_INSERT_ID(); ";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result;
        }

        /// <summary>
        /// 更新渠道信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(ChannelEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        Channel 
                                    SET
                                        ChannelName = @ChannelName,
                                        ChannelFlag = @ChannelFlag,
                                        Remarks  = @Remarks,
                                        UpdateTime  = @UpdateTime,
                                        Status  = @Status
                                    WHERE ChannelNO = @ChannelNO ;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 删除渠道信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            #region CommandText

            string commandText = @"UPDATE Channel SET Status = 3 WHERE ChannelNO= @ChannelNO";

            #endregion

            return ExecuteNonQuery(commandText, new ChannelEntity() { ChannelNO = ID });
        }

        /// <summary>
        /// 修改渠道信息状态
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID,int Status)
        {
            #region CommandText

            string commandText = @"UPDATE Channel SET Status = @Status WHERE ChannelNO= @ChannelNO";

            #endregion

            return ExecuteNonQuery(commandText, new ChannelEntity() { ChannelNO = ID, Status = Status });
        }
        /// <summary>
        /// 渠道列表信息
        /// </summary>
        /// <returns></returns>
        public List<ChannelEntity> Select()
        {
            #region CommandText

            string commandText = @"select * from Channel WHERE Status !=3";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<ChannelEntity>() as List<ChannelEntity>;
            } 
        }

        /// <summary>
        /// 绑定渠道列表信息
        /// </summary>
        /// <returns></returns>
        public List<ChannelEntity> BindList()
        {
            #region CommandText

            string commandText = @"select ChannelNO,concat(ChannelNO,':',ChannelName) as ChannelName,Status from Channel WHERE Status = 1";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<ChannelEntity>() as List<ChannelEntity>;
            }
        }
        /// <summary>
        /// 查询单个渠道信息
        /// </summary>
        /// <param name="Channelno"></param>
        /// <returns></returns>
        public ChannelEntity SelectByNo(int Channelno)
        {
            #region CommandText

            string commandText = @"select * from Channel WHERE Status !=3 and ChannelNO= "+Channelno;

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToModel<ChannelEntity>() as ChannelEntity;
            }
        }
    }
}
