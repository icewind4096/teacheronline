package com.windvalley.guli.service.edu.feign;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.feign.failback.VodMediaServiceFailBack;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@FeignClient(value = "service-vod", fallback = VodMediaServiceFailBack.class) //微服务注册到注册中心的名字, 如果远程微服务挂了，调用fallback
public interface IVodMediaService {
    @PostMapping("/admin/vod/media/remove/{id}")
    R remove(@PathVariable("id") String id);

    @PostMapping("/admin/vod/media/remove")
    R removeByIdList(@RequestBody List<String> ids);
}
