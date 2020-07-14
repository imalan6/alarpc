package com.alan6.rpc.common;

import lombok.Builder;
import lombok.Data;

/**
 * RPC Response
 * @author huangyong
 */

@Data
@Builder
public class RpcResponse {
    private String requestId;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }
}
