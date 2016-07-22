using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

using AppStore.Common;
using System.Text;
using System.Net;
using System.Collections;

namespace AppStore.Web
{
    public partial class PasswdCheck : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.ContentLength > 0)
            {
                string result = "";
                try
                {
                    var requestData = Request.BinaryRead((int)Request.ContentLength);
                    WebClientMyself client = new WebClientMyself();
                    client.Encoding = Encoding.UTF8;
                    System.Net.CookieContainer cc = new System.Net.CookieContainer();
                    result = client.Post("http://cms.niuwan.cc/passport.aspx", requestData, ref cc);
                    List<Cookie> ccList = GetAllCookies(cc);
                    Cookie ck = ccList.Where(t => t.Name == "userCert").FirstOrDefault();
                    if (ck != null)
                    {
                        Response.Cookies.Add(new HttpCookie(ck.Name, ck.Value) { Domain = ck.Domain, Expires = ck.Expires });
                    }
                }
                catch (Exception ex)
                {
                    nwbase_utils.TextLog.Default.Error("PasswdCheck_Error:" + ex.Message);
                }
                Response.Write(result);
                Response.End();
            }
        }

        public List<Cookie> GetAllCookies(CookieContainer cc)
        {
            List<Cookie> lstCookies = new List<Cookie>();
            Hashtable table = (Hashtable)cc.GetType().InvokeMember("m_domainTable",
                System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.GetField |
                System.Reflection.BindingFlags.Instance, null, cc, new object[] { });
            foreach (object pathList in table.Values)
            {
                SortedList lstCookieCol = (SortedList)pathList.GetType().InvokeMember("m_list",
                    System.Reflection.BindingFlags.NonPublic | System.Reflection.BindingFlags.GetField
                    | System.Reflection.BindingFlags.Instance, null, pathList, new object[] { });
                foreach (CookieCollection colCookies in lstCookieCol.Values)
                    foreach (Cookie c in colCookies) lstCookies.Add(c);
            }
            return lstCookies;
        }

    }
}