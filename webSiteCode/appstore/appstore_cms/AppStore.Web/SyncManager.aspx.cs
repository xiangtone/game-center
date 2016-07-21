using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
//using AppStore.BLL.Redis;
using AppStore.BLL;
using AppStore.Common;


namespace AppStore.Web
{
    public partial class SyncManager : BasePage 
    {
        public string PageType { get { return this.Request<string>("page", ""); } }
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        /// <summary>
        /// 同步缓存
        /// </summary>
        /// <param name="s"></param>
        /// <param name="e"></param>
        protected void OnRsyncStart(object s, EventArgs e)
        {

            bool result = new SyncManagerBLL().NewRedis();

            if (result.Equals(true))
            {
                string msg = "缓存同步成功";
                result = new SyncManagerBLL().EffectiveSync();
                if (!result)
                {
                    msg += "，但通知失败";
                }
                this.Alert(msg);
            }
            else
            {
                this.Alert("缓存同步失败");
            }

        }

        /// <summary>
        /// 立即生效
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void SyncEffective_Click(object sender, EventArgs e)
        {
            Response.Redirect("SyncEffective.aspx?action=effective");
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            bool result = new SyncManagerBLL().NewRedis();

            if (result.Equals(true))
            {
                this.Alert("缓存同步成功");
            }
            else
            {
                this.Alert("缓存同步失败");
            }
        }
    }
}