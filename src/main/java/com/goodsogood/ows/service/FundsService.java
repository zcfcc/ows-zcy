package com.goodsogood.ows.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.goodsogood.ows.mapper.DataMapper;
import com.goodsogood.ows.mapper.FundsMapper;
import com.goodsogood.ows.model.db.FundsEntity;
import com.goodsogood.ows.model.db.PageNumber;
import com.goodsogood.ows.model.vo.FundsVo;
import com.goodsogood.ows.model.vo.LoginResult;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class FundsService {
    private FundsMapper mapper;
    private DataMapper dataMapper;
    @Autowired
    public FundsService(FundsMapper fundsMapper,DataMapper dataMappers) {
        this.mapper = fundsMapper;
        this.dataMapper=dataMappers;
    }

    /**
     * 申请医创梦计划基金
     */
    public Integer AddFuns(FundsEntity fundsEntity) {
        Integer result = this.mapper.Insert(fundsEntity);
        return result;
    }

    /**
     * 修改 医创梦计划基金
     */
    public Integer AlterFuns(FundsEntity fundsEntity) {
        Integer result = this.mapper.Update(fundsEntity.getFundId(), fundsEntity.getTitle(), fundsEntity.getIntroduction(),fundsEntity.getSuccess());
        return result;
    }

    /**
     * 用户查询 医创梦计划基金
     */
    public PageInfo<FundsEntity> GetByUserId(Long userId,int type, PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.Get(userId,type));

    }

    /**
     * 管理员查询 医创梦计划基金
     */
    public PageInfo<FundsVo> GetByAdmin(Integer type,String name,Integer success,int types,PageNumber pageNumber) {
        int p = Preconditions.checkNotNull(pageNumber.getPage());
        int r = Preconditions.checkNotNull(pageNumber.getRows());
        PageHelper.startPage(p, r);
        return new PageInfo<>(this.mapper.GetAll(type,name,success,types));
    }

    /**
     *  管理员 删除
     * @param funId
     * @return
     */
    public LoginResult Del(Long funId)
    {
        LoginResult result =new LoginResult();
        result.setMsg("删除失败");
        result.setIsb(false);
        int num=this.mapper.CustomDelete(funId);
        boolean isb=num>0?true:false;
        if(isb)
        {
            this.dataMapper.delCount(3);
            result.setIsb(isb);
            result.setMsg("删除成功");
        }
        return  result;

    }

}
