using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
//
using ServiceStack.Redis;
using MySql.Data.MySqlClient;
using AppStore.Model;
using AppStore.Common;

using nwbase_utils;
using nwbase_utils.Cache;

namespace AppStore.DAL.NewRedis
{
    public class NewRedisDAL : nwbase_utils.Cache.RedisHelper
    {
        /// <summary>
        /// Key命名的前缀
        /// </summary>
        string Prefix_Key = "apps:";
        string Api_Cache_Prefix_Key = "apps_api:";

        private RedisClient Redis { get; set; }

        private RedisTransaction trans { get; set; }

        public string ConnectionString { get { return Tools.GetConnStrConfig("ConnectionString"); } }

        public NewRedisDAL()
            : base(Tools.GetAppSetting("RedisHost", string.Empty), Tools.GetAppSetting("RedisPort", 0), Tools.GetAppSetting("RedisDB", 0))
        {
            this.Redis = _Client;
        }

        public void InitTran()
        {
            RedisClient redis = new RedisClient(
                Tools.GetAppSetting("RedisHost", string.Empty),
                Tools.GetAppSetting("RedisPort", 0),
                null,
                Tools.GetAppSetting("RedisDB", 0));
            this.trans = new RedisTransaction(redis);
        }

        /// <summary>
        /// 提交Redis事务
        /// </summary>
        /// <returns></returns>
        public bool SubmitTrans()
        {
            //判断事务是否存在
            if (this.trans == null)
            {
                return false;
            }
            //提交事务
            trans.Commit();
            trans.Dispose();
            return true;
        }

        /// <summary>
        /// # 方案下的分组id列表[集合]
        /// ～Name：sGroupScheme:[schemeid]
        /// ～Val： [groupids]
        /// </summary>
        /// <returns></returns>
        public bool sGroupScheme()
        {
            //查询语句
            string commandText = @"select gs.SchemeID,gs.GroupID 
                                        from GroupSchemes gs 
                                        inner join GroupInfo gi on gs.GroupID = gi.GroupID
                                            and gs.Status=1 
                                            and gi.Status=1";

            //从数据库中获取分组方案表数据，并添加到List集合中
            List<GroupSchemesEntity> sourceList = null;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<GroupSchemesEntity>() as List<GroupSchemesEntity>;
            }

            //处理数据结构,以便写入缓存时使用
            Dictionary<int, List<string>> dic = new Dictionary<int, List<string>>();
            foreach (GroupSchemesEntity item in sourceList)
            {
                //如果Key已存在，则添加Value到对应的Key中，否者添加Key + Value
                if (dic.ContainsKey(item.SchemeID))
                {
                    dic[item.SchemeID].Add(item.GroupID.ToString());
                }
                else
                {
                    dic.Add(item.SchemeID, new List<string>() { item.GroupID.ToString() });
                }
            }

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "sGroupScheme:*");

            //判断Redis事务
            if (this.trans != null)
            {
                // 从内存中获取已有的sGroupScheme集合，并清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //添加新缓存
                foreach (KeyValuePair<int, List<string>> pair in dic)
                {
                    //把分组方案添加至缓存中
                    trans.QueueCommand(s => s.AddRangeToSet(string.Format(Prefix_Key + "sGroupScheme:{0}", pair.Key), pair.Value));
                }

                //提交Redis事务
                //trans.Commit();

                //return this.Redis.SearchKeys(Prefix_Key + "sGroupScheme:*").Count() > 0;
                return true;
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// # 分组信息[哈希] 		
        /// ～Name：hGroupInfo:[groupid]
        /// ～Desc：分组id/分组类别/分组类型/排序类型/排序号/推荐语/分组名称/分组描述/分组图片url/开始时间/结束时间
        /// ～Key ：gid/gclass/gtype/odtype/odno/gtips/gname/gdesc/gpic/tpic/stime/etime/
        /// </summary>
        /// <returns></returns>
        public bool hGroupInfo()
        {
            #region CommandText

            string commandText = @"select g.GroupID, t.TypeClass, g.GroupTypeID, g.OrderType, g.OrderNo
		                                    , g.GroupTips, g.GroupName, g.GroupDesc, g.GroupPicUrl, g.StartTime, g.EndTime
                                            ,t.TypePicUrl
	                                    from groupinfo as g
	                                    inner join GroupTypes as t on g.GroupTypeID = t.TypeID
	                                    where g.Status = 1
	                                    order by g.OrderNo desc ";

            #endregion

            //从数据库中获取分组方案表数据，并添加到List集合中
            List<GroupInfoEntity> sourceList = null;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;
            }

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "hGroupInfo:*");
            //声明Redis事务
            if (this.trans != null)
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //添加新缓存
                foreach (GroupInfoEntity item in sourceList)
                {
                    Dictionary<string, string> dic = new Dictionary<string, string>();
                    //分组id
                    dic.Add("gid", item.GroupID.ToString());
                    //分组类别
                    dic.Add("gclass", item.TypeClass.ToString());
                    //分组类型
                    dic.Add("gtype", item.GroupTypeID.ToString());
                    //排序类型
                    dic.Add("odtype", item.OrderType.ToString());
                    //排序号
                    dic.Add("odno", item.OrderNo.ToString());
                    //推荐语
                    dic.Add("gtips", item.GroupTips.ToString());
                    //分组名称
                    dic.Add("gname", item.GroupName.ToString());
                    //分组描述
                    dic.Add("gdesc", item.GroupDesc.ToString());
                    //分组图片url
                    dic.Add("gpic", item.GroupPicUrl);
                    //开始时间
                    dic.Add("stime", item.StartTime.ToString("yyyyMMddHHmmss"));
                    //结束时间
                    dic.Add("etime", item.EndTime.ToString("yyyyMMddHHmmss"));
                    //类型图片Url
                    dic.Add("tpic", item.TypePicUrl);

                    //把dic写到内存中
                    trans.QueueCommand(s => s.SetRangeInHash(string.Format(Prefix_Key + "hGroupInfo:{0}", item.GroupID), dic));
                }

                return true;

            }
            else
            {
                return false;
            }
        }


        /// <summary>
        /// #  配置的分组元素的key,以posid*10000+orderNo为分值排序
        /// ～Name：ssGroupElemKey:[groupid]
        /// ～Val：[groupelemid]_[elemtype]_[elemid]
        /// </summary>
        /// <returns></returns>
        public bool ssGroupElemKey()
        {
            string datetime = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss");
            #region CommandText

            string commandText = @"SELECT GroupElemID,GroupID,ElemType,ElemID,StartTime,EndTime,PosID,OrderNo  
                                        FROM GroupElems 
                                        WHERE STATUS=1 and StartTime < '" + datetime + "' and  EndTime > '" + datetime + "' order by posid * 10000 desc, OrderNo desc;";

            #endregion

            //从数据库中获取分组元素表数据，并添加到List集合中
            List<GroupElemsEntity> sourceGroupElemsList = null;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceGroupElemsList = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "ssGroupElemKey:*");
            //声明Redis事务
            if (this.trans != null)
            {
                //从内存中获取已有的ssGroupElemKey集合，并清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //重新写入缓存
                int i = 0;
                foreach (GroupElemsEntity item in sourceGroupElemsList.OrderBy(t => t.PosID * 10000).ThenBy(t => t.OrderNo).ToList())
                {
                    trans.QueueCommand(s => s.AddItemToSortedSet(string.Format(Prefix_Key + "ssGroupElemKey:{0}", item.GroupID), string.Format("{0}_{1}_{2}_{3}", item.GroupElemID, item.ElemType, item.ElemID, item.PosID), i++));
                }

                return true;
            }
            else
            {
                return false;
            }
        }

        /// <summary>
        /// #  分组元素列表
        /// ～Name：hGroupElems:[groupid]_[groupelemid]_[elemtype]_[elemid]
        /// ～Desc：分组元素id/分组id/位置id/。。。。。（参考数据库）
        /// ～Key ：geid/gid/posid/etype/eid/stype/gtype/otype/rval/rtitle/rword/rpic/rmks/stime/etime/ctime/utime/
        /// </summary>
        /// <returns></returns>
        public bool HGroupElems()
        {
            #region CommandText

            string groupElemsSQL = @"SELECT 
	                                        a.GroupElemID,
	                                        a.GroupID,
	                                        a.PosID,
	                                        a.OrderNo,
	                                        a.ElemType,
	                                        a.RecommTitle,
	                                        a.RecommWord,
	                                        a.RecommVal,
	                                        a.RecommPicUrl,
	                                        a.ShowType,
	                                        a.GroupType,
	                                        a.OrderType,
	                                        a.ElemID,
	                                        a.StartTime,
	                                        a.EndTime,
	                                        a.Remarks,
	                                        a.CreateTime,
	                                        a.UpdateTime,
                                            a.RecommTag 
                                        FROM GroupElems AS a 
                                        WHERE a.Status = 1 
                                        ORDER BY a.OrderNo ASC;";

            #endregion

            //从数据库中读取分组元素列表
            List<GroupElemsEntity> groupElemsList = new List<GroupElemsEntity>();
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, groupElemsSQL))
            {
                //查询GroupElems中的数据，处理按更新时间排序的数据
                groupElemsList = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "hGroupElems:*_*");
            //声明Redis事务
            if (this.trans != null)
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }
                //添加新缓存
                foreach (GroupElemsEntity item in groupElemsList)
                {
                    Dictionary<string, string> dic = new Dictionary<string, string>();
                    //元素ID
                    dic.Add("geid", item.GroupElemID.ToString());
                    //分组ID
                    dic.Add("gid", item.GroupID.ToString());
                    //位置索引
                    dic.Add("posid", item.PosID.ToString());
                    //元素类型：1=App，2=Link，3=跳转至分类，4=跳转至网游或单机，5=跳转至专题，6=搜索词；（不支持跳转至推荐）
                    dic.Add("etype", item.ElemType.ToString());
                    //元素源ID，如应用（或Link、分类、专题）的ID
                    dic.Add("eid", item.ElemID.ToString());
                    //展示类型: 1=广告位...
                    dic.Add("stype", item.ShowType.ToString());
                    //跳转至分组类型
                    dic.Add("gtype", item.GroupType.ToString());
                    //跳转至分组的排序类型
                    dic.Add("otype", item.OrderType.ToString());
                    dic.Add("ono", item.OrderNo.ToString());
                    //推广权重
                    dic.Add("rval", item.RecommVal.ToString());
                    //推荐名称
                    dic.Add("rtitle", item.RecommTitle.ToString());
                    //推广语
                    dic.Add("rword", item.RecommWord.ToString());
                    //推广图片URL
                    dic.Add("rpic", item.RecommPicUrl.ToString());
                    //备注
                    dic.Add("rmks", item.Remarks.ToString());
                    //开始时间
                    dic.Add("stime", item.StartTime.ToString("yyyyMMddHHmmss"));
                    //结束时间
                    dic.Add("etime", item.EndTime.ToString("yyyyMMddHHmmss"));
                    //创建日期
                    dic.Add("ctime", item.CreateTime.ToString("yyyyMMddHHmmss"));
                    //更新日期
                    dic.Add("utime", item.UpdateTime.ToString("yyyyMMddHHmmss"));
                    // 推荐角标
                    dic.Add("rmtag", item.RecommTag.ToString());


                    //写入缓存
                    trans.QueueCommand(s => s.SetRangeInHash(string.Format(Prefix_Key + "hGroupElems:{0}_{1}_{2}_{3}_{4}", item.GroupID, item.GroupElemID, item.ElemType, item.ElemID, item.PosID), dic));
                }
                return true;
            }
            return false;

        }


        /// <summary>
        /// # 链接信息[哈希] 		
        /// ～Name：hLinkInfo:[linkid]
        /// ～Desc：
        /// ～Key ：lid/cid/dname/lname/sname/lurl/iurl/ctype/ltag/ldesc/rmks/ctime/utime/
        /// </summary>
        /// <returns></returns>
        public bool hLinkInfo()
        {
            #region CommandText

            string commandText = @"select LinkID, CPID, DevName, LinkName, ShowName, LinkUrl, IconUrl
	                                        , CoopType, LinkTag, LinkDesc, Remarks, CreateTime, UpdateTime
                                        from LinkInfo
                                        where Status = 1;";

            #endregion

            //从数据库中获取分组方案表数据，并添加到List集合中
            List<LinkInfoEntity> sourceList = null;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<LinkInfoEntity>() as List<LinkInfoEntity>;
            }

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "hLinkInfo:*");
            //声明Redis事务
            if (this.trans != null)
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //添加新缓存
                foreach (LinkInfoEntity item in sourceList)
                {
                    Dictionary<string, string> dic = new Dictionary<string, string>();
                    //外链ID
                    dic.Add("lid", item.LinkID.ToString());
                    //CPID
                    dic.Add("cid", item.CPID.ToString());
                    //开发者名称
                    dic.Add("dname", item.DevName.ToString());
                    //外链名称，运营内部叫法
                    dic.Add("lname", item.LinkName.ToString());
                    //显示名称，显示给用户
                    dic.Add("sname", item.ShowName.ToString());
                    //外链URL
                    dic.Add("lurl", item.LinkUrl.ToString());
                    //ICON URL
                    dic.Add("iurl", item.IconUrl.ToString());
                    //合作类型，例如：1=CPC，2=CPL...
                    dic.Add("ctype", item.CoopType.ToString());
                    //外链标签，例如：1=独家，2=首发，4=...位运算
                    dic.Add("ltag", item.LinkTag.ToString());
                    //外链描述
                    dic.Add("ldesc", item.LinkDesc.ToString());
                    //备注
                    dic.Add("rmks", item.Remarks.ToString());
                    //创建时间
                    dic.Add("ctime", item.CreateTime.ToString("yyyyMMddHHmmss"));
                    //更新时间
                    dic.Add("utime", item.UpdateTime.ToString("yyyyMMddHHmmss"));

                    //把dic写到内存中
                    trans.QueueCommand(s => s.SetRangeInHash(string.Format(Prefix_Key + "hLinkInfo:{0}", item.LinkID), dic));
                }

                return true;
            }
            return false;
        }

        /// <summary>
        /// 最新应用版本[哈希]
        ///～Name：hNewestAppVer:[packname]
        ///～Name：hNewestAppVer:[packname]_[md5(signcode)]
        /// 撤除方法
        /// </summary>
        /// <returns></returns>
        public bool HNewestAppVer()
        {
            string commandText = @"
                                select 
                                    a.AppID,MainPackID,a.PackName,MainVerName,MainVerCode,AppType div 100 as AppClass,a.PackSign
                                from
                                    appinfo a
                                        inner join
                                    packinfo p ON a.AppID = p.AppID
                                        and a.MainPackID = p.PackID
                                        and a.DataStatus = 1
                                        and a.Status = 1
                                        and p.Status = 1
                                ";

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "hNewestAppVer:*");
            List<AppInfoEntity> sourceList = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }

            if (this.trans != null)
            {
                foreach (string key in keyList)
                {
                    trans.QueueCommand(s => s.Remove(key));
                }

                string emptyMd5 = SecurityExtension.MD5(string.Empty).ToLower();
                foreach (AppInfoEntity item in sourceList)
                {
                    Dictionary<string, string> dic = new Dictionary<string, string>();

                    dic.Add("appid", item.AppID.ToString());
                    dic.Add("packid", item.PackID.ToString());
                    dic.Add("vname", item.MainVerName);
                    dic.Add("vcode", item.MainVerCode.ToString());
                    dic.Add("aclass", item.AppClass.ToString());
                    dic.Add("pname", item.PackName);

                    // remarks: 暂时不写空的signcode
                    //if (!string.IsNullOrEmpty(item.PackSign.Trim()))
                    //{
                    //    trans.QueueCommand(s => s.SetRangeInHash(string.Format(Prefix_Key + "hNewestAppVer:{0}_{1}", item.PackName, emptyMd5), dic));
                    //}
                    trans.QueueCommand(s => s.SetRangeInHash(string.Format(Prefix_Key + "hNewestAppVer:{0}_{1}", item.PackName, SecurityExtension.MD5(item.PackSign.Trim()).ToLower()), dic));
                }
                return true;

                //trans.Commit();
            }
            return false;
            //return this.Redis.SearchKeys("hNewestAppVer:*").Count > 0;

        }

        /// <summary>
        /// 最新应用的版本[哈希]
        /// 增量生成方法
        /// </summary>
        /// <returns></returns>
        public bool HNewestAppVer2()
        {
            // 得到上个数据版本
            string appVerKey = Prefix_Key + "BGLastNewestAppVer";
            long appVer = this.Redis.Get<long>(appVerKey);

            string commandText = string.Format(@"
                      select 
                        a.AppID,
                        MainPackID,
                        a.PackName,
                        MainVerName,
                        MainVerCode,
                        AppType div 100 as AppClass,
                        a.PackSign,
                        a.OpUpdateTime,
	                    a.DataStatus,
	                    if(p.PackID is null,0,1) ExistFlag
                    from
                        appinfo a
                            left join
                        packinfo p ON a.AppID = p.AppID
                            and a.MainPackID = p.PackID
                            and a.DataStatus = 1
                            and a.Status = 1
                            and p.Status = 1
                    {0}
                    -- order by a.OpUpdateTime desc
                    ", appVer <= 0 ? "" :
                     string.Format("where a.OpUpdateTime > '{0}'", ConvertHelper.ToDateTime(appVer, "yyyyMMddHHmmss")));

            List<AppInfoEntity> sourceList = null;
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }

            if (this.trans != null && sourceList != null && sourceList.Count > 0)
            {
                string newAppVer = sourceList.Max(t => t.OpUpdateTime).ToString("yyyyMMddHHmmss");

                string emptyMd5 = SecurityExtension.MD5(string.Empty).ToLower();
                foreach (AppInfoEntity item in sourceList)
                {
                    // 带packsign
                    string key = string.Format(Prefix_Key + "hNewestAppVer:{0}_{1}", item.PackName, SecurityExtension.MD5(item.PackSign.Trim()).ToLower());

                    // 空packsign
                    string key2 = string.Format(Prefix_Key + "hNewestAppVer:{0}_{1}", item.PackName, emptyMd5);

                    if (item.DataStatus != 1 || item.ExistFlag != 1)
                    {
                        trans.QueueCommand(s => s.Remove(key));

                        if (key != key2)
                        {
                            trans.QueueCommand(s => s.Remove(key2));
                        }
                    }
                    else
                    {
                        Dictionary<string, string> dic = new Dictionary<string, string>();
                        dic.Add("appid", item.AppID.ToString());
                        dic.Add("packid", item.PackID.ToString());
                        dic.Add("vname", item.MainVerName);
                        dic.Add("vcode", item.MainVerCode.ToString());
                        dic.Add("aclass", item.AppClass.ToString());
                        dic.Add("pname", item.PackName);

                        trans.QueueCommand(s => s.SetRangeInHash(key, dic));

                        if (key != key2)
                        {
                            trans.QueueCommand(s => s.SetRangeInHash(key2, dic));
                        }
                    }
                }
                trans.QueueCommand(s => s.Set<long>(appVerKey, ConvertHelper.ToInt64(newAppVer)));
            }
            return true;
        }

        /// <summary>
        /// 当前版本分发渠道汇总列表
        /// </summary>
        /// <returns></returns>
        public bool lChnnoList()
        {
            HashSet<string> channelNoSet = new HashSet<string>();
            string commandText = "select ChannelNos from appinfo where Status=1 and IssueType=2";

            List<string> keyList = this.Redis.SearchKeys(Prefix_Key + "lChnnoList");

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                var channelNoList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
                if (channelNoList != null)
                {
                    foreach (var appinfo in channelNoList)
                    {
                        if (!string.IsNullOrEmpty(appinfo.ChannelNos))
                        {
                            appinfo.ChannelNos.Trim(',').Split(',').ToList().ForEach(
                                delegate(string t)
                                {
                                    if (!channelNoSet.Contains(t))
                                    {
                                        channelNoSet.Add(t);
                                    }
                                });
                        }
                    }
                }
            }
            if (this.trans != null && channelNoSet != null && channelNoSet.Count > 0)
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                string keyName = string.Format(Prefix_Key + "lChnnoList");

                foreach (var str in channelNoSet)
                {
                    trans.QueueCommand(s => s.AddItemToList(keyName, str));
                }
            }
            return true;
        }

        /// <summary>
        /// 清除前台接口生成的缓存
        /// </summary>
        /// <returns></returns>
        public bool ClearApiCache()
        {
            List<string> keyList = this.Redis.SearchKeys(Api_Cache_Prefix_Key + "*");
            if (this.trans != null)
            {
                foreach (string key in keyList)
                {
                    trans.QueueCommand(s => s.Remove(key));
                }
                return true;
            }
            return false;

        }

    }
}
