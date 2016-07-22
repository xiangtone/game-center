using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Model;
using System.Text;
using AppStore.BLL;
using AppStore.Common;
namespace AppStore.Web.API
{
    public partial class SourceUrlSync : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                string data = Encoding.UTF8.GetString(this.Request.BinaryRead(Request.ContentLength));

                if (string.IsNullOrEmpty(data))
                {
                    Response.Write("{\"res\":\"数据为空\"}");
                    return;
                }

                LogHelper.Default.Info(string.Format("From Jensen : {0}", data));

                SourceEntity srcEntity = data.JsonDeserialize<SourceEntity>();

                ResEntity resEntity = new ResEntity();
                resEntity.res = new List<ResEntity.ResItems>();

                StringBuilder objSb = new StringBuilder();

                foreach (SourceEntity.SourceItems item in srcEntity.data)
                {
                    bool result = false;

                    try
                    {
                        switch (item.cID)
                        {
                            case 0: result = false; break;
                            case 11: result = new SourceUrlSyncBLL().UpdateAppInfoThumbPicUrl(item); break;
                            case 12: result = new SourceUrlSyncBLL().UpdateAppInfoMainIconPicUrl(item); break;
                            case 21: result = new SourceUrlSyncBLL().UpdateAppPicListPicUrl(item); break;
                            case 31: result = new SourceUrlSyncBLL().UpdateGroupElemsRecommPicUrl(item); break;
                            case 41: result = new SourceUrlSyncBLL().UpdateGroupInfoGroupPicUrl(item); break;
                            case 51: result = new SourceUrlSyncBLL().UpdateGroupTypeTypePicUrl(item); break;
                            case 61: result = new SourceUrlSyncBLL().UpdateLinkInfoThumbPicUrl(item); break;
                            case 62: result = new SourceUrlSyncBLL().UpdateLinkInfoIconPicUrl(item); break;
                            case 72: result = this.UpDatePackInfo(item); break;
                        }

                        resEntity.res.Add(new ResEntity.ResItems() { resID = item.resID, resCode = 0 });


                        objSb.AppendLine(string.Format("OldSourceUrl:{0}\t{1}\t{2}\t{3}", result, item.cID, item.oldResUrl, item.newResUrl));
                    }
                    catch (Exception ex)
                    {
                        Response.Write("{\"res\":\"数据异常\"}");
                        LogHelper.Default.Error(string.Format("OldSourceUrl:{0}\t{1}\t{2}\t{3}", item.cID, item.oldResUrl, item.newResUrl, ex.ToString()));
                        return;
                    }
                }


                LogHelper.Default.Info(objSb.ToString());

                string responseJson = resEntity.JsonSerialize<ResEntity>();

                Response.Write(responseJson);
            }
        }

        /// <summary>
        /// 特殊处理安装包信息中，Icon和安装包Url
        /// </summary>
        /// <param name="item"></param>
        /// <returns></returns>
        private bool UpDatePackInfo(SourceEntity.SourceItems item)
        {
            bool result = false;

            if (item.oldResUrl.EndsWith(".apk"))
            {
                if (item.oldResUrl.StartsWith("http://fs0"))
                {
                    result = new SourceUrlSyncBLL().UpdatePackInfoPackUrl(item);

                    if (result.Equals(false))
                    {
                        item.oldResUrl = item.oldResUrl.Replace("http://fs0", "http://fs1");
                        result = new SourceUrlSyncBLL().UpdatePackInfoPackUrl(item);
                    }
                }
                else
                {
                    result = new SourceUrlSyncBLL().UpdatePackInfoPackUrl(item);

                    if (result.Equals(false))
                    {
                        item.oldResUrl = item.oldResUrl.Replace("http://fs1", "http://fs0");
                        result = new SourceUrlSyncBLL().UpdatePackInfoPackUrl(item);
                    }
                }

            }
            else
            {
                if (item.oldResUrl.StartsWith("http://fs0"))
                {
                    result = new SourceUrlSyncBLL().UpdatePackInfoIconPicUrl(item);

                    if (result.Equals(false))
                    {
                        item.oldResUrl = item.oldResUrl.Replace("http://fs0", "http://fs1");
                        result = new SourceUrlSyncBLL().UpdatePackInfoIconPicUrl(item);
                    }
                }
                else
                {
                    result = new SourceUrlSyncBLL().UpdatePackInfoIconPicUrl(item);

                    if (result.Equals(false))
                    {
                        item.oldResUrl = item.oldResUrl.Replace("http://fs1", "http://fs0");
                        result = new SourceUrlSyncBLL().UpdatePackInfoIconPicUrl(item);
                    }
                }
            }

            return result;
        }
    }
}