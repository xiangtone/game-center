using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
     public class CPsBLL
    {
        /// <summary>
        /// 新增CPs信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(CPsEntity entity)
        {
            return new CPsDAL().Insert(entity);
        }

        /// <summary>
        /// 更新CPs信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(CPsEntity entity)
        {
            return new CPsDAL().Update(entity);
        }
        /// <summary>
        /// 修改信息状态
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID, int Status)
        {
            return new CPsDAL().UpdateStatus(ID, Status);
        }
        /// <summary>
        /// 删除CPs信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            return new CPsDAL().Delete(ID);
        }
        /// <summary>
        /// CPs列表信息
        /// </summary>
        /// <returns></returns>
        public List<CPsEntity> Select()
        {
            return new CPsDAL().Select();
        }
        /// <summary>
        /// 查询单个CPs信息
        /// </summary>
        /// <param name="CPID"></param>
        /// <returns></returns>
        public CPsEntity SelectByNo(int CPID)
        {
            return new CPsDAL().SelectByNo(CPID);
        }
    }
 }