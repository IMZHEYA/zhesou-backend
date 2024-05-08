package com.yupi.springbootinit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.model.dto.picture.PictureQueryRequest;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图片接口
 *
 *
 *
 */
@RestController
@RequestMapping("/picture")
@Slf4j
@CrossOrigin(originPatterns = {"http://localhost:8081"}, allowCredentials = "true", allowedHeaders = {"*"})
public class PictureController {

    @Resource
    private PictureService pictureService;

    /**
     * 分页搜索（从 ES 查询，封装类）
     *
     * @param pictureQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    public BaseResponse<Page<Picture>> searchPictureByPage(@RequestBody PictureQueryRequest pictureQueryRequest,
                                                          HttpServletRequest request) {
        long size = pictureQueryRequest.getPageSize();
        long current = pictureQueryRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        String searchText = pictureQueryRequest.getSearchText();
        Page<Picture> picturePage = pictureService.searchPicture(searchText,current,size);
        return ResultUtils.success(picturePage);
    }

}
