# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: Packet.proto

from google.protobuf.internal import enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)




DESCRIPTOR = _descriptor.FileDescriptor(
  name='Packet.proto',
  package='',
  serialized_pb='\n\x0cPacket.proto\"\xbf\x01\n\tReqPacket\x12\x0c\n\x04mask\x18\x01 \x02(\x05\x12\x0b\n\x03udi\x18\x02 \x02(\t\x12\x0e\n\x06\x61\x63tion\x18\x03 \x03(\t\x12\x0e\n\x06params\x18\x04 \x03(\x0c\x12\r\n\x05reqNo\x18\x05 \x02(\x05\x12\r\n\x05\x63hnNo\x18\x06 \x01(\x05\x12\x0e\n\x06\x63hnPos\x18\x07 \x01(\x05\x12\x10\n\x08\x63lientId\x18\x08 \x02(\x05\x12\x11\n\tclientPos\x18\t \x01(\x05\x12\x11\n\tclientVer\x18\n \x01(\t\x12\x11\n\trsaKeyVer\x18\x0b \x01(\t\"Z\n\tRspPacket\x12\x0c\n\x04mask\x18\x01 \x02(\x05\x12\x0f\n\x07rescode\x18\x02 \x02(\x05\x12\x0e\n\x06resmsg\x18\x03 \x02(\t\x12\x0e\n\x06\x61\x63tion\x18\x04 \x03(\t\x12\x0e\n\x06params\x18\x05 \x03(\x0c\"+\n\tRspRsaKey\x12\x0e\n\x06rsaVer\x18\x01 \x02(\t\x12\x0e\n\x06rsaKey\x18\x02 \x02(\t*8\n\x08MaskCode\x12\x0b\n\x07\x44\x45\x46\x41ULT\x10\x00\x12\x0f\n\x0bPARAMS_GZIP\x10\x01\x12\x0e\n\nPARAMS_RSA\x10\x02')

_MASKCODE = _descriptor.EnumDescriptor(
  name='MaskCode',
  full_name='MaskCode',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='DEFAULT', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='PARAMS_GZIP', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='PARAMS_RSA', index=2, number=2,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=347,
  serialized_end=403,
)

MaskCode = enum_type_wrapper.EnumTypeWrapper(_MASKCODE)
DEFAULT = 0
PARAMS_GZIP = 1
PARAMS_RSA = 2



_REQPACKET = _descriptor.Descriptor(
  name='ReqPacket',
  full_name='ReqPacket',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='mask', full_name='ReqPacket.mask', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='udi', full_name='ReqPacket.udi', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='action', full_name='ReqPacket.action', index=2,
      number=3, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='params', full_name='ReqPacket.params', index=3,
      number=4, type=12, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='reqNo', full_name='ReqPacket.reqNo', index=4,
      number=5, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='chnNo', full_name='ReqPacket.chnNo', index=5,
      number=6, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='chnPos', full_name='ReqPacket.chnPos', index=6,
      number=7, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='clientId', full_name='ReqPacket.clientId', index=7,
      number=8, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='clientPos', full_name='ReqPacket.clientPos', index=8,
      number=9, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='clientVer', full_name='ReqPacket.clientVer', index=9,
      number=10, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='rsaKeyVer', full_name='ReqPacket.rsaKeyVer', index=10,
      number=11, type=9, cpp_type=9, label=1,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=17,
  serialized_end=208,
)


_RSPPACKET = _descriptor.Descriptor(
  name='RspPacket',
  full_name='RspPacket',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='mask', full_name='RspPacket.mask', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='rescode', full_name='RspPacket.rescode', index=1,
      number=2, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='resmsg', full_name='RspPacket.resmsg', index=2,
      number=3, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='action', full_name='RspPacket.action', index=3,
      number=4, type=9, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='params', full_name='RspPacket.params', index=4,
      number=5, type=12, cpp_type=9, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=210,
  serialized_end=300,
)


_RSPRSAKEY = _descriptor.Descriptor(
  name='RspRsaKey',
  full_name='RspRsaKey',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='rsaVer', full_name='RspRsaKey.rsaVer', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='rsaKey', full_name='RspRsaKey.rsaKey', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  extension_ranges=[],
  serialized_start=302,
  serialized_end=345,
)

DESCRIPTOR.message_types_by_name['ReqPacket'] = _REQPACKET
DESCRIPTOR.message_types_by_name['RspPacket'] = _RSPPACKET
DESCRIPTOR.message_types_by_name['RspRsaKey'] = _RSPRSAKEY

class ReqPacket(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REQPACKET

  # @@protoc_insertion_point(class_scope:ReqPacket)

class RspPacket(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _RSPPACKET

  # @@protoc_insertion_point(class_scope:RspPacket)

class RspRsaKey(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _RSPRSAKEY

  # @@protoc_insertion_point(class_scope:RspRsaKey)


# @@protoc_insertion_point(module_scope)