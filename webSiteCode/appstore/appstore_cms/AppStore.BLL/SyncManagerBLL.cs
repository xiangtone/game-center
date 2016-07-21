using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Text;

using System.Web;
using AppStore.BLL.Redis;
using System.Runtime.Serialization;
using AppStore.Common;
using AppStore.DAL.NewRedis;

namespace AppStore.BLL
{
    /// <summary>
    /// 同步管理器
    /// </summary>
    public class SyncManagerBLL
    {
        /// <summary>
        /// 同步地址
        /// </summary>
        public string SyncUrl { get; set; }

        /// <summary>
        /// 同步开发者信息
        /// </summary>
        /// <returns></returns>
        public bool DeveloperSync()
        {
            this.SyncUrl = "http://baselib.io.niuwan.cc/dev/getlist.api";

            bool result = false;

            try
            {
                CPsEntity currentEntity = new B_DevBLL().GetLastSingleData();

                WebClient client = new WebClient();

                string responseData = Encoding.UTF8.GetString(client.DownloadData(string.Format("{0}?UpdateTimeBegin={1}",
                                                                                  this.SyncUrl,
                                                                                  currentEntity.UpdateTime.ToString("yyyyMMddhhmmss"))));

                List<CPsEntity> list = responseData.JsonDeserialize<List<CPsEntity>>();

                Dictionary<int, string> dic = new B_DevBLL().GetParmaryKey();

                foreach (CPsEntity item in list)
                {
                    if (dic.ContainsKey(item.CPID))
                    {
                        result = new B_DevBLL().Update(item);
                    }
                    else
                    {
                        result = new B_DevBLL().Insert(item);
                    }
                }

                return result;
            }
            catch (Exception ex)
            {
                LogHelper.Default.Error(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// 同步Redis缓存
        /// </summary>
        /// <returns></returns>
        public bool RedisSync()
        {
            bool result = false;

            result = new RedisBLL().SAppScheme();
            result = new RedisBLL().HAppInfo();
            result = new RedisBLL().SSchemeGroups();
            result = new RedisBLL().HSchemeGroups();
            result = new RedisBLL().SsGroups();
            result = new RedisBLL().HNewestAppVer();
            result = new RedisBLL().HGroupElems();

            return result;
        }

        /// <summary>
        /// 缓存实时生效
        /// </summary>
        /// <returns></returns>
        public bool EffectiveSync()
        {
            try
            {
                //时间戳
                long ts = DateTime.Now.Ticks;

                string requestUrl = Extensions.AppSettings("SyncEffective", "");

                string requestParams = string.Format("ts={0}&sign={1}", ts.ToString(), ts.ToString().MD5());

                LogHelper.Default.Debug("实时生效:" + requestUrl);

                string result = WebExtension.Post(requestUrl, new Des().Encrypt(requestParams));

                ResponseCode currentEntity = result.JsonDeserialize<ResponseCode>();

                if (currentEntity.rescode.Equals("0"))
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
            catch (Exception ex)
            {
                LogHelper.Default.Error(ex.ToString());
                return false;
            }
        }

        /// <summary>
        /// 同步Redis缓存
        /// </summary>
        /// <returns></returns>
        public bool NewRedis()
        {
            bool result = false;

            //初始化NewRedisDAL，并声明Redis事务
            NewRedisDAL newRedisDal = new NewRedisDAL();

            newRedisDal.InitTran();


            result = newRedisDal.sGroupScheme();
            result = newRedisDal.hGroupInfo();
            result = newRedisDal.ssGroupElemKey();
            result = newRedisDal.HGroupElems();
            result = newRedisDal.hLinkInfo();
            result = newRedisDal.HNewestAppVer2();
            result = newRedisDal.lChnnoList();
            result = newRedisDal.ClearApiCache();

            result = newRedisDal.SubmitTrans();

            return result;
        }
    }


    public class ResponseCode
    {
        [DataMemberAttribute]
        public string rescode { get; set; }

        [DataMemberAttribute]
        public string resmsg { get; set; }
    }
}
