using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.BLL;
using AppStore.Common;

namespace AppStore.Web
{
    public partial class LinkInfoEdit : BasePage
    {
        public LinkInfoEntity CurrentEntity { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.Action == "Edit")
                {
                    this.BindData();
                }
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

        private void Add()
        {
            LinkInfoEntity entity = new LinkInfoEntity()
            {
                //站点名称
                LinkName = this.SiteName.Text.Trim(),
                //显示名称
                ShowName = this.ShowName.Text.Trim(),
                //开发者ID
                CPID = this.DevID.Value.Trim().Convert<int>(1),
                //开发者名称
                DevName = this.CPName.Value.Trim(),
                //实际URL地址
                LinkUrl = this.LinkUrl.Text.Trim(),
                //合作类型，例如：1=CPC，2=CPL...
                CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                //外链标签，例如：1=独家，2=首发，4=...位运算
                LinkTag = this.LinkTag.SelectedValue.Convert<int>(0),
                //外链描述
                LinkDesc = this.LinkDesc.Text.Trim(),
                //ICON URL
                IconUrl = this.IconPicUrl.Value.Trim(),
                //状态，定义：1=正常，2=禁用
                Status = this.Status.SelectedValue.Convert<int>(0),
                //备注
                Remarks = this.Remarks.Text.Trim(),
                //运营状态，定义：1=正常，2=禁用
                //OpStatus = 1
            };

            if (new LinkInfoBLL().Insert(entity))
            {
                Response.Redirect("LinkInfoList.aspx");
            }
            else
            {
                this.Alert("添加失败");
            }
        }

        private void Edit()
        {
            LinkInfoEntity entity = new LinkInfoEntity()
            {
                LinkID = this.Id,
                //站点名称
                LinkName = this.SiteName.Text.Trim(),
                //显示名称
                ShowName = this.ShowName.Text.Trim(),
                //开发者ID
                CPID = this.DevID.Value.Trim().Convert<int>(1),
                //开发者名称
                DevName = this.CPName.Value.Trim(),
                //实际URL地址
                LinkUrl = this.LinkUrl.Text.Trim(),
                //合作类型，例如：1=CPC，2=CPL...
                CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                //外链标签，例如：1=独家，2=首发，4=...位运算
                LinkTag = this.LinkTag.SelectedValue.Convert<int>(0),
                //外链描述
                LinkDesc = this.LinkDesc.Text.Trim(),
                //ICON URL
                IconUrl = this.IconPicUrl.Value.Trim(),
                //状态，定义：1=正常，2=禁用
                Status = this.Status.SelectedValue.Convert<int>(0),
                //备注
                Remarks = this.Remarks.Text.Trim(),
                //运营状态，定义：1=正常，2=禁用
                //OpStatus = this.Status.SelectedValue.Convert<int>(0)
            };

            if (new LinkInfoBLL().Update(entity))
            {
                Response.Redirect("LinkInfoList.aspx");
            }
            else
            {
                this.Alert("修改失败");
            }
        }

        private void BindData()
        {
            if (this.Action == "Edit")
            {
                this.CurrentEntity = new LinkInfoBLL().GetSingle(this.Id);

                if (CurrentEntity != null)
                {
                    this.SiteName.Text = this.CurrentEntity.LinkName;
                    this.ShowName.Text = this.CurrentEntity.ShowName;
                    this.DevID.Value = this.CurrentEntity.CPID.ToString();
                    this.DevName.Text = this.CurrentEntity.DevName.ToString();
                    this.CPName.Value = this.CurrentEntity.DevName.ToString();
                    this.LinkUrl.Text = this.CurrentEntity.LinkUrl;
                    this.CoopType.SelectedValue = this.CurrentEntity.CoopType.ToString();
                    this.LinkTag.SelectedValue = this.CurrentEntity.LinkTag.ToString();
                    this.LinkDesc.Text = this.CurrentEntity.LinkDesc.ToString();
                    this.Remarks.Text = this.CurrentEntity.Remarks;
                    this.Status.SelectedValue = this.CurrentEntity.Status.ToString();
                    this.IconPicUrl.Value = this.CurrentEntity.IconUrl;
                }
                else
                {
                    this.Alert("参数非法");
                }
            }
        }
    }
}