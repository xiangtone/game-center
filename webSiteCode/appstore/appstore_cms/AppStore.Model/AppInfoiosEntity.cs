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

        ///<summary>
        /// App类型显示
        /// </summary>
        public string AppType
        {
            set;
            get;
        }

        /// <summary>
        /// App显示大小
        /// </summary>
        public string AppSize
        {
            set;
            get;
        }

        /// <summary>
        /// App版本
        /// </summary>
        public string AppVersion
        {
            set;
            get;
        }

        /// <summary>
        ///  app价格
        /// </summary>
        public string AppPrice
        {
            set;
            get;
        }
        ///<summary>
        /// 推荐标示语
        /// </summary>
        public string RecommFlagWord
        {
            set;
            get;
        }

        /// <summary>
        /// 缩略图URL地址
        /// </summary>
        public string ThumbPicUrl { get; set; }

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
        public string IconPicUrl
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

