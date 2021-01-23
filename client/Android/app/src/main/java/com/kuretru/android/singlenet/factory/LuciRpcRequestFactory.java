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
        String command = "/sbin/uci set network." + networkInterface + ".username=" + username;
        return new LuciRpcRequest(3, "exec", buildParams(command));
    }

    public static LuciRpcRequest getPassword(String networkInterface) {
        List<String> params = new ArrayList<>(3);
        params.add("network");
        params.add(networkInterface);
        params.add("password");
        return new LuciRpcRequest(4, "get", params);
    }

    public static LuciRpcRequest setPassword(String networkInterface, String password) {
        String command = "/sbin/uci set network." + networkInterface + ".password=" + password;
        return new LuciRpcRequest(5, "exec", buildParams(command));
    }

    public static LuciRpcRequest commit() {
        String command = "/sbin/uci commit network";
        return new LuciRpcRequest(6, "exec", buildParams(command));
    }

    public static LuciRpcRequest getInterfaceStatus(String networkInterface) {
        String command = "/sbin/ifstatus " + networkInterface;
        return new LuciRpcRequest(7, "exec", buildParams(command));
    }

    public static LuciRpcRequest setInterfaceUp(String networkInterface) {
        String command = "/sbin/ifdown " + networkInterface + " && /sbin/ifup " + networkInterface;
        return new LuciRpcRequest(8, "exec", buildParams(command));
    }

    private static List<String> buildParams(String command) {
        List<String> params = new ArrayList<>(1);
        params.add(command);
        return params;
    }

}
