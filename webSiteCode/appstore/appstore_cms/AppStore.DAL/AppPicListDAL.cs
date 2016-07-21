using MySql.Data.MySqlClient;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using AppStore.Common;

namespace AppStore.DAL
{
    public class AppPicListDAL : BaseDAL
    {
        #region 封装参数

        private List<MySqlParameter> GetMySqlParameters(AppPicListEntity entity)
        {
            List<MySqlParameter> paramsList = new List<MySqlParameter>();
            paramsList.Add(new MySqlParameter("@AppPicID", entity.AppPicID));
            paramsList.Add(new MySqlParameter("@AppID", entity.AppID));
            paramsList.Add(new MySqlParameter("@PackID", entity.PackID));
            paramsList.Add(new MySqlParameter("@OrderNo", entity.OrderNo));
            paramsList.Add(new MySqlParameter("@PicUrl", entity.PicUrl));
            paramsList.Add(new MySqlParameter("@OldPicUrl", entity.OldPicUrl));

            return paramsList;
        }

        #endregion

        #region 执行非查询操作

        private bool ExecuteNonQuery(string commandText, AppPicListEntity entity)
        {
            List<MySqlParameter> paramsList = GetMySqlParameters(entity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return base.ExecuteStatus(result);
        }


        #endregion


        public bool Insert(AppPicListEntity entity)
        {
            #region CommandText

            string commandText = @"INSERT INTO AppPicList (
                                                            AppID,
                                                            PackID,
                                                            OrderNo,
                                                            PicUrl
                                                        ) 
                                                        VALUES
                                                            (
                                                                @AppID,
                                                                @PackID,
                                                                @OrderNo,
                                                                @PicUrl
                                                            ) ;";

            #endregion

            return this.ExecuteNonQuery(commandText, entity);
        }

        public bool Update(AppPicListEntity entity)
        {
            #region CommandText

            string commandText = @"UPDATE 
                                        AppPicList 
                                        SET
                                        OrderNo = @OrderNo,
                                        PicUrl = @PicUrl 
                                        WHERE PicUrl = @OldPicUrl ;";

            #endregion

            return this.ExecuteNonQuery(commandText, entity);
        }

        public List<AppPicListEntity> GetDataList(int packID)
        {
            string commandText = @"SELECT AppPicID,AppID,PackID,OrderNo,PicUrl FROM  AppPicList WHERE  PackID=@PackID Order By OrderNo;";

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText, new MySqlParameter("@PackID", packID)))
            {
                return objReader.ReaderToList<AppPicListEntity>() as List<AppPicListEntity>;
            }
        }

        public bool DeleteByPackID(int packID)
        {
            string commandText = @"DELETE FROM AppPicList WHERE  PackID=@PackID;";

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, new MySqlParameter("@PackID", packID));

            return this.ExecuteStatus(result);
        }

        public bool DeleteByIDs(string IDs)
        {
            string[] apIDs = IDs.Split(',');
            IDs = string.Empty;

            for (int i = 0; i < apIDs.Length; i++)
            {
                IDs += string.Format("'{0}',", apIDs[i]);
            }

            string commandText = string.Format("DELETE FROM AppPicList WHERE AppPicID IN ({0})", IDs.TrimEnd(','));


            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText);

            return ExecuteStatus(result);
        }

        public bool UpdataByID(AppPicListEntity currentEntity)
        {
            #region CommandText

            string commandText = @"UPDATE AppPicList
                                    SET
                                      PicUrl = @PicUrl,
                                      AppID=@AppID,
                                      PackID=@PackID,
                                      OrderNo=@OrderNo
                                    WHERE AppPicID = @AppPicID ";

            #endregion

            List<MySqlParameter> paramsList = this.GetMySqlParameters(currentEntity);

            int result = MySqlHelper.ExecuteNonQuery(this.ConnectionString, commandText, paramsList.ToArray());

            return this.ExecuteStatus(result);
        }
    }
}
