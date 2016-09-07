using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace updatesys_cms.Web
{
    public partial class test : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            
            var oldList= new List<int>();
            
            for (int i = 0; i < 20; i++)
            {
                oldList.Add(i);
            }

            IEnumerable<int> list = oldList;

            foreach (int eachItem in list)
            {
                Response.Write(eachItem + ",");
            }
        }
    }
}