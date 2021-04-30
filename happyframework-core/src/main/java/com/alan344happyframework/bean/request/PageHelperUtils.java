package com.alan344happyframework.bean.request;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.function.Supplier;

/**
 * @author AlanSun
 * @date 2020/6/9 13:18
 */
public final class PageHelperUtils {
    private PageHelperUtils() {
    }

    /**
     * 获取一个分页 Page
     *
     * @param pageRequest 分页请求
     * @return {@link Page}
     */
    private static <E> Page<E> createPage(PageRequest pageRequest) {
        return PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
    }

    /**
     * 执行分页操作
     *
     * @param fun         func
     * @param pageRequest {@link PageRequest} 的子类
     * @param <R>         返回类型
     * @param <T>         请求类型
     * @return PageInfo
     */
    public static <R, T extends PageRequest> PageInfo<R> doPage(Supplier<List<R>> fun, T pageRequest) {
        try (final Page<Object> page = createPage(pageRequest)) {
            return page.doSelectPageInfo(fun::get);
        }
    }

    /**
     * 克隆 page
     *
     * @param pageSource 源 page
     * @return page
     */
    public static <R> vip.tuoyang.base.bean.response.Page<R> copyPage(vip.tuoyang.base.bean.response.Page<?> pageSource) {
        vip.tuoyang.base.bean.response.Page<R> page = new vip.tuoyang.base.bean.response.Page<>();
        page.setPageNum(pageSource.getPageNum());
        page.setPageSize(pageSource.getPageSize());
        page.setTotal(pageSource.getTotal());
        page.setPageTotal(pageSource.getPageTotal());
        return page;
    }
}
