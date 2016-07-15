#-*- encoding=utf8 -*-
TORNADO_SETTINGS = {
    "debug": False,
    "static_path": '/static',
    "gzip": False,
    "xsrf_cookies": False,
    #"cookie_secret":"",
    #"template_path": "templates",
}
GLOBAL_SETTINGS = {
    "default_port": 42040,
    "load_global": True,
    "cache_prefix":"localapps",
    "redis": {
        'db':1,
        'host':'127.0.0.1',
        'port':55501
    },
    'logger': {
        'level': '127.0.0.1',
        'name': 'stat',
    },
    "db": {
        'name':'appstore',
        'user':'root',
        #'psw': 'AP2IJDqB5yt5',
        'psw':'1234',
        'host':"127.0.0.1:55511"
    },
    "stat": {
        "svcid": 8
    }
}
