using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class AppPackCfgEntity:BaseEntity
    {
        /// <summary>
        /// 主键自增
        /// </summary>
        public int CfgID { get; set; }

        /// <summary>
        /// 语言ID
        /// </summary>
        public int  LanID { get; set; }

        /// <summary>
        /// 地区ID
        /// </summary>
        public int AreaID { get; set; }

        /// <summary>
        /// 平台ID
        /// </summary>
        public int PFID { get; set; }

        /// <summary>
        /// 机型ID
        /// </summary>
        public int ModelID { get; set; }

        /// <summary>
        /// 设备类型ID
        /// </summary>
        public int EquiTypeID { get; set; }

    }
}
