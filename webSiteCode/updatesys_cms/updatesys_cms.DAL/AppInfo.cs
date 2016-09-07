using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace updatesys_cms.DAL
{
    public class AppInfo:Base
    {
        public List<Model.AppInfo> GetAll()
        {
            string sql = "select AppID,AppName,PackFlag,AppToken,CreateTime,`Status` from `OwnAppInfo`;";
            using (var sr = _AppLibHelper.ExecuteReader(System.Data.CommandType.Text, sql, null))
            {
                if (sr != null && !sr.IsClosed)
                {
                    List<Model.AppInfo> result = new List<Model.AppInfo>();
                    while (sr.Read())
                    {
                        Model.AppInfo eachItem = new Model.AppInfo();
                        eachItem.AppID = sr.GetInt32(0);
                        eachItem.AppName = sr.GetString(1);
                        eachItem.PackFlag = sr.GetString(2);
                        eachItem.AppToken = sr.GetString(3);
                        eachItem.CreateTime = sr.GetDateTime(4);
                        eachItem.Status = sr.GetInt32(5);
                        result.Add(eachItem);
                    }
                    return result;
                }
                else
                    return null;
            }
        }
    }
}
