package com.yupi.springbootinit.dataSource;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.poi.ss.formula.functions.T;

/**
 * 数据源接口
 * （新接入的数据源必须实现）
 */
public interface DataSource<T> {

    Page<T> doSearch(String searchText, long pageNum, long pageSize);
}
