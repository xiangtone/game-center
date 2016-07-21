using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class AutoCollectList : BasePage
    {
        public List<AppInfoEntity> list;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {

                if (Request.QueryString["action"] == "del")
                {
                    int id = Convert.ToInt32(Request.QueryString["id"]);
                    if (id > 0)
                    {
                        AppInfoEntity info = new AppInfoEntity()
                        {
                            AppID = id,
                            UpdateTime = DateTime.Now,
                            Status = 98
                        };
                        new AppInfoBLL().DeleteByID(info);
                        BindGridView();
                    }
                }
                else { BindGridView(); }
            }
        }



        private void BindGridView()
        {
            list = new AppInfoBLL().SelectListByStatus(99);

        }

        //protected void GridView1_RowCommand(object sender, GridViewCommandEventArgs e)
        //{
        //    if (e.CommandName == "Delete")
        //    {
        //        nwbase_utils.TextLog.Default.Info(e.CommandArgument.ToString());
        //        AppInfoEntity info = new AppInfoEntity()
        //        {
        //            AppID = Convert.ToInt32(e.CommandArgument.ToString()), UpdateTime=DateTime.Now, Status=98
        //        };
        //        new AppInfoBLL().DeleteByID(info);
        //    }
        //    BindGridView();
        //}


    }
}