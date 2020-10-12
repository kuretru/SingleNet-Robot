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

    public static LuciRpcRequest setUsername(String networkInterface, String username) {
        List<String> params = new ArrayList<>(4);
        params.add("network");
        params.add(networkInterface);
        params.add("username");
        params.add(username);
        return new LuciRpcRequest(3, "set", params);
    }

    public static LuciRpcRequest getPassword(String networkInterface) {
        List<String> params = new ArrayList<>(3);
        params.add("network");
        params.add(networkInterface);
        params.add("password");
        return new LuciRpcRequest(4, "get", params);
    }

    public static LuciRpcRequest setPassword(String networkInterface, String password) {
        List<String> params = new ArrayList<>(4);
        params.add("network");
        params.add(networkInterface);
        params.add("password");
        params.add(password);
        return new LuciRpcRequest(5, "set", params);
    }

    public static LuciRpcRequest commit() {
        List<String> params = new ArrayList<>(1);
        params.add("network");
        return new LuciRpcRequest(6, "commit", params);
    }

    public static LuciRpcRequest getInterfaceStatus(String networkInterface) {
        List<String> params = new ArrayList<>(1);
        params.add("/sbin/ifstatus " + networkInterface);
        return new LuciRpcRequest(7, "exec", params);
    }

    public static LuciRpcRequest setInterfaceUp(String networkInterface) {
        List<String> params = new ArrayList<>(1);
        params.add("/sbin/ifdown " + networkInterface + " && /sbin/ifup " + networkInterface);
        return new LuciRpcRequest(8, "exec", params);
    }

}
