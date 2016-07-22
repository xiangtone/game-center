using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// LoginInfoModel 的摘要说明
/// </summary>
/// 

namespace AppStore.Web
{
    public class LoginInfoModel
    {
        public LoginInfoModel()
        {
            //
            // TODO: 在此处添加构造函数逻辑
            //
        }


        /// <summary>
        /// 开发者登陆账号
        /// </summary>
        public string DevLoginAccount
        {
            get;
            set;
        }

        /// <summary>
        /// 开发者登陆密码
        /// </summary>
        public string DevLoginPassword
        {
            get;
            set;
        }

        /// <summary>
        /// 登陆成功后保持登陆时间（天）
        /// </summary>
        public int HoldLoginTime
        {
            get;
            set;
        }
    }
}