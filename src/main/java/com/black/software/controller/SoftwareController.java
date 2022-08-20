package com.black.software.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.category.pojo.Category;
import com.black.category.service.CategoryService;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.file.pojo.File;
import com.black.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.black.software.pojo.Software;
import com.black.software.service.SoftwareService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/software")
public class SoftwareController {

    @Autowired
    SoftwareService softwareService;

    @Autowired
    FileService fileService;

    @Autowired
    CategoryService categoryService;

    @PostMapping("/")
    public responseJson add(@RequestBody Software software) {
            File file = new File();
            Software sameFile = softwareService.getOne(new QueryWrapper<Software>().eq("name", software.getName()));
            if (sameFile == null) {
                softwareService.save(software);
                return new responseJson(softwareService.listByPage(new Software()));
            } else {
                return new responseJson(responseCode.NAME_REPEAT);
            }
        }

    @DeleteMapping("/{id}")
    public responseJson delCategory(@PathVariable Long id) {
        Software oldSoftware = softwareService.getById(id);
        fileService.removeById(oldSoftware.getFileId());
        softwareService.removeById(id);
        return new responseJson(softwareService.listByPage(new Software()));
    }

    @PutMapping("/")
    public responseJson upd(@RequestBody Software software) {
        softwareService.updateById(software);
        return new responseJson(softwareService.listByPage(new Software()));
    }

    @PatchMapping("/")
    public responseJson select(@RequestBody Software software) {
        return new responseJson(softwareService.listByPage(software));
    }

    @GetMapping("/")
    public responseJson softwareList(){
        List<Category> categoryList = categoryService.list();
        Map<String, Object> filerMap=new HashMap<>();
        List<Object> list=new ArrayList<>();
        filerMap.put("size",10);
        for (Category item : categoryList) {
            filerMap.put("categoryId",item.getId());
            List<Software> softwareList = softwareService.selectByCategory(filerMap);
            if(softwareList.size()>0){
                Map<String, Object> map=new HashMap<>();
                map.put("category",item.getCategory());
                map.put("themeColor",item.getThemeColor());
                map.put("categoryId",item.getId());
                map.put("software",softwareList);
                list.add(map);
            }
        }
        return new responseJson(list);
    }

    @PostMapping("/verify")
    public responseJson verify(@RequestBody  Map<String, Object> map){
        Software software = softwareService.getById(Long.parseLong(map.get("id")+""));
        if(software.getVerify()==0){
            software.setVerify(1);
        }else{
            software.setVerify(0);
        }
        softwareService.updateById(software);
        return new responseJson("success");
    }
}
