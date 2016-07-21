using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class CpslEdit : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                var a = nwbase_utils.Tools.GetRequestVal("action", "");
                if (nwbase_utils.Tools.GetRequestVal("action", "") == "update")
                {
                    Bind(Convert.ToInt32(nwbase_utils.Tools.GetRequestVal("id", "")));
                }
            }
        }
        /// <summary>
        /// 绑定修改信息
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Bind(int ID)
        {
            CPsEntity entity = new CPsBLL().SelectByNo(ID);
            if (entity != null)
            {
                hidID.Value = entity.CPID.ToString();
                drpCPType.SelectedValue = entity.CPType.ToString();
                drpIsDeveloper.SelectedValue = entity.IsDeveloper.ToString();
                txtCpsName.Text = entity.CPName;
                txtFullName.Text = entity.FullName;
            }
        }
        /// <summary>
        /// 新增信息
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void Button1_Click(object sender, EventArgs e)
        {
            CPsEntity entity = new CPsEntity()
            {
                CPID = Convert.ToInt32(hidID.Value),
                CPType = Convert.ToInt32(drpCPType.SelectedValue),
                IsDeveloper = Convert.ToInt32(drpIsDeveloper.SelectedValue),
                CPName = txtCpsName.Text,
                FullName = txtFullName.Text,
                Status = 1,
                Remarks = "",
                CreateTime = DateTime.Now,
                UpdateTime = DateTime.Now,
            };
            if (Convert.ToInt32(hidID.Value) > 0)
            {

                bool rult = new CPsBLL().Update(entity);
                if (rult)
                {
                    Response.Redirect("CPsList.aspx");
                }
                else
                {
                    Response.Write("修改失败");
                }
            }
            else
            {
                int rult = new CPsBLL().Insert(entity);
                if (rult > 0)
                {
                    Response.Redirect("CPsList.aspx");
                }
                else
                {
                    Response.Write("添加失败");
                }
            }
        }
    }
}