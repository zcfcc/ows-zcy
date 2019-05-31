package com.goodsogood.ows.mapper;

import com.goodsogood.ows.model.db.UsersEntity;
import com.goodsogood.ows.model.vo.UserInfoVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UsersMapper extends MyMapper<UsersEntity> {
    @Insert({
            "<script>",
            "INSERT INTO zcy_users(",
            "<if test='userId !=null' >user_id,</if> ",
            "user_name,user_hospital,user_department,",
            "user_position,user_email,user_bank_card_number,user_cardholder_name,",
            "user_cardholder_phone,user_cardholder_idcard,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{userName,jdbcType=VARCHAR},#{userHospital,jdbcType=VARCHAR},",
            "#{userDepartment,jdbcType=VARCHAR},#{userPosition,jdbcType=BIT},",
            "#{userEmail,jdbcType=VARCHAR},#{userBankCardNumber,jdbcType=VARCHAR},",
            "#{userCardholderName,jdbcType=VARCHAR},#{userCardholderPhone,jdbcType=VARCHAR},",
            "#{userCardholderIdcard,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "user_id", keyProperty = "userId", before = false, resultType = Long.class)
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long PersonalInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_users(",
            "<if test='userId !=null' >user_id,</if> ",
            "company_name,company_code,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{companyName,jdbcType=VARCHAR},#{companyCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "user_id", keyProperty = "userId", before = false, resultType = Long.class)
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long CompanyInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_users(",
            "<if test='userId !=null' >user_id,</if> ",
            "organization_name,organization_code,review,enable,code,referrer,is_referrer,addtime,updatetime",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{organizationName,jdbcType=VARCHAR},#{organizationCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP}",
            ")",
            "</script>"
    })
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "user_id", keyProperty = "userId", before = false, resultType = Long.class)
//    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    Long MechanismInsert(UsersEntity usersEntity);

    @Insert({
            "<script>",
            "INSERT INTO zcy_users(",
            "<if test='userId !=null' >user_id,</if> ",
            "user_name,user_hospital,user_department,",
            "user_position,user_email,user_bank_card_number,user_cardholder_name,",
            "user_cardholder_phone,user_cardholder_idcard,company_name,company_code,organization_name,organization_code,review,enable,code,referrer,",
            "is_referrer,addtime,updatetime,phone",
            ") VALUES(",
            "<if test='userId !=null' >#{userId,jdbcType=BIGINT},</if> ",
            "#{userName,jdbcType=VARCHAR},#{userHospital,jdbcType=VARCHAR},",
            "#{userDepartment,jdbcType=VARCHAR},#{userPosition,jdbcType=BIT},",
            "#{userEmail,jdbcType=VARCHAR},#{userBankCardNumber,jdbcType=VARCHAR},",
            "#{userCardholderName,jdbcType=VARCHAR},#{userCardholderPhone,jdbcType=VARCHAR},",
            "#{userCardholderIdcard,jdbcType=VARCHAR},#{companyName,jdbcType=VARCHAR},#{companyCode,jdbcType=VARCHAR},#{organizationName,jdbcType=VARCHAR},#{organizationCode,jdbcType=VARCHAR},#{review,jdbcType=BIT},",
            "#{enable,jdbcType=BIT},#{code,jdbcType=VARCHAR},#{referrer,jdbcType=BIGINT},",
            "#{isReferrer,jdbcType=BIT},#{addtime,jdbcType=TIMESTAMP},#{updatetime,jdbcType=TIMESTAMP},",
            "#{phone,jdbcType=VARCHAR})",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "user_id")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyColumn = "user_id", keyProperty = "userId", before = false, resultType = Long.class)
    Long Insert(UsersEntity usersEntity);


    @Select({
            "<script>",
            "SELECT ",
            "zu.addtime,zu.`code`,zu.company_code as companyCode,zu.company_name as companyName",
            ",zu.`enable`,zu.is_referrer as isReferrer,zu.organization_code as organizationCode,",
            "zu.organization_name as organizationName,zu.referrer ,zu.review ,zu.updatetime,",
            "zu.user_bank_card_number as userBankCardNumber,zu.user_cardholder_idcard as userCardholderIdcard",
            ",zu.user_cardholder_name as userCardholderName,zu.user_cardholder_phone as userCardholderPhone",
            ",zu.user_department as userDepartment,zu.user_email as userEmail,zu.user_hospital as userHospital",
            ",zu.user_id as userId,zu.user_name as userName,zu.user_position as userPosition,",
            "zr.role_id as roleId,zr.role_name as roleName,zu.phone",
            "FROM zcy_users zu ",
            "LEFT JOIN zcy_accounts_users_roles zaur on zu.user_id=zu.user_id",
            "LEFT JOIN zcy_roles zr on zaur.role_id=zr.role_id ",
            "WHERE zu.user_id=#{userId,jdbcType=BIGINT} and zu.review=2 and  zu.enable=1",
            "</script>"
    })
    UserInfoVo GetUserById(@Param(value = "userId") Long userId);


    @Select({
            "<script>",
            "SELECT ",
            "zu.addtime,zu.`code`,zu.company_code as companyCode,zu.company_name as companyName",
            ",zu.`enable`,zu.is_referrer as isReferrer,zu.organization_code as organizationCode,",
            "zu.organization_name as organizationName,zu.referrer ,zu.review ,zu.updatetime,",
            "zu.user_bank_card_number as userBankCardNumber,zu.user_cardholder_idcard as userCardholderIdcard",
            ",zu.user_cardholder_name as userCardholderName,zu.user_cardholder_phone as userCardholderPhone",
            ",zu.user_department as userDepartment,zu.user_email as userEmail,zu.user_hospital as userHospital",
            ",zu.user_id as userId,zu.user_name as userName,zu.user_position as userPosition,",
            "zr.role_id as roleId,zr.role_name as roleName,",
            "zu.phone",
            "FROM zcy_users zu ",
            "LEFT JOIN zcy_accounts_users_roles zaur on zaur.user_id=zu.user_id",
            "LEFT JOIN zcy_roles zr on zaur.role_id=zr.role_id ",
            "</script>"

    })
        // "WHERE zu.review=2 and  zu.enable=2",
    List<UserInfoVo> GetByAll();


    @Select({
            "<script>",
            "SELECT ",
            "zu.addtime,zu.`code`,zu.company_code as companyCode,zu.company_name as companyName",
            ",zu.`enable`,zu.is_referrer as isReferrer,zu.organization_code as organizationCode,",
            "zu.organization_name as organizationName,zu.referrer ,zu.review ,zu.updatetime,",
            "zu.user_bank_card_number as userBankCardNumber,zu.user_cardholder_idcard as userCardholderIdcard",
            ",zu.user_cardholder_name as userCardholderName,zu.user_cardholder_phone as userCardholderPhone",
            ",zu.user_department as userDepartment,zu.user_email as userEmail,zu.user_hospital as userHospital",
            ",zu.user_id as userId,zu.user_name as userName,zu.user_position as userPosition,",
            "zr.role_id as roleId,zr.role_name as roleName,",
            "zu.phone,zaur.account_id as accountId",
//          "(select phone from zcy_accounts za where za.account_id=zaur.account_id and za.enable=1)phone",
            "FROM zcy_users zu ",
            "LEFT JOIN zcy_accounts_users_roles zaur on zaur.user_id=zu.user_id",
            "LEFT JOIN zcy_roles zr on zaur.role_id=zr.role_id  where zr.role_id =#{roleId,jdbcType=BIGINT}",
            "<if test='name !=null'> and zu.user_name like concat(concat('%',#{name,jdbcType=VARCHAR}),'%')</if>",
            "</script>"

    })
        // "WHERE zu.review=2 and  zu.enable=2",
    List<UserInfoVo> GetByRoleAll(@Param(value = "roleId") Long roleId,@Param(value = "name") String name);


    @Update({
            "<script>",
            "UPDATE zcy_users SET `enable`=#{seal,jdbcType=BIT} where user_id=#{userId,jdbcType=BIGINT}",
            "</script>"
    })
    int UpdateForbidden(@Param(value = "userId") Long userId, @Param(value = "seal") Integer seal);


    @Update({
            "<script>",
            "UPDATE zcy_users SET review=#{isDel,jdbcType=BIT} WHERE user_id=#{userId,jdbcType=BIGINT} and review=1",
            "</script>"
    })
    int Examine(@Param(value = "userId") Long userId,@Param(value = "isDel") Integer isDel);

    @Select({
            "<script>",
            " Select Count(0) from  zcy_users where user_id=#{userId,jdbcType=BIGINT}  and review=2 and  enable=1",
            "</script>"
    })
    int GetCount(@Param(value = "userId") Long userId);
}
