using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public  class AppInformBLL
    {

         /// <summary>
        /// 列表信息
        /// </summary>
        /// <returns></returns>
         public List<AppInformEntity> GetAppinformList()
        {
            return new AppInformDAL().GetAppinformList();
        }

         public int UpdateRemarks(int id, string r)
         {
             return new AppInformDAL().UpdateRemarks(id, r);
         }
    }
}
