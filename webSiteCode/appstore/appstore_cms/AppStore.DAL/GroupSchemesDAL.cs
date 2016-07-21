using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

//
using MySql.Data.MySqlClient;

using AppStore.Model;
using AppStore.Common;

namespace AppStore.DAL
{
    public class GroupSchemesDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(GroupSchemesEntity entity)
        {

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@SchemeID", entity.SchemeID));
            paramsList.Add(new MySqlParameter("@GroupID", entity.GroupID));
            paramsList.Add(new MySqlParameter("@GroupTypeID", entity.GroupTypeID));
            paramsList.Add(new MySqlParameter("@OrderType", entity.OrderType));
            paramsList.Add(new MySqlParameter("@CreateTime", DateTime.Now));
            paramsList.Add(new MySqlParameter("@UpdateTime", DateTime.Now));
            paramsList.Add(new MySqlParameter("@Status", entity.Status));

            return paramsList;
        }

        private bool ExecuteNonQuery(string commandText, GroupSchemesEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }

        #endregion

        public bool Insert(GroupSchemesEntity entity)
        {
            string commandText = @"INSERT INTO `GroupSchemes`
                                                (`SchemeID`,
                                                `GroupID`,
                                                `GroupTypeID`,
                                                `OrderType`,
                                                `CreateTime`,
                                                `UpdateTime`,
                                                `Status`)
                                                VALUES
                                                (
                                                @SchemeID,
                                                @GroupID,
                                                @GroupTypeID,
                                                @OrderType,
                                                @CreateTime,
                                                @UpdateTime,
                                                @Status
                                                );";
            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 禁用方案
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Delete(GroupSchemesEntity entity)
        {
            string commandText = @"UPDATE GroupSchemes SET Status = 0,UpDateTime=NOW() Where SchemeID=@SchemeID and GroupID=@GroupID;";

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 更新方案
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(GroupSchemesEntity entity)
        {
            string commandText = @"Update GroupSchemes Set GroupTypeID =@GroupTypeID,OrderType = @OrderType,UpDateTime=NOW(),Status=@Status Where SchemeID=@SchemeID and GroupID=@GroupID;";

            return ExecuteNonQuery(commandText, entity);
        }

        /// <summary>
        /// 获取一条方案信息
        /// </summary>
        /// <param name="schemeID"></param>
        /// <param name="groupID"></param>
        /// <returns></returns>
        public GroupSchemesEntity GetSingle(int schemeID, int groupID)
        {
            string commandText = @"SELECT 
                                      SchemeID,
                                      GroupID,
                                      GroupTypeID,
                                      OrderType,
                                      CreateTime,
                                      UpdateTime,
                                      Status 
                                    FROM
                                      GroupSchemes WHERE  SchemeID=@SchemeID and GroupID=@GroupID; ";

            List<MySqlParameter> paramsList = this.GetMySqlParameters(new GroupSchemesEntity() { SchemeID = schemeID, GroupID = groupID });

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, paramsList.ToArray()))
            {
                return objReader.ReaderToModel<GroupSchemesEntity>() as GroupSchemesEntity;
            }

        }

        /// <summary>
        /// 获取方案列表
        /// </summary>
        /// <param name="startIndex"></param>
        /// <param name="endIndex"></param>
        /// <returns></returns>
        public List<GroupSchemesEntity> GetGroupSchemesList(int schemeID, int startIndex, int endIndex)
        {
            #region CommandText

            string commandText = @"SELECT 
                                      a.SchemeID,
                                      a.GroupID,
                                      b.GroupName,
                                      a.GroupTypeID,
                                      c.TypeName,
                                      b.OrderType,
                                      a.CreateTime,
                                      a.UpdateTime,
                                      c.Remarks,
                                      a.Status 
                                    FROM
                                      GroupSchemes AS a 
                                      INNER JOIN GroupInfo AS b 
                                      ON a.GroupID=b.GroupID
                                      INNER JOIN GroupTypes AS c
                                      ON a.GroupTypeID=c.TypeID
                                    Where SchemeID=@SchemeID
                                     LIMIT @StartIndex, @EndIndex ; ";

            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@SchemeID", schemeID));
            paramsList.Add(new MySqlParameter("@StartIndex", startIndex));
            paramsList.Add(new MySqlParameter("@EndIndex", endIndex));

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, paramsList.ToArray()))
            {
                return objReader.ReaderToList<GroupSchemesEntity>() as List<GroupSchemesEntity>;
            }
        }

        public int TotalCount(int schemeID)
        {
            string commandText = @"select count(0) from GroupSchemes where SchemeID=@SchemeID";

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText, new MySqlParameter("@SchemeID", schemeID)).Convert<int>(0);

            return result;
        }
    }
}
