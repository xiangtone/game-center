using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using System.Drawing;
using AppStore.Web.WebReference;
using System.IO;

namespace AppStore.Web
{
    public partial class AppInfoEdit : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public AppInfoEntity CurrentEntity { get; set; }
        public double X1 { get; set; }
        public double X2 { get; set; }
        public double Y1 { get; set; }
        public double Y2 { get; set; }
        public double Width { get; set; }
        public double Height { get; set; }
        public string AppClass { get { return this.Request<string>("AppClass", string.Empty); } }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {

                this.BindAppType();

                if (AppID.Equals(0))
                {
                    this.Alert("参数非法", "/AppInfoList.aspx");
                }
                else
                {
                    CurrentEntity = new AppInfoBLL().GetSingle(this.AppID);

                    //新表结构暂不使用 2014-10-27   momo
                    //this.hidUAppID.Value = CurrentEntity.UAppID.ToString();
                    //this.UAppName.Text = CurrentEntity.UAppID.ToString();
                    this.AppName.Text = CurrentEntity.AppName;
                    this.ShowName.Text = CurrentEntity.ShowName;
                    this.ForDeviceTypeList.SelectedValue = CurrentEntity.ForDeviceType.ToString();
                    this.DevID.Value = CurrentEntity.CPID.ToString();
                    this.CPID.Text = CurrentEntity.CPID.ToString();
                    this.CPName.Text = CurrentEntity.DevName.ToString();
                    this.SearchKeys.Text = CurrentEntity.SearchKeys;
                    this.DownTimes.Text = CurrentEntity.DownTimes.ToString();
                    this.RecommWord.Text = CurrentEntity.RecommWord;
                    this.AppType.SelectedValue = this.GetAppType(CurrentEntity.AppType.ToString());
                    this.OldAppType.Value = this.GetAppType(CurrentEntity.AppType.ToString());
                    this.EvilLevel.SelectedValue = CurrentEntity.EvilLevel.ToString();
                    this.RecommLevel.SelectedValue = CurrentEntity.RecommLevel.ToString();

                    this.IssueType.SelectedValue = CurrentEntity.IssueType.ToString();
                    this.AppDesc.Text = CurrentEntity.AppDesc;
                    this.ShowThumbPic.ImageUrl = string.IsNullOrEmpty(CurrentEntity.ThumbPicUrl) ? @"Theme/Images/empty.png" : CurrentEntity.ThumbPicUrl;
                    this.ThumbPicUrl.Value = CurrentEntity.ThumbPicUrl;
                    this.OldThumbPicUrl.Value = CurrentEntity.ThumbPicUrl;
                    this.Remarks.Text = CurrentEntity.Remarks;
                    this.Status.SelectedValue = CurrentEntity.Status.ToString();
                    this.MainPackName.Value = CurrentEntity.PackName;
                    this.IsNetGame.SelectedValue = CurrentEntity.IsNetGame.ToString(); //this.GetIsNetGame(CurrentEntity.AppType.ToString());
                    this.OldIsNetGame.Value = CurrentEntity.IsNetGame.ToString();// this.GetIsNetGame(CurrentEntity.AppType.ToString()) == "0" ? "2102" : "2101";
                    this.drpIssueType.SelectedValue = CurrentEntity.IssueType.ToString();
                    if (CurrentEntity.IssueType == 1)
                    {
                        cbChannel.Visible = false;
                    }
                    else
                    {
                        cbChannel.Visible = true;
                    }

                    string channel = CurrentEntity.ChannelNos;
                    if (channel.IndexOf(',') > -1)
                    {
                        string[] channels = channel.Split(',');

                        foreach (ListItem li in cbChannel.Items)
                        {
                            foreach (string item in channels)
                            {
                                if (li.Value == item)
                                {
                                    li.Selected = true;
                                }
                            }
                        }

                    }
                    string channel2 = CurrentEntity.ChannelAdaptation;
                    if (channel2.IndexOf(',') > -1)
                    {
                        string[] channels2 = channel2.Split(',');
                        foreach (ListItem li in cbChannel2.Items)
                        {
                            foreach (string item in channels2)
                            {
                                if (li.Value == item)
                                {
                                    li.Selected = true;
                                }
                            }
                        }
                    }
                    BindTag(cbRecommFlag, CurrentEntity.RecommTag);
                    BindTag(CurrentEntity);
                    BindArchitecture(CurrentEntity);

                }
            }
        }

        private void BindTag(AppInfoEntity CurrentEntity)
        {
            int apptag = CurrentEntity.AppTag;
            if (apptag == 1)
            {
                chbIsSafe.Items[0].Selected = true;
            }
            else if (apptag == 3)
            {
                chbIsSafe.Items[0].Selected = true;
                chbIsSafe.Items[1].Selected = true;
            }
            else if (apptag == 5)
            {
                chbIsSafe.Items[0].Selected = true;
                chbIsSafe.Items[2].Selected = true;
            }
            else if (apptag == 6)
            {
                chbIsSafe.Items[1].Selected = true;
                chbIsSafe.Items[2].Selected = true;
            }
            else if (apptag == 7)
            {
                chbIsSafe.Items[0].Selected = true;
                chbIsSafe.Items[1].Selected = true;
                chbIsSafe.Items[2].Selected = true;
            }

        }
        private void BindArchitecture(AppInfoEntity CurrentEntity)
        {
            int Architecture = CurrentEntity.Architecture;
            if (Architecture == 1)
            {
                cbArchitecture.Items[0].Selected = true;
            }
            else if (Architecture == 2)
            {
                cbArchitecture.Items[1].Selected = true;
            }
            else if (Architecture == 3)
            {
                cbArchitecture.Items[0].Selected = true;
                cbArchitecture.Items[1].Selected = true;
            }
        }

        /// <summary>
        /// 绑定应用类型
        /// </summary>
        private void BindAppType()
        {
            List<GroupTypeEntity> list = new GroupTypeBLL().GetDataList(this.AppClass.Convert<int>(11));

            if (this.AppClass == "11")
            {
                this.IsNetGame.Enabled = false;
            }

            ListItem objItem = new ListItem("分类筛选", "");
            this.AppType.Items.Add(objItem);

            foreach (GroupTypeEntity item in list)
            {
                if (item.TypeID == 1100 || item.TypeID == 1200)
                {
                    continue;
                }
                else
                {
                    this.AppType.Items.Add(new ListItem(item.TypeName, item.TypeID.ToString()));
                }
            }

            List<ChannelEntity> chList = new ChannelBLL().BindList();
            cbChannel.DataSource = chList;
            cbChannel.DataTextField = "ChannelName";
            cbChannel.DataValueField = "ChannelNo";
            cbChannel.DataBind();

            cbChannel2.DataSource = chList;
            cbChannel2.DataTextField = "ChannelName";
            cbChannel2.DataValueField = "ChannelNo";
            cbChannel2.DataBind();
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {


            AppInfoEntity appInfo = new AppInfoEntity();
            appInfo.AppID = this.AppID;
            //appInfo.UAppID = this.hidUAppID.Value.Convert<int>(0);
            appInfo.MainPackID = 0;
            appInfo.AppName = this.AppName.Text.Trim();
            appInfo.ShowName = this.ShowName.Text.Trim();
            appInfo.Architecture = GetArchitecture();
            appInfo.ForDeviceType = this.ForDeviceTypeList.SelectedValue.Convert<int>(0);
            appInfo.PackName = this.MainPackName.Value;
            appInfo.PackSign = string.Empty;
            appInfo.CPID = this.DevID.Value.Convert<int>(0);
            appInfo.IssueType = Convert.ToInt32(drpIssueType.SelectedItem.Value);
            appInfo.ChannelNos = GetChannel();
            appInfo.ChannelAdaptation = GetChannel2();
            appInfo.DevName = this.CPName.Text.Trim();
            appInfo.AppClass = this.AppClass.Convert<int>(11);
            appInfo.IsNetGame = this.IsNetGame.SelectedValue.Convert<int>(0);
            appInfo.EvilLevel = this.EvilLevel.SelectedValue.Convert<int>(0);
            appInfo.RecommLevel = this.RecommLevel.SelectedValue.Convert<int>(0);
            appInfo.RecommWord = this.RecommWord.Text.Trim();
            appInfo.ThumbPicUrl = this.ThumbPicUrl.Value;
            appInfo.AppDesc = this.AppDesc.Text.Trim();
            appInfo.SearchKeys = this.SearchKeys.Text.Trim();
            appInfo.DownTimes = this.DownTimes.Text.Trim().Convert<int>();
            appInfo.Remarks = this.Remarks.Text.Trim();
            appInfo.Status = this.Status.SelectedValue.Convert<int>();
            appInfo.MainVerName = string.Empty;
            appInfo.MainVerCode = 0;
            appInfo.MainSignCode = string.Empty;
            appInfo.MainIconUrl = string.Empty;
            appInfo.RecommTag = GetRecommFlag();
            appInfo.AppTag = GetTag();

            this.X1 = Math.Round(this.Request.Params["x1"].Convert<double>(0));
            this.X2 = Math.Round(this.Request.Params["x2"].Convert<double>(0));
            this.Y1 = Math.Round(this.Request.Params["y1"].Convert<double>(0));
            this.Y2 = Math.Round(this.Request.Params["y2"].Convert<double>(0));
            this.Width = Math.Round(this.Request.Params["w"].Convert<double>(0));
            this.Height = Math.Round(this.Request.Params["h"].Convert<double>(0));


            if (string.IsNullOrEmpty(this.AppType.SelectedValue))
            {
                this.Alert("请选择分类信息");
                return;
            }

            //应用类型
            appInfo.AppType = this.AppType.SelectedValue.Convert<int>(0);

            #region 处理缩略图
            if (this.OldThumbPicUrl.Value != this.ThumbPicUrl.Value)
            {
                if (!string.IsNullOrEmpty(appInfo.ThumbPicUrl))
                {
                    if (this.Width != 0 && this.Height != 0)
                    {

                        //裁剪方式
                        string croptype = this.Request<string>("cropType", string.Empty);

                        Bitmap bitSource = ImageHelper.GetBitmapFromUrl(appInfo.ThumbPicUrl);

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
                        string token = up.StartTransfer(2, 11, appInfo.ThumbPicUrl.Substring(appInfo.ThumbPicUrl.LastIndexOf("."), 4), 1, imageBytes.Length, string.Empty);
                        string resultString = up.Transfer(token, imageBytes, 1);
                        appInfo.ThumbPicUrl = resultString.Split(',')[1];

                        //up.GenerateThumb(appInfo.ThumbPicUrl, 0);
                        up.GenerateThumb(token);

                        bitSource.Dispose();
                    }
                }
            }
            #endregion

            if (string.IsNullOrEmpty(appInfo.AppName))
            {
                this.Alert("资料不完整，请完善后再试");
                return;
            }


            //写入数据库
            bool result = new AppInfoBLL().Update(appInfo);

            //判断是否修改成功
            if (result)
            {
                if (this.CheckBox1.Checked == true)
                {
                    new AppCommentSummaryBLL().UpdateSummary(appInfo.AppID, appInfo.DownTimes, appInfo.RecommLevel);
                }
                if (this.AppClass == "12")
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = appInfo.AppID,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        OperateType = "2",
                        OperateExplain = "修改游戏信息",
                        SourcePage = 1,
                        OperateContent = appInfo.ShowName,
                        UserName = GetUserName(),
                    };
                    new OperateRecordBLL().Insert(info);
                    this.Alert("操作成功", string.Format("GameInfoList.aspx"));
                }
                else
                {
                    this.Alert("操作成功", string.Format("AppInfoList.aspx"));
                }
            }
            else
            {
                this.Alert("操作失败");
            }
        }

        /// <summary>
        /// 绑定推荐标签
        /// </summary>
        /// <param name="cbl"></param>
        /// <param name="value"></param>
        public void BindTag(CheckBoxList cbl, int value)
        {
            if (value > 0)
            {
                for (int i = 0; i < cbl.Items.Count; i++)
                {
                    if ((Convert.ToInt32(cbl.Items[i].Value) & value) > 0)
                    {
                        cbl.Items[i].Selected = true;
                    }
                }
            }
        }
        /// <summary>
        /// 获取推荐标签
        /// </summary>
        /// <returns></returns>
        public int GetRecommFlag()
        {
            int flag = 0;
            for (int i = 0; i < cbRecommFlag.Items.Count; i++)
            {
                if (cbRecommFlag.Items[i].Selected == true)
                {
                    flag = flag + int.Parse(cbRecommFlag.Items[i].Value);
                }
            }
            return flag;
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
        public string GetChannel()
        {
            string checkStr = "";
            for (int i = 0; i < cbChannel.Items.Count; i++)
            {
                if (cbChannel.Items[i].Selected == true)
                {
                    checkStr += cbChannel.Items[i].Value + ",";
                }
            }
            if (checkStr != "")
            {
                checkStr = "," + checkStr;
            }
            return checkStr;
        }

        public string GetChannel2()
        {
            string checkStr = "";
            for (int i = 0; i < cbChannel2.Items.Count; i++)
            {
                if (cbChannel2.Items[i].Selected == true)
                {
                    checkStr += cbChannel2.Items[i].Value + ",";
                }
            }
            if (checkStr != "")
            {
                checkStr = "," + checkStr;
            }
            return checkStr;
        }
        private int GetTag()
        {
            int tag = 0;
            for (int i = 0; i < chbIsSafe.Items.Count; i++)
            {
                if (chbIsSafe.Items[i].Selected == true)
                {
                    tag = tag + int.Parse(chbIsSafe.Items[i].Value);
                }
            }
            return tag;
        }
        public string GetIsNetGame(string TypeID)
        {
            string[] temp = TypeID.Split(',');

            string result = string.Empty;

            for (int i = 0; i < temp.Length; i++)
            {
                if (temp[i] == "2101")
                {
                    //网游
                    result = "1";
                }
                else if (temp[i] == "2102")
                {
                    //单机
                    result = "0";
                }
                else
                {
                    continue;
                }
            }

            return result;
        }
        private int GetArchitecture()
        {
            int tag = 0;
            for (int i = 0; i < cbArchitecture.Items.Count; i++)
            {
                if (cbArchitecture.Items[i].Selected == true)
                {
                    tag = tag + int.Parse(cbArchitecture.Items[i].Value);
                }
            }
            return tag;
        }
        public string GetAppType(string TypeID)
        {
            string[] temp = TypeID.Split(',');

            string result = string.Empty;

            for (int i = 0; i < temp.Length; i++)
            {
                int current = temp[i].Convert<int>(0);

                if ((current > 1100 && current < 1200) || (current > 1200 && current < 1300))
                {
                    result = current.ToString();
                }
            }

            return result;
        }

        protected void drpIssueType_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (drpIssueType.SelectedValue == "1")
            {
                cbChannel.Visible = false;
            }
            else
            {
                cbChannel.Visible = true;
            }
        }

       
    }
}