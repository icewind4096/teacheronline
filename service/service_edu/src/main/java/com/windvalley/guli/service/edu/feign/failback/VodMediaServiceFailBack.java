package com.windvalley.guli.service.edu.feign.failback;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.service.edu.feign.IVodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class VodMediaServiceFailBack implements IVodMediaService {
    @Override
    public R remove(String id) {
        log.info("远程服务挂了，熔断保护");
        return R.error();
    }

    @Override
    public R removeByIdList(List<String> ids) {
        log.info("远程服务挂了，熔断保护");
        return R.error();
    }
}
