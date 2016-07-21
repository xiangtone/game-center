using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.BLL;
using AppStore.Common;

namespace AppStore.Web.API
{
    public partial class SyncCache : System.Web.UI.Page
    {
        public string From { get { return this.Request<string>("from"); } }

        public string Token { get { return this.Request<string>("token"); } }

        public string Key { get { return Extensions.AppSettings<string>("SyncCacheKey", "37a967f5236352bf9f20bc740261f53e"); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                string md5 = SecurityExtension.MD5(string.Format("{0}:{1}", this.From, this.Key));

                if (md5.ToLower().Equals(this.Token.ToLower()))
                {
                    if (this.ExecuteSyncInterface())
                    {
                        Response.Write("Result:Success");
                    }
                    else
                    {
                        Response.Write("Result:Fail");
                    }
                }
                else
                {
                    LogHelper.Default.Info(string.Format("缓存同步通知接口：{0}:{1}", this.Form, this.Token));

                    Response.Write("Result:非法参数");
                }
            }
        }

        private bool ExecuteSyncInterface()
        {
            bool result = new SyncManagerBLL().NewRedis();
            if (result)
            {
                result = new SyncManagerBLL().EffectiveSync();
                return result;
            }
            return result;

        }
    }
}