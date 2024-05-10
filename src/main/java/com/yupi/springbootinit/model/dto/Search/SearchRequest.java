package com.yupi.springbootinit.model.dto.Search;

import com.yupi.springbootinit.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户创建请求
 *
 * @author
 * @from
 */
@Data
public class SearchRequest extends  PageRequest implements Serializable {


    private String searchText;


    private String type;

    private static final long serialVersionUID = 1L;
}