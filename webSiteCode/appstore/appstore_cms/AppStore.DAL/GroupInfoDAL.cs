using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using MySql.Data.MySqlClient;
using AppStore.Model;
using nwbase_sdk;
using AppStore.Common;
using System.Data;

namespace AppStore.DAL
{
    public class GroupInfoDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(GroupInfoEntity entity)
        {

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupID", entity.GroupID));
            paramsList.Add(new MySqlParameter("@GroupTypeID", entity.GroupTypeID));
            paramsList.Add(new MySqlParameter("@OrderType", entity.OrderType));
            paramsList.Add(new MySqlParameter("@OrderNo", entity.OrderNo));
            paramsList.Add(new MySqlParameter("@GroupName", entity.GroupName));
            paramsList.Add(new MySqlParameter("@GroupDesc", entity.GroupDesc));
            paramsList.Add(new MySqlParameter("@GroupPicUrl", entity.GroupPicUrl));
            paramsList.Add(new MySqlParameter("@RecommWord", entity.GroupTips));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@StartTime", entity.StartTime));
            paramsList.Add(new MySqlParameter("@EndTime", entity.EndTime));
            paramsList.Add(new MySqlParameter("@CreateTime", entity.CreateTime));
            paramsList.Add(new MySqlParameter("@UpdateTime", entity.UpdateTime));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));

            return paramsList;
        }

        private bool ExecuteNonQuery(string commandText, GroupInfoEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }

        #endregion

        public GroupInfoEntity GetOneByID(int groupInfoID)
        {
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@GroupID", groupInfoID));


            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"select `GroupID`,
	                                    `GroupTypeID`,
	                                    `OrderType`,
	                                    `OrderNo`,
	                                    `GroupName`,
	                                    `GroupDesc`,
	                                    `GroupPicUrl`,
	                                    `GroupTips` as RecommWord,
	                                    `Remarks`,
	                                    `StartTime`,
	                                    `EndTime`,
	                                    `CreateTime`,
	                                    `UpdateTime`,
	                                    `Status`
                                        from GroupInfo
                                        where GroupID = @GroupID AND Status = 1;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), param.ToArray()))
            {
                return objReader.ReaderToModel<GroupInfoEntity>() as GroupInfoEntity;
            }
        }

        public bool Insert(GroupInfoEntity entity)
        {
            //            string commandText = @"INSERT INTO `GroupInfo`
            //                                                (`GroupID`,
            //                                                `GroupTypeID`,
            //                                                `OrderType`,
            //                                                `OrderNo`,
            //                                                `GroupName`,
            //                                                `GroupDesc`,
            //                                                `GroupPicUrl`,
            //                                                `RecommWord`,
            //                                                `Remarks`,
            //                                                `StartTime`,
            //                                                `EndTime`,
            //                                                `CreateTime`,
            //                                                `UpdateTime`,
            //                                                `Status`)
            //                                                VALUES
            //                                                (
            //                                                @GroupID,
            //                                                @GroupTypeID,
            //                                                @OrderType,
            //                                                @OrderNo,
            //                                                @GroupName,
            //                                                @GroupDesc,
            //                                                @GroupPicUrl,
            //                                                @RecommWord,
            //                                                @Remarks,
            //                                                @StartTime,
            //                                                @EndTime,
            //                                                @CreateTime,
            //                                                @UpdateTime,
            //                                                @Status
            //                                                );";
            //            return ExecuteNonQuery(commandText, entity);
            int newId = InsertForId(entity);
            return newId > 0;
        }

        public int InsertForId(GroupInfoEntity entity)
        {
            string commandText = @"INSERT INTO `GroupInfo`
                                                (`GroupID`,
                                                `GroupTypeID`,
                                                `OrderType`,
                                                `OrderNo`,
                                                `GroupName`,
                                                `GroupDesc`,
                                                `GroupPicUrl`,
                                                `GroupTips`,
                                                `Remarks`,
                                                `StartTime`,
                                                `EndTime`,
                                                `CreateTime`,
                                                `UpdateTime`,
                                                `Status`)
                                                VALUES
                                                (
                                                @GroupID,
                                                @GroupTypeID,
                                                @OrderType,
                                                @OrderNo,
                                                @GroupName,
                                                @GroupDesc,
                                                @GroupPicUrl,
                                                @RecommWord,
                                                @Remarks,
                                                @StartTime,
                                                @EndTime,
                                                @CreateTime,
                                                @UpdateTime,
                                                @Status);select last_insert_id();";
            int newId = Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, GetMySqlParameters(entity).ToArray()), 0);
            if (newId > 0)
            {
                new GroupSchemesDAL().Insert(new GroupSchemesEntity()
                {
                    GroupID = newId,
                    SchemeID = entity.SchemeID,
                    GroupTypeID = entity.GroupTypeID,
                    OrderType = 0,
                    CreateTime = DateTime.Now,
                    UpdateTime = DateTime.Now,
                    Status = 1
                });
                return newId;
            }
            return -1;
        }

        /// <summary>
        /// 更新分组信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(GroupInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE `GroupInfo`
                                   SET
                                    `OrderType` = @OrderType,
                                    `OrderNo` = @OrderNo,
                                    `GroupName` = @GroupName,
                                    `GroupDesc` = @GroupDesc,
                                    `GroupPicUrl` = @GroupPicUrl,
                                    `GroupTips` = @RecommWord,
                                    `Remarks` = @Remarks,
                                    `StartTime` = @StartTime,
                                    `EndTime` = @EndTime,
                                    `UpdateTime` = @UpdateTime,
                                    `Status` = @Status
                                    WHERE `GroupID` = @GroupID;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 删除分组信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int groupID)
        {
            #region CommandText

            string commandText = @"UPDATE GroupInfo SET STATUS = 0 WHERE GroupID= @GroupID";

            #endregion

            return ExecuteNonQuery(commandText, new GroupInfoEntity() { GroupID = groupID });
        }


        /// <summary>
        /// 获取按时间排序的分组ID
        /// </summary>
        /// <returns></returns>
        public GroupInfoEntity[] GetorderByTimeArray()
        {
            string commandText = @"SELECT  GroupID  FROM GroupInfo  WHERE OrderType = 2 ";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                List<GroupInfoEntity> result = objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;

                return result.ToArray();
            }
        }

        /// <summary>
        /// 查询条件拼接
        /// </summary>
        /// <param name="commandText">sql查询语句</param>
        /// <param name="entity">分组实体类</param>
        /// <returns>拼接后的sql语句</returns>
        private StringBuilder SearchCondition(StringBuilder commandText, GroupInfoEntity entity)
        {
            #region CommandText

            string status = "";
            if (entity.SearchStatus == "1")
            {
                status = status + " and gi.Status=1";
            }
            if (entity.SearchStatus == "0")
            {
                status = status + " and gi.Status=0";
            }
            #endregion

            // 拼接索要查询的数据状态
            StringBuilder newCommandText = new StringBuilder();
            newCommandText.AppendFormat(commandText.ToString(), status);

            //List<MySqlParameter> paramsList = new List<MySqlParameter>();

            //#region【搜索条件】

            //if (entity.SearchGroupType != "0")
            //{
            //    newCommandText.Append(@" and gt.TypeClass=@TypeClass");
            //    paramsList.Add(new MySqlParameter("@TypeClass", entity.SearchGroupType));
            //}
            //if (entity.SearchKeys != "")
            //{
            //    newCommandText.Append(@" and gi.GroupName like '%@GroupName%' or gi.GroupID like '%@GroupID%'");
            //    //paramsList.Add(new MySqlParameter("@GroupName", string.Format("%{0}%", entity.SearchKeys)));
            //    //paramsList.Add(new MySqlParameter("@GroupID", string.Format("%{0}%", entity.SearchKeys)));
            //    paramsList.Add(new MySqlParameter("@GroupName", entity.SearchKeys));
            //    paramsList.Add(new MySqlParameter("@GroupID",entity.SearchKeys));
            //}
            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            #region【搜索条件】

            if (entity.SearchGroupType != "0")
            {
                newCommandText.AppendFormat(@" and gt.TypeClass={0}", entity.SearchGroupType);
                paramsList.Add(new MySqlParameter("@TypeClass", entity.SearchGroupType));
            }
            if (entity.SearchKeys != "")
            {
                newCommandText.AppendFormat(@" and gi.GroupName like '%{0}%' or gi.GroupID like '%{1}%'", entity.SearchKeys, entity.SearchKeys);

            }


            return newCommandText;
            #endregion
        }
        #region【分组管理】
        /// <summary>
        /// 获取总记录数
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetTotalCount(GroupInfoEntity entity, int scId)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT
                                  COUNT(1), 
                                  gt.TypeName,
                                  (SELECT 
                                    COUNT(0) 
                                  FROM
                                    GroupElems ge 
                                  WHERE ge.GroupID = gi.GroupID and ge.Status=1) AS 'ElementCount',
                                  gi.* 
                                FROM
                                  groupinfo gi,
                                  grouptypes gt 
                                WHERE gi.GroupTypeID = gt.TypeID {0} ");
            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            commandText = SearchCondition(commandText, entity);
            #region【搜索条件】
            if (scId > 0)
            {
                commandText.Append(string.Format(@"and gt.TypeClass in (11,12) and GroupTypeID not in(1200,1100) and GroupID in(select GroupID from groupschemes where SchemeID=" + scId + ") Order By {0} desc", entity.SearchOrderType));

            }
            else
            {
                commandText.Append(string.Format(@" Order By {0} desc", entity.SearchOrderType));

            }
            #endregion
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), paramsList.ToArray()).Convert<int>();

        }

        /// <summary>
        /// 获取分组信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupInfoEntity> GetDataList(GroupInfoEntity entity, int scId)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                  gt.TypeName,
                                  (SELECT 
                                    COUNT(0) 
                                  FROM
                                    GroupElems ge 
                                  WHERE ge.GroupID = gi.GroupID and ge.Status=1) AS 'ElementCount',
                                  gi.* 
                                FROM
                                  groupinfo gi,
                                  GroupTypes gt 
                                WHERE gi.GroupTypeID = gt.TypeID {0} ");
            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            #region【搜索条件】

            commandText = SearchCondition(commandText, entity);
            if (scId > 0)
            {
                commandText.Append(string.Format(@"and gt.TypeClass in (11,12) and GroupTypeID not in(1200,1100) and GroupID in(select GroupID from groupschemes where SchemeID=" + scId + ") Order By {0} desc LIMIT @StartIndex, @EndIndex;", entity.SearchOrderType));

            }
            else
            {
                commandText.Append(string.Format(@" Order By {0} desc LIMIT @StartIndex, @EndIndex;", entity.SearchOrderType));

            }
            paramsList.Add(new MySqlParameter("@startIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@endIndex", entity.EndIndex));
            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;
            }
        }

        /// <summary>
        /// 根据分组ID查询该条信息
        /// </summary>
        /// <param name="groupID">所需查询分组ID</param>
        /// <returns></returns>
        public GroupInfoEntity QueryGroupInfoByGroupID(int groupID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                        GroupID,
                                        GroupTypeID,
                                        OrderType,
                                        OrderNo,
                                        GroupName,
                                        GroupDesc,
                                        GroupPicUrl,
                                        GroupTips as RecommWord,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status 
                                    FROM
                                        GroupInfo 
                                    WHERE GroupID=@GroupID");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupID", groupID)))
            {
                return objReader.ReaderToModel<GroupInfoEntity>() as GroupInfoEntity;
            }
        }

        /// <summary>
        /// 更新分组信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateGroupInfo(GroupInfoEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE `GroupInfo`
                                   SET
                                    `GroupName` = @GroupName,
                                    `OrderType` = @OrderType,
                                    `GroupTypeID` = @GroupTypeID,
                                    `GroupTips` = @RecommWord,
                                    `Status` = @Status,
                                    `Remarks` = @Remarks,
                                    `StartTime` = @StartTime,
                                    `EndTime` = @EndTime,
                                    `GroupDesc` = @GroupDesc,
                                    `GroupPicUrl` = @GroupPicUrl,
                                    `UpdateTime` = now()
                                    WHERE `GroupID` = @GroupID";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 插入GroupInfo表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns>false:插入失败；true：插入成功</returns>
        public bool InsertGroupInfo(GroupInfoEntity entity)
        {
            string commandText = @"INSERT INTO `GroupInfo`
                                                (
                                                `GroupTypeID`,
                                                `OrderType`,
                                                `OrderNo`,
                                                `GroupName`,
                                                `GroupDesc`,
                                                `GroupPicUrl`,
                                                `GroupTips`,
                                                `Remarks`,
                                                `StartTime`,
                                                `EndTime`,
                                                `CreateTime`,
                                                `Status`)
                                                VALUES
                                                (
                                                @GroupTypeID,
                                                @OrderType,
                                                @OrderNo,
                                                @GroupName,
                                                @GroupDesc,
                                                @GroupPicUrl,
                                                @RecommWord,
                                                @Remarks,
                                                @StartTime,
                                                @EndTime,
                                                now(),
                                                @Status);select last_insert_id();";
            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>();

            return result > 0 ? true : false;
        }

        /// <summary>
        /// 根据分组ID查询分组名称
        /// </summary>
        /// <param name="groupID">所要查询分组ID</param>
        /// <returns>分组名称</returns>
        public string QueryGroupNameByID(int groupID)
        {
            StringBuilder commandText = new StringBuilder();
            commandText.Append("select GroupName from GroupInfo where GroupID=@GroupID");
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupID", groupID));
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), paramsList.ToArray()).ToString();
        }
        #endregion

        public DataSet GetDataSetList(GroupInfoEntity entity, int scId)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                  gt.TypeName,
                                  (SELECT 
                                    COUNT(0) 
                                  FROM
                                    GroupElems ge 
                                  WHERE ge.GroupID = gi.GroupID and ge.Status=1) AS 'ElementCount',
                                  gi.* 
                                FROM
                                  groupinfo gi,
                                  GroupTypes gt 
                                WHERE gi.GroupTypeID = gt.TypeID {0} ");
            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();

            #region【搜索条件】

            commandText = SearchCondition(commandText, entity);
            if (scId > 0)
            {
                commandText.Append(string.Format(@"and gt.TypeClass in (11,12) and GroupTypeID not in(1200,1100) and GroupID in(select GroupID from groupschemes where SchemeID=" + scId + ") Order By {0} desc LIMIT @StartIndex, @EndIndex;", entity.SearchOrderType));

            }
            else
            {
                commandText.Append(string.Format(@" Order By {0} desc LIMIT @StartIndex, @EndIndex;", entity.SearchOrderType));

            }
            paramsList.Add(new MySqlParameter("@startIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@endIndex", entity.EndIndex));
            #endregion

            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText.ToString(), paramsList.ToArray());
            
        }


    }
}
