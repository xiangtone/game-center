using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.BLL;
using AppStore.Common;
using System.Web.Script.Serialization;
using AppStore.Model;

namespace AppStore.Web.API
{
    public partial class InsertAppInfo : System.Web.UI.Page
    {
        //public string AppInfo { get { return this.Request<string>("AppInfo"); } }

        //http://localhost:16436/API/InsertAppInfo.aspx
        //{"AppId":"10003","AppName":"游戏名","PackName":"包名","ChannelAdaptation":"渠道","DevName":"开发者","userName":"zhang"}
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                string rult = "";
                HttpRequest request = HttpContext.Current.Request;
                if (request != null && request.InputStream != null && request.InputStream.Length > 0)
                {
                    byte[] buffer = new byte[request.InputStream.Length];
                    request.InputStream.Read(buffer, 0, (int)request.InputStream.Length);
                    string str = System.Text.Encoding.UTF8.GetString(buffer);
                    JavaScriptSerializer js = new JavaScriptSerializer();   //实例化一个能够序列化数据的类
                    ToJsonMy list = js.Deserialize<ToJsonMy>(str);    //将json数据转化为对象类型并赋值给list
                    //nwbase_utils.TextLog.Default.Info(str);
                    if (list.AppId > 10000 && list.AppId < 100000)
                    {
                        int count = new AppInfoBLL().GetCountById(list.AppId);
                        if (count == 0)
                        {
                            AppInfoEntity appInfo = new AppInfoEntity();

                            appInfo.AppID = list.AppId;
                            //主安装包ID
                            appInfo.MainPackID = 0;
                            //应用名称
                            appInfo.AppName = list.AppName;
                            //显示名称
                            appInfo.ShowName = list.AppName;
                            //适用设备类型，定义：1=手机，2=平板，4=...位运算
                            appInfo.ForDeviceType = 1;
                            //包名
                            appInfo.PackName = list.PackName;
                            //包签名
                            appInfo.PackSign = string.Empty;
                            //开发者ID
                            appInfo.CPID = 0;
                            //分发类型：1=不分渠道，2=分渠道分发
                            appInfo.IssueType = 0;
                            //多个渠道号,只有当IssueType=2时生效。逗号分隔，首尾要加上逗号
                            appInfo.ChannelNos = string.Empty;
                            appInfo.ChannelAdaptation = "," + list.ChannelAdaptation.ToString() + ",";
                            //开发者名
                            appInfo.DevName = list.DevName;
                            //应用分类
                            appInfo.AppClass = 1;
                            //是否网游，定义：1=网游，2=单机
                            appInfo.IsNetGame = 1;
                            //邪恶等级，1~5表示从纯洁到邪恶
                            appInfo.EvilLevel = 0;
                            //推荐值，0~10代表从不推荐到推荐
                            appInfo.RecommLevel = 0;
                            //推荐语
                            appInfo.RecommWord = string.Empty;
                            //缩略图URL
                            appInfo.ThumbPicUrl = string.Empty;
                            //应用描述
                            appInfo.AppDesc = string.Empty;
                            //搜索关键字
                            appInfo.SearchKeys = string.Empty;
                            //下载量，定期更新（不影响更新时间）
                            appInfo.DownTimes = 0;
                            //备注
                            appInfo.Remarks = string.Empty;
                            //状态:状态，定义：1=正常，2=禁用，3=删除，4=接入中 ，5=测试中，6=待审核，12=数据异常，22=控制禁用  ，99=自动获取待上传的， 98=自动获取后删除的
                            appInfo.Status = 4;
                            //主版本号
                            appInfo.MainVerName = string.Empty;
                            //主版本代码
                            appInfo.MainVerCode = 0;
                            //签名特征码
                            appInfo.MainSignCode = string.Empty;
                            //主ICON图URL地址
                            appInfo.MainIconUrl = string.Empty;
                            //推荐标签，编辑指定，1=推荐，2=热门，4=官方...位运算
                            appInfo.RecommTag = 0;
                            //分发类型：1=不分渠道，2=分渠道分发
                            //appInfo.IssueType = this.IssueType.SelectedValue.Convert<int>(1);
                            //联运游戏ID
                            appInfo.UAppID = 0;
                            //应用类型
                            appInfo.AppType = 1204;
                            //数据状态，定义：1=正常，2=异常

                            appInfo.DataStatus = 2;

                            appInfo.CoopType = 1;
                            new AppInfoBLL().Insert(appInfo);
                            if (new AppInfoBLL().GetCountById(list.AppId) > 0)
                            {
                                OperateRecordEntity info = new OperateRecordEntity()
                                {
                                    ElemId = list.AppId,
                                    reason = "",
                                    Status = 1,
                                    OperateFlag = "1",
                                    OperateExplain = "接入游戏",
                                    UserName = list.userName
                                };
                                new OperateRecordBLL().Insert(info);
                                rult = "{\"result\":\"0\",\"msg\":\"添加成功\"}";
                            }
                            else
                            {
                                rult = "{\"result\":\"1\",\"msg\":\"添加失败\"}";
                            }
                        }
                        else
                        {
                            rult = "{\"result\":\"2\",\"msg\":\"游戏已存在\"}";
                        }
                    }
                    else
                    {
                        rult = "{\"result\":\"3\",\"msg\":\"游戏Id范围不正确\"}";
                    }
                }

                Response.Write(rult);
                Response.End();
            }
        }
    }
    public struct ToJsonMy
    {
        public int AppId { get; set; }
        public string AppName { get; set; }
        public string PackName { get; set; }
        public string DevName { get; set; }
        public string userName { get; set; }

        public int ChannelAdaptation { get; set; }

    }
}