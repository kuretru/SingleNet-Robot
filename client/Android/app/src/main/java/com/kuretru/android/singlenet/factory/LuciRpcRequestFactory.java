package com.kuretru.android.singlenet.factory;

import com.kuretru.android.singlenet.entity.LuciRpcRequest;

import java.util.ArrayList;
import java.util.List;

public class LuciRpcRequestFactory {

    public static LuciRpcRequest auth(String username, String password) {
        List<String> params = new ArrayList<>(2);
        params.add(username);
        params.add(password);
        return new LuciRpcRequest(1, "login", params);
    }

    public static LuciRpcRequest getUsername(String networkInterface) {
        List<String> params = new ArrayList<>(3);
        params.add("network");
        params.add(networkInterface);
        params.add("username");
        return new LuciRpcRequest(2, "get", params);
    }

    public static LuciRpcRequest getPassword(String networkInterface) {
        List<String> params = new ArrayList<>(3);
        params.add("network");
        params.add(networkInterface);
        params.add("password");
        return new LuciRpcRequest(3, "get", params);
    }

    public static LuciRpcRequest ifStatus(String networkInterface) {
        List<String> params = new ArrayList<>(1);
        params.add("/sbin/ifstatus " + networkInterface);
        return new LuciRpcRequest(4, "exec", params);
    }

}
