#!/bin/env python
#-*- encoding=utf8 -*-
import tornado.web
import config as config
import libs.util as util
import model.appinfo as m_appinfo
#import base_utils.logger as nw_logger
#import base_utils.statistic as stat

#logger = nw_logger.GlobalLogger()

from logic.handlers import BaseHandler


class AppJumpHandler(BaseHandler):
    """ 下载安装包
    """
    def get(self):

        redirect_status = False
        result = {"rescode":1001, "resmsg":"pack not found!"}

        try:
            pack_name = self.get_argument("pn", "")
   
            # 记录统计日志
            comm_param = {}
            comm_param["reqaction"] = "AppJumpHandler"
            comm_param["ip"] = util.get_real_ip(self)
            comm_param["logno"] = util.get_log_session(self)
            comm_param['ext1'] = pack_name
            #stat.comm_stat.record(comm_param)
            self.stat.record(comm_param)

            appinfo = m_appinfo.get_appinfo_by_packname(pack_name)
            if appinfo:
                url = ''
                url = appinfo["PackUrl"] or appinfo["PackUrl2"]
                if url.find("cos.myqcloud.com") >= 0:
                    if pack_name == "com.newplay.launcher":
                        url = url + "?res_content_disposition=attachment;filename=%s" % ("lifelauncher.apk")
                    else:
                        url = url + "?res_content_disposition=attachment;filename=%s" % (pack_name + ".apk")
                self.redirect(url)
                redirect_status = True
        except Exception,ex:
            self.logger.write('AppJumpHandler',ex.message)

        if not redirect_status:
            r_json = util.MyJSONEncoder.encode(result)
            self.write(str(r_json))
            self.finish()
    
