using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Model;
using AppStore.DAL;
using System.Data;

namespace AppStore.BLL
{
    public class GroupBLL
    {
        #region 通用
        /// <summary>
        /// 删除分组元素信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool DeleteElem(int groupElemID)
        {
            return new GroupElemsDAL().Delete(groupElemID);
        }
        /// <summary>
        /// 删除分组信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool DeleteInfo(int groupID)
        {
            return new GroupInfoDAL().Delete(groupID);
        }
        /// <summary>
        /// 修改排序
        /// </summary>
        /// <param name="elemId"></param>
        /// <returns></returns>
        public int UpdateOrderNoById(int elemId, int orderNo)
        {
            return new GroupElemsDAL().UpdateOrderNoById(elemId, orderNo);
        }
        public bool InsertElem(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().Insert(entity);
        }

        public int InsertElemForId(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().InsertForId(entity);
        }
        public int InsertInfoForId(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().InsertForId(entity);
        }
        /// <summary>
        /// 更新排序号
        /// </summary>
        /// <param name="elems"></param>
        /// <returns></returns>
        public bool UpdateElemOrder(Dictionary<int, int> elems)
        {
            return new GroupElemsDAL().UpdateOrder(elems);
        }
        /// <summary>
        /// 更新位置
        /// </summary>
        /// <param name="elems"></param>
        /// <returns></returns>
        public bool UpdateElemPos(Dictionary<int, int> elems)
        {
            return new GroupElemsDAL().UpdatePos(elems);
        }
        public GroupElemsEntity GetGroupElemByID(int groupElemID)
        {
            return new GroupElemsDAL().GetOneByID(groupElemID);
        }
        public GroupElemsEntity GetGroupElemByPosID(int groupElemID)
        {
            return new GroupElemsDAL().GetOneByPosID(groupElemID);
        }

        public GroupInfoEntity GetGroupInfoByID(int groupID)
        {
            return new GroupInfoDAL().GetOneByID(groupID);
        }

        public List<GroupElemsEntity> GetGroupElemsByGroupID(int groupID)
        {
            return new GroupElemsDAL().GetListByGroupID(groupID);
        }

        public List<GroupElemsEntity> GetList(int top, string strWhere, string filedOrder)
        {
            return new GroupElemsDAL().GetList(top, strWhere, filedOrder);
        }

        /// <summary>
        /// 获取按时间排序的分组ID
        /// </summary>
        /// <returns></returns>
        public GroupInfoEntity[] GetorderByTimeArray()
        {
            return new GroupInfoDAL().GetorderByTimeArray();
        }

        #endregion

        #region 新手推荐
        /// <summary>
        /// 获取新手推荐列表
        /// </summary>
        /// <param name="GroupTypeID"></param>
        /// <param name="SchemeID"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> BeginnerRecommendGetList(int GroupTypeID, int SchemeID)
        {
            return new GroupDAL().BeginnerRecommendGetList(GroupTypeID, SchemeID);
        }

        /// <summary>
        /// 获取新手推荐列表的分组Id
        /// </summary>
        /// <returns></returns>
        public int BeginnerRecommendGetGroupId(int GroupTypeId, int SchemeID)
        {
            return new GroupDAL().BeginnerRecommendGetGroupId(GroupTypeId, SchemeID);
        }

        public bool BeginnerRecommendInsert(GroupElemsEntity entity)
        {

            return new GroupElemsDAL().Insert(entity);
        }

        public bool BeginnerRecommendUpdate(GroupElemsEntity entity)
        {

            return new GroupElemsDAL().Update(entity);
        }

        #endregion

        #region 闪屏

        /// <summary>
        /// 获取闪屏列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupInfoEntity> FlashPageGetList()
        {
            return new GroupDAL().FlashPageGetList();
        }

        /// <summary>
        /// 新增闪屏
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool FlashPageInsert(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Insert(entity);
        }
        /// <summary>
        /// 修改闪屏
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool FlashPageUpdate(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Update(entity);
        }
        #endregion

        #region 热门搜索词
        /// <summary>
        /// 获取热门搜索词列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> SearchWordsGetList(int SchemeID)
        {
            return new GroupDAL().SearchWordsGetList(SchemeID);
        }
        public int MaxPisId(int SchemeID)
        {
            return new GroupDAL().MaxPisId(SchemeID);
        }

        /// <summary>
        /// 获取热门搜索词的分组Id
        /// </summary>
        /// <returns></returns>
        public int SearchWordsGetGroupId(int SchemeID)
        {
            return new GroupDAL().SearchWordsGetGroupId(SchemeID);
        }

        public bool SearchWordsInsert(GroupElemsEntity entity)
        {

            return new GroupElemsDAL().Insert(entity);
        }

        public bool SearchWordsUpdate(GroupElemsEntity entity)
        {

            return new GroupElemsDAL().Update(entity);
        }

        #endregion

        #region 热门应用
        /// <summary>
        /// 获取热门应用列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupElemsEntity> PopularGameGetList(int SchemeID)
        {
            return new GroupDAL().PopularGameGetList(SchemeID);
        }

        /// <summary>
        /// 获取热门应用的分组Id
        /// </summary>
        /// <returns></returns>
        public int PopularGameGetGroupId(int SchemeID)
        {
            return new GroupDAL().PopularGameGetGroupId(SchemeID);
        }

        public bool PopularGameInsert(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().Insert(entity);
        }

        public bool PopularGameUpdate(GroupElemsEntity entity)
        {

            return new GroupElemsDAL().Update(entity);
        }

        #endregion

        #region 专题

        /// <summary>
        /// 获取专题列表
        /// </summary>
        /// <param name="StartIndex"></param>
        /// <param name="EndIndex"></param>
        /// <param name="entity"></param>
        /// <returns></returns>
        public List<GroupInfoEntity> SpecialTopicGetList(int SchemeID, int groupClass)
        {
            return new GroupDAL().SpecialTopicGetList(SchemeID, groupClass);
        }

         /// 修改分组排序号
        /// </summary>
        /// <param name="elemId"></param>
        /// <returns></returns>
        public int UpdateGroupOrderNoById(int elemId, int orderNo)
        {
            return new GroupDAL().UpdateOrderNoById(elemId, orderNo);
        }
        /// <summary>
        /// 新增专题
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool SpecialTopicInsert(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Insert(entity);
        }
        /// <summary>
        /// 修改专题
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool SpecialTopicUpdate(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Update(entity);
        }
        #endregion

        #region 首页推荐

        /// <summary>
        /// 获取首页推荐数据，需要根据PosID筛选不同位置的数据
        /// </summary>
        /// <param name="schemeID">方案ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> GetHomePageRecommend(int groupTypeID, int schemeID)
        {
            return new GroupDAL().GetHomePageRecommend(groupTypeID, schemeID);
        }
        public DataSet GetHomePageRecommendExcel(int groupTypeID, int schemeID)
        {
            return new GroupDAL().GetHomePageRecommendExcel(groupTypeID, schemeID);
        }

        #region 桌面游戏推荐

        /// <summary>
        /// 获取桌面游戏推荐，需要根据PosID筛选不同位置的数据
        /// </summary>
        /// <param name="schemeID">方案ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> GetGameRecommend(int groupTypeID, int schemeID)
        {
            return new GroupDAL().GetGameRecommend(groupTypeID, schemeID);
        }
        #endregion

        /// <summary>
        /// 根据首页推荐的位置ID获取元素
        /// </summary>
        /// <param name="posId"></param>
        /// <param name="GroupTypeID">分类ID</param>
        /// <param name="SchemeID">方案ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> HomePageRecommendGetElemsByPosId(int posId, int GroupTypeID, int SchemeID)
        {
            return new GroupDAL().HomePageRecommendGetElemsByPosId(posId, GroupTypeID, SchemeID);
        }
        /// <summary>
        /// 根据首页推荐的位置ID获取元素
        /// </summary>
        /// <param name="posId"></param>
        /// <param name="GroupTypeID">分类ID</param>
        /// <param name="SchemeID">方案ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> HomePageRecommendGetElemsByPosId(int GroupTypeID, int SchemeID)
        {
            return new GroupDAL().HomePageRecommendGetElemsByPosId(GroupTypeID, SchemeID);
        }
        /// <summary>
        /// 获取首页推荐的分组Id
        /// </summary>
        /// <param name="GroupTypeID"></param>
        /// <param name="SchemeID"></param>
        /// <returns></returns>
        public int HomePageRecommendGetGroupId(int GroupTypeID, int SchemeID)
        {
            return new GroupDAL().HomePageRecommendGetGroupId(GroupTypeID, SchemeID);
        }

        #endregion

        #region 桌面精品推荐

        /// <summary>
        /// 获取首页推荐数据，需要根据PosID筛选不同位置的数据
        /// </summary>
        /// <returns></returns>
        public List<GroupElemsEntity> GetLauncherRecommend(int schemeid, int groupTypeID)
        {
            return new GroupDAL().GetLauncherRecommend(schemeid, groupTypeID);
        }

        public DataSet GetDataSetLauncherRecommend(int schemeid, int groupTypeID)
        {
            return new GroupDAL().GetDataSetLauncherRecommend(schemeid, groupTypeID);
        }
        /// <summary>
        /// 获取桌面精品推荐的分组Id
        /// </summary>
        /// <returns></returns>
        /// 
        public int LauncherRecommendGetGroupId(int schemeid, int groupTypeID)
        {
            return new GroupDAL().LauncherRecommendGetGroupId(schemeid, groupTypeID);
        }

        /// <summary>
        /// 根据ID获取桌面精品推荐的一条数据
        /// </summary>
        /// <param name="groupElemID"></param>
        /// <returns></returns>
        public GroupElemsEntity GetLauncherRecommendSingle(int schemeid, int groupElemID, int groupTypeID)
        {
            return new GroupDAL().GetLauncherRecommendSingle(schemeid, groupElemID, groupTypeID);
        }

        /// <summary>
        /// 更新精品桌面的推荐内容
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateLauncherRecommend(GroupElemsEntity entity)
        {
            return new GroupDAL().UpdateLauncherRecommend(entity);
        }
        #endregion


        /// <summary>
        /// 判断同一分组，位置编号是否重复
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool IsExistPosID(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().IsExistPosID(entity);
        }

        public int GetMaxPosID(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().GetMaxPosID(entity);
        }
    }
}