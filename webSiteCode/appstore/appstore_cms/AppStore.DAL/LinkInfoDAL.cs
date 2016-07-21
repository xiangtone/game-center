using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data;
using MySql.Data.MySqlClient;

using AppStore.Model;
using AppStore.Common;


namespace AppStore.DAL
{
    public class LinkInfoDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(LinkInfoEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@LinkID", entity.LinkID));
            paramsList.Add(new MySqlParameter("@CPID", entity.CPID));
            paramsList.Add(new MySqlParameter("@DevName", entity.DevName));
            paramsList.Add(new MySqlParameter("@LinkName", entity.LinkName));
            paramsList.Add(new MySqlParameter("@ShowName", entity.ShowName));
            paramsList.Add(new MySqlParameter("@LinkUrl", entity.LinkUrl));
            paramsList.Add(new MySqlParameter("@IconUrl", entity.IconUrl));
            paramsList.Add(new MySqlParameter("@CoopType", entity.CoopType));
            paramsList.Add(new MySqlParameter("@LinkTag", entity.LinkTag));
            paramsList.Add(new MySqlParameter("@LinkDesc", entity.LinkDesc));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            //paramsList.Add(new MySqlParameter("@OpCreateTime", DateTime.Now));
            //paramsList.Add(new MySqlParameter("@OpUpdateTime", DateTime.Now));
            //paramsList.Add(new MySqlParameter("@OpStatus", entity.OpStatus));

            paramsList.Add(new MySqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", entity.EndIndex));

            return paramsList;
        }

        #endregion

        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, LinkInfoEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }


        #endregion

        public bool Insert(LinkInfoEntity entity)
        {
            #region CommandText

            string commandText = @"INSERT INTO LinkInfo (
                                        CPID,
				                        DevName,
				                        LinkName,
				                        ShowName,
				                        LinkUrl,
				                        IconUrl,
				                        CoopType,
				                        LinkTag,
				                        LinkDesc,
				                        Remarks,
				                        CreateTime,
				                        UpdateTime,
				                        Status
				                  
                                    ) 
                                    VALUES
                                        (
                                        @CPID,
                                        @DevName,
                                        @LinkName,
                                        @ShowName,
                                        @LinkUrl,
                                        @IconUrl,
                                        @CoopType,
                                        @LinkTag,
                                        @LinkDesc,
                                        @Remarks,
                                        @CreateTime,
                                        @UpdateTime,
                                        @Status
                                        ) ;";

            #endregion

            return this.ExecuteNonQuery(commandText, entity);
        }

        public bool Update(LinkInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                         LinkInfo 
                                    SET
                                        CPID = @CPID,
                                        DevName = @DevName,
                                        LinkName = @LinkName,
                                        ShowName = @ShowName,
                                        LinkUrl = @LinkUrl,
                                        IconUrl = @IconUrl,
                                        CoopType = @CoopType,
                                        LinkTag = @LinkTag,
                                        LinkDesc = @LinkDesc,
                                        Remarks = @Remarks,
                                        UpdateTime = @UpdateTime,
                                        Status = @Status,
                                        OpUpdateTime = @OpUpdateTime,
                                        OpStatus = @OpStatus 
                                    WHERE LinkID =@LinkID ;";

            #endregion

            return this.ExecuteNonQuery(commandText, entity);
        }

        public bool Delete(LinkInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                         LinkInfo 
                                    SET
                                        UpdateTime =@UpdateTime,
                                        Status =@Status,
                                      
                                    WHERE LinkID =@LinkID ;  ";

            #endregion

            return this.ExecuteNonQuery(commandText, entity);
        }

        public List<LinkInfoEntity> GetDataList(int StartIndex, int EndIndex, string showName)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        LinkID,
                                        CPID,
                                        DevName,
                                        LinkName,
                                        ShowName,
                                        LinkUrl,
                                        IconUrl,
                                        CoopType,
                                        LinkTag,
                                        LinkDesc,
                                        Remarks,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                    FROM
                                        LinkInfo ";

            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            if (!string.IsNullOrEmpty(showName))
            {
                commandText += @" Where ShowName like @ShowName";
                paramsList.Add(new MySqlParameter("@ShowName", string.Format("%{0}%", showName)));
            }

            commandText += @" LIMIT @StartIndex, @EndIndex ;";


            paramsList.Add(new MySqlParameter("@StartIndex", StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", EndIndex));

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, paramsList.ToArray()))
            {
                return objReader.ReaderToList<LinkInfoEntity>() as List<LinkInfoEntity>;
            }
        }

        public int GetTotalCount(string showName)
        {
            string commandText = @"Select Count(0) from LinkInfo ";

            List<MySqlParameter> paramsList = new List<MySqlParameter>();


            if (!string.IsNullOrEmpty(showName))
            {
                commandText += @" Where ShowName like @ShowName";
                paramsList.Add(new MySqlParameter("@ShowName", string.Format("%{0}%", showName)));
            }


            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();
        }


        public LinkInfoEntity GetSingle(int linkID)
        {
            string commandText = @"SELECT 
                                        LinkID,
                                        CPID,
                                        DevName,
                                        LinkName,
                                        ShowName,
                                        LinkUrl,
                                        IconUrl,
                                        CoopType,
                                        LinkTag,
                                        LinkDesc,
                                        Remarks,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        OpCreateTime,
                                        OpUpdateTime,
                                        OpStatus 
                                    FROM
                                        LinkInfo WHERE LinkID=@LinkID LIMIT 0,1";

            return MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@LinkID", linkID)).ReaderToModel<LinkInfoEntity>() as LinkInfoEntity;
        }
    }
}
