package com.kuretru.android.singlenet.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LuciRpcRequest {

    private Integer id;

    private String method;

    private List<String> params;

}
