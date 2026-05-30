package com.zzyl.mapper;

import com.zzyl.dto.BedDto;
import com.zzyl.vo.BedVo;
import org.apache.ibatis.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface BedMapper {

    List<BedVo> getBedsByRoomId(Long roomId);

    int addBed(BedVo bed);

    @Delete("delete from bed where id=#{id}")
    int deleteBed(Integer  id);

    int updateBed(@Param("bed") BedVo bed);

    BedVo getBedById(Long id);

    @Select("select * from bed where name = #{name}")
    List<BedDto> getBedByName(String name);

    @Select("select count(*) from bed where bed_number = #{bedNumber} and id != #{id}")
    int countByBedNumberExcludingId(@Param("bedNumber") String bedNumber, @Param("id") Long id);

    @Select("select count(*) from bed where bed_number = #{bedNumber}")
    int countByBedNumber(String bedNumber);
}


