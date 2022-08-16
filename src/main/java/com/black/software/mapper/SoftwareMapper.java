package com.black.software.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.software.pojo.Software;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
@Mapper
public interface SoftwareMapper extends BaseMapper<Software> {
    Page<Software> selectByPage(Page<Software> page,Software software);
    List<Software> selectByCategory(Map<String, Object> map);
}
