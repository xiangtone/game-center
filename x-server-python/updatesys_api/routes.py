import logic.protohandler
import logic.monitor
import logic.prototesthandler

URL_PATTERN = [
    (r'.*/api', logic.protohandler.ProtoHandler),
    (r'.*/prototest', logic.prototesthandler.ProtoTestHandler),
]
