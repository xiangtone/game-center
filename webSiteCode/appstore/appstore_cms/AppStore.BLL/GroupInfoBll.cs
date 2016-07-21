using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AppStore.DAL;
using AppStore.Model;
using System.Data;

namespace AppStore.BLL
{
    public class GroupInfoBll
    {
        public bool Insert(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Insert(entity);
        }

        public bool Update(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().Update(entity);
        }

        #region 【分组管理】
        /// <summary>
       /// 获取分组信息
       /// </summary>
       /// <param name="entity"></param>
       /// <returns></returns>
        public List<GroupInfoEntity> GetDataList(GroupInfoEntity entity, ref int totalCount,int scId)
        {
            totalCount = new GroupInfoDAL().GetTotalCount(entity, scId);

            return new GroupInfoDAL().GetDataList(entity, scId);
        }
        public DataSet GetDataSetList(GroupInfoEntity entity, int scId)
        {

            return new GroupInfoDAL().GetDataSetList(entity, scId);
        }
        
        /// <summary>
        /// 根据分组ID查询该条信息
        /// </summary>
        /// <param name="groupID">所需查询分组ID</param>
        /// <returns></returns>
        public GroupInfoEntity QueryGroupInfoByGroupID(int groupID)
        {
            return new GroupInfoDAL().QueryGroupInfoByGroupID(groupID);
        }

        /// <summary>
        /// 更新分组信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool UpdateGroupInfo(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().UpdateGroupInfo(entity);
        }

        /// <summary>
        /// 插入分组信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool InsertGroupInfo(GroupInfoEntity entity)
        {
            return new GroupInfoDAL().InsertGroupInfo(entity);
        }

        /// <summary>
        /// 根据分组ID查询分组名称
        /// </summary>
        /// <param name="groupID">所要查询分组ID</param>
        /// <returns>分组名称</returns>
        public string QueryGroupNameByID(int groupID)
        {
            return new GroupInfoDAL().QueryGroupNameByID(groupID);
        }
        #endregion
    }
}
