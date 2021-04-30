package com.alan344happyframework.bean.request;

import com.alan344happyframework.bean.response.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author AlanSun
 * @date 2020/7/16 16:43
 * <p>
 * 公共的分页请求类
 **/
public class PageRequest extends Request {
    /**
     * 分页 id，根据 id 分页时使用
     */
    private Integer pageId;
    private Integer pageNum = 1;
    private Integer pageSize = 10;

    public static String getLike(String content) {
        return "%" + content + "%";
    }

    public Integer getPageId() {
        return pageId;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }

    public <R> Page<R> doPage(Supplier<List<R>> fun) {
        final PageInfo<R> rPageInfo = PageHelperUtils.doPage(fun, this);
        final Page<R> rPage = new Page<>();
        rPage.setList(rPageInfo.getList());
        rPage.setPageNum(rPageInfo.getPageNum());
        rPage.setPageSize(rPageInfo.getPageSize());
        rPage.setPageTotal(rPageInfo.getPages());
        rPage.setTotal(rPageInfo.getTotal());
        return rPage;
    }

    public <R extends PageRequest> R copyTo(R pageRequest) {
        pageRequest.setPageId(this.pageId);
        pageRequest.setPageNum(this.pageNum);
        pageRequest.setPageSize(this.pageSize);
        return pageRequest;
    }
}


