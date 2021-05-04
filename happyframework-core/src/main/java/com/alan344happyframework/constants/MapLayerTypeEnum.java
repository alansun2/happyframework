package com.alan344happyframework.constants;

import lombok.Getter;

/**
 * @author AlanSun
 * @date 2020/12/1 13:51
 **/
@Getter
public enum MapLayerTypeEnum {

    /**
     * 点
     */
    POINT((byte) 1),
    /**
     * 线
     */
    LINE((byte) 2);

    private final byte type;

    MapLayerTypeEnum(byte type) {
        this.type = type;
    }
}
