using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using nwbase_sdk;
using System.Data;

namespace AppStore.Web
{
    public partial class SpecialTopicEdit : BasePage
    {
        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Request<int>("GroupTypeID", 3100); } }
        public string PageType { get { return this.Request<string>("page", ""); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }

        protected GroupInfoEntity _CurrentEntity = null;
        protected int _Id = 0;

        public List<GroupElemsEntity> applist = new List<GroupElemsEntity>();
        public string SpecialName = "";
        protected void Page_Load(object sender, EventArgs e)
        {
            _Id = nwbase_sdk.Tools.GetRequestVal("id", 0);
            if (!IsPostBack)
                Bind();
        }

        protected void OnSave(object sender, EventArgs e)
        {
            var currentEntity = new GroupInfoEntity();
            currentEntity.GroupID = _Id;
            currentEntity.GroupTypeID = this.GroupTypeID;
            currentEntity.OrderType = 0;
            currentEntity.OrderNo = 0;
            currentEntity.GroupName = txtGroupName.Text;
            currentEntity.GroupDesc = txtGroupDesc.Text;
            currentEntity.GroupTips = "";
            currentEntity.GroupPicUrl = ThumbPicUrl.Value;
            currentEntity.Remarks = "";
            currentEntity.StartTime = this.txtStartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            currentEntity.EndTime = this.txtEndTime.Text.Trim().Convert<DateTime>(new DateTime(2048, 1, 1));
            currentEntity.CreateTime = DateTime.Now;
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Status = 1; // 开启
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Remarks = string.Empty;
            currentEntity.SchemeID = this.SchemeID;

            bool result = false;
            if (_Id <= 0)
            {
                //新增
                currentEntity.CreateTime = DateTime.Now;
                int newId = new GroupBLL().InsertInfoForId(currentEntity);
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                           {
                               ElemId = newId,
                               reason = "",
                               Status = 1,
                               OperateFlag = "1",
                               SourcePage = 61,
                               OperateType = "5",
                               OperateExplain = "新增专题",
                               OperateContent = currentEntity.GroupName,
                               UserName = GetUserName(),
                           };
                    new OperateRecordBLL().Insert(info);
                }
                this.Alert("新增成功", "SpecialTopicEdit.aspx?id=" + newId.ToString() + "&page=" + PageType + "&SchemeID=" + SchemeID.ToString() + "&GroupTypeID=" + GroupTypeID.ToString());
            }
            else
            {
                result = new GroupBLL().SpecialTopicUpdate(currentEntity);
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = _Id,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        SourcePage = 61,
                        OperateType = "5",
                        OperateExplain = "编辑专题",
                        OperateContent = currentEntity.GroupName,
                        UserName = GetUserName(),
                    };
                    new OperateRecordBLL().Insert(info);
                }
            }
            if (result)
            {
                this.Alert("保存成功");
                Bind();
            }
            else
                this.Alert("保存失败！");
        }

        private void Bind()
        {
            if (_Id > 0)
            {
                _CurrentEntity = new GroupBLL().GetGroupInfoByID(_Id);
                if (_CurrentEntity != null)
                {
                    txtGroupName.Text = _CurrentEntity.GroupName;
                    SpecialName = _CurrentEntity.GroupName;
                    txtGroupDesc.Text = _CurrentEntity.GroupDesc;
                    ThumbPicUrl.Value = _CurrentEntity.GroupPicUrl;
                    ShowThumbPic.ImageUrl = _CurrentEntity.GroupPicUrl;
                    this.txtStartTime.Text = _CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    this.txtEndTime.Text = _CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                }
                applist = new GroupBLL().GetGroupElemsByGroupID(_Id);
                for (int i = 1; i <= applist.Count; i++)
                {
                    applist[i - 1].OrderNo = i;
                }
                DataList.DataSource = applist;
                DataList.DataBind();
                if (PageType == "new" && applist.Count > 0)
                {
                    Button2.Visible = true;
                }
            }
            else
            {
                txtStartTime.Text = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
                txtEndTime.Text = DateTime.Now.AddYears(10).ToString("yyyy-MM-dd HH:mm");
            }

            // BindShow
            if (GroupTypeID == 3200)
            {
                addAppLi.Visible = false;
            }

        }

        [WebMethod]
        public static int Add(int appid, string name, string pic, int gid)
        {
            var currentEntity = new GroupElemsEntity();
            currentEntity.GroupElemID = 0;
            currentEntity.ElemID = appid;
            currentEntity.ElemType = 1;
            currentEntity.RecommPicUrl = pic;
            currentEntity.RecommTitle = name;
            currentEntity.RecommWord = "";
            currentEntity.StartTime = new DateTime(1900, 1, 1);
            currentEntity.EndTime = new DateTime(2048, 1, 1);
            currentEntity.Status = 1;
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Remarks = string.Empty;
            currentEntity.CreateTime = DateTime.Now;
            currentEntity.GroupID = gid;
            currentEntity.OrderNo = 0;

            return new GroupBLL().InsertElemForId(currentEntity);
        }

        [WebMethod]
        public static void UpdateOrder(string orderinfo, int SchemeID)
        {
            Dictionary<int, int> items = new Dictionary<int, int>();
            foreach (string eachInfo in orderinfo.Split(','))
            {
                if (string.IsNullOrEmpty(eachInfo))
                    break;
                int appId = Tools.GetInt(eachInfo.Split(':')[0], 0);
                int order = Tools.GetInt(eachInfo.Split(':')[1], 0);
                items.Add(appId, order);
            }
            new GroupBLL().UpdateElemOrder(items);
        }


        [WebMethod]
        public static void Delete(int id, int SchemeID)
        {
            new GroupBLL().DeleteElem(id);
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            applist = new GroupBLL().GetGroupElemsByGroupID(_Id);
            if (applist.Count>0)
            {
                DataTable dt = BaseCommon.ToDataTable<GroupElemsEntity>(applist);
                string fileName = string.Format("Special_AppInfo_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                WriteAppInfoExcel(dt, path, fileName);
                DownloadFile(path); 
            }
        }
    }
}