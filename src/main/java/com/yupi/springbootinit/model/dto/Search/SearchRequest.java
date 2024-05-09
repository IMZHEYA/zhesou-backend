package com.yupi.springbootinit.model.dto.Search;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author
 * @from
 */
@Data
public class SearchRequest implements Serializable {


    private String searchText;

    private static final long serialVersionUID = 1L;
}