package com.windvalley.guli.service.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.PutObjectRequest;
import com.windvalley.guli.service.oss.service.IFileService;
import com.windvalley.guli.service.oss.utils.OSSProperties;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
public class FileService implements IFileService {
    //OSS配置信息自动注入
    @Autowired
    private OSSProperties ossProperties;

    @Override
    public String upload(InputStream inputStream, String destDir, String originalFileName) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 如果bucket不存在，创建bucket实例
        if (!ossClient.doesBucketExist(ossProperties.getBucketname())){
            ossClient.createBucket(ossProperties.getBucketname());
            ossClient.setBucketAcl(ossProperties.getBucketname(), CannedAccessControlList.Private);
        }

        // 创建PutObjectRequest对象。
        String uploadFileName = createObjectFileName(destDir, getDestFileName(originalFileName));
        PutObjectRequest putObjectRequest = new PutObjectRequest(ossProperties.getBucketname(), uploadFileName, inputStream);

        // 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // metadata.setObjectAcl(CannedAccessControlList.Private);
        // putObjectRequest.setMetadata(metadata);

        // 上传文件。
        ossClient.putObject(putObjectRequest);

        // 关闭OSSClient。
        ossClient.shutdown();

        //返回上传的url地址
        return getUploadFileURL(ossProperties.getBucketname(), ossProperties.getEndpoint(), uploadFileName);
    }

    @Override
    public void delete(String url) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ossProperties.getEndpoint();

        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ossProperties.getKeyid();
        String accessKeySecret = ossProperties.getKeysecret();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 如果bucket不存在，直接返回
        if (ossClient.doesBucketExist(ossProperties.getBucketname())){
            //删除文件
            System.out.println(getObjectNameFromURL(url, ossProperties.getBucketname(), ossProperties.getEndpoint()));
            ossClient.deleteObject(ossProperties.getBucketname(), getObjectNameFromURL(url, ossProperties.getBucketname(), ossProperties.getEndpoint()));
        }

        // 关闭OSSClient。
        ossClient.shutdown();
    }

    private String getObjectNameFromURL(String url, String bucketname, String endpoint) {
        return url.substring(getHostName(bucketname, endpoint).length());
    }

    private String getUploadFileURL(String bucketname, String endpoint, String uploadFileName) {
        return String.format("%s%s", getHostName(bucketname, endpoint), uploadFileName);
    }

    private String getHostName(String bucketname, String endpoint) {
        return String.format("https://%s.%s/", bucketname, endpoint);
    }

    private String getDestFileName(String originalFileName) {
        return UUID.randomUUID().toString() + getExtNameFromFileName(originalFileName);
    }

    private String getExtNameFromFileName(String originalFileName) {
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    //返回路径格式 avater/2021/02/06/原始文件名
    private String createObjectFileName(String directoryName, String destFileName) {
        return String.format("%s/%s/%s", directoryName, new DateTime().toString("yyyy/MM/dd"), destFileName);
    }
}
