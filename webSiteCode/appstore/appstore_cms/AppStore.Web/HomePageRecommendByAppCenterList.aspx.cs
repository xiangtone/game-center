using System;
using System.Collections.Generic;
using System.Linq;
using System.Web.UI.WebControls;
using System.Web.Services;

using AppStore.BLL;
using AppStore.Model;
using nwbase_sdk;
using AppStore.Common;
using System.Data;
using System.IO;
using System.Text;
using System.Threading;
using System.Web;

namespace AppStore.Web
{
    public partial class HomePageRecommendByAppCenterList : BasePage
    {
        /// <summary>
        /// 首页推荐全部数据
        /// </summary>
        public List<GroupElemsEntity> HomePageRecommList { get; set; }

        /// <summary>
        /// 首页轮播数据
        /// </summary>
        public List<GroupElemsEntity> FlashRecommList { get; set; }

        /// <summary>
        /// 固定推荐位数据
        /// </summary>
        public List<GroupElemsEntity> FixedRecommList { get; set; }

        /// <summary>
        /// 随机推荐位数据
        /// </summary>
        public List<GroupElemsEntity> RandomRecommList { get; set; }

        public string ActType { get { return this.Request<string>("acttype", "4102,1"); } }

        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int GroupTypeID { get { return this.ActType.Split(',')[0].Convert<int>(0); } }

        /// <summary>
        /// 方案ID，用于区分应用中心和游戏中心的数据
        /// </summary>
        public int SchemeID { get { return this.ActType.Split(',')[1].Convert<int>(0); } }
        public string PageType { get { return this.Request<string>("page", ""); } }



        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                BindData();
                BindDataLog();
            }
        }

        public void BindData()
        {
            this.HomePageRecommList = new GroupBLL().GetHomePageRecommend(this.GroupTypeID, this.SchemeID);

            if (this.HomePageRecommList != null)
            {
                this.FlashRecommList = this.HomePageRecommList.Where(p => p.PosID == 1).ToList();
                this.FixedRecommList = this.HomePageRecommList.Where(p => p.PosID >= 2 && p.PosID <= 3).ToList();
                this.RandomRecommList = this.HomePageRecommList.Where(p => p.PosID > 1).ToList();


                if (this.FlashRecommList.Count == 0)
                {
                    this.FlashRecommList = new List<GroupElemsEntity>();
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 2, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 3, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 4, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FlashRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 1, ElemType = 1, OrderNo = 5, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                }

                if (this.FixedRecommList.Count < 2)
                {
                    this.FixedRecommList = new List<GroupElemsEntity>();


                    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 2, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                    this.FixedRecommList.Add(new GroupElemsEntity() { GroupID = new GroupBLL().HomePageRecommendGetGroupId(this.GroupTypeID, this.SchemeID), PosID = 3, ElemType = 1, ElemID = 11101, RecommTitle = "Banner推荐位", RecommPicUrl = string.Format("{0}", "Images/configapp.png") });
                }

                for (int i = 0; i < this.RandomRecommList.Count; i++)
                {
                    this.RandomRecommList[i].PosID = i + 2;
                }

                //DataList.DataSource = this.RandomRecommList;
                //DataList.DataBind();
            }
        }

        protected string BindStatus(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.EndTime < currentTime)
            {
                return "<span class=\"red\">已过期</span>";
            }
            else if (obj.StartTime > currentTime)
            {
                var timeSpan = obj.StartTime - currentTime;

                return string.Format("<span class=\"blue\">即将启用</span>");
            }
            else
            {
                return "<span class=\"black\">开启</span>";
            }
        }

        protected string BindType(object id)
        {
            string str = "";
            if (id != null && id.ToString() != "")
            {
                int appid = int.Parse(id.ToString());
                AppInfoEntity entity = new AppInfoBLL().GetSingle2(appid);
                if (entity != null)
                {
                    str = entity.AppTypeName;
                }
            }
            return str;
        }
        /// <summary>
        /// 绑定推荐标签
        /// </summary>
        /// <param name="tag"></param>
        /// <returns></returns>
        public string BindTag(object tag)
        {
            Dictionary<int, string> dc = new Dictionary<int, string>();
            dc.Add(1, "官方");
            dc.Add(2, "推荐");
            dc.Add(4, "首发");
            dc.Add(8, "免费");
            dc.Add(16, "礼包");
            dc.Add(32, "活动");
            dc.Add(64, "内测");
            dc.Add(128, "热门");
            string str = "";
            if (tag != null && tag.ToString() != "")
            {
                int t = int.Parse(tag.ToString());
                foreach (var item in dc)
                {
                    if ((item.Key & t) >0 )
                    {
                        str += item.Value + ",";
                    }
                }
            }
            str = str.TrimEnd(',');
            return str;
        }


        protected string BindTime(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            return timePart;
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
        /// <summary>
        /// 同步缓存
        /// </summary>
        protected void Button1_Click(object sender, EventArgs e)
        {
            bool result = new SyncManagerBLL().NewRedis();

            if (result.Equals(true))
            {
                string msg = "数据生效成功";
                result = new SyncManagerBLL().EffectiveSync();
                if (!result)
                {
                    msg += "，但通知失败";
                }
                this.Alert(msg);
            }
            else
            {
                this.Alert("数据生效失败");
            }

            BindData();
        }

        protected void Button2_Click(object sender, EventArgs e)
        {
            try
            {
                DataSet dataset = new GroupBLL().GetHomePageRecommendExcel(this.GroupTypeID, this.SchemeID);
                DataTable datatable = dataset.Tables[0];
                //string path = Server.MapPath("/Export/GameInfo.xls");
                string fileName = string.Format("Home_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
                string path = Server.MapPath(ServerMapPath() + fileName);
                //if (!File.Exists(path))
                //File.Create(path);
                WritetableExcel(datatable, path, fileName);
                DownloadFile(path);
                BindData();
            }
            catch (Exception)
            {
                BindData();
                throw;
            }
        }

        private string GetRootURI()
        {
            string AppPath = "";
            HttpContext HttpCurrent = HttpContext.Current;
            HttpRequest Req;
            if (HttpCurrent != null)
            {
                Req = HttpCurrent.Request;
                string UrlAuthority = Req.Url.GetLeftPart(UriPartial.Authority);
                if (Req.ApplicationPath == null || Req.ApplicationPath == "/")
                    //直接安装在   Web   站点   
                    AppPath = UrlAuthority;
                else
                    //安装在虚拟子目录下   
                    AppPath = UrlAuthority + Req.ApplicationPath;
            }
            return AppPath;
        }

        /// <summary>
        /// 导出报表
        /// </summary>
        /// <param name="ds"></param>
        /// <param name="path"></param>
        /// <param name="fileName"></param>
        public void WritetableExcel(DataTable ds, string path, string fileName)
        {
            try
            {
                FindPath(path);
                StreamWriter sw = new StreamWriter(path, false, Encoding.GetEncoding("gb2312"));
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < ds.Columns.Count; k++)
                {
                    if (ds.Columns[k].ColumnName.ToString() != "ShowType" && ds.Columns[k].ColumnName.ToString() != "RecommTitle")
                    {
                        sb.Append(ds.Columns[k].ColumnName.ToString() + "\t");
                    }
                }
                sb.Append(Environment.NewLine);
                int a = 0;
                int b = 0;
                int c = 0;
                for (int i = 0; i < ds.Rows.Count; i++)
                {
                    for (int j = 0; j < ds.Columns.Count; j++)
                    {
                        if (j != 1 && j != 11)
                        {
                            if (j == 0)
                            {
                                if (ds.Rows[i][j].ToString() == "1")
                                {
                                    a += 1;
                                    sb.Append("Banner" + (a) + "\t");
                                }
                                else
                                {
                                    if (ds.Rows[i][1].ToString() == "1")
                                    {
                                        c += 1;
                                        sb.Append("广告位" + (c) + "\t");
                                    }
                                    else
                                    {
                                        b += 1;
                                        sb.Append("推荐位" + (b) + "\t");
                                    }

                                }
                            }
                            else if (j == 2)
                            {
                                //sb.Append(ds.Rows[i][j] + "\t");
                                sb.Append(BindElemType(ds.Rows[i][j]) + "\t");
                            }
                            else if (j == 3)
                            {
                                if (ds.Rows[i][2].ToString() != "1")
                                {
                                    sb.Append(ds.Rows[i][11] + "\t");
                                }
                                else
                                {
                                    sb.Append(ds.Rows[i][j].ToString() + "\t");
                                }

                            }
                            else if (j == 9)
                            {
                                sb.Append(BindStatus2(ds.Rows[i][j]) + "\t");
                            }
                            else if (j == 10)
                            {
                                sb.Append(BindPermission(ds.Rows[i][j].ToString(), 0) + "\t");
                            }
                            else if (j == 6)
                            {
                                sb.Append(BindType(ds.Rows[i][j]) + "\t");
                            }
                            else if (j == 7)
                            {
                                sb.Append(BindCooType(ds.Rows[i][j]) + "\t");
                            }
                            else
                            {
                                sb.Append(ds.Rows[i][j].ToString() + "\t");
                            }
                        }

                    }
                    sb.Append(Environment.NewLine);
                }
                sw.Write(sb.ToString());
                sw.Flush();
                sw.Close();
                sw.Dispose();
            }
            catch (Exception)
            {
                throw;
            }
        }
        /// <summary>
        /// 绑定跳转类型
        /// </summary>
        /// <param name="p"></param>
        /// <returns></returns>
        private string BindElemType(object p)
        {
            string val = "";
            if (p != null && p.ToString() != "")
            {
                int type = int.Parse(p.ToString());
                if (type == 1)
                {
                    val = "跳转至游戏";
                }
                else if (type == 2)
                {
                    val = "跳转至指定链接";
                }
                else if (type == 3)
                {
                    val = "跳转至指定分类";
                }
                else if (type == 4)
                {
                    val = "跳转至网游或单机";
                }
                else if (type == 5)
                {
                    val = "跳转至指定专题";
                }
            }
            return val;
        }
        /// <summary>
        /// 绑定游戏调用权限
        /// </summary>
        /// <param name="per"></param>
        /// <param name="id"></param>
        /// <returns></returns>
        public string BindPermission(string per, object id)
        {
            string str = "";
            if (per != null && per.ToString() != "")
            {
                if (per.Trim().Length > 0)
                {
                    str = GetAndroidPermission(per);
                }
            }

            return str;
        }
        /// <summary>
        /// 绑定合作类型
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindCooType(object val)
        {
            string status_val = "";
            if (val != null && val.ToString() != "")
            {
                int Status = Convert.ToInt32(val);
                if (Status == 1)
                {
                    status_val = "联运";
                }
                else if (Status == 2)
                {
                    status_val = "CPS";
                }
                else if (Status == 3)
                {
                    status_val = "CPA";
                }
                else if (Status == 99)
                {
                    status_val = "未合作";
                }
            }
            return status_val;
        }
        /// <summary>
        /// 判定状态显示
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindStatus2(object val)
        {
            string status_val = "";
            if (val != null && val.ToString() != "")
            {
                int Status = Convert.ToInt32(val);

                if (Status == 1)
                {
                    status_val = "正常";
                }
                else if (Status == 2)
                {
                    status_val = "禁用";
                }
                else
                {
                    status_val = val.ToString();
                }
            }
            return status_val;
        }
        protected void AspNetPager1_PageChanged(object sender, EventArgs e)
        {
            BindDataLog();
            BindData();
        }
        private void BindDataLog()
        {
            int totalCount = 0;

            int StartIndex = AspNetPager1.StartRecordIndex - 1;
            int EndIndex = AspNetPager1.PageSize;
            int type = 60;

            List<OperateRecordEntity> list = new OperateRecordBLL().GetDataList(StartIndex, EndIndex, type, ref totalCount);
            this.Repeater1.DataSource = list;
            this.Repeater1.DataBind();
            AspNetPager1.RecordCount = totalCount;
            AspNetPager1.DataBind();
        }
        protected void BtnExportLog_Click(object sender, EventArgs e)
        {
            DataSet dataset = new DataSet();
            DataTable datatable = new DataTable();
            dataset = new OperateRecordBLL().GetDataList(60);
            datatable = dataset.Tables[0];
            string fileName = string.Format("HomeLog_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
            string path = Server.MapPath(ServerMapPath() + fileName);
            base.WriteExcel(datatable, path, fileName);
            DownloadFile(path);
        }
    }
}