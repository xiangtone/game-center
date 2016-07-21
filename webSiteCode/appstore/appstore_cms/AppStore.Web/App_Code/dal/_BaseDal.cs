using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using MySql.Data;
using MySql.Data.Common;
using MySql.Data.Types;
using MySql.Data.MySqlClient;
using nwbase_utils;



namespace AppStore.Web
{
    public class BaseDal
    {
        public BaseDal()
        {
        }

        public string _connStr = Tools.GetConnStrConfig("cmsbase");
    }
}