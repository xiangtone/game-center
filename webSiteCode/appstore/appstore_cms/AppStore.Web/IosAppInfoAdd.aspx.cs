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
    
    
    
    public partial class IosAppInfoAdd : BasePage
    {

        public int AppID {get { return this.Request<int>("AppID", 0); } }
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
                DataBind();
            }
        }


        protected void btnSave_Click(object sender, EventArgs e)
        {
            try
            {
                AppInfoiosEntity appInfoios = new AppInfoiosEntity()
                {
                    //应用ID
                    AppID = this.AppID,
                    //应用名称
                    AppName = this.AppName.Text.Trim(),
                    //显示名称
                    ShowName = this.ShowName.Text.Trim(),
                    //开发商名称
                    DevName = this.DevName.Text.Trim(),
                    //应用类型
                    AppType = this.AppType.Text.Trim(),
                    //应用大小
                    AppSize = this.AppSize.Text.Trim(),
                    //应用价格
                    AppPrice = this.AppPrice.Text.Trim(),
                    //应用版本
                    AppVersion = this.AppVersion.Text.Trim(),
                    //应用标示语
                    RecommFlagWord = this.RecommFlagWord.Text.Trim(),
                    //缩略图
                    ThumbPicUrl = this.ThumbPicUrl.Value,

                    //应用连接Url地址
                    AppUrl = this.AppUrl.Text.Trim(),
                    //应用推荐语
                    RecommWord = this.RecommWord.Text.Trim(),
                    //描述语言
                    AppDesc = this.AppDesc.Text.Trim(),

                    //Icon Url地址
                    IconPicUrl = this.Request<string>("IconPicUrl", string.Empty),
                    //IconUrl = this.IconUrl.Value,
                    //广告Urls
                    //AdsPicUrl = this.AdsUrl.Text.Trim(),
                    AdsPicUrl = this.AdsPicUrl.Value,

                    //推荐语
                    Remarks = this.Remarks.Text.Trim(),
                    
                    //应用状态
                    Status = this.Status.SelectedValue.Convert<int>()

                };
                #region 判断数据有效性

                if (string.IsNullOrEmpty(appInfoios.ShowName))
                {
                    this.Alert("显示名称不能为空");
                    return;
                }
                else if (string.IsNullOrEmpty(appInfoios.AppName))
                {
                    this.Alert("安装包包名不能为空");
                    return;
                }
                else if (string.IsNullOrEmpty(appInfoios.IconPicUrl))
                {
                    this.Alert("Icon不能为空");
                    return;
                }

                bool result1 = false;
                //判断些应用信息是否已经存在
                result1 = new AppInfoiosBLL().IsExistAppInfo(this.AppName.Text.Trim());
                if (result1)
                {
                    this.Alert("应用已经存在");
                    return;
                }

                //iosApp信息cms后台管理
                string[] appPicUrl = this.Request.Params["AppPicUrl"].Split(',');
                string str_PicUrl = "";

                //添加应用截图，URL之间用英文逗号分隔
                for (int i = 0; i < appPicUrl.Length; i++)
                {
                    str_PicUrl = str_PicUrl == "" ? appPicUrl[i] : (str_PicUrl + ',' + appPicUrl[i]);
                }

                appInfoios.AppPicUrl = str_PicUrl;

                this.X1 = Math.Round(this.Request.Params["x1"].Convert<double>(0));
                this.X2 = Math.Round(this.Request.Params["x2"].Convert<double>(0));
                this.Y1 = Math.Round(this.Request.Params["y1"].Convert<double>(0));
                this.Y2 = Math.Round(this.Request.Params["y2"].Convert<double>(0));
                this.Width = Math.Round(this.Request.Params["w"].Convert<double>(0));
                this.Height = Math.Round(this.Request.Params["h"].Convert<double>(0));

                #endregion


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
                        //StartTransfer中的AppID在前端页面中上传控件定义
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
                int result2 = new AppInfoiosBLL().Insert(appInfoios);

                if (result2 != 0)
                {
        
                    if (this.AppClass == "23")
                    {
                        OperateRecordEntity info = new OperateRecordEntity()
                        {
                            ElemId = result2,
                            reason = "",
                            Status = 1,
                            OperateFlag = "1",
                            OperateType = "2",
                            OperateExplain = "新增IOS游戏",
                            SourcePage = 1,
                            OperateContent = appInfoios.ShowName,
                            UserName = GetUserName(),
                        };
                        new OperateRecordBLL().Insert(info);
                    }
                    this.Response.Redirect(string.Format("IosAppInfoList.aspx?AppID={0}&ShowName={1}", this.AppID, appInfoios.ShowName));
                }
                else
                {
                    this.Alert("新增失败");
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
     
      

        protected void AppUrl_TextChanged(object sender, EventArgs e)
        {

        }

        protected void DropDownList1_SelectedIndexChanged(object sender, EventArgs e) 
        {

        }


        protected void RecommWord_TextChanged(object sender, EventArgs e)
        {

        }
       
    }
}