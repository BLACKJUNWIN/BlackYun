package com.black.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.common.utils.commonUtils;
import com.black.common.utils.fileUtils;
import com.black.file.pojo.File;
import com.black.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/fileUtils")
public class fileUtilsController {
    @Autowired
    FileService fileService;
    @PostMapping("/")
    public responseJson addFile(@RequestParam MultipartFile file,@RequestParam String category){
        return fileUtils.uploadFile(file,category);
    }

    @DeleteMapping("/")
    public responseJson deleteFile(@RequestBody Map<String, Object> map){
        return fileUtils.removeFile(map.get("filePath").toString());
    }
}
