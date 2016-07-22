using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using AppStore.Common;
using nwbase_utils;
using System.IO;
using System.Xml;
using nwbase_auth;
using System.Data;
using System.Threading;
using System.Text;
using System.Web.UI.WebControls;
using System.Web.UI;
using System.Web.UI.HtmlControls;


namespace AppStore.Web
{
    public class BasePage : AuthPageBase
    {

        #region 参数属性
        /// <summary>
        /// 关键字
        /// </summary>
        public string Keyword_1 { get { return this.Request<string>("Keyword_1", string.Empty); } }
        public string Keyword_2 { get { return this.Request<string>("Keyword_2", string.Empty); } }
        public string Keyword_3 { get { return this.Request<string>("Keyword_3", string.Empty); } }
        public string Keyword_4 { get { return this.Request<string>("Keyword_4", string.Empty); } }
        public string Keyword_5 { get { return this.Request<string>("Keyword_5", string.Empty); } }
        public string Keyword_6 { get { return this.Request<string>("Keyword_6", string.Empty); } }
        public string Keyword_7 { get { return this.Request<string>("Keyword_7", string.Empty); } }
        public string Keyword_8 { get { return this.Request<string>("Keyword_8", string.Empty); } }
        public string Keyword_9 { get { return this.Request<string>("Keyword_9", string.Empty); } }
        public string Keyword_10 { get { return this.Request<string>("Keyword_10", string.Empty); } }

        public bool UnAuthLoginFlag { get; set; }


        public static string UserShowName;
        /// <summary>
        /// 用于 删，改，时的主键
        /// </summary>
        public int Id { get { return this.Request<string>("Id", string.Empty).Convert<int>(); } }


        public bool IsValidate { get { return Extensions.AppSettings<bool>("IsValidate", false); } }

        /// <summary>
        /// 操作方式
        /// </summary>
        public string Action { get { return this.Request<string>("Action", string.Empty); } }

        public string UploadUrl { get { return Extensions.AppSettings("UploadUrl", ""); } }

        #endregion
        #region 继承属性
        ///// <summary>
        ///// 用户对象
        ///// </summary>
        //protected nwbase_auth.Model.AuthUser AuthUser = null;
        ///// <summary>
        ///// 模块对象
        ///// </summary>
        //protected nwbase_auth.Model.AuthModule AuthModule = null;

        ///// <summary>
        ///// 是否已登录
        ///// </summary>
        //public static bool IsLogin = false;

        ///// <summary>
        ///// 是否已授权
        ///// </summary>
        //protected static bool IsAuth = false;

        ///// <summary>
        ///// 验证主域名，默认为cms.niuwan.cc
        ///// </summary>
        //protected string AuthDomain = "asus.cms.niuwan.cc";
        #endregion

        public BasePage()
        {
            UnAuthLoginFlag = true;
        }

        public BasePage(bool unAuthLoginFlag)
        {
            UnAuthLoginFlag = unAuthLoginFlag;
        }

        /// <summary> 
        /// 页面初始化时
        /// </summary>
        /// <param name="e"></param>
        protected override void OnInit(EventArgs e)
        {
            if (IsValidate)
            {
                var hr = Request;
                HttpCookie cookies = Request.Cookies["userCert"];
                if (cookies != null)
                {
                    base.OnInit(e);
                }
                else
                {
                    Response.Write("<script type=\"text/javascript\">parent.location.href = 'login.aspx'</script>");
                    Response.End();
                }
            }

        }

        /// <summary>
        /// 未授权时触发
        /// </summary>
        protected override void OnUnAuth()
        {
            //string unAuthHTML = string.Format("<html><head><script type=\"text/javascript\">document.domain = '{0}';top.UserLogin();</script></head></html>", AuthDomain);
            //Response.Write(unAuthHTML);
            //Response.End();

            //if (UnAuthLoginFlag == true)
            //{
            //    Response.Write("没有权限访问该页面！");
            //    Response.End();
            //}
        }


        /// <summary>
        /// 设置Android权限列表
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public string AndroidPermission(string str)
        {
            string data = "";
            //str = "android.permission.INTERNET#android.permission.READ_PHONE_STATE#android.permission.ACCESS_NETWORK_STATE#android.permission.ACCESS_WIFI_STATE#android.permission.WRITE_EXTERNAL_STORAGE#android.permission.READ_EXTERNAL_STORAGE#android.permission.ACCESS_FINE_LOCATION#android.permission.GET_TASKS#android.permission.WAKE_LOCK#";
            try
            {
                if (str != "")
                {
                    //判断配置信息文件是否存在
                    if (File.Exists(Server.MapPath("~/AndroidPermission/Android.xml")))
                    {
                        //读取配置文件信息
                        XmlDocument doc = new XmlDocument();
                        doc.Load(Server.MapPath("~/AndroidPermission/Android.xml"));
                        XmlNode xn = doc.SelectSingleNode("Worksheet");
                        XmlNodeList xnl = xn.ChildNodes;

                        data = "";
                        string d1 = "";
                        string d2 = "";
                        List<string> list = new List<String>();
                        string[] a = str.Split('#');
                        foreach (string i in a)
                        {
                            if (i != "")
                            {
                                string value = i.Substring(i.LastIndexOf('.') + 1, i.Length - (i.LastIndexOf('.') + 1));
                                //nwbase_utils.TextLog.Default.Info(value);

                                foreach (XmlNode xn1 in xnl)
                                {
                                    XmlElement x = (XmlElement)xn1;
                                    // 得到节点的所有子节点
                                    XmlNodeList xnc = x.ChildNodes;
                                    string val = xnc.Item(0).InnerText;
                                    string key = xnc.Item(1).InnerText;
                                    string desc = xnc.Item(2).InnerText;
                                    string priority = "";
                                    int c = xnc.Count;
                                    if (xnc.Count > 3)
                                    {
                                        priority = xnc.Item(3).InnerText;
                                    }

                                    if (key == value)
                                    {
                                        if (priority == "1")
                                        {
                                            if (!list.Contains(value))
                                            {
                                                list.Add(value);
                                                d1 += value + ",";
                                            }
                                        }
                                        else
                                        {
                                            if (!list.Contains(value))
                                            {
                                                list.Add(value);
                                                d2 += value + ",";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        data += d1 + d2;

                    }
                    else
                    {
                        nwbase_utils.TextLog.Default.Info("文件不存在");

                    }
                    data = data.TrimEnd(',') + "";
                }

                return data;
            }
            catch (Exception e)
            {
                nwbase_utils.TextLog.Default.Info(e.Message);
                return str;
            }
        }


        /// <summary>
        /// 设置Android权限列表
        /// </summary>
        /// <param name="str"></param>
        /// <returns></returns>
        public string GetAndroidPermission(string str)
        {
            string data = "";
            //str = "android.permission.INTERNET#android.permission.READ_PHONE_STATE#android.permission.ACCESS_NETWORK_STATE#android.permission.ACCESS_WIFI_STATE#android.permission.WRITE_EXTERNAL_STORAGE#android.permission.READ_EXTERNAL_STORAGE#android.permission.ACCESS_FINE_LOCATION#android.permission.GET_TASKS#android.permission.WAKE_LOCK#";
            try
            {

                if (str != "")
                {
                    //判断配置信息文件是否存在
                    if (File.Exists(Server.MapPath("~/AndroidPermission/Android.xml")))
                    {
                        //读取配置文件信息
                        XmlDocument doc = new XmlDocument();
                        doc.Load(Server.MapPath("~/AndroidPermission/Android.xml"));
                        XmlNode xn = doc.SelectSingleNode("Worksheet");
                        XmlNodeList xnl = xn.ChildNodes;

                        data = "";
                        string d1 = "";
                        string d2 = "";
                        string[] a = str.Split(',');
                        foreach (string i in a)
                        {
                            if (i != "")
                            {
                                //string value = i.Substring(i.LastIndexOf('.') + 1, i.Length - (i.LastIndexOf('.') + 1));
                                foreach (XmlNode xn1 in xnl)
                                {
                                    XmlElement x = (XmlElement)xn1;
                                    // 得到节点的所有子节点
                                    XmlNodeList xnc = x.ChildNodes;
                                    string val = xnc.Item(0).InnerText;
                                    string key = xnc.Item(1).InnerText;
                                    string desc = xnc.Item(2).InnerText;
                                    string priority = "";
                                    int c = xnc.Count;
                                    if (xnc.Count > 3)
                                    {
                                        priority = xnc.Item(3).InnerText;
                                    }
                                    if (key == i)
                                    {
                                        data += val + ",";
                                    }
                                }
                            }
                        }

                    }
                    data = data.TrimEnd(',') + "";
                }

                return data;
            }
            catch (Exception e)
            {
                nwbase_utils.TextLog.Default.Info(e.Message);
                return str;
            }

        }
        public string GetPer(string str)
        {
            string data = "";
            try
            {
                //判断配置信息文件是否存在
                if (File.Exists(Server.MapPath("~/AndroidPermission/Android.xml")))
                {
                    //读取配置文件信息
                    XmlDocument doc = new XmlDocument();
                    doc.Load(Server.MapPath("~/AndroidPermission/Android.xml"));
                    XmlNode xn = doc.SelectSingleNode("Worksheet");
                    XmlNodeList xnl = xn.ChildNodes;
                    foreach (XmlNode xn1 in xnl)
                    {
                        XmlElement x = (XmlElement)xn1;
                        // 得到节点的所有子节点
                        XmlNodeList xnc = x.ChildNodes;
                        string key = xnc.Item(0).InnerText;
                        string value = xnc.Item(1).InnerText;
                        if (key == str)
                        {
                            data = value;
                        }
                    }
                }
                else
                {
                    nwbase_utils.TextLog.Default.Info("");
                }
            }
            catch (Exception e)
            {
                nwbase_utils.TextLog.Default.Info(e.Message);
                return str;
            }

            return data;
        }


        public string GetUserName()
        {
            if (IsValidate)
            {
                try
                {
                    return AuthUser.UserName;
                }
                catch (Exception)
                {

                    return "";
                }

            }
            else
            {
                return "";
            }
        }

        public string ServerMapPath()
        {
            return ResolveUrl("~") + "/Export/";
            //return "/Export/";
        }
        public void FindPath(string path)
        {
            if (Directory.Exists(Server.MapPath(ServerMapPath())) == false)
            {
                Directory.CreateDirectory(Server.MapPath(ServerMapPath()));//如果文件夹不存在，创建文件夹
            }
            if (!File.Exists(path))
            {
                //FileStream myFs = new FileStream(path, FileMode.Create);//如果文件不存在，创建文件
                //myFs.Close();
                File.Create(path).Close();
            }
            Thread.Sleep(1000);

        }
        /// <summary>
        /// 写入文件
        /// </summary>
        /// <param name="ds"></param>
        /// <param name="path"></param>
        /// <param name="fileName"></param>
        public void WriteExcel(DataTable ds, string path, string fileName)
        {
            try
            {
                FindPath(path);
                StreamWriter sw = new StreamWriter(path, false, Encoding.GetEncoding("gb2312"));
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < ds.Columns.Count; k++)
                {
                    sb.Append(ds.Columns[k].ColumnName.ToString() + "\t");
                }
                sb.Append(Environment.NewLine);
                for (int i = 0; i < ds.Rows.Count; i++)
                {
                    for (int j = 0; j < ds.Columns.Count; j++)
                    {
                        sb.Append(ds.Rows[i][j].ToString() + "\t");
                    }
                    sb.Append(Environment.NewLine);
                }
                sw.Write(sb.ToString());
                sw.Flush();
                sw.Close();
            }
            catch (Exception)
            {

                throw;
            }
        }
        /// <summary>
        /// 导出报表
        /// </summary>
        /// <param name="ds"></param>
        /// <param name="path"></param>
        /// <param name="fileName"></param>
        public void WriteAppInfoExcel(DataTable ds, string path, string fileName)
        {
            try
            {
                FindPath(path);
                StreamWriter sw = new StreamWriter(path, false, Encoding.GetEncoding("gb2312"));
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < ds.Columns.Count; k++)
                {
                    if (ds.Columns[k].ColumnName.ToString() == "GroupElemID")
                    {
                        sb.Append("ID\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "ElemID")
                    {
                        sb.Append("游戏ID\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "RecommTitle")
                    {
                        sb.Append("推荐名称\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "RecommPicUrl")
                    {
                        sb.Append("ico图标url\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "Status")
                    {
                        sb.Append("状态\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "CreateTime")
                    {
                        sb.Append("添加时间\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "StartTime")
                    {
                        sb.Append("开始时间\t");
                    }
                    else if (ds.Columns[k].ColumnName.ToString() == "EndTime")
                    {
                        sb.Append("结束时间\t");
                    }
                    //else
                    //{
                    //    sb.Append(ds.Columns[k].ColumnName.ToString() + "\t");
                    //}
                }
                sb.Append(Environment.NewLine);
                List<string> list = new List<string>() { "GroupElemID","ElemID", "RecommTitle", "RecommPicUrl", "CreateTime", "StartTime", "EndTime" };
                //list.Add("ElemID");
                for (int i = 0; i < ds.Rows.Count; i++)
                {
                    for (int j = 0; j < ds.Columns.Count; j++)
                    {
                        if (list.Contains(ds.Columns[j].ColumnName.ToString()))
                        {
                            sb.Append(ds.Rows[i][j].ToString() + "\t");
                        }
                        else if (ds.Columns[j].ColumnName.ToString() == "Status")
                        {
                            sb.Append(BindStatus(ds.Rows[i][j].ToString()) + "\t");
                        }

                    }
                    sb.Append(Environment.NewLine);
                }
                sw.Write(sb.ToString());
                sw.Flush();
                sw.Close();
            }
            catch (Exception)
            {

                throw;
            }
        }
       
        /// <summary>
        /// 下载文件
        /// </summary>
        /// <param name="fileRpath"></param>
        public void DownloadFile(string fileRpath)
        {
            Response.ClearHeaders();
            Response.Clear();
            Response.Expires = 0;
            Response.Buffer = true;
            Response.AddHeader("Accept-Language", "zh-tw");
            string name = System.IO.Path.GetFileName(fileRpath);
            System.IO.FileStream files = new FileStream(fileRpath, FileMode.Open, FileAccess.Read, FileShare.Read);
            byte[] byteFile = null;
            if (files.Length == 0)
            {
                byteFile = new byte[1];
            }
            else
            {
                byteFile = new byte[files.Length];
            }
            files.Read(byteFile, 0, (int)byteFile.Length);
            files.Close();
            Response.AddHeader("Content-Disposition", "attachment;filename=" + HttpUtility.UrlEncode(name, System.Text.Encoding.UTF8));
            Response.ContentType = "application/octet-stream;charset=gbk";
            Response.BinaryWrite(byteFile);
            Response.End();
        }

        /// <summary>
        /// 导出Repeater控件报表
        /// </summary>
        /// <param name="objRepeater"></param>
        /// <param name="name"></param>
        public void ExcelExport(Repeater objRepeater, string name)
        {
            StringBuilder sb = new StringBuilder();
            StringWriter sw = new StringWriter(sb);
            HtmlTextWriter htw = new HtmlTextWriter(sw);
            Page page = new Page();
            HtmlForm form = new HtmlForm();
            objRepeater.EnableViewState = false;
            page.EnableEventValidation = false;
            page.DesignerInitialize();
            page.Controls.Add(form);
            form.Controls.Add(objRepeater);
            page.RenderControl(htw);
            Response.Clear();
            Response.Buffer = true;
            Response.Charset = "utf-8";
            Response.ContentEncoding = Encoding.UTF8; //System.Text.Encoding.GetEncoding("gbk");
            //Response.ContentEncoding = Encoding.Default;
            Response.ContentType = "application/ms-excel";
            Response.AddHeader("Content-Disposition", "attachment;filename=" + HttpUtility.UrlEncode(name + ".xls", Encoding.UTF8).ToString());
            Response.Write("<meta http-equiv=\"content-type\" content=\"application/vnd.ms-excel; charset=utf-8\"/>" + sb.ToString());
            Response.End();
        }
        /// <summary>
        /// 判定状态显示
        /// </summary>
        /// <param name="val"></param>
        /// <returns></returns>
        public string BindStatus(object val)
        {
            string status_val = "";
            if (val.ToString() != "" && val != null)
            {
                int Status = Convert.ToInt32(val);
                if (Status == 1)
                {
                    status_val = "正常";
                }
                else if (Status == 2)
                {
                    status_val = "已下架";
                }
                else if (Status == 6)
                {
                    status_val = "待审核";
                }
                else if (Status == 7)
                {
                    status_val = "审核未通过";
                }
                else if (Status == 4)
                {
                    status_val = "接入中";
                }
                else if (Status == 5)
                {
                    status_val = "测试中";
                }
            }
            return status_val;
        }
    }
}