using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class AppCommentsBLL
    {
        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <returns></returns>
        public List<AppCommentsEntity> GetDataList()
        {
             return new AppCommentsDAL().GetDataList();
        }

        /// <summary>
        /// 获取应用信息
        /// </summary>
        /// <returns></returns>
        public List<AppCommentsEntity> GetDataList(string searchType, string searchKey, string searchOrder, int pageIndex, int pageSize)
        {
            return new AppCommentsDAL().GetDataList(searchType, searchKey, searchOrder, pageIndex, pageSize);
        }
        public int GetTotalCount(string searchType, string searchKey, string searchOrder, int pageIndex = -1, int pageSize = -1)
        {
            return new AppCommentsDAL().GetTotalCount(searchType, searchKey, searchOrder, pageIndex, pageSize);
        }
    }
}
