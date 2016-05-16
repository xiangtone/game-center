#-*- encoding=utf8 -*-
TORNADO_SETTINGS = {
    "debug": False,
    "static_path": '/static',
    "gzip": False,
    "xsrf_cookies": False,

    #"cookie_secret":"",
    # "template_path": "templates",
}
GLOBAL_SETTINGS = {
    "default_port": 42010,
    "load_global": True,
    "cache_prefix": "apps",
    "channel_adaptation_list": [70,20],
    "web_default_channel":70,

    # 架构适配控制的渠道
    "architecture_check_channel":[70],

    # 后台主动控制的更新频率
    "check_update_rate":60*60*24,

    # 是否启用后台控制更新频率
    "control_update_rate":True,
    
    # 是否去除主动配置的并没有适配机器的APP 
    "remove_no_suit_bgconfig_app":True,

    # 控制检测应用升级的版本配置
    "activate_app_update":{
        "control_version":["5.09.25.70"]
    },

    # 更新检测频率控制
    "check_rate":{
        "update_check_rate":60*12,
        "recomm_check_rate":60*24*7,
    },

    # arm,x86列表
    "md_list":{
        "arm_list":['ASUS_Z00UDB','ASUS_Z010DA','ASUS_Z011D','ASUS_T00P'],
        "x86_list":['ASUS_Z00ADB','ASUS_Z00ADA','ASUS_Z00XSB','ASUS_Z00XSA','ASUS_T00J','ASUS_T00F','ASUS_T00G']
    },

    "redis": {
        'db': 1,
        'host':'127.0.0.1',
        'port':55501
    },
    'logger': {
        'syslog': {
            'level':'info',
            'name':'apps'
        },
        #'textlog': {
        #    'path': '/var/log/www/apps.txt',
        #    'level': 'debug'
        #},
    },
    "db": {
        'name': 'appstoreios',
        'user': 'root',
        # 'psw': 'AP2IJDqB5yt5',
        'psw': 'oddshou',
        'host':"127.0.0.1:3306"
    },
    "stat": {
        "svcid": 10,
        "autolog": True,
    },


}
