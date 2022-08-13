package com.black.category.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.category.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-07
 */
public interface CategoryService extends IService<Category> {
    Page<Category> listByPage(Map<String, Object> map);
}
