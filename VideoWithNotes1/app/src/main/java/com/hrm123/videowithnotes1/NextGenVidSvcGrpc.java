package com.hrm123.nextgenvideosvc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.25.0)",
    comments = "Source: nextgenvidsvc.proto")
public final class NextGenVidSvcGrpc {

  private NextGenVidSvcGrpc() {}

  public static final String SERVICE_NAME = "NextGenVideoService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.Chunk,
      com.hrm123.nextgenvideosvc.SvcResponse> getSaveMp4FileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SaveMp4File",
      requestType = com.hrm123.nextgenvideosvc.Chunk.class,
      responseType = com.hrm123.nextgenvideosvc.SvcResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.Chunk,
      com.hrm123.nextgenvideosvc.SvcResponse> getSaveMp4FileMethod() {
    io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.Chunk, com.hrm123.nextgenvideosvc.SvcResponse> getSaveMp4FileMethod;
    if ((getSaveMp4FileMethod = NextGenVidSvcGrpc.getSaveMp4FileMethod) == null) {
      synchronized (NextGenVidSvcGrpc.class) {
        if ((getSaveMp4FileMethod = NextGenVidSvcGrpc.getSaveMp4FileMethod) == null) {
          NextGenVidSvcGrpc.getSaveMp4FileMethod = getSaveMp4FileMethod =
              io.grpc.MethodDescriptor.<com.hrm123.nextgenvideosvc.Chunk, com.hrm123.nextgenvideosvc.SvcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SaveMp4File"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.Chunk.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.SvcResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getSaveMp4FileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileReq,
      com.hrm123.nextgenvideosvc.Chunk> getGetFileMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetFile",
      requestType = com.hrm123.nextgenvideosvc.FileReq.class,
      responseType = com.hrm123.nextgenvideosvc.Chunk.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileReq,
      com.hrm123.nextgenvideosvc.Chunk> getGetFileMethod() {
    io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileReq, com.hrm123.nextgenvideosvc.Chunk> getGetFileMethod;
    if ((getGetFileMethod = NextGenVidSvcGrpc.getGetFileMethod) == null) {
      synchronized (NextGenVidSvcGrpc.class) {
        if ((getGetFileMethod = NextGenVidSvcGrpc.getGetFileMethod) == null) {
          NextGenVidSvcGrpc.getGetFileMethod = getGetFileMethod =
              io.grpc.MethodDescriptor.<com.hrm123.nextgenvideosvc.FileReq, com.hrm123.nextgenvideosvc.Chunk>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetFile"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.FileReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.Chunk.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetFileMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileListReq,
      com.hrm123.nextgenvideosvc.FileListResp> getListFilesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListFiles",
      requestType = com.hrm123.nextgenvideosvc.FileListReq.class,
      responseType = com.hrm123.nextgenvideosvc.FileListResp.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileListReq,
      com.hrm123.nextgenvideosvc.FileListResp> getListFilesMethod() {
    io.grpc.MethodDescriptor<com.hrm123.nextgenvideosvc.FileListReq, com.hrm123.nextgenvideosvc.FileListResp> getListFilesMethod;
    if ((getListFilesMethod = NextGenVidSvcGrpc.getListFilesMethod) == null) {
      synchronized (NextGenVidSvcGrpc.class) {
        if ((getListFilesMethod = NextGenVidSvcGrpc.getListFilesMethod) == null) {
          NextGenVidSvcGrpc.getListFilesMethod = getListFilesMethod =
              io.grpc.MethodDescriptor.<com.hrm123.nextgenvideosvc.FileListReq, com.hrm123.nextgenvideosvc.FileListResp>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListFiles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.FileListReq.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  com.hrm123.nextgenvideosvc.FileListResp.getDefaultInstance()))
              .build();
        }
      }
    }
    return getListFilesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NextGenVideoServiceStub newStub(io.grpc.Channel channel) {
    return new NextGenVideoServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NextGenVideoServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NextGenVideoServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NextGenVideoServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NextGenVideoServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class NextGenVideoServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.Chunk> saveMp4File(
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.SvcResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getSaveMp4FileMethod(), responseObserver);
    }

    /**
     */
    public void getFile(com.hrm123.nextgenvideosvc.FileReq request,
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.Chunk> responseObserver) {
      asyncUnimplementedUnaryCall(getGetFileMethod(), responseObserver);
    }

    /**
     */
    public void listFiles(com.hrm123.nextgenvideosvc.FileListReq request,
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.FileListResp> responseObserver) {
      asyncUnimplementedUnaryCall(getListFilesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getSaveMp4FileMethod(),
            asyncClientStreamingCall(
              new MethodHandlers<
                com.hrm123.nextgenvideosvc.Chunk,
                com.hrm123.nextgenvideosvc.SvcResponse>(
                  this, METHODID_SAVE_MP4FILE)))
          .addMethod(
            getGetFileMethod(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.hrm123.nextgenvideosvc.FileReq,
                com.hrm123.nextgenvideosvc.Chunk>(
                  this, METHODID_GET_FILE)))
          .addMethod(
            getListFilesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.hrm123.nextgenvideosvc.FileListReq,
                com.hrm123.nextgenvideosvc.FileListResp>(
                  this, METHODID_LIST_FILES)))
          .build();
    }
  }

  /**
   */
  public static final class NextGenVideoServiceStub extends io.grpc.stub.AbstractStub<NextGenVideoServiceStub> {
    private NextGenVideoServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NextGenVideoServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NextGenVideoServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NextGenVideoServiceStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.Chunk> saveMp4File(
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.SvcResponse> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(getSaveMp4FileMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public void getFile(com.hrm123.nextgenvideosvc.FileReq request,
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.Chunk> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetFileMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listFiles(com.hrm123.nextgenvideosvc.FileListReq request,
        io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.FileListResp> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getListFilesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NextGenVideoServiceBlockingStub extends io.grpc.stub.AbstractStub<NextGenVideoServiceBlockingStub> {
    private NextGenVideoServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NextGenVideoServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NextGenVideoServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NextGenVideoServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.hrm123.nextgenvideosvc.Chunk> getFile(
        com.hrm123.nextgenvideosvc.FileReq request) {
      return blockingServerStreamingCall(
          getChannel(), getGetFileMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.hrm123.nextgenvideosvc.FileListResp listFiles(com.hrm123.nextgenvideosvc.FileListReq request) {
      return blockingUnaryCall(
          getChannel(), getListFilesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NextGenVideoServiceFutureStub extends io.grpc.stub.AbstractStub<NextGenVideoServiceFutureStub> {
    private NextGenVideoServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NextGenVideoServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NextGenVideoServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NextGenVideoServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.hrm123.nextgenvideosvc.FileListResp> listFiles(
        com.hrm123.nextgenvideosvc.FileListReq request) {
      return futureUnaryCall(
          getChannel().newCall(getListFilesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_FILE = 0;
  private static final int METHODID_LIST_FILES = 1;
  private static final int METHODID_SAVE_MP4FILE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NextGenVideoServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NextGenVideoServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_FILE:
          serviceImpl.getFile((com.hrm123.nextgenvideosvc.FileReq) request,
              (io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.Chunk>) responseObserver);
          break;
        case METHODID_LIST_FILES:
          serviceImpl.listFiles((com.hrm123.nextgenvideosvc.FileListReq) request,
              (io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.FileListResp>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SAVE_MP4FILE:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.saveMp4File(
              (io.grpc.stub.StreamObserver<com.hrm123.nextgenvideosvc.SvcResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NextGenVidSvcGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getSaveMp4FileMethod())
              .addMethod(getGetFileMethod())
              .addMethod(getListFilesMethod())
              .build();
        }
      }
    }
    return result;
  }
}
