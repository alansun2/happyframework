package com.alan344happyframework.bean.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

/**
 * 查询 列表分页
 *
 * @author cbdyzj
 * @since 2018-07-30
 */
@Getter
@Setter
@NoArgsConstructor
public class Page<T> {
    /**
     * 返回列表
     */
    private List<T> list = Collections.emptyList();
    /**
     * 数据总条数
     */
    private Long total = 0L;
    /**
     * 当前页码数
     */
    private Integer pageNum;
    /**
     * 每页数据大小
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer pageTotal = 0;

    public Page(List<T> list, int pageSize, int pageNum, Long total) {
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.total = total;
        this.pageTotal = (int) (total / pageSize + 1);
    }

    public Page(List<T> list, Long total) {
        this.list = list;
        this.total = total;
        this.pageNum = 1;
        this.pageSize = 10;
        this.pageTotal = (int) (total / 10 + 1);
    }

    public Page(List<T> list, Long total, Integer pageNum, Integer pageSize, Integer pageTotal) {
        this.list = list;
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.pageTotal = pageTotal;
    }

    public static <T> Page<T> pageFormat(List<T> list, Integer pageIndex, Integer pageSize, Integer count, Integer pageNum) {
        return new Page<>(list, count.longValue(), pageIndex, pageSize, pageNum);
    }

    public static <T, R> Page<R> copy(Page<T> originPage, List<R> list) {
        return new Page<>(list, originPage.total, originPage.pageNum, originPage.pageSize, originPage.pageTotal);
    }

    public static <T, R> Page<R> copy(Page<T> originPage) {
        return new Page<>(null, originPage.total, originPage.pageNum, originPage.pageSize, originPage.pageTotal);
    }
}
