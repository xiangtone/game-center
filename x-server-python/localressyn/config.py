#-*- encoding=utf8 -*-
GLOBAL_SETTINGS = {
    "load_global": True,
    "redis": {
        'db':1,
        'host':'127.0.0.1',
        'port':55501
    },
    # 'syslog': {
    #     'ip':'127.0.0.1',
    #     'port':'44514',
    #     'level':'info',
    #     'name': 'stat'
    # },
    'textlog': {
       'path': "D:\\log\\ressynlocal.log",#win
       #'path': "/var/log/localressyn.log",#linux
       'level': 'info'
    },
    "db": {
        'name':'appstore',
        'user':'root',
        #'psw': 'AP2IJDqB5yt5',#linux test
        'psw':'1234',#win dev
        'host':"127.0.0.1:55511"
    },
    "stat": {
        "svcid": 16,
        "autolog":True
    }
}
