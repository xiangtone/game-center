using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;

using ServiceStack.Redis;
using System.Configuration;
namespace updatesys_cms.Web
{
    public class BasePage : nwbase_auth.AuthPageBase //nwbase_utils.PageBase
    {
        protected RedisClient _GetRedisClient()
        {
            string host = ConfigurationManager.AppSettings["Redis_Host"];
            int port = Convert.ToInt32(ConfigurationManager.AppSettings["Redis_Port"]);
            int db = Convert.ToInt32(ConfigurationManager.AppSettings["Redis_Updater_Db"]);

            RedisClient rc = new RedisClient(host, port, null, db);
            return rc;
        }

        protected void ExecJS(string script)
        {
            this.ClientScript.RegisterClientScriptBlock(this.GetType(), "page", script, true);
        }

        protected void Alert(string msg)
        {
            ExecJS("alert('" + msg + "');");
        }

        protected void UpdateCache()
        {
            var rc = _GetRedisClient();
            var newPackList = new BLL.UpdateInfo().GetNewList();
            foreach (var eachPackKey in newPackList.Keys)
            {
                //eachPackKey=packName + channelNo
                string cache_key = "hPackList:" + eachPackKey;
                rc.Del(cache_key);
                Dictionary<string, string> cache_update_info = new Dictionary<string, string>();
                cache_update_info.Add("updateType", newPackList[eachPackKey].UpdateType.ToString());
                cache_update_info.Add("packName", newPackList[eachPackKey].PackName);
                cache_update_info.Add("newVerName", newPackList[eachPackKey].VerName);
                cache_update_info.Add("newVerCode", newPackList[eachPackKey].VerCode.ToString());
                cache_update_info.Add("packSize", newPackList[eachPackKey].PackSize.ToString());
                cache_update_info.Add("packMD5", newPackList[eachPackKey].PackMD5);
                cache_update_info.Add("packUrl", newPackList[eachPackKey].PackUrl);
                cache_update_info.Add("pubTime", newPackList[eachPackKey].PubTime.ToString("yyyy-MM-dd HH:mm:ss"));
                cache_update_info.Add("updatePrompt", newPackList[eachPackKey].UpdatePrompt);
                cache_update_info.Add("updateDesc", newPackList[eachPackKey].UpdateDesc);
                cache_update_info.Add("schemeId", newPackList[eachPackKey].SchemeId.ToString());
                cache_update_info.Add("forceUpdateVerCode", newPackList[eachPackKey].ForceUpdateVerCode.ToString());
                rc.SetRangeInHash(cache_key, cache_update_info);
            }
        }

        protected override void OnInit(EventArgs e)
        {
            int checkLogin = Convert.ToInt32(System.Configuration.ConfigurationManager.AppSettings.Get("Check_Login"));
            if (checkLogin == 1)
            {
                base.OnInit(e);
            }
        }
    }
}