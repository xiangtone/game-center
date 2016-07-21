using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;


namespace AppStore.Web
{
    public class UserInfo
    {
        public int UserId = 0;
        public int TeamType = 0;
        public int TeamRefId = 0;
        public string TeamName = "";
        public string TeamFlag = "";
        public string UserName = "";
        //public string UserPwd = "";
        public string RealName = "";
        public string NickName = "";
        public string CreateTime = "";
        public int Status = 0;

        private string _UserPwd;

        public string UserPwd
        {
            get
            {
                return _UserPwd;
            }
            set
            {
                _UserPwd = value;
                RawUserPwd = string.Empty;
                PwdSalt = string.Empty;

                if (_UserPwd != null && _UserPwd.Contains(':'))
                {
                    var pwdPart = _UserPwd.Split(':');
                    if (pwdPart.Length == 2)
                    {
                        RawUserPwd = pwdPart[0];
                        PwdSalt = pwdPart[1];
                    }
                }
                else
                    RawUserPwd = _UserPwd;
            }
        }

        public string RawUserPwd { get; set; }
        public string PwdSalt { get; set; }
    }

    public class UserSearchInfo
    {
        public int ModuleId = 0;
        public int RoleId = 0;
        public int TeamType = 0;
        public int TeamRefId = 0;
        public string TeamNameStr = "";
        public string UserNameStr = "";
        public int Status = 0;
        public int StartRec = 0;
        public int PageSize = 30;
        public string OrderStr = "";
    }

    public class RoleInfo
    {
        public int RoleId = 0;
        public int TheProj = 0;
        public string RoleName = "";
        public string RoleDesc = "";
        public int Status = 0;
    }

    public class RoleSearchInfo
    {
        public int TheProj = 0;
        public string RoleNameStr = "";
        //modify by kezesong 2014-4-26
        public int Status = 0;
        public int StartRec = 0;
        public int PageSize = 30;
    }

    public class ModuleInfo
    {
        public int ModuleId = 0;
        public int ParentId = 0;
        public string ModuleName = "";
        public string ModuleFlag = "";
        public string ModulePath = "";
        public int ModuleType = 0;
        public int OrderNo = 0;
        public int PermType = 0;
        public string PermDefine = "";
        public string ModuleUrl = "";
        public string ActionValue = "";
        public string ModuleDesc = "";
        public string CreateTime = "";
        public int Status = 0;
    }

    public class ModuleSearchInfo
    {
        public int RoleId = 0;
        public int ParentId = 0;
    }
}