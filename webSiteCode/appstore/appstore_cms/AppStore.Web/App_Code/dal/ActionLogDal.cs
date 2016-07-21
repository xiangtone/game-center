using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data;

using nwbase_utils.DbHelper;
/// <summary>
/// ActionLogDal 的摘要说明
/// </summary>
/// 
namespace AppStore.Web
{
    public class ActionLogDal : BaseDal
    {
        public ActionLogDal()
        {
            //
            // TODO: 在此处添加构造函数逻辑
            //
            _DBHelper = new MySqlHelper(nwbase_utils.Tools.GetConnStrConfig("cmsbase"));
        }

        private IDbHelper _DBHelper;

        public bool Add(ActionLogModel model)
        {
            string sql = "INSERT INTO actionlog	(UserId, UserIP, `Action`, Content, Level)	VALUES (?UserId, ?UserIP, ?Action, ?Content, ?Level);";
            List<MySql.Data.MySqlClient.MySqlParameter> param = new List<MySql.Data.MySqlClient.MySqlParameter>();
            param.Add(MySqlHelper.MakeInParam("?UserId", MySql.Data.MySqlClient.MySqlDbType.Int32, 0, model.UserId));
            param.Add(MySqlHelper.MakeInParam("?UserIP", MySql.Data.MySqlClient.MySqlDbType.VarChar, 50, model.UserIP));
            param.Add(MySqlHelper.MakeInParam("?Action", MySql.Data.MySqlClient.MySqlDbType.VarChar, 50, model.Action));
            param.Add(MySqlHelper.MakeInParam("?Content", MySql.Data.MySqlClient.MySqlDbType.VarChar, 3000, model.Content));
            param.Add(MySqlHelper.MakeInParam("?Level", MySql.Data.MySqlClient.MySqlDbType.Int32, 0, model.Level));
            var result = _DBHelper.ExecuteNonQuery(CommandType.Text, sql, param.ToArray());
            return result > 0;
        }

        public List<ActionLogModel> GetList(int pageSize, int currentPage, out int totalCount)
        {
            string sql = "SELECT LogID, a.UserId, b.UserName as UserName, UserIP, `Action`, Content, LogTime, a.Level FROM actionlog as a inner join rightusers as b on a.UserId=b.UserId where a.Level >1 order by LogTime desc;";

            //var sdr = _DBHelper.ExecuteReader(CommandType.Text, sql, null);

            //var result = sdr.ReaderToList<ActionLogModel>();

            //sdr.Close();

            //return result;
            using (var sdr = _DBHelper.ExecuteReader(System.Data.CommandType.Text, MySqlHelper.GetDbPager(sql, pageSize, currentPage), null))
            {
                var result = sdr.ReaderToList<ActionLogModel>();

                totalCount = 0;

                if (result != null)
                {
                    if (sdr.NextResult() && sdr.Read())
                    {
                        totalCount = sdr.GetInt32(0);
                    }
                }

                sdr.Close();

                return result;
            }
        }

        public int Count()
        {
            string sql = "SELECT count(*) FROM actionlog;";

            var res = _DBHelper.ExecuteScalar(CommandType.Text, sql, null);

            var result = nwbase_utils.Tools.GetInt(res, 0);

            return result;
        }
    }
}