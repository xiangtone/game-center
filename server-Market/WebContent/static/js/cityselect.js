var g_selProvince;var g_selCity;var Provinces=new Array(new Array("110000","北京市"),new Array("120000","天津市"),new Array("130000","河北省"),new Array("140000","山西省"),new Array("150000","内蒙古"),new Array("210000","辽宁省"),new Array("220000","吉林省"),new Array("230000","黑龙江省"),new Array("310000","上海市"),new Array("320000","江苏省"),new Array("330000","浙江省"),new Array("340000","安徽省"),new Array("350000","福建省"),new Array("360000","江西省"),new Array("370000","山东省"),new Array("410000","河南省"),new Array("420000","湖北省"),new Array("430000","湖南省"),new Array("440000","广东省"),new Array("450000","广西省"),new Array("460000","海南省"),new Array("500000","重庆市"),new Array("510000","四川省"),new Array("520000","贵州省"),new Array("530000","云南省"),new Array("540000","西藏省"),new Array("610000","陕西省"),new Array("620000","甘肃省"),new Array("630000","青海省"),new Array("640000","宁夏省"),new Array("650000","新疆省"));var Citys=new Array(new Array("110100","北京市"),new Array("120100","天津市"),new Array("130101","石家庄市"),new Array("130201","唐山市"),new Array("130301","秦皇岛市"),new Array("130701","张家口市"),new Array("130801","承德市"),new Array("131001","廊坊市"),new Array("130401","邯郸市"),new Array("130501","邢台市"),new Array("130601","保定市"),new Array("130901","沧州市"),new Array("133001","衡水市"),new Array("140101","太原市"),new Array("140201","大同市"),new Array("140301","阳泉市"),new Array("140501","晋城市"),new Array("140601","朔州市"),new Array("142201","忻州市"),new Array("142331","离石市"),new Array("142401","榆次市"),new Array("142601","临汾市"),new Array("142701","运城市"),new Array("140401","长治市"),new Array("150101","呼和浩特市"),new Array("150201","包头市"),new Array("150301","乌海市"),new Array("152601","集宁市"),new Array("152701","东胜市"),new Array("152801","临河市"),new Array("152921","阿拉善左旗市"),new Array("150401","赤峰市"),new Array("152301","通辽市"),new Array("152502","锡林浩特市"),new Array("152101","海拉尔市"),new Array("152201","乌兰浩特市"),new Array("210101","沈阳市"),new Array("210201","大连市"),new Array("210301","鞍山市"),new Array("210401","抚顺市"),new Array("210501","本溪市"),new Array("210701","锦州市"),new Array("210801","营口市"),new Array("210901","阜新市"),new Array("211101","盘锦市"),new Array("211201","铁岭市"),new Array("211301","朝阳市"),new Array("211401","锦西市"),new Array("210601","丹东市"),new Array("220101","长春市"),new Array("220201","吉林市"),new Array("220301","四平市"),new Array("220401","辽源市"),new Array("220601","浑江市"),new Array("222301","白城市"),new Array("222401","延吉市"),new Array("220501","通化市"),new Array("230101","哈尔滨市"),new Array("230301","鸡西市"),new Array("230401","鹤岗市"),new Array("230501","双鸭山市"),new Array("230701","伊春市"),new Array("230801","佳木斯市"),new Array("230901","七台河市"),new Array("231001","牡丹江市"),new Array("232301","绥化市"),new Array("230201","齐齐哈尔市"),new Array("230601","大庆市"),new Array("232601","黑河市"),new Array("232700","加格达奇市"),new Array("310100","上海市"),new Array("320101","南京市"),new Array("320201","无锡市"),new Array("320301","徐州市"),new Array("320401","常州市"),new Array("320501","苏州市"),new Array("320600","南通市"),new Array("320701","连云港市"),new Array("320801","淮阴市"),new Array("320901","盐城市"),new Array("321001","扬州市"),new Array("321101","镇江市"),new Array("323300","泰州市"),new Array("330101","杭州市"),new Array("330201","宁波市"),new Array("330301","温州市"),new Array("330401","嘉兴市"),new Array("330501","湖州市"),new Array("330601","绍兴市"),new Array("330701","金华市"),new Array("330801","衢州市"),new Array("330901","舟山市"),new Array("330902","台州市"),new Array("332501","丽水市"),new Array("332602","临海市"),new Array("340101","合肥市"),new Array("340201","芜湖市"),new Array("340301","蚌埠市"),new Array("340401","淮南市"),new Array("340501","马鞍山市"),new Array("340601","淮北市"),new Array("340701","铜陵市"),new Array("340801","安庆市"),new Array("341001","黄山市"),new Array("342101","阜阳市"),new Array("342201","宿州市"),new Array("342301","滁州市"),new Array("342401","六安市"),new Array("342501","宣州市"),new Array("342601","巢湖市"),new Array("342901","贵池市"),new Array("350101","福州市"),new Array("350201","厦门市"),new Array("350301","莆田市"),new Array("350401","三明市"),new Array("350501","泉州市"),new Array("350601","漳州市"),new Array("352101","南平市"),new Array("352201","宁德市"),new Array("352601","龙岩市"),new Array("352701","晋江市"),new Array("360101","南昌市"),new Array("360201","景德镇市"),new Array("362101","赣州市"),new Array("360301","萍乡市"),new Array("360401","九江市"),new Array("360501","新余市"),new Array("360601","鹰潭市"),new Array("362201","宜春市"),new Array("362301","上饶市"),new Array("362401","吉安市"),new Array("362502","临川市"),new Array("370101","济南市"),new Array("370201","青岛市"),new Array("370301","淄博市"),new Array("370401","枣庄市"),new Array("370501","东营市"),new Array("370601","烟台市"),new Array("370701","潍坊市"),new Array("370801","济宁市"),new Array("370901","泰安市"),new Array("371001","威海市"),new Array("371100","日照市"),new Array("372301","滨州市"),new Array("372401","德州市"),new Array("372501","聊城市"),new Array("372801","临沂市"),new Array("372901","菏泽市"),new Array("373101","即墨市"),new Array("410101","郑州市"),new Array("410201","开封市"),new Array("410301","洛阳市"),new Array("410401","平顶山市"),new Array("410501","安阳市"),new Array("410601","鹤壁市"),new Array("410701","新乡市"),new Array("410801","焦作市"),new Array("410901","濮阳市"),new Array("411001","许昌市"),new Array("411101","漯河市"),new Array("411201","三门峡市"),new Array("412301","商丘市"),new Array("412701","周口市"),new Array("412801","驻马店市"),new Array("412901","南阳市"),new Array("413001","信阳市"),new Array("420101","武汉市"),new Array("420201","黄石市"),new Array("420301","十堰市"),new Array("420400","沙市市"),new Array("420501","宜昌市"),new Array("420601","襄樊市"),new Array("420701","鄂州市"),new Array("420801","荆门市"),new Array("420901","石首市"),new Array("422103","黄州市"),new Array("422201","孝感市"),new Array("422301","咸宁市"),new Array("422421","江陵市"),new Array("422801","恩施市"),new Array("430101","长沙市"),new Array("430401","衡阳市"),new Array("430501","邵阳市"),new Array("432801","郴州市"),new Array("432901","永州市"),new Array("430801","大庸市"),new Array("433001","怀化市"),new Array("433101","吉首市"),new Array("430201","株洲市"),new Array("430301","湘潭市"),new Array("430601","岳阳市"),new Array("430701","常德市"),new Array("432301","益阳市"),new Array("432501","娄底市"),new Array("440101","广州市"),new Array("440301","深圳市"),new Array("441501","汕尾市"),new Array("441301","惠州市"),new Array("441601","河源市"),new Array("440601","佛山市"),new Array("441801","清远市"),new Array("441901","东莞市"),new Array("440401","珠海市"),new Array("440701","江门市"),new Array("441201","肇庆市"),new Array("442001","中山市"),new Array("440801","湛江市"),new Array("440901","茂名市"),new Array("440201","韶关市"),new Array("440501","汕头市"),new Array("441401","梅州市"),new Array("441701","阳江市"),new Array("442101","潮州市"),new Array("441101","揭阳市"),new Array("441001","云浮市"),new Array("450101","南宁市"),new Array("450401","梧州市"),new Array("450701","防城港市"),new Array("452501","玉林市"),new Array("450301","桂林市"),new Array("452601","百色市"),new Array("452701","河池市"),new Array("452802","钦州市"),new Array("450201","柳州市"),new Array("450501","北海市"),new Array("460100","海口市"),new Array("460200","三亚市"),new Array("510101","成都市"),new Array("513321","康定市"),new Array("513101","雅安市"),new Array("513229","马尔康市"),new Array("510301","自贡市"),new Array("500100","重庆市"),new Array("512901","南充市"),new Array("510501","泸州市"),new Array("510601","德阳市"),new Array("510701","绵阳市"),new Array("510901","遂宁市"),new Array("511001","内江市"),new Array("511101","乐山市"),new Array("512501","宜宾市"),new Array("510801","广元市"),new Array("513021","达县市"),new Array("513401","西昌市"),new Array("510401","攀枝花市"),new Array("520101","贵阳市"),new Array("520200","六盘水市"),new Array("522201","铜仁市"),new Array("522501","安顺市"),new Array("522601","凯里市"),new Array("522701","都匀市"),new Array("522301","兴义市"),new Array("522421","毕节市"),new Array("522101","遵义市"),new Array("530101","昆明市"),new Array("530201","东川市"),new Array("532201","曲靖市"),new Array("532301","楚雄市"),new Array("532401","玉溪市"),new Array("532501","个旧市"),new Array("532621","文山市"),new Array("532721","思茅市"),new Array("532101","昭通市"),new Array("532821","景洪市"),new Array("532901","大理市"),new Array("533001","保山市"),new Array("533121","潞西市"),new Array("533321","泸水市"),new Array("533421","中甸市"),new Array("533521","临沧市"),new Array("540101","拉萨市"),new Array("542121","昌都市"),new Array("542221","乃东市"),new Array("542301","日喀则市"),new Array("542421","那曲市"),new Array("542523","噶尔市"),new Array("542621","林芝市"),new Array("610101","西安市"),new Array("610201","铜川市"),new Array("610301","宝鸡市"),new Array("610401","咸阳市"),new Array("612101","渭南市"),new Array("612301","汉中市"),new Array("612401","安康市"),new Array("612501","商州市"),new Array("612601","延安市"),new Array("612701","榆林市"),new Array("620101","兰州市"),new Array("620401","白银市"),new Array("620301","金昌市"),new Array("620501","天水市"),new Array("622201","张掖市"),new Array("622301","武威市"),new Array("622421","定西市"),new Array("622624","成县市"),new Array("622701","平凉市"),new Array("622801","西峰市"),new Array("622901","临夏市"),new Array("623027","夏河市"),new Array("620201","嘉峪关市"),new Array("622102","酒泉市"),new Array("630100","西宁市"),new Array("632121","平安市"),new Array("632321","同仁市"),new Array("632521","共和市"),new Array("632621","玛沁市"),new Array("632721","玉树市"),new Array("632802","德令哈市"),new Array("640101","银川市"),new Array("640201","石嘴山市"),new Array("642101","吴忠市"),new Array("642221","固原市"),new Array("650101","乌鲁木齐市"),new Array("650201","克拉玛依市"),new Array("652101","吐鲁番市"),new Array("652201","哈密市"),new Array("652301","昌吉市"),new Array("652701","博乐市"),new Array("652801","库尔勒市"),new Array("652901","阿克苏市"),new Array("653001","阿图什市"),new Array("653101","喀什市"),new Array("654101","伊宁市"),new Array("211001","辽阳市"),new Array("653201","和田市"),new Array("542200","泽当镇市"),new Array("542600","八一镇市"));function initProvinceSelect(currentProvince,currentCity){fillProvince(currentProvince);fillCity(currentCity);$("#bankProvince").change(function(){fillCity();});}function fillProvince(currentProvince){$("<option selected=\"selected\" value=\"\">--请选择开户省--</option>").appendTo($("#bankProvince"));for(var i=0;i<Provinces.length;i++){if(Provinces[i][1]==currentProvince){$("<option selected=\"selected\" value="+Provinces[i][1]+">"+Provinces[i][1]+"</option>").appendTo($("#bankProvince"));}else{$("<option value="+Provinces[i][1]+">"+Provinces[i][1]+"</option>").appendTo($("#bankProvince"));}}}function fillCity(currentCity){$("#bankCity").empty();var currentProvince=$("option:selected",$("#bankProvince")).val();var provinceCode=getProvinceCode(currentProvince);$("<option selected=\"selected\" value=\"\">--请选择开户市--</option>").appendTo($("#bankCity"));if(null!=provinceCode&&''!=provinceCode){for(var i=0;i<Citys.length;i++){if(Citys[i][0].substring(0,2)==provinceCode.substring(0,2)){if(Citys[i][1]==currentCity){$("<option selected=\"selected\" value="+Citys[i][1]+">"+Citys[i][1]+"</option>").appendTo($("#bankCity"));}else{$("<option value="+Citys[i][1]+">"+Citys[i][1]+"</option>").appendTo($("#bankCity"));}}}}}function getProvinceCode(province){var provinceCode="";for(var i=0;i<Provinces.length;i++){if(Provinces[i][1]==province){provinceCode=Provinces[i][0];break;}}return provinceCode;}