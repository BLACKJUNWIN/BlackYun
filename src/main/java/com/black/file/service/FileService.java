package com.black.file.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.file.pojo.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
public interface FileService extends IService<File> {
    Page<File> listByPage(Map<String, Object> map);
}
