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
    public partial class IosAppInfoEdit : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }

        public AppInfoiosEntity CurrentEntity { get; set; }
        public double X1 { get; set; }
        public double X2 { get; set; }
        public double Y1 { get; set; }
        public double Y2 { get; set; }
        public double Width { get; set; }
        public double Height { get; set; }
        public string AppClass { get { return this.Request<string>("AppClass", string.Empty); } }
        public string AppPicUrl = "";

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {

                if (AppID.Equals(0))
                {
                    this.Alert("参数非法", "/IosAppInfoList.aspx");
                }
                else
                {
                    CurrentEntity = new AppInfoiosBLL().GetSingle(this.AppID);

         
                    this.AppName.Text = CurrentEntity.AppName;
                    this.ShowName.Text = CurrentEntity.ShowName;
                    this.DevName.Text = CurrentEntity.DevName.ToString();
                    this.RecommWord.Text = CurrentEntity.RecommWord;
                    this.AppDesc.Text = CurrentEntity.AppDesc;

                    this.AppType.Text = CurrentEntity.AppType;
                    this.AppSize.Text = CurrentEntity.AppSize;
                    this.AppVersion.Text = CurrentEntity.AppVersion;
                    this.AppPrice.Text = CurrentEntity.AppPrice;
                    this.RecommFlagWord.Text = CurrentEntity.RecommFlagWord;
                    this.ThumbPicUrl.Value = CurrentEntity.ThumbPicUrl;
                    this.ShowThumbPic.ImageUrl = string.IsNullOrEmpty(CurrentEntity.AdsPicUrl) ? @"Theme/Images/empty.png" : CurrentEntity.ThumbPicUrl;

                    this.ShowIconPic.ImageUrl = string.IsNullOrEmpty(CurrentEntity.IconPicUrl) ? @"Theme/Images/empty.png" : CurrentEntity.IconPicUrl;
                    this.IconUrl.Value = CurrentEntity.IconPicUrl;
                    this.AppUrl.Text = CurrentEntity.AppUrl;
                    this.AppPicUrl = CurrentEntity.AppPicUrl;
                    this.AdsPicUrl.Value = CurrentEntity.AdsPicUrl;
                    this.ShowAdsPic.ImageUrl = string.IsNullOrEmpty(CurrentEntity.AdsPicUrl) ? @"Theme/Images/empty.png" : CurrentEntity.AdsPicUrl;
                    this.Remarks.Text = CurrentEntity.Remarks;
                    this.Status.SelectedValue = CurrentEntity.Status.ToString();
                }
            }
        }

      

        protected void btnSave_Click(object sender, EventArgs e)
        {
            CurrentEntity = new AppInfoiosBLL().GetSingle(this.AppID);
            try
            {
                AppInfoiosEntity appInfoios = new AppInfoiosEntity();
                appInfoios.AppID = this.AppID;
                appInfoios.AppName = this.AppName.Text.Trim();

                appInfoios.RecommFlagWord = this.RecommFlagWord.Text.Trim();
                appInfoios.AppType = this.AppType.Text.Trim();
                appInfoios.AppSize = this.AppSize.Text.Trim();
                appInfoios.AppPrice = this.AppPrice.Text.Trim();
                appInfoios.AppVersion = this.AppVersion.Text.Trim();
                appInfoios.ThumbPicUrl = this.ThumbPicUrl.Value;

                appInfoios.ShowName = this.ShowName.Text.Trim();
                appInfoios.DevName = this.DevName.Text.Trim();
                appInfoios.RecommWord = this.RecommWord.Text.Trim();
                appInfoios.IconPicUrl = this.IconUrl.Value;
                appInfoios.AppDesc = this.AppDesc.Text.Trim();
                appInfoios.Remarks = this.Remarks.Text.Trim();
                appInfoios.Status = this.Status.SelectedValue.Convert<int>();
                appInfoios.AppUrl = this.AppUrl.Text.Trim();
                appInfoios.AdsPicUrl = this.AdsPicUrl.Value;
                appInfoios.AppPicUrl = (this.AppPicUrl == "") ? CurrentEntity.AppPicUrl : this.AppPicUrl;
              

                this.X1 = Math.Round(this.Request.Params["x1"].Convert<double>(0));
                this.X2 = Math.Round(this.Request.Params["x2"].Convert<double>(0));
                this.Y1 = Math.Round(this.Request.Params["y1"].Convert<double>(0));
                this.Y2 = Math.Round(this.Request.Params["y2"].Convert<double>(0));
                this.Width = Math.Round(this.Request.Params["w"].Convert<double>(0));
                this.Height = Math.Round(this.Request.Params["h"].Convert<double>(0));



                #region 处理缩略图
                if (!string.IsNullOrEmpty(appInfoios.IconPicUrl))
                {

                    if (this.Width != 0 && this.Height != 0)
                    {
                        //裁剪方式
                        string croptype = this.Request<string>("cropType", string.Empty);

                        Bitmap bitSource = ImageHelper.GetBitmapFromUrl(appInfoios.IconPicUrl);

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
                        string token = up.StartTransfer(2, 11, appInfoios.IconPicUrl.Substring(appInfoios.IconPicUrl.LastIndexOf("."), 4), 1, imageBytes.Length, string.Empty);
                        string resultString = up.Transfer(token, imageBytes, 1);

                        appInfoios.IconPicUrl = resultString.Split(',')[1];

                        //up.GenerateThumb(appInfo.ThumbPicUrl, 0);
                        up.GenerateThumb(token);

                        bitSource.Dispose();
                    }
                }
                #endregion

                if (string.IsNullOrEmpty(appInfoios.AppName))
                {
                    this.Alert("资料不完整，请完善后再试");
                    return;
                }


                //写入数据库
                bool result = new AppInfoiosBLL().Update(appInfoios);

                //判断是否修改成功
                if (result)
                {

                    OperateRecordEntity info = new OperateRecordEntity()
                    {
                        ElemId = appInfoios.AppID,
                        reason = "",
                        Status = 1,
                        OperateFlag = "2",
                        OperateType = "2",
                        OperateExplain = "修改IOS游戏信息",
                        SourcePage = 1,
                        OperateContent = appInfoios.ShowName,
                        UserName = GetUserName(),
                    };
                    new OperateRecordBLL().Insert(info);
                    this.Response.Redirect(string.Format("IosAppInfoList.aspx?AppID={0}&ShowName={1}", this.AppID, appInfoios.ShowName));
                    this.Alert("操作成功", string.Format("IosAppInfoEdit.aspx"));
                }
                else
                {
                    this.Alert("操作失败");
                }
            }
            catch (Exception ex)
            {
           
                nwbase_utils.TextLog.Default.Error(ex.Message);
                throw ex;
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