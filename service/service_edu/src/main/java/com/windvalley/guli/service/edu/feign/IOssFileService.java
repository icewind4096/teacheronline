package com.windvalley.guli.service.edu.feign;

import com.windvalley.guli.common.base.result.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@FeignClient("service-oss") //微服务注册到注册中心的名字
public interface IOssFileService {
    @ApiOperation(value = "测试微服务调用")
    @PostMapping("/admin/oss/file/test")
    R test();
}
