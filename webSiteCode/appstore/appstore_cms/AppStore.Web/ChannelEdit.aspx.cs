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
    public partial class ChannelEdit : BasePage
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
        /// <param name="id"></param>
        public void Bind(int id)
        {
            ChannelEntity entity = new ChannelBLL().SelectByNo(id);
            if (entity != null)
            {
                txtChannelNO.Text = entity.ChannelNO.ToString();
                txtChannelNO.Enabled = false;
                txtChannelName.Text = entity.ChannelName;
                txtChannelFlag.Text = entity.ChannelFlag;
                hidID.Value = entity.ChannelNO.ToString();
            }
        }
        /// <summary>
        /// 新增渠道信息
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void Button1_Click(object sender, EventArgs e)
        {
            ChannelEntity entity = new ChannelEntity()
            {
                ChannelNO = Convert.ToInt32(txtChannelNO.Text),
                ChannelName = txtChannelName.Text,
                ChannelFlag = txtChannelFlag.Text,
                Status = 1,
                Remarks = "",
                CreateTime = DateTime.Now,
                UpdateTime = DateTime.Now,
            };
            if (Convert.ToInt32(hidID.Value) > 0)
            {

                bool rult = new ChannelBLL().Update(entity);
                if (rult)
                {
                    Response.Redirect("ChannelList.aspx");
                }
                else
                {
                    Response.Write("修改失败");
                }
            }
            else
            {
                int rult = new ChannelBLL().Insert(entity);
                if (rult > 0)
                {
                    Response.Redirect("ChannelList.aspx");
                }
                else
                {
                    Response.Write("添加失败");
                }
            }
        }
    }
}