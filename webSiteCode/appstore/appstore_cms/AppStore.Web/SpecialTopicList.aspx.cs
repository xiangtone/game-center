using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using System.Data;
namespace AppStore.Web
{
    public partial class SpecialTopicList : BasePage
    {
        public string ActType { get { return this.Request<string>("acttype", "3100,2"); } }
        public string PageType { get { return this.Request<string>("page", ""); } }
        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.ActType.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.ActType.Split(',')[1].Convert<int>(0); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                Bind();
            BindDataLog();
            if (Request.QueryString["action"] == "ChangeOrdeNo")
            {
                string[] elemIdArray = SubStr(Request.Form["elemId"]);
                string[] OrderNoArray = SubStr(Request.Form["OrderNo"]);
                //修改OrderNO
                bool rult = true;
                for (int i = 0; i < elemIdArray.Length; i++)
                {
                    if (new GroupBLL().UpdateGroupOrderNoById(Convert.ToInt32(elemIdArray[i]), Convert.ToInt32(OrderNoArray[i])) > 0)
                    {
                        rult = true;
                    }
                    else { rult = false; }
                }
                if (rult == true)
                {
                    int scid = int.Parse(Request.QueryString["SchemeID"]);
                    if (scid == 104)
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = 0,
                            reason = "",
                            Status = 1,
                            OperateFlag = "4",
                            SourcePage = 61,
                            OperateType = "5",
                            OperateExplain = "专题列表位置排序",
                            OperateContent = "专题列表位置排序",
                            UserName = GetUserName(),
                        };
                        new OperateRecordBLL().Insert(info);
                    }

                    Response.Write("保存成功！");
                    Response.End();
                }
                else
                {
                    Response.Write("保存失败！");
                    Response.End();
                }
            }

        }
        public string[] SubStr(string data)
        {
            return data.Substring(0, data.Length - 1).Split(',');
        }
        private void Bind()
        {
            DataList.DataSource = new GroupBLL().SpecialTopicGetList(this.SchemeID, GroupTypeID / 100);
            DataList.DataBind();
        }

        public string BindSpecialStatus(object entity)
        {
            GroupInfoEntity obj = (GroupInfoEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                var timeSpan = obj.StartTime - currentTime;

                return string.Format("<span class=\"blue\">开启&nbsp;&nbsp;/&nbsp;&nbsp;{0}后显示</span>", timeSpan.Days > 0 ? timeSpan.Days.ToString() + "天" : timeSpan.Hours.ToString() + "小时");
            }
            else
            {
                return "<span class=\"black\">开启</span>";
            }
        }

        protected string BindTime(object entity)
        {
            GroupInfoEntity obj = (GroupInfoEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            DateTime currentTime = DateTime.Now;

            if (obj.EndTime < currentTime)
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
            else
            {
                return "<span class=\"black\">显示时间：" + timePart + "</span>";
            }
        }

        protected void OnDel(object s, CommandEventArgs e)
        {
            int id = nwbase_sdk.Tools.GetInt(e.CommandArgument, 0);
            if (id > 0)
            {
                GroupInfoEntity entity = new GroupBLL().GetGroupInfoByID(id);
                if (entity.GroupTypeID > GroupTypeID)
                {
                    this.Alert("删除操作无效");
                }
                else
                {
                    var result = new GroupBLL().DeleteInfo(id);
                    if (result)
                    {
                        if (SchemeID == 104)
                        {
                            OperateRecordEntity info = new OperateRecordEntity()
                            {
                                ElemId = entity.GroupID,
                                reason = "",
                                Status = 1,
                                OperateFlag = "3",
                                SourcePage = 61,
                                OperateType = "5",
                                OperateExplain = "删除专题",
                                OperateContent = "删除专题(" + entity.GroupName + ")",
                                UserName = GetUserName(),
                            };
                            new OperateRecordBLL().Insert(info);
                        }
                        this.Alert("删除成功");
                        BindDataLog();
                        Bind();
                    }
                    else
                        this.Alert("删除失败");
                }
            }
            else
                this.Alert("删除参数无效");
        }

        private void BindDataLog()
        {
            if (PageType == "new")
            {
                int totalCount = 0;
                int StartIndex = AspNetPager1.StartRecordIndex - 1;
                int EndIndex = AspNetPager1.PageSize;
                int type = 61;
                List<OperateRecordEntity> list = new OperateRecordBLL().GetDataList(StartIndex, EndIndex, type, ref totalCount);
                if (totalCount == 0)
                {
                    BtnExportLog.Visible = false;
                }
                this.Repeater1.DataSource = list;
                this.Repeater1.DataBind();
                AspNetPager1.RecordCount = totalCount;
                AspNetPager1.DataBind();
            }

        }
        protected void BtnExportLog_Click(object sender, EventArgs e)
        {
            DataSet dataset = new DataSet();
            DataTable datatable = new DataTable();
            dataset = new OperateRecordBLL().GetDataList(61);
            datatable = dataset.Tables[0];
            string fileName = string.Format("SpecialLog_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
            string path = Server.MapPath(ServerMapPath() + fileName);
            WriteExcel(datatable, path, fileName);
            DownloadFile(path);
        }
        protected void AspNetPager1_PageChanged(object sender, EventArgs e)
        {
            BindDataLog();
            Bind();
        }
    }
}