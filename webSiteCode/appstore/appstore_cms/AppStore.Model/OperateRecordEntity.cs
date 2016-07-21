using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class OperateRecordEntity
    {
        /// <summary>
        /// Id
        /// </summary>
        public int Id { get; set; }
        /// <summary>
        /// 操作元素Id
        /// </summary>
        public int ElemId { get; set; }

        /// <summary>
        /// 操作时间
        /// </summary>
        public DateTime OperateTime { get; set; }

        /// <summary>
        /// 操作用户
        /// </summary>
        public string UserName { get; set; }


        /// <summary>
        /// '操作类型，操作类型，1=首页配置，2=已上架游戏配置，3=待审核游戏,4=游戏接入情况
        /// </summary>
        public string OperateType { get; set; }

        /// <summary>
        /// 操作标识，
        /// 1:1=新增，2=修改，3=删除，4=排序；     
        /// 2：1=新增游戏，2=修改游戏信息，3=删除游戏 ，4=修改安装包信息，5=下架游戏，6=重新上架，7=设为主版本，；  
        /// 3：1=审核通过，2=审核不通过；4：1=接入游戏，2=修改状态为测试中，3=提交审核
        /// </summary>
        public string OperateFlag { get; set; }

        /// <summary>
        /// 操作说明
        /// </summary>
        public string OperateExplain { get; set; }
        /// <summary>
        /// 操作内容
        /// </summary>
        public string OperateContent { get; set; }
        

        /// <summary>
        /// 原因
        /// </summary>
        public string reason { get; set; }

        /// <summary>
        /// 创建日期
        /// </summary>
        public DateTime CreateTime { get; set; }

        /// <summary>
        /// 状态,1:启用, 2:禁用,
        /// </summary>
        public int Status { get; set; }


        /// <summary>
        /// 日志来源页面: 1=已上架游戏, 2=待审核游戏,  3=游戏接入情况 ，4=已下架游戏，5=审核不通过游戏 ,6=首页配置,
        /// </summary>
        public int SourcePage { get; set; }
    }
}
