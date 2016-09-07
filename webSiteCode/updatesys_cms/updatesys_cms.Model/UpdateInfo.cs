using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace updatesys_cms.Model
{
    public class UpdateInfo
    {
        public int UpdateId { get; set; }

        public int AppId { get; set; }

        public int SchemeId { get; set; }

        public string AppName { get; set; }

        public string PackName { get; set; }

        public string ChannelNo { get; set; }

        public string VerName { get; set; }

        public int VerCode { get; set; }

        public string PackUrl { get; set; }

        public int PackSize { get; set; }

        public string PackMD5 { get; set; }

        public int UpdateType { get; set; }

        public string UpdatePrompt { get; set; }

        public string UpdateDesc { get; set; }

        public DateTime PubTime { get; set; }

        public int Status { get; set; }

        public int ForceUpdateVerCode { get; set; }
    }
}
