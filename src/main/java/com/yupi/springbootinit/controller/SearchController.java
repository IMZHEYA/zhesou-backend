package com.yupi.springbootinit.controller;
import com.google.common.collect.Lists;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.Search.SearchRequest;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.dto.post.PostQueryRequest;
import com.yupi.springbootinit.model.dto.user.UserQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.vo.PostVO;
import com.yupi.springbootinit.model.vo.SearchVO;
import com.yupi.springbootinit.model.vo.UserVO;
import com.yupi.springbootinit.service.PictureService;
import com.yupi.springbootinit.service.PostService;
import com.yupi.springbootinit.service.UserService;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param searchRequest
     * @param request
     * @return
     */
    @PostMapping("/all")
    public BaseResponse<SearchVO> searchAll(@RequestBody SearchRequest searchRequest,HttpServletRequest request) {
        String searchText = searchRequest.getSearchText();
        CompletableFuture<Page<UserVO>> userTask = CompletableFuture.supplyAsync(() ->{
            UserQueryRequest userQueryRequest = new UserQueryRequest();
            userQueryRequest.setUserName(searchText);
            Page<UserVO> userVOPage = userService.listUserVOByPage(userQueryRequest);
            return userVOPage;
        });
        CompletableFuture<Page<PostVO>> postTask = CompletableFuture.supplyAsync(()->{
            PostQueryRequest postQueryRequest = new PostQueryRequest();
            postQueryRequest.setSearchText(searchText);
            Page<PostVO> postVOPage = postService.listPostVOByPage(postQueryRequest, request);
            return postVOPage;
        });
        CompletableFuture<Page<Picture>> pictureTask = CompletableFuture.supplyAsync(()->{
            Page<Picture> picturePage = pictureService.searchPicture(searchText, 1, 10);
            return picturePage;
        });
        //聚合
        CompletableFuture.allOf(userTask,postTask,pictureTask).join();
        try {
            Page<UserVO> userVOPage = userTask.get();
            Page<PostVO> postVOPage = postTask.get();
            Page<Picture> picturePage = pictureTask.get();
            SearchVO searchVO = new SearchVO();
            searchVO.setUserList(userVOPage.getRecords());
            searchVO.setPictureList(picturePage.getRecords());
            searchVO.setPostList(postVOPage.getRecords());
            return  ResultUtils.success(searchVO);
        }catch (Exception e){
            log.error("查询异常",e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"查询异常");
        }
    }

}
