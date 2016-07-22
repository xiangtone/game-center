using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class GroupElemsEntity : BaseEntity
    {
        /// <summary>
        /// 元素ID
        /// </summary>
        public int GroupElemID { get; set; }

        /// <summary>
        /// 分组ID
        /// </summary>
        public int GroupID { get; set; }

        /// <summary>
        /// 位置索引
        /// </summary>
        public int PosID { get; set; }

        /// <summary>
        /// 元素类型：1=App，2=Link，3=跳转至分类，4=跳转至网游或单机，5=跳转至专题，6=搜索词；（不支持跳转至推荐）
        /// </summary>
        public int ElemType { get; set; }

        /// <summary>
        /// 元素源ID，如应用（或Link、分类、专题）的ID
        /// </summary>
        public int ElemID { get; set; }

        /// <summary>
        /// 跳转至分组类型
        /// </summary>
        public int GroupType { get; set; }

        /// <summary>
        /// 跳转至分组的排序类型
        /// </summary>
        public int OrderType { get; set; }

        /// <summary>
        /// 推广权重
        /// </summary>
        public int RecommVal { get; set; }

        /// <summary>
        /// 推荐名称
        /// </summary>
        public string RecommTitle { get; set; }

        /// <summary>
        /// 推广语
        /// </summary>
        public string RecommWord { get; set; }

        /// <summary>
        /// 推广图片URL
        /// </summary>
        public string RecommPicUrl { get; set; }

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
        /// 显示名称
        /// </summary>
        public string ShowName { get; set; }

        /// <summary>
        /// LinkInfo表中的推荐语
        /// </summary>
        public string CRecommWord { get; set; }

        /// <summary>
        /// 推荐标识
        /// </summary>
        public int RecommFlag { get; set; }

        /// <summary>
        /// 推荐等级
        /// </summary>
        public int RecommLevel { get; set; }

        /// <summary>
        /// Icon地址
        /// </summary>
        public string MainIconUrl { get; set; }

        /// <summary>
        /// 推荐图片地址
        /// </summary>
        public string ThumbPicUrl { get; set; }

        /// <summary>
        /// 安装包包名
        /// </summary>
        public string PackName { get; set; }

        /// <summary>
        /// 安装包大小
        /// </summary>
        public string MainPackSize { get; set; }

        /// <summary>
        /// 主安装包ID
        /// </summary>
        public int MainPackID { get; set; }

        /// <summary>
        /// 签名代码
        /// </summary>
        public string MainSignCode { get; set; }

        public string PackSign { get; set; }

        /// <summary>
        /// 版本名
        /// </summary>
        public string MainVerName { get; set; }

        /// <summary>
        /// 版主代码
        /// </summary>
        public string MainVerCode { get; set; }

        /// <summary>
        /// 跳转ID
        /// </summary>
        public int LinkID { get; set; }

        /// <summary>
        /// 跳转地址
        /// </summary>
        public string LinkUrl { get; set; }

        /// <summary>
        /// 下载次数
        /// </summary>
        public int DownTimes { get; set; }

        /// <summary>
        /// 方案ID
        /// </summary>
        public int SchemeID { get; set; }

        /// <summary>
        /// 展示方式 0=默认 1=广告位
        /// </summary>
        public int ShowType { get; set; }

        #endregion


        /// <summary>
        /// 推荐标签
        /// </summary>
        public int RecommTag { get; set; }
    }
}
