package com.windvalley.guli.service.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.windvalley.guli.common.base.entry.JWTInfo;
import com.windvalley.guli.common.base.result.R;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.FormUtils;
import com.windvalley.guli.common.base.util.JWTUtils;
import com.windvalley.guli.common.base.util.MD5;
import com.windvalley.guli.common.base.util.PropertiesUtil;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.ucenter.entity.Member;
import com.windvalley.guli.service.ucenter.entity.vo.LoginVO;
import com.windvalley.guli.service.ucenter.entity.vo.RegisterVO;
import com.windvalley.guli.service.ucenter.mapper.MemberMapper;
import com.windvalley.guli.service.ucenter.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.windvalley.guli.common.base.util.FormUtils.isMobile;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author icewind4096
 * @since 2021-03-14
 */
@Service
public class MemberService extends ServiceImpl<MemberMapper, Member> implements IMemberService {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void register(RegisterVO registerVO) {
        if (registerVO.getMobile().isEmpty() || !FormUtils.isMobile(registerVO.getMobile())){
            throw new WindvalleyException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        if (registerVO.getNickName().isEmpty() || registerVO.getPassword().isEmpty() || registerVO.getCode().isEmpty()){
            throw new WindvalleyException(ResultCodeEnum.PARAM_ERROR);
        }

        String checkCode = (String) redisTemplate.opsForValue().get(registerVO.getMobile());
        if (!registerVO.getCode().equals(checkCode)){
            throw new WindvalleyException(ResultCodeEnum.CODE_ERROR);
        }

        if (mobileAlreadyExist(registerVO.getMobile())){
            throw new WindvalleyException(ResultCodeEnum.REGISTER_MOBLE_ERROR);
        }

        registerMember(registerVO.getNickName(), registerVO.getMobile(), registerVO.getPassword());
    }

    private boolean registerMember(String nickName, String mobile, String password) {
        Member member = new Member();
        member.setNickname(nickName);
        member.setMobile(mobile);
        member.setPassword(MD5.encrypt(password));
        member.setDisabled(false);
        member.setAvatar("https://windvalley-teacher.oss-cn-hangzhou.aliyuncs.com/avatar/2021/03/07/ac7173b7-e92c-4350-861a-7e961d94b53b.jpg");

        return baseMapper.insert(member) > 0;
    }

    private boolean mobileAlreadyExist(String mobile) {
        Member member = findMemberByMobile(mobile);
        return member != null;
    }

    private Member findMemberByMobile(String mobile) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper();
        queryWrapper.eq("mobile", mobile);

        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public String login(LoginVO loginVO) {
        //校验
        if (loginVO.getMobile().isEmpty() || !isMobile(loginVO.getMobile())){
            throw new WindvalleyException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }

        if (loginVO.getPassword().isEmpty()){
            throw new WindvalleyException(ResultCodeEnum.PARAM_ERROR);
        }

        Member member = findMemberByMobile(loginVO.getMobile());
        if (member == null){
            throw new WindvalleyException(ResultCodeEnum.LOGIN_MOBILE_ERROR);
        } else {
            if (!member.getPassword().equals(MD5.encrypt(loginVO.getPassword()))) {
                throw new WindvalleyException(ResultCodeEnum.LOGIN_PASSWORD_ERROR);
            }

            if (member.getDisabled()){
                throw new WindvalleyException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
            }
        }

        //产生登录token
        return generatorToken(member.getId(), member.getNickname(), member.getAvatar());

    }

    private String generatorToken(String id, String nickname, String avatar) {
        JWTInfo jwtInfo = new JWTInfo();

        jwtInfo.setId(id);
        jwtInfo.setNickName(nickname);
        jwtInfo.setAvatar(avatar);

        return JWTUtils.generatorJWT(jwtInfo);
    }
}
