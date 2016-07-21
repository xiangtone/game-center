using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;

namespace AppStore.Web
{
    public partial class PackInfoList : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public string ShowName { get { return this.Request<string>("ShowName", string.Empty); } }
        public string Type { get { return this.Request<string>("type", "应用管理"); } }
        public int PackID { get { return this.Request<int>("PackID", 0); } }
        public string PageType { get { return this.Request<string>("page", ""); } }


        protected void Page_Load(object sender, EventArgs e)
        {

            if (!IsPostBack)
            {
                string ac = Request.QueryString["ac"];
                if (ac == "permission")
                {
                    int id = Int32.Parse(Request.QueryString["id"]);
                    if (id > 0)
                    {
                        string permission = "";
                        PackInfoEntity info = new PackInfoBLL().GetSingle(id);
                        if (info != null)
                        {
                            permission = GetAndroidPermission(info.permission);
                        }
                        Response.Write(permission);
                        Response.End();
                    }
                }
                else if (ac == "updatever")
                {
                    PackInfoEntity entity = new PackInfoBLL().GetSingle(PackID);
                    if (new PackInfoBLL().UpdateMainVer(entity))
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = entity.AppID,
                            reason = "",
                            Status = 1,
                            OperateFlag = "7",
                            OperateType = "2",
                            OperateExplain = "设置主版本",
                            SourcePage =1,
                            OperateContent = entity.ShowName + "(" + entity.PackName + ")",
                            UserName = GetUserName(),
                        };
                        new OperateRecordBLL().Insert(info);
                        Response.Write("主版本设置成功");
                        Response.End();
                    }
                    else
                    {
                        Response.Write("主版本设置失败");
                        Response.End();
                    }
                }
                //new PackInfoBLL().AndroidPermission("");
                else
                {
                    if (this.PackID != 0)
                    {
                        if (new PackInfoBLL().Delete(this.PackID))
                        {
                            int packcount = new AppInfoBLL().GetPacksCount(AppID);
                            AppInfoEntity info = new AppInfoEntity() { AppID = AppID, PackCount = packcount };
                            bool ruslt = new AppInfoBLL().UpdatePackCount(info);
                            this.Alert("删除成功");
                        }
                        else
                        {
                            this.Alert("删除失败");
                        }
                    }
                }
                this.BindData();
            }
        }

        public void BindData()
        {
            PackInfoEntity entity = new PackInfoEntity() { AppID = this.AppID };

            if (entity.AppID.Equals(0))
            {
                this.Alert("参数非法！");
                return;
            }



            List<PackInfoEntity> list = new PackInfoBLL().GetDataList(entity);

            objRepeater.DataSource = list;
            objRepeater.DataBind();
        }

        public string BindPermission(string per, object id)
        {
            string str = "";
            //GetAndroidPermission(Eval("permission").ToString()).Length>11) ? GetAndroidPermission(Eval("permission").ToString()).Substring(0,10).Insert(10,"<span class='selpermission' data-id='"+Eval("PackID")+"' onclick='selper("+Eval("PackID")+");'>  ...查看更多</span>"):GetAndroidPermission(Eval("permission").ToString()
            if (per.Trim().Length > 0)
            {
                string perstr = GetAndroidPermission(per);

                if (perstr.IndexOf(",") > -1)
                {
                    string perstr2 = perstr.Substring(perstr.IndexOf(',') + 1, perstr.Length - (perstr.IndexOf(',') + 1));
                    str += perstr.Substring(0, perstr.IndexOf(',')) + "<br/>";
                    str += perstr2.Substring(0, perstr2.IndexOf(',')) + "<br/>";
                }
                else
                {
                    str = perstr;
                }
                str += "<span class='selpermission' data-id='" + Int32.Parse(id.ToString()) + "' onclick='selper(" + Int32.Parse(id.ToString()) + ");'> 查看更多</span>";
            }
            return str;

        }

        public string BindVer(int IsMainVer, int packid, int appid)
        {
            string str = "";
            if (IsMainVer != 1)
            {
                str += "<br /><a onclick=updatever('"+ShowName+"',"+packid+","+appid+");>设为主版本</a>";
            }
            return str;
        }
    }
}