package com.alan6.rpc.server.starter;

import com.alan6.rpc.server.handler.RpcServerHandlerDispather;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("serverChannelInitializer")
@Slf4j
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private RpcServerHandlerDispather rpcHandlerDispather;

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("encoder", MqttEncoder.INSTANCE)
				.addLast("decoder", new MqttDecoder())
				.addLast(new IdleStateHandler(180, 0, 0))
				.addLast(rpcHandlerDispather);
	}
}
