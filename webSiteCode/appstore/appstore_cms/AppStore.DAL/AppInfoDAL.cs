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
    public enum AppEnum
    {
        AppSoft = 1,
        NetGame = 2
    }

    public class AppInfoDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(AppInfoEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@AppID", entity.AppID));
            paramsList.Add(new MySqlParameter("@CPID", entity.CPID));
            paramsList.Add(new MySqlParameter("@CoopType", entity.CoopType));
            paramsList.Add(new MySqlParameter("@IssueType", entity.IssueType));
            paramsList.Add(new MySqlParameter("@ChannelNos", entity.ChannelNos));
            paramsList.Add(new MySqlParameter("@DevName", entity.DevName));
            paramsList.Add(new MySqlParameter("@PackName", entity.PackName));
            paramsList.Add(new MySqlParameter("@PackSign", entity.PackSign));
            paramsList.Add(new MySqlParameter("@AppName", entity.AppName));
            paramsList.Add(new MySqlParameter("@ShowName", entity.ShowName));
            paramsList.Add(new MySqlParameter("@ForDeviceType", entity.ForDeviceType));
            paramsList.Add(new MySqlParameter("@IsNetGame", entity.IsNetGame));
            paramsList.Add(new MySqlParameter("@AppType", entity.AppType));
            paramsList.Add(new MySqlParameter("@AppTag", entity.AppTag));
            paramsList.Add(new MySqlParameter("@EvilLevel", entity.EvilLevel));
            paramsList.Add(new MySqlParameter("@DownTimes", entity.DownTimes));
            paramsList.Add(new MySqlParameter("@DownTimesReal", entity.DownTimesReal));
            paramsList.Add(new MySqlParameter("@CommentTimes", entity.CommentTimes));
            paramsList.Add(new MySqlParameter("@SearchKeys", entity.SearchKeys));
            paramsList.Add(new MySqlParameter("@AppDesc", entity.AppDesc));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@DataStatus", entity.DataStatus));
            paramsList.Add(new MySqlParameter("@RecommTag", entity.RecommTag));
            paramsList.Add(new MySqlParameter("@RecommLevel", entity.RecommLevel));
            paramsList.Add(new MySqlParameter("@MainPackID", entity.MainPackID));
            paramsList.Add(new MySqlParameter("@RecommWord", entity.RecommWord));
            paramsList.Add(new MySqlParameter("@MainIconUrl", entity.MainIconUrl));
            paramsList.Add(new MySqlParameter("@MainPackSize", entity.MainPackSize));
            paramsList.Add(new MySqlParameter("@MainVerCode", entity.MainVerCode));
            paramsList.Add(new MySqlParameter("@MainVerName", entity.MainVerName));
            paramsList.Add(new MySqlParameter("@OpCreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@OpUpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            paramsList.Add(new MySqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", entity.EndIndex));
            paramsList.Add(new MySqlParameter("@ChannelAdaptation", entity.ChannelAdaptation));
            paramsList.Add(new MySqlParameter("@PackCount", entity.PackCount));
            paramsList.Add(new MySqlParameter("@Architecture", entity.Architecture));
            return paramsList;
        }

        #endregion

        #region 重构分页条件

        private void Condition(StringBuilder commandText, List<MySqlParameter> paramsList, AppInfoEntity entity)
        {
            if (entity.Status == 0)
            {
                commandText.Append(" and a.Status = 1");

            }

            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                //应用名称
                if (entity.SearchType == "0")
                {
                    commandText.Append(@" and a.ShowName like @ShowName ");

                    paramsList.Add(new MySqlParameter("@ShowName", string.Format("%{0}%", entity.SearchKeys)));

                }
                else if (entity.SearchType == "1")
                {
                    commandText.AppendFormat(@" and a.DevID in ({0}) ", entity.SearchKeys);
                }
                else if (entity.SearchType == "2")
                {
                    commandText.Append(@" and a.AppID=@AppID ");
                    paramsList.Add(new MySqlParameter("@AppID", entity.SearchKeys));
                }
            }

            if (entity.TypeID != "0" && !string.IsNullOrEmpty(entity.TypeID))
            {
                commandText.Append(@" and b.TypeID=@TypeID ");
                paramsList.Add(new MySqlParameter("@TypeID", entity.TypeID));
            }

            if (entity.AppClass == 12)
            {
                commandText.Append(@"  AND b.TypeID > 1200   AND b.TypeID < 1300  AND c.TypeID > 2100  AND c.TypeID < 2103  ");
            }
            else
            {
                commandText.Append(@" AND b.TypeID > 1100  AND b.TypeID < 1108 ");
            }

            if (entity.IsNetGame != 0)
            {
                commandText.Append(@"  AND c.TypeID = @CTypeID ");
                paramsList.Add(new MySqlParameter("@CTypeID", entity.IsNetGame));
            }

            paramsList.Add(new MySqlParameter("@AppClass", entity.AppClass));
        }

        #endregion

        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, AppInfoEntity entity)
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
        public int Insert(AppInfoEntity entity)
        {
            #region CommandText

            string commandText = @" INSERT INTO AppInfo (
                                                        AppID,
                                                        CPID,
                                                        IssueType,
                                                        ChannelNos,
                                                        ChannelAdaptation,
                                                        DevName,
                                                        PackName,
                                                        PackSign,
                                                        AppName,
                                                        ShowName,
                                                        ForDeviceType,
                                                        Architecture,
                                                        IsNetGame,
                                                        AppType,
                                                        AppTag,
                                                        EvilLevel,
                                                        DownTimes,
                                                        DownTimesReal,
                                                        CommentTimes,
                                                        SearchKeys,
                                                        AppDesc,
                                                        Remarks,
                                                        CreateTime,
                                                        UpdateTime,
                                                        DataStatus,
                                                        RecommTag,
                                                        RecommLevel,
                                                        RecommWord,
                                                        MainPackID,
                                                        MainIconUrl,
                                                        MainPackSize,
                                                        MainVerCode,
                                                        MainVerName,
                                                        OpCreateTime,
                                                        OpUpdateTime,
                                                        Status
                                                    ) 
                                                    VALUES
                                                        (
                                                           @AppID,
                                                           @CPID,
                                                           @IssueType,
                                                           @ChannelNos,
                                                           @ChannelAdaptation,
                                                           @DevName,
                                                           @PackName,
                                                           @PackSign,
                                                           @AppName,
                                                           @ShowName,
                                                           @ForDeviceType,
                                                           @Architecture,
                                                           @IsNetGame,
                                                           @AppType,
                                                           @AppTag,
                                                           @EvilLevel,
                                                           @DownTimes,
                                                           @DownTimesReal,
                                                           @CommentTimes,
                                                           @SearchKeys,
                                                           @AppDesc,
                                                           @Remarks,
                                                           @CreateTime,
                                                           @UpdateTime,
                                                           @DataStatus,
                                                           @RecommTag,
                                                           @RecommLevel,
                                                           @RecommWord,
                                                           @MainPackID,
                                                           @MainIconUrl,
                                                           @MainPackSize,
                                                           @MainVerCode,
                                                           @MainVerName,
                                                           @OpCreateTime,
                                                           @OpUpdateTime,
                                                           @Status
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
        public bool Update(AppInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        AppInfo 
                                    SET
                                        AppID  = @AppID,
                                        CPID  = @CPID,
                                        IssueType=@IssueType,
                                        ChannelNos=@ChannelNos,
                                        ChannelAdaptation=@ChannelAdaptation,
                                        DevName  = @DevName,
                                        PackSign  = @PackSign,
                                        AppName  = @AppName,
                                        ShowName  = @ShowName,
                                        ForDeviceType  = @ForDeviceType,
                                        IsNetGame  = @IsNetGame,
                                        Architecture=@Architecture,
                                        AppType  = @AppType,
                                        AppTag  = @AppTag,
                                        EvilLevel  = @EvilLevel,
                                        DownTimes  = @DownTimes,
                                        DownTimesReal  = @DownTimesReal,
                                        CommentTimes  = @CommentTimes,
                                        SearchKeys  = @SearchKeys,
                                        AppDesc  = @AppDesc,
                                        Remarks  = @Remarks,
                                        UpdateTime  = @UpdateTime,
                                        RecommTag  = @RecommTag,
                                        RecommLevel  = @RecommLevel,
                                        RecommWord  = @RecommWord,
                                        Status  = @Status
                                    WHERE AppID = @AppID ;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }


        /// <summary>
        /// 更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdatePackCount(AppInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        AppInfo 
                                    SET
                                        PackCount  = @PackCount
                                    WHERE AppID = @AppID ;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 删除应用信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            #region CommandText

            string commandText = @"UPDATE AppInfo SET Status = 2 WHERE AppID= @AppID";

            #endregion

            return ExecuteNonQuery(commandText, new AppInfoEntity() { AppID = ID });
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID, int Status)
        {
            #region CommandText

            string commandText = @"UPDATE AppInfo SET Status = @Status,Updatetime='" + DateTime.Now + "' WHERE AppID= @AppID";

            #endregion

            return ExecuteNonQuery(commandText, new AppInfoEntity() { AppID = ID, Status = Status });
        }


        private string GetStrWhere(AppInfoEntity entity, List<MySqlParameter> paramsList)
        {
            StringBuilder str = new StringBuilder();
            if (entity.AppClass > 0)
            {
                str.AppendFormat(" and (r.AppClass = {0} or r.AppClass is null)", entity.AppClass);
            }
            if (entity.Status == 0)
            {
                str.Append(" and a.Status = 1");
            }
            if (entity.TypeID != "0" && !string.IsNullOrEmpty(entity.TypeID))
            {
                str.AppendFormat(" and a.AppType = {0}", entity.TypeID);
            }
            if (entity.AppType > 0 && !string.IsNullOrEmpty(entity.AppType.ToString()))
            {
                str.AppendFormat(" and a.AppType = {0}", entity.AppType);
            }
            if (entity.IsNetGame != 0)
            {
                str.AppendFormat(" and a.IsNetGame = {0}", entity.IsNetGame);
            }
            paramsList.Add(new MySqlParameter("@AppClass", entity.AppClass));
            return str.ToString();
        }

        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        private string GetStrWhereSeach(AppInfoEntity entity)
        {
            string str = "";
            str += " where tt.Status not in (98,99)";
            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                //应用名称
                if (entity.SearchType == "0")
                {
                    str += "and  tt.ShowName like '%" + entity.SearchKeys + "%'";
                }
                else if (entity.SearchType == "1")
                {
                    str += "and  tt.CPID in ({" + entity.SearchKeys + "}) ";
                }
                else if (entity.SearchType == "2")
                {
                    str += " and tt.AppID=" + entity.SearchKeys;
                }
            }
            if (entity.AppType > 0)
            {
                str += " and tt.AppType=" + entity.AppType;
            }
            return str;
        }
        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoEntity> GetDataListNew(AppInfoEntity entity, string status, string channel)
        {
            StringBuilder commandText = new StringBuilder();
            #region CommandText

            #region bak
            #endregion
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select r.AppTypeName,r.AppClass ,
                                     a.AppID,a.CPID,a.DevName,a.DownTimes,a.AppName,a.ShowName,a.DownTimesReal,a.CoopType,a.IsNetGame,a.Architecture,a.AppType,a.CreateTime,a.UpdateTime,a.Status,a.ChannelAdaptation,a.PackCount,a.MainPackID,a.MainIconUrl,a.MainVerName, a.PackName,p.permission 
                                    from AppInfo a left join packinfo p on p.PackID=a.MainPackID
                                    left join AppTypes r on a.AppType = r.AppType
                                    where (1=1 {0})
                                    {4} and a.Status in ({3}) {2}  Order By {1} desc LIMIT @StartIndex, @EndIndex
                                ", GetStrWhere(entity, paramsList), entity.OrderType, GetWhere(entity), status, GetChannelAdaptation(channel));
            #endregion
            #region paramsList
            paramsList.Add(new MySqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", entity.EndIndex));
            #endregion
            //nwbase_utils.TextLog.Default.Info(commandText.ToString());
            using (MySqlDataReader reader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return reader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;

            }
        }
        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCountNew(AppInfoEntity entity, string status, string channel)
        {
            #region CommandText


            StringBuilder commandText = new StringBuilder();
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            commandText.AppendFormat(@"  select count(a.AppID)
                                    from AppInfo a left join packinfo p on p.PackID=a.MainPackID
                                    left join AppTypes r on a.AppType = r.AppType
                                    where (1=1 {0} )
                                    {3} and a.Status in ({2}) {1}
                                ", GetStrWhere(entity, paramsList), GetWhere(entity), status, GetChannelAdaptation(channel));
            #endregion
            //nwbase_utils.TextLog.Default.Info(commandText.ToString());
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString()).Convert<int>();

        }


        /// <summary>
        /// 
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public string GetWhere(AppInfoEntity entity)
        {
            string str = "";
            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                //应用名称
                if (entity.SearchType == "0")
                {
                    str = " and a.ShowName like '%" + entity.SearchKeys + "%'";
                }
                else if (entity.SearchType == "1")
                {
                    str = " and a.DevName like '%" + entity.SearchKeys + "%'";
                }
                else if (entity.SearchType == "2")
                {
                    str = " and a.AppID=" + entity.SearchKeys;
                }
                else if (entity.SearchType == "3")
                {
                    str = " and a.PackName='" + entity.SearchKeys + "'";
                }
            }
            return str;
        }
        public string GetChannelAdaptation(string channel)
        {
            if (channel == "")
            {
                return "";
            }
            else
            {
                return "and a.ChannelAdaptation like '%" + channel + "%'";
            }
        }

        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoEntity GetSingle(int ID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        AppID,
                                        CPID,
                                        IssueType,
                                        ChannelNos,
                                        ChannelAdaptation,
                                        DevName,
                                        PackName,
                                        Architecture,
                                        PackSign,
                                        AppName,
                                        ShowName,
                                        ForDeviceType,
                                        IsNetGame,
                                        AppType,
                                        AppTag,
                                        EvilLevel,
                                        DownTimes,
                                        DownTimesReal,
                                        CommentTimes,
                                        SearchKeys,
                                        AppDesc,
                                        Remarks,
                                        CreateTime,
                                        UpdateTime,
                                        DataStatus,
                                        RecommTag,
                                        RecommLevel,
                                        RecommWord,
                                        MainPackID,
                                        MainIconUrl,
                                        MainPackSize,
                                        MainVerCode,
                                        MainVerName,
                                        OpCreateTime,
                                        OpUpdateTime,
                                        Status,CoopType
                                    FROM
                                        AppInfo
                                    WHERE AppInfo.AppID = @AppID
                                    LIMIT 0, 1";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", ID)))
            {
                return objReader.ReaderToModel<AppInfoEntity>() as AppInfoEntity;
            }
        }

        /// <summary>
        /// 获取一条应用信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public AppInfoEntity GetSingle2(int ID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        a.*,r.AppTypeName
                                    FROM
                                        AppInfo a,AppTypes r where a.AppType=r.AppType
                                    and a.AppID = @AppID
                                    LIMIT 0, 1";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", ID)))
            {
                return objReader.ReaderToModel<AppInfoEntity>() as AppInfoEntity;
            }
        }
        /// <summary>
        /// 获取最新一条安装包信息
        /// </summary>
        /// <returns></returns>
        public PackInfoEntity GetNewInfo(int AppID)
        {
            #region CommandText

            string commandText = @"SELECT * FROM packinfo where AppID=@AppID AND Status = 1 order by updatetime desc LIMIT 0, 1";
            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", AppID)))
            {
                return objReader.ReaderToModel<PackInfoEntity>() as PackInfoEntity;
            }
        }
        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCount(AppInfoEntity entity)
        {


            StringBuilder commandText = new StringBuilder();

            #region CommandText

            #endregion
            #region CommandText
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            commandText.AppendFormat(@"
                                select t1.* from 
                                (
                                  select a.*,r.AppTypeName,r.AppClass
                                from AppInfo a,AppTypes r where a.AppType=r.AppType
                                {0}
                                )t1
                                left join 
                                (
                                select a.appid
                                from AppInfo a,AppTypes r where a.AppType=r.AppType
                                )t2
                                on t1.appid=t2.appid
                                ", GetStrWhere(entity, paramsList));


            #endregion
            string sql = string.Format(@"select count(*) from ({0})tt  {1} Order By {2} desc", commandText.ToString(), GetStrWhereSeach(entity), entity.OrderType);

            //nwbase_utils.TextLog.Default.Info(sql);
            return MySqlHelper.ExecuteScalar(this.ConnectionString, sql).Convert<int>();

        }

        /// <summary>
        /// 判断是否已经推荐过一条应用信息
        /// </summary>
        /// <param name="appID"></param>
        /// <returns></returns>
        public bool IsExistRecommApp(int appID)
        {
            string commandText = @"SELECT AppID FROM RecommApps WHERE AppID = @AppID LIMIT 0,1";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", appID)))
            {
                return objReader.HasRows;
            }
        }

        /// <summary>
        /// 判断已经存在同一款应用
        /// </summary>
        /// <param name="showName"></param>
        /// <returns></returns>
        public bool IsExistAppInfo(string showName)
        {
            string commandText = @"SELECT ShowName FROM AppInfo WHERE ShowName = @ShowName AND Status = 1 LIMIT 0,1";
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@ShowName", showName)))
            {
                return objReader.HasRows;
            }
        }


        /// <summary>
        /// 判断在GroupElems中是否存在
        /// </summary>
        /// <param name="elemID"></param>
        /// <returns></returns>
        public bool IsExistGroupElems(int elemID)
        {
            string commandText = @"SELECT elemID FROM GroupElems WHERE elemID = @elemID and Status=1 LIMIT 0,1";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@elemID", elemID)))
            {
                return objReader.HasRows;
            }
        }

        /// <summary>
        /// </summary>
        /// <param name="elemID"></param>
        /// <returns></returns>
        public bool DelGroupElems(int elemID)
        {
            AppInfoEntity entity = new AppInfoEntity() { AppID = elemID };
            string commandText = @"update GroupElems set Status = 2  WHERE elemID = @AppID and Status=1 ";

            return ExecuteNonQuery(commandText, entity);

        }

        /// <summary>
        /// 上传安装包后更新应用信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpDatePackInfo(AppInfoEntity entity)
        {
            string commandText = @"Update AppInfo set 
                                            MainPackID=@MainPackID,
                                            MainIconUrl = @MainIconUrl,
                                            CoopType = @CoopType,
                                            PackName=@PackName,
                                            MainVerCode=@MainVerCode,
                                            MainPackSize=@MainPackSize,
                                            MainVerName=@MainVerName,
                                            PackSign = @PackSign,
                                            OpUpdateTime = @OpUpdateTime,
                                            DataStatus = @DataStatus
                                            where AppID=@AppID;";

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 根据应用类别获取当前应用表中最后一个应用ID
        /// </summary>
        /// <param name="isJoint">是否为联运游戏</param>
        /// <param name="AppClassID"></param>
        /// <returns></returns>
        public int GetLastAppID(int isJoint, int AppClassID)
        {
            //1-500W为联运应用 500W-1000W联运游戏  1000W-1500W应用 1500W-2000W为游戏

            string commandText = @"SELECT MAX(AppID) AS 'AppID' FROM AppInfo  ";

            if (isJoint == 0 && AppClassID == 1)
            {
                commandText += @"Where AppID > 10000000 and AppID<=15000000";
            }
            else if (isJoint == 0 && AppClassID == 2)
            {
                commandText += @"Where AppID > 15000000 and AppID<=20000000";
            }
            else if (isJoint == 1 && AppClassID == 1)
            {
                commandText += @"Where AppID > 0 and AppID<=5000000";
            }

            else if (isJoint == 1 && AppClassID == 2)
            {
                commandText += @"Where AppID > 5000000 and AppID<=10000000";
            }

            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText).Convert<int>();
        }


        /// <summary>
        /// 获取当前AppID下的安装包数量
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, int> GetPacksCount()
        {
            string commandText = @"SELECT AppID, COUNT(0) AS 'PackCount'  FROM  PackInfo   GROUP BY AppID";

            List<AppInfoEntity> list = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                list = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;

                return list.ToDictionary(s => s.AppID, s => s.PackCount);
            }
        }


        /// <summary>
        /// 从统计数据库中获取联运游戏列表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<AppInfoEntity> GetUAppInfoList(AppInfoEntity entity)
        {
            string sqlConnStr = Tools.GetConnStrConfig("ActionStateConnectionString");

            List<SqlParameter> paramsList = new List<SqlParameter>();

            paramsList.Add(new SqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new SqlParameter("@EndIndex", entity.EndIndex));

            string commandText = @"select * FROM (
                        select AppID,AppName,ROW_NUMBER() OVER(ORDER by AppID) as rowNumber from UAppInfo )t
                        where t.rowNumber >@StartIndex AND t.rowNumber <=@EndIndex ";

            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                commandText += @" and t.AppName like @SearchKeys ";

                paramsList.Add(new SqlParameter("@SearchKeys", string.Format("%{0}%", entity.SearchKeys)));
            }

            using (SqlDataReader objReader = SqlHelper.ExecuteReader(sqlConnStr, CommandType.Text, commandText, paramsList.ToArray()))
            {
                return objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }

        }

        public int GetUAppInfoCount(AppInfoEntity entity)
        {
            string sqlConnStr = Tools.GetConnStrConfig("ActionStateConnectionString");

            List<SqlParameter> paramsList = new List<SqlParameter>();



            string commandText = @"select count(0) from UAppInfo ";

            if (!string.IsNullOrEmpty(entity.SearchKeys))
            {
                commandText += @" Where AppName like @SearchKeys ";

                paramsList.Add(new SqlParameter("@SearchKeys", string.Format("%{0}%", entity.SearchKeys)));
            }

            var result = SqlHelper.ExecuteScalar(sqlConnStr, CommandType.Text, commandText, paramsList.ToArray());

            return result.Convert<int>(0);
        }

        /// <summary>
        /// 判断应用安装包是否重复
        /// </summary>
        /// <param name="AppID">应用ID，当为0时，则判断其他应用是否存在此包，否则判断添加的包是否一致</param>
        /// <param name="PackName"></param>
        /// <param name="PackSign"></param>
        /// <returns></returns>
        public int CheckAppRepeat(int AppID, string PackName, string PackSign)
        {
            string sqlConnStr = Tools.GetConnStrConfig("ActionStateConnectionString");

            #region CommandText

            string commandText = " select count(0) from packinfo where PackName = \"" + PackName + "\" and PackSign = \"" + PackSign + "\"";

            if (AppID != 0)
            {
                commandText += " and AppID = " + AppID;
            }
            #endregion

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText).Convert<int>(0);

            return result;


        }


        /// <summary>
        /// 通过安装包PackName + PackSign获取应用信息
        /// </summary>
        /// <param name="PackName">包名</param>
        /// <param name="PackSign">签名验证码</param>
        /// <returns></returns>
        public AppInfoEntity GetAppByPackNameAndPackSign(string PackName, string PackSign)
        {
            #region CommandText

            string commandText = "SELECT AppID,CPID,DevName,PackName, PackSign,AppName,ShowName,ForDeviceType,IsNetGame,AppType,AppTag,EvilLevel,DownTimes, DownTimesReal,CommentTimes,SearchKeys, AppDesc,Remarks";
            commandText += " , CreateTime,UpdateTime,DataStatus,RecommTag,RecommLevel,RecommWord,MainPackID,MainIconUrl,MainPackSize,MainVerCode, MainVerName,OpCreateTime, OpUpdateTime,Status";
            commandText += " from  AppInfo WHERE AppInfo.PackSign = \"" + PackSign + "\" and AppInfo.PackName = \"" + PackName + "\" LIMIT 1;";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToModel<AppInfoEntity>() as AppInfoEntity;
            }
        }

        /// <summary>
        /// 修改更新时间
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool ChangeUpdateTime(AppInfoEntity entity)
        {
            string commandText = @"Update AppInfo set UpdateTime =@UpdateTime,
                                            OpUpdateTime = @OpUpdateTime
                                            where AppID=@AppID;";
            return ExecuteNonQuery(commandText, entity);
        }

        public List<AppInfoEntity> SelectListByStatus(int p)
        {
            #region CommandText

            string commandText = @"SELECT * FROM appinfo where Status=99";
            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }
        }

        public bool DeleteByID(AppInfoEntity entity)
        {
            string commandText = @"Update Appinfo set UpdateTime =@UpdateTime,Status=@Status where AppID=@AppID;";
            return ExecuteNonQuery(commandText, entity);
        }

        public int GetPacksCount(int Appid)
        {
            string commandText = " SELECT count(*) FROM packinfo where AppID =" + Appid + " and Status=1";
            return Convert.ToInt32(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText));

        }

        public int GetCountById(int ID)
        {
            string commandText = " select Count(*) from appinfo where AppId =" + ID;
            return Convert.ToInt32(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText));
        }

        public DataSet GetExportDataList(AppInfoEntity entity, string status, string channel)
        {
            StringBuilder commandText = new StringBuilder();
            #region CommandText

            #region bak
            #endregion
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select r.AppTypeName,r.AppClass ,
                                     a.AppID,a.CPID,a.DevName,a.DownTimes,a.AppName,a.ShowName,a.CoopType,a.IsNetGame,a.Architecture,a.AppType,a.CreateTime,a.UpdateTime,a.Status,a.ChannelAdaptation,a.PackCount,a.MainPackID,a.MainIconUrl,a.MainVerName, a.PackName,p.permission 
                                    from AppInfo a inner join packinfo p on p.PackID=a.MainPackID
                                    left join AppTypes r on a.AppType = r.AppType
                                    where (1=1 {0})
                                ", GetStrWhere(entity, paramsList));
            #endregion

            string pagecount = "";
            if (entity.EndIndex != 0){
                pagecount = " LIMIT "+entity.StartIndex+","+entity.EndIndex;
            }
            #region sql
            string sql = "";
            //Type 1=已上架游戏 2=待审核游戏  3=游戏接入情况 
            sql = string.Format(@"select tt.ShowName as '游戏名',tt.PackName as '包名',tt.MainVerName as '版本',tt.AppTypeName as '游戏分类',tt.CoopType as '合作类型',tt.DevName as '开发者',tt.Status as '状态',tt.permission as '调用权限'  from ({0})tt where tt.ChannelAdaptation like '%{3}%' and tt.Status in ({2}) Order By {1} desc {4}", commandText.ToString(), entity.OrderType, status, channel , pagecount);
            //nwbase_utils.TextLog.Default.Info(sql);
            #endregion

            return MySqlHelper.ExecuteDataset(this.ConnectionString, sql, null);
        }


        /// <summary>
        /// 修改机型适配
        /// </summary>
        /// <param name="id"></param>
        /// <param name="arch"></param>
        /// <returns></returns>
        public bool UpdateArch(int id, int arch)
        {
            #region CommandText

            string commandText = @"UPDATE AppInfo SET Architecture = @Architecture,Updatetime='" + DateTime.Now + "' WHERE AppID= @AppID";

            #endregion

            return ExecuteNonQuery(commandText, new AppInfoEntity() { AppID = id, Architecture = arch });
        }

        public DataSet GetExportDataList2(AppInfoEntity entity, string status, string channel)
        {
            StringBuilder commandText = new StringBuilder();
            #region CommandText

            #region bak
            #endregion
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText.AppendFormat(@"
                                 select r.AppTypeName,r.AppClass ,
                                     a.AppID,a.CPID,a.DevName,a.DownTimes,a.AppName,a.ShowName,a.CoopType,a.IsNetGame,a.Architecture,a.AppType,a.CreateTime,a.UpdateTime,a.Status,a.ChannelAdaptation,a.PackCount,a.MainPackID,a.MainIconUrl,a.MainVerName, a.PackName,p.permission 
                                    from AppInfo a inner join packinfo p on p.PackID=a.MainPackID
                                    left join AppTypes r on a.AppType = r.AppType
                                    where (1=1 {0} and p.Status=1)
                                ", GetStrWhere(entity, paramsList));
            #endregion

            string pagecount = "";
            if (entity.EndIndex != 0){
                pagecount = " LIMIT "+entity.StartIndex+","+entity.EndIndex;
            }
            #region sql
            string sql = "";
            //Type 1=已上架游戏 2=待审核游戏  3=游戏接入情况 
            sql = string.Format(@"select tt.ShowName as '游戏名',tt.PackName as '包名',tt.MainVerName as '版本',tt.AppTypeName as '游戏分类',tt.CoopType as '合作类型',tt.DevName as '开发者',tt.Status as '状态',tt.permission as '调用权限',tt.AppID as 'ID'  from ({0})tt where tt.ChannelAdaptation like '%{3}%' and tt.Status in ({2}) Order By {1} desc {4}", commandText.ToString(), entity.OrderType, status, channel, pagecount);
            //nwbase_utils.TextLog.Default.Info(sql);
            #endregion

            return MySqlHelper.ExecuteDataset(this.ConnectionString, sql, null);
        }
    }
}
