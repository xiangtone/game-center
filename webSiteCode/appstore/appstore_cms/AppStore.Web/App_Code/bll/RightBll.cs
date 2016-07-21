using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;
using System.Text;
using nwbase_utils;

namespace AppStore.Web
{
    public class RightBll 
    {
        RightDal dal = new RightDal();

        public RightBll()
        {
        }


        /// <summary>
        /// 获取用户列表
        /// 返回用户列表于DataSet.Tables[0]中，DataSet为NULL表示获取失败
        /// </summary>
        /// <returns></returns>
        public DataSet GetUserList()
        {
            return dal.GetUserList();
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
            return dal.GetUserInfo(userId, userName);
        }

        /// <summary>
        /// 新增用户
        /// 返回新增用户的ID：0=异常失败，-1=用户名已经存在
        /// </summary>
        /// <param name="info">用户信息实体，有效字段包括：TeamType/TeamId/UserName/UserPwd/RealName/NickName/Status</param>
        /// <returns></returns>
        public int AddUser(UserInfo info)
        {
            return dal.AddUser(info);
        }

        /// <summary>
        /// 设置用户信息
        /// 返回值：1=成功，0=失败，-1=用户名已经存在
        /// </summary>
        /// <param name="info">用户信息实体，有效字段包括：UserId/UserName/RealName/NickName/Status</param>
        /// <returns></returns>
        public int SetUserInfo(UserInfo info)
        {
            return dal.SetUserInfo(info);
        }

        /// <summary>
        /// 删除用户
        /// </summary>
        /// <param name="userId"></param>
        /// <returns></returns>
        public bool DelUser(int userId)
        {
            return dal.DelUser(userId);
        }

        /// <summary>
        /// 设置用户密码（用于用户自己修改密码）
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="userName"></param>
        /// <param name="oldUserPwd"></param>
        /// <param name="newUserPwd"></param>
        /// <returns></returns>
        public int SetUserPwd(int userId, string userName, string oldUserPwd, string newUserPwd)
        {
            return dal.SetUserPwd(userId, userName, oldUserPwd, newUserPwd, true);
        }

        /// <summary>
        /// 设置用户密码（用于管理员重置用户密码）
        /// </summary>
        /// <param name="userId"></param>
        /// <param name="userName"></param>
        /// <param name="newUserPwd"></param>
        /// <returns></returns>
        public int SetUserPwd(int userId, string userName, string newUserPwd)
        {
            return dal.SetUserPwd(userId, userName, "", newUserPwd, false);
        }

        /// <summary>
        /// 验证用户登录
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="userName">用户名</param>
        /// <param name="userPwd">用户密码（原始密码，未加密）</param>
        /// <returns></returns>
        public int AuthUser(int userId, string userName, string userPwd)
        {
            if ((userId < 1 && userName.Length < 1) || userPwd.Length < 1)
                return -1;

            UserInfo info = dal.GetUserInfo(userId, userName);
            if (info != null)
            {

                if (info.Status == (int)UserStatueEnum.UnActive)
                {
                    return (int)UserStatueEnum.UnActive;
                }
                else if (info.Status != 0)
                    return (int)UserStatueEnum.UnNormol;
                else
                {
                    //userPwd = CryptHelper.MD5CmsUserPwd(userPwd);
                    // db pwd = md5(md5(raw_pwd) + salt)
                    userPwd = nwbase_utils.Encryption.MD5Hash(userPwd);

                    if (info.RawUserPwd == nwbase_utils.Encryption.MD5HashWithSalt(userPwd, info.PwdSalt))
                        return info.UserId;
                    else
                        return -1;
                }
            }
            return 0;
        }

        /// <summary>
        /// 验证用户登录
        /// </summary>
        /// <param name="userName">用户名</param>
        /// <param name="userPwd">用户密码（原始密码，MD5）</param>
        /// <returns>0=成功，-1=密码错误，-2=账号不存在，-3=账号状态异常</returns>
        public int AuthUser(string userName, string userPwd)
        {
            UserInfo info = dal.GetUserInfo(0, userName);
            if (info != null)
            {
                if (info.Status != (int)UserStatueEnum.Normol)
                {
                    //账号状态异常
                    return -3;
                }
                else
                {
                    //userPwd = nwbase_utils.Encryption.MD5Hash(userPwd);
                    if (info.RawUserPwd == nwbase_utils.Encryption.MD5HashWithSalt(userPwd, info.PwdSalt))
                    {
                        //验证成功
                        return 0;
                    }
                    else
                        return -1; // 密码错误
                }
            }
            else
                return -2; // 账号不存在
        }

        /// <summary>
        /// 获取用户列表
        /// 返回用户列表于DataSet.Tables[0]中，DataSet为NULL表示获取失败
        /// </summary>
        /// <param name="info">用户信息，有效参数：NameStr/Status</param>
        /// <param name="listCount">返回过滤但不分页情况下的列表大小</param>
        /// <returns></returns>
        public DataSet GetUsers(UserSearchInfo info, out int listCount)
        {
            listCount = 0;
            DataSet ds = dal.GetUserList();
            if (ds == null)
                return null;
            string filter = "1=1";
            filter += (info.UserNameStr.Length > 0) ? string.Format(" and (userName like '%{0}%' or nickName like '%{0}%' or realName like '%{0}%')", info.UserNameStr) : "";
            filter += (info.Status > -1) ? string.Format(" and (status={0})", info.Status.ToString()) : " and (status<2)";
            DataRow[] drs = ds.Tables[0].Select(filter, "userId desc");
            listCount = drs.Length;
            if (listCount > 0)
            {
                return Tools.GetTableDs(Tools.GetDt4Drs(drs, info.StartRec, info.StartRec + info.PageSize - 1), true);
            }
            return Tools.GetTableDs(null, false);
        }


        /// <summary>
        /// 获取模块列表
        /// 返回模块列表于DataSet.Tables[0]中，DataSet为空表示获取失败
        /// </summary>
        /// <returns></returns>
        public DataSet GetModuleList()
        {
            return dal.GetModuleList();
        }

        /// <summary>
        /// 获取模块信息
        /// 返回模块信息，Null表示获取失败
        /// </summary>
        /// <param name="moduleId"></param>
        /// <returns></returns>
        public ModuleInfo GetModuleInfo(int moduleId)
        {
            return dal.GetModuleInfo(moduleId);
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
            return dal.GetModuleInfo(requri, actval);
        }

        /// <summary>
        /// 获取模块ID与模块名对应表
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, string> GetModuleNameDic()
        {
            return dal.GetModuleNameDic();
        }

        /// <summary>
        /// 新增模块
        /// 返回模块ID，0=异常失败
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int AddModule(ModuleInfo info)
        {
            return dal.AddModule(info);
        }

        /// <summary>
        /// 设置用户信息
        /// 返回值：1=成功，0=失败
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int SetModuleInfo(ModuleInfo info)
        {
            return dal.SetModuleInfo(info);
        }

        /// <summary>
        /// 获取某个模块的所有子级页面类型的模块
        /// </summary>
        /// <param name="parentId">父级模块的ID</param>
        /// <returns></returns>
        public DataTable GetSeekupModules(int parentId)
        {
            DataSet ds = dal.GetModuleList();
            if (Tools.IsValidDs(ds))
            {
                DataRow[] drs = ds.Tables[0].Select(string.Format("ModuleType>1 and Status<>1 and ModulePath like '%,{0},%'", parentId.ToString()), "ModulePath,OrderNo");
                return Tools.GetDt4Drs(drs);
            }
            return null;
        }


        /// <summary>
        /// 获取用户角色列表
        /// </summary>
        /// <returns></returns>
        public DataSet GetUserRoleList()
        {
            return dal.GetUserRoleList();
        }

        /// <summary>
        /// 获取角色模块列表
        /// </summary>
        /// <returns></returns>
        public DataSet GetRoleModuleList()
        {
            return dal.GetRoleModuleList();
        }

        /// <summary>
        /// 获取用户模块权限集合
        /// （根据用户查角色，根据角色查模块，然后或运算计算权限值）
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, Dictionary<int, int>> GetUserRights()
        {
            return dal.GetUserRights();
        }

        /// <summary>
        /// 获取某一用户模块权限集合
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <returns></returns>
        public Dictionary<int, int> GetUserRights(int userId)
        {
            Dictionary<int, Dictionary<int, int>> dic = dal.GetUserRights();
            if (dic != null && dic.ContainsKey(userId))
                return dic[userId];
            return null;
        }

        /// <summary>
        /// 设置用户的所属角色
        /// </summary>
        /// <param name="userId">用户ID</param>
        /// <param name="roleIds">多个角色ID</param>
        /// <returns></returns>
        public int SetUserRoles(int userId, int[] roleIds)
        {
            return dal.SetUserRoles(userId, roleIds);
        }

        /// <summary>
        /// 设置角色的所属模块
        /// </summary>
        /// <param name="parentModuleId">当前模块列表的父目录ID</param>
        /// <param name="roleId">角色ID</param>
        /// <param name="module_rights">模块与权限值</param>
        /// <returns></returns>
        public int SetRoleModules(string parentModuleId, int roleId, Dictionary<int, int> module_rights)
        {
            return dal.SetRoleModules(parentModuleId, roleId, module_rights);
        }

        /// <summary>
        /// 设置角色所属用户
        /// </summary>
        /// <param name="roleId">角色ID</param>
        /// <param name="userIds">多个用户ID</param>
        /// <returns></returns>
        public int SetRoleUsers(int roleId, int[] userIds)
        {
            return dal.SetRoleUsers(roleId, userIds);
        }

        /// <summary>
        /// 设置模块所属角色
        /// </summary>
        /// <param name="moduleId">模块ID</param>
        /// <param name="role_rights">角色与权限值</param>
        /// <returns></returns>
        public int SetModuleRoles(int moduleId, Dictionary<int, int> role_rights)
        {
            return dal.SetModuleRoles(moduleId, role_rights);
        }


        /// <summary>
        /// 获取用户对于某一父级模块的所属模块列表，用于菜单展示
        /// </summary>
        /// <param name="parentId">父级模块的ID</param>
        /// <returns></returns>
        public DataTable GetUserModules(int userId)
        {
            Dictionary<int, Dictionary<int, int>> dic = dal.GetUserRights();
            // dicUserRights 包含该用户所有的 moduleId=>rights
            Dictionary<int, int> dicUserRights = new Dictionary<int, int>();
            if (dic.ContainsKey(userId))
            {
                dicUserRights = dic[userId];
            }
            if (dic.ContainsKey(0))
            {
                foreach (KeyValuePair<int, int> kv in dic[0])
                {
                    if (!dicUserRights.ContainsKey(kv.Key))
                        dicUserRights.Add(kv.Key, kv.Value);
                }
            }

            Dictionary<int, string> dicModulePath = dal.GetModulePathDic();
            StringBuilder sb = new StringBuilder();
            foreach (int mid in dicUserRights.Keys)
            {
                if (dicModulePath[mid].Length > 0)
                    sb.Append(dicModulePath[mid] + mid);
            }
            string moduleIdStr = sb.ToString().Substring(1);

            DataSet ds = dal.GetModuleList();
            if (Tools.IsValidDs(ds))
            {
                DataRow[] drs = ds.Tables[0].Select(string.Format("Status<>1 and ModuleId in ({0})", moduleIdStr), "ModulePath,OrderNo");
                return Tools.GetDt4Drs(drs);
            }
            return null;
        }


        /// <summary>
        /// 查询系统所有的角色列表
        /// added by kezesong 
        /// </summary>
        /// <returns></returns>
        public DataSet GetRightRoles()
        {
            return dal.GetRightRoles();
        }

        /// <summary>
        /// 根据条件搜索系统角色
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <param name="info"></param>
        /// <param name="listCount"></param>
        /// <returns></returns>
        public DataSet GetRightRoles(RoleSearchInfo info, out int listCount)
        {
            listCount = 0;
            DataSet ds = dal.GetRightRoles();
            if (ds == null)
                return null;
            string filter = "1=1";
            filter += (info.RoleNameStr.Length > 0) ? string.Format(" and (RoleName like '%{0}%' or RoleDesc like '%{0}%' )", info.RoleNameStr) : "";
            filter += (info.Status > -1) ? string.Format(" and (status={0})", info.Status.ToString()) : "";
            DataRow[] drs = ds.Tables[0].Select(filter, "RoleId desc");
            listCount = drs.Length;
            if (listCount > 0)
            {
                return Tools.GetTableDs(Tools.GetDt4Drs(drs, info.StartRec, info.StartRec + info.PageSize - 1), true);
            }
            return Tools.GetTableDs(null, false);
        }

        public ModuleMenu GetAllMenu()
        {
            return ModuleMenu.BuildTree(dal.GetModuleListDict());
        }

        public ModuleMenu GetUserMenu(int userId)
        {
            Dictionary<int, Dictionary<int, int>> dic = dal.GetUserRights();
            // dicUserRights 包含该用户所有的 moduleId=>rights
            Dictionary<int, int> dicUserRights = new Dictionary<int, int>();
            if (dic.ContainsKey(userId))
            {
                dicUserRights = dic[userId];
            }
            if (dic.ContainsKey(0))
            {
                foreach (KeyValuePair<int, int> kv in dic[0])
                {
                    if (!dicUserRights.ContainsKey(kv.Key))
                        dicUserRights.Add(kv.Key, kv.Value);
                }
            }
            return ModuleMenu.BuildTree(dal.GetModuleListDict(), dicUserRights.Keys);
        }


        /// <summary>
        /// 根据RoleId查询角色信息
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <param name="roleId"></param>
        /// <returns></returns>
        public RoleInfo GetRoleInfo(int roleId)
        {
            DataSet ds = dal.GetRightRoles();
            if (Tools.IsValidDs(ds))
            {
                DataRow[] dataRows = ds.Tables[0].Select(" RoleId=" + roleId);
                if (dataRows.Length > 0)
                {
                    RoleInfo info = new RoleInfo();
                    info.RoleId = Tools.GetInt(dataRows[0]["RoleId"], 0);
                    info.RoleName = Tools.GetStr(dataRows[0]["RoleName"], string.Empty);
                    info.Status = Tools.GetInt(dataRows[0]["Status"], 0);
                    info.TheProj = Tools.GetInt(dataRows[0]["TheProj"], 0);
                    info.RoleDesc = Tools.GetStr(dataRows[0]["RoleDesc"], string.Empty);
                    return info;
                }
            }

            return null;
        }

        /// <summary>
        /// 添加系统角色
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <param name="info"></param>
        /// <returns></returns>
        public int AddRightRoles(RoleInfo info)
        {
            return dal.AddRightRoles(info);
        }


        /// <summary>
        /// 修改角色信息
        /// added by kezesong 2014-4-16
        /// </summary>
        /// <returns></returns>
        public int SetRightRoles(RoleInfo info)
        {
            return dal.SetRightRoles(info);
        }
    }
}