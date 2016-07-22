using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using System.Data;

namespace AppStore.Web
{
    public partial class FeedBack : BasePage
    {
        public List<FeedBackEntity> FeedBackList;
        public int ClientId { get { return this.Request<int>("ClientId", 0); } }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
            }
        }

        public void Bind()
        {
            int totalCount = 0;
            int StartIndex = pagerList.StartRecordIndex - 1;
            int EndIndex = pagerList.PageSize;
            FeedBackList = new FeedBackBLL().GetFeedBackList(ClientId, StartIndex, EndIndex, ref totalCount);
            pagerList.RecordCount = totalCount;
            pagerList.DataBind();
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            int id = int.Parse(HidFBID.Value, 0);
            string remarks = txtRemarks.Text.Trim();
            if (id > 0)
            {
                new FeedBackBLL().UpdateRemarks(id, remarks);
                Bind();
            }
        }
        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            Bind();
        }
    }


}