using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;




namespace AppStore.Web
{
    /// <summary>
    /// ActionLog 的摘要说明
    /// </summary>
    public class ActionLogBll
    {
        public bool Add(ActionLogModel model)
        {
            try
            {
                return new ActionLogDal().Add(model);
            }
            catch (Exception ex)
            {
                nwbase_utils.TextLog.Error("error", "Add Action Log", ex);
                return false;
            }
        }

        public List<ActionLogModel> GetList(int pageSize, int currentPage, out int totalCount)
        {
            try
            {
                return new ActionLogDal().GetList(pageSize, currentPage, out totalCount);
            }
            catch (Exception ex)
            {
                nwbase_utils.TextLog.Error("error", "Get Action Log", ex);
                totalCount = 0;
                return null;
            }
        }

        public int Count()
        {
            try
            {
                return new ActionLogDal().Count();
            }
            catch (Exception ex)
            {
                nwbase_utils.TextLog.Error("error", "Get Action Log Count", ex);
                return 0;
            }
        }
    }
}