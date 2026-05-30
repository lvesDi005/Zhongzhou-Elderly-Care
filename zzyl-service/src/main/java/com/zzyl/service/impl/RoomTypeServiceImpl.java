package com.zzyl.service.impl;

import com.zzyl.config.OSSAliyunFileStorageService;
import com.zzyl.dto.RoomTypeDto;
import com.zzyl.entity.RoomType;
import com.zzyl.mapper.RoomTypeMapper;
import com.zzyl.service.RoomService;
import com.zzyl.service.RoomTypeService;
import com.zzyl.utils.ObjectUtil;
import com.zzyl.vo.RoomTypeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    @Autowired
    private RoomTypeMapper roomTypeMapper;

    @Autowired
    private RoomService roomService;

    @Autowired
    private OSSAliyunFileStorageService fileStorageService;

    /**
     * 添加房间类型
     * @param roomTypeDTO 房间类型DTO
     */
    @Override
    public void addRoomType(RoomTypeDto roomTypeDTO) {
        RoomType roomType = new RoomType();
        BeanUtils.copyProperties(roomTypeDTO, roomType);
        roomTypeMapper.addRoomType(roomType);
    }

    /**
     * 删除房间类型
     * @param id 房间类型id
     */
    @Override
    public void removeRoomType(Long id) {
        RoomType roomType = roomTypeMapper.findRoomTypeById(id);
        if (StringUtils.hasText(roomType.getPhoto())) {
            fileStorageService.delete(roomType.getPhoto());
        }
        roomTypeMapper.removeRoomType(id);
    }

    /**
     * 修改房间类型
     * @param id 房间类型id
     * @param roomTypeDTO 房间类型DTO
     */
    @Override
    public void modifyRoomType(Long id, RoomTypeDto roomTypeDTO) {
        // 查询旧数据，如果图片有变化则删除旧图片
        RoomType oldRoomType = roomTypeMapper.findRoomTypeById(id);
        String oldPhoto = oldRoomType.getPhoto();
        String newPhoto = roomTypeDTO.getPhoto();
        if (StringUtils.hasText(oldPhoto) && !oldPhoto.equals(newPhoto)) {
            fileStorageService.delete(oldPhoto);
        }

        RoomType roomType = new RoomType();
        BeanUtils.copyProperties(roomTypeDTO, roomType);
        roomType.setId(id);
        roomTypeMapper.modifyRoomType(roomType);
    }

    /**
     * 根据id查找房间类型
     * @param id 房间类型id
     * @return 房间类型VO
     */
    @Override
    public RoomTypeVo findRoomTypeById(Long id) {
        RoomType roomType = roomTypeMapper.findRoomTypeById(id);
        RoomTypeVo roomTypeVO = new RoomTypeVo();
        BeanUtils.copyProperties(roomType, roomTypeVO);
        return roomTypeVO;
    }

    /**
     * 查找所有房间类型
     * @return 房间类型VO列表
     */
    @Override
    public List<RoomTypeVo> findRoomTypeList() {
        List<RoomType> roomTypes = roomTypeMapper.findRoomTypeList();
        return convertToVOList(roomTypes);
    }

    /**
     * 根据状态查找房间类型
     * @param status 状态
     * @return 房间类型VO列表
     */
    @Override
    public List<RoomTypeVo> findRoomTypeListByStatus(Integer status) {
        List<RoomType> roomTypes = roomTypeMapper.findRoomTypeListByStatus(status);
        return convertToVOList(roomTypes);
    }

    /**
     * 根据类型名查找房间类型
     * @param typeName 类型名
     * @return 房间类型VO列表
     */
    @Override
    public List<RoomTypeVo> findRoomTypeListByTypeName(String typeName) {
        List<RoomType> roomTypes = roomTypeMapper.findRoomTypeListByTypeName(typeName);
        return convertToVOList(roomTypes);
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        roomTypeMapper.updateStatus(id, status);
    }

    /**
     * 将房间类型列表转换为VO列表
     * @param roomTypes 房间类型列表
     * @return 房间类型VO列表
     */
    private List<RoomTypeVo> convertToVOList(List<RoomType> roomTypes) {
        List<String> collect = roomTypes.stream().map(RoomType::getName).distinct().collect(Collectors.toList());
        if(collect == null || collect.size() == 0){
            return new ArrayList<>();
        }
        Map<String, Integer> map = roomService.countRoomByTypeName(collect);
        return roomTypes.stream().map(roomType -> {
            RoomTypeVo roomTypeVO = new RoomTypeVo();
            BeanUtils.copyProperties(roomType, roomTypeVO);
            Object o = map.get(roomType.getName());
            if (ObjectUtil.isNotEmpty(o)) {
                HashMap hashMap  = (HashMap) o;
                roomTypeVO.setRoomCount(Integer.parseInt(hashMap.get("count").toString()));
            } else {
                roomTypeVO.setRoomCount(0);
            }
            return roomTypeVO;
        }).collect(Collectors.toList());
    }
}
