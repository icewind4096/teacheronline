package com.windvalley.guli.service.ucenter.controller.API;

import com.windvalley.guli.common.base.entry.JWTInfo;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.JWTUtils;
import com.windvalley.guli.service.ucenter.entity.vo.LoginVO;
import com.windvalley.guli.service.ucenter.entity.vo.RegisterVO;
import com.windvalley.guli.service.ucenter.service.IMemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public R login(@RequestBody LoginVO loginVO){
        String token = memberService.login(loginVO);
        return R.ok().message("登录成功").data("token", token);
    }

    @ApiOperation(value = "根据token获取登录信息")
    @PostMapping("getlogininfo")
    public R getLoginInfo(HttpServletRequest httpServletRequest){
        JWTInfo jwtInfo = JWTUtils.getMemberInfoByToken(httpServletRequest);
        if (jwtInfo != null){
            return R.ok().data("member", jwtInfo);
        } else {
            return R.error().code(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD.getCode()).message(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD.getMessage());
        }
    }


}

