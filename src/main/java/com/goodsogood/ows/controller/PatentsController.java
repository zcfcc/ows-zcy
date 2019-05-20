package com.goodsogood.ows.controller;


import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.PatentsEntity;
import com.goodsogood.ows.model.vo.PatentApplicationForm;
import com.goodsogood.ows.model.vo.Result;
import com.goodsogood.ows.service.PatentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/v-patents")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "我的知识产权", tags = {"Patents manager"})
public class PatentsController {
    private final PatentsService service;
    private final Errors errors;

    public PatentsController(PatentsService patentsService, Errors errors) {
        this.service = patentsService;
        this.errors = errors;
    }

    /**
     * 专利申请
     */
    @ApiModelProperty(value = "申请专利")
    @PostMapping("/patentAdd")
    public ResponseEntity<Result<Boolean>> PatentApplication(@Valid @RequestBody PatentApplicationForm patent, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setPatentType(1);
        entity.setPatentMoney(0);
        entity.setIsNeedPay(2);
        entity.setAddtime(new Date());
        entity.setIsPay(0);
        entity.setPatentContent(patent.content);
        entity.setPatentTitle(patent.title);
        entity.setUserId(patent.userid);
        Boolean bool = this.service.Insert(entity);
        if (!bool) {
            throw new ApiException("服务器繁忙，申请专利失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 我的专利
     */
    @ApiModelProperty(value = "我的专利")
    @PostMapping("/myPatentAdd")
    public ResponseEntity<Result<Boolean>> MyPatent(@Valid @RequestBody PatentApplicationForm patent, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        PatentsEntity entity = new PatentsEntity();
        entity.setPatentType(3);
        entity.setPatentMoney(0);
        entity.setIsNeedPay(2);
        entity.setAddtime(new Date());
        entity.setIsPay(0);
        entity.setPatentContent(patent.content);
        entity.setPatentTitle(patent.title);
        entity.setUserId(patent.userid);
        Boolean bool = this.service.Insert(entity);
        if (!bool) {
            throw new ApiException("服务器繁忙，申请专利失败", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        Result<Boolean> result = new Result<>(true, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


}
