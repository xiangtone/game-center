using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using System.Drawing;
using AppStore.Web.WebReference;
using System.IO;
using AppStore.Common;

namespace AppStore.Web
{
    public partial class AppInfoAdd : BasePage
    {
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
            }
        }

        /// <summary>
        /// 绑定应用类型
        /// </summary>
        private void BindAppType()
        {
            List<AppTypeEntity> list = new AppTypeBLL().GetAPPTypeList(this.AppClass.Convert<int>(0));

            if (this.AppClass == "11")
            {
                this.IsNetGame.Enabled = false;
            }

            ListItem objItem = new ListItem("分类筛选", "");
            this.AppType.Items.Add(objItem);

            foreach (AppTypeEntity item in list)
            {
                this.AppType.Items.Add(new ListItem(item.AppTypeName, item.AppType.ToString()));
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

        protected void btnNext_Click(object sender, EventArgs e)
        {
            AppInfoEntity appInfo = new AppInfoEntity();

            //主安装包ID
            appInfo.MainPackID = 0;
            //应用名称
            appInfo.AppName = this.AppName.Text.Trim();
            //显示名称
            appInfo.ShowName = this.ShowName.Text.Trim();

            //架构适配，1=arm，2=x86，...位运算
            appInfo.Architecture = GetArchitecture();

            //适用设备类型，定义：1=手机，2=平板，4=...位运算
            appInfo.ForDeviceType = this.ForDeviceTypeList.SelectedValue.Convert<int>(0);
            //包名
            appInfo.PackName = string.Empty;
            //包签名
            appInfo.PackSign = string.Empty;
            //开发者ID
            appInfo.CPID = this.DevID.Value.Convert<int>(0);
            //分发类型：1=不分渠道，2=分渠道分发
            appInfo.IssueType = Convert.ToInt32(drpIssueType.SelectedValue);
            //多个渠道号,只有当IssueType=2时生效。逗号分隔，首尾要加上逗号
            appInfo.ChannelNos = GetChannel();
            appInfo.ChannelAdaptation = GetChannel2();
            //开发者名
            appInfo.DevName = this.CPName.Text.Trim();
            //应用分类
            appInfo.AppClass = this.AppClass.Convert<int>(0);
            //是否网游，定义：1=网游，2=单机
            appInfo.IsNetGame = this.IsNetGame.SelectedValue.Convert<int>(0);
            //邪恶等级，1~5表示从纯洁到邪恶
            appInfo.EvilLevel = this.EvilLevel.SelectedValue.Convert<int>(0);
            //推荐值，0~10代表从不推荐到推荐
            appInfo.RecommLevel = this.RecommLevel.SelectedValue.Convert<int>(0);
            //推荐语
            appInfo.RecommWord = this.RecommWord.Text.Trim();
            //缩略图URL
            appInfo.ThumbPicUrl = this.ThumbPicUrl.Value;
            //应用描述
            appInfo.AppDesc = this.AppDesc.Text.Trim();
            //搜索关键字
            appInfo.SearchKeys = this.SearchKeys.Text.Trim();
            //下载量，定期更新（不影响更新时间）
            appInfo.DownTimes = this.DownTimes.Text.Trim().Convert<int>(0);
            //备注
            appInfo.Remarks = this.Remarks.Text.Trim();
            //状态:1=正常，2=禁用，12=数据异常，22=控制禁用
            appInfo.Status = this.Status.SelectedValue.Convert<int>();
            //主版本号
            appInfo.MainVerName = string.Empty;
            //主版本代码
            appInfo.MainVerCode = 0;
            //签名特征码
            appInfo.MainSignCode = string.Empty;
            //主ICON图URL地址

            appInfo.AppTag = GetTag();
            appInfo.MainIconUrl = string.Empty;
            //推荐标签，编辑指定，1=官方 2=推荐 4=首发 8=免费 16=礼包 32=活动 64=内测 128=热门...位运算
            appInfo.RecommTag = GetRecommFlag();
            //分发类型：1=不分渠道，2=分渠道分发
            //appInfo.IssueType = this.IssueType.SelectedValue.Convert<int>(1);
            //联运游戏ID
            appInfo.UAppID = this.hidUAppID.Value.Convert<int>(0);

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
            //数据状态，定义：1=正常，2=异常
            appInfo.DataStatus = 2;

            if (new AppInfoBLL().IsExistAppInfo(appInfo.ShowName))
            {
                this.Alert("当前应用已经存在，请修改后重试");
                return;
            }

            #region 处理缩略图
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
            #endregion

            if (string.IsNullOrEmpty(appInfo.AppName))
            {
                this.Alert("资料不完整，请完善后再试");
                return;
            }

            //写入数据库
            int result = new AppInfoBLL().Insert(appInfo);

            #region 由于数据结构变更，不存在此操作   2014-10-23 momo
            //if (result != 0)
            //{
            //    RAppTypeEntity rAppTypeEntity = new RAppTypeEntity()
            //    {
            //        AppID = result,
            //        TypeID = this.AppType.SelectedValue.Convert<int>(0)
            //    };

            //    if (new R_AppTypeBLL().Insert(rAppTypeEntity))
            //    {
            //        //游戏需要处理R_AppType表数据
            //        if (this.AppClass == "12")
            //        {
            //            rAppTypeEntity = new RAppTypeEntity()
            //            {
            //                AppID = result,
            //                TypeID = this.IsNetGame.SelectedValue.Convert<int>(0) == 0 ? 2102 : 2101
            //            };

            //            new R_AppTypeBLL().Insert(rAppTypeEntity);
            //        }

            //        Response.Redirect(string.Format("PackInfoAdd.aspx?AppID={0}&ShowName={1}&type=app", result, appInfo.ShowName));
            //    }
            //    else
            //    {
            //        this.Alert("操作失败");
            //    }
            //}
            //else
            //{
            //    this.Alert("操作失败");
            //}

            #endregion

            if (result != 0)
            {
                if (this.CheckBox1.Checked == true)
                {
                    new AppCommentSummaryBLL().UpdateSummary(result, appInfo.DownTimes, appInfo.RecommLevel);
                }
                if (this.AppClass == "12")
                {
                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = result,
                        reason = "",
                        Status = 1,
                        OperateFlag = "1",
                        OperateType = "2",
                        OperateExplain = "新增游戏",
                         SourcePage=1,
                        OperateContent = appInfo.ShowName,
                        UserName = GetUserName(),
                    };
                    new OperateRecordBLL().Insert(info);
                }
                //页面跳转至添加包页面
                Response.Redirect(string.Format("PackInfoAdd.aspx?AppID={0}&ShowName={1}&type=app", result, appInfo.ShowName));
            }
            else
            {
                this.Alert("新增失败");
            }
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

        private string GetChannel2()
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
        public int GetRecommFlag() {
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