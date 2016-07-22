using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
namespace AppStore.Web
{
    public partial class GroupElementAdd : BasePage
    {
        public int GroupID { get { return this.Request<int>("GroupID", 0); } }
        public int SchemeID { get { return this.Request<int>("SchemeID", 0); } }
        public string PageType { get { return this.Request<string>("page", ""); } }
        public string GroupName { get; set; }
        // 所要修改元素ID，0代表新增元素
        protected int GroupElementID = 0;
        protected GroupElemsEntity _CurrentEntity = null;
        // 导航条显示状态（新增还是编辑）
        public string NavShowStatus { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            GroupElementID = nwbase_sdk.Tools.GetRequestVal("GroupElementID", 0);
            if (!IsPostBack)
            {
                if (GroupElementID > 0)
                {
                    NavShowStatus = "编辑";
                }
                else
                {
                    NavShowStatus = "新增";
                    txtStartTime.Text = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
                    txtEndTime.Text = DateTime.Now.AddYears(20).ToString("yyyy-MM-dd HH:mm");
                }
                Bind();
            }
            QueryGroupNameByID();
        }

        /// <summary>
        /// 根据分组ID查询分组名称（绑定到导航）
        /// </summary>
        private void QueryGroupNameByID()
        {
            this.GroupName = this.Request<string>("GroupName", "");
            if (GroupName == "")
            {
                this.GroupName = new GroupInfoBll().QueryGroupNameByID(GroupID);
            }
        }

        protected void OnSave_Click(object sender, EventArgs e)
        {
            var currentEntity = new GroupElemsEntity();
            currentEntity.GroupID = GroupID;
            currentEntity.GroupElemID = GroupElementID;
            currentEntity.ElemID = nwbase_sdk.Tools.GetInt(hfAppID.Value, 0);
            currentEntity.ElemType = 1;
            currentEntity.RecommPicUrl = hfIconUrl.Value;
            currentEntity.RecommTitle = txtShowName.Text;
            currentEntity.RecommWord = txtRecommWord.Text;
            currentEntity.StartTime = DateTime.ParseExact(txtStartTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.EndTime = DateTime.ParseExact(txtEndTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.Status = nwbase_sdk.Tools.GetInt(ddlStatus.SelectedValue, 1);

            currentEntity.Remarks = string.Empty;

            currentEntity.PosID = txtPosID.Text.Trim().Convert<int>(0);

            //判断同一分组，位置编号是否重复
            bool answer = new GroupBLL().IsExistPosID(currentEntity);
            if (answer)
            {
                this.Alert("位置编号不能重复");
                return;
            }
            if (txtPosID.Text.Trim().Convert<int>(0)<1)
            {
                this.Alert("位置编号不能小于1");
                return;
            }
            if (txtPosID.Text.Trim().Convert<int>(0) > 500)
            {
                this.Alert("位置编号不能大于500");
                return;
            }

            bool result = false;
            if (GroupElementID <= 0)
            {
                //新增
                // 如果同一分组内存在相同元素
                if (new GroupElemsBLL().ExistSameElementIDInGroup(currentEntity.GroupID, currentEntity.ElemID))
                {
                    this.Alert("此分组存在相同元素...");
                    return;
                }
                result = new GroupElemsBLL().InsertGroupElement(currentEntity);
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = 0,
                        reason = "",
                        Status = 1,
                        OperateFlag = "1",
                        OperateContent = GroupName + "(" + new AppInfoBLL().GetSingle(currentEntity.ElemID).ShowName + ")",
                        UserName = GetUserName(),
                    };
                    if (GroupID != 93 && GroupID != 94)
                    {
                        info.SourcePage = 62;
                        info.OperateType = "6";
                        info.OperateExplain = "分类列表：新增游戏";
                    }
                    else
                    {
                        if (GroupID == 93)
                        {
                            info.SourcePage = 63;
                            info.OperateType = "7";
                            info.OperateExplain = "游戏排行: 新增游戏";
                        }
                        else if (GroupID == 94)
                        {
                            info.SourcePage = 64;
                            info.OperateType = "8";
                            info.OperateExplain = "最新游戏: 新增游戏";
                        }
                    }
                    new OperateRecordBLL().Insert(info);
                }
            }
            else
            {
                // 修改
                result = new GroupBLL().BeginnerRecommendUpdate(currentEntity);
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = GroupElementID,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        OperateContent = GroupName + "(" + new AppInfoBLL().GetSingle(currentEntity.ElemID).ShowName + ")",
                        UserName = GetUserName(),
                    };
                    if (GroupID != 93 && GroupID != 94)
                    {
                        info.SourcePage = 62;
                        info.OperateType = "6";
                        info.OperateExplain = "分类列表：编辑游戏";
                    }
                    else
                    {
                        if (GroupID == 93)
                        {
                            info.SourcePage = 63;
                            info.OperateType = "7";
                            info.OperateExplain = "游戏排行: 编辑游戏";
                        }
                        else if (GroupID == 94)
                        {
                            info.SourcePage = 64;
                            info.OperateType = "8";
                            info.OperateExplain = "最新游戏: 编辑游戏";
                        }
                    }
                    new OperateRecordBLL().Insert(info);
                }
            }

            if (result)
            {

                this.Alert("保存成功", string.Format("GroupElement.aspx?GroupID=" + GroupID + "&GroupName=" + GroupName + "&SchemeID=" + SchemeID + "&page=" + PageType));

            }
            else
                this.Alert("保存失败！");
        }

        private void Bind()
        {
            if (GroupElementID > 0)
            {
                _CurrentEntity = new GroupBLL().GetGroupElemByID(GroupElementID);
                if (_CurrentEntity != null)
                {
                    hfAppID.Value = _CurrentEntity.ElemID.ToString();
                    hfIconUrl.Value = _CurrentEntity.RecommPicUrl;
                    txtShowName.Text = _CurrentEntity.RecommTitle;
                    txtRecommWord.Text = _CurrentEntity.RecommWord;
                    txtStartTime.Text = _CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    txtEndTime.Text = _CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                    ddlStatus.SelectedValue = _CurrentEntity.Status.ToString();
                    txtPosID.Text = _CurrentEntity.PosID.ToString();
                }
            }

            // 绑定最大位置id Tips
            var currentEntity = new GroupElemsEntity();
            currentEntity.GroupID = GroupID;
            int posid = new GroupBLL().GetMaxPosID(currentEntity);
            //todo:

        }
    }
}