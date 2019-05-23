package com.goodsogood.ows.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.goodsogood.log4j2cm.annotation.HttpMonitorLogger;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.CacheConfiguration;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.helper.HttpRequestUtils;
import com.goodsogood.ows.helper.MD5Utils;
import com.goodsogood.ows.helper.SmsUtils;
import com.goodsogood.ows.model.db.*;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.*;
import com.unboundid.util.json.JSONException;
import com.unboundid.util.json.JSONObject;
import com.unboundid.util.json.JSONObjectReader;
import com.unboundid.util.json.JSONValue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/v-login")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "登录注册", tags = {"login manager"})
public class LoginController {

    private final Errors errors;
    private final UsersService usersService;
    private final AccountsUsersRolesService accountsUsersRolesService;
    private final LoginService loginService;
    private final MenusService menusService;
    private final String Url = "http://www.ztsms.cn/sendNSms.do";
    private final String pwd = "zcyun2019GS";
    private final String productid = "95533";
    private final String username = "zcyun";
    private Map<String, String> map;

    @Autowired
    public LoginController(Errors errors, UsersService userService, AccountsUsersRolesService aurService, LoginService logins, MenusService menus) {
        this.errors = errors;
        this.usersService = userService;
        this.accountsUsersRolesService = aurService;
        this.loginService = logins;
        this.menusService = menus;
    }

    /**
     * 注册账号(推荐人分享)
     */
    @ApiOperation(value = "注册账号")
    @HttpMonitorLogger
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> addUser(@Valid @RequestBody UsersForm user, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (user.phoneCode.isEmpty()) {
            throw new ApiException("验证码，不可为空！", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(this.usersService.Register(user), errors);
        return new ResponseEntity<>(result, HttpStatus.OK);

    }


    /**
     * 注册账号(管理员分享)
     */
    @ApiOperation(value = "注册账号")
    @PostMapping("/addAdmin")
    public ResponseEntity<Result<Boolean>> addAdminUser(@Valid @RequestBody UsersForm user, BindingResult bindingResult) {

        log.debug("bindingResult->{}", bindingResult);
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Boolean bool = this.usersService.AdminRegister(user);
        if (!bool) {
            throw new ApiException("注册失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(bool, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 登录校验是否存在多个身份
     */
    @ApiOperation(value = "登录校验是否存在多个身份")
    @PostMapping("/isLoigin")
    public ResponseEntity<Result<List<RoleUserEntity>>> IsLoigin(@Valid @RequestBody LoginForm user, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (user.getPhone().isEmpty()) {
            throw new ApiException("账号不能为空或空字符串", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<RoleUserEntity> rlist = null;
        if (user.code.isEmpty()) {
            rlist = this.loginService.getList(user.getPhone(), MD5Utils.MD5(user.getPassword()));
            if (rlist == null) {
                throw new ApiException("服务器繁忙，登录失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
        }
        if (!user.code.isEmpty() && user.getPassword().isEmpty()) {
            rlist = this.loginService.getListPhone(user.getPhone(), user.code);
            if (rlist == null) {
                throw new ApiException("服务器繁忙，登录失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
            }
        }
        Result<List<RoleUserEntity>> result = new Result<>(rlist, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 登录
     */
    @ApiOperation(value = "登录，返回菜单")
    @PostMapping("/index")
    public ResponseEntity<Result<UserMenusVo>> Login(@Valid @RequestBody Long userid, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        List<MenusEntity> entity = this.menusService.GetMenus(userid);
        UserMenusVo userMenusVo = new UserMenusVo();
        userMenusVo.setTime(CacheConfiguration.GetDate(60 * 60 * 1000L));
        userMenusVo.setToken(MD5Utils.MD5(userid.toString() + CacheConfiguration.GetDate(60 * 60 * 1000L)));
        userMenusVo.setUserId(userid);
        userMenusVo.setEntity(entity);
        CacheConfiguration.cache.put(userMenusVo.token, userMenusVo);
        Result<UserMenusVo> result = new Result<>(userMenusVo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 身份效验
     */
    @ApiOperation(value = "根据账号及权限ID 查询身份是否存在")
    @PostMapping("/isIdentity")
    public ResponseEntity<Result<Boolean>> IsIdentity(@Valid @RequestBody IdentityForm identityForm, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        AccountsUsersRolesEntity accountsUsersRolesEntity = this.accountsUsersRolesService.Get(identityForm.getPhone(), identityForm.getRoleId());
        Result<Boolean> result = new Result<>(false, errors);
        if (accountsUsersRolesEntity == null) {
            result = new Result<>(true, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    /**
     * 管理员登录
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "管理员登录")
    @PostMapping("/adminlogin")
    public ResponseEntity<Result<AdminVo>> AdminLogin(@Valid @RequestBody AdminForm form, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        if (form.getPhone().isEmpty() || form.getPwd().isEmpty()) {
            throw new ApiException("账号或密码，不可为空", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        AdminVo vo = new AdminVo();
        Long userid = this.accountsUsersRolesService.GetByFind(form.getPhone(), MD5Utils.MD5(form.getPwd()));
        List<MenusEntity> entity = this.menusService.GetMenus(userid);
        vo.setUserId(userid);
        vo.setToken(MD5Utils.MD5(userid + "" + CacheConfiguration.GetDate(60 * 60 * 1000L)));
        vo.setTime(CacheConfiguration.GetDate(60 * 60 * 1000L));
        vo.setEntity(entity);
        CacheConfiguration.cache.put(vo.getToken(), vo);
        Result<AdminVo> result = new Result<>(vo, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 退出登录
     *
     * @param token         token
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "退出登录")
    @PostMapping("/outLogin")
    public ResponseEntity<Result<Boolean>> outLogin(@Valid @RequestBody String token, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        CacheConfiguration.cache.remove(token);
        return new ResponseEntity<>(new Result<>(true, errors), HttpStatus.OK);
    }


    /**
     * 获取验证码
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码")
    @PostMapping(value = "/code")
    public ResponseEntity<Result<SmssEntity>> getCode(@Valid @RequestBody CodeForm entity) throws Exception {
        Date time = new Date();
        getResArgs(entity.getMobile(), "");
        String code = map.get("code");
        String Phone = map.get("phoneNum");
        String content = "验证码：" + code + "，请不要把验证码泄露给他人，谢谢！【知创云】";
        SmssEntity smsEntity = new SmssEntity();
        smsEntity.setSmsPhone(Phone);
        smsEntity.setSmsCode(code);
        smsEntity.setSmsContent(content);
        smsEntity.setAddtime(time);
        String sms = SmsUtils.postEncrypt(Url, username, pwd, Phone, content, productid);
        Integer getCode = Integer.parseInt(sms.split(",")[0]);
        if (getCode == 1) {

            smsEntity.setSmsSendType(1); //短信发送类型 1、文字 2、语音
            smsEntity.setSmsType(1); //短信类型 1、注册账号 2、修改密码
            smsEntity.setSmsSendFrequency(1); //发送次数
            smsEntity.setSmsUse(1);
            smsEntity.setSmsExpireDate(stampToDate(Long.parseLong(sms.split(",")[1])));    //失效时间
            smsEntity = this.usersService.SmsInsert(smsEntity, 1);
        }
        Result<SmssEntity> result = new Result<>(smsEntity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public String getResArgs(String phone, String code) {
        //生成6位数验证码
        String codeNew = String.valueOf(new Random().nextInt(899999) + 100000);
        if (code != "") {
            codeNew = code;
        }
        map = new HashMap<>();
        map.put("code", codeNew);
        map.put("phone", phone);
        return codeNew;
    }

    /*
     * 将时间戳转换为时间并加10分钟
     */
    public Date stampToDate(Long s) {
        Date date = new Date(s);
        date.setTime(date.getTime() + 10 * 60 * 1000);
        return date;
    }


    /**
     * 微信测试
     */
    private final String AppID = "";

    public String wxLogin(HttpServletResponse response) throws IOException {
        //这里是回调的url
        String redirect_url = URLEncoder.encode("http://回调页面的路径", "UTF-8");
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=APPID" +
                "&redirect_uri=REDIRECT_URI" +
                "&response_type=code" +
                "&scope=SCOPE" +
                "&state=123#wechat_redirect";
        response.sendRedirect(url.replace("APPID", "你的APPID").replace("REDIRECT_URL", redirect_url).replace("SCOPE", "snsapi_userinfo"));
        return url;
    }

    public void index(String code) throws JSONException {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
        String param = "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        String OpenOne = HttpRequestUtils.sendGet(url, param.replace("APPID", "你的APPID").replace("SECRET", "你的SECRET")
                .replace("CODE", code));
        //第二次请求
        url = "https://api.weixin.qq.com/sns/userinfo";
        param = "access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        HttpRequestUtils.sendGet(url, param.replace("OPENID", "openid").replace("ACCESS_TOKEN", "access_token"));

    }


}
