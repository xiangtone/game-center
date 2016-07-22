using MySql.Data.MySqlClient;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Common;

namespace AppStore.DAL
{
    public class B_DevDAL : BaseDAL
    {
        public List<CPsEntity> GetDataListByPager(int startIndex, int endIndex, string CPName)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            #region CommandText

            string commandText = @"SELECT
                                          CPID,
                                          CPType,
                                          IsDeveloper,
                                          FullName,
                                          CPName,
                                          Remarks,
                                          CreateTime,
                                          UpdateTime,
                                          Status
                                        FROM CPs  where Status=1 ";

            if (!string.IsNullOrEmpty(CPName))
            {
                commandText += " and CPName like @CPName ";
                paramsList.Add(new MySqlParameter("@CPName", string.Format("%{0}%", CPName)));
            }
            commandText += "  Order By UpdateTime desc LIMIT @startIndex, @endIndex ";

            #endregion


            paramsList.Add(new MySqlParameter("@startIndex", startIndex));
            paramsList.Add(new MySqlParameter("@endIndex", endIndex));

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(base.ConnectionString, commandText, paramsList.ToArray()))
            {
                return objReader.ReaderToList<CPsEntity>() as List<CPsEntity>;
            }

        }

        public List<CPsEntity> GetDevList()
        {
            string commandText = @"SELECT CPID, CPType, IsDeveloper, CPName, FullName, Remarks, CreateTime, UpdateTime, Status FROM CPs";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<CPsEntity>() as List<CPsEntity>;
            }
        }

        public List<CPsEntity> GetDevIDByName(string CPName)
        {
            string commandText = @"SELECT CPID, CPType, IsDeveloper, CPName, FullName, Remarks, CreateTime, UpdateTime, Status FROM CPs Where CPName like @CPName";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@CPName", string.Format("%{0}%", CPName))))
            {
                return objReader.ReaderToList<CPsEntity>() as List<CPsEntity>;
            }
        }

        public int GetTotalCount(string CPName)
        {
            string commandText = @"SELECT COUNT(*) FROM CPs WHERE 1=1 and Status=1  ";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            if (!string.IsNullOrEmpty(CPName))
            {
                commandText += " and CPName like @CPName";
                paramsList.Add(new MySqlParameter("@CPName", string.Format("%{0}%", CPName)));
            }


            int result = MySqlHelper.ExecuteScalar(base.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result;
        }


        public CPsEntity GetLastSingleData()
        {
            string commandText = @"SELECT CPID, CPType, IsDeveloper, CPName, FullName, Remarks, CreateTime, UpdateTime, Status FROM CPs ORDER BY UpdateTime DESC  LIMIT 0,1";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToModel<CPsEntity>() as CPsEntity;
            }
        }

        public List<CPsEntity> GetParmaryKey()
        {
            string commandText = @"SELECT CPID FROM CPs";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<CPsEntity>() as List<CPsEntity>;
            }
        }

        public bool Insert(CPsEntity currentEntity)
        {
            string commandText = @"INSERT INTO  CPs (
                                                CPID,
                                                CPType,
                                                IsDeveloper,
                                                FullName,
                                                CPName,
                                                Remarks,
                                                CreateTime,
                                                UpdateTime,
                                                Status)
                                        VALUES (@CPID,
                                               @CPType,
                                               @IsDeveloper,
                                               @FullName,
                                               @Remarks,
                                               @CreateTime,
                                               @UpdateTime,
                                               @Status);";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@CPID", currentEntity.CPID));
            paramsList.Add(new MySqlParameter("@CPType", currentEntity.CPType));
            paramsList.Add(new MySqlParameter("@IsDeveloper", currentEntity.IsDeveloper));
            paramsList.Add(new MySqlParameter("@CPName", currentEntity.CPName));
            paramsList.Add(new MySqlParameter("@FullName", currentEntity.FullName));
            paramsList.Add(new MySqlParameter("@Remarks", currentEntity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", currentEntity.CreateTime));
            paramsList.Add(new MySqlParameter("@UpdateTime", currentEntity.UpdateTime));
            paramsList.Add(new MySqlParameter("@Status", currentEntity.Status));

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return this.ExecuteStatus(result);
        }


        /// <summary>
        /// 更新开发者数据
        /// </summary>
        /// <param name="currentEntity"></param>
        /// <returns></returns>
        public bool Update(CPsEntity currentEntity)
        {
            string commandText = @"UPDATE CPs
                                            SET 
                                              CPType = @CPType
                                              IsDeveloper = @IsDeveloper
                                              CPName = @CPName,
                                              FullName = @FullName,
                                              Remarks = @Remarks,
                                              CreateTime = @CreateTime,
                                              UpdateTime = @UpdateTime,
                                              Status = @Status
                                            WHERE CPID = @CPID;";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@CPID", currentEntity.CPID));
            paramsList.Add(new MySqlParameter("@CPType", currentEntity.CPType));
            paramsList.Add(new MySqlParameter("@IsDeveloper", currentEntity.IsDeveloper));
            paramsList.Add(new MySqlParameter("@CPName", currentEntity.CPName));
            paramsList.Add(new MySqlParameter("@FullName", currentEntity.FullName));
            paramsList.Add(new MySqlParameter("@Remarks", currentEntity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", currentEntity.CreateTime));
            paramsList.Add(new MySqlParameter("@UpdateTime", currentEntity.UpdateTime));
            paramsList.Add(new MySqlParameter("@Status", currentEntity.Status));

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return this.ExecuteStatus(result);
        }
    }
}
