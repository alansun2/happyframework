package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author AlanSun
 * @date 2020/11/2 13:47
 */
@Getter
@Setter
public class MapLayerCatalogTree {
    /**
     * 名称
     */
    private String name;
    /**
     * 查询地址
     */
    private String url;
    /**
     * 类型
     */
    private Byte type;
    /**
     * 子目录
     */
    private List<MapLayerCatalogTree> subMapLayerCatalogTree = Collections.emptyList();
}
