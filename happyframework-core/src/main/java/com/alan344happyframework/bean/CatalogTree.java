package com.alan344happyframework.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author AlanSun
 * @date 2020/10/27 9:10
 * 目录树
 */
@Getter
@Setter
public class CatalogTree {
    /**
     * 目录名称
     */
    private String catalogName;
    /**
     * 对应的 id
     */
    private String targetId;
    /**
     * 子目录
     */
    private List<CatalogTree> subCatalogTrees;
}
