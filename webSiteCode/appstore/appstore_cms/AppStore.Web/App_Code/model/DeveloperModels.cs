using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;



namespace AppStore.Web
{
/// <summary>
/// 开发者对应Models
/// added by kezesong 2014-5-8
/// </summary>
    public class DeveloperModels
    {
        public int UserId
        {
            get;
            set;
        }

        public string DevName
        {
            get;
            set;
        }

        /// <summary>
        /// 开发者类型 开发者:0,CP:1
        /// </summary>
        public int DevType
        {
            get;
            set;
        }
    }
}