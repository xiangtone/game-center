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
    public partial class GroupInfo : BasePage
    {

        public int SchemeID { get { return this.Request<int>("SchemeID", 0); } }
        public string PageType { get { return this.Request<string>("page", ""); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            BindData();
            BindDataLog();
        }

        private void BindData()
        {
            try
            {
                int totalCount = 0;
                string selectStatus = dropStatus.SelectedValue;

                GroupInfoEntity groupInfo = new GroupInfoEntity()
                {
                    SearchGroupType = this.GroupType.SelectedValue,
                    SearchKeys = this.Keyword_2.Value.Trim(),
                    SearchOrderType = OrderType.SelectedValue,
                    SearchStatus = dropStatus.SelectedValue,
                    StartIndex = pagerList.StartRecordIndex - 1,
                    EndIndex = pagerList.PageSize,
                    Status = 1
                };
                List<GroupInfoEntity> list = new GroupInfoBll().GetDataList(groupInfo, ref totalCount, SchemeID);
                list = list.OrderByDescending(a => a.UpdateTime).OrderByDescending(a => a.Status).ToList();
                for (int i = 0; i < list.Count; i++)
                {
                    if (list[i].GroupPicUrl == "")
                    {
                        list[i].GroupPicUrl = "http://cos.myqcloud.com/1002877/nwfs/M00/02/ED/Co9ZBlPE_uaEAKpAAAAAAC-RJBY895.jpg";
                    }
                }
                this.objRepeater.DataSource = list;
                this.objRepeater.DataBind();

                pagerList.RecordCount = totalCount;
                pagerList.DataBind();
            }
            catch (Exception)
            {

                throw;
            }
        }

        protected void pagerList_PageChanged(object sender, EventArgs e)
        {
            BindData();
        }
        protected void btnSearch_Click(object sender, EventArgs e)
        {
            BindData();
        }
        private void BindDataLog()
        {
            try
            {
                if (PageType == "new")
                {
                    //Button1.Visible = true;
                    int totalCount = 0;
                    int StartIndex = AspNetPager1.StartRecordIndex - 1;
                    int EndIndex = AspNetPager1.PageSize;
                    int type = 62;
                    List<OperateRecordEntity> list = new OperateRecordBLL().GetDataList(StartIndex, EndIndex, type, ref totalCount);
                    if (totalCount == 0)
                    {
                        BtnExportLog.Visible = false;
                    }
                    this.Repeater1.DataSource = list;
                    this.Repeater1.DataBind();
                    AspNetPager1.RecordCount = totalCount;
                    AspNetPager1.DataBind();
                }
            }
            catch (Exception)
            {

                throw;
            }
        }
        protected void BtnExportLog_Click(object sender, EventArgs e)
        {
            try
            {
                DataSet dataset = new DataSet();
                DataTable datatable = new DataTable();
                dataset = new OperateRecordBLL().GetDataList(62);
                datatable = dataset.Tables[0];
                string fileName = string.Format("GroupInfoLog_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                WriteExcel(datatable, path, fileName);
                DownloadFile(path);
            }
            catch (Exception)
            {
                throw;
            }
        }
        protected void AspNetPager1_PageChanged(object sender, EventArgs e)
        {
            BindDataLog();
            BindData();
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
            try
            {
                int totalCount = 0;
                string selectStatus = dropStatus.SelectedValue;

                GroupInfoEntity groupInfo = new GroupInfoEntity()
                {
                    SearchGroupType = this.GroupType.SelectedValue,
                    SearchKeys = this.Keyword_2.Value.Trim(),
                    SearchOrderType = OrderType.SelectedValue,
                    SearchStatus = dropStatus.SelectedValue,
                    StartIndex = pagerList.StartRecordIndex - 1,
                    EndIndex = pagerList.PageSize,
                    Status = 1
                };
                DataTable datatable = new DataTable();
                DataSet dt = new GroupInfoBll().GetDataSetList(groupInfo, SchemeID);
                datatable = dt.Tables[0];
                string fileName = string.Format("GroupInfo_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                WriteExcel(datatable, path, fileName);
                DownloadFile(path);
            }
            catch (Exception)
            {
                throw;
            }
        }
    }
}