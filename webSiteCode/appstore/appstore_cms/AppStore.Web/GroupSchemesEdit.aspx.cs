using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.BLL;
using AppStore.Model;

namespace AppStore.Web
{
    public partial class GroupSchemesEdit : BasePage
    {
        public int SchemeID { get { return this.Request<int>("SchemeID"); } }

        public int GroupID { get { return this.Request<int>("GroupID"); } }

        public int GroupTypeID { get { return this.Request<int>("GroupTypeID"); } }

        public int OrderType { get { return this.Request<int>("OrderType"); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.Action == "Edit")
                {
                    this.BindData();
                }
                else if (this.Action == "Add")
                {
                    this.txtGroupID.Text = this.GroupID.ToString();
                    this.txtGroupTypeID.Text = this.GroupTypeID.ToString();
                    this.txtOrderType.Text = this.OrderType.ToString();
                }

            }
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            bool result = false;

            GroupSchemesEntity entity = new GroupSchemesEntity();
            entity.SchemeID = this.txtSchemeID.Text.Convert<int>();
            entity.GroupID = this.txtGroupID.Text.Convert<int>();
            entity.GroupTypeID = this.txtGroupTypeID.Text.Convert<int>();
            entity.OrderType = this.txtOrderType.Text.Convert<int>();
            entity.Status = this.ddlStatus.SelectedValue.Convert<int>(0);

            if (this.Action == "Edit")
            {
                result = new GroupSchemesBLL().Update(entity);
            }

            else if (this.Action == "Add")
            {
                result = new GroupSchemesBLL().Insert(entity);
            }

            this.Alert(result == true ? "操作成功" : "操作失败", "GroupSchemesList.aspx?acttype=2");
        }

        private void BindData()
        {
            GroupSchemesEntity entity = new GroupSchemesBLL().GetSingle(this.SchemeID, this.GroupID);

            this.txtSchemeID.Text = entity.SchemeID.ToString();
            this.txtGroupID.Text = entity.GroupID.ToString();
            this.txtGroupTypeID.Text = entity.GroupTypeID.ToString();
            this.txtOrderType.Text = entity.OrderType.ToString();
            this.ddlStatus.SelectedValue = entity.Status.ToString();
        }
    }
}