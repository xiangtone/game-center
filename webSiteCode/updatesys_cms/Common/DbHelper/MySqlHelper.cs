using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;

using MySql.Data.MySqlClient;
using MySql.Data.Types;


namespace Common.DbHelper
{
    public class MySqlHelper : _HelperBase
    {

        public MySqlHelper(string connectionString)
        {
            Initial(connectionString);
        }
        private System.Data.IDbConnection GetConnection()
        {
            return new MySqlConnection(ConStr);
        }

        #region IDbHelper 成员




        public override int ExecuteNonQuery(System.Data.CommandType cmdType, string cmdText, params System.Data.IDataParameter[] commandParameters)
        {
            using (IDbConnection cn = GetConnection())
            {
                try
                {
                    cn.Open();
                    IDbCommand cmd = new MySqlCommand();
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
                    IDbCommand cmd = new MySqlCommand();
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
                IDbCommand cmd = new MySqlCommand();
                cn = GetConnection();
                cn.Open();

                PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                IDataReader rdr = cmd.ExecuteReader(CommandBehavior.CloseConnection);
                return rdr;
            }
            catch (Exception ex)
            {
                if (cn != null)
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
                    MySqlCommand cmd = new MySqlCommand();
                    cn.Open();

                    PrepareCommand(cn, cmd, cmdType, cmdText, commandParameters);
                    MySqlDataAdapter adp = new MySqlDataAdapter(cmd);
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
        public static MySqlParameter MakeOutParam(string ParamName, MySqlDbType DbType, int Size)
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
        public static MySqlParameter MakeParam(string ParamName, MySqlDbType DbType, Int32 Size, ParameterDirection Direction, object Value)
        {
            MySqlParameter param;
            #region

            if (Size > 0)
                param = new MySqlParameter(ParamName, DbType, Size);
            else
                param = new MySqlParameter(ParamName, DbType);

            param.Direction = Direction;
            if (!(Direction == ParameterDirection.Output && Value == null))
                param.Value = Value;

            return param;
            #endregion
        }

        /// <summary>
        /// 生成输入Parameter
        /// </summary>
        public static MySqlParameter MakeInParam(string ParamName, MySqlDbType DbType, int Size, object Value)
        {
            MySqlParameter param;
            if (Size < 1)
                param = new MySqlParameter(ParamName, DbType);
            else
                param = new MySqlParameter(ParamName, DbType, Size);

            if (DbType == MySqlDbType.VarChar || DbType == MySqlDbType.Text || DbType == MySqlDbType.String || DbType == MySqlDbType.VarString)
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

        /// <summary>
        /// 生成分页Sql语句（SQL2005及以上专用）
        /// </summary>
        /// <param name="sqlString">数据查询语句（必须含有from,order子句）</param>
        /// <returns></returns>
        public static string GetDbPager(string sqlString, int pageSize, int curPage)
        {
            sqlString = sqlString.ToLower();
            pageSize = pageSize > 0 ? pageSize : 10;
            curPage = curPage > 0 ? curPage : 1;

            int fromIndex = sqlString.IndexOf("from");
            if (fromIndex < 0) return string.Empty;
            int whereIndex = sqlString.IndexOf("where");
            int orderIndex = sqlString.LastIndexOf("order by");
            if (orderIndex < 0) return string.Empty;
            string fields = sqlString.Substring(7, fromIndex - 8);
            string tables = string.Empty;
            if (whereIndex < 0)
            {
                //无where子句
                tables = sqlString.Substring(fromIndex + 5, orderIndex - fromIndex - 5);
            }
            else
            {
                //有where子句
                //whereIndex = whereIndex < orderIndex ? whereIndex : orderIndex;
                tables = sqlString.Substring(fromIndex + 5, whereIndex - fromIndex - 5);
            }
            string whereString = string.Empty;
            if (whereIndex > 0)
            {
                if (orderIndex > 0)
                {
                    whereString = sqlString.Substring(whereIndex, orderIndex - whereIndex);
                }
                else
                    whereString = sqlString.Substring(whereIndex);
            }
            string orderString = sqlString.Substring(orderIndex);


            string result = string.Format(@"with tmptb as(select {0},Row_Number() over({3}) as [RowNumber] from {1} {2})
                                select top({4}) {0} from tmptb where [RowNumber]>{4}*({5}-1);select count(*) from {1} {2};", fields, tables, whereString, orderString, pageSize, curPage);

            return result;
        }
    }
}
