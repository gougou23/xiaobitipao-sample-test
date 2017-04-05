package com.xiaobitipao.sample.test.netty.runtime;

import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;

/**
 * Marshalling 工厂
 */
public final class MarshallingCodeCFactory {

    /**
     * 创建 Jboss Marshalling 解码器 MarshallingDecoder
     * 
     * @return MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {

        // 1.通过 Marshalling 工具类获取 Marshalling 实例对象。参数 serial 标识创建的是 java 序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        // 2.创建 MarshallingConfiguration 对象，配置版本号为 5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);

        // 3.根据 marshallerFactory 和 configuration 创建 provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);

        // 4.构建 Netty 的 MarshallingDecoder 对象。俩个参数分别为 provider 和单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024);

        return decoder;
    }

    /**
     * 创建 Jboss Marshalling 编码器 MarshallingEncoder
     * 
     * @return MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {

        // 1.通过 Marshalling 工具类获取 Marshalling 实例对象。参数 serial 标识创建的是 java 序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");

        // 2.创建 MarshallingConfiguration 对象，配置版本号为 5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);

        // 3.根据 marshallerFactory 和 configuration 创建 provider
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);

        // 4.构建Netty的MarshallingEncoder对象。MarshallingEncoder用于实现序列化接口的POJO对象序列化为二进制数组
        MarshallingEncoder encoder = new MarshallingEncoder(provider);

        return encoder;
    }
}
