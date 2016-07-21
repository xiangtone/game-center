using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class GroupSchemesEntity : BaseEntity
    {
        /// <summary>
        /// 方案ID
        /// </summary>
        public int SchemeID { get; set; }

        /// <summary>
        /// 分组ID
        /// </summary>
        public int GroupID { get; set; }

        /// <summary>
        /// 分组类型
        /// </summary>
        public int GroupTypeID { get; set; }

        /// <summary>
        /// 排序类型
        /// </summary>
        public int OrderType { get; set; }

        /// <summary>
        /// 类别ID （扩展字段）
        /// </summary>
        public int TypeClass { get; set; }


        /// <summary>
        /// 分组名称（扩展字段）
        /// </summary>
        public string GroupName { get; set; }


        /// <summary>
        /// 类别名称（扩展字段）
        /// </summary>
        public string TypeName { get; set; }
    }
}
