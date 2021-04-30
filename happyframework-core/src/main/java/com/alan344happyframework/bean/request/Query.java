package com.alan344happyframework.bean.request;


import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * @author AlanSun
 * @date 2019/4/26 14:08
 **/
public class Query extends HashMap<String, Object> {
    private Query(PageRequest pageRequest, int pageType) {
        if (pageRequest.getPageId() != null) {
            this.put("pageId", pageRequest.getPageId());
        }
        this.put("pageSize", pageRequest.getPageSize());
        this.put("pageType", pageType);
        if (pageType == PAGETYPE2) {
            this.put("offset", pageRequest.getOffset());
        }
    }

    public void setOrderBy(String orderBy) {
        this.put("orderBy", orderBy);
    }

    private Query() {
    }

    @Override
    @NonNull
    public Query put(String key, Object value) {
        if (value != null) {
            super.put(key, value);
        }
        return this;
    }

    @NonNull
    public <R> Query put(boolean condition, String key, Supplier<R> fuc) {
        if (condition) {
            super.put(key, fuc.get());
        }
        return this;
    }

    public static Query create(PageRequest pageRequest, int pageType) {
        return new Query(pageRequest, pageType);
    }

    public static Query create() {
        return new Query();
    }

    //pageId
    public static final int PAGETYPE1 = 1;
    //pageNo
    public static final int PAGETYPE2 = 2;
}
