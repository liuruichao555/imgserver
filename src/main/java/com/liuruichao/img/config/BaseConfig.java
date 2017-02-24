package com.liuruichao.img.config;

/**
 * 全局配置
 * 
 * @author liuruichao
 * Created on 2016-01-15 13:58
 */
public interface BaseConfig {
    String DOMAIN = "//img.51universe.com";
    long MAX_UPLOAD_FILE_SIZE = 5242880; // 5M
    String[] ALLOW_EXTENSION = {"png", "jpg", "jpeg", "gif"}; // 允许上传文件类型
    String BASE_UPLOAD_FILE_PATH = "/mnt/online/upload_images";
    //String BASE_UPLOAD_FILE_PATH = "/Users/liuruichao/temp";
}
