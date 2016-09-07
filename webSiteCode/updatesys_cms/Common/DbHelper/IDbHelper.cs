using System;
using System.Data;

namespace Common.DbHelper
{
    public interface IDbHelper
    {

        /// <summary>
        /// 执行无返回值SQL语句
        /// </summary>
        /// <param name="cmdType">命令类型</param>
        /// <param name="cmdText">命令文本</param>
        /// <param name="commandParameters">命令参数</param>
        int ExecuteNonQuery(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        /// <summary>
        /// 执行单返回值SQL语句
        /// </summary>
        /// <param name="cmdType">命令类型</param>
        /// <param name="cmdText">命令文本</param>
        /// <param name="commandParameters">命令参数</param>
        /// <returns>返回命令执行后第一行第一列的数据值</returns>
        object ExecuteScalar(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        /// <summary>
        /// 执行返回DataReader 的SQL语句
        /// </summary>
        /// <param name="cmdType">命令类型</param>
        /// <param name="cmdText">命令文本</param>
        /// <param name="commandParameters">命令参数</param>
        /// <returns>返回命令执行后所读取到的批量数据值</returns>
        IDataReader ExecuteReader(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);

        /// <summary>
        /// 执行返回DataTable 的SQL语句
        /// </summary>
        /// <param name="cmdType">命令类型</param>
        /// <param name="cmdText">命令文本</param>
        /// <param name="commandParameters">命令参数</param>
        /// <returns>返回命令执行后所读取到的批量数据值</returns>
        DataTable GetDataTable(CommandType cmdType, string cmdText, params IDataParameter[] commandParameters);
    }
}
