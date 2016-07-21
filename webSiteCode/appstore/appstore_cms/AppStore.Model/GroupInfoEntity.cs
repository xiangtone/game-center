using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class GroupInfoEntity : BaseEntity
    {
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
        /// 分组名称
        /// </summary>
        public string GroupName { get; set; }

        /// <summary>
        /// 分组描述
        /// </summary>
        public string GroupDesc { get; set; }

        /// <summary>
        /// 分组图片URL
        /// </summary>
        public string GroupPicUrl { get; set; }

        /// <summary>
        /// 推荐语，如果该值为空串，则客户端显示为包含应用数
        /// </summary>
        public string GroupTips { get; set; }

        /// <summary>
        /// 开始时间
        /// </summary>
        public DateTime StartTime { get; set; }

        /// <summary>
        /// 结束时间
        /// </summary>
        public DateTime EndTime { get; set; }

      

        #region 扩展字段

        /// <summary>
        /// 分组类别
        /// </summary>
        public int TypeClass { get; set; }

        /// <summary>
        /// 类别名称
        /// </summary>
        public string TypeName { get; set; }

        /// <summary>
        /// 类别描述
        /// </summary>
        public string TypeDesc { get; set; }

        /// <summary>
        /// 类别图片
        /// </summary>
        public string TypePicUrl { get; set; }

        /// <summary>
        /// 应用Icon
        /// </summary>
        public string MainIconPicUrl { get; set; }

        public int AppCount { get; set; }

        public string TopAppNames { get; set; }

        /// <summary>
        /// 组元素数量
        /// </summary>
        public int ElementCount { get; set; }

        /// <summary>
        /// 搜索关键字
        /// </summary>
        public string SearchKeys { get; set; }

        /// <summary>
        /// 排序方式
        /// </summary>
        public string SearchOrderType;

        /// <summary>
        /// 搜索分组类型
        /// </summary>
        public string SearchGroupType;

        /// <summary>
        /// 搜索分组状态：1为有效数据，0为无效数据，""为全部
        /// </summary>
        public string SearchStatus;


        /// <summary>
        /// 方案ID
        /// </summary>
        public int SchemeID { get; set; }

        #endregion

    }
}
