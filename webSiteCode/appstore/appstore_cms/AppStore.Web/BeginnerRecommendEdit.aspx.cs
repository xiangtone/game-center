using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using nwbase_sdk;
namespace AppStore.Web
{
    public partial class BeginnerRecommendEdit : BasePage
    {
        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.Request<int>("GroupTypeID", 4101); } }

        public int OrderNo { get { return Tools.GetInt("order", 0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }

        protected GroupElemsEntity _CurrentEntity = null;
        protected int _Id = 0;
        protected void Page_Load(object sender, EventArgs e)
        {
            _Id = nwbase_sdk.Tools.GetRequestVal("id", 0);
            if (!IsPostBack)
                Bind();
        }

        protected void OnSave(object sender, EventArgs e)
        {
            var currentEntity = new GroupElemsEntity();
            currentEntity.GroupElemID = _Id;
            currentEntity.ElemID = nwbase_sdk.Tools.GetInt(hfAppID.Value, 0);
            currentEntity.ElemType = 1;
            currentEntity.RecommPicUrl = hfIconUrl.Value;
            currentEntity.RecommTitle = txtShowName.Text;
            currentEntity.RecommWord = txtRecommWord.Text;
            currentEntity.StartTime = DateTime.ParseExact(txtStartTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.EndTime = DateTime.ParseExact(txtEndTime.Text, "yyyy-MM-dd HH:mm", System.Globalization.CultureInfo.CurrentCulture);
            currentEntity.Status = nwbase_sdk.Tools.GetInt(ddlStatus.SelectedValue, 1);
            currentEntity.UpdateTime = DateTime.Now;
            currentEntity.Remarks = string.Empty;
            currentEntity.GroupID = new GroupBLL().BeginnerRecommendGetGroupId(this.GroupTypeID, this.SchemeID);
            currentEntity.OrderNo = Tools.GetRequestVal("order", 0);
            currentEntity.PosID = txtPosID.Text.Trim().Convert<int>(0);

            //判断同一分组，位置编号是否重复
            bool answer = new GroupBLL().IsExistPosID(currentEntity);
            if (answer)
            {
                this.Alert("位置编号不能重复");
                return;
            }

            bool result = false;
            if (_Id <= 0)
            {
                //新增
                currentEntity.CreateTime = DateTime.Now;
                result = new GroupBLL().BeginnerRecommendInsert(currentEntity);
            }
            else
            {
                result = new GroupBLL().BeginnerRecommendUpdate(currentEntity);
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
                _CurrentEntity = new GroupBLL().GetGroupElemByID(_Id);
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
            else
            {
                // 绑定最大位置id Tips
                var currentEntity = new GroupElemsEntity();
                currentEntity.GroupID = new GroupBLL().BeginnerRecommendGetGroupId(this.GroupTypeID, this.SchemeID);
                int posid = new GroupBLL().GetMaxPosID(currentEntity);
                txtStartTime.Text = DateTime.Now.ToString("yyyy-MM-dd HH:mm");
                txtEndTime.Text = DateTime.Now.AddYears(10).ToString("yyyy-MM-dd HH:mm");
                if (posid > 0)
                {
                    labelPosIDTips.Text = "目前已设置的最大位置id:" + posid.ToString();
                }
                txtPosID.Text = Tools.GetStr(posid + 1, "0");

            }



        }
    }
}