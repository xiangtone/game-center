using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Configuration;

namespace updatesys_cms.DAL
{
    public class Base
    {
        protected Common.DbHelper.MySqlHelper _UpdaterHelper;
        protected Common.DbHelper.MySqlHelper _AppLibHelper;

        public Base()
        {
            _UpdaterHelper = new Common.DbHelper.MySqlHelper(ConfigurationManager.ConnectionStrings["Updater_DB"].ConnectionString);
            _AppLibHelper = new Common.DbHelper.MySqlHelper(ConfigurationManager.ConnectionStrings["AppLib_DB"].ConnectionString);
        }
    }

}
