-- MySQL dump 10.13  Distrib 5.6.29, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: cmsbase
-- ------------------------------------------------------
-- Server version	5.6.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `actionlog`
--

DROP TABLE IF EXISTS `actionlog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actionlog` (
  `LogID` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `UserId` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `UserIP` varchar(50) NOT NULL DEFAULT '' COMMENT '用户IP',
  `Action` varchar(50) NOT NULL DEFAULT '' COMMENT '操作',
  `Content` varchar(3000) NOT NULL DEFAULT '' COMMENT '日志内容',
  `Level` int(11) NOT NULL DEFAULT '1' COMMENT '日志级别（1=debug，2=info，3=error）',
  `LogTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
  PRIMARY KEY (`LogID`)
) ENGINE=InnoDB AUTO_INCREMENT=6810 DEFAULT CHARSET=utf8 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `actionlog`
--

LOCK TABLES `actionlog` WRITE;
/*!40000 ALTER TABLE `actionlog` DISABLE KEYS */;
INSERT INTO `actionlog` VALUES (6736,6,'182.254.242.200','清除缓存','',2,'2016-04-29 12:47:42'),(6737,6,'182.254.242.200','清除缓存','',2,'2016-04-29 12:58:56'),(6738,6,'183.38.213.244','登录成功','',1,'2016-04-29 13:11:32'),(6739,6,'183.38.213.244','修改角色模块权限','角色ID：27，角色名：游戏中心管理员，模块ID：233',2,'2016-04-29 13:16:58'),(6740,6,'183.38.213.244','重置密码','用户ID：1，用户名：admin，操作结果：重置密码成功。',2,'2016-04-29 13:18:06'),(6741,1,'183.38.213.244','登录成功','',1,'2016-04-29 13:18:14'),(6742,1,'183.38.213.244','登录成功','',1,'2016-04-29 13:18:16'),(6743,6,'183.38.213.244','登录成功','',1,'2016-04-29 13:18:26'),(6744,6,'183.38.213.244','登录成功','',1,'2016-04-29 13:20:49'),(6745,6,'183.38.213.244','修改角色模块权限','角色ID：28，角色名：更新系统管理员，模块ID：252',2,'2016-04-29 13:30:17'),(6746,6,'183.38.213.244','修改角色模块权限','角色ID：29，角色名：数据统计管理员，模块ID：246',2,'2016-04-29 13:30:27'),(6747,6,'183.38.213.244','修改用户角色','用户ID：39，用户名：oddshou，角色ID：27,28,29',2,'2016-04-29 13:30:41'),(6748,6,'183.38.213.244','修改用户角色','用户ID：19，用户名：greenyhu，角色ID：27,28,29',2,'2016-04-29 13:30:49'),(6749,6,'183.38.213.244','修改用户角色','用户ID：39，用户名：oddshou，角色ID：1,27,28,29',2,'2016-04-29 13:30:57'),(6750,6,'183.38.213.244','修改用户角色','用户ID：39，用户名：oddshou，角色ID：1,27,28,29',2,'2016-04-29 13:31:16'),(6751,6,'183.38.213.244','登录成功','',1,'2016-04-29 13:33:46'),(6752,6,'183.38.213.244','重置密码','用户ID：39，用户名：oddshou，操作结果：重置密码成功。',2,'2016-04-29 13:34:51'),(6753,39,'14.127.86.144','登录成功','',1,'2016-04-29 13:35:04'),(6754,6,'183.38.213.244','新增用户','用户ID：0，用户名：lihuang，操作结果：新增用户成功，返回用户ID：40',2,'2016-04-29 13:35:59'),(6755,6,'183.38.213.244','修改用户角色','用户ID：40，用户名：lihuang，角色ID：1',2,'2016-04-29 13:36:02'),(6756,40,'14.127.86.144','登录成功','',1,'2016-04-29 13:36:59'),(6757,40,'14.127.86.144','登录成功','',1,'2016-04-29 13:38:45'),(6758,19,'14.127.86.144','登录成功','',1,'2016-04-29 14:01:50'),(6759,40,'14.127.86.144','重置密码','用户ID：19，用户名：greenyhu，操作结果：重置密码成功。',2,'2016-04-29 14:12:32'),(6760,19,'14.127.86.144','登录成功','',1,'2016-04-29 14:13:41'),(6761,19,'14.127.86.144','登录成功','',1,'2016-04-29 14:15:09'),(6762,39,'14.127.86.144','登录成功','',1,'2016-04-29 14:16:49'),(6763,40,'14.127.86.144','新增用户','用户ID：0，用户名：laiyuqing，操作结果：新增用户成功，返回用户ID：41',2,'2016-04-29 14:21:24'),(6764,40,'14.127.86.144','修改用户角色','用户ID：41，用户名：laiyuqing，角色ID：27,28,29',2,'2016-04-29 14:21:31'),(6765,40,'14.127.86.144','修改用户角色','用户ID：41，用户名：laiyuqing，角色ID：27,28,29',2,'2016-04-29 14:22:01'),(6766,41,'14.127.86.144','登录成功','',1,'2016-04-29 14:22:54'),(6767,19,'14.127.86.144','登录成功','',1,'2016-04-29 14:35:37'),(6768,41,'14.127.86.144','登录成功','',1,'2016-04-29 15:23:48'),(6769,41,'14.127.86.144','登录成功','',1,'2016-04-29 15:23:55'),(6770,6,'183.38.213.244','登录成功','',1,'2016-04-29 15:33:23'),(6771,41,'14.127.86.144','登录成功','',1,'2016-04-29 15:35:44'),(6772,41,'14.127.86.144','登录成功','',1,'2016-04-29 16:36:55'),(6773,41,'14.127.86.144','登录成功','',1,'2016-04-29 17:44:25'),(6774,40,'14.127.86.144','登录成功','',1,'2016-04-29 18:26:30'),(6775,19,'14.127.86.144','登录成功','',1,'2016-04-29 18:48:04'),(6776,41,'14.127.86.144','登录成功','',1,'2016-04-29 18:53:37'),(6777,19,'14.127.86.144','登录成功','',1,'2016-04-29 19:48:35'),(6778,19,'14.127.86.144','登录成功','',1,'2016-04-29 20:30:54'),(6779,6,'183.38.213.244','登录成功','',1,'2016-04-30 14:43:37'),(6780,6,'112.95.196.45','登录成功','',1,'2016-05-02 11:45:21'),(6781,6,'112.95.198.242','登录成功','',1,'2016-05-02 16:11:03'),(6782,41,'183.15.254.175','登录成功','',1,'2016-05-03 09:13:07'),(6783,40,'183.15.254.175','登录成功','',1,'2016-05-03 09:16:39'),(6784,19,'183.15.254.175','登录成功','',1,'2016-05-03 09:17:00'),(6785,41,'183.15.254.175','登录成功','',1,'2016-05-03 10:22:03'),(6786,19,'183.15.254.175','登录成功','',1,'2016-05-03 10:36:32'),(6787,40,'183.15.254.175','登录成功','',1,'2016-05-03 11:10:56'),(6788,41,'183.15.254.175','登录成功','',1,'2016-05-03 11:29:48'),(6789,19,'14.127.84.213','登录成功','',1,'2016-05-03 11:45:40'),(6790,19,'14.127.84.213','登录成功','',1,'2016-05-03 14:47:40'),(6791,41,'14.127.84.213','登录成功','',1,'2016-05-03 15:45:17'),(6792,41,'14.127.84.213','登录成功','',1,'2016-05-03 17:10:43'),(6793,40,'14.127.84.213','登录成功','',1,'2016-05-03 17:22:18'),(6794,40,'14.127.84.213','登录成功','',1,'2016-05-03 18:33:51'),(6795,41,'14.127.84.213','登录成功','',1,'2016-05-04 09:39:31'),(6796,41,'14.127.84.213','登录成功','',1,'2016-05-04 10:41:35'),(6797,41,'14.127.84.213','登录成功','',1,'2016-05-04 11:42:34'),(6798,41,'14.127.84.213','登录成功','',1,'2016-05-04 15:11:52'),(6799,41,'14.127.84.213','登录成功','',1,'2016-05-04 16:13:22'),(6800,19,'14.127.84.213','登录成功','',1,'2016-05-04 18:03:45'),(6801,19,'14.127.84.213','登录成功','',1,'2016-05-05 09:14:17'),(6802,41,'14.127.84.213','登录成功','',1,'2016-05-05 09:15:05'),(6803,41,'14.127.84.213','登录成功','',1,'2016-05-05 10:15:18'),(6804,41,'14.127.84.213','登录成功','',1,'2016-05-05 11:16:48'),(6805,41,'183.15.194.216','登录成功','',1,'2016-05-05 13:47:42'),(6806,19,'183.15.194.216','登录成功','',1,'2016-05-05 14:22:56'),(6807,41,'183.15.194.216','登录成功','',1,'2016-05-05 14:47:52'),(6808,41,'183.15.194.216','登录成功','',1,'2016-05-05 15:48:20'),(6809,41,'183.15.194.216','登录成功','',1,'2016-05-05 16:48:59');
/*!40000 ALTER TABLE `actionlog` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightinfo`
--

DROP TABLE IF EXISTS `rightinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightinfo` (
  `UserId` int(11) NOT NULL DEFAULT '0',
  `ModuleId` int(11) NOT NULL DEFAULT '0',
  `PermValue` int(11) DEFAULT NULL,
  PRIMARY KEY (`UserId`,`ModuleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightinfo`
--

LOCK TABLES `rightinfo` WRITE;
/*!40000 ALTER TABLE `rightinfo` DISABLE KEYS */;
/*!40000 ALTER TABLE `rightinfo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightmodules`
--

DROP TABLE IF EXISTS `rightmodules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightmodules` (
  `ModuleId` int(11) NOT NULL AUTO_INCREMENT COMMENT '模块ID',
  `ParentId` int(11) NOT NULL DEFAULT '0' COMMENT '模块父ID',
  `ModuleName` varchar(50) NOT NULL DEFAULT '' COMMENT '模块名称',
  `ModuleFlag` varchar(50) NOT NULL DEFAULT '' COMMENT '模块标识',
  `ModulePath` varchar(500) NOT NULL DEFAULT '' COMMENT '模块路径,所有父模块的模块ID,逗号分隔',
  `ModuleType` int(11) NOT NULL DEFAULT '0' COMMENT '模块类型，1=目录，2=页面，3=隐页',
  `OrderNo` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `PermType` int(11) NOT NULL DEFAULT '1' COMMENT '授权类型，1=授权使用，2=登录用户，3=无需登录',
  `PermDefine` varchar(200) NOT NULL DEFAULT '' COMMENT '授权定义，如：1:新增,2:删除,4:修改,8:查看详情。等等',
  `ModuleUri` varchar(500) NOT NULL DEFAULT '' COMMENT '模块URI',
  `ActionValue` varchar(200) NOT NULL DEFAULT '' COMMENT 'ActType参数值',
  `ModuleDesc` varchar(500) NOT NULL DEFAULT '' COMMENT '模块描述',
  `CreateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `Status` int(11) NOT NULL DEFAULT '0' COMMENT '模块状态，0=正常，1=禁用，2=开发，3=测试',
  PRIMARY KEY (`ModuleId`)
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8 COMMENT='权限模块表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightmodules`
--

LOCK TABLES `rightmodules` WRITE;
/*!40000 ALTER TABLE `rightmodules` DISABLE KEYS */;
INSERT INTO `rightmodules` VALUES (1,0,'全部权限','',',0,',1,0,1,'','','','','2014-04-07 15:04:35',0),(2,1,'管理员','',',0,1,',1,0,1,'','','','','2014-04-07 15:04:35',0),(3,2,'系统模块管理(old)','',',0,1,2,',3,0,1,'<span>[1=添加]</span><span>[2=删除]</span><span>[4=修改]</span>','http://cms.huashenggame.com/mgr/modulelist.aspx','','','2014-04-07 15:04:35',0),(4,2,'编辑系统模块','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mgr/modulemodify.aspx','','','2014-04-07 15:04:35',0),(5,2,'系统用户管理(old)','',',0,1,2,',3,0,1,'<span>[1=添加]</span><span>[2=删除]</span><span>[4=修改]</span>','http://cms.huashenggame.com/mgr/userlist.aspx','','','2014-04-07 15:04:35',0),(6,2,'编辑系统用户','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mgr/usermodify.aspx','','','2014-04-07 15:04:35',0),(7,2,'用户权限管理','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mgr/userright.aspx','','','2014-04-07 15:04:35',0),(8,2,'系统用户管理','',',0,1,2,',2,0,1,'','http://cms.huashenggame.com/mng/sysUserlist.aspx','','','2014-04-07 15:04:35',0),(9,2,'系统模块管理','',',0,1,2,',2,0,1,'','http://cms.huashenggame.com/mng/sysmodulelist.aspx','','','2014-04-07 15:04:35',0),(10,2,'系统角色管理','',',0,1,2,',2,0,1,'','http://cms.huashenggame.com/mng/sysRolelist.aspx','','','2014-04-07 15:04:35',0),(11,2,'系统用户编辑页面','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mng/sysUserModify.aspx','','系统用户编辑和添加页面','2014-04-07 15:04:35',0),(12,2,'系统模块编辑页面','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mng/sysModuleModify.aspx','','系统模块编辑页面','2014-04-07 15:04:35',0),(13,2,'系统角色编辑页面','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mng/sysRoleModify.aspx','','系统角色的添加和编辑页面','2014-04-17 15:47:56',0),(14,2,'角色所属模块设置页面','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mng/setModuleParent.aspx','','','2014-04-17 15:50:30',0),(15,2,'系统角色权限分配页面','',',0,1,2,',3,0,1,'','http://cms.huashenggame.com/mng/setRoleModuleRight.aspx','','用来设置角色对应模块的权限','2014-04-18 11:42:17',0),(16,2,'系统缓存管理','',',0,1,2,',2,0,1,'<span>[511=全部权限]</span>','http://cms.huashenggame.com/mng/sysCacheList.aspx','','管理系统缓存','2014-04-22 15:10:17',0),(24,2,'操作日志','',',0,1,2,',2,0,1,'','http://cms.huashenggame.com/mng/ActionLogList.aspx','','','2014-08-27 16:52:44',0),(42,1,'个人设定页','',',0,1,',3,1,2,'','http://cms.huashenggame.com/settings.aspx','','','2014-09-01 20:57:57',0),(43,1,'默认页','',',0,1,',3,1,2,'','http://cms.huashenggame.com/default.aspx','','','2014-09-01 20:58:12',0),(44,1,'注销页','',',0,1,',3,1,2,'','http://cms.huashenggame.com/logout.aspx','','','2014-09-01 20:58:28',0),(115,1,'登录检查页','',',0,1,',3,1,2,'','http://cms.huashenggame.com/logincheck.aspx','','','2015-05-27 14:57:29',0),(233,1,'游戏中心','',',0,1,',1,2,1,'','','','','2016-04-29 13:02:46',0),(234,233,'游戏配置','',',0,1,233,',2,1,1,'','http://cms.huashenggame.com/appstore_cms/GameInfoList.aspx','','','2016-04-29 13:04:01',0),(235,233,'首页配置','',',0,1,233,',2,2,1,'','http://cms.huashenggame.com/appstore_cms/HomePageRecommendByAppCenterList.aspx?acttype=4102,104','','','2016-04-29 13:04:36',0),(236,233,'精品推荐','',',0,1,233,',2,3,1,'','http://cms.huashenggame.com/appstore_cms/LauncherRecommendList.aspx?acttype=4108,104','','','2016-04-29 13:05:09',0),(237,233,'官方推荐','',',0,1,233,',2,4,1,'','http://cms.huashenggame.com/appstore_cms/BeginnerRecommendList.aspx?acttype=4101,104','','','2016-04-29 13:05:35',0),(238,233,'跳转链接配置','',',0,1,233,',2,5,1,'','http://cms.huashenggame.com/appstore_cms/LinkInfoList.aspx','','','2016-04-29 13:06:05',0),(239,233,'专题配置','',',0,1,233,',2,6,1,'','http://cms.huashenggame.com/appstore_cms/SpecialTopicList.aspx?acttype=3200,104','','','2016-04-29 13:06:32',0),(240,233,'搜索词配置','',',0,1,233,',2,7,1,'','http://cms.huashenggame.com/appstore_cms/PopularList.aspx?acttype=104','','','2016-04-29 13:07:27',0),(241,233,'渠道配置','',',0,1,233,',2,8,1,'','http://cms.huashenggame.com/appstore_cms/ChannelList.aspx','','','2016-04-29 13:07:59',0),(242,233,'cps配置','',',0,1,233,',2,9,1,'','http://cms.huashenggame.com/appstore_cms/CpsList.aspx','','','2016-04-29 13:08:34',0),(243,233,'举报管理','',',0,1,233,',2,10,1,'','http://cms.huashenggame.com/appstore_cms/AppInformList.aspx','','','2016-04-29 13:08:58',0),(244,233,'用户反馈','',',0,1,233,',2,11,1,'','http://cms.huashenggame.com/appstore_cms/FeedBack.aspx?ClientId=12','','','2016-04-29 13:10:00',0),(245,233,'数据同步','',',0,1,233,',2,12,1,'','http://cms.huashenggame.com/appstore_cms/SyncManager.aspx','','','2016-04-29 13:10:48',0),(246,1,'统计数据','',',0,1,',1,3,1,'','','','','2016-04-29 13:22:54',0),(247,246,'首页','',',0,1,246,',2,1,1,'','http://cms.huashenggame.com/gcstat_cms/Index.aspx','','','2016-04-29 13:23:32',0),(248,246,'基础数据','',',0,1,246,',2,2,1,'','http://cms.huashenggame.com/gcstat_cms/BaseData.aspx','','','2016-04-29 13:23:54',0),(249,246,'下载相关','',',0,1,246,',2,3,1,'','http://cms.huashenggame.com/gcstat_cms/DownLoad.aspx','','','2016-04-29 13:24:15',0),(250,246,'下载人数','',',0,1,246,',2,4,1,'','http://cms.huashenggame.com/gcstat_cms/DownLoadDevice.aspx','','','2016-04-29 13:24:42',0),(251,246,'页面访问','',',0,1,246,',2,5,1,'','http://cms.huashenggame.com/gcstat_cms/PageVisit.aspx','','','2016-04-29 13:25:07',0),(252,1,'更新系统','',',0,1,',1,4,1,'','','','','2016-04-29 13:27:49',0),(253,252,'更新管理','',',0,1,252,',2,1,1,'','http://cms.huashenggame.com/updatesys_cms/packList.aspx','','','2016-04-29 13:28:34',0),(254,252,'测试方案','',',0,1,252,',2,2,1,'','http://cms.huashenggame.com/updatesys_cms/TestScheme.aspx','','','2016-04-29 13:29:18',0);
/*!40000 ALTER TABLE `rightmodules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightrolemodules`
--

DROP TABLE IF EXISTS `rightrolemodules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightrolemodules` (
  `RoleType` int(11) NOT NULL DEFAULT '1' COMMENT '角色类型：1=角色ID，2=用户ID',
  `RoleId` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `ModuleId` int(11) NOT NULL DEFAULT '0' COMMENT '模块ID',
  `Rights` int(11) NOT NULL DEFAULT '0' COMMENT '权限值，参考指定模块的PermDefine，位运算',
  PRIMARY KEY (`RoleType`,`RoleId`,`ModuleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightrolemodules`
--

LOCK TABLES `rightrolemodules` WRITE;
/*!40000 ALTER TABLE `rightrolemodules` DISABLE KEYS */;
INSERT INTO `rightrolemodules` VALUES (1,27,234,1),(1,27,235,1),(1,27,236,1),(1,27,237,1),(1,27,238,1),(1,27,239,1),(1,27,240,1),(1,27,241,1),(1,27,242,1),(1,27,243,1),(1,27,244,1),(1,27,245,1),(1,28,253,1),(1,28,254,1),(1,29,247,1),(1,29,248,1),(1,29,249,1),(1,29,250,1),(1,29,251,1);
/*!40000 ALTER TABLE `rightrolemodules` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightroles`
--

DROP TABLE IF EXISTS `rightroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightroles` (
  `RoleId` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `TheProj` int(11) NOT NULL DEFAULT '0' COMMENT '所属项目，关联至RightModules表一级目模块的ModuleId',
  `RoleName` varchar(50) NOT NULL DEFAULT '' COMMENT '角色名',
  `RoleDesc` varchar(500) NOT NULL DEFAULT '' COMMENT '角色描述',
  `Status` int(11) NOT NULL DEFAULT '0' COMMENT '角色状态，0=正常，1=禁用',
  PRIMARY KEY (`RoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COMMENT='权限角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightroles`
--

LOCK TABLES `rightroles` WRITE;
/*!40000 ALTER TABLE `rightroles` DISABLE KEYS */;
INSERT INTO `rightroles` VALUES (1,0,'超级管理员','超级管理员',0),(27,0,'游戏中心管理员','',0),(28,0,'更新系统管理员','',0),(29,0,'数据统计管理员','',0),(30,0,'huxiaomeng','',0);
/*!40000 ALTER TABLE `rightroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightuserroles`
--

DROP TABLE IF EXISTS `rightuserroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightuserroles` (
  `UserId` int(11) NOT NULL DEFAULT '0' COMMENT '用户ID',
  `RoleId` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  PRIMARY KEY (`UserId`,`RoleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightuserroles`
--

LOCK TABLES `rightuserroles` WRITE;
/*!40000 ALTER TABLE `rightuserroles` DISABLE KEYS */;
INSERT INTO `rightuserroles` VALUES (1,1),(6,1),(19,27),(19,28),(19,29),(39,1),(39,27),(39,28),(39,29),(40,1),(41,27),(41,28),(41,29);
/*!40000 ALTER TABLE `rightuserroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rightusers`
--

DROP TABLE IF EXISTS `rightusers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rightusers` (
  `UserId` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `TeamType` int(11) NOT NULL DEFAULT '1' COMMENT '团队类型，1=艺果，2=厂商，3=CP',
  `TeamRefId` int(11) NOT NULL DEFAULT '0' COMMENT '团队关联ID',
  `TeamName` varchar(50) NOT NULL DEFAULT '' COMMENT '团队名称',
  `TeamFlag` varchar(50) NOT NULL DEFAULT '' COMMENT '团队标识',
  `UserName` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `UserPwd` varchar(50) NOT NULL DEFAULT '' COMMENT '密码',
  `RealName` varchar(50) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `NickName` varchar(50) NOT NULL DEFAULT '' COMMENT '昵称',
  `CreateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `Status` int(11) NOT NULL DEFAULT '0' COMMENT '用户状态，0=正常，1=禁用',
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='权限用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rightusers`
--

LOCK TABLES `rightusers` WRITE;
/*!40000 ALTER TABLE `rightusers` DISABLE KEYS */;
INSERT INTO `rightusers` VALUES (1,1,0,'','','admin','596B88E6B35E6C21C6837F4D4DCDCA5F:75916','管理员','Admin','2013-07-13 14:49:36',0),(6,1,0,'','','jensenliang','9B78E8B183876489474C2329C99B5A15:44452','梁建顺','ZeroJensen','2014-08-28 20:49:34',0),(19,1,0,'','','greenyhu','09B1038FDF1FF7E2A673FB743D6753E8:15738','胡笑萌','greeny','2015-06-01 17:41:30',0),(39,1,0,'','','oddshou','68F7183065FE67174D562E1DECCEEBC9:87936','候林慧','','2016-02-01 09:26:39',0),(40,1,0,'','','lihuang','3C76799B20C013CDE8A3B8B9DABB7FFA:48009','黄李','黄李','2016-04-29 13:35:59',0),(41,1,0,'','','laiyuqing','B8B10AFB2951B76E69276AE16A582A4E:56923','赖钰清','','2016-04-29 14:21:24',0);
/*!40000 ALTER TABLE `rightusers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-05 17:27:36
