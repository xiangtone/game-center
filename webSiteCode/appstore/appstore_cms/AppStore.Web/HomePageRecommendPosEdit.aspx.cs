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
    public partial class HomePageRecommendPosEdit : BasePage
    {
        #region 全局属性

        /// <summary>
        /// 推荐位ID
        /// </summary>
        public int PosID { get { return this.Request<int>("PosID", 0); } }

        /// <summary>
        /// 排序号
        /// </summary>
        public int OrderNo { get { return this.Request<int>("OrderNo", 0); } }

        public GroupElemsEntity CurrentEntity { get; set; }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Request<int>("GroupTypeID", 4102); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }

        public string PageType { get { return this.Request<string>("page", ""); } }


        #endregion

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                InitPage();
                this.BindData();
            }
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            if (this.Action == "Add")
            {
                this.Add();
            }
            else if (this.Action == "Edit")
            {
                this.Edit();
            }
        }

        private void InitPage()
        {
            StartTime.Text = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
            EndTime.Text = DateTime.Now.AddYears(10).ToString("yyyy-MM-dd HH:mm");
        }

        private void Add()
        {
            GroupElemsEntity entity = new GroupElemsEntity();
            entity.GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID);
            entity.PosID = this.PosID;
            entity.ElemType = this.ElemType.SelectedValue.Convert<int>(1);
            entity.GroupType = this.GroupType.Value.Convert<int>(0);
            entity.OrderNo = this.OrderNo;
            entity.ShowType = this.ShowType.SelectedValue.Convert<int>(0);

            // Banner位，OrderNo处理
            // 取最大的OrderNo+1
            if (this.PosID == 1)
            {
                string strWhere = string.Format(" GroupID={0} and PosID={1}", entity.GroupID, PosID);
                List<GroupElemsEntity> groupElemsList = new GroupBLL().GetList(1, strWhere, " orderNo desc");
                if (groupElemsList != null && groupElemsList.Count > 0)
                {
                    entity.OrderNo = groupElemsList[0].OrderNo + 1;
                }
            }

            //跳转至单机或网游时元素ID为0
            if (this.ElemType.SelectedValue == "4")
            {
                entity.ElemID = 0;
                entity.GroupType = this.Request<int>("gameType", 0);
            }
            else
            {
                entity.ElemID = this.ElemID.Value.Convert<int>(0);
            }

            //if (entity.ShowType == 0)
            //{
            //    entity.RecommPicUrl = this.MainIconPicUrl.Value;
            //    if (this.MainIconPicUrl.Value.Trim() == "")
            //    {
            //        CurrentEntity.RecommPicUrl = this.RecommPicUrl.Value;
            //    }
            //}
            //else if (entity.ShowType == 1)
            //{
            //    entity.RecommPicUrl = this.RecommPicUrl.Value;
            //}
            //else
            //{
            //    entity.RecommPicUrl = string.Empty;
            //}
            entity.RecommPicUrl = this.RecommPicUrl.Value;
            if (this.RecommPicUrl.Value.Trim() == "")
            {
                entity.RecommPicUrl = this.MainIconPicUrl.Value;
            }
            entity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            entity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            entity.RecommTitle = this.RecommTitle.Text.Trim();
            entity.RecommWord = this.RecommWord.Text.Trim();
            entity.RecommTag = GetRecommTag();
            entity.Status = this.Status.SelectedValue.Convert<int>(0);
            entity.Remarks = this.Remarks.Text.Trim();


            if (new GroupElemsBLL().IsExist(entity))
            {
                this.Alert("当前应用已存在，添加失败");
            }
            else
            {
                bool result = new GroupElemsBLL().Insert(entity);
                if (result)
                {
                    if (SchemeID == 104)
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = entity.GroupElemID,
                            reason = "",
                            Status = 1,
                            OperateFlag = "1",
                            OperateType = "1",
                            SourcePage = 60,
                            OperateExplain = "首页推荐位增加",
                            UserName = GetUserName(),
                        };
                        if (entity.PosID == 1)
                        {
                            info.OperateContent = entity.RecommTitle + "(Bnaner)";
                        }
                        else
                        {
                            if (entity.ShowType == 1)
                            {
                                info.OperateContent = entity.RecommTitle + "(广告位)";
                            }
                            else
                            {
                                info.OperateContent = entity.RecommTitle + "(推荐位)";
                            }
                        }
                        new OperateRecordBLL().Insert(info);
                    }

                    this.Alert("添加成功", string.Format("HomePageRecommendByAppCenterList.aspx?acttype={0},{1}&page={2}", this.GroupTypeID, this.SchemeID, this.PageType));
                }
                else
                {
                    this.Alert("添加失败");
                }
            }
        }

        private void Edit()
        {
            this.CurrentEntity = new GroupElemsEntity();
            CurrentEntity.GroupElemID = this.Id.Convert<int>(0);
            CurrentEntity.GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID);
            CurrentEntity.PosID = this.PosID;
            CurrentEntity.ElemType = this.ElemType.SelectedValue.Convert<int>(1);
            CurrentEntity.GroupType = this.GroupType.Value.Convert<int>(0);
            CurrentEntity.OrderNo = this.OrderNo;
            CurrentEntity.ShowType = this.ShowType.SelectedValue.Convert<int>(0);

            //跳转至单机或网游时元素ID为0
            if (this.ElemType.SelectedValue == "4")
            {
                CurrentEntity.ElemID = 0;
                CurrentEntity.GroupType = this.Request<int>("gameType", 0);
            }
            else
            {
                CurrentEntity.ElemID = this.ElemID.Value.Convert<int>(0);
            }

            //if (CurrentEntity.ShowType == 0)
            //{
            //    CurrentEntity.RecommPicUrl = this.MainIconPicUrl.Value;
            //}
            //else if (CurrentEntity.ShowType == 1)
            //{
            //    CurrentEntity.RecommPicUrl = this.RecommPicUrl.Value;
            //}
            //else
            //{
            //    CurrentEntity.RecommPicUrl = string.Empty;
            //}
            CurrentEntity.RecommPicUrl = this.RecommPicUrl.Value;
            if (this.RecommPicUrl.Value.Trim() == "")
            {
                CurrentEntity.RecommPicUrl = this.MainIconPicUrl.Value;
            }
            CurrentEntity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            CurrentEntity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            CurrentEntity.RecommTitle = this.RecommTitle.Text.Trim();
            CurrentEntity.RecommTag = GetRecommTag();
            CurrentEntity.RecommWord = this.RecommWord.Text.Trim();
            CurrentEntity.RecommTag = GetRecommTag();
            CurrentEntity.Status = this.Status.SelectedValue.Convert<int>(0);
            CurrentEntity.Remarks = this.Remarks.Text.Trim();



            bool result = new GroupElemsBLL().Update(CurrentEntity);

            if (result)
            {
                if (SchemeID == 104)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = CurrentEntity.GroupElemID,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        OperateType = "1",
                        OperateExplain = "首页推荐位修改",
                        SourcePage = 60,
                        UserName = GetUserName(),
                    }; if (CurrentEntity.PosID == 1)
                    {
                        info.OperateContent = CurrentEntity.RecommTitle + "(Bnaner)";
                    }
                    else
                    {
                        if (CurrentEntity.ShowType == 1)
                        {
                            info.OperateContent = CurrentEntity.RecommTitle + "(广告位)";
                        }
                        else
                        {
                            info.OperateContent = CurrentEntity.RecommTitle + "(推荐位)";
                        }
                    }
                    new OperateRecordBLL().Insert(info);
                }

                this.Alert("修改成功", string.Format("HomePageRecommendByAppCenterList.aspx?acttype={0},{1}&page={2}", this.GroupTypeID, this.SchemeID, this.PageType));
            }
            else
            {
                this.Alert("修改失败");
            }

        }
        public int GetRecommTag()
        {
            int flag = 0;
            for (int i = 0; i < cbRecommFlag.Items.Count; i++)
            {
                if (cbRecommFlag.Items[i].Selected == true)
                {
                    flag = flag + int.Parse(cbRecommFlag.Items[i].Value);
                }
            }
            return flag;
        }
        protected string BindType(object id)
        {
            string str = "";
            int appid = int.Parse(id.ToString());
            AppInfoEntity entity = new AppInfoBLL().GetSingle2(appid);
            if (entity != null)
            {
                str = entity.AppTypeName;
            }
            return str;
        }
        private void BindData()
        {
            if (this.Action == "Edit")
            {
                this.CurrentEntity = new GroupBLL().GetGroupElemByID(this.Id);

                if (CurrentEntity != null)
                {
                    this.ElemID.Value = CurrentEntity.ElemID.ToString();
                    this.txtShowName.Text = CurrentEntity.RecommTitle;
                    this.ElemType.SelectedValue = CurrentEntity.ElemType.ToString();
                    this.RecommTitle.Text = CurrentEntity.RecommTitle;
                    this.RecommWord.Text = CurrentEntity.RecommWord;
                    this.StartTime.Text = CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    this.EndTime.Text = CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                    this.Status.SelectedValue = CurrentEntity.Status.ToString();
                    this.RecommPicUrl.Value = CurrentEntity.RecommPicUrl;
                    this.MainIconPicUrl.Value = CurrentEntity.RecommPicUrl;
                    this.Remarks.Text = CurrentEntity.Remarks;
                    this.GroupType.Value = CurrentEntity.GroupType.ToString();
                    this.ShowType.SelectedValue = CurrentEntity.ShowType.ToString();
                    this.TypeName.Text = BindType(CurrentEntity.ElemID);
                    new AppInfoEdit().BindTag(cbRecommFlag, CurrentEntity.RecommTag);
                }
                else
                {
                    this.Alert("参数非法");
                }
            }
            else if (this.Action == "Add" && this.PosID >= 1 && this.PosID <= 3)
            {
                this.ShowType.SelectedValue = "1";
            }
        }
    }
}