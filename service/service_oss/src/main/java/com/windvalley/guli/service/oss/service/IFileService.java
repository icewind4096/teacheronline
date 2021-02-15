package com.windvalley.guli.service.oss.service;

import java.io.InputStream;

public interface IFileService {
    /**
     * 阿里云oss文件上传接口
     * @param inputStream 输入流
     * @param destDir 目标文件地址
     * @param originalFileName 上传文件原始文件名
     * @return
     */
    String upload(InputStream inputStream, String destDir, String originalFileName);
    void delete(String url);
}
