package com.alan6.rpc.common;

import lombok.Data;

/**
 * RPC Request
 * @author huangyong
 */

@Data
public class RpcRequest {
    private String requestId;
    private String serviceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
}