using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Auth 的摘要说明
/// </summary>
/// 

namespace AppStore.Web
{
    public class Auth : APIBase
    {

        public override string Deal(Dictionary<string, string> param)
        {
            AuthResult Result = new AuthResult();
            if (!param.ContainsKey("user_id") ||
                !param.ContainsKey("user_token") ||
                !param.ContainsKey("request_url"))
            {
                return "{\"code\":1,\"msg\":\"wrong_params\"}";
            }
            else
            {

                try
                {
                    var userId = nwbase_utils.Tools.GetInt(param["user_id"], 0);
                    var userToken = param["user_token"];
                    var requestUrl = param["request_url"];
                    var actionValue = param.ContainsKey("action_value") ? param["action_value"] : string.Empty;

                    if (userId < 0 || string.IsNullOrEmpty(userToken) || string.IsNullOrEmpty(requestUrl) || !requestUrl.StartsWith("http"))
                    {
                        Result.code = 2;
                        Result.msg = "当前用户没有该模块的访问权限";
                    }
                    else
                    {
                        RightBll rightBll = new RightBll();

                        #region 验证用户Token

                        string server_utoken = nwbase_utils.Cache.CacheHelper.GetCache(string.Format("LoginUser_{0}", userId)) as string;
                        if (server_utoken == userToken)
                        {
                            //用户Token 验证成功
                            Result.auth_user = new UserModelResult();
                            Result.auth_user.UserId = userId;
                            Result.auth_user.UserToken = userToken;

                            // 用户信息
                            UserInfo userInfo = rightBll.GetUserInfo(userId, "");
                            if (userInfo != null)
                            {
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
                            Result.auth_user = null;

                        #endregion

                        #region 验证模块权限
                        ModuleInfo moduleInfo = rightBll.GetModuleInfo(requestUrl, actionValue);
                        if (moduleInfo != null)
                        {
                            Result.auth_module = new ModuleResult();

                            Result.auth_module.ModuleId = moduleInfo.ModuleId;
                            Result.auth_module.PermType = moduleInfo.PermType;
                            Result.auth_module.ModuleName = moduleInfo.ModuleName;
                            Result.auth_module.ModulePath = moduleInfo.ModulePath;
                            if (Result.auth_user == null && Result.auth_module.PermType == 3)
                            {
                                // 3=无需授权
                                Result.auth_module.Rights = 511;
                            }
                            else if (Result.auth_user == null)
                            {
                                Result.auth_module.Rights = 0;
                            }
                            else if (Result.auth_module.PermType == 2)
                            {
                                // 2=登录授权
                                Result.auth_module.Rights = 511;
                            }
                            else
                            {
                                // 权限信息
                                Dictionary<int, int> dicUserRights = rightBll.GetUserRights(Result.auth_user.UserId);
                                if (dicUserRights != null && dicUserRights.ContainsKey(Result.auth_module.ModuleId))
                                {
                                    // 验证成功
                                    Result.auth_module.Rights = dicUserRights[Result.auth_module.ModuleId];
                                }
                                else
                                {
                                    Result.auth_module = null;
                                }

                            }
                        }
                        else
                            Result.auth_module = null;
                        #endregion

                        if (Result.auth_module == null || Result.auth_module.Rights <= 0)
                        {
                            Result.code = 2;
                            Result.msg = "当前用户没有该模块的访问权限";
                        }
                        else
                        {
                            Result.code = 0;
                            Result.msg = "授权成功";
                        }
                    }
                    return nwbase_utils.JsonSerializer.Serialize<AuthResult>(Result);
                }
                catch (Exception ex)
                {
                    Result.code = 1;
                    Result.msg = "服务器异常，请稍后重试";
                    nwbase_utils.TextLog.Error("error", "Auth Exception", ex);
                }
                return string.Empty;
            }
        }
    }

    public class AuthResult : ResultBase
    {
        public UserModelResult auth_user { get; set; }

        public ModuleResult auth_module { get; set; }
    }

    public class ModuleResult
    {

        /// <summary>
        /// 模块ID
        /// </summary>
        [JsonProperty(PropertyName = "module_id")]
        public int ModuleId = 0;


        /// <summary>
        /// 授权类型
        /// </summary>
        [JsonProperty(PropertyName = "perm_type")]
        public int PermType = 0;


        /// <summary>
        /// 模块名
        /// </summary>
        [JsonProperty(PropertyName = "module_name")]
        public string ModuleName = "";


        /// <summary>
        /// 模块路径（不是URL）
        /// </summary>
        [JsonProperty(PropertyName = "module_path")]
        public string ModulePath = "";


        /// <summary>
        /// 权限值
        /// </summary>
        [JsonProperty(PropertyName = "rights")]
        public int Rights = 0;
    }
}