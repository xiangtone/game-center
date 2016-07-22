using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class GroupSchemesBLL
    {
        public bool Insert(GroupSchemesEntity entity)
        {
            return new GroupSchemesDAL().Insert(entity);
        }
        /// <summary>
        /// 获取一条方案信息
        /// </summary>
        /// <param name="schemeID"></param>
        /// <param name="groupID"></param>
        /// <returns></returns>
        public GroupSchemesEntity GetSingle(int schemeID, int groupID)
        {
            return new GroupSchemesDAL().GetSingle(schemeID, groupID);
        }

        /// <summary>
        /// 禁用方案
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Delete(GroupSchemesEntity entity)
        {
            return new GroupSchemesDAL().Delete(entity);
        }

        /// <summary>
        /// 更新方案
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(GroupSchemesEntity entity)
        {
            return new GroupSchemesDAL().Update(entity);
        }

        /// <summary>
        /// 获取方案列表
        /// </summary>
        /// <param name="startIndex"></param>
        /// <param name="endIndex"></param>
        /// <returns></returns>
        public List<GroupSchemesEntity> GetGroupSchemesList(int schemeID, int startIndex, int endIndex, ref int totalCount)
        {
            totalCount = new GroupSchemesDAL().TotalCount(schemeID);
            return new GroupSchemesDAL().GetGroupSchemesList(schemeID, startIndex, endIndex);
        }
    }
}
