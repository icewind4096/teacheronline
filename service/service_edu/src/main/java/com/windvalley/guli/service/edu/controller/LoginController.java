package com.windvalley.guli.service.edu.controller;

import com.windvalley.guli.common.base.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@Api(description = "后台管理系统")
@RestController
@RequestMapping("/user")
public class LoginController {
    /**
     * 用户登录
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("login")
    public R login(){
        return R.ok().data("token", "admin");
    }

    /**
     * 用户登出
     * @param
     * @return
     */
    @ApiOperation("用户登出")
    @PostMapping("logout")
    public R logout(){
        return R.ok();
    }

    /**
     * 用户信息
     * @param token
     * @return
     */
    @ApiOperation("携带token，获得用户信息")
    @GetMapping("info")
    public R info(@ApiParam(value = "用户登录token", required = true) @RequestParam String token){
        return R.ok().data("name", "admin")
                .data("roles", "[admin]")
                .data("avatar", "https://goss.cfp.cn/creative/vcg/800/new/VCG41N1263738419.jpg");
    }


}
