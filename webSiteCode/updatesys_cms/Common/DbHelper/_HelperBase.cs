using System;
using System.Data;
using System.Configuration;

namespace Common.DbHelper
{
    public abstract class _HelperBase : IDbHelper
    {
        internal string ConStr;
        /// <summary>
        /// 初始化
        /// </summary>
        /// <param name="connectionString">连接字符串</param>
        internal virtual void Initial(string connectionString)
        {
            if (string.IsNullOrEmpty(connectionString))
            {
                ConStr = ConfigurationManager.ConnectionStrings["DbName"].ConnectionString;
            }
            else
                ConStr = connectionString;
        }


        /// <summary>
        /// 准备数据库查询参数
        /// </summary>
        /// <param name="cn"></param>
        /// <param name="cmd"></param>
        /// <param name="commandType"></param>
        /// <param name="commandText"></param>
        /// <param name="cmdParams"></param>
        internal  virtual void PrepareCommand(IDbConnection cn, IDbCommand cmd, CommandType commandType, string commandText, IDataParameter[] cmdParams)
        {
            if (cmd == null) return;
            cmd.Connection = cn;
            cmd.CommandText = commandText;
            cmd.CommandType = commandType;

            if (cmdParams != null)
            {
                foreach (IDataParameter parm in cmdParams)
                    cmd.Parameters.Add(parm);
            }
        }


        #region IDbHelper 成员

        public abstract int ExecuteNonQuery(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        public abstract object ExecuteScalar(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        public abstract IDataReader ExecuteReader(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        public abstract DataTable GetDataTable(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        #endregion
    }
}
