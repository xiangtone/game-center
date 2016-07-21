using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using System.Data;

namespace AppStore.Web
{
    public partial class GroupElement : BasePage
    {
        public int GroupID { get { return this.Request<int>("GroupID", 0); } }
        public int SchemeID { get { return this.Request<int>("SchemeID", 0); } }
        public string PageType { get { return this.Request<string>("page", ""); } }
        public string GroupName { get { return this.Request<string>("GroupName", ""); } }
        public string type { get { return this.Request<string>("type", ""); } }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
                BindDataLog();
            }
            ////QueryGroupNameByID();
        }
        private void Bind()
        {
            List<GroupElemsEntity> list = new GroupElemsBLL().QueryGroupElementByGroupID(GroupID);
            DataList1.DataSource = list;
            DataList1.DataBind();
            if (PageType == "new")
            {
                Button1.Visible = true;
            }
        }

        /// <summary>
        /// 根据分组ID查询分组名称（绑定到导航）
        /// </summary>
        //private void QueryGroupNameByID()
        //{
        //    this.GroupName = new GroupInfoBll().QueryGroupNameByID(GroupID);
        //}

        protected string BindTimeStatus(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            DateTime currentTime = DateTime.Now;
            if (obj.StartTime > currentTime || obj.EndTime < currentTime)
            {
                return "<span class=\"red\">不在有效日期  &nbsp; &nbsp; | &nbsp;  &nbsp; 位置编号：" + obj.PosID + "</span> ";
            }
            else if (obj.EndTime.AddHours(-24) < currentTime)
            {
                return "<span class=\"blue\">启用中  &nbsp; &nbsp; | &nbsp;  &nbsp; 位置编号：" + obj.PosID + "</span> ";
            }
            else
            {
                return "<span class=\"white\">启用中  &nbsp; &nbsp; | &nbsp;  &nbsp; 位置编号：" + obj.PosID + "</span> ";
            }
        }

        protected string BindTime(object entity)
        {
            GroupElemsEntity obj = (GroupElemsEntity)entity;
            string timePart = string.Format("{0:yyyy.MM.dd HH:mm} ~ {1:yyyy.MM.dd HH:mm}", obj.StartTime, obj.EndTime);
            DateTime currentTime = DateTime.Now;
            if (obj.StartTime > currentTime || obj.EndTime < currentTime)
            {
                return "<span class=\"red\">" + timePart + "</span>";
            }
            else if (obj.EndTime.AddHours(-24) < currentTime)
            {
                return "<span class=\"blue\">" + timePart + "</span>";
            }
            else
            {
                return "<span class=\"white\">" + timePart + "</span>";
            }
        }

        protected void OnDel(object s, CommandEventArgs e)
        {
            int id = nwbase_sdk.Tools.GetInt(e.CommandArgument, 0);
            if (id > 0)
            {
                var result = new GroupBLL().DeleteElem(id);
                if (result)
                {
                    if (SchemeID == 104)
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = id,
                            reason = "",
                            Status = 1,
                            OperateFlag = "3",
                            OperateContent = new GroupBLL().GetGroupElemByID(id).RecommTitle,
                            UserName = GetUserName(),
                        };
                        if (GroupID != 93 && GroupID != 94)
                        {
                            info.SourcePage = 62;
                            info.OperateType = "6";
                            info.OperateExplain = GroupName + ": 删除游戏";
                        }
                        else
                        {
                            if (GroupID == 93)
                            {
                                info.SourcePage = 63;
                                info.OperateType = "7";
                                info.OperateExplain = "游戏排行: 删除游戏";
                            }
                            else if (GroupID == 94)
                            {
                                info.SourcePage = 64;
                                info.OperateType = "8";
                                info.OperateExplain = "最新游戏: 删除游戏";
                            }
                        }
                        new OperateRecordBLL().Insert(info);
                    }
                    this.Alert("删除成功");
                    Bind();
                    BindDataLog();
                }
                else
                    this.Alert("删除失败");
            }
            else
                this.Alert("删除参数无效");
        }
        private void BindDataLog()
        {
            try
            {
                if (PageType == "new")
                {
                    int totalCount = 0;
                    int StartIndex = AspNetPager1.StartRecordIndex - 1;
                    int EndIndex = AspNetPager1.PageSize;
                    int type = 0;
                    if (GroupID == 93)
                    {
                        type = 63;
                    }
                    else if (GroupID == 94)
                    {
                        type = 64;
                    }
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
                int type = 0;
                string filename = "";
                if (GroupID == 93)
                {
                    type = 63;
                    filename = "GameRankingLog";
                }
                else if (GroupID == 94)
                {
                    type = 64;
                    filename = "GameNewest";
                }
                dataset = new OperateRecordBLL().GetDataList(type);
                datatable = dataset.Tables[0];
                string fileName = string.Format(filename + "_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
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
            Bind();
        }

        protected void Button1_Click(object sender, EventArgs e)
        {

            DataSet dataset = new DataSet();
            DataTable datatable = new DataTable();
            dataset = new GroupElemsBLL().QueryDataSetByGroupID2(GroupID);
            datatable = GetGroupInfo(GroupID);
            DataTable dt = dataset.Tables[0];
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    for (int j = 0; j < dt.Columns.Count; j++)
                    {
                        if (dt.Columns[j].ColumnName.ToString() == "PosID")
                        {
                            int posid = int.Parse(dt.Rows[i][j].ToString());
                            int id = int.Parse(dt.Rows[i][4].ToString());
                            AppInfoEntity entity = new AppInfoBLL().GetSingle(id);
                            int a = 0;
                            for (int k = 0; k < datatable.Rows.Count; k++)
                            {
                                if (int.Parse(datatable.Rows[k][datatable.Columns.Count-1].ToString()) == id)
                                {
                                    a = k;
                                }
                            }
                            if (a > 0)
                            {
                                datatable.Rows.RemoveAt(a);
                            }
                            DataRow drr = datatable.NewRow();
                            if (entity != null)
                            {
                                drr["ID"] = entity.AppID;
                                drr["游戏名"] = entity.ShowName;
                                drr["包名"] = entity.PackName;
                                drr["版本"] = entity.MainVerName;
                                AppTypeEntity type = new AppTypeBLL().GetSingle(entity.AppType);
                                if (type != null)
                                {
                                    drr["游戏分类"] = type.AppTypeName;
                                }

                                drr["合作类型"] = entity.CoopType;
                                drr["开发者"] = entity.DevName;
                                drr["状态"] = entity.Status;
                                PackInfoEntity info = new PackInfoBLL().GetSingle(entity.MainPackID);
                                if (info != null)
                                {
                                    drr["调用权限"] = info.permission;
                                }
                            }
                            //datatable.Rows.RemoveAt(posid-1);
                            datatable.Rows.InsertAt(drr, posid - 1);
                        }

                    }
                }
            }
            string fileName = string.Format("GroupInfo_{0}.xls", DateTime.Now.ToString("yyyyMMddHHmmss"));
            string path = Server.MapPath(ServerMapPath() + fileName);
            new AppInfoListNew().WriteExcel2(BaseCommon.DtSelectTop(500, datatable), path, fileName);
            DownloadFile(path);

        }

        public DataTable GetGroupInfo(int GroupID)
        {
            DataTable dt = new DataTable();

            DataSet dataset = new GroupElemsBLL().QueryDataSetByGroupID(GroupID);
            DataTable datatable = dataset.Tables[0];

            int typeId = new GroupInfoBll().QueryGroupInfoByGroupID(GroupID).GroupTypeID;
            AppInfoEntity entity = new AppInfoEntity()
               {

                   AppClass = 12,
                   AppType = typeId,
                   StartIndex = 0,
                   OrderType = "DownTimes",
                   EndIndex = 500,
                   Status = 1,
               };
            if (GroupID == 93)
            {
                entity.AppType = 0;
            }
            else if (GroupID == 94)
            {
                entity.AppType = 0;
                entity.OrderType = "CreateTime";
            }
            string status = "1";
            DataSet ds = new AppInfoBLL().GetExportDataList2(entity, status, "70");
            dt = ds.Tables[0];
            return dt;

        }
    }
}