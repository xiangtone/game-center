
package com.hykj.gamecenter.protocol;

// Generated by the protocol buffer compiler.  DO NOT EDIT!

@SuppressWarnings("hiding")
public interface Reported {

    public static final class ReqReported extends
            com.google.protobuf.nano.MessageNano {

        private static volatile ReqReported[] _emptyArray;

        public static ReqReported[] emptyArray() {
            // Lazily initializes the empty array
            if (_emptyArray == null) {
                synchronized (com.google.protobuf.nano.InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ReqReported[0];
                    }
                }
            }
            return _emptyArray;
        }

        // repeated .ReportedInfo reportedInfo = 1;
        public Reported.ReportedInfo[] reportedInfo;

        public ReqReported() {
            clear();
        }

        public ReqReported clear() {
            reportedInfo = Reported.ReportedInfo.emptyArray();
            cachedSize = -1;
            return this;
        }

        @Override
        public void writeTo(com.google.protobuf.nano.CodedOutputByteBufferNano output)
                throws java.io.IOException {
            if (this.reportedInfo != null && this.reportedInfo.length > 0) {
                for (int i = 0; i < this.reportedInfo.length; i++) {
                    Reported.ReportedInfo element = this.reportedInfo[i];
                    if (element != null) {
                        output.writeMessage(1, element);
                    }
                }
            }
            super.writeTo(output);
        }

        @Override
        protected int computeSerializedSize() {
            int size = super.computeSerializedSize();
            if (this.reportedInfo != null && this.reportedInfo.length > 0) {
                for (int i = 0; i < this.reportedInfo.length; i++) {
                    Reported.ReportedInfo element = this.reportedInfo[i];
                    if (element != null) {
                        size += com.google.protobuf.nano.CodedOutputByteBufferNano
                                .computeMessageSize(1, element);
                    }
                }
            }
            return size;
        }

        @Override
        public ReqReported mergeFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    default: {
                        if (!com.google.protobuf.nano.WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                    }
                    case 10: {
                        int arrayLength = com.google.protobuf.nano.WireFormatNano
                                .getRepeatedFieldArrayLength(input, 10);
                        int i = this.reportedInfo == null ? 0 : this.reportedInfo.length;
                        Reported.ReportedInfo[] newArray =
                                new Reported.ReportedInfo[i + arrayLength];
                        if (i != 0) {
                            java.lang.System.arraycopy(this.reportedInfo, 0, newArray, 0, i);
                        }
                        for (; i < newArray.length - 1; i++) {
                            newArray[i] = new Reported.ReportedInfo();
                            input.readMessage(newArray[i]);
                            input.readTag();
                        }
                        // Last one without readTag.
                        newArray[i] = new Reported.ReportedInfo();
                        input.readMessage(newArray[i]);
                        this.reportedInfo = newArray;
                        break;
                    }
                }
            }
        }

        public static ReqReported parseFrom(byte[] data)
                throws com.google.protobuf.nano.InvalidProtocolBufferNanoException {
            return com.google.protobuf.nano.MessageNano.mergeFrom(new ReqReported(), data);
        }

        public static ReqReported parseFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            return new ReqReported().mergeFrom(input);
        }
    }

    public static final class ReportedInfo extends
            com.google.protobuf.nano.MessageNano {

        private static volatile ReportedInfo[] _emptyArray;

        public static ReportedInfo[] emptyArray() {
            // Lazily initializes the empty array
            if (_emptyArray == null) {
                synchronized (com.google.protobuf.nano.InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new ReportedInfo[0];
                    }
                }
            }
            return _emptyArray;
        }

        // required int32 statActId = 1;
        public int statActId;

        // optional int32 statActId2 = 2;
        public int statActId2;

        // optional string actionTime = 3;
        public java.lang.String actionTime;

        // optional string ext1 = 4;
        public java.lang.String ext1;

        // optional string ext2 = 5;
        public java.lang.String ext2;

        // optional string ext3 = 6;
        public java.lang.String ext3;

        // optional string ext4 = 7;
        public java.lang.String ext4;

        // optional string ext5 = 8;
        public java.lang.String ext5;

        public ReportedInfo() {
            clear();
        }

        public ReportedInfo clear() {
            statActId = 0;
            statActId2 = 0;
            actionTime = "";
            ext1 = "";
            ext2 = "";
            ext3 = "";
            ext4 = "";
            ext5 = "";
            cachedSize = -1;
            return this;
        }

        @Override
        public void writeTo(com.google.protobuf.nano.CodedOutputByteBufferNano output)
                throws java.io.IOException {
            output.writeInt32(1, this.statActId);
            if (this.statActId2 != 0) {
                output.writeInt32(2, this.statActId2);
            }
            if (!this.actionTime.equals("")) {
                output.writeString(3, this.actionTime);
            }
            if (!this.ext1.equals("")) {
                output.writeString(4, this.ext1);
            }
            if (!this.ext2.equals("")) {
                output.writeString(5, this.ext2);
            }
            if (!this.ext3.equals("")) {
                output.writeString(6, this.ext3);
            }
            if (!this.ext4.equals("")) {
                output.writeString(7, this.ext4);
            }
            if (!this.ext5.equals("")) {
                output.writeString(8, this.ext5);
            }
            super.writeTo(output);
        }

        @Override
        protected int computeSerializedSize() {
            int size = super.computeSerializedSize();
            size += com.google.protobuf.nano.CodedOutputByteBufferNano
                    .computeInt32Size(1, this.statActId);
            if (this.statActId2 != 0) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeInt32Size(2, this.statActId2);
            }
            if (!this.actionTime.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(3, this.actionTime);
            }
            if (!this.ext1.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(4, this.ext1);
            }
            if (!this.ext2.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(5, this.ext2);
            }
            if (!this.ext3.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(6, this.ext3);
            }
            if (!this.ext4.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(7, this.ext4);
            }
            if (!this.ext5.equals("")) {
                size += com.google.protobuf.nano.CodedOutputByteBufferNano
                        .computeStringSize(8, this.ext5);
            }
            return size;
        }

        @Override
        public ReportedInfo mergeFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    default: {
                        if (!com.google.protobuf.nano.WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                    }
                    case 8: {
                        this.statActId = input.readInt32();
                        break;
                    }
                    case 16: {
                        this.statActId2 = input.readInt32();
                        break;
                    }
                    case 26: {
                        this.actionTime = input.readString();
                        break;
                    }
                    case 34: {
                        this.ext1 = input.readString();
                        break;
                    }
                    case 42: {
                        this.ext2 = input.readString();
                        break;
                    }
                    case 50: {
                        this.ext3 = input.readString();
                        break;
                    }
                    case 58: {
                        this.ext4 = input.readString();
                        break;
                    }
                    case 66: {
                        this.ext5 = input.readString();
                        break;
                    }
                }
            }
        }

        public static ReportedInfo parseFrom(byte[] data)
                throws com.google.protobuf.nano.InvalidProtocolBufferNanoException {
            return com.google.protobuf.nano.MessageNano.mergeFrom(new ReportedInfo(), data);
        }

        public static ReportedInfo parseFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            return new ReportedInfo().mergeFrom(input);
        }
    }

    public static final class RspReported extends
            com.google.protobuf.nano.MessageNano {

        private static volatile RspReported[] _emptyArray;

        public static RspReported[] emptyArray() {
            // Lazily initializes the empty array
            if (_emptyArray == null) {
                synchronized (com.google.protobuf.nano.InternalNano.LAZY_INIT_LOCK) {
                    if (_emptyArray == null) {
                        _emptyArray = new RspReported[0];
                    }
                }
            }
            return _emptyArray;
        }

        // required int32 rescode = 1;
        public int rescode;

        // required string resmsg = 2;
        public java.lang.String resmsg;

        public RspReported() {
            clear();
        }

        public RspReported clear() {
            rescode = 0;
            resmsg = "";
            cachedSize = -1;
            return this;
        }

        @Override
        public void writeTo(com.google.protobuf.nano.CodedOutputByteBufferNano output)
                throws java.io.IOException {
            output.writeInt32(1, this.rescode);
            output.writeString(2, this.resmsg);
            super.writeTo(output);
        }

        @Override
        protected int computeSerializedSize() {
            int size = super.computeSerializedSize();
            size += com.google.protobuf.nano.CodedOutputByteBufferNano
                    .computeInt32Size(1, this.rescode);
            size += com.google.protobuf.nano.CodedOutputByteBufferNano
                    .computeStringSize(2, this.resmsg);
            return size;
        }

        @Override
        public RspReported mergeFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            while (true) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        return this;
                    default: {
                        if (!com.google.protobuf.nano.WireFormatNano.parseUnknownField(input, tag)) {
                            return this;
                        }
                        break;
                    }
                    case 8: {
                        this.rescode = input.readInt32();
                        break;
                    }
                    case 18: {
                        this.resmsg = input.readString();
                        break;
                    }
                }
            }
        }

        public static RspReported parseFrom(byte[] data)
                throws com.google.protobuf.nano.InvalidProtocolBufferNanoException {
            return com.google.protobuf.nano.MessageNano.mergeFrom(new RspReported(), data);
        }

        public static RspReported parseFrom(
                com.google.protobuf.nano.CodedInputByteBufferNano input)
                throws java.io.IOException {
            return new RspReported().mergeFrom(input);
        }
    }
}
