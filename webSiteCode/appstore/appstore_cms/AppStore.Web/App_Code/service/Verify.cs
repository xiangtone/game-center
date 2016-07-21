using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Verify 的摘要说明
/// </summary>
/// 
namespace AppStore.Web
{
    public class Verify : APIBase
    {
        public override string Deal(Dictionary<string, string> param)
        {
            VerifyResult Result = new VerifyResult();
            //throw new NotImplementedException();
            if (!param.ContainsKey("user_name") ||
                !param.ContainsKey("password"))
            {
                return "{\"code\":1,\"msg\":\"wrong_params\"}";
            }
            else
            {
                try
                {
                    var userName = param["user_name"];
                    var userPwd = param["password"];

                    //0=成功，-1=密码错误，-2=账号不存在，-3=账号状态异常
                    int res = (new RightBll()).AuthUser(userName, userPwd);
                    #region 封装结果
                    Result.code = Math.Abs(res);
                    if (Result.code == 0)
                        Result.msg = "验证成功";
                    else
                    {
                        Result.msg = "验证失败，";
                        switch (Result.code)
                        {
                            case 1:
                                Result.msg += "密码错误";
                                break;
                            case 2:
                                Result.msg += "账号不存在";
                                break;
                            case 3:
                                Result.msg += "账号无效";
                                break;
                        }
                    }
                    #endregion

                }
                catch (Exception ex)
                {
                    Result.code = 1;
                    Result.msg = "服务器异常，请稍后重试";
                    nwbase_utils.TextLog.Error("error", "Verify Exception", ex);

                }
                return nwbase_utils.JsonSerializer.Serialize<VerifyResult>(Result);
            }
        }
    }

    public class VerifyResult : ResultBase
    {
    }
}