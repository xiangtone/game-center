using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace updatesys_cms.Model
{
    /// <summary>
    /// 基础应用信息，包括AppId，AppKey
    /// </summary>
    public class AppInfo
    {
        public int AppID { get; set; }

        public string AppName { get; set; }

        public string PackFlag { get; set; }

        public string AppToken { get; set; }

        public DateTime CreateTime { get; set; }

        /// <summary>
        /// 状态，0=正常，1=禁用
        /// </summary>
        public int Status { get; set; }
    }
}
