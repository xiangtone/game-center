using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using AppStore.DAL;

namespace AppStore.BLL
{
    public class FeedBackBLL
    {
        public List<FeedBackEntity> GetFeedBackList(int ClientId, int pageindex, int pagesize, ref int totalCount)
        {
            return new FeedBackDAL().GetFeedBackList(ClientId,pageindex,pagesize,ref totalCount);
        }

        public int UpdateRemarks(int id, string r) {
            return new FeedBackDAL().UpdateRemarks(id, r);
        }
    }
}
