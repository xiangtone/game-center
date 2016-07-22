using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public class FeedBackEntity
    {
        private int _fbid;
        private string _openid;
        private string _content;
        private string _usercontact;
        private int? _clientid = 0;
        private string _channelno;
        private string _version;
        private DateTime _createtime;
        private int? _status = 1;
        private DateTime _updatetime;
        private string _brandflag;
        private string _modelflag;
        private string _deviceflag;
        private string _remarks;
        /// <summary>
        /// auto_increment
        /// </summary>
        public int FBId
        {
            set { _fbid = value; }
            get { return _fbid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string OpenId
        {
            set { _openid = value; }
            get { return _openid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string Content
        {
            set { _content = value; }
            get { return _content; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string UserContact
        {
            set { _usercontact = value; }
            get { return _usercontact; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? ClientId
        {
            set { _clientid = value; }
            get { return _clientid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string ChannelNo
        {
            set { _channelno = value; }
            get { return _channelno; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string Version
        {
            set { _version = value; }
            get { return _version; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime CreateTime
        {
            set { _createtime = value; }
            get { return _createtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int? Status
        {
            set { _status = value; }
            get { return _status; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime UpdateTime
        {
            set { _updatetime = value; }
            get { return _updatetime; }
        }

        /// <summary>
        /// 
        /// </summary>
        public string BrandFlag
        {
            set { _brandflag = value; }
            get { return _brandflag; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string ModelFlag
        {
            set { _modelflag = value; }
            get { return _modelflag; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string DeviceFlag
        {
            set { _deviceflag = value; }
            get { return _deviceflag; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string Remarks
        {
            set { _remarks = value; }
            get { return _remarks; }
        }
    }
}
