using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class AppCommentsEntity : BaseEntity
    {
        /// <summary>
        /// 评论ID，由前台生成
        /// </summary>
        public long CommentID { get; set; }

        /// <summary>
        /// 用户ID
        /// </summary>
        public int UserID { get; set; }

        /// <summary>
        /// 用户名
        /// </summary>
        public string UserName { get; set; }

        /// <summary>
        /// 用户设备号
        /// </summary>
        public string UserEI { get; set; }


        /// <summary>
        /// 用户评分，0代表无评分，1~5代表从差评到好评
        /// </summary>
        public int UserScore { get; set; }

         
        /// <summary>
        /// 本地版本代码
        /// </summary>
        public int LocalVerCode { get; set; }

        /// <summary>
        /// 本地版本号
        /// </summary>
        public string LocalVerName { get; set; }

        /// <summary>
        /// 评论内容，空串代表无评论
        /// </summary>
        public string Comments { get; set; }

        /// <summary>
        /// 评论时间
        /// </summary>
        public DateTime CommentTime { get; set; }

        /// <summary>
        /// 审核状态：0=审核不通过，1=审核通过
        /// </summary>
        public int AuditStatus { get; set; }

        

    }
}
