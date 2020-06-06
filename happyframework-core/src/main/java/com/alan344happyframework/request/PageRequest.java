package com.alan344happyframework.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest extends Request {
    /**
     * 分页id
     */
    private Integer pageId;
    /**
     * 页码
     */
    private Integer pageNo = 1;
    /**
     * 每一页显示数量
     */
    private Integer pageSize = 10;

    private Integer index;

    public Integer getIndex() {
        return (pageNo - 1) * pageSize;
    }
}
