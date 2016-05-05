#-*- encoding=utf8 -*-
TORNADO_SETTINGS = {
    "debug": True,
    "static_path": '/static',
    "gzip": False,
    "xsrf_cookies": False,
    #"cookie_secret":"",
    "template_path": "templates",
}
GLOBAL_SETTINGS = {
    "default_port": 8091,
    "load_global": True,
    "redis": {
        'db': 2,
        'socket_timeout': 3
    },
    'logger': {
        'level': 'info',
        'name': 'updater',
    },

    "stat": {
        "svcid": 6,
        "autolog": True,
    }
}
