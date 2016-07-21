using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using AppStore.Common;
using AppStore.BLL;
using AppStore.Model;


namespace AppStore.Web.FindBack
{
    public partial class F_AllCategoryList : BasePage
    {

        public int typeClass { get { return this.Request<int>("typeclass", 0); } }
        public int SchemeID { get { return this.Request<int>("SchemeID", 1); } }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                this.BindData();
            }
        }

        private void BindData()
        {
            List<GroupTypeEntity> list = new List<GroupTypeEntity>();
            if (SchemeID>1)
            {
                list = new GroupTypeBLL().GetCategory(SchemeID);
                 
            }
            else
            {
                list = new GroupTypeBLL().GetAllCategory(typeClass);
            }
            this.objRepeater.DataSource = list;
            this.objRepeater.DataBind();

        }
    }
}