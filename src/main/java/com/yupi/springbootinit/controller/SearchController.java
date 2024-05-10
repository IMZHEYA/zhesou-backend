package com.yupi.springbootinit.controller;
import com.google.common.collect.Lists;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.SearchFacade;
import com.yupi.springbootinit.model.dto.Search.SearchRequest;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

/**
 * 图片接口
 *
 *
 *
 */
@RestController
@RequestMapping("/search")
@Slf4j
@CrossOrigin(originPatterns = {"http://localhost:8081"}, allowCredentials = "true", allowedHeaders = {"*"})
public class SearchController {

    @Resource
    private PictureService pictureService;


    @Resource
    private PostService postService;

    @Resource
    private UserService userService;

    @Resource
    private SearchFacade searchFacade;
    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param searchRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest,HttpServletRequest request) {
        return  ResultUtils.success(searchFacade.searchAll(searchRequest,request));
    }

}
