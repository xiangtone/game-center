#!/bin/env python
#-*- encoding=utf8 -*-


class CommStatusCode(object):

    """公共的返回状态
    """

    exception_error = 100
    exception_error_msg = u'异常失败'

    illegal_request = 101
    illegal_request_msg = u'非法请求'

    illegal_user = 102
    illegal_user_msg = u'用户token验证失败'

    illegal_role = 103
    illegal_role_msg = u'角色token验证失败'

    illegal_app = 104
    illegal_app_msg = u'应用token验证失败'

    opera_freq = 105
    opera_freq_msg = u'操作频繁'

    service_busy = 106
    service_busy_msg = u'服务器繁忙'
