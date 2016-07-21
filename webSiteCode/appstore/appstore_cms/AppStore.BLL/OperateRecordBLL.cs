using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class OperateRecordBLL
    {
        public List<OperateRecordEntity> GetListByAppId(int AppId)
        {
            return new OperateRecordDAL().GetListByElemId(AppId);

        }


        public List<OperateRecordEntity> GetReasonByElemId(int ElemId)
        {
            return new OperateRecordDAL().GetReasonByElemId(ElemId);
        }
        /// <summary>
        /// 新增信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(OperateRecordEntity entity)
        {
            return new OperateRecordDAL().Insert(entity);
        }

        public List<OperateRecordEntity> GetDataList(int StartIndex, int EndIndex, int type, ref int totalCount)
        {
            return new OperateRecordDAL().GetDataList(StartIndex, EndIndex, type, ref totalCount);
        }

        public DataSet GetDataList(int type)
        {
            return new OperateRecordDAL().GetDataList(type);
        }
    }
}
