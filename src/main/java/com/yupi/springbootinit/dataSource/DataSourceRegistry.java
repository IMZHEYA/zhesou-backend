package com.yupi.springbootinit.dataSource;

import com.yupi.springbootinit.model.enums.SearchTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataSourceRegistry {

    @Resource
    private PictureDataSource pictureDataSource;


    @Resource
    private UserDataSource userDataSource;

    @Resource
    private PostDataSource postDataSource;

    private Map<String, DataSource> typeDataSourceMap;

    /**
     * @PostConstruct标识的方法会在Spring容器完成依赖注入后立即执行。
     * 没有这个初始化方法的话，依赖注入以后还没有执行typeDataSourceMap的赋值操作，就调用了getDataSourceByType方法，肯定是返回空的
     */
    @PostConstruct
    public void init() {
        typeDataSourceMap = new HashMap() {
            {
                put(SearchTypeEnum.POST.getValue(), postDataSource);
                put(SearchTypeEnum.USER.getValue(), userDataSource);
                put(SearchTypeEnum.PICTURE.getValue(), pictureDataSource);
            }
        };
    }

    public DataSource getDataSourceByType(String type) {
        if(typeDataSourceMap == null){
            return null;
        }
        return typeDataSourceMap.get(type);
    }
}
