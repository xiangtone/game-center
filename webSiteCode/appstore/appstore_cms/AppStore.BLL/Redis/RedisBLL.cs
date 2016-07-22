using AppStore.DAL.Redis;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL.Redis
{
    public class RedisBLL
    {
        /// <summary>
        /// # 方案下的分组id列表[集合]
        ///～Name：sAppScheme:[schemeid]
        ///～Desc：分组id列表
        ///～Val： [groupids]
        /// </summary>
        /// <returns></returns>
        public bool SAppScheme()
        {
            return new RedisDAL().SAppScheme();
        }

        /// <summary>
        /// 应用信息 hAppInfo:[appid]
        /// </summary>
        /// <returns></returns>
        public bool HAppInfo()
        {
            return new RedisDAL().HAppInfo();
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
            return new RedisDAL().SSchemeGroups();
        }
        /// <summary>
        /// # 方案分组信息[哈希]
        ///～Name：hSchemeGroups:[schemeid]_[groupid]
        /// </summary>
        /// <returns></returns>
        public bool HSchemeGroups()
        {
            return new RedisDAL().HGroups();
        }

        public bool SsGroups()
        {
            return new RedisDAL().SsGroups();
        }

        /// <summary>
        /// 最新应用版本[哈希]
        ///～Name：hNewestAppVer:[packname]
        /// </summary>
        /// <returns></returns>
        public bool HNewestAppVer()
        {
            return new RedisDAL().HNewestAppVer();
        }

        /// <summary>
        /// # 分组元素列表[哈希]
        ///～Name：hGroupElems:[groupid]_[elemid]
        /// </summary>
        /// <returns></returns>
        public bool HGroupElems()
        {
            return new RedisDAL().HGroupElems();
        }
    }
}
