package com.example.rpcdemo.remoting.handler;

import com.example.rpcdemo.exception.RpcException;
import com.example.rpcdemo.factory.SingletonFactory;
import com.example.rpcdemo.provider.ServiceProvider;
import com.example.rpcdemo.provider.ServiceProviderImpl;
import com.example.rpcdemo.remoting.dto.RpcRequest;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author cxr
 * @Date 2020/12/18 18:39
 *
 * 处理 RPC 请求
 */
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest:调用相应的方法，然后返回该方法
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 获取方法执行结果
     *
     * @param rpcRequest 客户端请求
     * @param service    服务端实体
     * @return 目标方法执行的结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("service:[{}] 成功调用方法:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}