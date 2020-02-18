package com.juice.community.cache;

import com.juice.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDTO> getTags(){
        List<TagDTO>tags=new ArrayList<>();
        TagDTO lang = new TagDTO();
        lang.setTabName("开发语言");
        lang.setTags(Arrays.asList("java","javascript","html","C#","C","C++","python","ruby","go","node.js","scala","flutter","sss"));
        tags.add(lang);

        TagDTO tool=new TagDTO();
        tool.setTabName("开发工具");
        tool.setTags(Arrays.asList("idea","hbuilder","git","chrome","github","hg","xcode","textmate","ci"));
        tags.add(tool);

        TagDTO server=new TagDTO();
        server.setTabName("服务器");
        server.setTags(Arrays.asList("linux","tomcat","docker","apache","unix","centos","ubuntu"));
        tags.add(server);
        return tags;
    }
    public static String filterInvalid(String tagStr){
        //验证标签是否有效
        String[] tags = StringUtils.split(tagStr, "-");
        List<TagDTO> tagsModel = getTags();//模板tag
        List<String> tagList = tagsModel.stream().flatMap(tag -> tag.getTags().stream()).collect(Collectors.toList());
        String invalidTag = Arrays.stream(tags).filter(t -> !tagList.contains(t)).collect(Collectors.joining("-"));//拿到无效标签
        return invalidTag;
    }
}
