using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class AppTypeEntity : BaseEntity
    {
        /// <summary>
        /// 应用类型，规则：11xx代表应用，12xx代表游戏
        /// </summary>
        public int AppType { get; set; }

        /// <summary>
        /// 应用分类，定义：11=应用，12=游戏
        /// </summary>
        public int AppClass { get; set; }

        /// <summary>
        /// 类型名
        /// </summary>
        public string AppTypeName { get; set; }

    }
}
