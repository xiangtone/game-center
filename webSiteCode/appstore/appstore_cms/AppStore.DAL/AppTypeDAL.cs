using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data;
using MySql.Data.MySqlClient;

using AppStore.Common;


namespace AppStore.DAL
{
    public class AppTypeDAL : BaseDAL
    {
        /// <summary>
        /// 根据分类获取应用类型
        /// </summary>
        /// <param name="AppClass">应用分类，当为0时，表示所有分类</param>
        /// <returns></returns>
        public List<AppTypeEntity> GetAPPTypeList(int AppClass)
        {
            string commandText = @"SELECT AppType, AppClass, AppTypeName, Remarks, CreateTime, UpdateTime, Status FROM AppTypes WHERE Status = 1";
            if (AppClass != 0)
            {
                commandText += " and AppClass=" + AppClass;
            }
 
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<AppTypeEntity>() as List<AppTypeEntity>;
            }
        }

        public AppTypeEntity GetSingle(int id)
        {
            string commandText = @"SELECT AppType, AppClass, AppTypeName, Remarks, CreateTime, UpdateTime, Status FROM AppTypes WHERE Status = 1 and AppType="+id;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToModel<AppTypeEntity>() as AppTypeEntity;
            }
        }
    }
}
