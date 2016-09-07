using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace updatesys_cms.Web
{
    public partial class TestScheme : System.Web.UI.Page
    {
        private nwbase_utils.Cache.RedisHelper _RedisHandler = null;
        protected void Page_Load(object sender, EventArgs e)
        {
            string host = nwbase_utils.Tools.GetAppSetting("Redis_Host", string.Empty);
            int port = nwbase_utils.Tools.GetAppSetting("Redis_Port", 0);
            int db = nwbase_utils.Tools.GetAppSetting("Redis_Updater_Db", 0);
            _RedisHandler = new nwbase_utils.Cache.RedisHelper(host, port, db);

            if (!IsPostBack)
            {
                Bind();
            }
        }

        private void Bind()
        {
            var IMEIList = _RedisHandler.SMembers("test_scheme");
            rpResultList.DataSource = IMEIList;
            rpResultList.DataBind();
        }

        protected void OnAdd(object sender, EventArgs e)
        {
            var newImgi = txtIMEI.Text;
            _RedisHandler.SAdd("test_scheme", newImgi);
            txtIMEI.Text = "";
            Bind();
        }


        protected void OnDel(object s, CommandEventArgs e)
        {
            var imei = e.CommandArgument.ToString();
            _RedisHandler.SRemove("test_scheme", imei);
            Bind();
        }
    }
}