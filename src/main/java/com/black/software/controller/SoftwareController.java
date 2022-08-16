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
        if (software.getMd5() != null && !software.getMd5().equals("")) {
            File file = new File();
            File sameFile = fileService.getOne(new QueryWrapper<File>().eq("md5", software.getMd5()));
            if (sameFile == null) {
                file.setRealName(software.getRealName());
                file.setName(software.getName());
                file.setPath(software.getPath());
                file.setSize(software.getSize());
                file.setType(software.getType());
                file.setMd5(software.getMd5());
                file.setCategoryId(software.getCategoryId());
                fileService.save(file);
            } else {
                return new responseJson(responseCode.FILE_EXIST);
            }
            File saveFile = fileService.getOne(new QueryWrapper<File>().eq("md5", software.getMd5()));
            software.setFileId(saveFile.getId());
        }
        List<Software> fileList = softwareService.list(new QueryWrapper<Software>().eq("file_id",software.getFileId()));
        if (fileList.size() >= 1) {
            return new responseJson(responseCode.FILE_EXIST);
        }
        softwareService.save(software);
        return new responseJson(softwareService.listByPage(new Software()));
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
}
