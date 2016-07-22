using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;

namespace AppStore.Web
{
    public partial class PackInfoEdit : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public int PackID { get { return this.Request<int>("PackID", 0); } }
        public string ShowName { get { return this.Request<string>("ShowName", string.Empty); } }
        public string Type { get { return this.Request<string>("type", "应用管理"); } }
        public PackInfoEntity CurrentEntity { get; set; }
        public List<AppPicListEntity> AppPicList { get; set; }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (this.PackID.Equals(0))
                {
                    this.Alert("参数非法", string.Format("PackInfoList.aspx?AppID={0}&ShowName={1}&type={2}", this.AppID, this.ShowName, this.Type));
                }
                else
                {
                    Bind();
                    //AppPicList = new AppPicListBLL().GetDataList(this.PackID);
                }
            }
        }

        public void Bind()
        {
            CurrentEntity = new PackInfoBLL().GetSingle(this.PackID);
            this.PackForm.SelectedValue = this.CurrentEntity.PackFrom.ToString();
            this.CoopType.SelectedValue = this.CurrentEntity.CoopType.ToString();
            this.IsMainVer.SelectedValue = this.CurrentEntity.IsMainVer.ToString();
            this.Status.SelectedValue = this.CurrentEntity.Status.ToString();

        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            try
            {


                PackInfoEntity packInfoEntity = new PackInfoEntity()
                {
                    AppID = this.AppID,
                    PackID = this.PackID,
                    ShowName = this.ShowName,
                    PackName = this.Request<string>("PackName", string.Empty),
                    VerCode = this.Request<int>("VerCode", 0),
                    VerName = this.Request<string>("VerName", string.Empty),
                    PackMD5 = this.Request<string>("PackMD5", string.Empty),
                    PackSize = this.Request<int>("PackSize", 0),
                    CompDesc = this.Request<string>("CompDesc", string.Empty).HtmlEncode(),
                    PackFrom = this.PackForm.SelectedValue.Convert<int>(0),
                    CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                    IsMainVer = this.IsMainVer.SelectedValue.Convert<int>(0),
                    //运营状态，定义：1=正常，2=禁用
                    Status = this.Status.SelectedValue.Convert<int>(0),
                    Remarks = this.Request<string>("Remarks", string.Empty).HtmlEncode(),
                    UpdateDesc = this.Request<string>("UpDateDesc", string.Empty).HtmlEncode(),
                    PackUrl = this.Request<string>("PackUrl", string.Empty),
                    IconUrl = this.Request<string>("IconPicUrl", string.Empty),
                    DownTimes = this.Request<int>("DownTimes", 0),
                    PackSign = this.Request<string>("SignCode", string.Empty),
                    AppPicUrl = "",
                    PackUrl2 = "",
                    //permission = this.Request<string>("permission", string.Empty)
                };
                string per = this.Request<string>("permission", string.Empty);
                if (per.Contains("#"))
                {
                    packInfoEntity.permission = AndroidPermission(per);
                }
                else
                {
                    packInfoEntity.permission = per;
                }
                bool ruslt = true;
                #region 判断数据有效性

                if (string.IsNullOrEmpty(this.ShowName))
                {
                    this.Alert("显示名称不能为空");
                    ruslt = false;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.PackUrl))
                {
                    this.Alert("请先上传安装包");
                    ruslt = false;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.PackName))
                {
                    this.Alert("安装包包名不能为空");
                    ruslt = false;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.IconUrl))
                {
                    this.Alert("Icon不能为空");
                    ruslt = false;
                }

                //判断安装包是否重复 2014-10-30 momo--------------- begin

                int result = 0;
                //判断此应用是否已添加安装包，有则判断新增安装包的PackName + PackSign 与旧安装包是否一致,否则根据PackName + PackSign判断其他应用是否存在此安装包 
                result = new PackInfoBLL().GetAppToPackCount(this.AppID);
                if (result > 0)
                {
                    //判断新增安装包的PackName + PackSign 与旧安装包是否一致，一致则可进行添加操作，否则提示用户上传的安装包不一致,如需添加则可通过新增应用方式添加此安装包
                    result = new AppInfoBLL().CheckAppRepeat(this.AppID, packInfoEntity.PackName, packInfoEntity.PackSign);
                    if (result == 0)
                    {
                        this.Alert("上传的安装包与现有的安装包不一致");
                        ruslt = false;
                    }
                }
                else
                {
                    //判断其他应用是否存在此安装包
                    result = new AppInfoBLL().CheckAppRepeat(0, packInfoEntity.PackName, packInfoEntity.PackSign);
                    if (result > 0)
                    {
                        AppInfoEntity appInfo = new AppInfoBLL().GetAppByPackNameAndPackSign(packInfoEntity.PackName, packInfoEntity.PackSign);
                        string msg = "应用" + appInfo.AppName + "-" + appInfo.AppID + "已存在此安装包";
                        this.Alert(msg);
                        ruslt = false;
                    }
                }

                //判断安装包是否重复 2014-10-30 momo--------------- end

                #endregion
                //如果当前安装包版本为主版本，则更新应用表信息
                if (packInfoEntity.IsMainVer == 1)
                {
                    // 更新应用的主安装包ID及包名
                    AppInfoEntity appInfoEntity = new AppInfoEntity()
                    {
                        AppID = this.AppID,
                        CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                        MainPackID = packInfoEntity.PackID,
                        MainPackSize = packInfoEntity.PackSize,
                        MainVerCode = packInfoEntity.VerCode,
                        MainVerName = packInfoEntity.VerName,
                        MainIconUrl = packInfoEntity.IconUrl,
                        PackName = packInfoEntity.PackName,
                        PackSign = packInfoEntity.PackSign,
                        DataStatus = 1
                    };

                    if (!new AppInfoBLL().UpDatePackInfo(appInfoEntity))
                    {
                        this.Alert("更新ICON、包名等信息时发生错误");
                        ruslt = false;
                    }
                }

                if (ruslt == true)
                {
                    int OldAppID = Request.Params["OldAppID"].Convert<int>(0);

                    if (Request.Params["AppPicUrls"] != null && Request.Params["AppPicUrls"].ToString() != "")
                    {
                        string[] AppPicUrls = Request.Params["AppPicUrls"].ToString().Split(',');
                        if (AppPicUrls.Length > 0)
                        {
                            string str_PicUrl = "";
                            for (int i = 0; i < AppPicUrls.Length; i++)
                            {
                                str_PicUrl = str_PicUrl == "" ? AppPicUrls[i] : (str_PicUrl + ',' + AppPicUrls[i]);
                            }

                            packInfoEntity.AppPicUrl = str_PicUrl;
                        }
                    }
                    else
                    {
                        packInfoEntity.AppPicUrl = " ";
                    }
                    string OldAppPicIDs = Request.Params["OldAppPicID"];

                    new PackInfoBLL().UpdateMainVersion(packInfoEntity);

                    bool packID = new PackInfoBLL().Update(packInfoEntity);

                    if (packID)
                    {
                        int packcount = new AppInfoBLL().GetPacksCount(AppID);
                        AppInfoEntity info = new AppInfoEntity() { AppID = AppID, PackCount = packcount };
                        new AppInfoBLL().UpdatePackCount(info);
                        this.Response.Redirect(string.Format("PackInfoList.aspx?AppID={0}&ShowName={1}&type={2}", this.AppID, packInfoEntity.ShowName, this.Type));
                    }
                    else
                    {
                        this.Alert("新增安装包失败！");
                    }
                }
                else
                {
                    CurrentEntity = packInfoEntity;
                    this.PackForm.SelectedValue = packInfoEntity.PackFrom.ToString();
                    this.CoopType.SelectedValue = packInfoEntity.CoopType.ToString();
                    this.IsMainVer.SelectedValue = packInfoEntity.IsMainVer.ToString();
                    this.Status.SelectedValue = packInfoEntity.Status.ToString();
                }
            }
            catch (Exception ex)
            {
                nwbase_utils.TextLog.Default.Error(ex.Message);
                throw ex;
            }
        }


    }
}