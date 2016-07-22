using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class PackInfoEntity : BaseEntity
    {

        /// <summary>
        /// 是否主版主
        /// </summary>
        public int IsMainVer { get; set; }

        /// <summary>
        /// 显示名称，显示给用户的名称
        /// </summary>
        public string ShowName { get; set; }

        /// <summary>
        /// 分发标识，如渠道号等
        /// </summary>
        public string IssueFlag { get; set; }

        /// <summary>
        /// 下载量
        /// </summary>
        public int DownTimes { get; set; }

        /// <summary>
        /// 真实下载量
        /// </summary>
        public int DownTimesReal { get; set; }

        /// <summary>
        /// 用户评论次数
        /// </summary>
        public int CommentTimes { get; set; }

        /// <summary>
        /// 用户评分
        /// </summary>
        public int CommentScore { get; set; }

        /// <summary>
        /// ICON图URL地址
        /// </summary>
        public string IconUrl { get; set; }
        /// <summary>
        /// ICON图URL地址
        /// </summary>
        public string IconUrl2 { get; set; }

        /// <summary>
        /// 应用截图，URL之间用英文逗号分隔
        /// </summary>
        public string AppPicUrl { get; set; }
        /// <summary>
        /// 应用截图，URL之间用英文逗号分隔
        /// </summary>
        public string AppPicUrl2 { get; set; }

        /// <summary>
        /// 合作类型 定义：1=联运，2=CPS，3=CPA，99=未合作
        /// </summary>
        public int CoopType { get; set; }

        /// <summary>
        /// 安装包来源：1=直接合作，2=自由市场，3=豌豆荚
        /// </summary>
        public int PackFrom { get; set; }

        /// <summary>
        /// 安装包大小
        /// </summary>
        public int PackSize { get; set; }

        /// <summary>
        /// 版本代码
        /// </summary>
        public int VerCode { get; set; }

        /// <summary>
        /// 版本号
        /// </summary>
        public string VerName { get; set; }

        /// <summary>
        /// 签名特征码，签名特征码相同，则可以升级
        /// </summary>
        public string PackSign { get; set; }

        /// <summary>
        /// 安装包地址
        /// </summary>
        public string PackUrl { get; set; }

        /// <summary>
        /// 安装包地址
        /// </summary>
        public string PackUrl2 { get; set; }

        public string permission { get; set; }

        /// <summary>
        /// 安装包MD5校验值
        /// </summary>
        public string PackMD5 { get; set; }

        /// <summary>
        /// 适用性说明
        /// </summary>
        public string CompDesc { get; set; }

        /// <summary>
        /// 更新描述
        /// </summary>
        public string UpdateDesc { get; set; }

        #region 扩展字段

        /// <summary>
        /// 安装包名
        /// </summary>
        public string PackName { get; set; }

        #endregion
    }
}
