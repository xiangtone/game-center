using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class AppPicListEntity:BaseEntity
    {
        /// <summary>
        /// 主键自增长
        /// </summary>
        public int AppPicID { get; set; }

        /// <summary>
        /// 图片地址
        /// </summary>
        public string PicUrl { get; set; }


        /// <summary>
        /// 更新前的截图地址
        /// </summary>
        public string OldPicUrl { get; set; }
    }
}
