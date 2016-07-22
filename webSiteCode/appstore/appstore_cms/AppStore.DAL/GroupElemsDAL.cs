using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using MySql.Data;
using MySql.Data.MySqlClient;
using AppStore.Model;

using nwbase_sdk;
using AppStore.Common;
using System.Data;

namespace AppStore.DAL
{
    public class GroupElemsDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(GroupElemsEntity entity)
        {

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupElemID", entity.GroupElemID));
            paramsList.Add(new MySqlParameter("@GroupID", entity.GroupID));
            paramsList.Add(new MySqlParameter("@PosID", entity.PosID));
            paramsList.Add(new MySqlParameter("@ElemType", entity.ElemType));
            paramsList.Add(new MySqlParameter("@ElemID", entity.ElemID));
            paramsList.Add(new MySqlParameter("@GroupType", entity.GroupType));
            paramsList.Add(new MySqlParameter("@OrderType", entity.OrderType));
            paramsList.Add(new MySqlParameter("@OrderNo", entity.OrderNo));
            paramsList.Add(new MySqlParameter("@RecommVal", entity.RecommVal));
            paramsList.Add(new MySqlParameter("@RecommTitle", entity.RecommTitle));
            paramsList.Add(new MySqlParameter("@RecommTag", entity.RecommTag));
            paramsList.Add(new MySqlParameter("@RecommWord", entity.RecommWord));
            paramsList.Add(new MySqlParameter("@RecommPicUrl", entity.RecommPicUrl));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));
            paramsList.Add(new MySqlParameter("@StartTime", entity.StartTime));
            paramsList.Add(new MySqlParameter("@EndTime", entity.EndTime));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));
            paramsList.Add(new MySqlParameter("@ShowType", entity.ShowType));
            paramsList.Add(new MySqlParameter("@StartIndex", entity.StartIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", entity.EndIndex));

            return paramsList;
        }

        private bool ExecuteNonQuery(string commandText, GroupElemsEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }

        #endregion
        /// 修改排序号
        /// </summary>
        /// <param name="elemId"></param>
        /// <returns></returns>
        public int UpdateOrderNoById(int elemId, int orderNo)
        {

            string commandText = @"Update groupelems set UpdateTime='" + DateTime.Now + "',PosId=" + orderNo + ",OrderNo=" + orderNo + " where GroupElemID=" + elemId;

            return MySql.Data.MySqlClient.MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText);


        }

        public GroupElemsEntity GetOneByID(int groupElemID)
        {
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@GroupElemID", groupElemID));


            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTag,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        ShowType
                                        from GroupElems
                                        where GroupElemID = @GroupElemID");

            #endregion
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), param.ToArray()))
            {
                return objReader.ReaderToModel<GroupElemsEntity>() as GroupElemsEntity;
            }
        }
        public GroupElemsEntity GetOneByPosID(int PosID)
        {
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@PosID", PosID));


            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommTag,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        ShowType
                                        from GroupElems
                                        where PosID = @PosID AND Status = 1;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), param.ToArray()))
            {
                return objReader.ReaderToModel<GroupElemsEntity>() as GroupElemsEntity;
            }
        }
        public List<GroupElemsEntity> GetListByGroupID(int groupID)
        {
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@GroupID", groupID));


            StringBuilder commandText = new StringBuilder();

            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommTag,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                        from GroupElems
                                        where GroupID = @GroupID AND Status = 1 Order by OrderNo "); // Desc;


            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), param.ToArray()))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }

        /// <summary>
        /// 获取前几行数据
        /// </summary>
        /// <param name="top"></param>
        /// <param name="strWhere"></param>
        /// <param name="filed"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> GetList(int top, string strWhere, string filedOrder)
        {
            StringBuilder strSql = new StringBuilder();
            strSql.Append("select ");
            //if (top > 0)
            //{
            //    strSql.Append(" top " + top.ToString());
            //}
            strSql.Append(@" GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommTag,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status,
                                        ShowType ");
            strSql.Append(" FROM GroupElems ");
            if (strWhere.Trim() != "")
            {
                strSql.Append(" where " + strWhere);
            }

            strSql.Append(" order by " + filedOrder);

            if (top > 0)
            {
                strSql.Append(" limit " + top);
            }
            StringBuilder strSqls = strSql;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, strSql.ToString()))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }


        public bool Insert(GroupElemsEntity entity)
        {
            string commandText = @"INSERT INTO `GroupElems`
                                        (`GroupElemID`,
                                        `GroupID`,
                                        `PosID`,
                                        `ElemType`,
                                        `ElemID`,
                                        `ShowType`,
                                        `GroupType`,
                                        `OrderType`,
                                        `OrderNo`,
                                        `RecommVal`,
                                        `RecommTitle`,
                                        `RecommTag`,
                                        `RecommWord`,
                                        `RecommPicUrl`,
                                        `Remarks`,
                                        `StartTime`,
                                        `EndTime`,
                                        `CreateTime`,
                                        `UpdateTime`,
                                        `Status`)
                                        VALUES
                                        (
                                        @GroupElemID,
                                        @GroupID,
                                        @PosID,
                                        @ElemType,
                                        @ElemID,
                                        @ShowType,
                                        @GroupType,
                                        @OrderType,
                                        @OrderNo,
                                        @RecommVal,
                                        @RecommTitle,
                                        @RecommTag,
                                        @RecommWord,
                                        @RecommPicUrl,
                                        @Remarks,
                                        @StartTime,
                                        @EndTime,
                                        @CreateTime,
                                        @UpdateTime,
                                        @Status
                                        );
                                        ";

            return ExecuteNonQuery(commandText, entity);
        }

        public int InsertForId(GroupElemsEntity entity)
        {
            string commandText = @"INSERT INTO `GroupElems`
                                        (`GroupElemID`,
                                        `GroupID`,
                                        `PosID`,
                                        `ElemType`,
                                        `ElemID`,
                                        `GroupType`,
                                        `OrderType`,
                                        `OrderNo`,
                                        `RecommVal`,
                                        `RecommTitle`,
                                        `RecommTag`,    
                                        `RecommWord`,
                                        `RecommPicUrl`,
                                        `Remarks`,
                                        `StartTime`,
                                        `EndTime`,
                                        `CreateTime`,
                                        `UpdateTime`,
                                        `Status`)
                                        VALUES
                                        (
                                        @GroupElemID,
                                        @GroupID,
                                        @PosID,
                                        @ElemType,
                                        @ElemID,
                                        @GroupType,
                                        @OrderType,
                                        @OrderNo,
                                        @RecommVal,
                                        @RecommTitle,
                                        @RecommTag,
                                        @RecommWord,
                                        @RecommPicUrl,
                                        @Remarks,
                                        @StartTime,
                                        @EndTime,
                                        @CreateTime,
                                        @UpdateTime,
                                        @Status);select last_insert_id();
                                        ";

            return Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, GetMySqlParameters(entity).ToArray()), 0);
        }

        /// <summary>
        /// 更新分组元素信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(GroupElemsEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE `GroupElems`
                                    SET                               
                                    `PosID` = @PosID,
                                    `ElemType` = @ElemType,
                                    `ElemID` = @ElemID,
                                    `GroupType` = @GroupType,
                                    `OrderType` = @OrderType,
                                    `OrderNo` = @OrderNo,
                                    `RecommVal` = @RecommVal,
                                    `RecommTitle` = @RecommTitle,
                                    `RecommTag` = @RecommTag,
                                    `RecommWord` = @RecommWord,
                                    `RecommPicUrl` = @RecommPicUrl,
                                    `Remarks` = @Remarks,
                                    `StartTime` = @StartTime,
                                    `EndTime` = @EndTime,
                                    `UpdateTime` = @UpdateTime,
                                    `ShowType` = @ShowType,
                                    `Status` = @Status
                                    WHERE `GroupElemID` = @GroupElemID;";

            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 删除分组元素信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int GroupElemID)
        {
            #region CommandText

            string commandText = @"UPDATE GroupElems SET STATUS = 2 WHERE GroupElemID= @GroupElemID";

            #endregion

            return ExecuteNonQuery(commandText, new GroupElemsEntity() { GroupElemID = GroupElemID });
        }

        /// <summary>
        /// 更新排序号
        /// </summary>
        /// <param name="elems"></param>
        /// <returns></returns>
        public bool UpdateOrder(Dictionary<int, int> elems)
        {
            StringBuilder commandText = new StringBuilder();
            string sqlTemplate = "Update GroupElems set OrderNo = {0} where GroupElemID={1};";
            List<MySqlParameter> param = new List<MySqlParameter>();
            int i = 0;
            foreach (KeyValuePair<int, int> eachElem in elems)
            {
                commandText.Append(string.Format(sqlTemplate, "@OrderNo" + i.ToString(), "@ID" + i.ToString()));
                param.Add(new MySqlParameter("@ID" + i.ToString(), eachElem.Key));
                param.Add(new MySqlParameter("@OrderNo" + i.ToString(), eachElem.Value));
                i++;
            }
            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText.ToString(), param.ToArray());

            return base.ExecuteStatus(result);
        }

        /// <summary>
        /// 更新位置
        /// </summary>
        /// <param name="elems"></param>
        /// <returns></returns>
        public bool UpdatePos(Dictionary<int, int> elems)
        {
            StringBuilder commandText = new StringBuilder();
            string sqlTemplate = "Update GroupElems set PosID = {0} where GroupElemID={1};";
            List<MySqlParameter> param = new List<MySqlParameter>();
            int i = 0;
            foreach (KeyValuePair<int, int> eachElem in elems)
            {
                commandText.Append(string.Format(sqlTemplate, "@PosID" + i.ToString(), "@ID" + i.ToString()));
                param.Add(new MySqlParameter("@ID" + i.ToString(), eachElem.Key));
                param.Add(new MySqlParameter("@PosID" + i.ToString(), eachElem.Value));
                i++;
            }
            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText.ToString(), param.ToArray());

            return base.ExecuteStatus(result);
        }

        /// <summary>
        /// 判断推荐元素是否已存在
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool IsExist(GroupElemsEntity entity)
        {
            #region CommandText

            string commandText = @"SELECT COUNT(0) FROM  GroupElems  WHERE  GroupID=@GroupID  AND GroupType=@GroupType AND ElemID = @ElemID AND ElemID != 0 AND Status = 1 AND StartTime < NOW() and EndTime > Now()";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>(0);

            return this.ExecuteStatus(result);
        }

        #region【分组】
        /// <summary>
        /// 根据分组ID查询分组下元素
        /// </summary>
        /// <param name="groupID">分组ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> QueryGroupElementByGroupID(int groupID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText
            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                        from GroupElems
                                        where GroupID = @GroupID and Status=1");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupID", groupID)))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }

        }

        /// <summary>
        /// 同一分组内是否存在相同元素
        /// </summary>
        /// <param name="groupID">分组ID</param>
        /// <param name="elementID">元素ID</param>
        /// <returns>false：分组内不存在相同元素；true：分组内存在相同元素</returns>
        public bool ExistSameElementIDInGroup(int groupID, int elementID)
        {
            #region CommandText
            StringBuilder commandText = new StringBuilder();
            commandText.Append(@"SELECT 
                                  COUNT(*) 
                                FROM
                                  GroupElems 
                                WHERE GroupID = @GroupID 
                                  AND ElemID = @ElementID 
                                  AND STATUS = 1 ");
            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupID", groupID));
            paramsList.Add(new MySqlParameter("@ElementID", elementID));
            return MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), paramsList.ToArray()).Convert<int>(0) > 0 ? true : false;
        }

        /// <summary>
        /// 根据GroupID插入分组元素表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool InsertGroupElement(GroupElemsEntity entity)
        {
            #region CommandText
            string commandText = @"INSERT INTO `GroupElems`
                                        (`GroupElemID`,
                                        `GroupID`,
                                        `PosID`,
                                        `ElemType`,
                                        `ElemID`,
                                        `GroupType`,
                                        `OrderType`,
                                        `OrderNo`,
                                        `RecommVal`,
                                        `RecommTitle`,
                                        `RecommWord`,
                                        `RecommPicUrl`,
                                        `Remarks`,
                                        `StartTime`,
                                        `EndTime`,
                                        `CreateTime`,
                                        `Status`)
                                        VALUES
                                        (
                                        @GroupElemID,
                                        @GroupID,
                                        @PosID,
                                        @ElemType,
                                        @ElemID,
                                        @GroupType,
                                        @OrderType,
                                        @OrderNo,
                                        @RecommVal,
                                        @RecommTitle,
                                        @RecommWord,
                                        @RecommPicUrl,
                                        @Remarks,
                                        @StartTime,
                                        @EndTime,
                                        now(),
                                        @Status
                                        )";
            #endregion

            return ExecuteNonQuery(commandText, entity);
        }

        #endregion

        /// <summary>
        /// 判断同一分组，位置编号是否重复
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool IsExistPosID(GroupElemsEntity entity)
        {
            #region CommandText

            string commandText = @"SELECT COUNT(0) FROM  GroupElems  WHERE  GroupID=@GroupID and Status=1  and PosID = @PosID and GroupElemID <> @GroupElemID";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, paramsList.ToArray()).Convert<int>(0);

            return this.ExecuteStatus(result);
        }

        /// <summary>
        /// 获取同一分组中，最大的位置id
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int GetMaxPosID(GroupElemsEntity entity)
        {
            #region CommandText

            string commandText = @"SELECT MAX(POSID) FROM  GroupElems  WHERE  GroupID=@GroupID ";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(entity);

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                if (objReader.Read())
                {
                    return Tools.GetInt(objReader[0], 0);
                }
            }

            return 0;
        }

        public List<GroupElemsEntity> GetGroupEntityByID(int GroupElemID)
        {
            List<MySqlParameter> param = new List<MySqlParameter>();
            param.Add(new MySqlParameter("@GroupElemID", GroupElemID));


            StringBuilder commandText = new StringBuilder();

            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommTag,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                        from GroupElems
                                        where GroupElemID = @GroupElemID AND Status = 1 ");


            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), param.ToArray()))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }

        public DataSet QueryDataSetByGroupID(int GroupID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText
            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                        from GroupElems
                                        where GroupID = @GroupID and Status=1");

            #endregion


            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupID", GroupID));

        }
        public DataSet QueryDataSetByGroupID2(int GroupID)
        {
            StringBuilder commandText = new StringBuilder();
            string datetime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            #region CommandText
            commandText.Append(@"select GroupElemID,
                                        GroupID,
                                        PosID,
                                        ElemType,
                                        ElemID,
                                        GroupType,
                                        OrderType,
                                        OrderNo,
                                        RecommVal,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        Remarks,
                                        StartTime,
                                        EndTime,
                                        CreateTime,
                                        UpdateTime,
                                        Status
                                        from GroupElems
                                        where GroupID = @GroupID and Status=1 and StartTime < '" + datetime + "' and EndTime > '" + datetime + "' order by PosID");

            #endregion

           
            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupID", GroupID));

        }
    }
}
