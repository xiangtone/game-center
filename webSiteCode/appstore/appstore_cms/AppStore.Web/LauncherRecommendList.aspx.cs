using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Web.Services;


using AppStore.BLL;
using AppStore.Model;
using nwbase_sdk;
using AppStore.Common;
using System.Data;

namespace AppStore.Web
{
    public partial class LauncherRecommendList : BasePage
    {
        /// <summary>
        /// 桌面精品推荐数据
        /// </summary>
        public List<GroupElemsEntity> LauncherRecommList { get; set; }

        public string ActType { get { return this.Request<string>("acttype", "4102,1"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.ActType.Split(',')[0].Convert<int>(0); } }


        public string PageType { get { return this.Request<string>("page", ""); } }
        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.ActType.Split(',')[1].Convert<int>(0); } }




        protected void Page_Load(object sender, EventArgs e)
        {

            if (!IsPostBack)
            {
                InitPage();
                BindData();
                BindDataLog();

            }
        }
        public string[] SubStr(string data)
        {
            return data.Substring(0, data.Length - 1).Split(',');
        }
        private void InitPage()
        {
            BindNavName();
        }

        private void BindNavName()
        {
            string navName = "推荐";
            switch (GroupTypeID)
            {
                case 4107:
                    navName = "应用游戏精品推荐";
                    break;
                case 4108:
                    if (PageType == "new")
                    {
                        navName = "热门游戏管理";
                    }
                    else
                    {
                        navName = "精品游戏推荐";
                    }
                    break;
                case 5101:
                    navName = "桌面精品推荐";
                    break;
            }

            literalNavName.Text = navName;
        }

        public void BindData()
        {
            this.LauncherRecommList = new GroupBLL().GetLauncherRecommend(SchemeID, GroupTypeID);
            DataList.DataSource = this.LauncherRecommList;
            DataList.DataBind();
            if (PageType == "new" && LauncherRecommList.Count > 0)
            {
                Button1.Visible = true;
            }
        }


        [WebMethod]
        public static void UpdateOrder(string orderinfo)
        {
            Dictionary<int, int> items = new Dictionary<int, int>();
            foreach (string eachInfo in orderinfo.Split(','))
            {
                if (string.IsNullOrEmpty(eachInfo))
                    break;
                int appId = Tools.GetInt(eachInfo.Split(':')[0], 0);
                int order = Tools.GetInt(eachInfo.Split(':')[1], 0);
                items.Add(appId, order);
            }
            new GroupBLL().UpdateElemPos(items);

        }

        [WebMethod]
        public static void DeleteElem(string elemId, int SchemeID, int GroupTypeID, string username)
        {
            new GroupBLL().DeleteElem(Convert.ToInt32(elemId));
            new LauncherRecommendList().AddLog(Convert.ToInt32(elemId), SchemeID, GroupTypeID, username);

        }
        public void AddLog(int elemId, int SchemeID, int GroupTypeID, string username)
        {
            if (SchemeID == 104)
            {
                if (GroupTypeID == 4108)
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = elemId,
                        reason = "",
                        Status = 1,
                        OperateFlag = "3",
                        SourcePage = 65,
                        OperateType = "9",
                        OperateExplain = "热门游戏管理: 删除游戏",
                        OperateContent = new GroupBLL().GetGroupElemByID(elemId).RecommTitle,
                        UserName = username,
                    };
                    new OperateRecordBLL().Insert(info);
                }
            }
        }
        [WebMethod]
        public static void UpdateOrderNo(string orderinfo)
        {
            //修改OrderNO
            Dictionary<int, int> items = new Dictionary<int, int>();
            foreach (string eachInfo in orderinfo.Split(','))
            {
                if (string.IsNullOrEmpty(eachInfo))
                    break;
                int appId = Tools.GetInt(eachInfo.Split(':')[0], 0);
                int order = Tools.GetInt(eachInfo.Split(':')[1], 0);
                items.Add(appId, order);
            }
            new GroupBLL().UpdateElemPos(items);
        }
        private void BindDataLog()
        {
            try
            {
                if (PageType == "new" && GroupTypeID == 4108)
                {
                    int totalCount = 0;
                    int StartIndex = AspNetPager1.StartRecordIndex - 1;
                    int EndIndex = AspNetPager1.PageSize;
                    int type = 65;
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
                dataset = new OperateRecordBLL().GetDataList(65);
                datatable = dataset.Tables[0];
                string fileName = string.Format("GameHotLog_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
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
            //ExcelExport(DataList, "GameHotInfo");
            try
            {
                DataSet dataset = new DataSet();
                DataTable datatable = new DataTable();
                dataset = new GroupBLL().GetDataSetLauncherRecommend(SchemeID, GroupTypeID);
                datatable = dataset.Tables[0];
                string fileName = string.Format("GameHotInfo_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                WriteAppInfoExcel(datatable, path, fileName);
                DownloadFile(path);
            }
            catch (Exception)
            {
                throw;
            }
        }
    }
}