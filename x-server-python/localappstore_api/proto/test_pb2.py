# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: test.proto

from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)




DESCRIPTOR = _descriptor.FileDescriptor(
  name='test.proto',
  package='',
  serialized_pb='\n\ntest.proto\"$\n\x07ReqTest\x12\x0c\n\x04name\x18\x01 \x02(\t\x12\x0b\n\x03\x61ge\x18\x02 \x01(\x05\"9\n\x07RspTest\x12\x0f\n\x07rescode\x18\x01 \x02(\x05\x12\x0e\n\x06resmsg\x18\x02 \x02(\t\x12\r\n\x05\x65xmsg\x18\x03 \x01(\t')




_REQTEST = _descriptor.Descriptor(
  name='ReqTest',
  full_name='ReqTest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='name', full_name='ReqTest.name', index=0,
      number=1, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='age', full_name='ReqTest.age', index=1,
      number=2, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
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
  serialized_start=14,
  serialized_end=50,
)


_RSPTEST = _descriptor.Descriptor(
  name='RspTest',
  full_name='RspTest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='rescode', full_name='RspTest.rescode', index=0,
      number=1, type=5, cpp_type=1, label=2,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='resmsg', full_name='RspTest.resmsg', index=1,
      number=2, type=9, cpp_type=9, label=2,
      has_default_value=False, default_value=unicode("", "utf-8"),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='exmsg', full_name='RspTest.exmsg', index=2,
      number=3, type=9, cpp_type=9, label=1,
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
  serialized_start=52,
  serialized_end=109,
)

DESCRIPTOR.message_types_by_name['ReqTest'] = _REQTEST
DESCRIPTOR.message_types_by_name['RspTest'] = _RSPTEST

class ReqTest(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _REQTEST

  # @@protoc_insertion_point(class_scope:ReqTest)

class RspTest(_message.Message):
  __metaclass__ = _reflection.GeneratedProtocolMessageType
  DESCRIPTOR = _RSPTEST

  # @@protoc_insertion_point(class_scope:RspTest)


# @@protoc_insertion_point(module_scope)
