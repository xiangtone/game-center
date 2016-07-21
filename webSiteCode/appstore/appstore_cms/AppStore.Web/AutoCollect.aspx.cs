using AppStore.BLL;
using AppStore.Model;
using nwbase_sdk;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
namespace AppStore.Web
{

    public partial class AutoCollect : BasePage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            BindAppType();
            //TextBox1.Text = "http://zhushou.360.cn/detail/index/soft_id/3009145#next";
            //TextBox1.Text = "http://zhushou.360.cn/detail/index/soft_id/263710";
        }

        private string GetWebClient(string url)
        {
            string strHTML = "";
            WebClient myWebClient = new WebClient();
            Stream myStream = myWebClient.OpenRead(url);
            StreamReader sr = new StreamReader(myStream, System.Text.Encoding.GetEncoding("utf-8"));
            strHTML = sr.ReadToEnd();
            myStream.Close();
            return strHTML;
        }
        protected void Button1_Click(object sender, EventArgs e)
        {
            if (TextBox1.Text == "")
            {
                this.Alert("请输入url");
            }
            else
            {
                string str = GetWebClient(TextBox1.Text);
                //Response.Write(str);
                Bind(str);

                string picList = "<div id=\"scrollbar\" data-snaps=\"(?<ImgList>.*?)\">";
                Match ml = Regex.Match(str, picList);
                if (ml.Success)
                {
                    string[] pics = ml.Groups["ImgList"].Value.Split(',');

                    for (int i = 0; i < pics.Count(); i++)
                    {
                        switch (i)
                        {
                            case 0:
                                Image2.ImageUrl = pics[i];
                                Image2.Visible = true;
                                break;
                            case 1:
                                Image3.ImageUrl = pics[i];
                                Image3.Visible = true;
                                break;
                            case 2:
                                Image4.ImageUrl = pics[i];
                                Image4.Visible = true;
                                break;
                            case 3:
                                Image5.ImageUrl = pics[i];
                                Image5.Visible = true;
                                break;
                            case 4:
                                Image6.ImageUrl = pics[i];
                                Image6.Visible = true;
                                break;
                            default:
                                break;
                        }

                    }
                }

                //"<!DOCTYPE HTML>\r\n<html>\r\n<head>\r\n<meta charset=\"utf-8\">\r\n<title>会说话的汤姆猫2_360手机助手</title>\r\n<meta name=\"keywords\" content=\" 360手机助手 免费 android 安卓 平台\" >\r\n<meta name=\"description\" content=\"\">\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/style.120511.css\">\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"/style/plus.120511.css?v=3\">\r\n<!-- detail.css 1100697752 -->\r\n<link rel=\"stylesheet\" type=\"text/css\" href=\"http://s4.qhimg.com/static/74f6c81b2aede47e.css\" />\r\n<!-- zsclient.js -->\r\n<script type=\"text/javascript\" src=\"/script/zsclient.1.0.js\"></script>\r\n<style type=\"text/css\">\r\n    .warper{\r\n        width: 825px;\r\n        border: none;\r\n        float: left;\r\n        background-color: #f5f5f5;\r\n    }\r\n    .warper .main{\r\n        background-color: #f5f5f5;\r\n    }\r\n\r\n    /* 块 */\r\n    /*#app-info-panel, .mod-info, .app-tags, #login-form, .scmt-top, .scmt-list{\r\n        background-color: #eee;\r\n    }*/\r\n    a.dbtn{\r\n        background: url(http://p2.qhimg.com/d/inn/558a48bd/bg.png) no-repeat;\r\n        _background: url(http://p8.qhimg.com/t01c50d9cc8d6a353b1.gif) no-repeat;\r\n    }\r\n    .otherLogo{\r\n        width: 14px;\r\n        height: 14px;\r\n        vertical-align: middle;\r\n        margin: 0 4px;\r\n    }\r\n    .otherText{\r\n        vertical-align: middle;\r\n        line-height: 14px;\r\n        margin-right: 20px;\r\n    }\r\n    .mod h2{\r\n        background-color: #e7e8eb;\r\n    }\r\n    .othersrc{\r\n        padding-top: 6px;\r\n        margin-bottom: 10px;\r\n    }\r\n\r\n    .product{\r\n        padding-top: 12px;\r\n    }\r\n    .product .dbtn{\r\n        right: 40px;\r\n    }\r\n</style>\r\n<script>\r\nvar img_url = 'http://p15.qhimg.com/t010c11474f7e736f99.png';\r\n</script>\r\n<link rel=\"icon\" type=\"/image/vnd.microsoft.icon\" href=\"http://www.360.cn/favicon.ico\"/>\r\n</head>\r\n        <script>\r\n        var srcs = [\r\n                        {'source':'360market',\r\n                'size':'40534693',\r\n                'downurl':'http://shouji.360tpcdn.com/150605/0e60ba3807b6f8ddb993918f9bad33d3/com.outfit7.talkingtom2free_180.apk',\r\n                'mkid':'2',\r\n                'mkrate':'54',\r\n                'mkname':'360手机助手',\r\n                'mkicon':'http://w.qhimg.com/images/v2/mobi/adm/openMarketlogos/20120411/1334143081650.png'}\r\n                ];\r\n\r\n        function rnd(RAND_MAX) {\r\n            rnd.today = new Date();\r\n            rnd.seed  = rnd.today.getTime();\r\n            rnd.seed  = (rnd.seed*9301+49297) % 233280;\r\n            rnd.rslt  = Math.ceil(rnd.seed/(233280.0)*RAND_MAX);\r\n            return rnd.rslt;\r\n        };\r\n\r\n        function random_market(arrMarkets){\r\n            var sumOfRate=0;\r\n            var arrSegments=new Array(arrMarkets.length);\r\n\r\n            for ( var i = 0; i < arrMarkets.length; i++) {\r\n                arrSegments[i] = new Array(sumOfRate+1, sumOfRate+parseInt(arrMarkets[i].mkrate));\r\n                sumOfRate += parseInt(arrMarkets[i].mkrate);\r\n            }\r\n\r\n            var randMarketID = -1;\r\n            randNum = rnd(sumOfRate);\r\n            for ( var i = 0; i < arrSegments.length; i++) {\r\n                if(randNum>=arrSegments[i][0] && randNum<=arrSegments[i][1]) {\r\n                    randMarketID = i;\r\n                    break;\r\n                }\r\n            }\r\n            return randMarketID;\r\n        };\r\n\r\n        function srcSortDesc(a,b) {\r\n            return b.mkname.length - a.mkname.length;\r\n        }\r\n        srcs.sort(srcSortDesc);\r\n        //\r\n        var srcMarketID = random_market(srcs);\r\n        var src = srcs[srcMarketID];\r\n        // 0702 修改为取第一个\r\n        src = srcs[0];\r\n    </script>\r\n    <script src=\"http://s0.qhimg.com/lib/jquery/172.js\"></script>\r\n    <script src=\"http://s5.qhimg.com/!ffce04be/pic_lazyload.js\"></script>\r\n    <script src=\"/script/MMPlugin.js\"></script>\r\n    <script src=\"/script/client.120803.js?_1303131\"></script>\r\n    <script>var recPos = 'rjxq';</script>\r\n    <script>\r\n    // 详情页的命名空间\r\n    var detail = (function () {\r\n        return {\r\n            'sid': 10283,\r\n            'sname': '会说话的汤姆猫2',\r\n            'type': \"game\",\r\n            'cid1': \"2\",\r\n            'cid2': \"19\",\r\n            'pname': \"com.outfit7.talkingtom2free\",\r\n            'downloadUrl': 'http://shouji.360tpcdn.com/150605/0e60ba3807b6f8ddb993918f9bad33d3/com.outfit7.talkingtom2free_180.apk',\r\n            'filemd5': '0e60ba3807b6f8ddb993918f9bad33d3',\r\n            'vcode': '180',\r\n            'baike_name': '会说话的汤姆猫2'\r\n        };\r\n    })();\r\n    </script>\r\n    <body class=\"index\">\r\n        <!--顶部导航条 开始-->\r\n                <div class=\"head\">\r\n\t<h1 class=\"logo\" id=\"top\">手机助手</h1>\r\n\t<div class=\"tsch\">\r\n\t\t<div class=\"sch\">\r\n\t\t  <form action=\"/search/index/\">\r\n\t\t\t<input type=\"text\" id=\"kwd\" name=\"kw\" class=\"kwd\" value=\"应用总量 43066\" placeholder=\"应用总量 43066\">\r\n\t\t\t<button value=\"软件搜索\" ></button>\r\n\r\n\t\t  </form>\r\n\t\t</div>\r\n\t\t<span><a href=\"/search/index/kw/%E6%B1%9F%E6%B9%96\" >江湖</a>&nbsp;<a href=\"/search/index/kw/%E5%A1%94%E9%98%B2\" >塔防</a>&nbsp;<a href=\"/search/index/kw/%E8%B7%91%E9%85%B7\" >跑酷</a>&nbsp;<a href=\"/search/index/kw/%E6%96%97%E5%9C%B0%E4%B8%BB\" >斗地主</a>&nbsp;<a href=\"/search/index/kw/%E4%B8%89%E5%9B%BD\" >三国</a>&nbsp;<a href=\"/search/index/kw/%E6%89%BE%E8%8C%AC\" >找茬</a>&nbsp;\t\t</span>\r\n\t</div><!--end hsearch-->\r\n</div>\r\n<!--end head-->\r\n<div class=\"nav\">\r\n\t<ul>\r\n\t<li><a href=\"/\" cid=\"sy\">首页</a></li><li class=\"cur\"><a href=\"/game/\" cid=\"yx\">玩游戏</a></li><li><a href=\"/soft/\" cid=\"rj\">装软件</a></li><li><a href=\"/channel/vedio/\" cid=\"vedio\">看视频</a></li><li><a href=\"/channel/book/\" cid=\"book\">读小说</a></li><li><a href=\"/channel/pic/\" cid=\"pic\">看图片</a></li>\r\n\t<li  id=\"page-topic\"><a href=\"http://meihua.360.cn/?ref=zhushou\" cid=\"topic\" target=\"_blank\">换主题</a></li>\r\n\t<style>\r\n    .nav li.fbk{ width:60px;}\r\n    </style>\r\n    <li class=\"fr fbk\"><a href=\"#nogo\" id=\"fbk\">意见反馈</a></li>\r\n    <li class=\"fr fbk\" style=\"width:60px;\"><a href=\"http://zhushou.360.cn/peifu?from=webnav\">申请赔付</a></li>\r\n\t</ul>\r\n</div>\r\n<!--end nav-->\r\n\r\n                <!--顶部导航条 结束-->\r\n        <div class=\"warp\">\r\n            <!--左侧导航 开始-->\r\n                            <div class=\"menu\">\r\n\t<div class=\"menuc\">\t\t<h3 style=\"border-top:1px #b5bdc7 solid;\">安卓游戏</h3>\r\n\t\t<a cid=\"yxsy\" href=\"/game/\">游戏首页</a>\r\n         <a cid=\"yxpf\" href=\"http://zhushou.360.cn/peifu?from=sidenav\">先行赔付 <em style=\"color:#f00; font-style:normal; font-size:12px;\">new</em></a> \r\n\t\t<a cid=\"yxfl\" href=\"/list/index/cid/2\">游戏分类</a>\r\n\t\t<a cid=\"tszt\" href=\"/zhuanti/index/t/2\">特色专题</a>\r\n\t\t<a cid=\"top\" href=\"/list/hotList/cid/2\">排行榜</a>\t<h3>安全市场联盟</h3><a cid=\"g_ndou\" href=\"/game/parter/goto/g_ndou/\">N多市场</a><a cid=\"g_155\" href=\"/game/parter/goto/g_155/\">手游天下</a><a cid=\"g_mumayi\" href=\"/game/parter/goto/g_mumayi/\">木蚂蚁</a><a cid=\"g_eoemarket\" href=\"/game/parter/goto/g_eoemarket/\">优亿市场</a><a cid=\"g_jfsc\" href=\"/game/parter/goto/g_jfsc/\">机锋网</a><a cid=\"g_appchina\" href=\"/game/parter/goto/g_appchina/\">应用汇</a><h3>开放平台</h3><a cid=\"g_sohu\" href=\"/game/parter/goto/g_sohu/\">搜狐高速下载</a><a cid=\"g_netease\" href=\"/game/parter/goto/g_netease/\">网易应用</a><a cid=\"g_lenovomm\" href=\"/game/parter/goto/g_lenovomm\">联想乐商店</a>\r\n\t</div><!--end menuc-->\r\n\t\t<div class=\"menub\">\r\n\t\t\t<p>如果你是应用开发者欢迎提交软件到360</p>\r\n\t\t\t<a cid=\"tjyy\" href=\"http://dev.360.cn\" class=\"dbtn\">提交应用</a>\r\n\t\t</div><!--end menub-->\r\n\t</div>\r\n\t<!--end menu-->\r\n                        <!--左侧导航 结束-->\r\n\r\n\r\n            <div class=\"warper\">\r\n                <div class=\"main clearfix\">\r\n                    <div class=\"main-right\">\r\n                        <!-- 猜你喜欢 -->\r\n                        <div class=\"mod ulikecon\">\r\n                            <h2>下载的人还喜欢<a href=\"#guess-like\" title=\"换一换\" id=\"ulsw\" startIdx=\"0\"></a></h2>\r\n                            <div class=\"ulikecon2 btn_type2 mod-body\">\r\n                                <ul id=\"likelist\"></ul>\r\n                            </div>\r\n                        </div>\r\n                        <!-- 链接地址 detail3 -->\r\n                        <script type=\"text/template\" id=\"xihuan-template\">\r\n                            <li>\r\n                                <a class=\"click-log\" data-sid=\"{soft_id}\" data-pos=\"{dadian}_{index}_10283{diadian}\" href=\"/detail/index/soft_id/{soft_id}\">\r\n                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\" _src=\"{logo_url}\" title=\"{soft_name}\" class=\"png\">\r\n                                </a>\r\n                                <a class=\"click-log\" data-sid=\"{soft_id}\" data-pos=\"{dadian}_{index}_10283{diadian}\" href=\"/detail/index/soft_id/{soft_id}\">\r\n                                    <span class=\"sname\" title=\"{soft_name}\" style=\"cursor: pointer;\">{soft_sub_name}</span>\r\n                                </a>\r\n                                <div class=\"rate-box\">\r\n                                    <cite><strong>{rate}</strong>用户下载</cite>\r\n                                </div>\r\n                                <a href=\"zhushou360://type=apk&marketid=10000001&refer=thirdlink&name={soft_name}&icon={logo_url}&appmd5={apk_md5}&softid={soft_id}&appadb=&url={download_url}\" class=\"dbtn {soft_id}-btn normal\" data-sid=\"{soft_id}\" data-sname=\"{soft_name}\" data-downurl=\"{download_url}\" data-pos=\"{dadian}_{index}_10283{diadian}\">下载</a>\r\n                            </li>\r\n                        </script>\r\n                        <!-- 本类热门应用 -->\r\n                        <div class=\"mod category-rank\">\r\n                            <h2>本类热门应用</h2>\r\n                            <div class=\"rank-list btn_type2 mod-body\">\r\n                                <ul id=\"category-hot\">\r\n                                </ul>\r\n                            </div>\r\n                        </div>\r\n                        <script type=\"text/template\" id=\"category-hot-template\">\r\n                            <li>\r\n                                <a class=\"click-log\" data-sid=\"{soft_id}\" data-pos=\"{dadian}_{index}\" href=\"/detail/index/soft_id/{soft_id}\">\r\n                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\" _src=\"{logo_url}\" title=\"{soft_name}\" class=\"png\">\r\n                                </a>\r\n                                <a class=\"click-log\" data-sid=\"{soft_id}\" data-pos=\"{dadian}_{index}\" href=\"/detail/index/soft_id/{soft_id}\">\r\n                                    <span class=\"sname\" title=\"{soft_name}\" style=\"cursor: pointer;\">{soft_name}</span>\r\n                                </a>\r\n                                <div class=\"rate-box\">\r\n                                    <cite><strong>{download_times_fmt}</strong>次下载</cite>\r\n                                </div>\r\n                                <a href=\"zhushou360://type=apk&marketid=10000001&refer=thirdlink&name={soft_name}&icon={logo_url}&appmd5={apk_md5}&softid={soft_id}&appadb=&url={download_url}\" class=\"dbtn {soft_id}-btn normal\" data-sid=\"{soft_id}\" data-sname=\"{soft_name}\" data-downurl=\"{download_url}\" data-pos=\"{dadian}_{index}\">下载</a>\r\n                            </li>\r\n                        </script>\r\n                    </div>\r\n                    <div class=\"main-left fl\">\r\n                        <div id=\"app-info-panel\">\r\n                                                            <a href=\"http://zhushou.360.cn/peifu/\" class=\"jiao\">游戏先赔</a>\r\n                                                        <div class=\"product btn_type1\">\r\n                                <dl class=\"clearfix\">\r\n                                    <dt><img src=\"http://p15.qhimg.com/t010c11474f7e736f99.png\" width=\"72\" height=\"72\" alt=\"会说话的汤姆猫2\"></dt>\r\n                                    <dd>\r\n                                        <h2 id=\"app-name\"><span title=\"会说话的汤姆猫2\">会说话的汤姆猫2</span><cite class=\"verify_tag\"></cite></h2>\r\n                                        <div class=\"pf\">\r\n                                            <span class=\"s-1 js-votepanel\">8.8<em>分</em></span>\r\n                                            <span class=\"s-2\"><a href=\"#comment-list\" id=\"comment-num\"><span class=\"js-comments review-count-all\" style=\"margin:0;\">0</span>条评价</a></span>\r\n                                            <span class=\"s-3\">下载：5799万次</span>\r\n                                            <span class=\"s-3\">38.66M</span>\r\n                                        </div>\r\n                                                                                    <p><strong>【小编点评】</strong>会说话的汤姆猫2官方国内版</p>\r\n                                                                                                                        <a class=\"js-downLog dbtn 10283-btn normal\" href=\"zhushou360://type=apk&marketid=10000001&refer=thirdlink&name=会说话的汤姆猫2&icon=http://p15.qhimg.com/t010c11474f7e736f99.png&appmd5=0e60ba3807b6f8ddb993918f9bad33d3&softid=10283&appadb=&url=http://shouji.360tpcdn.com/150605/0e60ba3807b6f8ddb993918f9bad33d3/com.outfit7.talkingtom2free_180.apk\" data-sid=\"10283\" data-sname=\"会说话的汤姆猫2\" data-pos=\"D_D\">立即安装</a>\r\n                                    </dd>\r\n                                </dl>\r\n                            </div>\r\n                        </div>\r\n\r\n                        <div class=\"infors\">\r\n                            <div class=\"mod-info\">\r\n                                <!--应用介绍-->\r\n                                <div class=\"infors-txt\">\r\n                                    <div class=\"title\">\r\n                                        <strong>应用介绍</strong>\r\n                                        <ul>\r\n                                            <li class=\"item-1\" id=\"safeTiper\"><span class=\"icon-2\"></span>安全无毒</li>\r\n\r\n                                                                                            <li class=\"item-2\" id=\"adsPanel-tg\"><span class=\"icon-3\"></span>有广告</li>\r\n                                            \r\n                                                                                            <li class=\"item-2\" id=\"feePanel-tg\"><span class=\"icon-3\"></span>含支付项</li>\r\n                                            \r\n                                            \r\n                                                                                                    <li class=\"item-3\" id=\"authority-tg\"><span class=\"icon-3\"></span>\r\n                                                \r\n                                                权限\r\n                                                ：11                                                </li>\r\n                                            \r\n                                            <li class=\"item-1\"><span class=\"icon-2 join-green-act\"></span><a href=\"http://zhushou.360.cn/html/report/form.html?soft_id=77208&soft_name=360%E6%89%8B%E6%9C%BA%E5%8D%AB%E5%A3%AB\">参与绿剑行动</a></li>\r\n                                                                                    </ul>\r\n                                    </div>\r\n                                    <div id=\"vmPlayer\"></div>\r\n                                                                            <div class=\"sdesc clearfix\" id=\"sdesc\">\r\n                                            
                //<div class=\"breif\">\r\n                                                国内版是Out&nbsp;Fit&nbsp;7&nbsp;Ltd.专门针对中国市场开发的版本，更适合国内用户。<br/>\r\n<br/>\r\n会说话的汤姆猫2官方国内版：<br/>\r\n<br/>\r\n玩法<br/>\r\n.&nbsp;对汤姆说话,他将用滑稽的声音完整地复述您说的话。<br/>\r\n.&nbsp;抚摸汤姆的肚子或头,他会发出咕噜声。<br/>\r\n.&nbsp;用手指戳他的头、肚子或脚。<br/>\r\n.&nbsp;拍一下汤姆的左右脸。<br/>\r\n.&nbsp;拉或触摸他的尾巴。<br/>\r\n.&nbsp;按下放屁按钮,观看本放屁,汤姆厌恶地捂住他的鼻子。然后,汤姆会复述您说的话,而他的鼻子是闭着的。<br/>\r\n.&nbsp;按下袋子按钮,让本吹涨一个纸袋,吓汤姆一大跳。真是滑稽。<br/>\r\n.&nbsp;按&nbsp;?&nbsp;按钮让汤姆从背后随机抽出东西。                                                    <div class=\"base-info\">\r\n                                                        <br/><b>【基本信息】</b><br/>\r\n                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n                                                            <tbody>\r\n                                                                <tr>\r\n                                                                    <td width=\"50%\"><strong>作者：</strong>Outfit7 Limited</td>\r\n                                                                    <td width=\"50%\"><strong>更新时间：</strong>2015-06-05</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td><strong>版本：</strong>4.8<!--versioncode:180--><!--updatetime:2015-06-05--></td>\r\n                                                                    <td><strong>系统：</strong>Android 4.0.3以上</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td colspan=\"2\"><strong>语言：</strong>中文</td>\r\n                                                                </tr>\r\n                                                            </tbody>\r\n                                                        </table>\r\n                                                    </div>\r\n\r\n                                                    \r\n                                                    <br/><b>【更新内容】</b><br/>三款新配饰：<br />\r\n水果帽子——水果们都在汤姆的头上干些什么呢？<br />\r\n衬衫配领带——哦是的，汤姆穿上这身衣服看起来很漂亮呢！<br />\r\n兔头帽子——哈哈哈，你绝对没见着汤姆有过比这更萌样子！                                            </div>
                //<p class=\"brief-toggle\"><a href=\"#expand\" id=\"toggle-brief\" class=\"folded\"><span>展开</span><em></em></a></p>\r\n                                        </div>\r\n                                                                    </div>\r\n\r\n                                                                    <div id=\"scrollbar\" data-snaps=\"http://p16.qhimg.com/t017e10c824b29728e3.jpg,http://p16.qhimg.com/t013372459dd15bf10d.jpg,http://p15.qhimg.com/t01a61f39f54a323be7.jpg,http://p17.qhimg.com/t01334e9a499e5f1187.jpg,http://p16.qhimg.com/t01ef876c5dc8c7e21d.jpg\">\r\n                                        <a href=\"#prev\" class=\"arrow arrow-prev\"></a>\r\n                                        <a href=\"#next\" class=\"arrow arrow-next\"></a>\r\n                                        <div class=\"viewport\">\r\n                                            <div class=\"overview\">\r\n                                                                                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\">\r\n                                                                                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\">\r\n                                                                                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\">\r\n                                                                                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\">\r\n                                                                                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\">\r\n                                                                                            </div>\r\n                                        </div>\r\n                                        <!-- <div class=\"scrollbar\"><div class=\"inner\"><div class=\"track\"><div class=\"thumb\"><div class=\"end\"></div></div></div></div></div> -->\r\n                                    </div>\r\n                                                            </div>\r\n\r\n                                                            <div class=\"app-tags\">\r\n                                    <strong>应用标签：</strong>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E4%BC%91%E9%97%B2\" style=\"color:#007fc1\">休闲</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E4%BC%91%E9%97%B2%E7%9B%8A%E6%99%BA\" style=\"color:#007fc1\">休闲益智</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=360%E7%B2%BE%E5%93%81%E6%B8%B8%E6%88%8F\" style=\"color:#007fc1\">360精品游戏</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E7%B2%BE%E5%93%81%E5%8D%95%E6%9C%BA\" style=\"color:#007fc1\">精品单机</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E5%84%BF%E7%AB%A5%E6%B8%B8%E6%88%8F\" style=\"color:#007fc1\">儿童游戏</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E5%A4%A7%E4%BD%9C\" style=\"color:#007fc1\">大作</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E7%BB%8F%E5%85%B8\" style=\"color:#007fc1\">经典</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E8%90%8C\" style=\"color:#007fc1\">萌</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E5%96%B5%E6%98%9F%E4%BA%BA\" style=\"color:#007fc1\">喵星人</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E4%BC%9A%E8%AF%B4%E8%AF%9D\" style=\"color:#007fc1\">会说话</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E5%AF%B9%E8%AF%9D\" style=\"color:#007fc1\">对话</a>\r\n                                                                            <!-- 标签跳搜索结果页 test -->\r\n                                        <a href=\"http://zhushou.360.cn/search/index/?kw=%E6%95%8F%E6%8D%B7\" style=\"color:#007fc1\">敏捷</a>\r\n                                                                    </div>\r\n                                                                                                            </div>\r\n\r\n                        <!--用户评论-->\r\n                        <div class=\"scmt\" id=\"comment-list\" name=\"pl\">\r\n\r\n                            <div style=\"display:none;\">\r\n                                <div class=\"no-scmt tips-login\">您的评价是作者最大的动力，请给他们一些建议或者掌声吧。<a href=\"#comment\" style=\"color:#007fc1;\">发表评论</a></div>\r\n                                <div id=\"login-form\">\r\n                                    <label>用户：\r\n                                        <input type=\"text\" id=\"user-name\" autocomplete=\"off\">\r\n                                    </label>\r\n                                    <label>密码：\r\n                                        <input type=\"password\" id=\"user-pwd\">\r\n                                    </label>\r\n                                    <img src=\"http://w.qhimg.com/images/v2/mobi/box/360baoku/login_normal.gif\" style=\"cursor:pointer;\" id=\"btn-login\">\r\n                                    <label>\r\n                                        <input type=\"checkbox\" class=\"g\" checked=\"\" value=\"1\" name=\"keep_login\" id=\"isKeepAlive\">\r\n                                        保持登录状态\r\n                                    </label>\r\n                                    <p class=\"old-login-tips\">\r\n                                        <span id=\"err-username\" ></span>\r\n                                        <span id=\"err-pwd\"></span>\r\n                                        <a target=\"_blank\" href=\"http://i.360.cn/findpwd/\">忘记密码？</a> | <a href=\"http://i.360.cn/reg?src=mobilem&destUrl=http%3A%2F%2Fzhushou.mobilem.360.cn%2F360helper%2FDetailServletV1%3Fsoft_id%3D57512\" id=\"reg_url\">还没有帐号？马上注册&gt;&gt;</a>\r\n                                    </p>\r\n                                </div>\r\n\r\n                                <div class=\"scmt-top cmt-area\">\r\n                                    <div class=\"scmt-tit clearfix\">\r\n                                        <h2>用户评论</h2>\r\n                                    </div>\r\n                                    <div style=\"position:relative;\">\r\n                                        <div class=\"cmt-type\" id=\"cmt-type-list\">\r\n                                            <label class=\"cur\" for=\"type1\" hidefocus><input id=\"type1\" name=\"type\" type=\"radio\" checked><span class=\"good\">很好<em class=\"review-count-best\"></em></span></label>\r\n                                            <label for=\"type2\" hidefocus><input id=\"type2\" name=\"type\" type=\"radio\" ><span class=\"mid\">一般<em class=\"review-count-good\"></em></span></label>\r\n                                            <label for=\"type3\" hidefocus><input id=\"type3\" name=\"type\" type=\"radio\" ><span class=\"bad\">很差<em class=\"review-count-bad\"></em></span></label>\r\n                                        </div>\r\n                                        <div class=\"cmt-area-box\">\r\n                                            <textarea id=\"review-content\">您的赞美就是给作者最大的回报，觉得好就表扬TA一下吧。</textarea>\r\n                                            <p><span id=\"review-warn\">还可以输入<em>200</em>字</span><a data-type=\"best\" href=\"#nogo\" id=\"btn-do-review\">发表</a></p>\r\n                                        </div>\r\n                                    </div>\r\n                                </div>\r\n                            </div>\r\n\r\n                                <div class=\"scmt-list\" id=\"pj\" name=\"pj\">\r\n                                    <div class=\"scmt-list-type\">\r\n                                        <a class=\"cur\" href=\"#nogo\" id=\"tab-review-all\">全部评论<span  class=\"review-count-all\"></span></a>\r\n                                        <a href=\"#nogo\" id=\"tab-review-best\">好评<span class=\"review-count-best\"></span></a>\r\n                                        <a href=\"#nogo\" id=\"tab-review-good\">中评<span class=\"review-count-good\"></span></a>\r\n                                        <a href=\"#nogo\" id=\"tab-review-bad\">差评<span class=\"review-count-bad\"></span></a>\r\n                                        <a title=\"隐私举报\" class=\"btn-report fr\" href=\"#report\">隐私举报</a>\r\n                                    </div>\r\n                                    <ul id=\"review-panel\"></ul>\r\n                                    <div class=\"more-btn\" id=\"btn-review-more\"  data-type=\"\" data-start=\"0\"><a hidefocus href=\"#nogo\" class=\"icon-1\">查看更多评论</a></div>\r\n                                </div>\r\n\r\n                                <div class=\"no-scmt-list\">\r\n                                    <span>该应用暂时还没有评论，沙发空缺中。<br><a href=\"#nogo\">抢先评论 &gt;&gt;</a></span>\r\n                                </div>\r\n\r\n                            <!--评论js模板-->\r\n                            <script type=\"text/template\" id=\"review-loading-template\">\r\n                                <li>评论加载中</li>\r\n                            </script>\r\n\r\n                            <script type=\"text/template\" id=\"review-none-template\">\r\n                                <li style=\"text-align:center;\">该应用暂时还没有{text}</li>\r\n                            </script>\r\n\r\n                            <script type=\"text/template\" id=\"review-item-template\">\r\n                                <li class=\"{type}\">\r\n                                    <span class=\"scmt-result\">{commentText}</span>\r\n                                    <img src=\"http://p5.qhimg.com/d/inn/1e3715bc/space.gif\" _src=\"{image_url}\" alt=\"{username}\">\r\n                                    <div class=\"scmt-cont\">\r\n                                        <p><span class=\"scmt-usr\">{username}</span><span style=\"word-break:break-all;\">{content}</span></p>\r\n                                        <p class=\"last\">{create_time}</p>\r\n                                    </div>\r\n                                </li>\r\n                            </script>\r\n                            <script type=\"text/template\" id=\"review-change-template\">\r\n                                还可以输入 <em>{size}</em> 字\r\n                            </script>\r\n                            <script type=\"text/template\" id=\"review-over-template\">\r\n                                已经超出 <em style=\"color:#f00;\">{size}</em> 字\r\n                            </script>\r\n                        </div>\r\n                    </div>\r\n                </div>\r\n            </div>\r\n\r\n                        <div class=\"pf-box h-pf\" id=\"votePanel\">\r\n                <h2>应用评分：<span>8.8</span>分</h2>\r\n                <p class=\"all-pf\">共 <span class=\"vote-num\"></span> 份评分</p>\r\n                <ul class=\"pf-lt vote-detail\"></ul>\r\n                <p class=\"txt\">大家的选择就是正确的<br>选择，你值得拥有</p>\r\n                <div class=\"oth\">\r\n                    <p>请先下载此应用，才可对其评分。</p>\r\n                    <span>我来评分：</span>\r\n                    <ul id=\"starList\">\r\n                        <li><a href=\"#nogo\" class=\"star-1\"><span>很差</span></a></li>\r\n                        <li><a href=\"#nogo\" class=\"star-2\"><span>不好</span></a></li>\r\n                        <li><a href=\"#nogo\" class=\"star-3\"><span>一般</span></a></li>\r\n                        <li><a href=\"#nogo\" class=\"star-4\"><span>不错</span></a></li>\r\n                        <li><a href=\"#nogo\" class=\"star-5\"><span>很好</span></a></li>\r\n                    </ul>\r\n                </div>\r\n            </div>\r\n\r\n            <!--安全提示-->\r\n            <div class=\"tips-box green\" id=\"safeTiperPanel\">\r\n                <span class=\"arrow\"></span>\r\n                <h2><span class=\"icon-5\"></span>通过360手机卫士检测</h2>\r\n                <h3>安全无毒，可放心下载。</h3>\r\n                <p></p>\r\n            </div>\r\n\r\n            <!--广告提示-->\r\n            <div class=\"tips-box blue\" id=\"adsPanel\">\r\n                <span class=\"arrow\"></span>\r\n                <h2><span class=\"icon-3\"></span>广告类型</h2>\r\n                <p>\r\n                                    - 有内置广告<br>\r\n                                    - 有积分墙广告<br>\r\n                                    - 有插屏广告<br>\r\n                                </p>\r\n            </div>\r\n\r\n            <!--付费提示-->\r\n            <div class=\"tips-box blue\" id=\"feePanel\">\r\n                <span class=\"arrow\"></span>\r\n                <h2><span class=\"icon-3\"></span>付费项目</h2>\r\n                <p>- 道具付费</p>\r\n            </div>\r\n\r\n            <!--市场来源-->\r\n            <div class=\"from-link\" id=\"maket-list\"></div>\r\n\r\n            <!--权限-->\r\n            <div class=\"tips-box red\" id=\"authority-panel\">\r\n            <span class=\"arrow\"></span>\r\n            <h2><span class=\"icon-4\"></span>需要调用以下重要权限</h2>\r\n            <p>\r\n            \r\n                    -&nbsp;发送短信</br>-&nbsp;读取联系人</br>-&nbsp;读取短信内容</br>-&nbsp;获取手机地理位置</br>\r\n                \r\n                    -&nbsp;访问网络</br>-&nbsp;获取网络状态</br>-&nbsp;读取电话状态</br>-&nbsp;获取WiFi状态</br>-&nbsp;编写短信</br>-&nbsp;接收短信</br>-&nbsp;拨打电话</br>\r\n                            </p>\r\n            </div>\r\n\r\n            <div class=\"verify_tips\" id=\"verify_tips\"></div>\r\n            <div class=\"white_tips\" id=\"white_tips\"></div>\r\n            \r\n            \r\n\r\n            <!-- 登录与评论 见qcms_script 1100516484 -->\r\n            <script type=\"text/javascript\" src=\"http://s3.qhimg.com/static/621ef9519beb12ce.js\" info=\"登录与评论\"></script>\r\n            <script type=\"text/javascript\" src=\"/script/detail_pc.js\" info=\"detail_pc.js\"></script>\r\n\r\n        </div>\r\n        <!--end warp-->\r\n        <!--包含静态页脚-->\r\n        <div class=\"ft\">\r\n\t<p>Copyright&copy;2005-2013 360.CN All Rights Reserved 360安全中心 <a href=\"#nogo\" class=\"blue2\" id=\"fbk_btm\">意见反馈</a></p>\r\n</div>\r\n<!--end ft-->\r\n        <!-- test 老导航统计 还在用吗？ -->\r\n        <!--\r\n        <script src=\"http://hao.360.cn/css/monitorscript.js?v=110720.js\"></script>\r\n        <script>moniter.setUrl('http://zsall.mobilem.360.cn/detail/index/soft_id/').getTrack()</script>\r\n        -->\r\n    </body>\r\n    <script src=\"http://w.qhimg.com/images/v2/mobi/script/webhelper/detail.js\"></script>\r\n    <script src=\"http://w.qhimg.com/images/v2/mobi/script/webhelper/uarec.120207.1.js\"></script>\r\n    <script src=\"/script/feedback.130717.js\"></script>\r\n\r\n    <script type=\"text/javascript\">\r\n        var pageTitle = '会说话的汤姆猫2_360手机助手';\r\n        var pageForm = '' || \"defaultZS\";\r\n        var pageSid = '10283';\r\n\r\n        // 20131215 打点\r\n        var pageLog = {\r\n            uniqueId : (function(){\r\n                var time = +new Date() + \"-\",\r\n                    index = 0;\r\n\r\n                return function(){\r\n                    return time + (++index);\r\n                };\r\n            })(),\r\n\r\n            send : function(url, callback){\r\n                var uid = this.uniqueId(),\r\n                    data = window['imageLogData'] || (window['imageLogData'] = {}),\r\n                    image = data[uid] = new Image();\r\n                image.onerror = image.onload = function(){\r\n                    callback && callback();\r\n                    image.onerror = image.onload = null;\r\n                    image = null;\r\n                    delete data[uid];\r\n                };\r\n                image.src = url + \"&_=\" + uid;\r\n            }\r\n        };\r\n        if(pageForm){\r\n            // 详情页的下载 不包括猜你喜欢\r\n            $(\".js-downLog\").click(function(){\r\n                var target = $(this);\r\n                var sid = target.data(\"sid\");\r\n\r\n                var logUrl = 'http://s.np.mobilem.360.cn/s.html?suid=&url=&refer=&cid=' + pageForm + '&sid=' + sid + '&method=1&p=&action=soft_download&title=' + pageTitle;\r\n                pageLog.send(logUrl);\r\n            });\r\n            pageLog.send('http://s.np.mobilem.360.cn/s.html?suid=&url=' + encodeURIComponent(location.href.replace(/#.*$/, '')) + '&refer=&cid=' + pageForm + '&sid=' + pageSid + '&method=0&p=detail&action=&title=' + pageTitle);\r\n        }\r\n    </script>\r\n    <script src=\"http://s8.qhimg.com/!74b6f0bc/user.1438.js\"></script>\r\n\r\n    <script src=\"http://s0.qhimg.com/monitor/;monitor/2edd36ee.js\"></script>\r\n    <script type=\"text/javascript\">\r\n        monitor.setProject(\"shoujizhushou_help\");\r\n                    monitor.setUrl('http://zhushou.360.cn/detail/index/soft_id/');\r\n                    monitor.getTrack().getClickAndKeydown();\r\n    </script>\r\n\r\n    <!--[if lte IE 6]>\r\n    <script src=\"http://w.qhimg.com/images/v2/mobi/script/webhelper/fixie6.120320.js\"></script>\r\n    <script>\r\n    //DD_belatedPNG.fix('.png , .msoft dd a.dbtn,.stars,.stars span'); // argument is a CSS selector\r\n    </script>\r\n\r\n    <![endif]-->\r\n\r\n    <!--VM试玩-->\r\n    \r\n</html>\r\n"
                  //string desc = "(?s)(<div class=\"breif\"(.*?)>\r\n                                               (?<desc>.*?)                                                  <div class=\"base-info\">\r\n                                                        <br/><b>【基本信息】</b><br/>\r\n                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n                                                            <tbody>\r\n                                                                <tr>\r\n                                                                    <td width=\"50%\"><strong>作者：</strong>(?<au>.*?)</td>\r\n                                                                    <td width=\"50%\"><strong>更新时间：</strong>(?<updatetime>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td><strong>版本：</strong>(?<ver>.*?)<!--(.*?)--><!--updatetime:(.*?)--></td>\r\n                                                                    <td><strong>系统：</strong>(?<sys>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td colspan=\"2\"><strong>语言：</strong>(.*?)</td>\r\n                                                                </tr>\r\n                                                            </tbody>\r\n                                                        </table>\r\n                                                    </div>\r\n\r\n                                                    \r\n                                                    <br/><b>【更新内容】</b><br/>游戏特色：<br />\r\n(?<updatedesc>.*?)                                            </div>\r\n)";
                
              //string desc = "(?s)(<div class=\"breif\"(.*?)>\r\n                                                (?<desc>.*?)                                                    <div class=\"base-info\">\r\n                                                        <br/><b>【基本信息】</b><br/>\r\n                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n                                                            <tbody>\r\n                                                                <tr>\r\n                                                                    <td width=\"50%\"><strong>作者：</strong>(?<au>.*?)</td>\r\n                                                                    <td width=\"50%\"><strong>更新时间：</strong>(?<updatetime>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td><strong>版本：</strong>(?<ver>.*?)<!--(.*?)--><!--updatetime:(.*?)--></td>\r\n                                                                    <td><strong>系统：</strong>(?<sys>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td colspan=\"2\"><strong>语言：</strong>(.*?)</td>\r\n                                                                </tr>\r\n                                                            </tbody>\r\n                                                        </table>\r\n                                                    </div>\r\n\r\n                                                    \r\n                                                    <br/><b>【更新内容】</b><br/>游戏特色：<br />\r\n(?<updatedesc>.*?)                                            </div>\r\n)";                                       
                string desc = "(?s)(<div class=\"breif\"(.*?)>\r\n                                                (?<desc>.*?)                                                    <div class=\"base-info\">\r\n                                                        <br/><b>【基本信息】</b><br/>\r\n                                                        <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n                                                            <tbody>\r\n                                                                <tr>\r\n                                                                    <td width=\"50%\"><strong>作者：</strong>(?<au>.*?)</td>\r\n                                                                    <td width=\"50%\"><strong>更新时间：</strong>(?<updatetime>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td><strong>版本：</strong>(?<ver>.*?)<!--(.*?)--><!--updatetime:(.*?)--></td>\r\n                                                                    <td><strong>系统：</strong>(?<sys>.*?)</td>\r\n                                                                </tr>\r\n                                                                <tr>\r\n                                                                    <td colspan=\"2\"><strong>语言：</strong>(.*?)</td>\r\n                                                                </tr>\r\n                                                            </tbody>\r\n                                                        </table>\r\n                                                    </div>\r\n\r\n                                                    \r\n                                                    <br/><b>【更新内容】</b><br/>(?<updatedesc>.*?)                                            </div>)";
                Match ml2 = Regex.Match(str, desc);
                if (ml2.Success)
                {
                    txtdesc.Text = ml2.Groups["desc"].Value.Replace("<br/>", "\n");
                    txtversion.Text = ml2.Groups["ver"].Value;
                    txtauthor.Text = ml2.Groups["au"].Value;
                    txtupdatetime.Text = ml2.Groups["updatetime"].Value;
                    txtUpdatedesc.Text = ml2.Groups["updatedesc"].Value.Replace("<br/>", "\n");
                    txtsystem.Text = ml2.Groups["sys"].Value;
                }
            }
        }
        protected void Button2_Click(object sender, EventArgs e)
        {
            if (TextBox1.Text == "")
            {
                this.Alert("请输入url");
            }
            else
            {
                string str = GetWebClient(TextBox1.Text);
                Bind(str);

                string str2 = "(?s)(<div class=\"html-brief\" id=\"html-brief\">(?<desc>.*?)<div class=\"base-info\">\r\n                                                    <br/><b>【基本信息】</b><br/>\r\n                                                    <table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">\r\n                                                        <tbody>\r\n                                                            <tr>\r\n                                                                <td width=\"50%\"><strong>作者：</strong>(?<au>.*?)</td>\r\n                                                                <td width=\"50%\"><strong>更新时间：</strong>(?<updatetime>.*?)</td>\r\n                                                            </tr>\r\n                                                            <tr>\r\n                                                                <td><strong>版本：</strong>(?<ver>.*?)<!--versioncode:(.*?)--><!--updatetime:(.*?)--></td>\r\n                                                                <td><strong>系统：</strong>(?<sys>.*?)</td>\r\n                                                            </tr>\r\n                                                            <tr>\r\n                                                                <td colspan=\"2\"><strong>语言：</strong>(.*?)</td>\r\n                                                            </tr>\r\n                                                        </tbody>\r\n                                                    </table>\r\n                                                </div>\r\n\r\n                                                \r\n                                                <br/><b>【更新内容】</b><br/>(?<updatedesc>.*?)                                        </div>)";
                Match ml2 = Regex.Match(str, str2);
                if (ml2.Success)
                {
                    txtdesc.Text = ml2.Groups["desc"].Value.Replace("<br/>", "\n");
                    txtversion.Text = ml2.Groups["ver"].Value;
                    txtauthor.Text = ml2.Groups["au"].Value;
                    txtupdatetime.Text = ml2.Groups["updatetime"].Value;
                    txtUpdatedesc.Text = ml2.Groups["updatedesc"].Value.Replace("<br />", "\n");
                    txtsystem.Text = ml2.Groups["sys"].Value;
                    string desc = ml2.Groups["desc"].Value.Replace("<br/>", "\n");

                    //Regex reg2 = new Regex(@"(?<desc>[\u4E00-\u9FA5]*|(\\d))", RegexOptions.IgnoreCase);
                    //MatchCollection mc2 = reg2.Matches(desc);
                    //string value="";
                    //foreach (Match m in mc2)
                    //{
                    //   value += m.Groups["desc"].Value;
                    //}
                    //value = value.Replace("微软雅黑","");
                    Regex reg2 = new Regex("(?s)(>(?<desc>.*?)<)", RegexOptions.IgnoreCase);
                    MatchCollection mc2 = reg2.Matches(desc);
                    string value = "";
                    foreach (Match m in mc2)
                    {
                        value += m.Groups["desc"].Value;
                    }

                    txtdesc.Text = value;

                    string pics = @"<img alt="" src="" />";
                    IList<string> im = new List<string>();//定义一个泛型字符类
                    Regex reg = new Regex(@"<img.*?src=""(?<src>[^""]*)""[^>]*>", RegexOptions.IgnoreCase);
                    MatchCollection mc = reg.Matches(desc); //设定要查找的字符串
                    foreach (Match m in mc)
                    {
                        im.Add(m.Groups["src"].Value);
                    }

                    for (int i = 0; i < im.Count(); i++)
                    {
                        switch (i)
                        {
                            case 0:
                                Image2.ImageUrl = im[i];
                                Image2.Visible = true;
                                break;
                            case 1:
                                Image3.ImageUrl = im[i];
                                Image3.Visible = true;
                                break;
                            case 2:
                                Image4.ImageUrl = im[i];
                                Image4.Visible = true;
                                break;
                            case 3:
                                Image5.ImageUrl = im[i];
                                Image5.Visible = true;
                                break;
                            default:
                                break;
                        }

                    }

                }
            }
        }
        public void Clear()
        {
            txtauthor.Text = "";
            txtdesc.Text = "";
            txtname.Text = "";
            txtpackName.Text = "";
            txtpackurl.Text = "";
            txtsize.Text = "";
            txtsystem.Text = "";
            txttimes.Text = "";
            txtUpdatedesc.Text = "";
            txtupdatetime.Text = "";
            txtversion.Text = "";
            Image1.ImageUrl = "";
            Image2.ImageUrl = "";
            Image3.ImageUrl = "";
            Image4.ImageUrl = "";
            Image5.ImageUrl = "";
            Image6.ImageUrl = "";
        }
        public void Bind(string str)
        {
            Clear();

            string restr = "<div id=\"app-info-panel\">\r\n                                                            <a href=\"http://zhushou.360.cn/peifu/\" class=\"jiao\">(.*?)</a>\r\n                                                        <div class=\"product btn_type1\">\r\n                                <dl class=\"clearfix\">\r\n                                    <dt><img src=\"(?<Img>.*?)\" width=\"72\" height=\"72\" alt=\"(.*?)\"></dt>\r\n                                    <dd>\r\n                                        <h2 id=\"app-name\"><span title=\"(.*?)\">(?<Name>.*?)</span><cite class=\"verify_tag\"></cite></h2>\r\n                                        <div class=\"pf\">\r\n                                            <span class=\"s-1 js-votepanel\">(.*?)<em>分</em></span>\r\n                                            <span class=\"s-2\"><a href=\"#comment-list\" id=\"comment-num\"><span class=\"js-comments review-count-all\" style=\"margin:0;\">(.*?)</span>条评价</a></span>\r\n                                            <span class=\"s-3\">下载：(?<Times>.*?)次</span>\r\n                                            <span class=\"s-3\">(?<Size>.*?)</span>\r\n                                        </div>\r\n                                                                                    <p><strong>(.*?)</strong>(.*?)</p>\r\n                                                                                                                        <a class=\"(.*?)\" href=\"(?<packName>.*?)\" data-sid=\"(?<Id>.*?)\" data-sname=\"(.*?)\" data-pos=\"(.*?)\">立即安装</a>\r\n                                    </dd>\r\n                                </dl>\r\n                            </div>\r\n                        </div>\r\n\r\n                     ";
            Match m = Regex.Match(str, restr);
            if (m.Success)
            {
                Image1.ImageUrl = m.Groups["Img"].Value;
                Image1.Visible = true;
                txtname.Text = m.Groups["Name"].Value;
                txttimes.Text = m.Groups["Times"].Value;
            }
            int sta = str.IndexOf("'size':'");
            int end = str.IndexOf("'mkid':");
            string restr3 = "{" + str.Substring(sta, end - sta).Replace("\r\n                ", "").TrimEnd(',').Replace("'", "\"").Replace("://", "@") + "}";
            string json = "";
            foreach (string eachInfo in restr3.Split(','))
            {
                if (string.IsNullOrEmpty(eachInfo))
                    break;
                json += Tools.GetStr(eachInfo.Split(':')[1], "") + ",";
            }
            string[] json2 = json.Split(',');
            txtsize.Text = json2[0].Replace("\"", "");
            string packrul = json2[1].Replace("@", "://").Replace("\"", "").Replace("}", "");
            string packname = packrul.Substring(packrul.LastIndexOf("/") + 1, (packrul.IndexOf("_") - 1) - packrul.LastIndexOf("/"));
            txtpackurl.Text = packrul;
            txtpackName.Text = packname;

        }

        protected void Button3_Click(object sender, EventArgs e)
        {
            AppInfoEntity appInfo = new AppInfoEntity();

            //主安装包ID
            appInfo.MainPackID = 0;
            //应用名称
            appInfo.AppName = this.txtname.Text.Trim();
            //显示名称
            appInfo.ShowName = this.txtname.Text.Trim();
            //适用设备类型，定义：1=手机，2=平板，4=...位运算
            appInfo.ForDeviceType = 0;
            //包名
            appInfo.PackName = this.txtpackName.Text.Trim();
            //包签名
            appInfo.PackSign = string.Empty;
            //开发者ID
            appInfo.CPID = 0;
            //分发类型：1=不分渠道，2=分渠道分发
            appInfo.IssueType = 1;
            //多个渠道号,只有当IssueType=2时生效。逗号分隔，首尾要加上逗号
            appInfo.ChannelNos = "";
            //开发者名
            appInfo.DevName = this.txtauthor.Text.Trim();
            appInfo.ChannelAdaptation = ",70,";
            //应用分类
            appInfo.AppClass = Convert.ToInt32(this.AppType.SelectedValue);
            //是否网游，定义：1=网游，2=单机
            appInfo.IsNetGame = Convert.ToInt32(this.IsNetGame.SelectedValue);
            //邪恶等级，1~5表示从纯洁到邪恶
            appInfo.EvilLevel = 0;
            //推荐值，0~10代表从不推荐到推荐
            appInfo.RecommLevel = Convert.ToInt32(DroRecommLevel.SelectedValue);
            //推荐语
            appInfo.RecommWord = "";
            //缩略图URL
            //appInfo.ThumbPicUrl = this.ThumbPicUrl.Value;
            //应用描述
            appInfo.AppDesc = this.txtdesc.Text.Trim();
            //搜索关键字
            appInfo.SearchKeys = "";
            //下载量，定期更新（不影响更新时间）
            string times = this.txttimes.Text.Trim();

            times = times.Replace("万", "0000");

            appInfo.DownTimes = Convert.ToInt32(times);
            //备注
            appInfo.Remarks = "";
            //状态:1=正常，2=禁用，12=数据异常，22=控制禁用
            appInfo.Status = 99;
            //主版本号
            appInfo.MainVerName = string.Empty;
            //主版本代码
            appInfo.MainVerCode = 0;
            //签名特征码
            appInfo.MainSignCode = string.Empty;
            //主ICON图URL地址
            appInfo.MainIconUrl = this.Image1.ImageUrl;
            //推荐标签，编辑指定，1=推荐，2=热门，4=官方...位运算
            appInfo.RecommTag = 0;
            //分发类型：1=不分渠道，2=分渠道分发
            //appInfo.IssueType = this.IssueType.SelectedValue.Convert<int>(1);
            //联运游戏ID
            appInfo.UAppID = 0;
            //应用类型
            appInfo.AppType = Convert.ToInt32(this.AppType.SelectedValue);
            //数据状态，定义：1=正常，2=异常
            appInfo.DataStatus = 2;

            if (new AppInfoBLL().IsExistAppInfo(appInfo.PackName))
            {
                this.Alert("当前应用已经存在，请修改后重试");
                return;
            }

            //写入数据库
            //int result = new AppInfoBLL().Insert(appInfo);
            int NewId = new AppInfoBLL().Insert(appInfo);
            PackInfoEntity Entity = new PackInfoEntity()
        {
            ShowName = txtname.Text,
            AppID = NewId,
            IconUrl2 = Image1.ImageUrl,
            PackUrl2 = txtpackurl.Text.Trim(),
            Status = 99,
            UpdateDesc = txtUpdatedesc.Text,
            PackSize = Convert.ToInt32(txtsize.Text),
            VerCode = 0,
            VerName = "",
            UpdateTime = DateTime.Now,
            CreateTime = DateTime.Now,
            CommentScore = 0,
            CommentTimes = 0,
            CompDesc = txtsystem.Text,
            CoopType = 99,
            DownTimes = Convert.ToInt32(txttimes.Text.Replace("万", "0000")),
            DownTimesReal = 0,
            EndIndex = 0,
            IsMainVer = 1,
            IssueFlag = "",
            OrderNo = 0,
            PackFrom = 2,
            PackMD5 = "",
            PackName = txtpackName.Text,
            PackSign = "",
            PackUrl = "",
            Remarks = "",
            StartIndex = 0,
            //VerCode = Convert.ToInt32(txtversion.Text.ToString()),
            AppPicUrl2 = Image2.ImageUrl + "," + Image3.ImageUrl + "," + Image4.ImageUrl + "," + Image5.ImageUrl + "," + Image6.ImageUrl
        };
            //新增安装包
            int rult = new PackInfoBLL().InsertByAuto(Entity);
            if (rult > 0)
            {

                AppInfoEntity appInfoEntity = new AppInfoEntity()
                {
                    AppID = NewId,
                    MainPackID = rult,
                    MainPackSize = Entity.PackSize,
                    MainVerCode = 0,
                    MainVerName = "",
                    MainIconUrl = Entity.IconUrl2,
                    PackName = Entity.PackName,
                    PackSign = "",
                    UpdateTime = DateTime.Now,
                    OpUpdateTime = DateTime.Now,

                };
                new AppInfoBLL().UpDatePackInfo(appInfoEntity);
            }
            if (NewId > 0 && rult > 0)
            {
                this.Alert("添加成功");
                Response.Redirect("AutoCollectList.aspx");
            }
            else
            {
                this.Alert("添加失败");
                Response.Redirect("AutoCollectList.aspx");
            }
        }
        /// <summary>
        /// 绑定应用类型
        /// </summary>
        private void BindAppType()
        {
            List<GroupTypeEntity> list = new GroupTypeBLL().GetDataList(12);
            ListItem objItem = new ListItem("分类筛选", "0");
            this.AppType.Items.Add(objItem);

            foreach (GroupTypeEntity item in list)
            {
                if (item.TypeID == 1100 || item.TypeID == 1200)
                {
                    continue;
                }
                else
                {
                    this.AppType.Items.Add(new ListItem(item.TypeName, item.TypeID.ToString()));
                }
            }

            List<ChannelEntity> chList = new ChannelBLL().BindList();
            //cbChannel.DataSource = chList;
            //cbChannel.DataTextField = "ChannelName";
            //cbChannel.DataValueField = "ChannelNo";
            //cbChannel.DataBind();
        }

        //protected void Button4_Click(object sender, EventArgs e)
        //{
        //    if (txtpics1.Text !="")
        //    {
        //        Image2.ImageUrl=txtpics1.Text;
        //        Image2.Visible = true;
        //    }
        //    else if (txtpics2.Text != "")
        //    {
        //        Image3.ImageUrl = txtpics2.Text;
        //        Image3.Visible = true;
        //    }
        //    else if (txtpics3.Text != "")
        //    {
        //        Image4.ImageUrl = txtpics3.Text;
        //        Image4.Visible = true;
        //    }

        //    else if (txtpics4.Text != "")
        //    {
        //        Image5.ImageUrl = txtpics4.Text;
        //        Image5.Visible = true;
        //    }
        //    else if (txtpics5.Text != "")
        //    {
        //        Image6.ImageUrl = txtpics5.Text;
        //        Image6.Visible = true;
        //    }
        //}
    }
}
