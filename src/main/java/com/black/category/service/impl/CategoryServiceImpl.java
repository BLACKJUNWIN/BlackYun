package com.black.category.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.category.pojo.Category;
import com.black.category.mapper.CategoryMapper;
import com.black.category.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public Page<Category> listByPage(Map<String, Object> map){
        int page=1;
        if(map.get("page")!=null){
            page = (int)map.get("page");
        }
        Page<Category> paramPage = new Page<>(page,10);
        categoryMapper.selectPage(paramPage,null);
        return paramPage;
    }
}
