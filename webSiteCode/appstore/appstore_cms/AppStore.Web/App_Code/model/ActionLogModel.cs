using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;



namespace AppStore.Web
{
    /// <summary>
    /// ActionLogModel 的摘要说明
    /// </summary>
    public class ActionLogModel
    {
        public ActionLogModel()
        {
            //
            // TODO: 在此处添加构造函数逻辑
            //
        }

        public int LogId { get; set; }

        public int UserId { get; set; }

        public string UserName { get; set; }

        public string UserIP { get; set; }

        public string Action { get; set; }

        public string Content { get; set; }

        /// <summary>
        /// 日志等级（1=debug，2=info，3=error）
        /// </summary>
        public int Level { get; set; }

        public DateTime LogTime { get; set; }
    }
}