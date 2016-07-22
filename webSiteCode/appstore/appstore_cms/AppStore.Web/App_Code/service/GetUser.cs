using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// GetUser 的摘要说明
/// </summary>
/// 

namespace AppStore.Web
{
    public class GetUser : APIBase
    {

        public override string Deal(Dictionary<string, string> param)
        {
            GetUserResult Result = new GetUserResult();
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
                    {
                        UserInfo userInfo = new RightBll().GetUserInfo(0, userName);
                        if (userInfo == null)
                        {
                            Result.code = 4;
                            Result.msg = "用户信息获取失败";
                        }
                        else
                        {
                            Result.msg = "获取成功";
                            Result.auth_user = new UserModelResult();
                            Result.auth_user.UserId = userInfo.UserId;
                            Result.auth_user.TeamType = userInfo.TeamType;
                            Result.auth_user.TeamRefId = userInfo.TeamRefId;
                            Result.auth_user.TeamFlag = userInfo.TeamFlag;
                            Result.auth_user.TeamName = userInfo.TeamName;
                            Result.auth_user.UserName = userInfo.UserName;
                            Result.auth_user.NickName = userInfo.NickName;
                            Result.auth_user.RealName = userInfo.RealName;
                        }
                    }
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
                    nwbase_utils.TextLog.Error("error", "GetUser Exception", ex);

                }
                return nwbase_utils.JsonSerializer.Serialize<GetUserResult>(Result);
            }
        }
    }

    public class GetUserResult : ResultBase
    {
        public UserModelResult auth_user { get; set; }
    }

    public class UserModelResult
    {
        [JsonProperty(PropertyName = "user_id")]
        /// <summary>
        /// 用户ID
        /// </summary>
        public int UserId = 0;

        [JsonProperty(PropertyName = "user_token")]
        /// <summary>
        /// 用户Token
        /// </summary>
        public string UserToken = "";

        [JsonProperty(PropertyName = "team_type")]
        /// <summary>
        /// 团队类型，1=牛玩，2=厂商，3=CP
        /// </summary>
        public int TeamType = 0;

        [JsonProperty(PropertyName = "team_ref_id")]
        /// <summary>
        /// 团队关联ID，厂商ID或CPID，0=牛玩
        /// </summary>
        public int TeamRefId = 0;

        [JsonProperty(PropertyName = "team_flag")]
        /// <summary>
        /// 团队标识
        /// </summary>
        public string TeamFlag = "";

        [JsonProperty(PropertyName = "team_name")]
        /// <summary>
        /// 团队名称
        /// </summary>
        public string TeamName = "";

        [JsonProperty(PropertyName = "user_name")]
        /// <summary>
        /// 用户名
        /// </summary>
        public string UserName = "";

        [JsonProperty(PropertyName = "nickname")]
        /// <summary>
        /// 昵称
        /// </summary>
        public string NickName = "";

        [JsonProperty(PropertyName = "real_name")]
        /// <summary>
        /// 真实姓名
        /// </summary>
        public string RealName = "";

    }
}