package com.example.loadbalance;

import java.util.List;

/**
 * @Author cxr
 * @Date 2020/12/27 21:17
 *
 * 负载均衡策略的抽象类
 */
public abstract class AbstractLoadBalance implements LoadBalance{

    @Override
    public String selectServiceAddress(List<String> serviceAddresses) {
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses);
    }

    protected abstract String doSelect(List<String> serviceAddresses);
}
