using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class AppCommentSummaryEntity : BaseEntity
    {

        /// <summary>
        /// 评论次数
        /// </summary>
        public int CommentTimes { get; set; }

        /// <summary>
        /// 评分次数
        /// </summary>
        public int ScoreTimes { get; set; }
        /// <summary>
        /// 总得分，5分制总得分
        /// </summary>
        public int ScoreSum { get; set; }
        /// <summary>
        /// 平均评分，0代表无评分，1~10代表从差评到好评
        /// </summary>
        public int ScoreAvg { get; set; }
        /// <summary>
        /// 评分为1的次数
        /// </summary>
        public int ScoreTimes1 { get; set; }
        /// <summary>
        /// 评分为2的次数
        /// </summary>
        public int ScoreTimes2 { get; set; }
        /// <summary>
        /// 评分为3的次数
        /// </summary>
        public int ScoreTimes3 { get; set; }
        /// <summary>
        /// 评分为4的次数
        /// </summary>
        public int ScoreTimes4 { get; set; }
        /// <summary>
        /// 评分为5的次数
        /// </summary>
        public int ScoreTimes5 { get; set; }
    }
}
