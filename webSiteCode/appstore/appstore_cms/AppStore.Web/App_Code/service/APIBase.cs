using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

using nwbase_utils;




namespace AppStore.Web
{
    /// <summary>
    /// APIBase 的摘要说明
    /// </summary>
    public abstract class APIBase
    {
        public abstract string Deal(Dictionary<string, string> param);
    }

    public class RequestBody
    {
        public string action { get; set; }
        public Dictionary<string, string> param { get; set; }

        public static RequestBody Parse(string content)
        {
            return JsonSerializer.Deserialize<RequestBody>(content);
        }
    }
    public class ResultBase
    {
        /// <summary>
        /// 结果代码，0=成功，其它=失败
        /// </summary>
        public int code { get; set; }
        /// <summary>
        /// 结果消息
        /// </summary>
        public string msg { get; set; }
    }
}