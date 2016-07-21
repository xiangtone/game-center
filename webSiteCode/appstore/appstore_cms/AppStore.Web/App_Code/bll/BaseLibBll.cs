using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// BaseLibBll 的摘要说明
/// </summary>
/// 
namespace AppStore.Web
{
    public class BaseLibBll
    {
        BaseLibDal dal = new BaseLibDal();
        /// <summary>
        /// 添加开发者信息
        /// added by kezesong 2014-5-8
        /// </summary>
        /// <param name="developerModel"></param>
        /// <returns>返回成功的开发者编号</returns>
        public int AddDeveloper(DeveloperModels developerModel)
        {
            return dal.AddDeveloper(developerModel);
        }
    }
}