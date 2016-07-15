
'地铁资源部署sql表逻辑'
drop table if exists `download_sites`;
create table `download_sites`(
   `SiteID` int primary key not null auto_increment,
   `Domainstr` varchar(30) unique default '' comment '内网域名地址'
)

drop table if exists `pack_site_relations`;
create table `pack_site_relations`(
   `id` bigint primary key not null auto_increment,
   `SiteID` int not null,
   `PackID` int not null,
   `SubwayPackUrl` varchar(500) default '' comment '内网PackUrl地址',
   foreign key (`SiteID`) references `download_sites`(`SiteID`),
   foreign key (`PackID`) references `packinfo`(`PackID`)
)

'联合查询'

'以packinfo表基准'
select p.PackID,p.PackUrl2.url,p.SubWayUrl1,ds.url,ds.TitleDomain from pack_info p
	   left join pack_site_relations prs on p.PackID=prs.PackID 
	   left join download_sites ds on prs.SiteID = ds.SiteID 

'批量导入副链Url存储到过程'

DELIMITER //
    drop procedure if exists `appstore`.`insert_url_proc`
    create procedure insert_url_proc(out s int)
	begin
	   declare appcount int default 0; /*app计数器*/
	   declare subpack_url varchar(500) default '';
	   declare pack_url varchar(500) default '';
	   declare continue handler for not found set is_found=FALSE;
	   declare cur_pack_url cursor from select p.PackID,p.SubWayUrl1,ds.url,ds.TitleDomain from pack_info p
	   left join pack_site_relations prs on p.PackID=prs.PackID 
	   left join download_sites ds on prs.SiteID = ds.SiteID ;
	   
	   --url副链生成
	   
	   --打开游标
	   open cur_pack_url;
	   
	   --遍历游标
	   
	   read_loop:loop
	   
	   if not is_found then
	      leave read_loop;
	   end if;
	   
	   --这里做循环的事件
	   
	   set @pack_url=cur_pack_url.pack_info;
	   
	   --截取packurl再替换到subwayurl
	   
	   
	   insert 
	   
	end

DELIMITER //
	   


