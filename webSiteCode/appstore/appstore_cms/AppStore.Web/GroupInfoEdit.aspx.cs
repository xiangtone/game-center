using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using AppStore.Web.WebReference;
using System.Drawing;
using System.IO;

namespace AppStore.Web
{
    public partial class GroupInfoEdit : BasePage
    {
        public string PageType { get { return this.Request<string>("PageType", ""); } }
        public int GroupID { get { return this.Request<int>("GroupID", -1); } }
        public double X1 { get; set; }
        public double X2 { get; set; }
        public double Y1 { get; set; }
        public double Y2 { get; set; }
        public double Width { get; set; }
        public double Height { get; set; }
        public GroupInfoEntity CurrentEntity { get; set; }
        // 导航条显示状态（新增还是编辑）
        public string NavShowStatus { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                BindGroupType();
                // 新增页面
                if (this.PageType == "addGroup")
                {
                    // 导航显示
                    this.NavShowStatus = "新增";
                }
                // 编辑页面
                if (this.PageType == "editGroup")
                {
                    this.NavShowStatus = "修改";
                    BindData();
                }
                // 有误
                if(this.PageType == "")
                {
                    this.Alert("参数非法", "/GroupInfo.aspx");
                }
            }
        }

        /// <summary>
        /// 绑定应用类型
        /// </summary>
        private void BindGroupType()
        {
            List<GroupTypeEntity> list = new GroupTypeBLL().QueryGroupType();
            ListItem objItem = new ListItem("分类筛选", "");
            this.dropGroupType.Items.Add(objItem);
            foreach (GroupTypeEntity item in list)
            {
                this.dropGroupType.Items.Add(new ListItem(item.TypeName, item.TypeID.ToString()));
            }
        }

        /// <summary>
        /// 编辑时绑定分组信息
        /// </summary>
        private void BindData()
        {
            if (GroupID.Equals(-1))
            {
                this.Alert("参数非法", "/GroupInfo.aspx");
            }
            else
            {
                CurrentEntity = new GroupInfoBll().QueryGroupInfoByGroupID(this.GroupID);
                this.GroupName.Text = CurrentEntity.GroupName;
                this.dropOrderType.SelectedValue = CurrentEntity.OrderType.ToString();
                this.dropGroupType.SelectedValue = CurrentEntity.GroupTypeID.ToString();
                this.RecommWord.Text = CurrentEntity.GroupTips;
                this.Status.SelectedValue = CurrentEntity.Status.ToString();
                this.Remarks.Text = CurrentEntity.Remarks;
                this.GroupDesc.Text = CurrentEntity.GroupDesc;
                this.StartTime.Text = CurrentEntity.StartTime.ToString();
                this.EndTime.Text = CurrentEntity.EndTime.ToString();

                // 显示图片
                this.ShowThumbPic.ImageUrl = string.IsNullOrEmpty(CurrentEntity.GroupPicUrl) ? @"Theme/Images/empty.png" : CurrentEntity.GroupPicUrl;
                this.ThumbPicUrl.Value = CurrentEntity.GroupPicUrl;
                this.OldThumbPicUrl.Value = CurrentEntity.GroupPicUrl;
            }
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            if (this.PageType == "addGroup")
            {
                Insert();
            }
            if (this.PageType == "editGroup")
            {
                Update();
            }
        }

        /// <summary>
        /// 修改数据
        /// </summary>
        private void Update()
        {
            GroupInfoEntity entity = new GroupInfoEntity();
           
            entity.GroupName = this.GroupName.Text;
            entity.OrderType = this.dropOrderType.SelectedValue.Convert<int>(1);
            entity.GroupTypeID = this.dropGroupType.SelectedValue.Convert<int>(1);
            entity.GroupTips = this.RecommWord.Text;
            entity.Status = this.Status.SelectedValue.Convert<int>(1);
            entity.Remarks = this.Remarks.Text;
            entity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            entity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            entity.GroupDesc = this.GroupDesc.Text;
            entity.GroupPicUrl = this.ThumbPicUrl.Value;
            entity.GroupID = this.GroupID;

            #region 处理缩略图
            if (this.OldThumbPicUrl.Value != this.ThumbPicUrl.Value)
            {
                if (!string.IsNullOrEmpty(entity.GroupPicUrl))
                {
                    if (this.Width != 0 && this.Height != 0)
                    {

                        //裁剪方式
                        string croptype = this.Request<string>("cropType", string.Empty);

                        Bitmap bitSource = ImageHelper.GetBitmapFromUrl(entity.GroupPicUrl);

                        if (bitSource == null)
                        {
                            this.Alert("远程图片解析失败，请刷新后再试");
                            return;
                        }

                        int rX = Convert.ToInt32(Math.Round(this.X1, MidpointRounding.AwayFromZero));
                        int rY = Convert.ToInt32(Math.Round(this.Y1, MidpointRounding.AwayFromZero));
                        int rW = Convert.ToInt32(Math.Round(this.Width, MidpointRounding.AwayFromZero));
                        int rH = Convert.ToInt32(Math.Round(this.Height, MidpointRounding.AwayFromZero));

                        bitSource = ImageHelper.KiCut(bitSource, rX, rY, rW, rH);

                        if (croptype == "hengping")
                        {
                            bitSource = ImageHelper.ImageCompress(bitSource, 260, 195);
                        }
                        else
                        {
                            bitSource = ImageHelper.ImageCompress(bitSource, 195, 260);
                        }

                        UploadFile up = new UploadFile();

                        byte[] imageBytes = BitmapToBytes(bitSource);
                        //StartTransfer中的AppID和CID在前端页面中上传控件定义
                        string token = up.StartTransfer(2, 41, entity.GroupPicUrl.Substring(entity.GroupPicUrl.LastIndexOf("."), 4), 1, imageBytes.Length, string.Empty);
                        string resultString = up.Transfer(token, imageBytes, 1);

                        entity.GroupPicUrl = resultString.Split(',')[1];

                        //up.GenerateThumb(entity.GroupPicUrl, 0);
                        up.GenerateThumb(token);

                        bitSource.Dispose();
                    }
                }
            }
            #endregion

            //写入数据库
            bool result = new GroupInfoBll().UpdateGroupInfo(entity);
            if (result)
            {
                this.Alert("操作成功", string.Format("/GroupInfo.aspx"));
            }
            else
            {
                this.Alert("操作失败");
            }
        }

        /// <summary>
        /// 插入数据
        /// </summary>
        private void Insert()
        {
            GroupInfoEntity entity = new GroupInfoEntity();

            entity.GroupName = this.GroupName.Text;
            entity.OrderType = this.dropOrderType.SelectedValue.Convert<int>(1);
            entity.GroupTypeID = this.dropGroupType.SelectedValue.Convert<int>(1);
            entity.GroupTips = this.RecommWord.Text;
            entity.Status = this.Status.SelectedValue.Convert<int>(1);
            entity.Remarks = this.Remarks.Text;
            entity.StartTime = this.StartTime.Text.Trim().Convert<DateTime>(DateTime.Now);
            entity.EndTime = this.EndTime.Text.Trim().Convert<DateTime>(DateTime.Now.AddDays(7));
            entity.GroupDesc = this.GroupDesc.Text;
            entity.GroupPicUrl = this.ThumbPicUrl.Value;
            

            //写入数据库
            bool result = new GroupInfoBll().InsertGroupInfo(entity);
            if (result)
            {
                this.Alert("操作成功", string.Format("/GroupInfo.aspx"));
            }
            else
            {
                this.Alert("操作失败");
            }
        }

        public static byte[] BitmapToBytes(Bitmap bmp)
        {
            using (MemoryStream ms = new MemoryStream())
            {
                bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Jpeg);
                byte[] bytes = ms.GetBuffer();
                return bytes;
            }
        }
    }
}