package com.juice.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PageDTO<T> {
    private List<T> data;
    private boolean showPre;//上一页
    private boolean showFirst;//第一页
    private boolean showNext;//下一页
    private boolean showEnd;//最后一页
    private Integer curPage;//当前页
    private List<Integer>pages=new ArrayList<>();//要展示的页面
    private Integer totalPage;//总页数
    //根据总页数和当前页数来判定各个关系
    public void setPage(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        if(page<1){
            page=1;
        }
        if(page>totalPage){
            page=totalPage;
        }
        this.curPage=page;//当前页面
        pages.add(page);//添加当前页面到要展示的页面
        for(int i=1;i<=3;i++){
            if(page-i>0) pages.add(0,page-i);
            if(page+i<=totalPage) pages.add(page+i);
        }
        if(page==1){//当前第一页 不显示上一页图标 也不现实返回第一页图标
            showPre=false;
        }else {
            showPre=true;//不是第一页 就会显示上一页图标
        }
        if(page==totalPage){
            showNext=false;//最后一页不展示下一页
        }else{
            showNext=true;
        }
        if(pages.contains(1)){//分割的五页不含第一页时就要展示 返回第一页
            showFirst=false;
        }else{
            showFirst=true;
        }
        if(pages.contains(totalPage)){//包含最后一页就不展示跳转到最后一页
            showEnd=false;
        }else {
            showEnd=true;
        }
    }
}
