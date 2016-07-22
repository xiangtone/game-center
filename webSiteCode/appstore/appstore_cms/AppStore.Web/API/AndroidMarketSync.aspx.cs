using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

//using AppStore.BLL.Redis;
using AppStore.BLL;
using AppStore.Common;

namespace AppStore.Web.API
{
    public partial class AndroidMarketSync : BasePage
    {

        public string From { get { return this.Request<string>("from"); } }

        public string Token { get { return this.Request<string>("token"); } }

        public string Key { get { return  Extensions.AppSettings<string>("AndroidMarketKey", "99caddc82f999796b4f2b5b244fb4e63"); } }


        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                string md5 = SecurityExtension.MD5(string.Format("{0}:{1}", this.From, this.Key));

                if (md5.Equals(this.Token))
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
                    LogHelper.Default.Info(string.Format("安卓市场通知接口：{0}:{1}", this.Form, this.Token));

                    Response.Write("Result:非法参数");
                }
            }
        }


        /// <summary>
        /// 调用各回调接口
        /// </summary>
        private bool ExecuteSyncInterface()
        {
            bool result = false;

            //调用同步开发者信息接口
            //result = new SyncManagerBLL().DeveloperSync();

            //调用Redis缓存
            result = new SyncManagerBLL().RedisSync();

            // todo: 调用实时生效接口
            result = new SyncManagerBLL().EffectiveSync();

            return result;
        }

    }
}