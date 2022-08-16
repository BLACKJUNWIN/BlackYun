package com.black.software.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.software.pojo.Software;
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
public interface SoftwareService extends IService<Software> {
    Page<Software> listByPage(Software software);

    List<Software> selectByCategory(Map<String, Object> map);
}
