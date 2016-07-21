using AppStore.BLL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace AppStore.Web
{
    public partial class AppInformList : BasePage
    {

        public List<AppInformEntity> Appinformt;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                Bind();
            }
        }

        public void Bind()
        {
            Appinformt = new AppInformBLL().GetAppinformList();
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {

            int id = int.Parse(HidID.Value, 0);
            string remarks = txtRemarks.Text.Trim();
            if (id > 0)
            {
                new AppInformBLL().UpdateRemarks(id, remarks);
                Bind();
            }
        }

        public string BindInform(string val)
        {
            string value = "";
            if (val.IndexOf(',') > -1)
            {
                string[] str = val.Split(',');
                foreach (string i in str)
                {
                    value += GetInform(i) + ",";
                }
                value = value.TrimEnd(',');
            }

            return value;
        }
        // 1=强制广告，2=无法安装，3=质量不好，4=版本旧，5=恶意扣费，6=携带病毒
        public string GetInform(string val)
        {
            switch (val)
            {
                case "1":
                    return "强制广告";
                case "2":
                    return "无法安装";
                case "3":
                    return "质量不好";
                case "4":
                    return "版本旧";
                case "5":
                    return "恶意扣费";
                case "6":
                    return "携带病毒";
                default:
                    return "";
            }
        }
        public string GetAppName(int id)
        {
            string str = "";
            AppInfoEntity entity = new AppInfoBLL().GetSingle(id);
            if (entity !=null)
            {
                str = entity.AppName;

            }
            return str;
        }
    }
}