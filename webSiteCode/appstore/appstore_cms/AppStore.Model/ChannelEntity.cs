using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class ChannelEntity : BaseEntity
    {
        /// <summary>
        /// 渠道号
        /// </summary>
        public int ChannelNO { get; set; }

        /// <summary>
        /// 渠道名
        /// </summary>
        public string ChannelName { get; set; }

        /// <summary>
        /// 渠道标识
        /// </summary>
        public string ChannelFlag { get; set; }

        /// <summary>
        /// 备注
        /// </summary>
        public string Remarks { get; set; }

        /// <summary>
        /// 创建日期
        /// </summary>
        public DateTime CreateTime { get; set; }

        /// <summary>
        /// 更新日期
        /// </summary>
        public DateTime UpdateTime { get; set; }

        /// <summary>
        /// 状态
        /// </summary>
        public int Status { get; set; }

    }
}
