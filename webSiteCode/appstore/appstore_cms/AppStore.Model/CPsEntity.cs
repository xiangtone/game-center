using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class CPsEntity 
    {
        /// <summary>
        /// 内容提供商Id
        /// </summary>
        public int CPID { get; set; }

        /// <summary>
        /// 内容提供商类型
        /// </summary>
        public int CPType { get; set; }

        /// <summary>
        /// 是否开发者
        /// </summary>
        public int IsDeveloper { get; set; }

        /// <summary>
        /// 开发者姓名
        /// </summary>
        public string CPName { get; set; }

        /// <summary>
        /// 开发者全名
        /// </summary>
        public string FullName { get; set; }

        /// <summary>
        /// 安装包索引
        /// </summary>
        //public string PackFlagIdx { get; set; }

        /// <summary>
        /// 排序号
        /// </summary>
        //public int OrderNo { get; set; }

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
