package com.alan6.rpc.server.handler;

import com.alan6.rpc.common.RpcRequest;
import com.alan6.rpc.common.RpcResponse;
import com.alan6.rpc.common.util.StringUtil;
import com.alan6.rpc.server.cache.RpcServerCache;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.springframework.stereotype.Component;


@Slf4j
@ChannelHandler.Sharable
@Component
public class RpcServerHandlerDispather extends AbstractRpcServerHandler {

    @Override
    protected void doRequest(ChannelHandlerContext ctx, RpcRequest request) throws Exception {
        log.info("================ 开始处理Rpc request ====================");
        Channel channel = ctx.channel();

        Object result = this.handle(request);

        if (result != null){
            RpcResponse rpcResponse = RpcResponse.builder().build();
            rpcResponse.setResult(result);

            // 写入 RPC 响应结果
            ctx.writeAndFlush(rpcResponse);
        }else {

        }
    }

    private Object handle(RpcRequest request) throws Exception {
        // 获取服务对象
        String serviceName = request.getServiceName();
        String serviceVersion = request.getServiceVersion();
        if (StringUtil.isNotEmpty(serviceVersion)) {
            serviceName += "-" + serviceVersion;
        }

        // 查询bean map缓存
        Object serviceBean = RpcServerCache.getServiceBeanMap().get(serviceName);
        if (serviceBean != null) {
            // 获取反射调用所需的参数
            Class<?> serviceClass = serviceBean.getClass();
            String methodName = request.getMethodName();
            Class<?>[] parameterTypes = request.getParameterTypes();
            Object[] parameters = request.getParameters();

            // 执行反射调用
            FastClass serviceFastClass = FastClass.create(serviceClass);
            FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
            return serviceFastMethod.invoke(serviceBean, parameters);
        }else {
            log.error("Cannot find service bean in bean map: %s", serviceName);
            return RpcResponse.builder().error("no rpc service");
        }
    }
}
