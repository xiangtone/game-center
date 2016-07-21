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
     public class AppInformDAL :BaseDAL
    {

        /// <summary>
        /// 列表信息
        /// </summary>
        /// <returns></returns>
         public List<AppInformEntity> GetAppinformList()
        {
            #region CommandText

            string commandText = @"select * from appinform";

            #endregion
            //MySqlHelper.ExecuteNonQuery(ConnectionString, "SET NAMES utf8mb4; ");

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {

                return objReader.ReaderToList<AppInformEntity>() as List<AppInformEntity>;
            }

        }

        public int UpdateRemarks(int id, string r)
        {
            #region CommandText

            string commandText = @"update appinform set Remarks ='" + r + "',Status =2 where informId = " + id;

            #endregion
            return MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText);
        }
    }
}
