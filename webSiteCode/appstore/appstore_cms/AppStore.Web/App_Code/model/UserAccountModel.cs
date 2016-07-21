using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;



namespace AppStore.Web
{
/// <summary>
/// 开发者平台提交的注册信息对应的Models
/// </summary>
    public class UserAccountModel
    {

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
        /// 团队类型 1：牛玩 2：厂商 3：CP
        /// 开发者平台提交过来的数据默认为 CP
        /// </summary>
        public int TeamType
        {
            get;
            set;
        }


        /// <summary>
        /// 激活账号后需要转向的url地址
        /// </summary>
        public string CallbackUrl
        {
            get;
            set;
        }
    }
}