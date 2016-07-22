using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class GroupTypeEntity : BaseEntity
    {
        /// <summary>
        /// 分组类型ID
        /// </summary>
        public int TypeID { get; set; }

        /// <summary>
        /// 分组类别：11=应用分类，12=游戏分类，21=网游单机，31=专题，41=推荐
        /// </summary>
        public int TypeClass { get; set; }

        /// <summary>
        /// 分组类型名称，分类时可作为分类名称（优先取具体组的特定名称）
        /// </summary>
        public string TypeName { get; set; }

        /// <summary>
        /// 分组类型描述，分类时可作为分类描述（优先取具体组的特定描述）
        /// </summary>
        public string TypeDesc { get; set; }

        /// <summary>
        /// 分组图片URL，分类时可作为分类的展示图（优先取具体组的特定图片）
        /// </summary>
        public string TypePicUrl { get; set; }
    }
}
