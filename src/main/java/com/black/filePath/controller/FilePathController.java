package com.black.filePath.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import com.black.common.utils.fileUtils;
import com.black.common.utils.tokenUtils;
import com.black.file.pojo.File;
import com.black.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.util.*;

import com.black.filePath.pojo.FilePath;
import com.black.filePath.service.FilePathService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/filePath")
public class FilePathController {

    @Autowired
    FilePathService filePathService;

    @Autowired
    FileService fileService;

    @PostMapping("/")
    public responseJson add(@RequestBody FilePath filePath) {
        filePathService.save(filePath);
        return new responseJson(filePathService.listByPage(new HashMap<>()));
    }

    @DeleteMapping("/{id}")
    public responseJson delCategory(@PathVariable Long id) {
        filePathService.removeById(id);
        return new responseJson(filePathService.listByPage(new HashMap<>()));
    }

    @PutMapping("/")
    public responseJson upd(@RequestBody FilePath filePath) {
        filePathService.updateById(filePath);
        return new responseJson(filePathService.listByPage(new HashMap<>()));
    }

    @PatchMapping("/")
    public responseJson select(@RequestBody Map<String, Object> map) {
        return new responseJson(filePathService.listByPage(map));
    }

    @PostMapping("/show_pack")
    public responseJson showPack(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        map.put("userId", tokenUtils.requestGetId(request));
        return new responseJson(filePathService.showPack(map));
    }

    @PostMapping("/new_mkdir")
    public responseJson newMkdir(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String fileName = map.get("fileName") + "";

        if (filePathService.list(new QueryWrapper<FilePath>().eq("name", fileName)).size() > 0) {
            fileName += "_r" + new Random().nextInt(100);
        }

        //创建用户文件路径
        FilePath filePath = new FilePath();
        filePath.setFileId(1L);
        filePath.setName(fileName);
        filePath.setUserId(tokenUtils.requestGetId(request));
        filePath.setLevel(map.get("level") + "");
        filePathService.save(filePath);

        return new responseJson(responseCode.SUCCESS);
    }

    @PostMapping("/upload_slice_file")
    public responseJson uploadSliceFile(@RequestParam MultipartFile file, @RequestParam String fileName, @RequestParam int index) {
        boolean temp = fileUtils.subUpload(file, "temp", fileName, index);
        if (temp) {
            return new responseJson(index);
        }
        return new responseJson(responseCode.FILE_FAIL);

    }

    @PostMapping("/file_exist")
    public responseJson fileExist(@RequestBody File file, HttpServletRequest request) {
        File sameFile = fileService.getOne(new QueryWrapper<File>().eq("md5", file.getMd5()));
        if (sameFile == null) {
            return new responseJson(responseCode.FILE_NULL);
        }
        if (!filePathService.saveFilePath(file, sameFile.getId(), tokenUtils.requestGetId(request))) {
            return new responseJson(responseCode.FAIL);
        }
        return new responseJson(responseCode.SUCCESS);

    }

    @PostMapping("/file_combined")
    public responseJson fileCombined(@RequestBody File file, HttpServletRequest request) {
        //文件上传服务器成功,同步数据库数据
        file.setType(file.getName().substring(file.getName().lastIndexOf(".") + 1));
        file.setCategoryId(1534448966809767937L);
        file.setSize((new java.io.File(fileUtils.path + "/temp/" + file.getRealName())).length());
        file.setLevel("/");
        fileService.save(file);

        File newFile = fileService.getOne(new QueryWrapper<File>().eq("md5", file.getMd5()));
        if (filePathService.saveFilePath(file, newFile.getId(), tokenUtils.requestGetId(request))) {
            return new responseJson(responseCode.SUCCESS);
        }
        return new responseJson(responseCode.FAIL);
    }

    @GetMapping("/upload_cancel")
    public responseJson uploadCancel(@RequestParam String fileName) {
        java.io.File File = new java.io.File(fileUtils.path + "/temp/" + fileName);
        boolean delete = File.delete();
        return new responseJson(delete);
    }

    @GetMapping("/down")
    public void downFile(@RequestParam String md5, HttpServletResponse response, HttpServletRequest request) {
        File file = fileService.getOne(new QueryWrapper<com.black.file.pojo.File>().eq("md5", md5));
        fileUtils.downFile(file, response, request);
    }
}
