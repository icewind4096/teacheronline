package com.windvalley.guli.service.ucenter.controller.API;

import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.service.ucenter.entity.vo.RegisterVO;
import com.windvalley.guli.service.ucenter.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-14
 */
@CrossOrigin
@Api(description = "用户")
@RestController
@RequestMapping("/api/ucenter/member")
@Slf4j
public class APIMemberController {
    @Autowired
    IMemberService memberService;

    @ApiOperation(value = "会员注册")
    @PostMapping("register")
    public R reigster(@RequestBody RegisterVO registerVO){
        memberService.register(registerVO);

        return R.ok().message("注册成功");
    }
}

