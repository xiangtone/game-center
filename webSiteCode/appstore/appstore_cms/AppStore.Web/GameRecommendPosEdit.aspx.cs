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
    public partial class GameRecommendPosEdit : BasePage
    {
        #region 全局属性

        /// <summary>
        /// 推荐位ID
        /// </summary>
        public int PosID { get { return this.Request<int>("PosID", 0); } }

        /// <summary>
        /// 展示方式
        /// </summary>
        public int showtype { get { return this.Request<int>("showtype", 0); } }

        /// <summary>
        /// 排序号
        /// </summary>
        public int OrderNo { get { return this.Request<int>("OrderNo", 0); } }

        public GroupElemsEntity CurrentEntity { get; set; }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Request<int>("GroupTypeID", 5102); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }


        #endregion

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                this.ShowType.SelectedValue = showtype.ToString();
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
            else if (this.Action == "Edit" || this.Action == "Editapp")
            {
                this.Edit();
            }
        }
        //private void BandShowType()
        //{
        //    if (this.showtype == 1)
        //    {
        //        this.ShowType.Items.Add(new ListItem("广告位","1"));
        //    }
        //    else
        //    {
        //        this.ShowType.Items.Add(new ListItem("默认","0"));
        //    }
        //}
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
            entity.GroupType = GroupTypeID;
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


            if (this.RecommPicUrl.Value != "")
            {

                entity.RecommPicUrl = this.RecommPicUrl.Value;
            }
            else
            {
                entity.RecommPicUrl = this.MainIconPicUrl.Value;
            }
            entity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            entity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            entity.RecommTitle = this.RecommTitle.Text.Trim();
            entity.RecommTag = Convert.ToInt32(this.RecommTag.Text.Trim());
            entity.RecommWord = this.RecommWord.Text.Trim();

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
                    this.Alert("添加成功", string.Format("GameRecommendationList.aspx?Action={0},{1}", this.GroupTypeID, this.SchemeID));
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
            CurrentEntity.GroupType = GroupTypeID;
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
            if (this.RecommPicUrl.Value != "")
            {

                CurrentEntity.RecommPicUrl = this.RecommPicUrl.Value;
            }
            else
            {
                CurrentEntity.RecommPicUrl = this.MainIconPicUrl.Value;
            }

            CurrentEntity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            CurrentEntity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            CurrentEntity.RecommTitle = this.RecommTitle.Text.Trim();
            CurrentEntity.RecommTag = Convert.ToInt32(this.RecommTag.Text.Trim());
            CurrentEntity.RecommWord = this.RecommWord.Text.Trim();

            CurrentEntity.Status = this.Status.SelectedValue.Convert<int>(0);
            CurrentEntity.Remarks = this.Remarks.Text.Trim();



            bool result = new GroupElemsBLL().Update(CurrentEntity);

            if (result)
            {
                this.Alert("修改成功", string.Format("GameRecommendationList.aspx?Action={0},{1}", this.GroupTypeID, this.SchemeID));
            }
            else
            {
                this.Alert("修改失败");
            }

        }

        private void BindData()
        {
            if (this.Action == "Edit" || this.Action == "Editapp")
            {
                this.CurrentEntity = new GroupBLL().GetGroupElemByID(this.Id);

                if (CurrentEntity != null)
                {
                    this.ElemID.Value = CurrentEntity.ElemID.ToString();
                    this.txtShowName.Text = CurrentEntity.RecommTitle;
                    this.ElemType.SelectedValue = CurrentEntity.ElemType.ToString();
                    this.RecommTitle.Text = CurrentEntity.RecommTitle;
                    this.RecommWord.Text = CurrentEntity.RecommWord;
                    this.RecommTag.Text = CurrentEntity.RecommTag.ToString();
                    this.StartTime.Text = CurrentEntity.StartTime.ToString("yyyy-MM-dd HH:mm");
                    this.EndTime.Text = CurrentEntity.EndTime.ToString("yyyy-MM-dd HH:mm");
                    this.Status.SelectedValue = CurrentEntity.Status.ToString();
                    this.RecommPicUrl.Value = CurrentEntity.RecommPicUrl;
                    this.Remarks.Text = CurrentEntity.Remarks;
                    this.GroupType.Value = CurrentEntity.GroupType.ToString();
                    this.ShowType.SelectedValue = CurrentEntity.ShowType.ToString();
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