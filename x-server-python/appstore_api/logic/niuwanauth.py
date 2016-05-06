#!/bin/env python
#-*- encoding=utf8 -*-
import urllib2
import json
from functools import wraps


REQUEST_TPL = """{"action": "auth", "param": {"user_id":"%(user_id)s", "user_token":"%(token)s", "request_url":"%(url)s"}}"""
REQUEST_URL = "http://cms.niuwan.cc/api/auth.aspx"
LOGIN_URL = "http://cms.niuwan.cc/login.aspx"

def _auth(user_cert, url):
    user_cert_part = user_cert.split('&')
    try:
        if len(user_cert_part) == 2:
            uid, token = user_cert_part
            param = {}
            param['user_id'] = uid
            param['token'] = token
            param['url'] = url
            fl = urllib2.urlopen(REQUEST_URL, REQUEST_TPL % param)
            result = fl.read()
            fl.close()
            if result:
                json_obj = json.loads(result)
                if json_obj and int(json_obj["code"]) == 0:
                    return True
    except Exception, e:
        return False


def auth(func):
    @wraps(func)
    def auth_func(self, *args):
        user_cert = self.get_cookie('userCert')

        if not user_cert or not _auth(user_cert, self.request.full_url()):
            # self.redirect('http://cms.niuwan.cc/login.aspx')
            self.write('<html><script>alert("请先登录");window.location="%s"</script></html>' % LOGIN_URL)
        else:
            func(self, *args)
    return auth_func
