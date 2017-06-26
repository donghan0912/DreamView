package com.dream.dreamview.meinv;

import com.google.gson.annotations.SerializedName;

import java.security.acl.Owner;
import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */

public class Gallery {
    public String id;
    public String desc;
    public List<String> tags;
    public Owner owner;
    public String fromPageTitle;
    public String column;
    public String parentTag;
    public String date;
    public String downloadUrl;
    public String imageUrl;
    public int imageWidth;
    public int imageHeight;
    @SerializedName("thumbnailUrl")
    public String url;
    public int thumbnailWidth;
    public int thumbnailHeight;
    public int thumbLargeWidth;
    public int thumbLargeHeight;
    @SerializedName("thumbLargeUrl")
    public String largeUrl;
    public int thumbLargeTnWidth;
    public int thumbLargeTnHeight;
    public String thumbLargeTnUrl;
    public String siteName;
    public String siteLogo;
    public String siteUrl;
    public String fromUrl;
    public String isBook;
    public String bookId;
    public String objUrl;
    public String shareUrl;
    public String setId;
    public String albumId;
    public int isAlbum;
    public String albumName;
    public int albumNum;
    public String userId;
    public int isVip;
    public int isDapei;
    public String dressId;
    public String dressBuyLink;
    public int dressPrice;
    public int dressDiscount;
    public String dressExtInfo;
    public String dressTag;
    public int dressNum;
    public String objTag;
    public int dressImgNum;
    public String hostName;
    public String pictureId;
    public String pictureSign;
    public String dataSrc;
    public String contentSign;
    public String albumDi;
    public String canAlbumId;
    public String albumObjNum;
    public String appId;
    public String photoId;
    public int fromName;
    public String fashion;
    public String title;
    public int height;
    public boolean isChecked = false;

    class Owner {
        public String userName;
        public String userId;
        public String userSign;
        public String isSelf;
        public String portrait;
        public String isVip;
        public String isLanv;
        public String isJiaju;
        public String isHunjia;
        public String orgName;
        public String resUrl;
        public String cert;
        public String budgetNum;
        public String lanvName;
        public String contactName;
    }
}


