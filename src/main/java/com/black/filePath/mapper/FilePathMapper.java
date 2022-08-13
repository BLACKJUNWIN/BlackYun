package com.black.filePath.mapper;

import com.black.filePath.pojo.FilePath;
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
public interface FilePathMapper extends BaseMapper<FilePath> {
    List<FilePath> showPack(Map<String, Object> map);
}
