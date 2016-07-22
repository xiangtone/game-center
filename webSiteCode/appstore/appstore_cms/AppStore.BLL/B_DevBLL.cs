using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class B_DevBLL
    {
        /// <summary>
        /// 返回开发者列表
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, string> GetDevListDic()
        {
            return new B_DevDAL().GetDevList().ToDictionary(s => s.CPID, s => s.CPName);
        }


        /// <summary>
        /// 根据开发者名称获取开发者ID
        /// </summary>
        /// <param name="devName"></param>
        /// <returns></returns>
        public string GetDevIDByName(string CPName)
        {
            List<CPsEntity> list = new B_DevDAL().GetDevIDByName(CPName);

            StringBuilder result = new StringBuilder();

            foreach (CPsEntity item in list)
            {
                result.AppendFormat("'{0}',", item.CPID);
            }

            return result.ToString().TrimEnd(',');
        }

        public List<CPsEntity> GetDataListByPager(int startIndex, int endIndex, ref int total, string CPName)
        {
            total = new B_DevDAL().GetTotalCount(CPName);
            return new B_DevDAL().GetDataListByPager(startIndex, endIndex, CPName);
        }

        /// <summary>
        /// 获取最后一条更新的数据的实体
        /// </summary>
        /// <returns></returns>
        public CPsEntity GetLastSingleData()
        {
            return new B_DevDAL().GetLastSingleData();
        }


        /// <summary>
        /// 获取主键列表
        /// </summary>
        /// <returns></returns>
        public Dictionary<int, string> GetParmaryKey()
        {
            List<CPsEntity> list = new B_DevDAL().GetParmaryKey();

            Dictionary<int, string> dic = new Dictionary<int, string>();

            foreach (CPsEntity item in list)
            {
                if (dic.ContainsKey(item.CPID))
                {
                    continue;
                }
                else
                {
                    dic.Add(item.CPID, item.CPName);
                }
            }

            return dic;
        }


        /// <summary>
        /// 新增一条开发者信息
        /// </summary>
        /// <param name="currentEntity"></param>
        /// <returns></returns>
        public bool Insert(CPsEntity currentEntity)
        {
            return new B_DevDAL().Insert(currentEntity);
        }


        /// <summary>
        /// 更新开发者数据
        /// </summary>
        /// <param name="currentEntity"></param>
        /// <returns></returns>
        public bool Update(CPsEntity currentEntity)
        {
            return new B_DevDAL().Update(currentEntity);
        }

    }
}
