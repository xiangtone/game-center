using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;



namespace AppStore.Web
{
    /// <summary>
    /// ResetPasswordModel 的摘要说明
    /// 该类用于忘记密码是，调用发送充值密码邮件接口时需要的Module
    /// added by kezesong 2014-5-14
    /// </summary>
    public class ResetPasswordModel
    {
        /// <summary>
        /// 需要发送的邮件地址
        /// </summary>
        public string EmailAddr
        {
            get;
            set;
        }

        /// <summary>
        /// 重置密码成功后需要跳转到的地址
        /// </summary>
        public string CallbackUrl
        {
            get;
            set;
        }
    }
}