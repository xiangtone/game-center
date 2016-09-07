using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using System.Web;

namespace updatesys_cms.BLL
{
    public class AppInfo
    {
        /// <summary>
        /// 获取应用列表信息
        /// </summary>
        /// <returns></returns>
        public List<Model.AppInfo> GetAll()
        {
            string cache_name = "AppList";

            var cache = HttpContext.Current.Cache;

            var appListFromCache = cache[cache_name];

            if (appListFromCache == null)
            {
                var appListFromDb = new DAL.AppInfo().GetAll();
                if (appListFromDb != null)
                    cache.Add(cache_name, appListFromDb, null, DateTime.Now.AddHours(2), System.Web.Caching.Cache.NoSlidingExpiration, System.Web.Caching.CacheItemPriority.Default, null);
                return appListFromDb;
            }
            else
                return (List<Model.AppInfo>)appListFromCache;
        }

        /// <summary>
        /// 获取应用列表信息（字典形式）
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Model.AppInfo> GetDictAll()
        {
            var appList = GetAll();
            if (appList != null)
            {
                Dictionary<int, Model.AppInfo> result = new Dictionary<int, Model.AppInfo>();
                foreach (var eachApp in appList)
                {
                    result.Add(eachApp.AppID, eachApp);
                }
                return result;
            }
            return null;
        }
    }
}
