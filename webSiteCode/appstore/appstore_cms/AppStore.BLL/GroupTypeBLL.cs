using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class GroupTypeBLL
    {
        public List<GroupTypeEntity> GetDataList(int typeClass)
        {
            return new GroupTypeDAL().GetDataList(typeClass);
        }

        /// <summary>
        /// 获取全部分类
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> GetAllCategory(int typeclass)
        {
            return new GroupTypeDAL().GetAllCategory(typeclass);
        }
            /// <summary>
        /// 获取全部分类
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> GetCategory(int SchemeID)
        {
            return new GroupTypeDAL().GetCategory(SchemeID);
        }

        /// <summary>
        /// 获取分组类型的列表
        /// </summary>
        /// <param name="startIndex"></param>
        /// <param name="endIndex"></param>
        /// <param name="totalCount"></param>
        /// <returns></returns>
        public List<GroupTypeEntity> GetGroupTypeList(int startIndex, int endIndex, ref int totalCount)
        {
            totalCount = new GroupTypeDAL().TotalCount();

            return new GroupTypeDAL().GetGroupTypeList(startIndex, endIndex);
        }

        #region【分组管理】
        /// <summary>
        /// 编辑分组时查询分组类型
        /// </summary>
        /// <returns></returns>
        public List<GroupTypeEntity> QueryGroupType()
        {
            return new GroupTypeDAL().QueryGroupType();
        }
        #endregion
    }
}
