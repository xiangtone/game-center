using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

using nwbase_utils;
//using nwbase_utils.DbHelper;
using MySql.Data.MySqlClient;



namespace AppStore.DAL
{
    public class BaseDAL
    {
        public string ConnectionString { get { return Tools.GetConnStrConfig("ConnectionString"); } }

        public bool ExecuteStatus(int result)
        {
            if (result > 0)
            {
                return true;
            }
            else
            {
                return false;
            }
        }


    }
}
