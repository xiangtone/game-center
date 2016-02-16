
CREATE DATABASE /*!32312 IF NOT EXISTS*/`mas_market` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mas_market`;

DROP TABLE IF EXISTS `t_market_info`;

CREATE TABLE `t_market_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `market_num` VARCHAR(20) DEFAULT NULL COMMENT '平台编号',
  `company_name` VARCHAR(20) DEFAULT NULL COMMENT '公司名',
  `name` VARCHAR(20) NOT NULL COMMENT '平台名',
  `version` DOUBLE NOT NULL COMMENT '版本号',
  `pac_name` VARCHAR(20) NOT NULL COMMENT '包名',
  `url` VARCHAR(100) NOT NULL COMMENT '下载地址',
  `size` INT NOT NULL COMMENT '包大小',
  `city_id` INT DEFAULT 1 COMMENT '所在区代号',
  `remark` VARCHAR(30) DEFAULT NULL COMMENT '备注',
  `roll_back` INT DEFAULT 1 COMMENT '版本回滚 0否 1是',
  `state` BIT(1) NOT NULL DEFAULT b'1' COMMENT '有效值.0无效 1有效',
  `start_time` DATETIME DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='平台信息';


DROP TABLE IF EXISTS `t_app_info`;

CREATE TABLE `t_app_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '游戏编号',
  `marketInfoId` INTEGER(11) DEFAULT '0' COMMENT '对应平台',
  `password` VARCHAR(30) DEFAULT '000000' COMMENT '密码(６位加密数)',
  `channelId` INT(11) NOT NULL COMMENT '父渠道管理公司ID',
  `name` VARCHAR(100) DEFAULT NULL COMMENT '应用名',
  `cpId` INT(11) DEFAULT NULL COMMENT '游戏的厂商ID',
  `tags` VARCHAR(255) DEFAULT NULL COMMENT '标签可能会有多个，以|分割',
  `pinyin` VARCHAR(200) DEFAULT NULL,
  `free` VARCHAR(10) DEFAULT NULL COMMENT '是否免费 0免费　1收费',
  `brief` VARCHAR(100) DEFAULT NULL COMMENT '描述',
  `description` VARCHAR(1000) DEFAULT NULL COMMENT '长的介绍',
  `logo` VARCHAR(200) DEFAULT NULL COMMENT '图标',
  `bigLogo` VARCHAR(200) DEFAULT NULL COMMENT '大图',
  `stars` INT(11) DEFAULT NULL COMMENT '星级',
  `officalIcon` VARCHAR(200) DEFAULT NULL COMMENT '官网 游戏分类小图标',
  `officalScore` DOUBLE(10,2) DEFAULT '9.00' COMMENT '游戏评分',
  `officalImg` VARCHAR(200) DEFAULT NULL COMMENT '官网 游戏列表配图',
  `backgroundImg` VARCHAR(200) DEFAULT NULL COMMENT '微官网banner大图',
  `machineType` INT(11) DEFAULT NULL COMMENT '机器类型(未确定)',
  `keyword` VARCHAR(200) DEFAULT NULL COMMENT '关键字',
  `initial` VARCHAR(5) DEFAULT NULL COMMENT '首字母',
  `hasSite` TINYINT(1) DEFAULT '1' COMMENT '是否有微官网(暂时不用)',
  `updateTime` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
  `operator` VARCHAR(15) DEFAULT NULL COMMENT '操作员',
  `remark` VARCHAR(30) DEFAULT NULL COMMENT '备注',
  `state` BIT(1) NOT NULL DEFAULT b'1' COMMENT '有效值.0无效 1有效',
  `categoryId` INT(8) DEFAULT NULL COMMENT '类型编号',
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `sort` INT(8) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COMMENT='app基本信息'


DROP TABLE IF EXISTS `t_app_file`;

CREATE TABLE `t_app_file` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '游戏编号',
  `appId` INT(11) NOT NULL COMMENT 'appId',
  `channelId` INT(11) DEFAULT NULL COMMENT '子渠道id',
  `cpId` INT(11) DEFAULT NULL COMMENT 'cpid',
  `apkKey` VARCHAR(100) DEFAULT NULL COMMENT '文件key',
  `serverId` INT(11) DEFAULT NULL COMMENT '服务器id',
  `upgradeType` INT(1) DEFAULT NULL COMMENT '1不更新，2下拉框更新，3对话框更新',
  `packageName` VARCHAR(100) DEFAULT NULL COMMENT '民名',
  `versionCode` INT(11) DEFAULT NULL COMMENT '版本号',
  `versionName` VARCHAR(100) DEFAULT NULL COMMENT '版本名',
  `url` VARCHAR(200) DEFAULT NULL COMMENT '下载地址',
  `osType` INT(11) DEFAULT NULL COMMENT '操作系统类型 1,ios ２.andriod',
  `resolution` VARCHAR(100) DEFAULT 'all' COMMENT '分辨率（默认all是支持所有的，如果有单独的就查询分该辨率，以|隔开，如果查不到就弹出框）',
  `fileSize` INT(20) DEFAULT NULL COMMENT '大小',
  `cpChannelCode` VARCHAR(100) DEFAULT NULL COMMENT '游戏厂商分配的游戏的渠道号',
  `updateInfo` VARCHAR(500) DEFAULT NULL COMMENT '游戏更新信息',
  `language` INT(11) DEFAULT 1 COMMENT '语言 1,中文 2,英语 3,其他',
  `remark` VARCHAR(30) DEFAULT NULL COMMENT '备注',
  `state` BIT(1) NOT NULL DEFAULT b'1' COMMENT '有效值.0无效 1有效',
  `updateTime` TIMESTAMP NULL DEFAULT NULL COMMENT '更新时间',
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`appId`),
  UNIQUE KEY `unique_apkKey` (`apkKey`),
  UNIQUE KEY `unique_appid_channelid` (`appId`,`channelId`),
  UNIQUE KEY `unique_package` (`channelId`,`packageName`)
) ENGINE=INNODB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT ='app对应文件信息';

/*Table structure for table `app_picture` */

DROP TABLE IF EXISTS `t_app_picture`;

CREATE TABLE `t_app_picture` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `appId` INT(11) NOT NULL COMMENT '应用&游戏编号',
  `url` VARCHAR(500) NOT NULL COMMENT '资源地址',
  `length` INT(11) DEFAULT NULL COMMENT '图片长度',
  `width` VARCHAR(20) DEFAULT NULL COMMENT '图片尺寸',
  `fileSize` INT(11) DEFAULT NULL COMMENT '图片大小(byte)',
  `thumbnailUrl` VARCHAR(500) DEFAULT NULL COMMENT '缩略图地址',
  `title` VARCHAR(100) DEFAULT NULL COMMENT '标题',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '长文字描述',
  `fileType` INT(11) DEFAULT 1 COMMENT '图片类型 1.jpg 2.gif',
  `state` BIT(1) NOT NULL,
  `sort` INT(8) DEFAULT '0' COMMENT '排序值',
  `updateTime` DATETIME DEFAULT NULL,
  `operator` VARCHAR(15) DEFAULT NULL,
  `remark` VARCHAR(30) DEFAULT NULL,
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT ='app对应图片';

/*Data for the table `app_picture` */

DROP TABLE IF EXISTS `t_app_album`;

CREATE TABLE `t_app_album` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) DEFAULT NULL COMMENT '专题名称',
  `sort` INT(11) DEFAULT '1' COMMENT '排序（按数字大小从大到小排序）',
  `state` BIT(1) DEFAULT NULL COMMENT '状态',
  `createTime` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
  `operator` VARCHAR(30) DEFAULT NULL COMMENT '后台操作人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT ='app对应专辑';

/*Data for the table `app_album` */

DROP TABLE IF EXISTS `t_app_album_column`;
CREATE TABLE `t_app_album_column` (
  `columnId` INT(11) NOT NULL,
  `albumId` INT(11) DEFAULT NULL COMMENT '大类别Id',
  `name` VARCHAR(100) DEFAULT NULL COMMENT '页签名称',
  `nameCn` VARCHAR(100) DEFAULT NULL COMMENT '页签中文名称',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '页签的小图标（列表显示）',
  `bigicon` VARCHAR(100) DEFAULT NULL COMMENT '专题大图（页签详情时要显示）',
  `description` VARCHAR(30) DEFAULT NULL COMMENT '页签描述',
  `sort` INT(11) DEFAULT '1' COMMENT '排序（按数字大小从大到小排序）',
  `state` BIT(1) DEFAULT b'1' COMMENT '状态',
  `flag` INT(11) DEFAULT '1' COMMENT '标识',
  `createTime` TIMESTAMP NULL DEFAULT NULL COMMENT '创建时间',
  `operator` VARCHAR(30) DEFAULT NULL COMMENT '后台操作人',
  PRIMARY KEY (`columnId`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='app对应专辑'

/*Table structure for table `app_album_res` */

DROP TABLE IF EXISTS `t_app_album_res`;

CREATE TABLE `t_app_album_res` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `raveId` INT(11) DEFAULT NULL COMMENT '平台Id',
  `albumId` INT(11) DEFAULT NULL COMMENT '大类别Id',
  `columnId` INT(11) DEFAULT NULL COMMENT '页签Id',
  `categoryId` INT(11) DEFAULT NULL COMMENT '分类id',
  `sort` INT(11) DEFAULT '0' COMMENT '排序（按数字大小从大到小排序）',
  `appId` INT(11) DEFAULT NULL COMMENT '对应的t_app_info的Id',
  `appName` VARCHAR(100) DEFAULT NULL COMMENT 'app应用名称',
  `free` INT(11) DEFAULT NULL COMMENT '0公共资源1平台2自运营',
  `logo` VARCHAR(100) DEFAULT NULL COMMENT '图标',
  `bigLogo` VARCHAR(100) DEFAULT NULL COMMENT '大图标',
  `brief` VARCHAR(100) DEFAULT NULL COMMENT '简介',
  `description` VARCHAR(5000) DEFAULT NULL COMMENT '详细长描述',
  `stars` INT(100) DEFAULT NULL COMMENT '星星评级（1-10）每一个表示半颗星',
  `apkId` INT(11) DEFAULT NULL COMMENT '对应的t_app_file的Id',
  `fileSize` INT(11) DEFAULT NULL COMMENT 'apk的文件大小',
  `packageName` VARCHAR(100) DEFAULT NULL COMMENT '包名',
  `versionCode` INT(11) DEFAULT NULL COMMENT '版本号',
  `versionName` VARCHAR(100) DEFAULT NULL COMMENT '版本名',
  `url` VARCHAR(200) DEFAULT NULL COMMENT 'apk下载地址',
  `initDowdload` INT(11) DEFAULT '0' COMMENT '初始下载数（初始5万到10万）',
  `realDowdload` INT(11) DEFAULT '0' COMMENT '真正下载数',
  `operator` VARCHAR(30) DEFAULT NULL COMMENT '后台操作人',
  `createTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`albumId`,`appId`,`apkId`)
) ENGINE=INNODB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8 COMMENT ='app专辑';

/*Data for the table `app_album_res` */

/*Table structure for table `app_ad` */

DROP TABLE IF EXISTS `t_app_album_theme`;

CREATE TABLE `t_app_album_theme` (
  `themeId` INT(11) NOT NULL AUTO_INCREMENT,
  `albumId` INT(11) DEFAULT NULL COMMENT '大类别Id',
  `name` VARCHAR(100) DEFAULT NULL COMMENT '主题名称',
  `nameCn` VARCHAR(100) DEFAULT NULL COMMENT '主题中文名称',
  `icon` VARCHAR(100) DEFAULT NULL COMMENT '主题的小图标（列表显示）',
  `bigicon` VARCHAR(100) DEFAULT NULL COMMENT '主题大图（页签详情时要显示）',
  `description` VARCHAR(30) DEFAULT NULL COMMENT '主题描述',
  `sort` INT(11) DEFAULT '1' COMMENT '排序（按数字大小从大到小排序）',
  `state` BIT(1) DEFAULT b'1' COMMENT '状态',
  `flag` INT(11) DEFAULT '1' COMMENT '标识1表示资源、2表示列表',
  `apkId` INT(11) DEFAULT NULL COMMENT 'flag为1时对应的t_app_file的Id，2时为空或0',
  `createTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` TIMESTAMP NULL DEFAULT NULL,
  `operator` VARCHAR(30) DEFAULT NULL COMMENT '后台操作人',
  PRIMARY KEY (`themeId`)
) ENGINE=MYISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='app对应主题'


/*Data for the table `t_app_ad` */

/*Table structure for table `app_server` */

DROP TABLE IF EXISTS `t_app_server`;

CREATE TABLE `t_app_server` (
  `id` VARCHAR(50) NOT NULL,
  `appId` INT(8) DEFAULT NULL,
  `serverName` VARCHAR(30) DEFAULT NULL COMMENT 'server中文名',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `categoryId` INT(4) DEFAULT NULL COMMENT '类型编号',
  `categoryCn` VARCHAR(20) DEFAULT NULL COMMENT '类型中文名',
  `remark` VARCHAR(50) DEFAULT NULL,
  `operator` VARCHAR(30) DEFAULT NULL,
  `updateTime` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `createTime` DATETIME DEFAULT NULL
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT='app对应服务器分区信息';

/*Data for the table `app_server` */

INSERT  INTO `t_app_server`(`id`,`appId`,`serverName`,`description`,`categoryId`,`categoryCn`,`remark`,`operator`,`updateTime`,`createTime`) VALUES ('2',60,'广州一区','测试数据',1,'001','001','001','2013-03-28 15:03:37','2013-03-18 12:04:13'),('1',60,'广州二区','测试2',2,'002','002','002','2013-04-12 11:18:26','2013-03-18 12:03:44'),('wly-86-0755-sz-zhidiansj-001',62,'卧龙吟','联运卧龙吟',1,'003','003','003','2013-04-26 18:35:41','2013-04-26 18:35:41'),('apk:6',81,'6区 忠肝义胆',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:40:57',NULL),('apk:7',81,'7区 天下无双',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:04',NULL),('apk:8',81,'8区 谁与争锋',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:12',NULL),('apk:5',81,'5区 义薄云天',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:18',NULL),('apk:4',81,'4区 武林争霸',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:26',NULL),('apk:3',81,'3区 逼上梁山',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:32',NULL),('apk:2',81,'2区 替天行道',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:40',NULL),('apk:1',81,'1区 群雄聚义',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:47',NULL),('apk:9',81,'9区 战无不胜',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:41:55',NULL),('apk:10',81,'10区 结义金兰',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:01',NULL),('apk:11',81,'11区 江山美人',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:07',NULL),('apk:12',81,'12区 威震天下',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:14',NULL),('apk:13',81,'13区 凤舞九天',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:19',NULL),('apk:14',81,'14区 龙啸九州',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:50','2013-05-27 14:40:27'),('apk:15',81,'15区 十面埋伏',NULL,NULL,NULL,NULL,NULL,'2013-05-27 14:42:33','2013-05-27 14:40:24'),('apk:16',81,'16区 保国安民',NULL,NULL,NULL,NULL,NULL,'2013-05-29 12:38:23','2013-05-29 12:38:23'),('apk:17',81,'17区 君临天下',NULL,NULL,NULL,NULL,NULL,'2013-05-31 10:26:03','2013-05-31 10:26:03'),('apk:18',81,'18区 天魁星',NULL,NULL,NULL,NULL,NULL,'2013-06-03 10:08:00','2013-06-03 10:08:00'),('apk:19',81,'19区 天罡星',NULL,NULL,NULL,NULL,NULL,'2013-06-04 12:47:17','2013-06-04 12:47:17'),('apk:20',81,'20区天机星','',NULL,NULL,'',NULL,'2013-06-18 10:00:13','2013-06-18 09:37:58'),('apk:21',81,'21区天闲星','',NULL,NULL,'',NULL,'2013-06-18 10:00:33','2013-06-18 09:38:46'),('apk:22',81,'22区天勇星','',NULL,NULL,'',NULL,'2013-06-18 10:00:49','2013-06-18 09:39:05'),('apk:23',81,'23区天雄星','',NULL,NULL,'',NULL,'2013-06-18 10:01:12','2013-06-18 09:39:30'),('apk:24',81,'24区天猛星','',NULL,NULL,'',NULL,'2013-06-18 10:01:33','2013-06-18 09:39:56'),('apk:25',81,'25区天威星','',NULL,NULL,'',NULL,'2013-06-18 10:01:45','2013-06-18 09:40:13'),('apk:26',81,'26区','',NULL,NULL,'',NULL,NULL,'2013-06-18 09:40:27'),('apk:27',81,'27区','',NULL,NULL,'',NULL,NULL,'2013-06-18 09:40:50'),('apk:28',81,'28区','',NULL,NULL,'',NULL,NULL,'2013-06-18 09:41:12'),('apk:29',81,'29区','',NULL,NULL,'',NULL,NULL,'2013-06-18 09:41:36'),('apk:30',81,'30区','',NULL,NULL,'',NULL,NULL,'2013-06-18 09:41:55');

/*Table structure for table `category` */

DROP TABLE IF EXISTS `t_category`;

CREATE TABLE `t_category` (
  `id` INT(8) NOT NULL AUTO_INCREMENT COMMENT '类型编号(命名规则需要整理成文档',
  `marketInfoId` INT(11) DEFAULT '1' COMMENT '对应平台',
  `name` VARCHAR(30) NOT NULL COMMENT '类型名',
  `categoryCn` VARCHAR(50) DEFAULT NULL COMMENT '类型中文名',
  `fatherId` INT(8) DEFAULT NULL COMMENT '父类编号(默认0)',
  `你` VARCHAR(100) DEFAULT NULL COMMENT '图标',
  `bigicon` VARCHAR(100) DEFAULT NULL COMMENT '大图标',
  `recommend` VARCHAR(100) DEFAULT NULL COMMENT '推荐的应用',
  `sort` INT(11) DEFAULT '0' COMMENT '排序值',
  `state` BIT(1) DEFAULT b'1' COMMENT '状态',
  `createTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` TIMESTAMP NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`) USING BTREE
) ENGINE=MYISAM AUTO_INCREMENT=56 DEFAULT CHARSET=utf8 COMMENT='app对应分类信息'


/*Data for the table `category` */

INSERT  INTO `t_category`(`id`,`name`,`categoryCn`,`fatherId`,`icon`,`pinyin`,`remark`,`sort`,`state`,`flag`,`createTime`,`updateTime`) VALUES (1,'platform','平台',0,'0',NULL,NULL,NULL,NULL,0,'2012-12-19 10:44:06','2012-12-19 10:44:18'),(11,'app','游戏',1,NULL,NULL,NULL,NULL,NULL,0,NULL,'2012-12-26 10:41:11'),(12,'music','音乐',1,NULL,NULL,NULL,NULL,NULL,0,NULL,'2012-12-23 16:44:06'),(13,'book','小说',1,NULL,NULL,NULL,NULL,NULL,0,NULL,'2012-12-23 16:45:04'),(14,'video','视频',1,NULL,NULL,NULL,NULL,NULL,0,NULL,'2012-12-23 16:45:02'),(15,'image','图片',1,NULL,NULL,NULL,NULL,NULL,0,NULL,'2012-12-25 15:33:38'),(16,'游戏','精选',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 06:21:54','2012-12-26 10:59:05'),(17,'游戏','网游',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:21:54','2013-01-14 18:23:09'),(18,'游戏','益智',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:23:51','2013-01-14 18:23:10'),(19,'游戏','竞技',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:24:04','2013-01-14 18:23:10'),(20,'游戏','竞速',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:24:49','2013-01-14 18:23:11'),(21,'游戏','策略',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:25:05','2013-01-14 18:23:12'),(22,'游戏','经营',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:25:21','2013-01-14 18:23:13'),(23,'游戏','角色',11,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:25:33','2013-01-14 18:23:16'),(24,'音乐','排行',12,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:25:51','2013-01-14 18:23:26'),(25,'音乐','网络',12,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:26:06','2013-01-14 18:23:26'),(26,'音乐','流行',12,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:26:19','2013-01-14 18:23:27'),(27,'音乐','伤感',12,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:26:37','2013-01-14 18:23:29'),(28,'小说','玄幻',13,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:27:10','2013-01-14 18:23:37'),(29,'小说','言情',13,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:27:22','2013-01-14 18:23:37'),(30,'小说','历史',13,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:27:31','2013-01-14 18:23:38'),(31,'小说','推理',13,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:27:41','2013-01-14 18:23:41'),(32,'视频','热门',14,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:28:46','2013-01-14 18:23:45'),(33,'视频','电影',14,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:28:57','2013-01-14 18:23:49'),(34,'视频','电视',14,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:29:08','2013-01-14 18:23:50'),(35,'视频','MTV',14,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:29:16','2013-01-14 18:23:53'),(36,'图片','高清',15,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:29:42','2013-01-14 18:23:57'),(37,'图片','美女',15,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:29:53','2013-01-14 18:24:01'),(38,'图片','来电',15,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:30:03','2013-01-14 18:24:02'),(39,'图片','爱情',15,NULL,NULL,NULL,NULL,'',NULL,'2012-12-25 18:30:13','2013-01-14 18:24:05');

/*Table structure for table `channel_info` */

DROP TABLE IF EXISTS `t_channel_info`;

CREATE TABLE `t_channel_info` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '渠道管理公司ID',
  `fatherId` INT(11) NOT NULL COMMENT '父渠道管理公司ID',
  `fatherName` VARCHAR(100) DEFAULT NULL,
  `password` VARCHAR(30) DEFAULT '000000' COMMENT '密码(６位加密数)',
  `name` VARCHAR(100) NOT NULL COMMENT '渠道管理公司',
  `type` INT(11) DEFAULT NULL COMMENT '渠道类型（1：运营  2：市场）',
  `contacter` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `phone` VARCHAR(15) DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '联系地址',
  `provinceId` INTEGER(11) NOT NULL COMMENT '省',
  `description` VARCHAR(1000) DEFAULT NULL COMMENT '简介说明',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `state` BIT(1) NOT NULL DEFAULT b'1' COMMENT '是否有效',
  `sort` INT(11) DEFAULT NULL COMMENT '排序',
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `operator` VARCHAR(15) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=MYISAM AUTO_INCREMENT=10114 DEFAULT CHARSET=utf8 COMMENT='channelAdmin由系统用户手动增加进去';

/*Data for the table `channel_info` */

INSERT  INTO `t_channel_info`(`id`,`fatherId`,`fatherName`,`name`,`type`,`accout`,`password`,`contacter`,`phone`,`email`,`address`,`province`,`city`,`region`,`description`,`remark`,`state`,`sort`,`createTime`,`operator`) VALUES (1,-1,NULL,'联运渠道管理',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'',NULL,'2013-05-02 19:28:42',NULL),(14,10032,'测试总渠道','豌豆荚',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-22 10:21:23',NULL),(21,10032,'测试总渠道','广州合作商',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','佛山','佛山市','','','',NULL,'2013-05-24 18:55:32',NULL),(10000,10111,'社区渠道','公共渠道(卧龙吟)',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-07-11 11:33:42',NULL),(10001,10037,'admob','admob',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','广州','广州市','','','',NULL,'2013-05-24 18:56:22',NULL),(10002,10038,'3G门户','3G门户',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','广州','广州市','','','',NULL,'2013-05-24 18:56:37',NULL),(10003,10039,'懒人听书','懒人听书',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','广州','广州市','','','',NULL,'2013-05-24 18:56:57',NULL),(10004,10040,'商易神州','商易神州',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','安庆','安庆市','','','',NULL,'2013-05-24 18:57:08',NULL),(10005,10041,'智汇云','智汇云',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-29 17:30:09',NULL),(10006,10042,'龚志其','龚志其',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-05-24 18:57:36',NULL),(10007,10043,'黄文花','黄文花',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','白银','白银市','','','',NULL,'2013-05-24 18:57:53',NULL),(10008,10042,'龚志其','龚志其2',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','安庆','安庆市','','','',NULL,'2013-05-24 18:57:42',NULL),(10009,10045,'梦游市场','梦游市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','蚌埠市','','','',NULL,'2013-05-24 18:58:08',NULL),(10010,10046,'应用酷','应用酷',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 18:58:26',NULL),(10011,10047,'盛大在线','盛大在线',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 18:58:42',NULL),(10012,10048,'闪播网','闪播网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 18:59:42',NULL),(10013,10049,'泡椒网','泡椒网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 19:00:00',NULL),(10014,10050,'春庭月','春庭月',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 19:00:14',NULL),(10015,10051,'安软市场','安软市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 19:00:30',NULL),(10016,10052,'安粉市场','安粉市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-05-24 19:00:48',NULL),(10017,10053,'wifikey','wifikey',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 19:02:02',NULL),(10018,10054,'G媒','G媒',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','固镇县','','','',NULL,'2013-05-24 19:02:21',NULL),(10019,10055,'湖北联通','湖北联通',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','蚌埠市','','','',NULL,'2013-05-24 19:02:45',NULL),(10020,10034,'天涯','天涯',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 18:55:51',NULL),(10021,1,'联运渠道管理','欧亿时空',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 17:56:43',NULL),(10022,1,'联运渠道管理','友盟',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','安庆','怀宁县','','','',NULL,'2013-05-24 18:54:19',NULL),(10023,10036,'公共渠道','多盟',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建瓯市','','','\0',NULL,'2013-07-11 11:35:37',NULL),(10024,1,'联运渠道管理','亿动',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 17:57:46',NULL),(10025,1,'联运渠道管理','inmobi',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 18:55:01',NULL),(10026,10021,'优推','欧亿时空',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','朝阳区','','','',NULL,'2013-07-01 13:36:57',NULL),(10027,10022,'友盟','友盟',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','东城区','','','',NULL,'2013-07-01 13:37:06',NULL),(10028,10024,'亿动','亿动',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 17:58:07',NULL),(10029,10111,'社区渠道','多盟(YY水浒)',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-07-11 11:34:09',NULL),(10030,10080,'安智','安智',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 17:59:35',NULL),(10032,1,'联运渠道管理','测试总渠道',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','深圳','深圳市','供测试使用','','',NULL,'2013-05-24 18:55:22',NULL),(10034,1,'联运渠道管理','天涯',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-05-24 18:55:44',NULL),(10036,1,'联运渠道管理','公共渠道',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','固镇县','','','\0',NULL,'2013-07-11 11:35:44',NULL),(10037,1,'联运渠道管理','admob',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','固镇县','','','',NULL,'2013-05-24 18:56:12',NULL),(10038,1,'联运渠道管理','3G门户',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','安徽','蚌埠','怀远县','','','',NULL,'2013-05-24 18:56:31',NULL),(10039,1,'联运渠道管理','懒人听书',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 18:56:51',NULL),(10040,1,'联运渠道管理','商易神州',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-05-24 18:57:02',NULL),(10041,1,'联运渠道管理','智汇云',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','定西县','','','',NULL,'2013-05-24 18:57:17',NULL),(10042,1,'联运渠道管理','龚志其',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','东莞','东莞市','','','',NULL,'2013-05-24 18:57:31',NULL),(10043,1,'联运渠道管理','黄文花',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建阳市','','','',NULL,'2013-05-24 18:57:48',NULL),(10045,1,'联运渠道管理','梦游市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 18:58:03',NULL),(10046,1,'联运渠道管理','应用酷',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','河源','和平县','','','',NULL,'2013-05-24 18:58:20',NULL),(10047,1,'联运渠道管理','盛大在线',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','广州','广州市','','','',NULL,'2013-05-24 18:58:37',NULL),(10048,1,'联运渠道管理','闪播网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','朝阳区','','','',NULL,'2013-05-24 18:59:35',NULL),(10049,1,'联运渠道管理','泡椒网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','东莞','东莞市','','','',NULL,'2013-05-24 18:59:54',NULL),(10050,1,'联运渠道管理','春庭月',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','广州','广州市','','','',NULL,'2013-05-24 19:00:09',NULL),(10051,1,'联运渠道管理','安软市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建瓯市','','','',NULL,'2013-05-24 19:00:22',NULL),(10052,1,'联运渠道管理','安粉市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','定西县','','','',NULL,'2013-05-24 19:00:42',NULL),(10053,1,'联运渠道管理','wifikey',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建阳市','','','',NULL,'2013-05-24 19:01:54',NULL),(10054,1,'联运渠道管理','G媒',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-24 19:02:12',NULL),(10055,1,'联运渠道管理','湖北联通',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','佛山','佛山市','','','',NULL,'2013-05-24 19:02:36',NULL),(10056,1,'联运渠道管理','安卓市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','佛山','佛山市','','','',NULL,'2013-05-24 19:02:52',NULL),(10057,1,'联运渠道管理','360手机助手',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 19:03:08',NULL),(10058,1,'联运渠道管理','google play',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-28 11:46:01',NULL),(10059,1,'联运渠道管理','91手机助手',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','贵州','贵阳','清镇市','','','',NULL,'2013-05-24 19:03:38',NULL),(10060,1,'联运渠道管理','应用汇',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','福州','长乐市','','','',NULL,'2013-05-24 19:03:56',NULL),(10061,1,'联运渠道管理','豌豆荚',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 19:04:12',NULL),(10062,1,'联运渠道管理','机锋市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-24 19:04:28',NULL),(10063,1,'联运渠道管理','N多网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','东莞','东莞市','','','',NULL,'2013-05-24 19:04:43',NULL),(10064,1,'联运渠道管理','优亿市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-05-24 19:05:08',NULL),(10065,1,'联运渠道管理','木蚂蚁',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','广东','东莞','东莞市','','','',NULL,'2013-05-24 18:59:15',NULL),(10066,10056,'安卓市场','安卓市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','定西县','','','',NULL,'2013-05-24 19:03:00',NULL),(10067,10057,'360手机助手','360手机助手',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建阳市','','','',NULL,'2013-05-24 19:03:16',NULL),(10068,10058,'google play','google play',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-28 11:46:18',NULL),(10069,10059,'91手机助手','91手机助手',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-24 19:03:45',NULL),(10070,10060,'应用汇','应用汇',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','陇西县','','','',NULL,'2013-05-24 19:04:03',NULL),(10071,10061,'豌豆荚','豌豆荚',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','连城县','','','',NULL,'2013-05-24 19:04:19',NULL),(10072,10062,'机锋市场','机锋市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 19:04:35',NULL),(10073,10063,'N多网','N多网',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','临洮县','','','',NULL,'2013-05-24 19:04:58',NULL),(10074,10064,'优亿市场','优亿市场',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建阳市','','','',NULL,'2013-05-24 19:05:15',NULL),(10075,10065,'木蚂蚁','木蚂蚁',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','龙岩','长汀县','','','',NULL,'2013-05-24 18:59:24',NULL),(10076,1,'联运渠道管理','优亿',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','甘南藏族自治州','合作市','','','',NULL,'2013-05-24 17:55:20',NULL),(10077,1,'联运渠道管理','应用宝',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','定西','临洮县','','','',NULL,'2013-05-28 11:44:35',NULL),(10078,10076,'优亿','优亿',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','甘肃','白银','白银市','','','',NULL,'2013-05-24 17:55:51',NULL),(10079,1,'联运渠道管理','品致传媒',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 17:52:04',NULL),(10080,1,'联运渠道管理','安智',1,'123456','e10adc3949ba59abbe56e057f20f883e','','','','','福建','南平','建阳市','','','',NULL,'2013-05-24 17:59:15',NULL),(10081,10080,'出门儿','出门儿',1,'','','','','','','广东','佛山','佛山市','','','\0',NULL,'2013-05-24 18:00:19',NULL),(10099,10038,'3G门户','3G门户-YY水浒',1,'193948','afe7df235ebd0b852e97ada6f8aa50e6','','','','','北京','北京市','海淀区','','','',NULL,'2013-05-24 17:37:56',NULL),(10100,10025,'inmobi','inmobi',1,'737234','cb84995b46359fc6cda9281682ace357','','','','','北京','北京市','西城区','','','',NULL,'2013-05-24 17:38:20',NULL),(10101,10079,'品致传媒','品致传媒',1,'586691','a038c6f92790d5aa2ba13a2f2e8c5b9e','','','','','甘肃','定西','临洮县','','','',NULL,'2013-05-24 17:53:14',NULL),(10102,10077,'应用宝','应用宝',1,'448971','88131454acc9724e7a532670ab932abc','','','','','甘肃','甘南藏族自治州','合作市','','','',NULL,'2013-05-28 11:44:50',NULL),(10103,10077,'应用宝','应用宝',1,'783750','6aa70746e7dbe95a5d73c7caa6606871','','','','','福建','龙岩','连城县','','','\0',NULL,'2013-07-04 11:44:42',NULL),(10104,10058,'应用宝','应用宝',1,'941317','934c72d1677fee59d7d50d459fee7b2c','','','','','甘肃','甘南藏族自治州','迭部县','','','\0',NULL,'2013-05-27 15:00:03',NULL),(10105,1,'联运渠道管理','移通网络',1,'411123','0edaab5518a77c6fc0e8ec7feef39321','','','','','甘肃','甘南藏族自治州','合作市','','','',NULL,'2013-05-29 14:04:29',NULL),(10106,10105,'移通网络','移通网络',1,'766514','9ce32b9ee5de8f3eda789699348da729','','','','','广西','防城港','防城港市','','','',NULL,'2013-05-29 14:04:44',NULL),(10107,1,'联运渠道管理','tapjoy',1,'910313','47840ccd9990c7b9c40a7f4c69c99cfc','','','','','福建','南平','建瓯市','','','',NULL,'2013-05-29 14:03:54',NULL),(10108,10107,'tapjoy','tapjoy ',1,'233825','f2616f58bfe8752475a144c58966ac84','','','','','甘肃','定西','陇西县','','','',NULL,'2013-05-29 14:04:11',NULL),(10109,1,'联运渠道管理','优推',1,'985075','ef95e05f73752a6549882e1606b93aa8','','','','','甘肃','甘南藏族自治州','碌曲县','','','',NULL,'2013-05-29 14:35:57',NULL),(10110,10109,'优推','优推',1,'708864','aabd0fbc15b538a97bca7a8441099eca','','','','','福建','宁德','宁德市','','','',NULL,'2013-05-29 14:36:12',NULL),(10111,1,'联运渠道管理','社区渠道',1,'888971','d22183ef0398fa290da09213fef4d44c','','','','','广东','深圳','请选择区县','','','',NULL,'2013-07-04 10:36:56',NULL),(10112,10111,'社区渠道','社区公共渠道',1,'695878','c722f3ed8ccebf63a140ea967647caff','','','','','广东','深圳','深圳市','','','',NULL,'2013-07-04 11:54:11',NULL),(10113,10111,'社区渠道','web方式渠道',1,'808505','18bf18f64dc7b02fe722c19b10582bba','','','','','广东','深圳','深圳市','','','',NULL,'2013-07-04 17:40:23',NULL);

/*Table structure for table `cp` */

DROP TABLE IF EXISTS `t_cp`;

CREATE TABLE `t_cp` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) DEFAULT NULL COMMENT '游戏厂商名称',
  `password` VARCHAR(30) DEFAULT '000000' COMMENT '密码(６位加密数)',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
  `backendAccount` VARCHAR(100) DEFAULT NULL COMMENT '后台帐号',
  `backendUrl` VARCHAR(100) DEFAULT NULL COMMENT 'cp后台地址',
  `backendPassword` VARCHAR(100) DEFAULT NULL COMMENT 'cp后台密码',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `phoneNum` VARCHAR(50) DEFAULT NULL COMMENT '联系电话',
  `qq` VARCHAR(30) DEFAULT NULL,
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱地址',
  `contact` VARCHAR(100) DEFAULT NULL COMMENT '联系人',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注',
  `state` BIT(1) NOT NULL DEFAULT b'1' COMMENT '是否有效',
  `payWay` VARCHAR(12) DEFAULT NULL COMMENT 'list对象，返回支付方式列表',
  `createTime` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` DATETIME DEFAULT NULL,
  `priKey` VARCHAR(200) DEFAULT NULL COMMENT '私钥',
  `n` VARCHAR(200) DEFAULT NULL COMMENT '随机数',
  `callbackUrl` VARCHAR(200) DEFAULT NULL,
  `pkey` VARCHAR(255) DEFAULT NULL COMMENT '公钥',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8 COMMENT ='co信息';

/*Data for the table `cp` */

INSERT  INTO `t_cp`(`id`,`name`,`description`,`backendAccount`,`backendUrl`,`backendPassword`,`address`,`phoneNum`,`qq`,`email`,`contact`,`remark`,`state`,`payWay`,`createTime`,`updateTime`,`priKey`,`n`,`callbackUrl`,`pkey`) VALUES (15,'A厂商','很好很强大很好很强大',NULL,'http://www.uqee.com/mobilepay/notify/t/zhidian',NULL,'深圳南山科技园','1234837439',NULL,'aaa@163.com','abc','就是这样好很好很强大很好很强大',1,NULL,'2012-12-21 14:05:01',NULL,'91090858208819549012266930713465297897','117786486491984797064738672854520985031','http://192.168.1.13:8080/lyPayDemo/servlet/CallBack','17331935507719291673'),(19,'23434455','','','','','','','','','','',1,'','2013-03-28 15:23:30',NULL,'91090858208819549012266930713465297897','117786486491984797064738672854520985031','http://192.168.1.13:8080/lyPayDemo/servlet/CallBack','17331935507719291673'),(1001,'上海游奇科技有限公司','','','http://www.uqee.com/mobilepay/notify/t/zhidian','','','','2318877512','','毛翔宇','',1,'','2013-04-22 13:49:13',NULL,'91090858208819549012266930713465297897','117786486491984797064738672854520985031','http://www.uqee.com/mobilepay/notify/t/zhidian','17331935507719291673'),(1002,'深圳天拓立方网络有限公司','','','http://apk.pengyougame.com/api/zhidian/pay_result_notify_process','','','','524188907','','金建文','',1,'','2013-04-22 14:02:52',NULL,'91090858208819549012266930713465297897','117786486491984797064738672854520985031','http://apk.pengyougame.com/api/zhidian/pay_result_notify_process','17331935507719291673'),(1003,'UC','','','','','','','','','','',1,'','2013-04-22 19:45:13',NULL,'',NULL,NULL,NULL);

/*Table structure for table `pay_indomog` */

DROP TABLE IF EXISTS `t_pay_indomog`;

CREATE TABLE `t_pay_indomog` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `mogValue` INT(11) DEFAULT '0' COMMENT 'mogValue面额',
  `channelId` INT(11) DEFAULT '0' COMMENT '渠道Id',
  `appId` INT(11) DEFAULT '0' COMMENT 'appId',
  `cpId` INT(11) DEFAULT '0' COMMENT 'cpId',
  `aValuePresent` INT(11) DEFAULT '0' COMMENT 'indomog充值赠送a币',
  `remark` VARCHAR(50) DEFAULT NULL COMMENT '备注',
  `state` BIT(1) DEFAULT b'1' COMMENT '状态',
  `createTime` DATETIME DEFAULT NULL COMMENT '创建时间',
  `updateTime` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `operator` VARCHAR(30) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=MYISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='indomog充值赠送a币';

/*Data for the table `pay_indomog` */

INSERT  INTO `t_pay_indomog`(`id`,`mogValue`,`channelId`,`cpId`,`appId`,`aValuePresent`,`remark`,`state`,`createTime`,`updateTime`,`operator`) VALUES (1,20000,0,0,0,2,NULL,'','2013-12-19 13:18:15','2013-12-20 13:16:05',NULL),(2,50000,0,0,0,6,NULL,'','2013-12-19 13:18:29','2013-12-20 13:16:17',NULL),(3,100000,0,0,0,13,NULL,'','2013-12-19 13:18:39','2013-12-20 13:17:05',NULL),(4,200000,0,0,0,28,NULL,'','2013-12-19 13:18:43','2013-12-20 13:17:24',NULL),(5,500000,0,0,0,60,NULL,'','2013-12-19 13:18:47','2013-12-20 13:18:08',NULL),(6,1000000,0,0,0,115,NULL,'','2013-12-19 13:18:51','2013-12-20 13:18:15',NULL);


DROP TABLE IF EXISTS `t_app_comment`;

CREATE TABLE `t_app_comment` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `appId` INT NOT NULL COMMENT 'app信息',
  `name` VARCHAR(20) DEFAULT '匿名' COMMENT '用户名',
  `type` INT DEFAULT 1 COMMENT '１手机　2平板 3其他',
  `phoneType` VARCHAR(20) DEFAULT NULL COMMENT '手机型号',
  `content` VARCHAR(100) DEFAULT NULL COMMENT '内容',
  `step` INT DEFAULT 0 COMMENT '顺序',
  `state` BIT(1) DEFAULT NULL COMMENT '是否显示评论０不显示 1显示',
  `createTime` DATETIME DEFAULT NULL COMMENT '评论时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='app对应评论';



DROP TABLE IF EXISTS `t_province`;

CREATE TABLE `t_province` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL COMMENT '省份名',
  `desc` VARCHAR(30) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='省份信息';

DROP TABLE IF EXISTS `t_city`;

CREATE TABLE `t_city` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `pId` INT NOT NULL COMMENT '城市代号',
  `name` VARCHAR(20) NOT NULL COMMENT '区域名',
  `desc` VARCHAR(30) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='区域信息';

DROP TABLE IF EXISTS `t_app_channel`;

CREATE TABLE `t_app_channel` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `apkId` INT NOT NULL COMMENT 'apk信息',
  `channelId` INT NOT NULL COMMENT '渠道号',
  `status` INT DEFAULT 0 COMMENT '渠道状态０关闭 1打开',
  `create_time` DATETIME DEFAULT NULL COMMENT '创建时间',
   `up_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='apk对应渠道信息(备用)';


DROP TABLE IF EXISTS `t_cp_channel`;

CREATE TABLE `t_cp_channel` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `channelId` INT NOT NULL COMMENT '渠道号',
  `cp_id` INT NOT NULL COMMENT 'cp信息',
  `status` INT DEFAULT 0 COMMENT '渠道状态０关闭 1打开',
  `createTime` DATETIME DEFAULT NULL COMMENT '创建时间',
   `up_time` DATETIME DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='cp对应渠道信息(备用)';

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `type_id` INT(11) DEFAULT NULL COMMENT '用户类型 对应t_user_role',
  `code` VARCHAR(30) DEFAULT NULL COMMENT '对应代码',
  `name` VARCHAR(30) DEFAULT NULL COMMENT '菜单名',
  `icon` VARCHAR(50) DEFAULT NULL,
  `seq` INT(11) DEFAULT NULL,
  `uri` VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='菜单信息';

/*Data for the table `t_menu` */

INSERT  INTO `t_menu`(`id`,`type_id`,`code`,`name`,`icon`,`seq`,`uri`) VALUES (5,5,'apkInfo','应用管理',NULL,5,'apkInfo/list'),(6,5,'role','角色管理','',2,'role/list'),(7,5,'user','用户管理','',3,'user/list'),(8,5,'phone','手机管理',NULL,4,'phone/list'),(9,5,'log','统计',NULL,6,'log/list'),(10,5,'ad','广告管理',NULL,7,'adAdmin/list'),(11,5,'collection','手机分类管理',NULL,8,'collection/list'),(12,5,'region','区域',NULL,9,'region/list'),(13,5,'country','国家',NULL,10,'country/list');


DROP TABLE IF EXISTS `t_menu_type`;

CREATE TABLE `t_menu_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) DEFAULT NULL COMMENT '名字',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `seq` INT(11) DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT ='菜单类型';

INSERT  INTO `t_menu_type`(`id`,`name`,`icon`,`seq`) VALUES (5,'基本图标',NULL,1);

DROP TABLE IF EXISTS `t_operation`;

CREATE TABLE `t_operation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) DEFAULT NULL,
  `code` VARCHAR(30) DEFAULT NULL,
  `type_id` INT(11) DEFAULT NULL,
  `description` VARCHAR(50) DEFAULT NULL,
  `seq` INT(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

/*Data for the table `t_operation` */

/*Table structure for table `t_operation_type` */

DROP TABLE IF EXISTS `t_operation_type`;

CREATE TABLE `t_operation_type` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) DEFAULT NULL COMMENT '名字',
  `seq` INT(11) DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT ='操作类型';



DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) DEFAULT NULL COMMENT '角色名',
  `description` VARCHAR(50) DEFAULT NULL COMMENT '描述',
  `seq` INT(11) DEFAULT NULL COMMENT '顺序',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT ='角色表';

/*Data for the table `t_role` */

INSERT  INTO `t_role`(`id`,`name`,`description`,`seq`) VALUES (1,'管理员','管理员',1);

/*Table structure for table `t_role_menu` */

DROP TABLE IF EXISTS `t_role_menu`;

CREATE TABLE `t_role_menu` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `role_id` INT(11) DEFAULT NULL COMMENT '角色id',
  `menu_id` INT(11) DEFAULT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COMMENT ='角色对应菜单';

/*Data for the table `t_role_menu` */

INSERT  INTO `t_role_menu`(`id`,`role_id`,`menu_id`) VALUES (1,1,6),(12,1,5),(13,1,7),(14,1,8),(15,1,9),(16,1,10),(17,1,11),(18,1,12);

/*Table structure for table `t_role_operation` */

DROP TABLE IF EXISTS `t_role_operation`;

CREATE TABLE `t_role_operation` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `role_id` INT(11) DEFAULT NULL COMMENT '角色id',
  `operation_id` INT(11) DEFAULT NULL COMMENT '操作id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8 COMMENT ='角色操作仅限';

/*Data for the table `t_role_operation` */


/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(30) DEFAULT NULL COMMENT '用户名',
  `password` VARCHAR(30) DEFAULT NULL COMMENT '密码',
  `activable` INT(11) DEFAULT NULL COMMENT '状态0冻结 1激活',
  `mobile` VARCHAR(15) DEFAULT NULL COMMENT '电话',
  `email` VARCHAR(50) DEFAULT NULL COMMENT 'email',
  `contacter` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `desc` VARCHAR(50) DEFAULT NULL COMMENT '描述',
  contact
  `insert_date` DATE DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT ='用户表';

/*Data for the table `t_user` */

INSERT  INTO `t_user`(`id`,`name`,`password`,`activable`,`mobile`,`email`,`insert_date`) VALUES (1,'admin','1111',1,NULL,NULL,'2011-11-12');

/*Table structure for table `t_user_role` */

DROP TABLE IF EXISTS `t_user_role`;

CREATE TABLE `t_user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `userd` INT(11) DEFAULT NULL COMMENT '用户id',
  `role_id` INT(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT ='用户角色表';

/*Data for the table `t_user_role` */

INSERT  INTO `t_user_role`(`id`,`user_id`,`role_id`) VALUES (4,1,1);