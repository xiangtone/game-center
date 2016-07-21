using MySql.Data.MySqlClient;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Common;

namespace AppStore.DAL
{
    public class GroupTypeDAL : BaseDAL
    {
        public List<GroupTypeEntity> GetDataList(int typeClass)
        {
            string commandText = @"SELECT TypeID,TypeClass,TypeName,OrderNo FROM grouptypes WHERE Status =1 and TypeClass = @TypeClass;";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@TypeClass", typeClass)))
            {
                return objReader.ReaderToList<GroupTypeEntity>() as List<GroupTypeEntity>;
            }
        }

        /// <summary>
        /// 获取分类列表
        /// </summary>
        /// <param name="startIndex"></param>
        /// <param name="endIndex"></param>
        /// <returns></returns>
        public List<GroupTypeEntity> GetGroupTypeList(int startIndex, int endIndex)
        {
            string commandText = @"SELECT TypeID,TypeClass,TypeName,OrderNo,Status FROM grouptypes limit @StartIndex,@EndIndex";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@StartIndex", startIndex), new MySqlParameter("@EndIndex", endIndex)))
            {
                return objReader.ReaderToList<GroupTypeEntity>() as List<GroupTypeEntity>;
            }
        }

        public int TotalCount()
        {
            string commandText = @"select count(0) from grouptypes";

            int result = MySqlHelper.ExecuteScalar(this.ConnectionString, commandText).Convert<int>();

            return result;

        }

        /// <summary>
        /// 获取全部分类
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> GetAllCategory(int typeclass)
        {
            #region CommandText

            string commandText = @"	SELECT 
	                                    typeID,
	                                    typeClass,
	                                    TypeName,
	                                    TypePicUrl,
	                                    CreateTime,
	                                    UpdateTime,
	                                    OrderNo,
	                                    STATUS 
	                                FROM
	                                    grouptypes 
	                                WHERE (
		                                (TypeID > 1100 
		                                    AND TypeID < 1200) 
		                                OR (TypeID > 1200 
		                                    AND TypeID < 1300)
	                                    ) 
	                                    AND STATUS = 1 ";
            if (typeclass > 0)
            {
                commandText += " AND TypeClass =" + typeclass;
            }

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                return objReader.ReaderToList<GroupTypeEntity>() as List<GroupTypeEntity>;
            }
        }
        
        /// <summary>
        /// 获取全部分类
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> GetCategory(int SchemeID)
        {
            #region CommandText

            string commandText = @"	SELECT * FROM appstore2.grouptypes where TypeID in ( SELECT 
                                      a.GroupTypeID
                                    FROM
                                      GroupSchemes AS a 
                                      INNER JOIN GroupInfo AS b 
                                      ON a.GroupID=b.GroupID
                                      INNER JOIN GroupTypes AS c
                                      ON a.GroupTypeID=c.TypeID
                                    Where SchemeID=@SchemeID) and TypeID not in(1100,1200) and TypeClass in(11,12) ";
            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@SchemeID", SchemeID)))
            {
                return objReader.ReaderToList<GroupTypeEntity>() as List<GroupTypeEntity>;
            }
        }
        

        #region【分组管理】
        /// <summary>
        /// 编辑分组时查询分组类型
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> QueryGroupType()
        {
            string commandText = @"SELECT TypeID,TypeClass,TypeName,OrderNo FROM grouptypes WHERE typeClass IN (11,12)";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, null))
            {
                return objReader.ReaderToList<GroupTypeEntity>() as List<GroupTypeEntity>;
            }
        }
        #endregion
    }
}

