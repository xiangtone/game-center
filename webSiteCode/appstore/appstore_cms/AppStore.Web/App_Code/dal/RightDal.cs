using System;
using System.Linq;
using System.Collections.Generic;
using System.Web;
using System.Data;
using System.Text;
using nwbase_utils;
using MySql.Data;
using MySql.Data.Common;
using MySql.Data.Types;
using MySql.Data.MySqlClient;

namespace AppStore.Web
{
    public class RightDal : BaseDal
    {
        public RightDal()
        {
        }


        /// <summary>
        /// 获取用户列表
        /// 返回用户列表于DataSet.Tables[0]中，DataSet为NULL表示获取失败
        /// </summary>
        /// <returns></returns>
        public DataSet GetUserList()
        {
            string cacheName = "rightUserList";
            DataSet ds = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as DataSet;
            if (ds == null)
            {
                string sql = "select UserId,TeamType,TeamRefId,TeamFlag,TeamName,UserName,UserPwd,RealName,NickName,CreateTime,Status from RightUsers where status < 2;";
                ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, ds);
                else
                    ds = null;
            }
            return ds;
        }

        /// <summary>
        /// 获取用户信息
        /// 返回用户信息，Null表示获取失败
        /// </summary>
        /// <param name="userId">为0表示仅根据userName获取用户信息</param>
        /// <param name="userName">为空时表示仅根据userId获取用户信息</param>
        /// <returns></returns>
        public UserInfo GetUserInfo(int userId, string userName)
        {
            string cacheName = string.Format("rightUserInfo_{0}", userId.ToString());
            UserInfo info = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as UserInfo;
            if (info == null)
            {
                DataSet ds = GetUserList();
                if (ds != null)
                {
                    DataRow[] drs = ds.Tables[0].Select(string.Format("userId={0} or userName='{1}'", userId.ToString(), userName));
                    if (drs.Length == 1)
                    {
                        info = new UserInfo();

                        info.UserId = Tools.GetInt(drs[0]["UserId"], 0);
                        info.TeamType = Tools.GetInt(drs[0]["TeamType"], 0);
                        info.TeamRefId = Tools.GetInt(drs[0]["TeamRefId"], 0);
                        info.TeamFlag = Tools.GetStr(drs[0]["TeamFlag"], "");
                        info.TeamName = Tools.GetStr(drs[0]["TeamName"], "");
                        info.UserName = Tools.GetStr(drs[0]["UserName"], "");
                        info.UserPwd = Tools.GetStr(drs[0]["UserPwd"], "");
                        info.RealName = Tools.GetStr(drs[0]["RealName"], "");
                        info.NickName = Tools.GetStr(drs[0]["NickName"], "");
                        info.CreateTime = Tools.GetDatetime(drs[0]["CreateTime"], DateTime.MinValue).ToString("yyyyMMddHHmmss");
                        info.Status = Tools.GetInt(drs[0]["Status"], 0);

                        cacheName = string.Format("rightUserInfo_{0}", info.UserId.ToString());
                        nwbase_utils.Cache.CacheHelper.SetCache(cacheName, info);
                    }
                }
            }
            return info;
        }

        /// <summary>
        /// 新增用户
        /// 返回新增用户的ID：0=异常失败，-1=用户名已经存在
        /// </summary>
        /// <param name="info">用户信息实体，有效字段包括：TeamType/TeamId/UserName/UserPwd/RealName/NickName/Status</param>
        /// <returns></returns>
        public int AddUser(UserInfo info)
        {
            // 用户名重复性检测，经过这个检测仍有极少可能出错，以异常形式表现
            if (GetUserInfo(0, info.UserName) != null)
                return -1;

            string salt = new nwbase_utils.UniqueRandom(100000).Next().ToString().PadLeft(5, '0');
            string newPwd = info.UserPwd;
            newPwd = nwbase_utils.Encryption.MD5Hash(newPwd);
            newPwd = nwbase_utils.Encryption.MD5HashWithSalt(newPwd, salt);
            newPwd += ":" + salt;
            info.UserPwd = newPwd;

            string sql = @"insert into RightUsers(TeamType,TeamRefId,TeamFlag,TeamName,UserName,UserPwd,RealName,NickName,Status)
values(@TeamType, @TeamRefId, @TeamFlag, @TeamName, @UserName, @UserPwd, @RealName, @NickName, @Status); select last_insert_id();";

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@TeamType", info.TeamType));
            paramList.Add(new MySqlParameter("@TeamRefId", info.TeamRefId));
            paramList.Add(new MySqlParameter("@TeamFlag", info.TeamFlag));
            paramList.Add(new MySqlParameter("@TeamName", info.TeamName));
            paramList.Add(new MySqlParameter("@UserName", info.UserName));
            paramList.Add(new MySqlParameter("@UserPwd", info.UserPwd));
            paramList.Add(new MySqlParameter("@RealName", info.RealName));
            paramList.Add(new MySqlParameter("@NickName", info.NickName));
            paramList.Add(new MySqlParameter("@Status", info.Status));

            object ret = MySqlHelper.ExecuteScalar(_connStr, sql, paramList.ToArray());
            int id = Tools.GetInt(ret, 0);
            if (id > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserList", true);
                return id;
            }
            return 0;
        }

        /// <summary>
        /// 设置用户信息
        /// 返回值：1=成功，0=失败，-1=用户名已经存在
        /// </summary>
        /// <param name="info">用户信息实体，有效字段包括：UserId/UserName/RealName/NickName/Status</param>
        /// <returns></returns>
        public int SetUserInfo(UserInfo info)
        {
            // 用户名重复性检测，经过这个检测仍有极少可能出错，以异常方式表现
            UserInfo oldNameUserInfo = GetUserInfo(0, info.UserName);
            if (oldNameUserInfo != null && oldNameUserInfo.UserId != info.UserId)
            {
                return -1;
            }

            string sql = @"update RightUsers set teamType=@TeamType,teamRefId=@TeamRefId,teamFlag=@TeamFlag,teamName=@TeamName,
            userName=@UserName,realName=@RealName,nickName=@NickName,status=@Status where userId=@UserId";

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@TeamType", info.TeamType));
            paramList.Add(new MySqlParameter("@TeamRefId", info.TeamRefId));
            paramList.Add(new MySqlParameter("@TeamFlag", info.TeamFlag));
            paramList.Add(new MySqlParameter("@TeamName", info.TeamName));
            paramList.Add(new MySqlParameter("@UserName", info.UserName));
            paramList.Add(new MySqlParameter("@RealName", info.RealName));
            paramList.Add(new MySqlParameter("@NickName", info.NickName));
            paramList.Add(new MySqlParameter("@Status", info.Status));
            paramList.Add(new MySqlParameter("@UserId", info.UserId));

            int ret = MySqlHelper.ExecuteNonQuery(_connStr, sql, paramList.ToArray());
            if (ret == 1)
            {
                nwbase_utils.Cache.CacheHelper.DelCache(string.Format("rightUserInfo_{0}", info.UserId.ToString()), true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserList", true);
                return 1;
            }
            return 0;
        }

        /// <summary>
        /// 删除用户
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public bool DelUser(int userId)
        {
            string sql = "update RightUsers set status = 2 where userId=@UserId;";
            var param = new MySqlParameter("@UserId", userId);

            var result = MySqlHelper.ExecuteNonQuery(_connStr, sql, param);
            if (result > 0)
            {
                string cacheName = string.Format("rightUserInfo_{0}", userId.ToString());
                nwbase_utils.Cache.CacheHelper.DelCache(cacheName, true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserList", true);
                return true;
            }
            return false;
            //return result > 0;
        }

        /// <summary>
        /// 设置用户密码
        /// 返回值：0=异常，1=成功，-1=用户不存在，-2=旧密码错误
        /// </summary>
        /// <param name="userId">为0时表示使用userName</param>
        /// <param name="userName">为空时表示使用userId</param>
        /// <param name="oldUserPwd">旧密码（原文）</param>
        /// <param name="newUserPwd">新密码</param>
        /// <param name="isCheckOldPwd">是否验证旧密码</param>
        /// <returns></returns>
        public int SetUserPwd(int userId, string userName, string oldUserPwd, string newUserPwd, bool isCheckOldPwd)
        {
            //oldUserPwd = CryptHelper.MD5CmsUserPwd(oldUserPwd);
            //newUserPwd = CryptHelper.MD5CmsUserPwd(newUserPwd);

            // 用户与旧密码判断
            UserInfo info = GetUserInfo(userId, userName);


            if (info == null)
                return -1;                          // 用户不存在
            else if (isCheckOldPwd)
            {
                oldUserPwd = nwbase_utils.Encryption.MD5Hash(oldUserPwd);
                oldUserPwd = nwbase_utils.Encryption.MD5HashWithSalt(oldUserPwd, info.PwdSalt);
                if (info.RawUserPwd != oldUserPwd)
                    return -2;
                else
                {
                    oldUserPwd += ":" + info.PwdSalt;
                }
            }
            string salt = new nwbase_utils.UniqueRandom(100000).Next().ToString().PadLeft(5, '0');
            string newPwd = newUserPwd;
            newPwd = nwbase_utils.Encryption.MD5Hash(newPwd);
            newPwd = nwbase_utils.Encryption.MD5HashWithSalt(newPwd, salt);
            newPwd += ":" + salt;

            string sql = @"update RightUsers set userPwd=@NewUserPwd where userId=@UserId ";

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@NewUserPwd", newPwd));
            paramList.Add(new MySqlParameter("@UserId", info.UserId));

            if (isCheckOldPwd)
            {
                sql += " and userPwd=@OldUserpwd;";
                paramList.Add(new MySqlParameter("@OldUserpwd", oldUserPwd));
            }

            int ret = MySqlHelper.ExecuteNonQuery(_connStr, sql, paramList.ToArray());
            if (ret > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache(string.Format("rightUserInfo_{0}", info.UserId.ToString()), true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserList", true);
                return 1;
            }
            else
                return -2;
            return 0;
        }


        /// <summary>
        /// 获取模块列表
        /// 返回模块列表于DataSet.Tables[0]中，DataSet为空表示获取失败
        /// </summary>
        /// <returns></returns>
        public DataSet GetModuleList()
        {
            string cacheName = "rightModuleList";
            DataSet ds = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as DataSet;
            if (ds == null)
            {
                string sql = @"select ModuleId,ParentId,ModuleName,ModuleFlag,ModulePath,ModuleType,OrderNo,PermType,PermDefine,
                case when ActionValue<>'' then concat(ModuleUri,'?acttype=',ActionValue) else ModuleUri end ModuleUrl,
	            ModuleUri,ActionValue,ModuleDesc,CreateTime,Status from RightModules";
                ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, ds);
            }
            return ds;
        }

        /// <summary>
        /// 获取全部模块列表（dictionary），结果同GetModuleList
        /// </summary>
        /// <returns> moduleid=> (fieldName=>fieldValue) </returns>
        public Dictionary<int, ModuleInfo> GetModuleListDict()
        {
            //
            DataSet moduleList = GetModuleList();
            if (moduleList != null && moduleList.Tables != null && moduleList.Tables.Count > 0)
            {
                var groups = moduleList.Tables[0].AsEnumerable().Select(
                        r => new ModuleInfo()
                        {
                            ModuleId = r.Field<int>("ModuleId"),
                            ParentId = r.Field<int>("ParentId"),
                            ModuleName = r.Field<string>("ModuleName"),
                            ModuleFlag = r.Field<string>("ModuleFlag"),
                            ModulePath = r.Field<string>("ModulePath"),
                            ModuleType = r.Field<int>("ModuleType"),
                            OrderNo = r.Field<int>("OrderNo"),
                            PermType = r.Field<int>("PermType"),
                            PermDefine = r.Field<string>("PermDefine"),
                            ModuleUrl = r.Field<string>("ModuleUrl"),
                            ActionValue = r.Field<string>("ActionValue"),
                            ModuleDesc = r.Field<string>("ModuleDesc"),
                            CreateTime = r.Field<DateTime>("CreateTime").ToString(),
                            Status = r.Field<int>("Status"),
                        }).Where(s => s.Status != 1).GroupBy(e => e.ModuleId);
                //groups.ToDictionary<int, Dictionary<string,string>>(s=>new Dictionary<int, Dictionary<string,string>.ValueCollection(){ {s.Key.ModuleId, s.Select(e=>e)} })
                return groups.ToDictionary(s => s.Key, s => s.ToList().First());
            }
            else
                return null;
        }

        /// <summary>
        /// 获取模块信息
        /// 返回模块信息，Null表示获取失败
        /// </summary>
        /// <param name="moduleId"></param>
        /// <returns></returns>
        public ModuleInfo GetModuleInfo(int moduleId)
        {
            string cacheName = string.Format("rightModuleInfo_{0}", moduleId.ToString());
            ModuleInfo info = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as ModuleInfo;
            if (info == null)
            {
                DataSet ds = GetModuleList();
                if (Tools.IsValidDs(ds))
                {
                    DataRow[] drs = ds.Tables[0].Select(string.Format("moduleId={0}", moduleId.ToString()));
                    if (drs.Length == 1)
                    {
                        info = new ModuleInfo();

                        info.ModuleId = Tools.GetInt(drs[0]["ModuleId"], 0);
                        info.ParentId = Tools.GetInt(drs[0]["ParentId"], 0);
                        info.ModuleName = Tools.GetStr(drs[0]["ModuleName"], "");
                        info.ModuleFlag = Tools.GetStr(drs[0]["ModuleFlag"], "");
                        info.ModulePath = Tools.GetStr(drs[0]["ModulePath"], "");
                        info.ModuleType = Tools.GetInt(drs[0]["ModuleType"], 0);
                        info.OrderNo = Tools.GetInt(drs[0]["OrderNo"], 0);
                        info.PermType = Tools.GetInt(drs[0]["PermType"], 0);
                        info.PermDefine = Tools.GetStr(drs[0]["PermDefine"], "");
                        info.ModuleUrl = Tools.GetStr(drs[0]["ModuleUri"], "");
                        info.ActionValue = Tools.GetStr(drs[0]["ActionValue"], "");
                        info.ModuleDesc = Tools.GetStr(drs[0]["ModuleDesc"], "");
                        info.CreateTime = Tools.GetDatetime(drs[0]["CreateTime"], DateTime.MinValue).ToString("yyyyMMddHHmmss");
                        info.Status = Tools.GetInt(drs[0]["Status"], 0);

                        nwbase_utils.Cache.CacheHelper.SetCache(cacheName, info);
                    }
                }
            }
            return info;
        }

        /// <summary>
        /// 获取模块信息
        /// 返回模块信息，Null表示获取失败
        /// </summary>
        /// <param name="requri">请求的ModuleUri</param>
        /// <param name="actval">请求的ActionValue</param>
        /// <returns></returns>
        public ModuleInfo GetModuleInfo(string requri, string actval)
        {
            ModuleInfo info = new ModuleInfo();

            DataSet ds = GetModuleList();
            if (ds != null)
            {
                DataRow[] drs;
                drs = ds.Tables[0].Select(string.Format("ModuleUri='{0}' and ActionValue='{1}'", requri, actval));
                if (drs.Length == 1)
                {
                    info = new ModuleInfo();

                    info.ModuleId = Tools.GetInt(drs[0]["ModuleId"], 0);
                    info.ParentId = Tools.GetInt(drs[0]["ParentId"], 0);
                    info.ModuleName = Tools.GetStr(drs[0]["ModuleName"], "");
                    info.ModuleFlag = Tools.GetStr(drs[0]["ModuleFlag"], "");
                    info.ModulePath = Tools.GetStr(drs[0]["ModulePath"], "");
                    info.ModuleType = Tools.GetInt(drs[0]["ModuleType"], 0);
                    info.OrderNo = Tools.GetInt(drs[0]["OrderNo"], 0);
                    info.PermType = Tools.GetInt(drs[0]["PermType"], 0);
                    info.PermDefine = Tools.GetStr(drs[0]["PermDefine"], "");
                    info.ModuleUrl = Tools.GetStr(drs[0]["ModuleUri"], "");
                    info.ActionValue = Tools.GetStr(drs[0]["ActionValue"], "");
                    info.ModuleDesc = Tools.GetStr(drs[0]["ModuleDesc"], "");
                    info.CreateTime = Tools.GetDatetime(drs[0]["CreateTime"], DateTime.MinValue).ToString("yyyyMMddHHmmss");
                    info.Status = Tools.GetInt(drs[0]["Status"], 0);

                    string cacheName = string.Format("rightModuleInfo_{0}", info.ModuleId.ToString());
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, info);
                }
            }
            return info;
        }

        /// <summary>
        /// 获取模块ID与模块名对应表
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, string> GetModuleNameDic()
        {
            string cacheName = "rightModuleNameDic";
            Dictionary<int, string> dic = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as Dictionary<int, string>;
            if (dic == null)
            {
                DataSet ds = GetModuleList();
                if (Tools.IsValidDs(ds))
                {
                    dic = new Dictionary<int, string>();
                    foreach (DataRow dr in ds.Tables[0].Rows)
                    {
                        dic.Add(Tools.GetInt(dr["ModuleId"], 0), Tools.GetStr(dr["ModuleName"], "#"));
                    }
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, dic);
                }
            }
            return dic;
        }

        /// <summary>
        /// 获取各模块的上级（即当前模块的路径）
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, string> GetModulePathDic()
        {
            string cacheName = "rightModulePathDic";
            Dictionary<int, string> dic = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as Dictionary<int, string>;
            if (dic == null)
            {
                DataSet ds = GetModuleList();
                if (Tools.IsValidDs(ds))
                {
                    dic = new Dictionary<int, string>();
                    foreach (DataRow dr in ds.Tables[0].Rows)
                    {
                        dic.Add(Tools.GetInt(dr["ModuleId"], 0), Tools.GetStr(dr["ModulePath"], ""));
                    }
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, dic);
                }
            }
            return dic;
        }

        /// <summary>
        /// 新增模块
        /// 返回模块ID，0=异常失败
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int AddModule(ModuleInfo info)
        {
            string sql = @"insert into RightModules(ParentId,ModuleName,ModuleFlag,ModulePath,ModuleType,OrderNo,PermType,PermDefine,ModuleUri,ActionValue,ModuleDesc,Status)
values(@ParentId,@ModuleName,@ModuleFlag,@ModulePath,@ModuleType,@OrderNo,@PermType,@PermDefine,@ModuleUri,@ActionValue,@ModuleDesc,@Status); select last_insert_id();";

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@ParentId", info.ParentId));
            paramList.Add(new MySqlParameter("@ModuleName", info.ModuleName));
            paramList.Add(new MySqlParameter("@ModuleFlag", info.ModuleFlag));
            paramList.Add(new MySqlParameter("@ModulePath", info.ModulePath));
            paramList.Add(new MySqlParameter("@ModuleType", info.ModuleType));
            paramList.Add(new MySqlParameter("@OrderNo", info.OrderNo));
            paramList.Add(new MySqlParameter("@PermType", info.PermType));
            paramList.Add(new MySqlParameter("@PermDefine", info.PermDefine));
            paramList.Add(new MySqlParameter("@ModuleUri", info.ModuleUrl));
            paramList.Add(new MySqlParameter("@ActionValue", info.ActionValue));
            paramList.Add(new MySqlParameter("@ModuleDesc", info.ModuleDesc));
            paramList.Add(new MySqlParameter("@Status", info.Status));

            object ret = MySqlHelper.ExecuteScalar(_connStr, sql, paramList.ToArray());
            int id = Tools.GetInt(ret, 0);
            if (id > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("rightModuleList", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightModuleNameDic", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightModulePathDic", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserModules", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightRightsDic", true);
                return id;
            }
            return 0;
        }

        /// <summary>
        /// 设置用户信息
        /// 返回值：1=成功，0=失败
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int SetModuleInfo(ModuleInfo info)
        {
            string sql = @"update RightModules set ParentId=@ParentId,ModuleName=@ModuleName,ModuleFlag=@ModuleFlag,ModulePath=@ModulePath,ModuleType=@ModuleType,
    OrderNo=@OrderNo,PermType=@PermType,PermDefine=@PermDefine,ModuleUri=@ModuleUri,ActionValue=@ActionValue,ModuleDesc=@ModuleDesc,Status=@Status where ModuleId=@ModuleId";

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@ParentId", info.ParentId));
            paramList.Add(new MySqlParameter("@ModuleName", info.ModuleName));
            paramList.Add(new MySqlParameter("@ModuleFlag", info.ModuleFlag));
            paramList.Add(new MySqlParameter("@ModulePath", info.ModulePath));
            paramList.Add(new MySqlParameter("@ModuleType", info.ModuleType));
            paramList.Add(new MySqlParameter("@OrderNo", info.OrderNo));
            paramList.Add(new MySqlParameter("@PermType", info.PermType));
            paramList.Add(new MySqlParameter("@PermDefine", info.PermDefine));
            paramList.Add(new MySqlParameter("@ModuleUri", info.ModuleUrl));
            paramList.Add(new MySqlParameter("@ActionValue", info.ActionValue));
            paramList.Add(new MySqlParameter("@ModuleDesc", info.ModuleDesc));
            paramList.Add(new MySqlParameter("@Status", info.Status));
            paramList.Add(new MySqlParameter("@ModuleId", info.ModuleId));

            int ret = MySqlHelper.ExecuteNonQuery(_connStr, sql, paramList.ToArray());
            if (ret == 1)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("rightModuleList", true);
                nwbase_utils.Cache.CacheHelper.DelCache(string.Format("rightModuleInfo_{0}", info.ModuleId.ToString()), true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightModuleNameDic", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightModulePathDic", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserModules", true);
                return 1;
            }
            return 0;
        }


        /// <summary>
        /// 获取用户角色列表
        /// </summary>
        /// <returns></returns>
        public DataSet GetUserRoleList()
        {
            string cacheName = "rightUserRoleList";
            DataSet ds = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as DataSet;
            if (ds == null)
            {
                string sql = @"select UserId,RoleId from RightUserRoles";
                ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, ds);
            }
            return ds;
        }

        /// <summary>
        /// 获取角色模块列表
        /// </summary>
        /// <returns></returns>
        public DataSet GetRoleModuleList()
        {
            string cacheName = "rightRoleModuleList";
            DataSet ds = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as DataSet;
            if (ds == null)
            {
                string sql = @"select RoleType,RoleId,ModuleId,Rights from RightRoleModules";
                ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, ds);
            }
            return ds;
        }

        /// <summary>
        /// 获取用户模块列表
        /// （根据用户查角色，根据角色查模块，然后或运算计算权限值）
        /// </summary>
        /// <returns> userid=>(moduleid=>rights) </returns>
        public Dictionary<int, Dictionary<int, int>> GetUserRights()
        {
            string cacheName = "rightRightsDic";
            Dictionary<int, Dictionary<int, int>> dic = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as Dictionary<int, Dictionary<int, int>>;
            if (dic == null)
            {
                // 用户0承载一些无需授权的模块，用户1代表超级管理员，每个用户都会附加用户0的授权
                string sql = @"select b.RoleType,b.RoleId,a.UserId,b.ModuleId,b.Rights from RightUserRoles a,RightRoleModules b,RightModules c where a.RoleId>1 and a.RoleId=b.RoleId and b.RoleType=1 and b.ModuleId=c.ModuleId and c.Status<>1 and b.Rights>0
union select a.RoleType,a.RoleId,a.RoleId as UserId,a.ModuleId,a.Rights from RightRoleModules a,RightModules b where a.RoleType=2 and a.ModuleId=b.ModuleId and b.Status<>1 and a.Rights>0
union select 3 as RoleType,0 as RoleId,a.UserId,b.ModuleId,511 as Rights from RightUserRoles a,RightModules b where a.RoleId=1 and b.ModuleType>1 and b.PermType=1 and b.Status<>1
union select 4 as RoleType,0 as RoleId,0 as UserId,ModuleId,511 as Rights from RightModules where ModuleType>1 and PermType>1 and Status<>1  ;";
                DataSet ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                {
                    dic = new Dictionary<int, Dictionary<int, int>>();
                    Dictionary<int, int> dicTemp = new Dictionary<int, int>();
                    int userId, moduleId, rights;
                    foreach (DataRow dr in ds.Tables[0].Rows)
                    {
                        userId = Tools.GetInt(dr["UserId"], 0);
                        moduleId = Tools.GetInt(dr["ModuleId"], 0);
                        rights = Tools.GetInt(dr["Rights"], 0);
                        if (!dic.ContainsKey(userId))
                        {
                            // 不包含用户时，增加用户且增加用户对应当前模块的权限
                            dicTemp = new Dictionary<int, int>();
                            dicTemp.Add(moduleId, rights);
                            dic.Add(userId, dicTemp);
                        }
                        else if (!dic[userId].ContainsKey(moduleId))
                        {
                            // 包含用户但不包含当前模块时，增加用户对应当前模块的权限
                            dic[userId].Add(moduleId, rights);
                        }
                        else
                        {
                            // 包含用户且包含当前模块时，利用位运算更新当前用户当前模块的权限值
                            dic[userId][moduleId] = dic[userId][moduleId] | rights;
                        }
                    }
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, dic);
                }
            }
            return dic;
        }


        /// <summary>
        /// 设置用户的所属角色
        /// implement by kezesong 
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="roleIds">多个角色ID</param>
        /// <returns></returns>
        public int SetUserRoles(int userId, int[] roleIds)
        {
            string sqlText = @"delete from RightUserRoles where UserId=@UserId; ";
            StringBuilder sb = new StringBuilder();
            foreach (int roleId in roleIds)
            {
                sb.AppendFormat(@"insert into RightUserRoles(UserId,RoleId) values(@UserId,{0});", roleId);
            }
            sqlText += sb.ToString();

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@UserId", userId));

            int result = MySqlHelper.ExecuteNonQuery(_connStr, sqlText, paramList.ToArray());

            if (result > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("rightUserRoleList", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightRightsDic", true);
            }
            return result;
        }
        /// <summary>
        /// 设置角色的所属模块
        /// implment by kezesong 2014-04-17
        /// </summary>
        /// <param name="roleId">角色ID</param>
        /// <param name="module_rights">模块与权限值</param>
        /// <returns></returns>
        public int SetRoleModules(string parentModuleId, int roleId, Dictionary<int, int> module_rights)
        {
            DataRow[] modules = GetModuleList().Tables[0].Select(" ModuleId=" + parentModuleId);
            if (modules.Length == 0)
            {
                return 0;
            }
            string modulePath = modules[0]["ModulePath"].ToString();
            DataRow[] rows = GetModuleList().Tables[0].Select(string.Format(" modulePath like '%{0}%'", parentModuleId));
            StringBuilder sbModuleId = new StringBuilder();
            foreach (DataRow row in rows)
            {
                sbModuleId.Append(row["ModuleId"]).Append(",");
            }

            string moduleids = sbModuleId.ToString() + " -1";

            //先删除某一个父目录下的所有模块，然后再加入新的模块Id
            string sqlText = string.Format("delete from RightRoleModules where RoleId=@RoleId and RoleType= 1 and ModuleId in ({0});", moduleids);
            StringBuilder sb = new StringBuilder();
            foreach (int moduleId in module_rights.Keys)
            {
                sb.AppendFormat(" insert into RightRoleModules(RoleType,RoleId,ModuleId,Rights) values(@RoleType,@RoleId,{0},{1});", moduleId, module_rights[moduleId]);
            }
            sqlText += sb.ToString();

            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@RoleType", 1));
            paramList.Add(new MySqlParameter("@RoleId", roleId));


            int result = MySqlHelper.ExecuteNonQuery(_connStr, sqlText, paramList.ToArray());

            if (result > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("rightRoleModuleList", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightRightsDic", true);
            }
            return result;


        }
        /// <summary>
        /// 设置角色所属用户
        /// </summary>
        /// <param name="roleId">角色ID</param>
        /// <param name="userIds">多个用户ID</param>
        /// <returns></returns>
        public int SetRoleUsers(int roleId, int[] userIds)
        {
            return 0;
        }
        /// <summary>
        /// 设置模块所属角色
        /// </summary>
        /// <param name="moduleId">模块ID</param>
        /// <param name="role_rights">角色与权限值</param>
        /// <returns></returns>
        public int SetModuleRoles(int moduleId, Dictionary<int, int> role_rights)
        {
            return 0;
        }


        /// <summary>
        /// 查询系统所有的角色列表
        /// added by kezesong 
        /// </summary>
        /// <returns></returns>
        public DataSet GetRightRoles()
        {
            string cacheName = "RightRolesList";
            DataSet ds = nwbase_utils.Cache.CacheHelper.GetCache(cacheName) as DataSet;
            if (ds == null)
            {
                string sql = @"select RoleId,TheProj,RoleName,RoleDesc,Status from RightRoles";
                ds = MySqlHelper.ExecuteDataset(_connStr, sql);
                if (Tools.IsValidDs(ds))
                    nwbase_utils.Cache.CacheHelper.SetCache(cacheName, ds);
            }
            return ds;
        }

        /// <summary>
        /// 添加系统角色
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int AddRightRoles(RoleInfo info)
        {
            string sqlText = @"insert into RightRoles(TheProj,RoleName,RoleDesc,Status) values(@TheProj,@RoleName,@RoleDesc,@Status);select last_insert_id();";
            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@TheProj", info.TheProj));
            paramList.Add(new MySqlParameter("@RoleName", info.RoleName));
            paramList.Add(new MySqlParameter("@RoleDesc", info.RoleDesc));
            paramList.Add(new MySqlParameter("@Status", info.Status));


            object ret = MySqlHelper.ExecuteScalar(_connStr, sqlText, paramList.ToArray());
            int id = Tools.GetInt(ret, 0);
            if (id > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("RightRolesList", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightRightsDic", true); //同时删除用户权限缓存
                return id;
            }
            return 0;
        }

        /// <summary>
        /// 修改角色信息
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <returns></returns>
        public int SetRightRoles(RoleInfo info)
        {
            string sqlText = @"update RightRoles set TheProj=@TheProj,RoleName=@RoleName,RoleDesc=@RoleDesc,Status=@Status where RoleId=@RoleId";
            List<MySqlParameter> paramList = new List<MySqlParameter>();
            paramList.Add(new MySqlParameter("@TheProj", info.TheProj));
            paramList.Add(new MySqlParameter("@RoleName", info.RoleName));
            paramList.Add(new MySqlParameter("@RoleDesc", info.RoleDesc));
            paramList.Add(new MySqlParameter("@Status", info.Status));
            paramList.Add(new MySqlParameter("@RoleId", info.RoleId));

            int result = MySqlHelper.ExecuteNonQuery(_connStr, sqlText, paramList.ToArray());
            if (result > 0)
            {
                nwbase_utils.Cache.CacheHelper.DelCache("RightRolesList", true);
                nwbase_utils.Cache.CacheHelper.DelCache("rightRightsDic", true); //同时删除用户权限缓存
                return result;
            }
            return 0;
        }
    }
}