package com.windvalley.guli.service.cms.feign.failback;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.cms.feign.IOssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 容错的本地实现，当远程服务失效时，本地的一个托底处理方法
 */
@Service
@Slf4j
public class OssFileServiceFailBack implements IOssFileService {
    @Override
    public R deleteFile(String url) {
        log.info("远程服务挂了，熔断保护");
        return R.error();
    }
}
