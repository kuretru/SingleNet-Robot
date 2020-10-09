package com.kuretru.android.singlenet.entity;

import lombok.Getter;

@Getter
public enum InterfaceStatusEnum {

    DOWN(0),
    UP(1);

    private Integer code;

    private InterfaceStatusEnum(Integer code) {
        this.code = code;
    }

}
