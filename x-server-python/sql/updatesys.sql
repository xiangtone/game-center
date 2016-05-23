-- MySQL dump 10.13  Distrib 5.6.29, for linux-glibc2.5 (x86_64)
--
-- Host: localhost    Database: updatesys
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
-- Table structure for table `updateinfo`
--

DROP TABLE IF EXISTS `updateinfo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `updateinfo` (
  `UpdateId` int(11) NOT NULL AUTO_INCREMENT,
  `AppId` int(11) NOT NULL,
  `SchemeId` int(11) NOT NULL DEFAULT '0',
  `PackName` varchar(50) NOT NULL,
  `ChannelNo` varchar(20) NOT NULL DEFAULT '00',
  `VerName` varchar(50) NOT NULL,
  `VerCode` int(11) NOT NULL,
  `PackUrl` varchar(200) NOT NULL,
  `PackSize` int(11) DEFAULT '0',
  `PackMD5` varchar(50) DEFAULT NULL,
  `UpdateType` int(11) NOT NULL DEFAULT '1' COMMENT '1=提示-可忽略，2=标识-小红点，3=强制-不可忽略',
  `UpdatePrompt` varchar(200) DEFAULT NULL,
  `UpdateDesc` varchar(500) NOT NULL,
  `PubTime` datetime DEFAULT NULL,
  `Status` int(11) NOT NULL DEFAULT '1' COMMENT '1=正常，2=已删除',
  `ForceUpdateVerCode` int(11) NOT NULL DEFAULT '0' COMMENT '低于此版本码，强制更新。必须大于0',
  PRIMARY KEY (`UpdateId`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `updateinfo`
--

LOCK TABLES `updateinfo` WRITE;
/*!40000 ALTER TABLE `updateinfo` DISABLE KEYS */;
INSERT INTO `updateinfo` VALUES (3,6,0,'com.hykj.gamecenter','70','6.04.08.70',11,'http://hsfs-10029187.file.myqcloud.com/M00/00/01/CmnJpFcHF4qEIcOPAAAAACbCl3c782.apk',2191146,'1FEF20595E83EFFA10E2FAA8B5A28CD4',1,'测试包','测试描述语。。。','2016-04-08 10:30:17',1,0),(4,6,0,'com.hykj.gamecenter','70','6.04.08.70',104,'http://hsfs-10029187.file.myqcloud.com/M00/00/01/CmnJpFcHJMiES6nMAAAAACpM5pE086.apk',2191390,'6CD81A0C64A1CB0B46E0063FA28C5B4F',1,'测试code103','描述语。。。。。。。','2016-04-08 11:26:39',1,0),(5,6,0,'com.hykj.gamecenter','70','6.04.18.70',140,'http://hsfs-10029187.file.myqcloud.com/M00/00/04/CmnJpFcUt6KEZDTZAAAAACH7fzk000.apk',1961553,'C920161CBC9BC2510FB5BFCFD6E3DF4C',1,'功能升级（测试）\nversion code 139','分类界面修改，包大小压缩','2016-04-18 18:33:54',1,0),(6,6,0,'com.hykj.gamecenter','70','6.04.20.70',141,'http://hsfs-10029187.file.myqcloud.com/M00/00/05/CmnJpFcW83iET0B-AAAAAMYUJTw306.apk',1962485,'666195B5F5DCE7F6E9B5D77D51292DB3',1,'界面修改','推荐搜索栏固定顶部，搜索页风格统一','2016-04-20 11:13:46',1,0);
/*!40000 ALTER TABLE `updateinfo` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-05-05 17:28:31
