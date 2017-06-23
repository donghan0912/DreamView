package com.dream.dreamview;

/**
 * Created by lenovo on 2017/6/21.
 */

public class RequestConstant {
    // TODO 美女图片API：http://www.tngou.net/doc/gallery/29

    public static final String IMG_TYPE_LIST = "http://www.tngou.net/tnfs/api/classify";
    class Galleryclass{
        private int id; // 分类id，需要查询该类下的列表就需要传入才参数
        private String name; // 分类名称
        private String title; // 分类的标题（网页显示的标题）
        private String keywords; // 分类的关键词（网页显示的标题）
        private String description; // 分类的描述（网页显示的标题）
        private int seq;//排序 从0。。。。10开始 分类的排序，从小到大的递增排序
    }
    public static final String IMG_LIST = "http://image.baidu.com/data/imgs";

}
