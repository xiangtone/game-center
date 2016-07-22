using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.Model;
using AppStore.BLL;
using nwbase_utils;

namespace AppStore.Web
{
    public partial class PackInfoAdd : BasePage
    {
        public int AppID { get { return this.Request<int>("AppID", 0); } }
        public string ShowName { get { return this.Request<string>("ShowName", ""); } }
        public string Type { get { return this.Request<string>("type", "应用管理"); } }

        public string AppPicUrl = "";
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                this.IsMainVer.Enabled = false;
                int result = new PackInfoBLL().GetAppToPackCount(this.AppID);
                if (result > 0)
                {
                    PackInfoEntity packinfo = new PackInfoBLL().GetNewSingle(AppID);
                    AppPicUrl = packinfo.AppPicUrl.Trim();
                }
            }
        }


        /// <summary>
        /// 添加安装包   2014-10-27 momo update
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        protected void btnSave_Click(object sender, EventArgs e)
        {

            try
            {
                PackInfoEntity packInfoEntity = new PackInfoEntity()
                {

                    //应用ID
                    AppID = this.AppID,
                    //显示名称
                    ShowName = this.ShowName,
                    //合作类型，定义：1=联运，2=CPS，3=CPA，99=未合作
                    CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                    //安装包来源：1=直接合作，2=自由市场，3=豌豆荚
                    PackFrom = this.PackForm.SelectedValue.Convert<int>(0),
                    //下载量，定期更新（不影响更新时间）
                    DownTimes = this.Request<int>("DownTimes", 0),
                    //是否主版本，定义：1=是，2=否
                    IsMainVer = this.IsMainVer.SelectedValue.Convert<int>(0),
                    //安装包大小，单位：字节 B
                    PackSize = this.Request<int>("PackSize", 0),
                    //版本代码
                    VerCode = this.Request<int>("VerCode", 0),
                    //版本号
                    VerName = this.Request<string>("VerName", string.Empty),
                    //包名
                    PackName = this.Request<string>("PackName", string.Empty),
                    //签名验证码
                    PackSign = this.Request<string>("SignCode", string.Empty),
                    //ICON URL
                    IconUrl = this.Request<string>("IconPicUrl", string.Empty),
                    //首选安装包URL
                    PackUrl = this.Request<string>("PackUrl", string.Empty),
                    //备用安装包URL
                    PackUrl2 = "",
                    //安装包的MD5
                    PackMD5 = this.Request<string>("PackMD5", string.Empty),
                    //适用性描述
                    CompDesc = this.Request<string>("CompDesc", string.Empty).HtmlEncode(),
                    //更新描述
                    UpdateDesc = this.Request<string>("UpDateDesc", string.Empty).HtmlEncode(),
                    //备注
                    Remarks = this.Request<string>("Remarks", string.Empty).HtmlEncode(),
                    //状态，定义：1=正常，2=禁用
                    Status = this.Status.SelectedValue.Convert<int>(0),

                    permission = AndroidPermission(this.Request<string>("permission", string.Empty))

                };

                #region 判断数据有效性

                if (string.IsNullOrEmpty(this.ShowName))
                {
                    this.Alert("显示名称不能为空");
                    return;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.PackUrl))
                {
                    this.Alert("请先上传安装包");
                    return;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.PackName))
                {
                    this.Alert("安装包包名不能为空");
                    return;
                }
                else if (string.IsNullOrEmpty(packInfoEntity.IconUrl))
                {
                    this.Alert("Icon不能为空");
                    return;
                }

                //if (appPicUrl.Length == 0)
                //{
                //    this.Alert("请先上传应用截图");
                //    return;
                //}

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
                        return;
                    }
                }
                else
                {
                    //判断其他应用是否存在此安装包
                    result = new AppInfoBLL().CheckAppRepeat(0, packInfoEntity.PackName, packInfoEntity.PackSign);
                    //if (result > 0)
                    //{
                    //    AppInfoEntity appInfo = new AppInfoBLL().GetAppByPackNameAndPackSign(packInfoEntity.PackName, packInfoEntity.PackSign);
                    //    string msg = "应用" + appInfo.AppName + "-" + appInfo.AppID + "已存在此安装包";
                    //    this.Alert(msg);
                    //    return;
                    //}
                }

                //判断安装包是否重复 2014-10-30 momo--------------- end

                #endregion
                string[] appPicUrl = this.Request.Params["AppPicUrl"].Split(',');

                //添加应用截图，URL之间用英文逗号分隔
                string str_PicUrl = "";
                for (int i = 0; i < appPicUrl.Length; i++)
                {
                    str_PicUrl = str_PicUrl == "" ? appPicUrl[i] : (str_PicUrl + ',' + appPicUrl[i]);
                }
                packInfoEntity.AppPicUrl = str_PicUrl;
                if (str_PicUrl == "")
                {
                    PackInfoEntity packentity = new AppInfoBLL().GetNewInfo(AppID);
                    if (packentity !=null)
                    {
                        packInfoEntity.AppPicUrl = packentity.AppPicUrl;
                    }
                    
                }

                new PackInfoBLL().UpdateMainVersion(packInfoEntity);
                //添加安装包
                int packID = new PackInfoBLL().Insert(packInfoEntity);
                AppInfoEntity app = new AppInfoEntity() { UpdateTime = DateTime.Now, OpUpdateTime = DateTime.Now, AppID = AppID };
                new AppInfoBLL().ChangeUpdateTime(app);
                if (packID > 0)
                {
                    int packcount = new AppInfoBLL().GetPacksCount(AppID);
                    AppInfoEntity info = new AppInfoEntity() { AppID = AppID, PackCount = packcount };
                    bool ruslt = new AppInfoBLL().UpdatePackCount(info);
                    /*由于数据表结构的变动，故无需此操作 2014-10-27  momo
                    for (int i = 0; i < appPicUrl.Length; i++)
                    {
                        AppPicListEntity appPicListEntity = new AppPicListEntity()
                        {
                            AppID = this.AppID,
                            PackID = packID,
                            OrderNo = 0,
                            PicUrl = appPicUrl[i]
                        };

                        new AppPicListBLL().Insert(appPicListEntity);
                    }
                    */

                    //如果当前安装包版本为主版本，则把安装包信息更新到应用信息表中
                    if (packInfoEntity.IsMainVer == 1)
                    {
                        AppInfoEntity appInfoEntity = new AppInfoEntity()
                        {
                            AppID = this.AppID,
                            MainPackID = packID,
                            CoopType = this.CoopType.SelectedValue.Convert<int>(0),
                            MainPackSize = packInfoEntity.PackSize,
                            MainVerCode = this.Request<int>("VerCode", 0),
                            MainVerName = this.Request<string>("VerName", string.Empty),
                            MainIconUrl = packInfoEntity.IconUrl,
                            PackName = this.Request<string>("PackName", string.Empty),
                            PackSign = this.Request<string>("SignCode", string.Empty),
                            DataStatus = 1  // 数据状态正常
                        };

                        new AppInfoBLL().UpDatePackInfo(appInfoEntity);
                    }

                    this.Response.Redirect(string.Format("PackInfoList.aspx?AppID={0}&ShowName={1}", this.AppID, packInfoEntity.ShowName));
                }
                else
                {
                    this.Alert("新增安装包失败！");
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