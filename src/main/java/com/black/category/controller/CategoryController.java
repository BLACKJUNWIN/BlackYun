package com.black.category.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.category.pojo.Category;
import com.black.category.service.CategoryService;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-07
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @PostMapping("/")
    public responseJson addCategory(@RequestBody Category category){
        Category oldCategory = categoryService.getOne(new QueryWrapper<Category>().eq("category", category.getCategory()));
        if(oldCategory!=null){
            return new responseJson(responseCode.OBJECT_EXIST);
        }
        categoryService.save(category);
        return new responseJson(categoryService.listByPage(new HashMap<String, Object>()));
    }

    @DeleteMapping("/{id}")
    public responseJson delCategory(@PathVariable Long id){
        categoryService.removeById(id);
        return new responseJson(categoryService.listByPage(new HashMap<String, Object>()));
    }

    @PutMapping("/")
    public responseJson updCategory(@RequestBody Category category){
        categoryService.updateById(category);
        return new responseJson(categoryService.listByPage(new HashMap<String, Object>()));
    }

    @PatchMapping("/")
    public responseJson select(@RequestBody Map<String,Object> map){
        return new responseJson(categoryService.listByPage(map));
    }

    @GetMapping("/")
    public responseJson all(){
        return new responseJson(categoryService.list());
    }
}

