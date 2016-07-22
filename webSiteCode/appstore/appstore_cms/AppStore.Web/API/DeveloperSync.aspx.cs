using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using System.Net;
using System.Text;
using AppStore.BLL;

namespace AppStore.Web.API
{
    public partial class DeveloperSync : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                int syncType = this.Request<int>("type", 0);

                switch (syncType)
                {
                    case 0:
                        break;
                    case 1:
                        //  SyncDeveloperData();
                        break;
                    case 2:
                        //   SyncDeveloperData();
                        break;
                    case 3:
                        //  SyncDeveloperData();
                        break;
                    case 4:
                        //  SyncDeveloperData();
                        break;
                    case 5:
                        //  SyncDeveloperData();
                        break;
                    case 6:
                        //  SyncDeveloperData();
                        break;
                    case 7:
                        SyncDeveloperData();
                        break;
                }
            }
        }

        public void SyncDeveloperData()
        {
            try
            {
                bool result = new SyncManagerBLL().DeveloperSync();

                Response.Write(string.Format("Result:{0}", result));
            }
            catch (Exception ex)
            {
                Response.Write("Result:false " + ex.ToString());
            }
        }
    }
}