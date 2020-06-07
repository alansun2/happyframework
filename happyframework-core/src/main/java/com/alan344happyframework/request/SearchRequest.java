package com.alan344happyframework.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author alan
 * @date 17-12-28 * 搜索请求类
 */
@Getter
@Setter
public class SearchRequest extends PageRequest {
    /**
     * 搜索内容
     */
    @NotBlank(message = "搜索内容不能为空")
    @Size(min = 1, max = 30, message = "搜索内容不能超过30个字符")
    public String searchContent;

    /**
     * 搜索类型
     */
    public Integer searchType;

    public String getLikeContent() {
        return this.searchContent == null ? null : "%" + this.searchContent.trim() + "%";
    }
}