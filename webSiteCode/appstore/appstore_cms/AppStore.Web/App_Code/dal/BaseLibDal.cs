using nwbase_utils;
using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
///基础库的数据操作
///added by kezesong 2014-05-08
/// </summary>
/// 
namespace AppStore.Web
{
    public class BaseLibDal
    {
        protected readonly string constr = Tools.GetConnStrConfig("baseLib");


        /// <summary>
        /// 添加开发者信息
        /// added by kezesong 2014-5-8
        /// </summary>
        /// <param name="developerModel"></param>
        /// <returns>返回成功的开发者编号</returns>
        public int AddDeveloper(DeveloperModels developerModel)
        {
            int result = -1;
            try
            {
                string sqlText = "INSERT INTO `B_Dev` (`DevName`, `PackFlagIdx`, `Remark`, `CreateTime`, `UpdateTime`, `Status`) VALUES (@devName,@packFlagIdx,@Remark,@CreateTime,@UpdateTime,@status); select last_insert_id(); ";
                List<MySqlParameter> paramList = new List<MySqlParameter>();
                paramList.Add(new MySqlParameter("@devName", developerModel.DevName));
                paramList.Add(new MySqlParameter("@packFlagIdx", string.Empty));
                paramList.Add(new MySqlParameter("@Remark", string.Empty));
                paramList.Add(new MySqlParameter("@CreateTime", DateTime.Now));
                paramList.Add(new MySqlParameter("@UpdateTime", DateTime.Now));
                paramList.Add(new MySqlParameter("@status", 1));

                object ret = MySqlHelper.ExecuteScalar(constr, sqlText, paramList.ToArray());
                result = Tools.GetInt(ret, 0);
                return result;
            }
            catch (Exception ex)
            {
                return result;
            }
        }

    }
}