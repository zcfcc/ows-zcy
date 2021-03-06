package com.goodsogood.ows.controller;

import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.component.Errors;
import com.goodsogood.ows.configuration.Global;
import com.goodsogood.ows.exception.ApiException;
import com.goodsogood.ows.model.db.FundsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.*;
import com.goodsogood.ows.service.DataService;
import com.goodsogood.ows.service.FundsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("/v-funds")
@Log4j2
@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "医创梦计划基金", tags = {"Funds"})
public class FundsController {

    private final Errors errors;
    private final FundsService service;
    private final DataService dataService;

    @Autowired
    public FundsController(Errors errors, FundsService fundsService, DataService dataService) {
        this.errors = errors;
        this.service = fundsService;
        this.dataService = dataService;
    }

    /**
     * 医创梦计划基金申请
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "添加项目")
    @PostMapping("/add")
    public ResponseEntity<Result<Boolean>> Add(@Valid @RequestBody FundsAddForm form, BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        FundsEntity entity = new FundsEntity();
        entity.setAddtime(new Date());
        entity.setIdentity(form.getIdentity());
        entity.setIntroduction(form.getIntroduction());
        entity.setTitle(form.getTitle());
        entity.setUserId(form.getUserId());
        entity.setTypes(form.getType());
        entity.setSuccess(1);
        if(form.getType() == 1)
        {
            entity.setApplyMoney(form.getApplyMoney());
        }
        Integer num = this.service.AddFuns(entity);
        Result<Boolean> result = new Result<>(true, errors);
        if (num == null || num == 0) {
            result = new Result<>(false, errors);
        } else {
            Statistics();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 统计医创梦计划
     */
    private void Statistics() {
        this.dataService.Update(3);
    }

    /**
     * 医创梦计划基金申请修改
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "修改项目")
    @PostMapping(value = "/put")
    public ResponseEntity<Result<Boolean>> Put(@Valid @RequestBody FundsPutForm form, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        FundsEntity entity = new FundsEntity();
        entity.setTitle(form.getTitle());
        entity.setIntroduction(form.getIntroduction());
        entity.setFundId(form.getFundId());
        entity.setSuccess(form.getSuccess());
        Integer num = this.service.AlterFuns(entity);
        Result<Boolean> result = new Result<>(false, errors);
        if (num > 0) {
            result = new Result<>(true, errors);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     *  根据唯一标识删除，未审核
     * @param funId
     * @param bindingResult
     * @return
     */
    @ApiOperation(value = "删除")
    @PostMapping(value = "/del")
    public  ResponseEntity<Result<LoginResult>> Del(@Valid @RequestBody Long funId,BindingResult bindingResult)
    {
        if (bindingResult.hasFieldErrors() || funId==null || funId<=0) {
            throw new ApiException("参数错误", new Result<>(Global.Errors.VALID_ERROR.getCode(), bindingResult.getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        LoginResult loginResult=this.service.Del(funId);
        Result<LoginResult> result =new Result<>(loginResult,errors);
        return  new ResponseEntity<>(result,HttpStatus.OK);
    }



    /**
     * 用户查询
     *
     * @param id       用户唯一标识
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "用户查询")
    @GetMapping(value = "/getById/{id}")
    public ResponseEntity<Result<PageInfo<FundsEntity>>> getUser(@ApiParam(value = "id", required = true)
                                                                 @PathVariable Long id, int type,Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<FundsEntity> entity = this.service.GetByUserId(id,type, new PageNumber(page, pageSize));
        Result<PageInfo<FundsEntity>> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * 管理员查询
     *
     * @param type     类型
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "管理员查询")
    @GetMapping(value = "/getByType/{type}")
    public ResponseEntity<Result<PageInfo<FundsVo>>> getByAdmin(@ApiParam(value = "type", required = true)
                                                                @PathVariable Integer type,int types ,String name,Integer success, Integer page, Integer pageSize) {
        if (page == null || pageSize == null) {
            page = 1;
            pageSize = 10;
        }
        PageInfo<FundsVo> entity = this.service.GetByAdmin(type, name, success,types,new PageNumber(page, pageSize));
        Result<PageInfo<FundsVo>> result = new Result<>(entity, errors);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
