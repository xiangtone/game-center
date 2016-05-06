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
    "default_port": 10041,
    "load_global": True,
    "redis": {
    },
    'logger': {
        'level': 'info',
        'name': 'stat',
    },
    "db": {
    },
    "stat": {
        "svcid": 8,
    }
}
