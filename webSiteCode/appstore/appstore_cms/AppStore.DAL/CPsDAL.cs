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
    public class CPsDAL : BaseDAL
    {

        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(CPsEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@CPID", entity.CPID));
            paramsList.Add(new MySqlParameter("@CPType", entity.CPType));
            paramsList.Add(new MySqlParameter("@IsDeveloper", entity.IsDeveloper));
            paramsList.Add(new MySqlParameter("@CPName", entity.CPName));
            paramsList.Add(new MySqlParameter("@FullName", entity.FullName));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            return paramsList;
        }

        #endregion


        #region 执行非查询操作
        private bool ExecuteNonQuery(string commandText, CPsEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }
        #endregion

        /// <summary>
        /// 新增CPs信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(CPsEntity entity)
        {
            #region CommandText

            string commandText = @" INSERT INTO CPs (
                                                        CPType,
                                                        IsDeveloper,
                                                        CPName,
                                                        FullName,
                                                        Remarks,
                                                        CreateTime,
                                                        UpdateTime,
                                                        Status
                                                    ) 
                                                    VALUES
                                                        (
                                                           @CPType,
                                                           @IsDeveloper,
                                                           @CPName,
                                                           @FullName,
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
        /// 更新CPs信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(CPsEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        CPs 
                                    SET
                                        CPType = @CPType,
                                        IsDeveloper = @IsDeveloper,
                                        CPName = @CPName,
                                        FullName = @FullName,
                                        Remarks  = @Remarks,
                                        UpdateTime  = @UpdateTime,
                                        Status  = @Status
                                    WHERE CPID = @CPID ;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 修改信息状态
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID, int Status)
        {
            #region CommandText

            string commandText = @"UPDATE CPs SET Status = @Status WHERE CPID= @CPID";

            #endregion

            return ExecuteNonQuery(commandText, new CPsEntity() { CPID = ID, Status = Status });
        }
        /// <summary>
        /// 删除CPs信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            #region CommandText

            string commandText = @"UPDATE CPs SET Status = 3 WHERE CPID = @CPID";

            #endregion

            return ExecuteNonQuery(commandText, new CPsEntity() { CPID = ID });
        }
        /// <summary>
        /// CPs列表信息
        /// </summary>
        /// <returns></returns>
        public List<CPsEntity> Select()
        {
            #region CommandText

            string commandText = @"select * from CPs WHERE Status !=3";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<CPsEntity>() as List<CPsEntity>;
            }
        }
        /// <summary>
        /// 查询单个CPs信息
        /// </summary>
        /// <param name="CPID"></param>
        /// <returns></returns>
        public CPsEntity SelectByNo(int CPID)
        {
            #region CommandText

            string commandText = @"select * from CPs WHERE Status !=3 and CPID = " + CPID;

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToModel<CPsEntity>() as CPsEntity;
            }
        }
    }
}
