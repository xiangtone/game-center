using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    [Serializable]
    public class BaseEntity
    {

        /// <summary>
        /// 应用ID
        /// </summary>
        public int AppID { get; set; }

        /// <summary>
        /// 安装包ID，元素类型不为App时，填0
        /// </summary>
        public int PackID { get; set; }

        /// <summary>
        /// 排序号
        /// </summary>
        public int OrderNo { get; set; }

        /// <summary>
        /// 查询起始索引
        /// </summary>
        public int StartIndex { get; set; }

        /// <summary>
        /// 查询结束索引
        /// </summary>
        public int EndIndex { get; set; }

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
        /// 状态：1=正常，2=禁用，12=数据异常，22=控制禁用
        /// </summary>
        public int Status { get; set; }
    }
}
