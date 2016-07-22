using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;



namespace AppStore.Web
{
    /// <summary>
    /// 用户账号状态
    /// </summary>
    public enum UserStatueEnum
    {
        UnActive = -2, //未激活
        Forbidden = 1, //禁用
        Normol = 0,   //正常
        UnNormol = -3  //异常
    }
}