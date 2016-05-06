#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.gen
import model.updateinfo as model
import libs.scheme as scheme
import proto.Updater_pb2 as proto

from protohandler import ProtoBase


class ReqUpdate(ProtoBase):
    """获取产品更新
    """

    def __init__(self):
        ProtoBase.__init__(self, proto)

    @tornado.gen.coroutine
    def deal(self):
        result = ''
        # self.request = self.get_req_pack(params)
        # self.response = self.get_rsp_pack()

        # import pdb
        # pdb.set_trace()

        pack_name = self.request.packName

        schemeId = scheme.get_scheme_id(self.comm_params)  # 方案Id
        channel_name = '00'
        if self.request.verName != '' and self.request.verName.find('.') >= 0:
            verName_pars = self.request.verName.split('.')
            if len(verName_pars) > 2:
                channel_name = verName_pars[-1]
        pack_info = model.get_update_info(schemeId, pack_name, channel_name)

        if pack_info is not None and pack_info != {} and int(pack_info['newVerCode']) > self.request.verCode:
            self.response.rescode = 0
            self.response.resmsg = u"存在更新"
            self.response.updateType = int(pack_info['updateType'])
            self.response.packName = pack_info['packName']
            self.response.newVerName = pack_info['newVerName']

            self.response.newVerCode = int(pack_info['newVerCode'])
            self.response.packSize = int(pack_info['packSize'])
            self.response.packMD5 = pack_info['packMD5']
            self.response.packUrl = pack_info['packUrl']
            self.response.pubTime = pack_info['pubTime']
            self.response.updatePrompt = unicode(pack_info['updatePrompt'])
            self.response.updateDesc = unicode(pack_info['updateDesc'])

            # 低版本强制更新，date:2016.1.22
            force_update_vercode = int(pack_info['forceUpdateVerCode'])
            if force_update_vercode and self.request.verCode < force_update_vercode:
                self.response.updateType = 3

        else:
            # 无更新包
            self.response.rescode = 0
            self.response.resmsg = u"暂无更新"
            self.response.updateType = 0


    # def deal_json(self, packName, verName, verCode):
    #     import json
    #     schemeId = 0
    #     channel_name = '00'
    #     if verName != '' and verName.find('.') >= 0:
    #         verName_pars = verName.split('.')
    #         if len(verName_pars) > 2:
    #             channel_name = verName_pars[-1]
    #     pack_info = model.get_update_info(schemeId, packName, channel_name)

    #     result = {}

    #     result['rescode'] = 0
    #     result['resmsg'] = "暂无更新"
    #     result['updateType'] = 0
    #     result['packName'] = ''
    #     result['newVerName'] = ''
    #     result['newVerCode'] = 0
    #     result['packSize'] = 0
    #     result['packMD5'] = ''
    #     result['packUrl'] = ''
    #     result['pubTime'] = ''
    #     result['updatePrompt'] = ''
    #     result['updateDesc'] = ''

    #     if pack_info is not None and pack_info != {} and int(pack_info['newVerCode']) > verCode:
    #         result['rescode'] = 0
    #         result['resmsg'] = "存在更新"
    #         result['updateType'] = int(pack_info['updateType'])
    #         result['packName'] = pack_info['packName']
    #         result['newVerName'] = pack_info['newVerName']
    #         result['newVerCode'] = int(pack_info['newVerCode'])
    #         result['packSize'] = int(pack_info['packSize'])
    #         result['packMD5'] = pack_info['packMD5']
    #         result['packUrl'] = pack_info['packUrl']
    #         result['pubTime'] = pack_info['pubTime']
    #         result['updatePrompt'] = pack_info['updatePrompt']
    #         result['updateDesc'] = pack_info['updateDesc']

    #     return '{"rspUpdate": %s}' % json.dumps(result)
