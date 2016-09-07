using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using MySql.Data.MySqlClient;

namespace updatesys_cms.DAL
{
    public class UpdateInfo : Base
    {

        /// <summary>
        /// 获取所有更新信息
        /// </summary>
        /// <param name="appid"></param>
        /// <returns></returns>
        public List<Model.UpdateInfo> GetAll(int appid, int schemeId)
        {
            string sql = @"SELECT UpdateId,AppId,SchemeId,PackName,ChannelNo,VerName,VerCode,PackUrl,PackSize,PackMD5,UpdateType,PubTime,Status,ForceUpdateVerCode
                           FROM UpdateInfo";

            string condition_sql = " WHERE Status = 1 ";

            List<MySqlParameter> param = new List<MySqlParameter>();

            if (appid > 0)
            {
                condition_sql = condition_sql + " AND AppId=?AppId";
                var param0 = Common.DbHelper.MySqlHelper.MakeInParam("?AppId", MySqlDbType.Int32, 0, appid);
                param.Add(param0);
            }
            if (schemeId > -1)
            {
                condition_sql = condition_sql + " AND SchemeId=?SchemeId";
                var param0 = Common.DbHelper.MySqlHelper.MakeInParam("?SchemeId", MySqlDbType.Int32, 0, schemeId);
                param.Add(param0);
            }
            sql += condition_sql + " ORDER BY PubTime DESC,AppId,VerCode DESC,ChannelNo;";

            MySqlParameter[] paramArray = param.Count == 0 ? null : param.ToArray();

            using (var rdr = _UpdaterHelper.ExecuteReader(System.Data.CommandType.Text, sql, paramArray))
            {
                if (rdr != null && !rdr.IsClosed)
                {
                    List<Model.UpdateInfo> result = new List<Model.UpdateInfo>();
                    while (rdr.Read())
                    {
                        Model.UpdateInfo eachItem = new Model.UpdateInfo();
                        int i = 0;
                        eachItem.UpdateId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.AppId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.SchemeId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.ChannelNo = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.VerName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.VerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackUrl = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.PackSize = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackMD5 = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.UpdateType = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PubTime = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, new DateTime(1970, 1, 1));
                        eachItem.Status = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.ForceUpdateVerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        result.Add(eachItem);
                    }
                    return result;
                }
                else
                    return null;
            }
        }

        /// <summary>
        /// 新增更新信息
        /// </summary>
        /// <param name="updateInfo"></param>
        /// <returns></returns>
        public bool Add(Model.UpdateInfo updateInfo)
        {
            string sql = "INSERT INTO UpdateInfo(AppId,SchemeId,PackName,ChannelNo,VerName,VerCode,PackUrl,PackSize,PackMD5,UpdateType,UpdatePrompt,UpdateDesc,PubTime,Status,ForceUpdateVerCode) VALUES(?AppId,?SchemeId,?PackName,?ChannelNo,?VerName,?VerCode,?PackUrl,?PackSize,?PackMD5,?UpdateType,?UpdatePrompt,?UpdateDesc,?PubTime,?Status,?ForceUpdateVerCode);";

            MySqlParameter[] param = new MySqlParameter[15];
            int i = 0;
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?AppId", MySqlDbType.Int32, 0, updateInfo.AppId);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?SchemeId", MySqlDbType.Int32, 0, updateInfo.SchemeId);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackName", MySqlDbType.VarChar, 50, updateInfo.PackName);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?ChannelNo", MySqlDbType.VarChar, 20, updateInfo.ChannelNo);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?VerName", MySqlDbType.VarChar, 50, updateInfo.VerName);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?VerCode", MySqlDbType.Int32, 0, updateInfo.VerCode);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackUrl", MySqlDbType.VarChar, 200, updateInfo.PackUrl);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackSize", MySqlDbType.Int32, 0, updateInfo.PackSize);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackMD5", MySqlDbType.VarChar, 50, updateInfo.PackMD5);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateType", MySqlDbType.Int32, 0, updateInfo.UpdateType);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateDesc", MySqlDbType.VarChar, 500, updateInfo.UpdateDesc);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdatePrompt", MySqlDbType.VarChar, 200, updateInfo.UpdatePrompt);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PubTime", MySqlDbType.Datetime, 0, updateInfo.PubTime);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?Status", MySqlDbType.Int32, 0, updateInfo.Status);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?ForceUpdateVerCode", MySqlDbType.Int32, 0, updateInfo.ForceUpdateVerCode);

            var result = _UpdaterHelper.ExecuteNonQuery(System.Data.CommandType.Text, sql, param);
            return result > 0;
        }

        /// <summary>
        /// 修改更新信息
        /// </summary>
        /// <param name="updateInfo"></param>
        /// <returns></returns>
        public bool Update(Model.UpdateInfo updateInfo)
        {
            string sql = "UPDATE UpdateInfo set AppId=?AppId,SchemeId=?SchemeId,PackName=?PackName,ChannelNo=?ChannelNo,VerName=?VerName,VerCode=?VerCode,PackUrl=?PackUrl,PackSize=?PackSize,PackMD5=?PackMD5,UpdateType=?UpdateType,UpdatePrompt=?UpdatePrompt,UpdateDesc=?UpdateDesc,PubTime=?PubTime,Status=?Status,ForceUpdateVerCode=?ForceUpdateVerCode WHERE UpdateId=?UpdateId";

            MySqlParameter[] param = new MySqlParameter[16];
            int i = 0;
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?AppId", MySqlDbType.Int32, 0, updateInfo.AppId);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?SchemeId", MySqlDbType.Int32, 0, updateInfo.SchemeId);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackName", MySqlDbType.VarChar, 50, updateInfo.PackName);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?ChannelNo", MySqlDbType.VarChar, 20, updateInfo.ChannelNo);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?VerName", MySqlDbType.VarChar, 50, updateInfo.VerName);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?VerCode", MySqlDbType.Int32, 0, updateInfo.VerCode);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackUrl", MySqlDbType.VarChar, 200, updateInfo.PackUrl);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackSize", MySqlDbType.Int32, 0, updateInfo.PackSize);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PackMD5", MySqlDbType.VarChar, 50, updateInfo.PackMD5);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateType", MySqlDbType.Int32, 0, updateInfo.UpdateType);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdatePrompt", MySqlDbType.VarChar, 200, updateInfo.UpdatePrompt);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateDesc", MySqlDbType.VarChar, 500, updateInfo.UpdateDesc);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?PubTime", MySqlDbType.Datetime, 0, updateInfo.PubTime);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?Status", MySqlDbType.Int32, 0, updateInfo.Status);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateId", MySqlDbType.Int32, 0, updateInfo.UpdateId);
            param[i++] = Common.DbHelper.MySqlHelper.MakeInParam("?ForceUpdateVerCode", MySqlDbType.Int32, 0, updateInfo.ForceUpdateVerCode);


            var result = _UpdaterHelper.ExecuteNonQuery(System.Data.CommandType.Text, sql, param);
            return result > 0;
        }

        /// <summary>
        /// 删除更新信息
        /// </summary>
        /// <param name="updateId"></param>
        /// <returns></returns>
        public bool Del(int updateId)
        {
            string sql = "DELETE FROM UpdateInfo WHERE UpdateId=?UpdateId";
            MySqlParameter param = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateId", MySqlDbType.Int32, 0, updateId);
            var result = _UpdaterHelper.ExecuteNonQuery(System.Data.CommandType.Text, sql, param);
            return result > 0;
        }

        /// <summary>
        /// 获取更新信息
        /// </summary>
        /// <param name="appid"></param>
        /// <returns></returns>
        public Model.UpdateInfo GetOne(int updateId)
        {
            string sql = @"SELECT UpdateId,AppId,SchemeId,PackName,ChannelNo,VerName,VerCode,PackUrl,PackSize,PackMD5,UpdateType,UpdatePrompt,UpdateDesc,PubTime,Status,ForceUpdateVerCode
                           FROM UpdateInfo WHERE UpdateId = ?UpdateId AND Status = 1 LIMIT 1; ";

            MySqlParameter param = Common.DbHelper.MySqlHelper.MakeInParam("?UpdateId", MySqlDbType.Int32, 0, updateId);

            using (var rdr = _UpdaterHelper.ExecuteReader(System.Data.CommandType.Text, sql, param))
            {
                if (rdr != null && !rdr.IsClosed && rdr.Read())
                {
                    Model.UpdateInfo result = new Model.UpdateInfo();
                    int i = 0;
                    result.UpdateId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.AppId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.SchemeId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.PackName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.ChannelNo = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.VerName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.VerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.PackUrl = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.PackSize = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.PackMD5 = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.UpdateType = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.UpdatePrompt = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.UpdateDesc = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                    result.PubTime = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, new DateTime(1970, 1, 1));
                    result.Status = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    result.ForceUpdateVerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                    return result;
                }
                else
                    return null;
            }
        }

        /// <summary>
        /// 获取最新的安装包信息
        /// </summary>
        /// <returns></returns>
        public List<Model.UpdateInfo> GetNewList()
        {
            string sql = @"SELECT SchemeId,PackName,ChannelNo,VerName,VerCode,PackUrl,PackSize,PackMD5,UpdateType,UpdatePrompt,UpdateDesc,PubTime,Status,ForceUpdateVerCode
FROM UpdateInfo as a
where a.Status=1;";

            using (var rdr = _UpdaterHelper.ExecuteReader(System.Data.CommandType.Text, sql, null))
            {
                if (rdr != null && !rdr.IsClosed)
                {
                    List<Model.UpdateInfo> result = new List<Model.UpdateInfo>();
                    while (rdr.Read())
                    {
                        Model.UpdateInfo eachItem = new Model.UpdateInfo();
                        int i = 0;
                        eachItem.SchemeId = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.ChannelNo = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.VerName = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.VerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackUrl = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.PackSize = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.PackMD5 = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.UpdateType = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.UpdatePrompt = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.UpdateDesc = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, string.Empty);
                        eachItem.PubTime = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, new DateTime(1970, 1, 1));
                        eachItem.Status = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);
                        eachItem.ForceUpdateVerCode = Common.DbHelper.MySqlHelper.GetSqlResult(rdr, i++, 0);

                        result.Add(eachItem);
                    }
                    return result;
                }
                else
                    return null;
            }
            return null;
        }
    }
}