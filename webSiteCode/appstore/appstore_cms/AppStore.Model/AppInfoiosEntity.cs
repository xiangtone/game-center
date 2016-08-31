using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    /// <summary>
    /// AppInfo_ios:实体类(ios model实体类)
    /// </summary>
    [Serializable]


    public class AppInfoiosEntity : BaseEntity
    {

    
        /// <summary>
        /// 应用名称
        /// </summary>
        public string AppName { 
            set; get; 
        } 
     
        /// <summary>
        /// 开发商名称
        /// </summary>
        public string DevName
        {
            set;get; 
        }
        /// <summary>
        /// 显示名称
        /// </summary>
        public string ShowName
        {
            set;get; 
        }

        /// <summary>
        /// 搜索类型
        /// </summary>
        public string SearchType { 
            set; 
            get; 
        }

        /// <summary>
        /// 应用状态
        /// </summary>
        public int Status { get; set; }

        /// <summary>
        /// SearchKeys 搜索关键字
        /// </summary>
        public string SearchKeys { 
            set; 
            get; 
        }

        /// <summary>
        /// Iccn Url地址
        /// </summary>
        public string IconUrl
        {
            set;
            get; 
        }
      
        ///<summary>
        /// 应用图片地址
        /// </summary>
        public string AppPicUrl { 
            set; 
            get; 
        }

        /// <summary>
        /// 应用Url地址
        /// </summary>
        public string AppUrl
        {
            set;get; 
        }
        /// <summary>
        /// 应用描述
        /// </summary>
        public string AppDesc
        {
            set;get; 
        }
        /// <summary>
        /// 推荐语
        /// </summary>
        public string RecommWord
        {
            set;get; 
        }

        /// <summary>
        /// 广告url地址
        /// </summary>
        public string AdsPicUrl
        {
            set;get; 
        }
    }
}

