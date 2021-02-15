package com.windvalley.guli.service.edu.feign;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.feign.failback.OssFileServiceFailBack;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(value = "service-oss", fallback = OssFileServiceFailBack.class) //微服务注册到注册中心的名字, 如果远程微服务挂了，调用fallback
public interface IOssFileService {
    @ApiOperation(value = "测试微服务调用")
    @PostMapping("/admin/oss/file/test")
    R test();

    @ApiOperation(value = "调用文件删除服务")
    @PostMapping("/admin/oss/file/delete")
    R deleteFile(@RequestBody String url);
}
