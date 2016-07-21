using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using System.Runtime.Serialization;
using AppStore.Common;
using AppStore.BLL;

namespace AppStore.Web
{
    public partial class SyncEffective : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            this.ExtcuteEffective();
        }

        public void ExtcuteEffective()
        {
            try
            {
                string action = this.Request["action"];

                if (string.IsNullOrEmpty(action) || !action.Equals("effective"))
                {
                    this.Alert("SyncManager", "参数非法，同步数据失败，请联系管理员");
                    return;
                }

                bool result = new SyncManagerBLL().EffectiveSync();

                if (result.Equals(true))
                {
                    this.Alert("实时同步成功");
                }
                else
                {
                    this.Alert("实时同步失败，请联系管理员");
                }

            }
            catch
            {
                //XSS攻击？
                this.Alert("SyncManager", "操作失败，发生未知错误");
            }
        }
    }
}