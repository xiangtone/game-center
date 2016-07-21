using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.Model
{
    public  class AppInformEntity
    {

        private int _informid;
        private string _openid;
        private int _appid = 0;
        private int _clientid = 0;
        private int _channelno = 0;
        private string _version;
        private string _brandflag;
        private string _modelflag;
        private string _deviceflag;
        private int _level = 0;
        private string _informtype;
        private string _informdetail;
        private DateTime _informtime;
        private int _status = 0;
        private DateTime _createtime;
        private DateTime _updatetime;
        private string _remarks;
        /// <summary>
        /// auto_increment
        /// </summary>
        public int InformId
        {
            set { _informid = value; }
            get { return _informid; }
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
        public int AppId
        {
            set { _appid = value; }
            get { return _appid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int ClientId
        {
            set { _clientid = value; }
            get { return _clientid; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int ChannelNo
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
        public int Level
        {
            set { _level = value; }
            get { return _level; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string InformType
        {
            set { _informtype = value; }
            get { return _informtype; }
        }
        /// <summary>
        /// 
        /// </summary>
        public string InformDetail
        {
            set { _informdetail = value; }
            get { return _informdetail; }
        }
        /// <summary>
        /// 
        /// </summary>
        public DateTime InformTime
        {
            set { _informtime = value; }
            get { return _informtime; }
        }
        /// <summary>
        /// 
        /// </summary>
        public int Status
        {
            set { _status = value; }
            get { return _status; }
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
        public DateTime UpdateTime
        {
            set { _updatetime = value; }
            get { return _updatetime; }
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
