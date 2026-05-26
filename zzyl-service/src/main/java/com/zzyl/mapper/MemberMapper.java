package com.zzyl.mapper;

import com.zzyl.entity.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author sjqn
 */
@Mapper
public interface MemberMapper {

    @Select("SELECT * FROM member WHERE open_id = #{openId}")
    Member getByOpenId(String openId);

    void save(Member member);

    void update(Member member);

}