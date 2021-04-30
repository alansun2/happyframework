package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * @author AlanSun
 * @date 2020/12/1 13:28
 * <p>
 * 图层位置通用类
 */
@Getter
@Setter
public class MapLayerPositionCommon {
    /**
     * 点位 id ,一般用于请求详情
     */
    private String pointId;
    /**
     * 点位名称
     */
    private String pointName;
    /**
     * 点位类型：1：点；2: 线
     */
    private Byte pointType;

    /**
     * 当 pointType = 2 时，表示边界
     */
    private List<List<Double>> border = Collections.emptyList();

    /**
     * 当 pointType = 1 是，pointIcon 表示展示图标
     */
    private String pointIcon;
}
