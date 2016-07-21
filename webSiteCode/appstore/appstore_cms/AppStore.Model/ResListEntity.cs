using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class ResListEntity
    {
        /// <summary>
        /// 资源地址
        /// </summary>
        public string ResUrl { get; set; }

        /// <summary>
        /// 资源来源：1=帐号中心，2=应用商店
        /// </summary>
        public int ResForm { get; set; }

        /// <summary>
        /// 资源类型：11=应用Apk，12=应用ICON，13=应用缩略图，14=应用广告图，15=应用截图，21=链接图片，31=分类图片，41=专题图片
        /// </summary>
        public int ResFlag { get; set; }

        /// <summary>
        /// 资源源ID
        /// </summary>
        public int ResKey { get; set; }

    }
}
