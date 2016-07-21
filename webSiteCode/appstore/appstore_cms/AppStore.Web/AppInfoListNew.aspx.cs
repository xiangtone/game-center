using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using System.IO;
using System.Text;
using System.Runtime.Serialization.Json;
using System.Text.RegularExpressions;
using System.Web.Script.Serialization;
using System.Data;

using System.Windows.Forms;
using System.Reflection;
using System.Web.UI.HtmlControls;
using System.Threading;
using System.Collections;


namespace AppStore.Web
{
    public partial class AppInfoListNew : BasePage
    {
        public List<AppInfoEntity> CurrentList { get; set; }
        public Dictionary<int, string> dic_DevList { get; set; }
        public int AppID { get { return this.Request<int>("AppID", 0); } }

        /// <summary>
        /// 1=已上架；2=待审核；3=接入中；4=已下架；5=审核未通过
        /// </summary>
        public int actype { get { return this.Request<int>("Action", 1); } }

        public List<AppInfoEntity> excelList = new List<AppInfoEntity>();

        public DataSet dataset = new DataSet();
        public DataTable datatable = new DataTable();

        public int Count = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {

                string ac = Request.QueryString["ac"];
                if (ac == "getrecord")
                {
                    int id = Int32.Parse(Request.QueryString["id"]);
                    if (id > 0)
                    {
                        List<OperateRecordEntity> list = new OperateRecordBLL().GetListByAppId(id);
                        var list2 = list.Select(a => new { UserName = a.UserName, OperateExplain = a.OperateExplain, OperateTime = a.OperateTime.ToString("yyyy-MM-dd HH:mm:ss"), reason = a.reason }).ToList();
                        //javascript序列化器
                        JavaScriptSerializer jss = new JavaScriptSerializer();
                        //序列化学生集合对象得到json字符
                        string strJson = jss.Serialize(list2);
                        Response.Write(strJson);
                        Response.End();
                    }
                }
                else if (ac == "isexist")
                {
                    int id = Int32.Parse(Request.QueryString["id"]);
                    if (id > 0)
                    {
                        if (new AppInfoBLL().IsExistGroupElems(id))
                        {
                            Response.Write("1");
                            Response.End();
                        }
                        else
                        {
                            Response.Write("0");
                            Response.End();
                        }
                    }
                }
                else if (ac == "update")
                {
                    int actype2 = int.Parse(Request.QueryString["actype"]);
                    int status = int.Parse(Request.QueryString["status"]);
                    int id = int.Parse(Request.QueryString["id"]);
                    AppInfoEntity entity = new AppInfoBLL().GetSingle(id);
                    if (entity == null)
                    {
                        entity.ShowName = "";
                        entity.PackName = "";
                    }
                    if (status == 1)
                    {
                        string th = Request.QueryString["type"];
                        if (new AppInfoBLL().UpdateStatus(id, status))
                        {
                            OperateRecordEntity info = new OperateRecordEntity();
                            if (th == "th")
                            {
                                info.OperateType = "3";
                                info.OperateFlag = "1";
                                info.OperateExplain = "游戏审核通过";
                                info.OperateContent = entity.ShowName + "(" + entity.PackName + ")";
                                info.reason = "";
                                info.ElemId = id;
                                info.SourcePage = actype2;
                                info.Status = 1;
                                info.UserName = GetUserName();
                            }
                            else
                            {
                                info.OperateType = "2";
                                info.OperateFlag = "6";
                                info.OperateExplain = "重新上架架游戏";
                                info.OperateContent = entity.ShowName + "(" + entity.PackName + ")";
                                info.reason = "";
                                info.ElemId = id;
                                info.SourcePage = actype2;
                                info.Status = 1;
                                info.UserName = GetUserName();
                            }
                            new OperateRecordBLL().Insert(info);
                        }
                    }
                    else if (status == 2)
                    {
                        string reason = Request.QueryString["reason"];
                        string th = Request.QueryString["type"];
                        if (th == "th")
                        {
                            status = 7;
                            if (new AppInfoBLL().UpdateStatus(id, status))
                            {
                                OperateRecordEntity info = new OperateRecordEntity();
                                info.OperateType = "3";
                                info.OperateFlag = "2";
                                info.OperateExplain = "游戏审核不通过";
                                info.OperateContent = entity.ShowName + "(" + entity.PackName + ")";
                                info.reason = reason;
                                info.ElemId = id;
                                info.SourcePage = actype2;
                                info.Status = 1;
                                info.UserName = GetUserName();
                                new OperateRecordBLL().Insert(info);
                            }
                        }
                        else
                        {
                            status = 2;
                            if (new AppInfoBLL().IsExistGroupElems(id))
                            {
                                new AppInfoBLL().DelGroupElems(id);
                            }
                            if (new AppInfoBLL().UpdateStatus(id, status))
                            {
                                OperateRecordEntity info = new OperateRecordEntity();
                                info.OperateType = "2";
                                info.OperateFlag = "5";
                                info.OperateExplain = "下架游戏";
                                info.OperateContent = entity.ShowName + "(" + entity.PackName + ")";
                                info.reason = reason;
                                info.ElemId = id;
                                info.SourcePage = actype2;
                                info.Status = 1;
                                info.UserName = GetUserName();
                                new OperateRecordBLL().Insert(info);
                            }
                        }

                    }
                    else if (status == 4)
                    {
                        if (new AppInfoBLL().UpdateStatus(id, status))
                        {
                            OperateRecordEntity info = new OperateRecordEntity()
                            {
                                ElemId = id,
                                reason = "",
                                Status = 1,
                                OperateType = "4",
                                OperateFlag = "1",
                                SourcePage = actype2,
                                OperateExplain = "修改游戏状态为接入中",
                                OperateContent = entity.ShowName + "(" + entity.PackName + ")",
                                UserName = GetUserName(),
                            };
                            new OperateRecordBLL().Insert(info);
                        }
                    }
                    else if (status == 5)
                    {
                        if (new AppInfoBLL().UpdateStatus(id, status))
                        {
                            OperateRecordEntity info = new OperateRecordEntity()
                            {
                                ElemId = id,
                                reason = "",
                                Status = 1,
                                OperateFlag = "2",
                                SourcePage = actype2,
                                OperateType = "4",
                                OperateExplain = "修改游戏状态为测试中",
                                OperateContent = entity.ShowName + "(" + entity.PackName + ")",
                                UserName = GetUserName(),
                            };
                            new OperateRecordBLL().Insert(info);
                        }
                    }
                    else if (status == 6)
                    {
                        if (new AppInfoBLL().UpdateStatus(id, status))
                        {
                            OperateRecordEntity info = new OperateRecordEntity()
                            {
                                ElemId = id,
                                reason = "",
                                Status = 1,
                                SourcePage = actype2,
                                OperateFlag = "3",
                                OperateType = "4",
                                OperateExplain = "提交游戏审核",
                                OperateContent = entity.ShowName + "(" + entity.PackName + ")",
                                UserName = GetUserName(),
                            };
                            new OperateRecordBLL().Insert(info);
                        }
                        //Response.Redirect("http://localhost:16436/AppInfoListNew.aspx?Action=3");
                        Response.Write("1");
                        Response.End();
                    }
                }
                else if (ac == "updateappinfo")
                {
                    int status = int.Parse(Request.QueryString["status"]);
                    int id = int.Parse(Request.QueryString["id"]);
                    int arch = int.Parse(Request.QueryString["arch"]);
                    string reason = Request.QueryString["reason"].ToString();
                    int actype2 = int.Parse(Request.QueryString["actype"]);
                    AppInfoEntity entity = new AppInfoBLL().GetSingle(id);
                    if (entity == null)
                    {
                        entity.ShowName = "";
                        entity.PackName = "";
                    }
                    if (new AppInfoBLL().UpdateStatus(id, status) && new AppInfoBLL().UpdateArch(id, arch))
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = id,
                            reason = reason,
                            Status = 1,
                            SourcePage = actype2,
                            OperateType = "2",
                            UserName = GetUserName(),
                        };
                        if (status == 1)
                        {
                            info.OperateFlag = "6";
                            info.OperateContent = entity.ShowName + "(" + entity.PackName + "),机型适配：" + BindArch(arch);
                            info.OperateExplain = "上架游戏";
                        }
                        else if (status == 2)
                        {
                            info.OperateFlag = "5";
                            info.OperateContent = entity.ShowName + "(" + entity.PackName + ")";
                            info.OperateExplain = "下架游戏";
                        }
                        new OperateRecordBLL().Insert(info);
                        Response.Write("修改成功");
                        Response.End();
                    }
                    else
                    {
                        Response.Write("修改失败");
                        Response.End();
                    }
                    Response.Write("修改失败");
                    Response.End();
                }
                else
                {
                    BindData2(actype);
                    BindDataLog();
                }

            }
        }

        public static string Obj2Json(List<OperateRecordEntity> data)
        {

            DataContractJsonSerializer json = new DataContractJsonSerializer(data.GetType());
            string szJson = "";
            string jsonString = "";

            //序列化
            using (MemoryStream stream = new MemoryStream())
            {
                json.WriteObject(stream, data);

                szJson = Encoding.UTF8.GetString(stream.ToArray());

            }
            string p = @"///Date/((/d+)/+/d+/)///";
            MatchEvaluator matchEvaluator = new MatchEvaluator(ConvertJsonDateToDateString);
            Regex reg = new Regex(p);
            jsonString = reg.Replace(szJson, matchEvaluator);
            return jsonString;
        }
        private static string ConvertJsonDateToDateString(Match m)
        {
            string result = string.Empty;
            DateTime dt = new DateTime();
            dt = dt.AddMilliseconds(long.Parse(m.Groups[1].Value));
            dt = dt.ToLocalTime();
            result = dt.ToString("yyyy-MM-dd HH:mm:ss");
            return result;
        }

        private void BindData2(int Action)
        {
            try
            {
                int totalCount = 0;
                AppInfoEntity entity = new AppInfoEntity()
                {
                    SearchType = SearchType.SelectedValue,
                    SearchKeys = this.Keyword_2.Text.Trim(),
                    AppClass = 12,
                    //AppType = AppType.SelectedValue.Convert<int>(0),
                    OrderType = OrderType.SelectedValue,
                    StartIndex = pagerList.StartRecordIndex - 1,
                    EndIndex = pagerList.PageSize,
                    Status = Action,
                };

                //if (SearchType.SelectedValue == "1")
                //{
                //    entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Text);
                //}
                //dic_DevList = new B_DevBLL().GetDevListDic();
                //dic_DevList[0] = "";

                //Type 1=已上架游戏 2=待审核游戏  3=游戏接入情况 ，4=已下架游戏，5=审核不通过游戏
                string status = "";
                switch (Action)
                {
                    case 1:
                        status = "1";
                        break;
                    case 2:
                        status = "6";
                        break;
                    case 3:
                        status = "4,5,6,7";
                        break;
                    case 4:
                        status = "2";
                        break;
                    case 5:
                        status = "7";
                        break;
                    default:
                        break;
                }

                int Type = Action;
                List<AppInfoEntity> list = new AppInfoBLL().GetDataListNew(entity, status, ",70,", ref totalCount);
                this.objRepeaterData.DataSource = list;
                this.objRepeaterData.DataBind();
                Count = totalCount;
                pagerList.RecordCount = totalCount;
                pagerList.DataBind();
            }
            catch (Exception e)
            {
                throw;
            }
        }

        public string BindTitle(object val)
        {
            string value = "";
            switch (val.ToString())
            {
                case "1":
                    value = "已上架游戏";
                    break;
                case "2":
                    value = "待审核游戏";
                    break;
                case "3":
                    value = "游戏接入情况";
                    break;
                case "4":
                    value = "已下架游戏";
                    break;
                case "5":
                    value = "审核不通过游戏";
                    break;
                default:
                    break;
            }
            return value;
        }


        /// <summary>
        /// 绑定合作类型
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindType(object val)
        {
            string status_val = "";
            //int Status = Convert.ToInt32(val);
            if (val != null && val.ToString() != "")
            {
                int Status = ConvertHelper.ToInt(val.ToString());

                if (Status == 1)
                {
                    status_val = "联运";
                }
                else if (Status == 2)
                {
                    status_val = "CPS";
                }
                else if (Status == 3)
                {
                    status_val = "CPA";
                }
                else if (Status == 99)
                {
                    status_val = "未合作";
                }
            }
            return status_val;
        }
        /// <summary>
        /// 绑定机型适配
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindArch(object val)
        {
            int arch = 0;
            if (val != null && val.ToString() != "")
            {
                arch = Convert.ToInt32(val);
            }
            string status_val = "";
            if (arch == 1)
            {
                status_val = "Arm";
            }
            else if (arch == 2)
            {
                status_val = "X86";
            }
            else if (arch == 3)
            {
                status_val = "Arm,X86";
            }
            return status_val;
        }

        public string BindPackUrl(object val)
        {
            int id = Convert.ToInt32(val);
            string url = "";
            PackInfoEntity entity = new PackInfoBLL().GetSingle(id);
            if (entity != null)
            {
                url = entity.PackUrl;
            }
            return url;
        }
        public string BindThrough(object val)
        {
            int Status = Convert.ToInt32(val);
            string status_val = "";
            if (Status != 7)
            {
                status_val = "审核不通过";
            }
            else
            {
                status_val = "";
            }
            return status_val;
        }
        /// <summary>
        /// 绑定审核不通过原因
        /// </summary>
        /// <param name="status"></param>
        /// <param name="appid"></param>
        /// <returns></returns>
        public string BindReason(object status, object AppId)
        {
            int Status = Convert.ToInt32(status);
            int appid = Convert.ToInt32(AppId);
            string status_val = "";
            if (Status == 7)
            {
                var list = new OperateRecordBLL().GetReasonByElemId(appid);
                foreach (var item in list)
                {
                    status_val += item.reason + ",";
                }
            }
            else
            {
                status_val = "";
            }
            return status_val;

        }
        /// <summary>
        /// 判定状态显示
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindStatus2(object val)
        {
            int Status = Convert.ToInt32(val);
            string status_val = "";
            if (Status == 1)
            {
                status_val = "下架游戏";
            }
            else if (Status == 2)
            {
                status_val = "重新上架";
            }
            return status_val;
        }
        public string BindLast(object val)
        {
            int Status = Convert.ToInt32(val);
            string status_val = "";
            if (Status == 6)
            {
                status_val = "取消审核";
            }
            else if (Status == 4)
            {
                status_val = " ";
            }
            else
            {
                status_val = "上一步";
            }
            return status_val;
        }
        public string BindLast2(object val)
        {
            int Status = Convert.ToInt32(val);
            string status_val = "";
            if (Status == 6)
            {
                status_val = " ";
            }
            else
            {
                status_val = "下一步";
            }
            return status_val;
        }

        private void BindData()
        {
            int totalCount = 0;

            AppInfoEntity entity = new AppInfoEntity()
            {
                SearchType = SearchType.SelectedValue,
                SearchKeys = this.Keyword_2.Text.Trim(),
                AppClass = 11,
                //AppType = AppType.SelectedValue.Convert<int>(0),
                OrderType = OrderType.SelectedValue,
                StartIndex = pagerList.StartRecordIndex - 1,
                EndIndex = pagerList.PageSize,
                Status = 1
            };
            if (SearchType.SelectedValue == "1")
            {
                entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Text);
            }
            //dic_DevList = new B_DevBLL().GetDevListDic();
            //dic_DevList[0] = "";
            List<AppInfoEntity> list = new AppInfoBLL().GetDataList(entity, ref totalCount);
            this.objRepeaterData.DataSource = list;
            this.objRepeaterData.DataBind();
            pagerList.RecordCount = totalCount;
            pagerList.DataBind();
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {

            BindData2(actype);

        }
        protected void AspNetPager1_PageChanged(object sender, EventArgs e)
        {
            BindDataLog();

        }

        private void BindDataLog()
        {
            int totalCount = 0;

            int StartIndex = AspNetPager1.StartRecordIndex - 1;
            int EndIndex = AspNetPager1.PageSize;

            List<OperateRecordEntity> list = new OperateRecordBLL().GetDataList(StartIndex, EndIndex, actype, ref totalCount);
            this.Repeater1.DataSource = list;
            this.Repeater1.DataBind();
            AspNetPager1.RecordCount = totalCount;
            AspNetPager1.DataBind();
        }

        protected void btnSearch_Click(object sender, EventArgs e)
        {

            BindData2(actype);
        }
        public bool IsVal { get { return Extensions.AppSettings<bool>("IsValidate", false); } }


        protected void OrderType_SelectedIndexChanged(object sender, EventArgs e)
        {
            BindData2(actype);
        }

        //导出数据到报表
        protected void btnExport_Click(object sender, EventArgs e)
        {
            Export(actype);
        }
        public void Export(int actype)
        {
            try
            {
                AppInfoEntity entity = new AppInfoEntity()
                {
                    SearchType = SearchType.SelectedValue,
                    SearchKeys = this.Keyword_2.Text.Trim(),
                    AppClass = 12,
                    //AppType = AppType.SelectedValue.Convert<int>(0),
                    OrderType = OrderType.SelectedValue,
                    StartIndex = 0,
                    EndIndex = 0,
                    Status = actype,
                };

                if (SearchType.SelectedValue == "1")
                {
                    entity.SearchKeys = new B_DevBLL().GetDevIDByName(this.Keyword_2.Text);
                }
                string status = "";
                switch (actype)
                {
                    case 1:
                        status = "1";
                        break;
                    case 2:
                        status = "6";
                        break;
                    case 3:
                        status = "4,5,6,7";
                        break;
                    case 4:
                        status = "2";
                        break;
                    case 5:
                        status = "7";
                        break;
                    default:
                        break;
                }

                dataset = new AppInfoBLL().GetExportDataList(entity, status, ",70,");
                datatable = dataset.Tables[0];
                string fileName = string.Format("GameInfo_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                WriteExcel2(datatable, path, fileName);
                DownloadFile(path);
            }
            catch (Exception)
            {
                throw;
            }
        }
        /// <summary>
        /// 导出报表
        /// </summary>
        /// <param name="ds"></param>
        /// <param name="path"></param>
        /// <param name="fileName"></param>
        public void WriteExcel2(DataTable ds, string path, string fileName)
        {
            try
            {
                FindPath(path);
                StreamWriter sw = new StreamWriter(path, false, Encoding.GetEncoding("gb2312"));
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < ds.Columns.Count; k++)
                {
                    if (ds.Columns[k].ColumnName.ToString() != "ID")
                    {
                        sb.Append(ds.Columns[k].ColumnName.ToString() + "\t");
                    }

                }
                sb.Append(Environment.NewLine);
                for (int i = 0; i < ds.Rows.Count; i++)
                {
                    for (int j = 0; j < ds.Columns.Count; j++)
                    {
                        if (j != 8)
                        {
                            if (j == 6)
                            {
                                sb.Append(BindStatus(ds.Rows[i][j]) + "\t");
                            }
                            else if (j == 7)
                            {
                                sb.Append(BindPermission(ds.Rows[i][j].ToString(), 0) + "\t");
                            }
                            else if (j == 4)
                            {
                                sb.Append(BindType(ds.Rows[i][j]) + "\t");
                            }
                            else
                            {
                                sb.Append(ds.Rows[i][j].ToString() + "\t");
                            }
                        }
                    }
                    sb.Append(Environment.NewLine);
                }
                sw.Write(sb.ToString());
                sw.Flush();
                sw.Close();
            }
            catch (Exception)
            {

                throw;
            }
        }
        /// <summary>
        /// 绑定调用权限
        /// </summary>
        /// <param name="per"></param>
        /// <param name="id"></param>
        /// <returns></returns>
        public string BindPermission(string per, object id)
        {
            string str = "";
            if (per.Trim().Length > 0)
            {
                str = GetAndroidPermission(per);
            }
            return str;
        }
        protected void objRepeaterExport_ItemDataBound(object sender, RepeaterItemEventArgs e)
        {
            if (e.Item.ItemType == ListItemType.Item || e.Item.ItemType == ListItemType.AlternatingItem)
            {
                DataRowView drv = (DataRowView)e.Item.DataItem;
                if (e.Item.FindControl("LiteralItem") != null)
                {
                    Literal ltrtem = e.Item.FindControl("LiteralItem") as Literal;
                    StringBuilder sblItemText = new StringBuilder();

                    sblItemText.Append("<td>" + drv["游戏名"].ToString() + "</td>");
                    sblItemText.Append("<td>" + drv["包名"].ToString() + "</td>");
                    sblItemText.Append("<td>" + drv["版本"].ToString() + "</td>");
                    sblItemText.Append("<td>" + drv["游戏类型"].ToString() + "</td>");
                    sblItemText.Append("<td>" + drv["开发者"].ToString() + "</td>");
                    sblItemText.Append("<td>" + BindType(drv["合作类型"]) + "</td>");
                    sblItemText.Append("<td>" + BindStatus(drv["状态"]) + "</td>");
                    sblItemText.Append("<td>" + BindPermission(drv["权限调用"].ToString(), drv["AppID"]) + "</td>");
                    ltrtem.Text = sblItemText.ToString();
                }
            }
        }

        protected void BtnExportLog_Click(object sender, EventArgs e)
        {
            //int type = 2;
            //if (actype == 2)
            //{
            //    type = 3;
            //}
            dataset = new OperateRecordBLL().GetDataList(actype);
            datatable = dataset.Tables[0];
            string fileName = string.Format("GameInfoLog_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
            string path = Server.MapPath(ServerMapPath() + fileName);
            WriteExcel2(datatable, path, fileName);
            DownloadFile(path);
        }

        protected void btnExport2_Click(object sender, EventArgs e)
        {
            Export(actype);
        }

        public string BindCont(object cont, object reason)
        {

            if (reason.ToString() != "")
            {
                return cont.ToString() + "(原因：" + reason.ToString() + ")";
            }
            else
            {
                return cont.ToString();
            }
        }

    }
}