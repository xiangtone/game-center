using AppStore.DAL;
using AppStore.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AppStore.BLL
{
    public class ChannelBLL
    {
        /// <summary>
        /// 新增渠道信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public int Insert(ChannelEntity entity)
        {
            return new ChannelDAL().Insert(entity);
        }
        /// <summary>
        /// 更新渠道信息
        /// </summary>
        /// <param name="entity"></param>
        /// <returns></returns>
        public bool Update(ChannelEntity entity)
        {
            return new ChannelDAL().Update(entity);
        }

        /// <summary>
        /// 删除渠道信息（只是禁用）
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool Delete(int ID)
        {
            return new ChannelDAL().Delete(ID);
        }

        /// <summary>
        /// 修改渠道信息状态
        /// </summary>
        /// <param name="ID"></param>
        /// <returns></returns>
        public bool UpdateStatus(int ID, int Status)
        {
            return new ChannelDAL().UpdateStatus(ID, Status);
        }
        /// <summary>
        /// 渠道列表信息
        /// </summary>
        /// <returns></returns>
        public List<ChannelEntity> Select()
        {
            return new ChannelDAL().Select();
        }
               /// <summary>
        /// 绑定渠道列表信息
        /// </summary>
        /// <returns></returns>
        public List<ChannelEntity> BindList()
        {
            return new ChannelDAL().BindList();
        }
        /// <summary>
        /// 查询单个渠道信息
        /// </summary>
        /// <param name="Channelno"></param>
        /// <returns></returns>
        public ChannelEntity SelectByNo(int Channelno)
        {
            return new ChannelDAL().SelectByNo(Channelno);
        }
    }
}
