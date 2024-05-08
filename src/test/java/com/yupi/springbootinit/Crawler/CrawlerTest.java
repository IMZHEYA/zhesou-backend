package com.yupi.springbootinit.Crawler;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Picture;
import com.yupi.springbootinit.model.entity.Post;
import com.yupi.springbootinit.service.PostService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class CrawlerTest {

    @Resource
    private PostService postService;
    //抓取文章
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
    //抓取图片
    @Test
    void testFetchPicture() throws IOException {
        int current = 1;
        String url = "https://cn.bing.com/images/search?q=%E5%B0%8F%E9%BB%91%E5%AD%90&form=HDRSC2&first=" + current;
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(".iuscp.isv"); //数组，每个元素是每一张图片
        List<Picture> pictures = new ArrayList<>();
        for (Element element : elements) {
            //取图片地址murl
            String m = element.select(".iusc").attr("m");
            Map<String,Object> map = JSONUtil.toBean(m, Map.class);
            String murl  = (String) map.get("murl");
            //取标题
            String title = element.select(".inflnk").attr("aria-label");
//            System.out.println(murl);
//            System.out.println(title);
            Picture picture = new Picture();
            picture.setTitle(title);
            picture.setUrl(murl);
            pictures.add(picture);
        }
    }
}
