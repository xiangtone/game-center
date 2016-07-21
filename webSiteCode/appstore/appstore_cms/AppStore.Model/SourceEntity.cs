using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class SourceEntity
    {
        public class SourceItems
        {
            public int resID { get; set; }

            public int appID { get; set; }

            public string oldResUrl { get; set; }

            public string newResUrl { get; set; }

            public int cID { get; set; }

        }

        public List<SourceItems> data { get; set; }

    }

    public class ResEntity
    {
        public class ResItems
        {
            public int resID { get; set; }

            public int resCode { get; set; }
        }

        public List<ResItems> res { get; set; }
    }
}
