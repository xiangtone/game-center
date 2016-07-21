using MySql.Data;
using MySql.Data.MySqlClient;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Common;
using nwbase_utils;

namespace AppStore.DAL
{
    public class PackInfoDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(PackInfoEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            paramsList.Add(new MySqlParameter("@PackID", entity.PackID));
            paramsList.Add(new MySqlParameter("@AppID", entity.AppID));
            paramsList.Add(new MySqlParameter("@ShowName", entity.ShowName));
            paramsList.Add(new MySqlParameter("@CoopType", entity.CoopType));
            paramsList.Add(new MySqlParameter("@PackFrom", entity.PackFrom));
            paramsList.Add(new MySqlParameter("@DownTimes", entity.DownTimes));
            paramsList.Add(new MySqlParameter("@DownTimesReal", entity.DownTimesReal));
            paramsList.Add(new MySqlParameter("@CommentTimes", entity.CommentTimes));
            paramsList.Add(new MySqlParameter("@IsMainVer", entity.IsMainVer));
            paramsList.Add(new MySqlParameter("@PackSize", entity.PackSize));
            paramsList.Add(new MySqlParameter("@VerCode", entity.VerCode));
            paramsList.Add(new MySqlParameter("@VerName", entity.VerName));
            paramsList.Add(new MySqlParameter("@PackName", entity.PackName));
            paramsList.Add(new MySqlParameter("@PackSign", entity.PackSign));
            paramsList.Add(new MySqlParameter("@IconUrl", entity.IconUrl));
            paramsList.Add(new MySqlParameter("@AppPicUrl", entity.AppPicUrl));
            paramsList.Add(new MySqlParameter("@IconUrl2", entity.IconUrl2));
            paramsList.Add(new MySqlParameter("@AppPicUrl2", entity.AppPicUrl2));
            paramsList.Add(new MySqlParameter("@PackUrl", entity.PackUrl));
            paramsList.Add(new MySqlParameter("@PackUrl2", entity.PackUrl2));
            paramsList.Add(new MySqlParameter("@PackMD5", entity.PackMD5));
            paramsList.Add(new MySqlParameter("@CompDesc", entity.CompDesc));
            paramsList.Add(new MySqlParameter("@UpdateDesc", entity.UpdateDesc));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            paramsList.Add(new MySqlParameter("@OpCreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@OpUpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@MainIconUrl", entity.IconUrl));
            paramsList.Add(new MySqlParameter("@permission", entity.permission));


            return paramsList;
        }

        #endregion

        #region 重构分页条件

        private void Condition(StringBuilder commandText, List<MySqlParameter> paramsList, PackInfoEntity entity)
        {
            if (!string.IsNullOrEmpty(entity.ShowName))
            {
                commandText.Append(" and ShowName like @showName");
                paramsList.Add(new MySqlParameter("@showName", string.Format("%{0}%", entity.ShowName)));
            }
        }

        #endregion

        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, PackInfoEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }


        #endregion

        /// <summary>
        /// 新增安装包信息;默认新增安装包为主安装包版本
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(PackInfoEntity entity)
        {
            #region CommandText

            string commandText = @" 
                                UPDATE PackInfo SET IsMainVer = 0 WHERE AppID=@AppID;

                                INSERT INTO PackInfo (
                                        PackID,
                                        AppID,
                                        ShowName,
                                        CoopType,
                                        PackFrom,
                                        DownTimes,
                                        DownTimesReal,
                                        CommentTimes,
                                        IsMainVer,
                                        PackSize,
                                        VerCode,
                                        VerName,
                                        PackName,
                                        PackSign,
                                        IconUrl,
                                        AppPicUrl,
                                        PackUrl,
                                        PackUrl2,
                                        PackMD5,
                                        CompDesc,
                                        UpdateDesc,
                                        Remarks,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        OpCreateTime,
                                        OpUpdateTime,
                                        permission
                                ) 
                                VALUES
                                    (
                                        @PackID,
                                        @AppID,
                                        @ShowName,
                                        @CoopType,
                                        @PackFrom,
                                        @DownTimes,
                                        @DownTimesReal,
                                        @CommentTimes,
                                        @IsMainVer,
                                        @PackSize,
                                        @VerCode,
                                        @VerName,
                                        @PackName,
                                        @PackSign,
                                        @IconUrl,
                                        @AppPicUrl,
                                        @PackUrl,
                                        @PackUrl2,
                                        @PackMD5,
                                        @CompDesc,
                                        @UpdateDesc,
                                        @Remarks,
                                        @CreateTime,
                                        @UpdateTime,
                                        @Status,
                                        @OpCreateTime,
                                        @OpUpdateTime,
                                        @permission
                                    ) ; SELECT LAST_INSERT_ID();";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result;
        }
        public int InsertByAuto(PackInfoEntity entity)
        {
            #region CommandText

            string commandText = @" 
                                UPDATE PackInfo SET IsMainVer = 0 WHERE AppID=@AppID;

                                INSERT INTO PackInfo (
                                        PackID,
                                        AppID,
                                        ShowName,
                                        CoopType,
                                        PackFrom,
                                        DownTimes,
                                        DownTimesReal,
                                        CommentTimes,
                                        IsMainVer,
                                        PackSize,
                                        VerCode,
                                        VerName,
                                        PackName,
                                        PackSign,
                                        IconUrl2,
                                        AppPicUrl2,
                                        PackUrl,
                                        PackUrl2,
                                        PackMD5,
                                        CompDesc,
                                        UpdateDesc,
                                        Remarks,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        OpCreateTime,
                                        OpUpdateTime
                                ) 
                                VALUES
                                    (
                                        @PackID,
                                        @AppID,
                                        @ShowName,
                                        @CoopType,
                                        @PackFrom,
                                        @DownTimes,
                                        @DownTimesReal,
                                        @CommentTimes,
                                        @IsMainVer,
                                        @PackSize,
                                        @VerCode,
                                        @VerName,
                                        @PackName,
                                        @PackSign,
                                        @IconUrl2,
                                        @AppPicUrl2,
                                        @PackUrl,
                                        @PackUrl2,
                                        @PackMD5,
                                        @CompDesc,
                                        @UpdateDesc,
                                        @Remarks,
                                        @CreateTime,
                                        @UpdateTime,
                                        @Status,
                                        @OpCreateTime,
                                        @OpUpdateTime
                                    ) ; SELECT LAST_INSERT_ID();";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result;
        }

        /// <summary>
        /// 删除安装包信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            #region CommandText

            string commandText = @"UPDATE PackInfo SET STATUS = 2 WHERE PackID=@PackID; UPDATE appinfo set	OpUpdateTime = now() where MainPackID = @PackID ";

            #endregion

            return ExecuteNonQuery(commandText, new PackInfoEntity() { PackID = ID });
        }

        /// <summary>
        /// 更新一条安装包信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(PackInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        PackInfo 
                                    SET
                                        ShowName  = @ShowName,
                                        CoopType  = @CoopType,
                                        PackFrom  = @PackFrom,
                                        DownTimes = @DownTimes,
                                        DownTimesReal = @DownTimesReal,
                                        CommentTimes = @CommentTimes,
                                        IsMainVer = @IsMainVer,
                                        PackSize = @PackSize,
                                        VerCode = @VerCode,
                                        VerName = @VerName,
                                        PackName = @PackName,
                                        PackSign = @PackSign,
                                        IconUrl = @IconUrl,
                                        AppPicUrl = @AppPicUrl,
                                        PackUrl = @PackUrl,
                                        PackUrl2 = @PackUrl2,
                                        PackMD5 = @PackMD5,
                                        CompDesc = @CompDesc,
                                        UpdateDesc = @UpdateDesc,
                                        Remarks = @Remarks,
                                        UpdateTime = @UpdateTime,
                                        Status = @Status,
                                        OpUpdateTime = @OpUpdateTime,
                                        permission=@permission
                                    WHERE PackID = @PackID ;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 获取安装包列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<PackInfoEntity> GetDataList(PackInfoEntity entity)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"
                                SELECT 
                                    PackID,
                                    AppID,
                                    ShowName,
                                    CoopType,
                                    PackFrom,
                                    DownTimes,
                                    DownTimesReal,
                                    CommentTimes,
                                    IsMainVer,
                                    PackSize,
                                    VerCode,
                                    VerName,
                                    PackName,
                                    IconUrl,
                                    AppPicUrl,
                                    PackUrl,
                                    PackUrl2,
                                    PackMD5,
                                    CompDesc,
                                    UpdateDesc,
                                    Remarks,
                                    CreateTime,
                                    UpdateTime,
                                    Status,
                                    OpCreateTime,
                                    OpUpdateTime,
                                    permission
                                    FROM
                                PackInfo  WHERE Status !=3 and AppID = @AppID ORDER BY UpdateTime  DESC ");

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return objReader.ReaderToList<PackInfoEntity>() as List<PackInfoEntity>;
            }
        }

        /// <summary>
        /// 获取一条安装包信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public PackInfoEntity GetSingle(int ID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        a.PackID,
                                        a.AppID,
                                        a.IsMainVer,
                                        a.ShowName,
                                        a.DownTimes,
                                        a.CoopType,
                                        a.DownTimesReal,
                                        a.CommentTimes,
                                        a.IconUrl,
                                        a.AppPicUrl,
                                        a.PackFrom,
                                        a.PackName,
                                        a.PackSize,
                                        a.VerCode,
                                        a.VerName,
                                        a.PackSign,
                                        a.PackUrl,
                                        a.PackUrl2,
                                        a.PackMD5,
                                        a.CompDesc,
                                        a.UpdateDesc,
                                        a.Remarks,
                                        a.CreateTime,
                                        a.UpdateTime,
                                        a.Status, 
                                        a.OpCreateTime,
                                        a.OpUpdateTime,
                                        a.permission
                                    FROM
                                        PackInfo AS a 
                                        INNER JOIN AppInfo AS b 
                                            ON a.AppID = b.AppID 
                                    WHERE a.PackID = @PackID 
                                    LIMIT 0, 1 ";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@PackID", ID)))
            {
                return objReader.ReaderToModel<PackInfoEntity>() as PackInfoEntity;
            }
        }

        /// <summary>
        /// 获取最新一条安装包信息
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public PackInfoEntity GetNewSingle(int ID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        a.PackID,
                                        a.AppID,
                                        a.IsMainVer,
                                        a.ShowName,
                                        a.DownTimes,
                                        a.CoopType,
                                        a.DownTimesReal,
                                        a.CommentTimes,
                                        a.IconUrl,
                                        a.AppPicUrl,
                                        a.PackFrom,
                                        a.PackName,
                                        a.PackSize,
                                        a.VerCode,
                                        a.VerName,
                                        a.PackSign,
                                        a.PackUrl,
                                        a.PackUrl2,
                                        a.PackMD5,
                                        a.CompDesc,
                                        a.UpdateDesc,
                                        a.Remarks,
                                        a.CreateTime,
                                        a.UpdateTime,
                                        a.Status, 
                                        a.OpCreateTime,
                                        a.OpUpdateTime,
                                        a.permission
                                    FROM
                                        PackInfo AS a
                                    WHERE a.AppID = @AppID order by a.UpdateTime desc
                                    LIMIT 0, 1 ";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@AppID", ID)))
            {
                return objReader.ReaderToModel<PackInfoEntity>() as PackInfoEntity;
            }

        }

        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCount(PackInfoEntity entity)
        {
            #region CommandText

            StringBuilder commandText = new StringBuilder(@"SELECT COUNT(0) FROM PackInfo WHERE AppID=@AppID  ");

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            this.Condition(commandText, paramsList, entity);

            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), paramsList.ToArray()).Convert<int>();
        }

        /// <summary>
        /// 更新安装包主版本
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateMainVersion(PackInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE PackInfo SET IsMainVer = 0 WHERE AppID=@AppID;Update AppInfo set MainIconUrl = @MainIconUrl where AppID=@AppID";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }


        /// <summary>
        /// 获取应用下的安装包个数
        /// </summary>
        /// <param name="AppID"></param>
        /// <returns></returns>
        public int GetAppToPackCount(int AppID)
        {
            string sqlConnStr = Tools.GetConnStrConfig("ActionStateConnectionString");

            #region CommandText

            string commandText = " select count(0) from packinfo where AppID = " + AppID + " and Status=1;";

            #endregion

            var result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText);

            return result.Convert<int>(0);

        }

        /// <summary>
        /// 更新安装包主版本
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateMainVer(PackInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE PackInfo SET IsMainVer = 0 WHERE AppID=@AppID;UPDATE PackInfo SET IsMainVer = 1,UpdateTime=now() WHERE PackID=@PackID;Update AppInfo set MainIconUrl = @MainIconUrl,MainPackID = @PackID,MainPackSize =@PackSize,MainVerCode=@VerCode ,MainVerName=@VerName,PackName=@PackName,PackSign=@PackSign,OpUpdateTime=now() where AppID=@AppID";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }
    }
}