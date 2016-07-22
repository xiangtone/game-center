using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class LinkInfoBLL
    {
        public bool Insert(LinkInfoEntity entity)
        {
            return new LinkInfoDAL().Insert(entity);
        }

        public bool Update(LinkInfoEntity entity)
        {
            return new LinkInfoDAL().Update(entity);
        }

        public bool Delete(LinkInfoEntity entity)
        {
            return new LinkInfoDAL().Delete(entity);
        }

        public List<LinkInfoEntity> GetDataList(int StartIndex, int EndIndex, ref int totalCount, string showName)
        {
            totalCount = new LinkInfoDAL().GetTotalCount(showName);
            return new LinkInfoDAL().GetDataList(StartIndex, EndIndex, showName);
        }

        public LinkInfoEntity GetSingle(int linkID)
        {
            return new LinkInfoDAL().GetSingle(linkID);
        }
    }
}
