using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    [Serializable]
    public class AppInfoEntity : BaseEntity
    {

        /// <summary>
        /// 应用名称，用于内部运营沟通的通用名称
        /// </summary>
        public string AppName { get; set; }


        /// <summary>
        /// 显示名称，显示给用户的名称
        /// </summary>
        public string ShowName { get; set; }

        /// <summary>
        /// 适用设备类型，定义：1=手机，2=平板，4=...位运算
        /// </summary>
        public int ForDeviceType { get; set; }

        /// <summary>
        /// 应用类型
        /// </summary>
        public int AppType { get; set; }

        /// <summary>
        /// 应用包名，添加安装包时写入，初始值为空字串
        /// </summary>
        public string PackName { get; set; }

        /// <summary>
        /// 包签名
        /// </summary>
        public string PackSign { get; set; }

        /// <summary>
        /// 分发类型：1=不分渠道，2=分渠道分发
        /// </summary>
        public int IssueType { get; set; }

        /// <summary>
        /// 多个渠道号,只有当IssueType=2时生效。逗号分隔，首尾要加上逗号
        /// </summary>
        public string ChannelNos { get; set; }

        /// <summary>
        /// 开发者ID，关联基础库开发者信息表
        /// </summary>
        public int CPID { get; set; }

        /// <summary>
        /// 开发者名称
        /// </summary>
        public string DevName { get; set; }

        /// <summary>
        /// 应用分类：1=应用 2=游戏
        /// </summary>
        public int AppClass { get; set; }

        /// <summary>
        /// 黄暴等级：0~5代表从危险至安全
        /// </summary>
        public int EvilLevel { get; set; }

        /// <summary>
        /// 下载量，运营可以调整
        /// </summary>
        public int DownTimes { get; set; }
        /// <summary>
        /// 真实下载量，任何人都不可以调整，用于数据统计
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
        /// 应用标签，1=小编亲测，2=无广告，4=xxx...位运算
        /// </summary>
        public int AppTag { get; set; }

        /// <summary>
        /// 推荐标识（角标），编辑指定，1=推荐，2=热门，4=官方...位运算
        /// </summary>
        public int RecommTag { get; set; }

        /// <summary>
        /// 推荐值，0~10代表从不推荐到推荐
        /// </summary>
        public int RecommLevel { get; set; }

        /// <summary>
        /// 推荐语，如果为空串，则展示为下载量
        /// </summary>
        public string RecommWord { get; set; }

        /// <summary>
        /// 缩略图URL地址
        /// </summary>
        public string ThumbPicUrl { get; set; }

        /// <summary>
        /// 主版本ICON图URL地址
        /// </summary>
        public string MainIconUrl { get; set; }

        /// <summary>
        /// 主安装包ID
        /// </summary>
        public int MainPackID { get; set; }

        /// <summary>
        /// 主版本代码
        /// </summary>
        public int MainVerCode { get; set; }

        /// <summary>
        /// 主版本号
        /// </summary>
        public string MainVerName { get; set; }

        /// <summary>
        /// 签名特征码，签名特征码相同，则可以升级
        /// </summary>
        public string MainSignCode { get; set; }

        /// <summary>
        /// 搜索关键字
        /// </summary>
        public string SearchKeys { get; set; }

        /// <summary>
        /// 应用描述
        /// </summary>
        public string AppDesc { get; set; }

        /// <summary>
        /// 联运游戏ID（对应帐号中心）
        /// </summary>
        public int UAppID { get; set; }

        public string ChannelAdaptation { get; set; }

        #region 扩展字段

        /// <summary>
        /// 安装包片段MD5
        /// </summary>
        public string PartPackMD5 { get; set; }

        /// <summary>
        /// 应用截图
        /// </summary>
        public string PicUrl { get; set; }

        /// <summary>
        /// 安装包地址
        /// </summary>
        public string PackUrl { get; set; }

        /// <summary>
        /// 安装包地址2
        /// </summary>
        public string PackUrl2 { get; set; }


        /// <summary>
        /// 安装包大小
        /// </summary>
        public int MainPackSize { get; set; }

        /// <summary>
        /// 安装包MD5校验
        /// </summary>
        public string PackMD5 { get; set; }

        /// <summary>
        /// 安装包更新说明
        /// </summary>
        public string UpdateDesc { get; set; }

        /// <summary>
        /// 应用适用性说明
        /// </summary>
        public string CompDesc { get; set; }

        /// <summary>
        /// 应用类型ID
        /// </summary>
        public string TypeID { get; set; }

        /// <summary>
        /// 类型名称
        /// </summary>
        public string AppTypeName { get; set; }

        /// <summary>
        /// 位置ID
        /// </summary>
        public int PosID { get; set; }

        /// <summary>
        /// 分组ID
        /// </summary>
        public int GroupID { get; set; }

        /// <summary>
        /// 推荐广告图
        /// </summary>
        public string GroupPicUrl { get; set; }


        /// <summary>
        /// 搜索方式
        /// </summary>
        public string SearchType { get; set; }

        /// <summary>
        /// 用于标识是否为网游
        /// </summary>
        public int IsNetGame { get; set; }

        /// <summary>
        /// 排序方式
        /// </summary>
        public string OrderType { get; set; }

        /// <summary>
        /// 当前应用下的安装包数量
        /// </summary>
        public int PackCount { get; set; }

        /// <summary>
        /// 数据状态，定义：1=正常，2=异常
        /// </summary>
        public int DataStatus { get; set; }

        /// <summary>
        /// 运营导入时间
        /// </summary>
        public DateTime OpCreateTime { get; set; }

        /// <summary>
        /// 运营更新时间
        /// </summary>
        public DateTime OpUpdateTime { get; set; }

        /// <summary>
        /// 应用的数据状态2，用于判断应用状态是否正常
        /// 目前仅用于缓存动态生成的判断
        /// </summary>
        public int ExistFlag { get; set; }


        #endregion


        /// <summary>
        /// 合作类型 定义：1=联运，2=CPS，3=CPA，99=未合作
        /// </summary>
        public int CoopType { get; set; }



        /// <summary>
        /// 安装包调用权限
        /// </summary>
        public string permission { get; set; } 


        /// <summary>
        /// 架构适配，1=arm，2=x86，...位运算
        /// </summary>
        public int Architecture { get; set; }
    }
}
