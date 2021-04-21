package com.windvalley.guli.service.ucenter.controller.API;

import com.google.gson.Gson;
import com.windvalley.guli.common.base.entry.JWTInfo;
import com.windvalley.guli.common.base.result.ResultCodeEnum;
import com.windvalley.guli.common.base.util.ExceptionUtils;
import com.windvalley.guli.common.base.util.HttpClientUtils;
import com.windvalley.guli.common.base.util.JWTUtils;
import com.windvalley.guli.service.base.exception.WindvalleyException;
import com.windvalley.guli.service.ucenter.entity.Member;
import com.windvalley.guli.service.ucenter.entity.WChatTokenInfo;
import com.windvalley.guli.service.ucenter.entity.WChatUnionInfo;
import com.windvalley.guli.service.ucenter.service.IMemberService;
import com.windvalley.guli.service.ucenter.util.UCenterProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.UUID;

@CrossOrigin
@Api(description = "微信三方登录")
@RequestMapping("/api/ucenter/wx")
@Controller    //跳转网页，不要写为@RestController
@Slf4j
public class APIWChatController {
    @Autowired
    private UCenterProperties uCenterProperties;

    @Autowired
    private IMemberService memberService;

//    第一步：请求CODE
    @GetMapping("login")
    public String generatorQRConnect(HttpSession httpSession){
        String appid = uCenterProperties.getAppid();
        String redirectURI = encodeString(uCenterProperties.getRedirectURI());
        String state = UUID.randomUUID().toString();

        String qrCodeURL = getQRCodeURL(appid, redirectURI, "snsapi_login", state);

        //保存state值到session中，用于微信回调的时候校验是否本主机发出的对微信的请求
        httpSession.setAttribute("wx_open_state", state);

        //跳转网页 必须使用@Controller注解
        return "redirect:" + qrCodeURL;
    }

    private String getQRCodeURL(String appid, String redirectURI, String scope, String state) {
        return String.format("https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
                appid,
                redirectURI,
                scope,
                state);
    }

    private String encodeString(String value) {
        try {
            return URLEncoder.encode(uCenterProperties.getRedirectURI(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.URL_ENCODE_ERROR);
        }
    }

    //被微信拉起以后的回调
    @ApiOperation(value = "微信授权回调接口")
    @GetMapping("callback")
    public String callBack(String code, String state, HttpSession httpSession){
        //第一步:取出保存在session中的state值，校验是否本主机发出的对微信的请求
        if (!checkCode(code)){
            log.error("微信授权失败");
            throw new WindvalleyException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        if (!checkState(state, (String)httpSession.getAttribute("wx_open_state"))){
            log.error("回调验证值与Session保存的验证值不一致");
            throw new WindvalleyException(ResultCodeEnum.ILLEGAL_CALLBACK_REQUEST_ERROR);
        }

        //第二步：通过code获取WChatTokenInfo
        String content = getAccessToken(uCenterProperties.getAppid(), uCenterProperties.getAppSecret(), code, "authorization_code");
        WChatTokenInfo wChatTokenInfo = getWChatTokenInfoFromContent(content);

        //第三步：
        //1. 在本地数据库查找改用户是否存在，
        Member member = memberService.getByOpenId(wChatTokenInfo.getOpenId());
        //2. 如果不存在，进行用户注册
        if (member == null){
            //2.1使用openId去https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID获得用户微信基本信息
            content = getWChatUnionData(wChatTokenInfo.getAccessToken(), wChatTokenInfo.getOpenId());
            //2.2 由context中获得用户信息
            WChatUnionInfo wChatUnionInfo = getWChatUnionInfoFromContent(content);
            //2.3 由微信信息注册新用户
            member = getMemberFromWChatUnionInfo(wChatUnionInfo);
            memberService.save(member);
        }

        //4. 产生JWT 用户登录
        JWTInfo jwtInfo = new JWTInfo();
        jwtInfo.setId(member.getId());
        jwtInfo.setAvatar(member.getAvatar());
        jwtInfo.setNickName(member.getNickname());
        String jwtToken = JWTUtils.generatorJWT(jwtInfo, 1800);

        //携带Token到前端
        return "redirect:http://localhost:3000?token=" + jwtToken;
    }

    private Member getMemberFromWChatUnionInfo(WChatUnionInfo wChatUnionInfo) {
        Member member = new Member();
        member.setOpenid(wChatUnionInfo.getOpenid());
        member.setNickname(wChatUnionInfo.getNickname());
        member.setAvatar(wChatUnionInfo.getHeadimgurl());
        member.setSex(wChatUnionInfo.getSex());
        return member;
    }

    private WChatUnionInfo getWChatUnionInfoFromContent(String content) {
        Gson gson = new Gson();
        HashMap<String, Object> hashMap = gson.fromJson(content, HashMap.class);
        if (hashMap.containsKey("errcode")){
            log.error("获取微信用户信息失败" + "Code:" + (Double) hashMap.get("errcode") + ", Message:" + hashMap.get("errmsg"));
            throw new WindvalleyException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
        WChatUnionInfo wChatUnionInfo = new WChatUnionInfo();
        wChatUnionInfo.setOpenid((String) hashMap.get("openid"));
        wChatUnionInfo.setNickname((String) hashMap.get("nickname"));
        wChatUnionInfo.setSex(((Double) hashMap.get("sex")).intValue());
        wChatUnionInfo.setProvince((String) hashMap.get("province"));
        wChatUnionInfo.setCity((String) hashMap.get("city"));
        wChatUnionInfo.setCountry((String) hashMap.get("country"));
        wChatUnionInfo.setHeadimgurl((String) hashMap.get("headimgurl"));
        //wChatUnionInfo.setPrivilege(String) hashMap.get("privilege"); 数组，暂时不实现 todo
        wChatUnionInfo.setUnionid((String) hashMap.get("unionid"));
        return wChatUnionInfo;
    }

    private String getWChatUnionData(String accessToken, String openId) {
        HashMap<String, String> paramaters = new HashMap<>();
        paramaters.put("access_token", accessToken);
        paramaters.put("openid", openId);

        try {
            return getContentFromUrl(getUnionIdUrl(), paramaters);
        } catch (IOException e) {
            log.error("获取微信用户信息失败" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.FETCH_USERINFO_ERROR);
        }
    }

    private String getUnionIdUrl() {
        return "https://api.weixin.qq.com/sns/userinfo";
    }

    private WChatTokenInfo getWChatTokenInfoFromContent(String content) {
        if (!StringUtils.isEmpty(content)){
            //解析返回的数据,判断结果是否正确
            Gson gson = new Gson();
            HashMap<String, Object> hashMap = gson.fromJson(content, HashMap.class);
            if (hashMap.containsKey("errcode")){
                log.error("校验微信返回的Access Token错误" + "Code:" + (Double) hashMap.get("errcode") + ", Message:" + hashMap.get("errmsg"));
                throw new WindvalleyException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
            }
            WChatTokenInfo wChatTokenInfo = new WChatTokenInfo();
            wChatTokenInfo.setAccessToken((String) hashMap.get("access_token"));
            wChatTokenInfo.setOpenId((String) hashMap.get("openid"));
            return wChatTokenInfo;
        }
        return null;
    }

    private String getAccessToken(String appid, String appSecret, String code, String authorization_code) {
        HashMap<String, String> paramaters = new HashMap<>();
        paramaters.put("appid", uCenterProperties.getAppid());
        paramaters.put("secret", uCenterProperties.getAppSecret());
        paramaters.put("code", code);
        paramaters.put("grant_type", "authorization_code");

        try {
            return getContentFromUrl(getAccessTokenUrl(), paramaters);
        } catch (IOException e) {
            log.error("获取微信Access Token失败" + ExceptionUtils.getMessage(e));
            throw new WindvalleyException(ResultCodeEnum.FETCH_ACCESSTOKEN_FAILD);
        }
    }

    private String getContentFromUrl(String url, HashMap<String, String> paramaters) throws IOException {
        HttpClientUtils httpClient = new HttpClientUtils(url, paramaters);

        httpClient.get();

        return httpClient.getContent();
    }

    private String getAccessTokenUrl() {
        return "https://api.weixin.qq.com/sns/oauth2/access_token";
    }

    private boolean checkState(String state, String wx_open_state) {
        return wx_open_state.equals(state);
    }

    private boolean checkCode(String code) {
        return !StringUtils.isEmpty(code);
    }
}

