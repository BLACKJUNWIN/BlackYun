package com.black.filePath.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.filePath.pojo.FilePath;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
public interface FilePathService extends IService<FilePath> {
    Page<FilePath> listByPage(Map<String, Object> map);
    List<FilePath> showPack(Map<String, Object> map);
}
