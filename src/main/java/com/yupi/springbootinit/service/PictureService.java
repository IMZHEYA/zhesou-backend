package com.yupi.springbootinit.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yupi.springbootinit.model.entity.Picture;
import org.springframework.stereotype.Service;

@Service
public interface PictureService {



    Page<Picture> searchPicture(String searchText, long pageNum, long pageSize);
}
