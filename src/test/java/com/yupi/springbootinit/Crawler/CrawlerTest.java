package com.yupi.springbootinit.Crawler;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;
    @Test
    void testFetchPassage() {
        //1.获取数据
        String json = "{\"current\":1,\"pageSize\":8,\"sortField\":\"createTime\",\"sortOrder\":\"descend\",\"category\":\"文章\",\"tags\":[],\"reviewStatus\":1}";
        String url = "https://api.code-nav.cn/api/post/search/page/vo";
        String result = HttpRequest.post(url)
                .body(json)
                .execute().body();
        //2.处理数据:json转对象
        Map<String, Object> map = JSONUtil.toBean(result, Map.class);
        JSONObject data = (JSONObject) map.get("data");
        JSONArray records = (JSONArray) data.get("records");
        List<Post> postList = new ArrayList<>();
        for (Object record : records) {
            Post post = new Post();
            JSONObject tempRecord = (JSONObject) record;
            post.setId(0L);
            post.setTitle(tempRecord.getStr("title"));
            post.setContent(tempRecord.getStr("content"));
            //这里将json数组转为列表再转为json字符串，不知道是为了干什么，为什么不直接把json数组转为json字符串呢？
            JSONArray tags = (JSONArray) tempRecord.get("tags");
            List<String> tagList = tags.toList(String.class);
//            JSONUtil.toJsonStr(tags);
            post.setTags(JSONUtil.toJsonStr(tagList));
            System.out.println(JSONUtil.toJsonStr(tagList));
            post.setUserId(1L);
            postList.add(post);
            //3.写入数据库
            postService.saveBatch(postList);
        }
    }
}
