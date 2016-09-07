using System;
using System.Data;
using System.Configuration;
using System.Data.OleDb;

namespace Common.DbHelper
{
    public class OleDbHelper:_HelperBase
    {
        

        public OleDbHelper(string connectionString)
        {
            Initial(connectionString);
        }
        private System.Data.IDbConnection GetConnection()
        {
            return new OleDbConnection(ConStr);
        }

        #region IDbHelper 成员



        public override int ExecuteNonQuery(System.Data.CommandType cmdType, string cmdText, params System.Data.IDataParameter[] commandParameters)
        {
            using (IDbConnection cn = GetConnection())
            {
                try
                {
                    cn.Open();
                    IDbCommand cmd = new OleDbCommand();
                    PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                    int val = cmd.ExecuteNonQuery();
                    return val;
                }
                finally
                {
                    cn.Close();
                }
            }
        }

        public override object ExecuteScalar(System.Data.CommandType cmdType, string cmdText, params System.Data.IDataParameter[] commandParameters)
        {
            using (IDbConnection cn = GetConnection())
            {
                try
                {
                    cn.Open();
                    IDbCommand cmd = new OleDbCommand();
                    PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                    object val = cmd.ExecuteScalar();
                    return val;
                }
                finally
                {
                    cn.Close();
                }
            }
        }

        public override System.Data.IDataReader ExecuteReader(System.Data.CommandType cmdType, string cmdText, params System.Data.IDataParameter[] commandParameters)
        {
            IDbConnection cn = null;
            try
            {
                IDbCommand cmd = new OleDbCommand();
                cn = GetConnection();
                cn.Open();

                PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                IDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                return rdr;
            }
            catch (Exception ex)
            { 
                if(cn != null)
                    cn.Close();
                throw;
            }
        }

        public override System.Data.DataTable GetDataTable(System.Data.CommandType cmdType, string cmdText, params System.Data.IDataParameter[] commandParameters)
        {
            using (IDbConnection cn = GetConnection())
            {
                try
                {
                    OleDbCommand cmd = new OleDbCommand();
                    cn.Open();

                    PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                    OleDbDataAdapter adp = new OleDbDataAdapter(cmd);
                    DataTable result = new DataTable();
                    adp.Fill(result);
                    return result;
                }
                finally
                {
                    cn.Close();
                }
            }
        }

        #endregion

        #region 静态方法

        /// <summary>
        /// 传入返回值参数
        /// </summary>
        /// <param name="ParamName">存储过程名称</param>
        /// <param name="DbType">参数类型</param>
        /// <param name="Size">参数大小</param>
        /// <returns>新的 parameter 对象</returns>
        public static OleDbParameter MakeOutParam(string ParamName, OleDbType DbType, int Size)
        {
            #region
            return MakeParam(ParamName, DbType, Size, ParameterDirection.Output, null);
            #endregion
        }

        /// <summary>
        /// 生成存储过程参数
        /// </summary>
        /// <param name="ParamName">存储过程名称</param>
        /// <param name="DbType">参数类型</param>
        /// <param name="Size">参数大小</param>
        /// <param name="Direction">参数方向</param>
        /// <param name="Value">参数值</param>
        /// <returns>新的 parameter 对象</returns>
        public static OleDbParameter MakeParam(string ParamName, OleDbType DbType, int Size, ParameterDirection Direction, object Value)
        {
            OleDbParameter param;
            #region

            if (Size > 0)
                param = new OleDbParameter(ParamName, DbType, Size);
            else
                param = new OleDbParameter(ParamName, DbType);

            param.Direction = Direction;
            if (!(Direction == ParameterDirection.Output && Value == null))
                param.Value = Value;

            return param;
            #endregion
        }

        /// <summary>
        /// 生成输入Parameter
        /// </summary>
        public static OleDbParameter MakeInParam(string ParamName, OleDbType DbType, int Size, object Value)
        {
            OleDbParameter param;
            if (Size < 1)
                param = new OleDbParameter(ParamName, DbType);
            else
                param = new OleDbParameter(ParamName, DbType, Size);

            if (DbType == OleDbType.Char || DbType == OleDbType.VarChar || DbType == OleDbType.VarWChar || DbType == OleDbType.WChar
                || DbType == OleDbType.LongVarChar || DbType == OleDbType.LongVarWChar)
            {
                if (Value == null)
                {
                    param.IsNullable = true;
                    param.Value = DBNull.Value;
                    return param;
                }
            }

            param.Value = Value;
            return param;
        }

        /// <summary>
        /// 获取Sql查询结果（DataReader）
        /// </summary>
        public static string GetSqlResult(IDataReader rdr, int index, string defaultVal)
        {
            if (rdr == null || rdr.IsClosed == true)
                return null;
            if (rdr.IsDBNull(index))
            {
                return defaultVal;
            }
            else
            {
                return rdr.GetString(index);
            }
        }

        /// <summary>
        /// 获取Sql查询结果（DataReader）
        /// </summary>
        public static int GetSqlResult(IDataReader rdr, int index, int defaultVal)
        {
            if (rdr == null || rdr.IsClosed == true)
                return -1;
            if (rdr.IsDBNull(index))
            {
                return defaultVal;
            }
            else
            {
                return rdr.GetInt32(index);
            }
        }

        /// <summary>
        /// 获取Sql查询结果（DataReader）
        /// </summary>
        public static DateTime GetSqlResult(IDataReader rdr, int index, DateTime defaultVal)
        {
            if (rdr == null || rdr.IsClosed == true)
                return new DateTime(1900, 1, 1);
            if (rdr.IsDBNull(index))
            {
                return defaultVal;
            }
            else
            {
                return rdr.GetDateTime(index);
            }
        }

        /// <summary>
        /// 获取Sql查询结果
        /// </summary>
        public static bool GetSqlResult(IDataReader rdr, int index, bool defaultVal)
        {
            if (rdr == null || rdr.IsClosed == true)
                return defaultVal;
            if (rdr.IsDBNull(index))
            {
                return defaultVal;
            }
            return rdr.GetBoolean(index);
        }

        /// <summary>
        /// 获取Sql查询结果
        /// </summary>
        public static object GetSqlResult(IDataReader rdr, int index)
        {
            if (rdr == null || rdr.IsClosed == true)
                return null;
            return rdr[index];
        }
        #endregion
    }
}
