package com.windvalley.guli.service.ucenter.service;

import com.windvalley.guli.service.ucenter.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.windvalley.guli.service.ucenter.entity.vo.LoginVO;
import com.windvalley.guli.service.ucenter.entity.vo.RegisterVO;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-14
 */
public interface IMemberService extends IService<Member> {

    void register(RegisterVO registerVO);

    String login(LoginVO loginVO);
}
