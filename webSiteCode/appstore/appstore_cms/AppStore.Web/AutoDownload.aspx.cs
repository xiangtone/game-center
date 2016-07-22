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
    public partial class AutoDownload : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (nwbase_utils.Tools.GetRequestVal("action", "") == "download")
            {
                string url = "";
                string id = nwbase_utils.Tools.GetRequestVal("id", "");
                if (id.IndexOf(',') > -1)
                {
                    string[] ids = id.Split(',');
                    if (ids.Length > 0)
                    {
                        foreach (var i in ids)
                        {
                            PackInfoEntity entity = new PackInfoEntity() { AppID = Convert.ToInt32(i) };
                            List<PackInfoEntity> list = new PackInfoBLL().GetDataList(entity);
                            foreach (PackInfoEntity item in list)
                            {
                                if (item.IsMainVer == 1 && item.Status == 1)
                                {
                                    if (url == "")
                                    {
                                        //url = item.AppID + item.ShowName;
                                        url = item.PackUrl;
                                    }
                                    else
                                    {
                                        //url = url + "," + item.AppID + item.ShowName;
                                        url = url + "," + item.PackUrl;
                                    }
                                }
                            }
                        }
                    }
                    Response.Write(url);
                    Response.End();
                }

            }
        }
    }
}