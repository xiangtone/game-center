using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class GroupElemsBLL
    {
        public bool Insert(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().Insert(entity);
        }

        /// <summary>
        /// 更新分组元素信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().Update(entity);
        }

        /// <summary>
        /// 删除分组元素信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int GroupElemID)
        {
            return new GroupElemsDAL().Delete(GroupElemID);
        }

        
        /// <summary>
        /// 判断推荐元素是否已存在
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool IsExist(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().IsExist(entity);
        }

        #region【分组】

        /// <summary>
        /// 根据分组ID查询分组下元素
        /// </summary>
        /// <param name="groupID">分组ID</param>
        /// <returns></returns>
        public List<GroupElemsEntity> QueryGroupElementByGroupID(int groupID)
        {
            return new GroupElemsDAL().QueryGroupElementByGroupID(groupID);
        }

        /// <summary>
        /// 同一分组内是否存在相同元素
        /// </summary>
        /// <param name="groupID">分组ID</param>
        /// <param name="elementID">元素ID</param>
        /// <returns>false：分组内不存在相同元素；true：分组内存在相同元素</returns>
        public bool ExistSameElementIDInGroup(int groupID, int elementID)
        {
            return new GroupElemsDAL().ExistSameElementIDInGroup(groupID,elementID);
        }

        /// <summary>
        /// 根据分组ID插入分组元素表
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool InsertGroupElement(GroupElemsEntity entity)
        {
            return new GroupElemsDAL().InsertGroupElement(entity);
        } 
        #endregion

        public List<GroupElemsEntity> GetGroupEntityByID(int GroupElemID)
        {
            return new GroupElemsDAL().GetGroupEntityByID(GroupElemID);
        }

        public DataSet QueryDataSetByGroupID(int GroupID)
        {
            return new GroupElemsDAL().QueryDataSetByGroupID(GroupID);
        }
        public DataSet QueryDataSetByGroupID2(int GroupID)
        {
            return new GroupElemsDAL().QueryDataSetByGroupID2(GroupID);
        }
    }
}
