using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;


using MySql.Data.MySqlClient;

using AppStore.Model;
using AppStore.Common;
using System.Data;


namespace AppStore.DAL
{
    /// <summary>
    /// 与分组相关的DAL
    /// </summary>
    public class GroupDAL : BaseDAL
    {
        #region 新手推荐
        /// <summary>
        /// 获取新手推荐列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> BeginnerRecommendGetList(int GroupTypeID, int SchemeID)
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
                                        where GroupID = (
	                                       SELECT 
                                            GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = @GroupTypeID 
                                            AND STATUS = 1 AND SchemeID =@SchemeID
                                        LIMIT 1
                                        ) AND Status = 1 Order By OrderNo ASC;");

            #endregion


            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupTypeID", GroupTypeID), new MySqlParameter("@SchemeID", SchemeID)))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }

        }

        /// <summary>
        /// 获取新手推荐列表的分组Id
        /// </summary>
        /// <returns></returns>
        public int BeginnerRecommendGetGroupId(int GroupTypeID, int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                            GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = @GroupTypeID 
                                            AND STATUS = 1 AND SchemeID =@SchemeID
                                        LIMIT 1");

            #endregion

            return nwbase_sdk.Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupTypeID", GroupTypeID), new MySqlParameter("@SchemeID", SchemeID)), 0);


        }
        #endregion

        #region 闪屏相关
        /// <summary>
        /// 获取闪屏列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupInfoEntity> FlashPageGetList()
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"select `GroupID`,
                                    `GroupTypeID`,
                                    `OrderType`,
                                    `OrderNo`,
                                    `GroupName`,
                                    `GroupDesc`,
                                    `GroupPicUrl`,
                                    `RecommWord`,
                                    `Remarks`,
                                    `StartTime`,
                                    `EndTime`,
                                    `CreateTime`,
                                    `UpdateTime`,
                                    `Status` 
                                 from GroupInfo
                                 where GroupTypeID=4105 AND Status = 1
                                 order by `StartTime` asc;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), null))
            {
                return objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;
            }

        }
        #endregion

        #region 热门搜索词
        public List<GroupElemsEntity> SearchWordsGetList(int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT GroupElemID,
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
                                        STATUS
                                        FROM GroupElems
                                        WHERE GroupID = (
	                                      SELECT 
                                            GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = 4104 
                                            AND STATUS = 1 AND SchemeID =@SchemeID ORDER BY UpdateTime DESC
                                        LIMIT 1
                                        ) AND STATUS = 1
                                        ORDER BY PosID ASC;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), new MySqlParameter("@SchemeID", SchemeID)))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }
        public int MaxPisId(int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                        max(PosID)
                                        FROM GroupElems
                                        WHERE GroupID = (
	                                      SELECT 
                                            GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = 4104 
                                            AND STATUS = 1 AND SchemeID =@SchemeID ORDER BY UpdateTime DESC
                                        LIMIT 1
                                        ) AND STATUS = 1
                                        ORDER BY StartTime ASC;");

            #endregion

            return ConvertHelper.ToInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), new MySqlParameter("@SchemeID", SchemeID)));

        }
        /// <summary>
        /// 获取热门搜索词的分组Id
        /// </summary>
        /// <returns></returns>
        public int SearchWordsGetGroupId(int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT   GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = 4104
                                            AND STATUS = 1 
                                            AND SchemeID = @SchemeID order by UpdateTime desc
                                        LIMIT 1");

            #endregion

            return nwbase_sdk.Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), new MySqlParameter("@SchemeID", SchemeID)), 0);
        }
        #endregion

        #region 热门游戏
        public List<GroupElemsEntity> PopularGameGetList(int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                        GroupElemID,
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
                                        STATUS 
                                    FROM
                                        GroupElems 
                                    WHERE GroupID = 
                                        (SELECT 
                                            GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = 4103
                                            AND STATUS = 1 
                                            AND SchemeID = @SchemeID  ORDER BY UpdateTime DESC
                                        LIMIT 1) 
                                        AND STATUS = 1 
                                    ORDER BY StartTime ASC ; ");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), new MySqlParameter("@SchemeID", SchemeID)))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }

        /// <summary>
        /// 获取热门搜索词的分组Id
        /// </summary>
        /// <returns></returns>
        public int PopularGameGetGroupId(int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT   GroupID 
                                        FROM
                                            GroupSchemes 
                                        WHERE GroupTypeID = 4103
                                            AND STATUS = 1 
                                            AND SchemeID = @SchemeID order by UpdateTime desc
                                        LIMIT 1");

            #endregion

            return nwbase_sdk.Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), new MySqlParameter("@SchemeID", SchemeID)), 0);


        }
        #endregion

        #region 专题相关
        /// <summary>
        /// 获取专题列表
        /// </summary>
        /// <returns></returns>
        public List<GroupInfoEntity> SpecialTopicGetList(int SchemeID, int groupClass)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                    a.GroupID,
                                    a.GroupTypeID,
                                    a.OrderType,
                                    a.OrderNo,
                                    a.GroupName,
                                    a.GroupDesc,
                                    a.GroupPicUrl,
                                    a.GroupTips,
                                    a.Remarks,
                                    a.StartTime,
                                    a.EndTime,
                                    a.CreateTime,
                                    a.UpdateTime,
                                    a.Status,
                                    a.OrderNo
                                FROM
                                    GroupInfo AS a 
                                    INNER JOIN GroupSchemes AS b
                                    ON a.GroupID = b.GroupID
                                WHERE a.GroupTypeID div 100 = @groupClass

                                    AND a.STATUS = 1 AND b.Status=1 AND b.SchemeID=@SchemeID
                                ORDER BY OrderNo ASC ;
                                ");

            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@SchemeID", SchemeID));
            paramsList.Add(new MySqlParameter("@groupClass", groupClass));

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;
            }

        }

        /// 修改排序号
        /// </summary>
        /// <param name="elemId"></param>
        /// <returns></returns>
        public int UpdateOrderNoById(int elemId, int orderNo)
        {

            string commandText = @"Update GroupInfo set UpdateTime='" + DateTime.Now + "',OrderNo=" + orderNo + " where GroupID=" + elemId;

            return MySql.Data.MySqlClient.MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText);


        }
        #endregion
        #region 桌面游戏推荐

        public List<GroupElemsEntity> GetGameRecommend(int groupTypeID, int schemeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        b.SchemeID,
                                        a.GroupElemID,
                                        a.GroupID,
                                        a.PosID,
                                        a.OrderNo,
                                        a.ElemType,
                                        a.RecommTitle,
                                        a.RecommWord,
                                        a.RecommPicUrl,
                                        a.CreateTime,
                                        a.GroupType,
                                        a.OrderType,
                                        a.ElemID,
                                        a.STATUS,
                                        a.StartTime,
                                        a.EndTime,
                                        a.ShowType
                                    FROM
                                        GroupElems AS a,appinfo p
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID 
                                    WHERE a.STATUS = 1
                                        AND a.GroupID = (SELECT   GroupID  FROM  GroupSchemes  WHERE GroupTypeID = @GroupTypeID   AND STATUS = 1  AND SchemeID = @SchemeID  LIMIT 1) 
                                        AND a.STATUS = 1 
                                        AND a.StartTime <= NOW() 
                                        AND a.EndTime >= NOW() 
                                        AND b.SchemeID =@SchemeID
                                    ORDER BY a.PosID ASC,
                                        a.OrderNo ASC ";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@GroupTypeID", groupTypeID), new MySqlParameter("@SchemeID", schemeID)))
            {
                List<GroupElemsEntity> result = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;

                return result;
            }
        }
        #region 首页推荐

        public List<GroupElemsEntity> GetHomePageRecommend(int groupTypeID, int schemeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        b.SchemeID,
                                        a.GroupElemID,
                                        a.GroupID,
                                        a.PosID,
                                        a.OrderNo,
                                        a.ElemType,
                                        a.RecommTitle,
                                        a.RecommWord,
                                        a.RecommPicUrl,
                                        a.CreateTime,
                                        a.GroupType,
                                        a.OrderType,
                                        a.ElemID,
                                        a.STATUS,
                                        a.StartTime,
                                        a.EndTime,
                                        a.ShowType,
                                        a.RecommTag
                                    FROM
                                        GroupElems AS a 
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID 
                                    WHERE a.STATUS = 1 
                                        AND a.GroupID = (SELECT   GroupID  FROM  GroupSchemes  WHERE GroupTypeID = @GroupTypeID   AND STATUS = 1  AND SchemeID = @SchemeID  LIMIT 1) 
                                        AND a.STATUS = 1 
                                        AND a.StartTime <= NOW() 
                                        AND a.EndTime >= NOW() 
                                        AND b.SchemeID =@SchemeID
                                    ORDER BY a.PosID ASC,
                                        a.OrderNo ASC ";

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@GroupTypeID", groupTypeID), new MySqlParameter("@SchemeID", schemeID)))
            {
                List<GroupElemsEntity> result = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;

                return result;
            }
        }
        #endregion
        /// <summary>
        /// 根据首页推荐的位置ID获取元素
        /// </summary>
        /// <param name="posId"></param>
        /// <param name="GroupTypeID"></param>
        /// <param name="SchemeID"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> HomePageRecommendGetElemsByPosId(int posId, int GroupTypeID, int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@PosID", posId));
            paramsList.Add(new MySqlParameter("@GroupTypeID", GroupTypeID));
            paramsList.Add(new MySqlParameter("@SchemeID", SchemeID));


            #region CommandText

            commandText.Append(@"SELECT 
                                    GroupElemID,
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
                                    STATUS 
                                FROM
                                    GroupElems 
                                WHERE GroupID = 
                                    (SELECT 
                                        a.GroupID 
                                    FROM
                                        GroupInfo AS a 
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID
                                    WHERE a.GroupTypeID = @GroupTypeID
                                        AND a.Status = 1 
                                        AND b.Status = 1 
                                        AND b.SchemeID = @SchemeID
                                    LIMIT 1) 
                                    AND STATUS = 1 
                                    AND PosID = @PosID 
                                ORDER BY PosID ASC,
                                    OrderNo ASC;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }
        /// <summary>
        /// 根据首页推荐的位置ID获取元素
        /// </summary>
        /// <param name="posId"></param>
        /// <param name="GroupTypeID"></param>
        /// <param name="SchemeID"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> HomePageRecommendGetElemsByPosId(int GroupTypeID, int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupTypeID", GroupTypeID));
            paramsList.Add(new MySqlParameter("@SchemeID", SchemeID));


            #region CommandText

            commandText.Append(@"SELECT 
                                    GroupElemID,
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
                                    STATUS 
                                FROM
                                    GroupElems 
                                WHERE GroupID = 
                                    (SELECT 
                                        a.GroupID 
                                    FROM
                                        GroupInfo AS a 
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID
                                    WHERE a.GroupTypeID = @GroupTypeID
                                        AND a.Status = 1 
                                        AND b.Status = 1 
                                        AND b.SchemeID = @SchemeID
                                    LIMIT 1) 
                                    AND STATUS = 1 
                                ORDER BY PosID ASC,
                                    OrderNo ASC;");

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText.ToString(), paramsList.ToArray()))
            {
                return objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }
        }
        /// <summary>
        /// 获取首页推荐的分组Id
        /// </summary>
        /// <returns></returns>
        public int HomePageRecommendGetGroupId(int GroupTypeID, int SchemeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            commandText.Append(@"SELECT 
                                        a.GroupID 
                                    FROM
                                        GroupInfo AS a 
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID
                                    WHERE a.GroupTypeID = @GroupTypeID
                                        AND a.Status = 1 
                                        AND b.Status = 1 
                                        AND b.SchemeID = @SchemeID
                                    LIMIT 1");

            #endregion

            return nwbase_sdk.Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), new MySqlParameter("@GroupTypeID", GroupTypeID), new MySqlParameter("@SchemeID", SchemeID)), 0);
        }
        #endregion

        #region 桌面精品推荐

        /// <summary>
        /// 获取桌面精品推荐
        /// GroupTypeID 5101 为桌面精品分组
        /// SchemeID 1 为通用方案
        /// </summary>
        /// <returns></returns>
        public List<GroupElemsEntity> GetLauncherRecommend(int schemeID, int groupTypeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        GroupElemID,
                                        GroupID,
                                        PosID,
                                        OrderNo,
                                        ElemType,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        CreateTime,
                                        GroupType,
                                        OrderType,
                                        ElemID,
                                        STATUS,
                                        StartTime,
                                        EndTime 
                                    FROM
                                        GroupElems 
                                    WHERE STATUS = 1 
                                        AND GroupID = (
	                                        SELECT g.GroupID FROM GroupInfo as g inner join GroupSchemes as s
                                            on g.status = 1 
                                            and s.status = 1
                                            and s.SchemeID= @SchemeID
                                            and s.GroupTypeID = @GroupTypeID
                                            and g.GroupID = s.GroupID
                                            limit 1
                                        ) 
                                        AND STATUS = 1
                                    ORDER BY PosID ASC,OrderNo ASC ";

            MySqlParameter[] param = {
                new MySqlParameter("@GroupTypeID",groupTypeID),
                new MySqlParameter("@SchemeID",schemeID)
            };

            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, param))
            {
                List<GroupElemsEntity> result = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;

                return result;
            }
        }

        public DataSet GetDataSetLauncherRecommend(int schemeID, int groupTypeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        GroupElemID,
                                        GroupID,
                                        PosID,
                                        OrderNo,
                                        ElemType,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        CreateTime,
                                        GroupType,
                                        OrderType,
                                        ElemID,
                                        STATUS,
                                        StartTime,
                                        EndTime 
                                    FROM
                                        GroupElems 
                                    WHERE STATUS = 1 
                                        AND GroupID = (
	                                        SELECT g.GroupID FROM GroupInfo as g inner join GroupSchemes as s
                                            on g.status = 1 
                                            and s.status = 1
                                            and s.SchemeID= @SchemeID
                                            and s.GroupTypeID = @GroupTypeID
                                            and g.GroupID = s.GroupID
                                            limit 1
                                        ) 
                                        AND STATUS = 1
                                    ORDER BY PosID ASC,OrderNo ASC ";

            MySqlParameter[] param = {
                new MySqlParameter("@GroupTypeID",groupTypeID),
                new MySqlParameter("@SchemeID",schemeID)
            };

            #endregion

            return MySqlHelper.ExecuteDataset(this.ConnectionString, commandText, param);

        }

        /// <summary>
        /// 根据ID获取桌面精品推荐的一条数据
        /// </summary>
        /// <param name="groupElemID"></param>
        /// <returns></returns>
        public GroupElemsEntity GetLauncherRecommendSingle(int schemeID, int groupElemID, int groupTypeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        GroupElemID,
                                        GroupID,
                                        PosID,
                                        OrderNo,
                                        ElemType,
                                        RecommTitle,
                                        RecommWord,
                                        RecommPicUrl,
                                        RecommVal,
                                        CreateTime,
                                        GroupType,
                                        OrderType,
                                        ElemID,
                                        STATUS,
                                        StartTime,
                                        EndTime 
                                    FROM
                                        GroupElems 
                                    WHERE STATUS = 1 
                                        AND GroupID = 
                                        (SELECT 
                                            g.GroupID 
                                        FROM
                                            GroupInfo AS g 
                                            INNER JOIN GroupSchemes AS s 
                                                ON g.status = 1 
                                                AND s.status = 1 
                                                AND s.SchemeID = @SchemeID 
                                                AND s.GroupTypeID = @groupTypeID 
                                                AND g.GroupID = s.GroupID 
                                        LIMIT 1) 
                                        AND STATUS = 1  AND GroupElemID=@GroupElemID";

            MySqlParameter[] param = {
                new MySqlParameter("@GroupTypeID",groupTypeID),
                new MySqlParameter("@GroupElemID", groupElemID),
                new MySqlParameter("@SchemeID",schemeID)

            };
            #endregion

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, param))
            {
                return objReader.ReaderToModel<GroupElemsEntity>() as GroupElemsEntity;
            }
        }

        /// <summary>
        /// 更新精品桌面的推荐内容
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateLauncherRecommend(GroupElemsEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE GroupElems SET 
                                            GroupElemID = @GroupElemID,
                                            ElemID = @ElemID,
                                            RecommTitle = @RecommTitle,
                                            RecommWord = @RecommWord,
                                            RecommPicUrl = @RecommPicUrl,
                                            RecommVal = @RecommVal,
                                            STATUS = @STATUS,
                                            Remarks = @Remarks,
                                            UpdateTime  = NOW()
                                            WHERE GroupElemID=@GroupElemID
                                            ";

            #endregion

            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@GroupElemID", entity.GroupElemID));
            paramsList.Add(new MySqlParameter("@ElemID", entity.ElemID));
            paramsList.Add(new MySqlParameter("@RecommTitle", entity.RecommTitle));
            paramsList.Add(new MySqlParameter("@RecommWord", entity.RecommWord));
            paramsList.Add(new MySqlParameter("@RecommPicUrl", entity.RecommPicUrl));
            paramsList.Add(new MySqlParameter("@RecommVal", entity.RecommVal));
            paramsList.Add(new MySqlParameter("@STATUS", entity.Status));
            paramsList.Add(new MySqlParameter("@Remarks", entity.Remarks));

            return MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray()) > 0 ? true : false;
        }

        /// <summary>
        /// 获取桌面精品推荐的分组Id
        /// </summary>
        /// <returns></returns>
        public int LauncherRecommendGetGroupId(int schemeID, int groupTypeID)
        {
            StringBuilder commandText = new StringBuilder();

            #region CommandText

            //            commandText.Append(@"select GroupID from GroupInfo
            //	                             where GroupTypeID=@GroupTypeID AND Status = 1
            //	                             limit 1");


            commandText.Append(@"
                                SELECT g.GroupID FROM GroupInfo as g inner join GroupSchemes as s
                                            on g.status = 1 
                                            and s.status = 1
                                            and s.SchemeID= @SchemeID
                                            and s.GroupTypeID = @GroupTypeID
                                            and g.GroupID = s.GroupID
                                            limit 1 ");

            MySqlParameter[] param = {
                new MySqlParameter("@GroupTypeID",groupTypeID),
                new MySqlParameter("@SchemeID",schemeID)
            };
            #endregion

            return nwbase_sdk.Tools.GetInt(MySqlHelper.ExecuteScalar(this.ConnectionString, commandText.ToString(), param), 0);
        }
        #endregion


        public DataSet GetHomePageRecommendExcel(int groupTypeID, int schemeID)
        {
            #region CommandText

            string commandText = @"SELECT 
                                        b.SchemeID,
                                        a.GroupElemID,
                                        a.GroupID,
                                        a.PosID,
                                        a.OrderNo,
                                        a.ElemType,
                                        a.RecommTitle,
                                        a.RecommWord,
                                        a.RecommPicUrl,
                                        a.CreateTime,
                                        a.GroupType,
                                        a.OrderType,
                                        a.ElemID,
                                        a.STATUS,
                                        a.StartTime,
                                        a.EndTime,
                                        a.ShowType,
                                        a.RecommTag
                                    FROM
                                        GroupElems AS a 
                                        INNER JOIN GroupSchemes AS b 
                                            ON a.GroupID = b.GroupID 
                                    WHERE a.STATUS = 1 
                                        AND a.GroupID = (SELECT   GroupID  FROM  GroupSchemes  WHERE GroupTypeID = @GroupTypeID   AND STATUS = 1  AND SchemeID = @SchemeID  LIMIT 1) 
                                        AND a.STATUS = 1 
                                        AND a.StartTime <= NOW() 
                                        AND a.EndTime >= NOW() 
                                        AND b.SchemeID =@SchemeID
                                    ORDER BY a.PosID ASC,
                                        a.OrderNo ASC ";

            #endregion
            string sql = string.Format(@"select a.*,
                                            p.ShowName,
                                            p.PackName,
                                            p.MainVerName,
                                            p.CoopType,
                                            p.DevName,
                                            p.MainPackID from ({0}) as a left join appinfo p on a.ElemID = p.AppID", commandText);
            sql = string.Format(@"select g.PosID as '首页位置',g.ShowType,g.ElemType as '跳转类型',g.ShowName as '游戏名',g.PackName as '包名',g.MainVerName as '版本',p.AppID as '游戏分类',g.CoopType as '合作类型',g.DevName as '开发者',g.Status as '状态',p.permission as '调用权限',g.RecommTitle  from ({0}) as g left join packinfo p on p.PackID = g.MainPackID", sql);
            //nwbase_utils.TextLog.Default.Info(sql);
            return MySqlHelper.ExecuteDataset(this.ConnectionString, sql, new MySqlParameter("@GroupTypeID", groupTypeID), new MySqlParameter("@SchemeID", schemeID));
        }
    }
}
