using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class LinkInfoEntity : BaseEntity
    {
        /// <summary>
        /// 链接ID、主键自增
        /// </summary>
        public int LinkID { get; set; }

        /// <summary>
        /// 网站名称
        /// </summary>
        public string LinkName { get; set; }

        /// <summary>
        /// 显示名称
        /// </summary>
        public string ShowName { get; set; }

        /// <summary>
        /// 开发者ID，关联基础库开发者信息
        /// </summary>
        public int CPID { get; set; }

        /// <summary>
        /// 开发者名称
        /// </summary>
        public string DevName { get; set; }

        /// <summary>
        /// ICON图URL地址
        /// </summary>
        public string IconUrl { get; set; }

        /// <summary>
        /// 实际链接地址
        /// </summary>
        public string LinkUrl { get; set; }

        /// <summary>
        /// 合作类型，例如：1=CPC，2=CPL...
        /// </summary>
        public int CoopType { get; set; }

        /// <summary>
        /// 外链标签，例如：1=独家，2=首发，4=...位运算
        /// </summary>
        public int LinkTag { get; set; }

        /// <summary>
        /// 外链描述
        /// </summary>
        public string LinkDesc { get; set; }

        ///// <summary>
        ///// 运营状态，定义：1=正常，2=禁用
        ///// </summary>
        //public int OpStatus { get; set; }

    }
}
