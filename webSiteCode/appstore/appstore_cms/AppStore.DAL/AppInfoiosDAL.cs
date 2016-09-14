using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data;
using MySql.Data.MySqlClient;

using System.Data.SqlClient;
using System.Data;
using AppStore.Common;
using nwbase_utils;

namespace AppStore.DAL
{
    public class AppInfoiosDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(AppInfoiosEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@AppID", entity.AppID));
            paramsList.Add(new MySqlParameter("@AppName", entity.AppName));
            paramsList.Add(new MySqlParameter("@ShowName", entity.ShowName));
            paramsList.Add(new MySqlParameter("@DevName", entity.DevName));
            paramsList.Add(new MySqlParameter("@AppType", entity.AppType));
            paramsList.Add(new MySqlParameter("@AppSize", entity.AppSize));
            paramsList.Add(new MySqlParameter("@AppPrice", entity.AppPrice));
            paramsList.Add(new MySqlParameter("@AppVersion", entity.AppVersion));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            paramsList.Add(new MySqlParameter("@RecommFlagWord", entity.RecommFlagWord));
            paramsList.Add(new MySqlParameter("@IconPicUrl", entity.IconPicUrl));
            paramsList.Add(new MySqlParameter("@ThumbPicUrl", entity.ThumbPicUrl));
            paramsList.Add(new MySqlParameter("@IconUrl", entity.IconPicUrl));
            paramsList.Add(new MySqlParameter("@AppPicUrl", entity.AppPicUrl));
            paramsList.Add(new MySqlParameter("@AppUrl", entity.AppUrl));  
            paramsList.Add(new MySqlParameter("@AppDesc", entity.AppDesc));
            paramsList.Add(new MySqlParameter("@RecommWord", entity.RecommWord));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));     
            paramsList.Add(new MySqlParameter("@AdsPicUrl", entity.AdsPicUrl));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            return paramsList;
        }

        #endregion

        #region 重构分页条件

        private void Condition(StringBuilder commandText, List<MySqlParameter> paramsList, AppInfoiosEntity entity)
        {
            if (entity.Status == 0)
            {
                commandText.Append("and a.Status = 1");
            }

        }

        #endregion

        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, AppInfoiosEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }


        #endregion


        /// <summary>
        /// 新增应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(AppInfoiosEntity entity)
        {
            #region CommandText

            string commandText = @" INSERT INTO AppInfo_ios ( 
                                                            AppID,                                               
                                                            AppName,
                                                            ShowName,
                                                            DevName,
                                                            AppType,
                                                            AppSize,
                                                            AppPrice,
                                                            AppVersion,
                                                            Status,
                                                            RecommFlagWord,
                                                            ThumbPicUrl,
                                                            IconPicUrl,
                                                            AppPicUrl,
                                                            AppUrl,
                                                            AppDesc,
                                                            RecommWord,
                                                            CreateTime,
                                                            UpdateTime,
                                                            AdsPicUrl,
                                                            Remarks
                                                    ) 
                                                    VALUES
                                                        (
                                                            @AppID,
                                                            @AppName,
                                                            @ShowName,
                                                            @DevName,
                                                            @AppType,
                                                            @AppSize,
                                                            @AppPrice,
                                                            @AppVersion,
                                                            @Status,
                                                            @RecommFlagWord,
                                                            @ThumbPicUrl,
                                                            @IconPicUrl,
                                                            @AppPicUrl,
                                                            @AppUrl,
                                                            @AppDesc,
                                                            @RecommWord,
                                                            @CreateTime,
                                                            @UpdateTime,
                                                            @AdsPicUrl,
                                                            @Remarks
                                                        ); SELECT LAST_INSERT_ID(); ";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();
            return result;
        }

        /// <summary>
        /// 更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(AppInfoiosEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        appinfo_ios
                                    SET
                                        AppName  = @AppName,          
                                        ShowName  = @ShowName,
                                        DevName  = @DevName,
                                        AppType =@AppType,
                                        AppSize =@AppSize,
                                        AppPrice =@AppPrice,
                                        AppVersion=@AppVersion,
                                        Status  = @Status,
                                        RecommFlagWord=@RecommFlagWord,
                                        ThumbPicUrl=@ThumbPicUrl,
                                        IconPicUrl = @IconPicUrl,
                                        AppUrl  = @AppUrl,
                                        AppPicUrl =@AppPicUrl,
                                        AppDesc  = @AppDesc,
                                        Remarks  = @Remarks,
                                        UpdateTime  = @UpdateTime,
                                        RecommWord  = @RecommWord,
                                        AdsPicUrl = @AdsPicUrl       
                                    WHERE AppID = @AppID;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }


        /// <summary>
        /// 获取应用的安装包个数
        /// </summary>
        /// <param name="AppID"></param>
        /// <returns></returns>
        public int GetAppCount(int AppID)
        {

            #region CommandText
            StringBuilder commandText = new StringBuilder();
            commandText.AppendFormat("select count(0) from appinfo_ios where AppID ={0} and Status=1;",AppID);

            #endregion

            var result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString());

            return result.Convert<int>(0);

        }



        /// <summary>
        /// 删除应用信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            #region CommandText

            string commandText = @"UPDATE AppInfo_ios SET Status = 0 WHERE AppID= @AppID";

            #endregion

            return ExecuteNonQuery(commandText, new AppInfoiosEntity() { AppID = ID });
        }
        /// <summary>
        /// 更改应用的状态
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID, int Status)
        {
            #region CommandText

            string commandText = @"UPDATE appinfo_ios SET Status = @Status,UpdateTime='" + DateTime.Now + "' WHERE AppID= @AppID";

            #endregion

            return ExecuteNonQuery(commandText, new AppInfoiosEntity() { AppID = ID, Status = Status });
        }

        private string GetStrWhere(AppInfoiosEntity entity, List<MySqlParameter> paramsList)
        {
            StringBuilder str = new StringBuilder();
           if (entity.Status == 0)
            {
                str.Append(" and a.Status = 1");
            }
            if (entity.SearchType == "0") {
                if (!string.IsNullOrEmpty(entity.SearchKeys))
                {
                    str.AppendFormat("and a.ShowName like '%{0}%'", entity.SearchKeys);//按应用名称搜索
                }
                else
                {
                    str.ToString();
                }
                
            }
            if (entity.SearchType == "1")
            {
                if (!string.IsNullOrEmpty(entity.SearchKeys)) 
                {
                    str.AppendFormat("and a.DevName like '%{0}%'", entity.SearchKeys);//按开发者名称搜索
                }
                else
                {
                    str.ToString();
                }
                
            }
            if (entity.SearchType == "2")
            {
                if (!string.IsNullOrEmpty(entity.SearchKeys))
                {
                    str.AppendFormat("and a.AppID={0}", entity.SearchKeys);//按应用ID名称搜索
                }
                else
                {
                    str.ToString();
                }
                
            }
            return str.ToString();
        }

        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        private string GetStrWhereSeach(AppInfoiosEntity entity)
        {
            string str = "";
            str += " where tt.Status not in (0)";//未被禁用
            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                //应用名称
                if (entity.SearchType == "0")
                {
                    str += "and  tt.ShowName like '%" + entity.SearchKeys + "%'";
                }
                else if (entity.SearchType == "1")
                {
                    str += "and tt.DevName like '%" + entity.SearchKeys + "%'";
                }
                else if (entity.SearchType == "2")
                {
                    str += " and tt.AppID=" + entity.SearchKeys;
                }
            }
            return str;
        }


        /// <summary>
        /// 获取分页应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoiosEntity> GetDataListNew(AppInfoiosEntity entity)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select a.AppID,a.AppName,a.ShowName,a.DevName,a.AppType,a.AppSize,a.AppPrice,a.AppVersion,a.Status,a.RecommFlagWord,a.IconPicUrl,a.ThumbPicUrl,a.AppPicUrl,a.AppUrl,a.AppDesc,a.RecommWord,a.CreateTime,a.UpdateTime,a.AdsPicUrl,a.Remarks
                                    from appinfo_ios a
                                    where (1=1 ) {0}
                                    Order By a.AppID DESC LIMIT @StartIndex, @EndIndex
                                ", GetStrWhere(entity, paramsList));
            #endregion

            #region paramsList
            paramsList.Add(new MySqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", entity.EndIndex));
            #endregion
            //nwbase_utils.TextLog.Default.Info(commandText.ToString());
            using (MySqlDataReader reader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return reader.ReaderToList<AppInfoiosEntity>() as List<AppInfoiosEntity>;

            }
        }

        /// <summary>
        /// 获取对应总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalAppCountByStatus(AppInfoiosEntity entity, string status)
        {
            #region CommandText


            StringBuilder commandText = new StringBuilder();
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            if (status==null || status=="" || status.Length<=0) {         
                commandText.Append(@"select count(a.AppID)
                                    from AppInfo_ios a
                                    where (1=1)");
            }else{
                commandText.AppendFormat(@"select count(a.AppID)
                                    from AppInfo_ios a
                                    where (1=1)
                                    and a.Status in ({0})
                                ", status);
            }
            
            #endregion
            //nwbase_utils.TextLog.Default.Info(commandText.ToString());
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString()).Convert<int>();

        }


        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoiosEntity GetSingle(int ID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        AppID,
                                        AppName,
                                        ShowName,
                                        DevName,
                                        AppType,
                                        AppSize,
                                        AppPrice,
                                        AppVersion,
                                        Status,
                                        RecommFlagWord,
                                        IconPicUrl,
                                        ThumbPicUrl,
                                        AppPicUrl,
                                        AppUrl,
                                        AppDesc,
                                        RecommWord,                                    
                                        CreateTime,
                                        UpdateTime,
                                        AdsPicUrl,
                                        Remarks    
                                    FROM
                                        AppInfo_ios a
                                    WHERE a.AppID = @AppID
                                    LIMIT 0, 1;";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", ID)))
            {
                //return objReader.ReaderToModel<AppInfoiosEntity>() as AppInfoiosEntity;
                AppInfoiosEntity appInfoios = objReader.ReaderToModel<AppInfoiosEntity>() as AppInfoiosEntity;
                return appInfoios;
            }
        }



        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCount(AppInfoiosEntity entity)
        {

            string sql = string.Format(@"select count(*) from appinfo_ios tt Order By tt.AppID desc");

            //nwbase_utils.TextLog.Default.Info(sql);
            return MySqlHelper.ExecuteScalar(this.ConnectionString, sql).Convert<int>();

        }

        /// <summary>
        /// 判断已经存在同一款应用
        /// </summary>
        /// <param name="AppName"></param>
        /// <returns></returns>
        public bool IsExistAppInfo(string AppName)
        {
            string commandText = @"SELECT AppName FROM AppInfo_ios WHERE AppName = @AppName AND Status = 1 LIMIT 0,1";
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppName", AppName)))
            {
                return objReader.HasRows;
            }
        }



        /// <summary>
        /// 修改更新时间
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool ChangeUpdateTime(AppInfoiosEntity entity)
        {
            string commandText = @"Update AppInfo_ios set UpdateTime =@UpdateTime,
                                            OpUpdateTime = @OpUpdateTime
                                            where AppID=@AppID;";
            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 根据应用状态选择数据0:表示-禁用 1:表示-启用
        /// </summary>
        /// <param name="p"></param>
        /// <returns></returns>
        public List<AppInfoiosEntity> SelectListByStatus(int Status)
        {
            StringBuilder commandText = new StringBuilder();
            #region CommandText
            commandText.AppendFormat(@"
                                 SELECT * FROM appinfo_ios tt where Status={0} ORDER BY tt.AppID desc LIMIT @StartIndex, @EndIndex
                                ", Status);

            #endregion CommandText

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString()))
            {
                return objReader.ReaderToList<AppInfoiosEntity>() as List<AppInfoiosEntity>;
            }
        }

        public bool DeleteByID(AppInfoiosEntity entity)
        {
            string commandText = @"Update Appinfo set UpdateTime =@UpdateTime,Status=@Status where AppID=@AppID;";
            return ExecuteNonQuery(commandText, entity);
        }


        /// <summary>
        /// 根据ID查询
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public int GetCountById(int ID)
        {
            string commandText = " select Count(*) from appinfo_ios where AppId =" + ID;
            return Convert.ToInt32(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText));
        }


        /// <summary>
        /// 按分页取数据
        /// </summary>
        /// <param name="entity"></param>
        /// <param name="status"></param>
        /// <returns></returns>
        public DataSet GetExportDataList(AppInfoiosEntity entity, string status)
        {
            StringBuilder commandText = new StringBuilder();
            #region CommandText

            string pagecount = "";
            if (entity.EndIndex != 0)
            {
                pagecount = " LIMIT " + entity.StartIndex + "," + entity.EndIndex;
            }
            #region bak
            #endregion
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select a.AppID,a.AppName,a.ShowName,a.DevName,a.AppType,a.AppSize,a.AppPrice,a.AppVersion,a.Status,a.RecommFlagWord,a.ThumbPicUrl,a.IconPicUrl,a.AppPicUrl,a.AppUrl,AppDesc,a.RecommWord,a.CreateTime
                                 a.UpdateTime,a.AdsPicUrl,a.Remarks where (1=1 {0}) limit {1}
                                ", GetStrWhere(entity, paramsList), pagecount);
            #endregion



            string sql = "";
            //Type 1=已上架游戏 2=待审核游戏  3=游戏接入情况 
            sql = string.Format(@"select * from ({0})tt where tt.Status in ({1})  limit {2}", commandText.ToString(), status, pagecount);

            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText.ToString(), null);
        }

        /// <summary>
        /// 取得所有应用数据
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public DataSet GetExportAllDataList(AppInfoiosEntity entity)
        {
            #region SqlcommonText

            StringBuilder commandText = new StringBuilder();
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select a.AppID,a.AppName,a.ShowName,a.DevName,a.Status,a.IconUrl,a.AppUrl,AppDesc,a.RecommWord,a.CreateTime
                                 a.UpdateTime,a.AdsPicUrl,a.Remarks where (1=1 {0} and p.Status=1)
                                ", GetStrWhere(entity, paramsList));
            #endregion

            //分页
            string pagecount = "";
            if (entity.EndIndex != 0)
            {
                pagecount = " LIMIT " + entity.StartIndex + "," + entity.EndIndex;
            }

            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText.ToString(), null);


        }
    }
}
