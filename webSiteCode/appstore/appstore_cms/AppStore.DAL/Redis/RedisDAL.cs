using System;
using System.Collections.Generic;
using System.Linq;
using ServiceStack.Redis;
using MySql.Data.MySqlClient;
using AppStore.Model;
using AppStore.Common;
using nwbase_utils;
using nwbase_utils.Cache;

namespace AppStore.DAL.Redis
{
    public class RedisDAL : nwbase_utils.Cache.RedisHelper
    {
        # region 全局的私有变量

        private RedisClient Redis { get; set; }

        /// <summary>
        /// 不需要配置元素id的类型
        /// </summary>
        private List<int> elemTypeListWithoutElemid = new List<int>() { 2, 4, 6 };

        /// <summary>
        /// 需要按时间排序的分组id列表
        /// </summary>
        private int[] orderByTimeArray = new GroupInfoDAL().GetorderByTimeArray().Select(s => s.GroupID).ToArray();

        /// <summary>
        /// 分组元素长度的最大值
        /// </summary>
        private int elemCountInGroup = 500;

        /// <summary>
        /// 分组应用分组名,用于显示推荐语
        /// </summary>
        public Dictionary<int, string> GroupIDTypeNameDic = new Dictionary<int, string>();

        /// <summary>
        /// 应用id分组名，用于显示推荐语
        /// </summary>
        public Dictionary<int, string> AppIDTypeNameDic = new Dictionary<int, string>();

        public string ConnectionString { get { return Tools.GetConnStrConfig("ConnectionString"); } }

        #endregion

        public RedisDAL()
            : base(Tools.GetAppSetting("RedisHost", string.Empty), Tools.GetAppSetting("RedisPort", 0), Tools.GetAppSetting("RedisDB", 0))
        {
            this.Redis = _Client;
        }

        /// <summary>
        /// # 方案下的分组id列表[集合]
        ///～Name：sAppScheme:[schemeid]
        ///～Desc：分组id列表
        ///～Val： [groupids]
        /// </summary>
        /// <returns></returns>
        public bool SAppScheme()
        {
            string commandText = @"SELECT SchemeID,gs.GroupID FROM GroupSchemes gs inner join
                                    GroupInfo gi where gs.status=1 and gi.status=1";

            commandText = @"select gs.SchemeID,gs.GroupID from GroupSchemes gs inner join
                            GroupInfo gi on
                            gs.GroupID = gi.GroupID
                            and gs.Status=1 
                            and gi.Status=1";

            List<string> keyList = this.Redis.SearchKeys("sAppScheme:*");
            List<string> cacheList = this.Redis.GetHashKeys("hGroupsCacheVer");
            List<GroupSchemesEntity> sourceList = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<GroupSchemesEntity>() as List<GroupSchemesEntity>;
            }
            //处理数据结构
            Dictionary<int, List<string>> dic = new Dictionary<int, List<string>>();
            //Dictionary<string, string> cacheDic = new Dictionary<string, string>();

            foreach (GroupSchemesEntity item in sourceList)
            {
                if (dic.ContainsKey(item.SchemeID))
                {
                    //相同的Key只添加Value
                    dic[item.SchemeID].Add(item.GroupID.ToString());
                }
                else
                {
                    dic.Add(item.SchemeID, new List<string>() { item.GroupID.ToString() });
                }

                //if (!cacheDic.ContainsKey(item.SchemeID.ToString()))
                //{
                //    cacheDic.Add(item.SchemeID.ToString(), item.GroupID.ToString());
                //}
            }

            //声明Redis事务
            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //清除缓存版本
                foreach (string currentKey in cacheList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                foreach (KeyValuePair<int, List<string>> pair in dic)
                {
                    //添加新缓存
                    trans.QueueCommand(s => s.AddRangeToSet(string.Format("sAppScheme:{0}", pair.Key), pair.Value));

                    //添加缓存版本
                    trans.QueueCommand(s => s.SetEntryInHash("hGroupsCacheVer", string.Format("groups_{0}", pair.Key.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
                }


                //提交Redis事务
                trans.Commit();


                return this.Redis.SearchKeys("sAppScheme:*").Count() > 0;
            }
        }

        /// <summary>
        /// # 分组信息[哈希]
        ///～Name：hGroups:[groupid]
        /// </summary>
        /// <returns></returns>
        public bool HGroups()
        {
            #region CommandText

            string commandText = @"SELECT 
                                    GroupID,
                                    (SELECT 
                                        COUNT(0) 
                                    FROM
                                       AppInfo
                                    WHERE AppType = GroupTypeID) 
                                    + (select count(0) from GroupElems e where e.status=1 and e.groupid=GroupInfo.GroupID)
                                    AS 'AppCount',
                                    (SELECT 
                                        CONCAT_WS(
                                            '  ',
                                            (SELECT 
                                                a.ShowName 
                                            FROM
                                                AppInfo AS a 
                                             
                                            WHERE a.AppType = GroupTypeID 
                                                AND a.STATUS = 1 
                                            ORDER BY a.DownTimes DESC 
                                            LIMIT 0, 1),
                                            (SELECT 
                                                a.ShowName 
                                            FROM
                                                AppInfo AS a 
                                              WHERE a.AppType = GroupTypeID 
                                                AND a.STATUS = 1 
                                            ORDER BY a.DownTimes DESC 
                                            LIMIT 1, 1),
                                            (SELECT 
                                                a.ShowName 
                                            FROM
                                                AppInfo AS a 
                                              WHERE a.AppType = GroupTypeID 
                                                AND a.STATUS = 1 
                                            ORDER BY a.DownTimes DESC 
                                            LIMIT 2, 1)
                                        )) AS 'TopAppNames',
                                    TypeClass,
                                    GroupTypeID,
                                    OrderType,
                                    GroupInfo.OrderNo,
                                    GroupTips,
                                    GroupName,
                                    TypeName,
                                    GroupDesc,
                                    TypeDesc,
                                    (SELECT 
                                        MainIconUrl 
                                    FROM
                                        AppInfo,
                                        PackInfo
                                    WHERE  AppType = GroupTypeID 
                                        AND AppInfo.MainPackID = PackInfo.PackID
                                    ORDER BY AppInfo.UpdateTime DESC 
                                    LIMIT 0, 1) AS 'MainIconUrl',
                                    GroupPicUrl,
                                    TypePicUrl,
                                    StartTime,
                                    EndTime 
                                FROM
                                    GroupInfo,
                                    GroupTypes
                                WHERE GroupTypeID = TypeID 
                                    AND GroupInfo.Status = 1 ORDER BY OrderNo DESC";

            #endregion

            List<string> keyList = this.Redis.SearchKeys("hGroups:*");
            List<GroupInfoEntity> sourceList = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;
            }

            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                foreach (GroupInfoEntity item in sourceList)
                {
                    Dictionary<string, string> dic = new Dictionary<string, string>();

                    dic.Add("gid", item.GroupID.ToString());
                    dic.Add("gclass", item.TypeClass.ToString());
                    dic.Add("gtype", item.GroupTypeID.ToString());
                    dic.Add("odtype", item.OrderType.ToString());
                    dic.Add("odno", item.OrderNo.ToString());

                    if (string.IsNullOrEmpty(item.GroupTips))
                    {
                        dic.Add("rmword", string.Format("共{0}款", item.AppCount));
                    }
                    else
                    {
                        dic.Add("rmword", item.GroupTips.ToString());
                    }


                    if (string.IsNullOrEmpty(item.GroupName))
                    {
                        dic.Add("gname", item.TypeName.ToString());
                    }
                    else
                    {
                        dic.Add("gname", item.GroupName.ToString());
                    }

                    if (item.GroupTypeID >= 1101 && item.GroupTypeID <= 1300)
                    {
                        dic.Add("gdesc", item.TopAppNames);

                    }
                    else
                    {
                        if (string.IsNullOrEmpty(item.GroupDesc))
                        {
                            dic.Add("gdesc", item.TypeDesc.ToString());
                        }
                        else
                        {
                            dic.Add("gdesc", item.GroupDesc.ToString());
                        }
                    }


                    if (string.IsNullOrEmpty(item.GroupPicUrl))
                    {
                        if (string.IsNullOrEmpty(item.MainIconPicUrl))
                        {
                            dic.Add("gpic", item.TypePicUrl.ToString());
                        }
                        else
                        {
                            dic.Add("gpic", item.MainIconPicUrl.ToString());
                        }
                    }
                    else
                    {
                        dic.Add("gpic", item.GroupPicUrl);
                    }

                    dic.Add("stime", item.StartTime.ToString("yyyyMMddHHmmss"));
                    dic.Add("etime", item.EndTime.ToString("yyyyMMddHHmmss"));

                    trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroups:{0}", item.GroupID), dic));
                }

                trans.Commit();

                return this.Redis.SearchKeys("hGroups:*").Count > 0;

            }
        }

        /// <summary>
        /// # 方案分组信息[集合]  -- 目前用于分发接口匹配分组id使用
        ///～Name：sSchemeGroups:[schemeid]_[groupclass]_[grouptype]_[ordertype]
        ///～Desc：分组id列表
        ///～Key ：[groupids]
        /// </summary>
        /// <returns></returns>
        public bool SSchemeGroups()
        {
            #region commandText

            string commandText = @"SELECT 
                                        SchemeID,
                                        GroupID,
                                        TypeClass,
                                        GroupTypeID,
                                        OrderType 
                                    FROM
                                        GroupSchemes,
                                        GroupTypes 
                                    WHERE GroupTypes.TypeID = GroupSchemes.GroupTypeID 
                                        AND GroupSchemes.Status = 1 
                                        AND GroupTypes.Status = 1";

            #endregion

            List<string> keyList = this.Redis.SearchKeys("sSchemeGroups:*_*_*_*");
            List<string> cacheList = this.Redis.GetHashKeys("hGroupsCacheVer");
            List<GroupSchemesEntity> sourceList = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<GroupSchemesEntity>() as List<GroupSchemesEntity>;
            }



            //声明Redis事务
            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                //清除缓存版本
                foreach (string currentKey in cacheList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                foreach (var item in sourceList)
                {
                    //添加新缓存
                    trans.QueueCommand(s => s.AddItemToSet(string.Format("sSchemeGroups:{0}_{1}_{2}_{3}", item.SchemeID, item.TypeClass, item.GroupTypeID, item.OrderType), item.GroupID.ToString()));

                    //添加缓存版本
                    trans.QueueCommand(s => s.SetEntryInHash("hGroupsCacheVer", string.Format("groups_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
                }

                //提交Redis事务
                trans.Commit();

                return this.Redis.SearchKeys("sSchemeGroups:*_*_*_*").Count() > 0;
            }

        }

        /// <summary>
        /// 应用信息 hAppInfo:[appid]
        /// </summary>
        /// <returns></returns>
        public bool HAppInfo()
        {
            #region CommandText

            string commandText = @"SELECT 
                                        a.AppID,
                                        a.MainPackID,
                                        a.ShowName,
                                        a.PackName,
                                        a.PackSign,
                                        c.CPName,
                                        a.DevName,
                                        a.AppType,
                                        t.AppClass,
                                        a.DownTimesReal,
                                        a.CommentTimes,
                                        a.RecommLevel,
                                        a.AppTag,
                                        a.RecommTag,
                                        a.RecommWord,
                                        a.MainIconUrl,
                                        b.PackUrl,
                                        b.PackUrl2,
                                        a.SearchKeys,
                                        b.PackMD5,
                                        a.MainPackSize,
                                        a.MainVerCode,
                                        a.MainVerName,
                                        b.CompDesc,
                                        a.AppDesc,
                                        b.UpdateDesc,
                                        a.CreateTime,
                                        a.UpdateTime,
                                        b.AppPicUrl as PicUrl
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN PackInfo AS b 
                                            ON a.AppID = b.AppID 
                                            LEFT JOIN CPs AS c
                                            ON a.CPID = c.CPID
                                            left join AppTypes as t
                                            on a.AppType = t.AppType
                                    WHERE a.STATUS = 1 
                                        AND b.IsMainVer = 1 and b.Status=1";

            //暂无此表 2014-11-1 momo
            string picUrlCommandText = @"select ap.* from AppPicList as ap
                                            inner join AppInfo as ai
                                            on ap.appid=ai.appid 
                                            and ap.packid = ai.mainpackid";

            string apptypeCommandText = "select AppID, AppType from appinfo; ";

            #endregion

            #region 数据处理及优化
            List<string> keyList = this.Redis.SearchKeys("hAppInfo:*");
            List<string> cacheList = this.Redis.SearchKeys("hAppInfoCacheVer");
            List<AppInfoEntity> sourceList = null;
            Dictionary<string, string> apppicDic = new Dictionary<string, string>();


            //取应用信息
            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }
            #endregion


            //声明Redis事务
            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                //清除原缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }

                foreach (string currentKey in cacheList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }



                //添加新缓存
                foreach (AppInfoEntity item in sourceList)
                {
                    try
                    {

                        Dictionary<string, string> dic = new Dictionary<string, string>();

                        dic.Add("appid", item.AppID.ToString());
                        dic.Add("packid", item.MainPackID.ToString());
                        dic.Add("soname", item.ShowName);
                        dic.Add("pkname", item.PackName);
                        dic.Add("sgcode", item.PackSign);
                        dic.Add("devname", string.IsNullOrEmpty(item.DevName) ? "" : item.DevName);
                        dic.Add("aclass", item.AppClass.ToString());
                        dic.Add("atype", item.AppType.ToString());
                        dic.Add("dwtimes", this.GetDownlTimesChinese(item.DownTimesReal));
                        dic.Add("cmtimes", item.CommentTimes.ToString());
                        dic.Add("cmscore", item.CommentScore.ToString());
                        dic.Add("apptag", item.AppTag.ToString());
                        dic.Add("rmlevel", item.RecommLevel.ToString());
                        dic.Add("rmflag", item.RecommTag.ToString());
                        dic.Add("rmword", item.RecommWord);
                        dic.Add("tmurl", string.Empty);
                        dic.Add("icurl", item.MainIconUrl);
                        dic.Add("picurl", item.PicUrl);
                        //dic.Add("picurl", ConvertHelper.ToString(item.PicUrl).Replace(ConfigHelper.AppSettings<string>("OldPicUrl"), ConfigHelper.AppSettings<string>("NewPicUrl")));
                        dic.Add("pkurl", item.PackUrl);
                        dic.Add("pkurl2", item.PackUrl2);
                        dic.Add("shkeys", item.SearchKeys);
                        dic.Add("ptmd5", string.IsNullOrEmpty(item.PartPackMD5) ? "" : item.PartPackMD5);
                        dic.Add("pkmd5", item.PackMD5);
                        dic.Add("pksize", item.MainPackSize.ToString());
                        dic.Add("vcode", item.MainVerCode.ToString());
                        dic.Add("vname", item.MainVerName);
                        dic.Add("cmdesc", item.CompDesc);
                        dic.Add("landesc", "");
                        dic.Add("appdesc", item.AppDesc);
                        dic.Add("upddesc", item.UpdateDesc);
                        dic.Add("pbltime", item.UpdateTime.ToString("yyyyMMddHHmmss"));

                        Dictionary<string, string> cacheDic = new Dictionary<string, string>();
                        cacheDic.Add(string.Format("appinfo_{0}", item.AppID), item.UpdateTime.ToString("yyyyMMddHHmmss"));

                        trans.QueueCommand(s => s.SetRangeInHash(string.Format("hAppInfo:{0}", item.AppID), dic));
                        trans.QueueCommand(s => s.SetRangeInHash("hAppInfoCacheVer", cacheDic));
                    }
                    catch (Exception e)
                    {
                        LogHelper.Default.Info(item.AppID.ToString());
                    }
                }

                //提交Redis事务
                trans.Commit();

                return this.Redis.SearchKeys("hAppInfo:*").Count() > 0;
            }
        }

        /// <summary>
        /// 最新应用版本[哈希]
        ///～Name：hNewestAppVer:[packname]
        /// </summary>
        /// <returns></returns>
        public bool HNewestAppVer()
        {
            string commandText = @"SELECT AppID,MainPackID,PackName,MainVerName,MainVerCode,AppType div 100 as AppClass,PackSign FROM AppInfo WHERE STATUS=1 and packname != ''";

            List<string> keyList = this.Redis.SearchKeys("hNewestAppVer:*");
            List<AppInfoEntity> sourceList = null;

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }

            using (RedisTransaction trans = new RedisTransaction(this.Redis))
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
                    dic.Add("vername", item.MainVerName);
                    dic.Add("vercode", item.MainVerCode.ToString());
                    dic.Add("aclass", item.AppClass.ToString());
                    dic.Add("pname", item.PackName);

                    if (!string.IsNullOrEmpty(item.PackSign.Trim()))
                    {
                        trans.QueueCommand(s => s.SetRangeInHash(string.Format("hNewestAppVer:{0}_{1}", item.PackName, emptyMd5), dic));
                    }
                    trans.QueueCommand(s => s.SetRangeInHash(string.Format("hNewestAppVer:{0}_{1}", item.PackName, SecurityExtension.MD5(item.PackSign.Trim()).ToLower()), dic));
                }

                trans.Commit();
            }

            return this.Redis.SearchKeys("hNewestAppVer:*").Count > 0;

        }

        /// <summary>
        /// # 分组元素[有序集合]
        ///～Name：ssGroups:[groupid]
        /// </summary>
        /// <returns></returns>
        public bool SsGroups()
        {

            #region CommandText

            string commandText = @"SELECT GroupElemID,GroupID,ElemType,ElemID,StartTime,EndTime,PosID,OrderNo  FROM GroupElems WHERE STATUS=1 order by posid desc,orderNo desc";

            string commandText2 = @"(SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                        c.GroupID,
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                    ORDER BY a.DownTimes DESC) 
                                    UNION
                                    (SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                        (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1200 AND OrderType=0),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                        AND a.AppType div 100 = 12
                                    ORDER BY a.DownTimes DESC) 
                                    UNION
                                    (SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                          (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1100 AND OrderType=0),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                        AND a.AppType div 100 = 11 
                                    ORDER BY a.DownTimes DESC) 
                                    UNION
                                    (SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                        (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1200 AND OrderType=2),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                        AND a.AppType div 100 = 12 
                                    ORDER BY a.UpdateTime DESC) 
                                    UNION
                                    (SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                         (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1100 AND OrderType=2),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                        AND a.AppType div 100 = 11 
                                    ORDER BY a.UpdateTime DESC)
                                    UNION
                                    (
                                    SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                        (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1000 AND OrderType=0),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                      
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                    ORDER BY a.DownTimes DESC
                                    )
                                    UNION
                                    (
                                    SELECT 
                                        a.AppID,
                                        a.AppType div 100 as AppClass,
                                        a.AppType,
                                        (SELECT GroupID FROM GroupInfo WHERE GroupTypeID=1000 AND OrderType=2),
                                        a.DownTimes,
                                        a.UpdateTime 
                                    FROM
                                        AppInfo AS a 
                                     
                                        INNER JOIN GroupInfo AS c 
                                            ON a.AppType = c.GroupTypeID 
                                    WHERE a.Status = 1 
                                    ORDER BY a.UpdateTime DESC
                                    )";


            #endregion

            List<GroupElemsEntity> sourceGroupElemsList = null;
            List<AppInfoEntity> sourceAppInfoList = null;
            List<string> keyList = this.Redis.SearchKeys("ssGroups:*");
            List<string> cacheList = this.Redis.SearchKeys("hGroupElemsCacheVer");

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText))
            {
                sourceGroupElemsList = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;
            }

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, commandText2))
            {
                sourceAppInfoList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
            }

            #region  各分组数据处理

            // -----  过期处理  -----
            // 对于闪屏(4105)，是否过期由客户端去判断
            // 对于其他分组，服务端判断，过期不显示
            DateTime nowTime = DateTime.Now;
            sourceGroupElemsList.RemoveAll(s => s.GroupType != 4105 && (s.StartTime > nowTime || s.EndTime < nowTime));


            List<AppInfoEntity> tmpList = null;
            List<AppInfoEntity> appinfoList = new List<AppInfoEntity>();
            Dictionary<int, List<AppInfoEntity>> eachGroupAppList = new Dictionary<int, List<AppInfoEntity>>();

            // 重新整理分组
            foreach (AppInfoEntity item in sourceAppInfoList)
            {


                if (!eachGroupAppList.ContainsKey(item.GroupID))
                {
                    eachGroupAppList.Add(item.GroupID, new List<AppInfoEntity>() { item });
                }
                else
                {
                    eachGroupAppList[item.GroupID].Add(item);
                }
            }

            // 分组内容排序及截取长度
            foreach (var item in eachGroupAppList)
            {
     
                // 分组排序处理
                if (orderByTimeArray.Contains(item.Key))
                {
                    tmpList = item.Value.OrderByDescending(s => s.UpdateTime).ToList();
                }
                else
                {
                    tmpList = item.Value.OrderByDescending(s => s.DownTimesReal).ToList();
                }

                // 截取分组的长度
                if (tmpList.Count > elemCountInGroup)
                {
                    tmpList.RemoveRange(elemCountInGroup, item.Value.Count - elemCountInGroup);
                }

                appinfoList.AddRange(tmpList);
            }

            // 删除重复元素
            foreach (var item in sourceGroupElemsList)
            {
                if (item.ElemType == 1)
                {
                    AppInfoEntity appinfo = appinfoList.Where(t => t.GroupID == item.GroupID && t.AppID == item.ElemID).FirstOrDefault();
                    if (appinfo != null)
                    {
                        appinfoList.Remove(appinfo);
                    }
                }
            }

            sourceAppInfoList = appinfoList;

            #endregion

            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }
                int i = 0;

                // 根据i的大小顺序排序

                // 非配置的应用信息
                i = sourceGroupElemsList.Count;
                foreach (AppInfoEntity item in sourceAppInfoList)
                {
                    trans.QueueCommand(s => s.AddItemToSortedSet(string.Format("ssGroups:{0}", item.GroupID), string.Format("{0}_{1}_{2}", 0, 1, item.AppID), i++));

                    trans.QueueCommand(s => s.SetEntryInHash("hGroupElemsCacheVer", string.Format("groupelems_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
                }


                // 配置优先显示的应用信息
                //i = sourceList2.Count;
                i = 0;
                foreach (GroupElemsEntity item in sourceGroupElemsList.OrderBy(t => t.PosID).ThenBy(t => t.OrderNo).ToList())
                {
                    trans.QueueCommand(s => s.AddItemToSortedSet(string.Format("ssGroups:{0}", item.GroupID), string.Format("{0}_{1}_{2}", item.GroupElemID, item.ElemType, item.ElemID), i++));
                    trans.QueueCommand(s => s.SetEntryInHash("hGroupElemsCacheVer", string.Format("groupelems_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
                }

                trans.Commit();
            }


            return this.Redis.SearchKeys("ssGroups:*").Count > 0;
        }

        /// <summary>
        /// # 分组元素列表[哈希]
        ///～Name：hGroupElems:[groupelemid]_[elemtype]_[elemids]
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
                                        IFNULL(b.ShowName, a.RecommTitle) AS ShowName,
                                        IFNULL(b.MainPackSize, 0) AS 'MainPackSize',
                                        a.RecommWord AS 'RecommWord',
                                        IFNULL(b.DownTimesReal, 0) AS 'DownTimesReal',
                                        IFNULL(RecommTag, 0) AS 'RecommTag',
                                        IFNULL(b.RecommLevel, 0) AS 'RecommLevel',
                                        IFNULL(b.MainIconUrl, '') AS 'MainIconUrl',
                                        -- IFNULL(b.ThumbPicUrl, '') AS 'ThumbPicUrl',
                                        a.RecommPicUrl,
                                        a.CreateTime,
                                        a.ShowType,
                                        IFNULL(b.Appid, 0) AS 'AppID',
                                        IFNULL(b.PackName, '') AS 'PackName',
                                        IFNULL(b.MainPackID, 0) AS 'MainPackID',
                                        b.PackSign,
                                        b.MainVerCode,
                                        b.MainVerName,
                                        c.LinkID,
                                        c.LinkUrl,
                                        a.GroupType,
                                        a.OrderType,
                                        a.ElemID,
                                        a.UpdateTime 
                                    FROM
                                        GroupElems AS a 
                                        LEFT JOIN AppInfo AS b 
                                            ON a.ElemID = b.AppID 
                                        LEFT JOIN LinkInfo AS c 
                                            ON a.ElemID = c.LinkID 
                                    WHERE a.Status = 1 
                                    ORDER BY a.PosID ASC,
                                        a.OrderNo ASC ";

            string allCategorySQL = @"SELECT 
                                            b.GroupID,
                                            '0' AS 'PosID',
                                            b.OrderNo,
                                            a.AppType,
                                            a.ShowName,
											IFNULL( a.MainPackSize,0) AS 'MainPackSize',
                                            a.RecommWord AS 'RecommWord',
                                            a.DownTimesReal,
                                            a.RecommTag,
                                            a.RecommLevel,
                                            a.MainIconUrl,
                                            -- a.ThumbPicUrl,
                                            b.GroupPicUrl,
                                            a.CreateTime,
                                            a.AppID,
                                            a.PackName,
                                            a.MainPackID,
                                            a.MainVerCode,
                                            a.PackSign,
                                            a.MainVerName,
                                            a.UpdateTime 
                                        FROM
                                            AppInfo AS a 
                                           
                                            INNER JOIN GroupInfo AS b 
                                                ON b.GroupTypeID = a.AppType 
                                       
                                        WHERE a.Status = 1";

            //全部应用和游戏
            string allSoftSQL = @"SELECT 
                                    b.GroupID,
                                    '0' AS 'PosID',
                                    b.OrderNo,
                                    a.AppType,
                                    a.AppType div 100 as AppClass,
                                    a.ShowName,
                                   IFNULL( a.MainPackSize,0) AS 'MainPackSize',
                                    a.RecommWord AS 'RecommWord',
                                    a.DownTimesReal,
                                    a.RecommTag,
                                    a.RecommLevel,
                                    a.MainIconUrl,
                                    -- a.ThumbPicUrl,
                                    b.GroupPicUrl,
                                    a.CreateTime,
                                    a.AppID,
                                    a.PackName,
                                    a.MainPackID,
                                    a.MainVerCode,
                                    a.PackSign,
                                    a.MainVerName,
                                    a.UpdateTime 
                                FROM
                                    AppInfo AS a 
                                   
                                    INNER JOIN GroupInfo AS b 
                                        ON b.GroupTypeID = a.AppType 
                         
                                WHERE a.Status = 1  order by a.DownTimes desc";

            string groupInfoSQL = @"SELECT GroupTypeID,OrderType, GROUP_CONCAT(GroupID) AS 'GroupDesc'  FROM  GroupInfo   WHERE STATUS = 1   GROUP BY GroupTypeID,OrderType;";

            //应用中心-应用游戏分组数据
            string AppAndGameSQL = @"SELECT 
                                            (SELECT 
                                                groupid 
                                            FROM
                                                GroupInfo 
                                            WHERE GroupTypeID = 1000 
                                                AND orderType = 0 
                                                AND STATUS = 1 
                                            ORDER BY UpdateTime DESC 
                                            LIMIT 0, 1) 'GroupID',
                                            0 AS 'PosID',
                                            0 AS 'OrderNo',
                                            1000 AS 'TypeID',
                                            t.AppType div 100 as AppClass,
                                            t.ShowName,
                                            IFNULL(t.MainPackSize, 0) AS 'MainPackSize',
                                            t.RecommWord AS 'RecommWord',
                                            t.DownTimesReal,
                                            t.RecommTag,
                                            t.RecommLevel,
                                            t.MainIconUrl,
                                            -- t.ThumbPicUrl,
                                            (SELECT 
                                                GroupPicUrl 
                                            FROM
                                                GroupInfo 
                                            WHERE GroupTypeID = 1000 
                                                AND orderType = 0 
                                                AND STATUS = 1 
                                            ORDER BY UpdateTime DESC 
                                            LIMIT 0, 1) AS 'GroupPicUrl',
                                            t.CreateTime,
                                            t.AppID,
                                            t.PackName,
                                            t.MainPackID,
                                            t.MainVerCode,
                                            t.PackSign,
                                            t.MainVerName,
                                            t.UpdateTime 
                                        FROM
                                            AppInfo t 
                                        WHERE t.Status = 1 order by t.DownTimes desc";

            // 查询所有应用的分类名
            string AppTypeNameSql = @"select a.AppID,gt.TypeName from appinfo a
                                    inner join grouptypes gt
									on a.AppType=gt.TypeID
                                    and a.status=1
                                    and ((a.AppType > 1100 and a.AppType < 1199) or (a.AppType > 1200 and a.AppType < 1299))
                                    group by a.AppID,gt.TypeName;
                                    ";

            // 查询所有应用或者游戏分类的分组
            string GetAppTypeGroupIdsSqll = "select GroupID,GroupName from groupinfo where ((GroupTypeID > 1100 and GroupTypeID < 1199) or (GroupTypeID > 1200 and GroupTypeID < 1299)) and status=1 group by GroupID,GroupName";

            #endregion

            List<string> keyList = this.Redis.SearchKeys("hGroupElems:*_*");
            List<string> cacheList = this.Redis.SearchKeys("hGroupElemsCacheVer");

            List<GroupElemsEntity> groupElemsList = new List<GroupElemsEntity>();
            List<GroupElemsEntity> groupElemsListByUpdateTime = new List<GroupElemsEntity>();

            List<AppInfoEntity> allCategoryList = new List<AppInfoEntity>();
            List<AppInfoEntity> allCategoryListByUpdateTime = new List<AppInfoEntity>();

            List<AppInfoEntity> appList = new List<AppInfoEntity>();
            List<AppInfoEntity> gameList = new List<AppInfoEntity>();
            List<AppInfoEntity> appListByUpdateTime = new List<AppInfoEntity>();
            List<AppInfoEntity> gameListByUpdateTime = new List<AppInfoEntity>();

            List<AppInfoEntity> allAppList = null;
            List<AppInfoEntity> allAppListByUpdateTime = null;
            List<AppInfoEntity> allGameList = null;
            List<AppInfoEntity> allGameListByUpdateTime = null;

            List<AppInfoEntity> appAndGameLsit = null;
            List<AppInfoEntity> appAndGameListByUpdateTime = null;

            Dictionary<string, string> groupInfoDic = new Dictionary<string, string>();
            
            

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, groupInfoSQL))
            {
                List<GroupInfoEntity> list = objReader.ReaderToList<GroupInfoEntity>() as List<GroupInfoEntity>;

                groupInfoDic = list.ToDictionary(s => string.Format("{0}_{1}", s.GroupTypeID, s.OrderType), s => s.GroupDesc);
            }


            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, groupElemsSQL))
            {
                //查询GroupElems中的数据，处理按更新时间排序的数据
                groupElemsList = objReader.ReaderToList<GroupElemsEntity>() as List<GroupElemsEntity>;

                groupElemsListByUpdateTime = groupElemsList.OrderByDescending(s => s.UpdateTime).ToList();

            }

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, allCategorySQL))
            {
                //查询AppInfo表中的数据，按更新时间排序的数据
                allCategoryList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;

                allCategoryListByUpdateTime = allCategoryList.OrderByDescending(s => s.UpdateTime).ToList();
            }

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, allSoftSQL))
            {
                //查询全部应用和全部游戏，按更新时间排序的数据
                List<AppInfoEntity> allSoftList = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;

                //各应用分组
                appList = allSoftList.Where(s => s.AppClass == 11).ToList();
                appListByUpdateTime = appList.OrderByDescending(s => s.UpdateTime).ToList();

                //各游戏分组
                gameList = allSoftList.Where(s => s.AppClass == 12).ToList();
                gameListByUpdateTime = gameList.OrderByDescending(s => s.UpdateTime).ToList();

                //所有游戏默认排序
                allGameList = gameList.CloneObject<List<AppInfoEntity>>();
                allGameList.ForEach(s => s.GroupID = Convert.ToInt32(groupInfoDic["1200_0"]));

                //所有应用按更新时间倒序
                allAppListByUpdateTime = appListByUpdateTime.CloneObject<List<AppInfoEntity>>();
                allAppListByUpdateTime.ForEach(s => s.GroupID = Convert.ToInt32(groupInfoDic["1100_0"]));


                //所有游戏按更新时间倒序
                allGameListByUpdateTime = gameListByUpdateTime.CloneObject<List<AppInfoEntity>>();
                allGameListByUpdateTime.ForEach(s => s.GroupID = Convert.ToInt32(groupInfoDic["1200_2"]));

                //所有应用默认排序
                allAppList = appList.CloneObject<List<AppInfoEntity>>();
                allAppList.ForEach(s => s.GroupID = Convert.ToInt32(groupInfoDic["1100_2"]));

                // 截取列表长度 -- 所有游戏列表
                if (allGameList.Count > elemCountInGroup)
                {
                    allGameList.RemoveRange(elemCountInGroup, allGameList.Count - elemCountInGroup);
                }
                if (allGameListByUpdateTime.Count > elemCountInGroup)
                {
                    allGameListByUpdateTime.RemoveRange(elemCountInGroup, allGameListByUpdateTime.Count - elemCountInGroup);
                }
                // 截取列表长度 -- 所有应用列表
                if (allAppList.Count > elemCountInGroup)
                {
                    allAppList.RemoveRange(elemCountInGroup, allAppList.Count - elemCountInGroup);
                }
                if (allAppListByUpdateTime.Count > elemCountInGroup)
                {
                    allAppListByUpdateTime.RemoveRange(elemCountInGroup, allAppListByUpdateTime.Count - elemCountInGroup);
                }
            }


            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, AppAndGameSQL))
            {
                appAndGameLsit = objReader.ReaderToList<AppInfoEntity>() as List<AppInfoEntity>;
                appAndGameListByUpdateTime = appAndGameLsit.CloneObject<List<AppInfoEntity>>().OrderByDescending(t => t.UpdateTime).ToList();

                //应用中心中的“应用游戏分组数据”
                appAndGameListByUpdateTime.ForEach(s => s.GroupID = Convert.ToInt32(groupInfoDic["1000_2"]));

                // 截取列表长度 -- 所有应用游戏混合列表
                if (appAndGameListByUpdateTime.Count > elemCountInGroup)
                {
                    appAndGameListByUpdateTime.RemoveRange(elemCountInGroup, appAndGameListByUpdateTime.Count - elemCountInGroup);
                }
            }

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, AppTypeNameSql))
            {
                while (objReader.Read())
                {
                    AppIDTypeNameDic.Add(ConvertHelper.ToInt(objReader["AppID"]), ConvertHelper.ToString(objReader["TypeName"]));
                }
            }

            using (MySqlDataReader objReader = MySqlHelper.ExecuteReader(this.ConnectionString, GetAppTypeGroupIdsSqll))
            {
                GroupIDTypeNameDic = new Dictionary<int, string>();
                while (objReader.Read())
                {
                    GroupIDTypeNameDic.Add(ConvertHelper.ToInt(objReader["GroupID"]), ConvertHelper.ToString(objReader["GroupName"]));
                }
            }


            using (RedisTransaction trans = new RedisTransaction(this.Redis))
            {
                //清除源缓存
                foreach (string currentKey in keyList)
                {
                    trans.QueueCommand(s => s.Remove(currentKey));
                }


                this.WriteGroupElemsParams(trans, groupElemsList);
                this.WriteGroupElemsParams(trans, groupElemsListByUpdateTime);

                this.WriteAppInfoParams(trans, allCategoryList);
                this.WriteAppInfoParams(trans, allAppListByUpdateTime);

                this.WriteAppInfoParams(trans, appList);
                this.WriteAppInfoParams(trans, appListByUpdateTime);

                this.WriteAppInfoParams(trans, gameList);
                this.WriteAppInfoParams(trans, gameListByUpdateTime);

                this.WriteAppInfoParams(trans, allAppList);
                this.WriteAppInfoByUpdateTimeParams(trans, allAppListByUpdateTime);

                this.WriteAppInfoParams(trans, allGameList);
                this.WriteAppInfoByUpdateTimeParams(trans, allGameListByUpdateTime);

                this.WriteAppInfoParams(trans, appAndGameLsit);
                this.WriteAppInfoByUpdateTimeParams(trans, appAndGameListByUpdateTime);

                trans.Commit();

            }

            return this.Redis.SearchKeys("hGroupElems:*_*").Count > 0;
        }

        /// <summary>
        /// GroupElems相关（包括按时间排序）
        /// </summary>
        /// <param name="trans"></param>
        /// <param name="sourceList"></param>
        private void WriteGroupElemsParams(RedisTransaction trans, List<GroupElemsEntity> sourceList)
        {
            foreach (GroupElemsEntity item in sourceList)
            {
                Dictionary<string, string> dic = new Dictionary<string, string>();

                dic.Add("Igid", item.GroupID.ToString());
                dic.Add("Iposid", item.PosID.ToString());
                dic.Add("Iodno", item.OrderNo.ToString());
                dic.Add("Ietype", item.ElemType.ToString());

                if (item.ElemType == 6)
                {
                    dic.Add("Ssoname", string.IsNullOrEmpty(item.ShowName) ? item.RecommTitle : item.ShowName);
                }
                else
                {
                    dic.Add("Ssoname", string.IsNullOrEmpty(item.ShowName) ? "" : item.ShowName);
                }


                if (string.IsNullOrEmpty(item.RecommWord))
                {
                    // 如果是分类，则显示大小
                    if (GroupIDTypeNameDic.ContainsKey(item.GroupID))
                    {
                        dic.Add("Srmword", this.GetPackSizeDesc(item.MainPackSize.Convert<double>(0)));
                    }
                    else
                    { 
                        // 显示分类名
                        if (item.ElemType == 1 && AppIDTypeNameDic.ContainsKey(item.ElemID))
                        {
                            dic.Add("Srmword", AppIDTypeNameDic[item.ElemID]);
                        }
                        else
                        {
                            dic.Add("Srmword", "");
                        }

                    }
                    // dic.Add("Srmword", this.GetDownlTimesChinese(item.DownTimes));
                }
                else
                {
                    dic.Add("Srmword", item.RecommWord);
                }

                dic.Add("Irmflag", item.RecommFlag.ToString());
                dic.Add("Irmlevel", item.RecommLevel.ToString());
                dic.Add("Sicurl", string.IsNullOrEmpty(item.MainIconUrl) ? "" : item.MainIconUrl);
                dic.Add("Sthumburl", string.IsNullOrEmpty(item.ThumbPicUrl) ? "" : item.ThumbPicUrl);
                dic.Add("Sadsurl", string.IsNullOrEmpty(item.RecommPicUrl) ? "" : item.RecommPicUrl);
                dic.Add("Dptime", item.CreateTime.ToString("yyyyMMddHHmmss"));
                dic.Add("Iappid", item.AppID.ToString());
                dic.Add("Spkname", string.IsNullOrEmpty(item.PackName) ? "" : item.PackName);
                dic.Add("Impkid", item.MainPackID.ToString());
                dic.Add("Imvercode", string.IsNullOrEmpty(item.MainVerCode) ? "" : item.MainVerCode);
                dic.Add("Smsigncode", item.PackSign);
                dic.Add("Smvername", string.IsNullOrEmpty(item.MainVerName) ? "" : item.MainVerName);
                dic.Add("Impksize", string.IsNullOrEmpty(item.MainPackSize) ? "0" : item.MainPackSize);
                dic.Add("Ijlinkid", item.LinkID.ToString());
                dic.Add("Sjlinkurl", string.IsNullOrEmpty(item.LinkUrl) ? "" : item.LinkUrl);
                dic.Add("Ijgid", item.ElemID.ToString());
                dic.Add("Ijgtype", item.GroupType.ToString());
                dic.Add("Ijodtype", item.OrderType.ToString());
                dic.Add("Ishowtype", item.ShowType.ToString());

                var dicKeys = dic.Keys.ToArray();

                foreach (var eachKey in dicKeys)
                {
                    if (eachKey.StartsWith("S"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? string.Empty : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("I"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("D"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                }


                //if (elemTypeListWithoutElemid.Contains(item.ElemType))
                //{
                //    trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}", item.GroupID, item.ElemType, item.GroupElemID), dic));
                //}
                //else
                //{
                //    trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}", item.GroupID, item.ElemType, item.ElemID), dic));
                //}

                trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}_{3}", item.GroupID, item.GroupElemID, item.ElemType, item.ElemID), dic));

                trans.QueueCommand(s => s.SetEntryInHash("hGroupElemsCacheVer", string.Format("groupelems_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
            }
        }

        /// <summary>
        /// AppInfo相关默认排序
        /// </summary>
        /// <param name="trans"></param>
        /// <param name="sourceList"></param>
        private void WriteAppInfoParams(RedisTransaction trans, List<AppInfoEntity> sourceList)
        {
            foreach (AppInfoEntity item in sourceList)
            {
                Dictionary<string, string> dic = new Dictionary<string, string>();

                dic.Add("Igid", item.GroupID.ToString());
                dic.Add("Iposid", item.PosID.ToString());
                dic.Add("Iodno", item.OrderNo.ToString());
                dic.Add("Ietype", item.AppType.ToString());
                dic.Add("Ssoname", string.IsNullOrEmpty(item.ShowName) ? "" : item.ShowName);

                if (string.IsNullOrEmpty(item.RecommWord))
                {

                    //dic.Add("Srmword", this.GetDownlTimesChinese(item.DownTimes));
                    //dic.Add("Srmword", this.GetPackSizeDesc(item.MainPackSize.Convert<int>(0)));


                    // 如果是分类，则显示大小
                    if (GroupIDTypeNameDic.ContainsKey(item.GroupID))
                    {
                        dic.Add("Srmword", this.GetPackSizeDesc(item.MainPackSize.Convert<double>(0)));
                    }
                    else
                    {
                        // 显示分类名
                        if (AppIDTypeNameDic.ContainsKey(item.AppID))
                        {
                            dic.Add("Srmword", AppIDTypeNameDic[item.AppID]);
                        }
                        else
                        {
                            dic.Add("Srmword", "");
                        }
                    }
                }
                else
                {
                    dic.Add("Srmword", item.RecommWord);
                }

                dic.Add("Irmflag", item.RecommTag.ToString());
                dic.Add("Irmlevel", item.RecommLevel.ToString());
                dic.Add("Sicurl", item.MainIconUrl);
                dic.Add("Sthumburl", string.Empty);
                dic.Add("Sadsurl", string.IsNullOrEmpty(item.GroupPicUrl) ? "" : item.GroupPicUrl);
                dic.Add("Dptime", item.CreateTime.ToString("yyyyMMddHHmmss"));
                dic.Add("Iappid", item.AppID.ToString());
                dic.Add("Spkname", item.PackName);
                dic.Add("Impkid", item.MainPackID.ToString());
                dic.Add("Imvercode", item.MainVerCode.ToString());
                dic.Add("Smsigncode", item.PackSign);
                dic.Add("Smvername", item.MainVerName);
                dic.Add("Impksize", item.MainPackSize.ToString());
                dic.Add("Ijlinkid", "0");
                dic.Add("Sjlinkurl", string.Empty);
                dic.Add("Ijgid", "0");
                dic.Add("Ijgtype", "0");
                dic.Add("Ijodtype", "0");
                dic.Add("Ishowtype", "0");

                var dicKeys = dic.Keys.ToArray();

                foreach (var eachKey in dicKeys)
                {
                    if (eachKey.StartsWith("S"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? string.Empty : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("I"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("D"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                }

                //trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}", item.GroupID, 1, item.AppID), dic));
                trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}_{3}", item.GroupID, 0, 1, item.AppID), dic));

                trans.QueueCommand(s => s.SetEntryInHash("hGroupElemsCacheVer", string.Format("groupelems_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
            }
        }

        /// <summary>
        /// AppInfo相关 按更新时间排序
        /// </summary>
        /// <param name="trans"></param>
        /// <param name="sourceList"></param>
        private void WriteAppInfoByUpdateTimeParams(RedisTransaction trans, List<AppInfoEntity> sourceList)
        {
            foreach (AppInfoEntity item in sourceList)
            {
                Dictionary<string, string> dic = new Dictionary<string, string>();

                dic.Add("Igid", item.GroupID.ToString());
                dic.Add("Iposid", item.PosID.ToString());
                dic.Add("Iodno", item.OrderNo.ToString());
                dic.Add("Ietype", item.AppType.ToString());
                dic.Add("Ssoname", string.IsNullOrEmpty(item.ShowName) ? "" : item.ShowName);

                if (string.IsNullOrEmpty(item.RecommWord))
                {
                    //dic.Add("Srmword", string.Format("更新时间：{0}", item.UpdateTime.ToString("yyyy-MM-dd")));

                    // 如果是分类，则显示大小
                    if (GroupIDTypeNameDic.ContainsKey(item.GroupID))
                    {
                        dic.Add("Srmword", this.GetPackSizeDesc(item.MainPackSize.Convert<double>(0)));
                    }
                    else
                    {
                        // 显示分类名
                        if (AppIDTypeNameDic.ContainsKey(item.AppID))
                        {
                            dic.Add("Srmword", AppIDTypeNameDic[item.AppID]);
                        }
                        else
                        {
                            dic.Add("Srmword", "");
                        }
                    }
                }
                else
                {
                    dic.Add("Srmword", item.RecommWord);
                }

                dic.Add("Irmflag", item.RecommTag.ToString());
                dic.Add("Irmlevel", item.RecommLevel.ToString());
                dic.Add("Sicurl", item.MainIconUrl);
                dic.Add("Sthumburl", string.Empty);
                dic.Add("Sadsurl", string.IsNullOrEmpty(item.GroupPicUrl) ? "" : item.GroupPicUrl);
                dic.Add("Dptime", item.CreateTime.ToString("yyyyMMddHHmmss"));
                dic.Add("Iappid", item.AppID.ToString());
                dic.Add("Spkname", item.PackName);
                dic.Add("Impkid", item.MainPackID.ToString());
                dic.Add("Imvercode", item.MainVerCode.ToString());
                dic.Add("Smsigncode", item.PackSign);
                dic.Add("Smvername", item.MainVerName);
                dic.Add("Impksize", item.MainPackSize.ToString());
                dic.Add("Ijlinkid", "0");
                dic.Add("Sjlinkurl", string.Empty);
                dic.Add("Ijgid", "0");
                dic.Add("Ijgtype", "0");
                dic.Add("Ijodtype", "0");
                dic.Add("Ishowtype", "0");

                var dicKeys = dic.Keys.ToArray();

                foreach (var eachKey in dicKeys)
                {
                    if (eachKey.StartsWith("S"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? string.Empty : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("I"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                    else if (eachKey.StartsWith("D"))
                    {
                        dic[eachKey] = string.IsNullOrEmpty(dic[eachKey]) ? "0" : dic[eachKey];
                    }
                }

                //trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}", item.GroupID, 1, item.AppID), dic));
                trans.QueueCommand(s => s.SetRangeInHash(string.Format("hGroupElems:{0}_{1}_{2}_{3}", item.GroupID, 0, 1, item.AppID), dic));

                trans.QueueCommand(s => s.SetEntryInHash("hGroupElemsCacheVer", string.Format("groupelems_{0}", item.GroupID.ToString()), DateTime.Now.ToString("yyyyMMddHHmmss")));
            }
        }

        public string GetPackSizeDesc(double size)
        {
            return string.Format("{0}M", Math.Round(size / 1024 / 1024, 2));
        }

        public string GetDownlTimesChinese(int downTimes)
        {
            if (downTimes > 1000 && downTimes < 10000)
            {
                return string.Format("超过{0}千次", downTimes / 1000);
            }
            else if (downTimes > 10000)
            {
                return string.Format("超过{0}万次", downTimes / 10000);
            }
            else
            {
                return string.Format("下载{0}次", downTimes);
            }
        }

    }
}
