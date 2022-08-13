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
        Long userId = tokenUtils.requestGetId(request);
        String fileName = map.get("fileName") + "";
        String level = map.get("level") + "";

        if (filePathService.list(new QueryWrapper<FilePath>().eq("name", fileName)).size() > 0) {
            fileName += "_r" + new Random().nextInt(100);
        }

        //创建用户文件路径
        FilePath filePath = new FilePath();
        filePath.setFileId(1L);
        filePath.setName(fileName);
        filePath.setUserId(userId);
        filePath.setLevel(level);
        filePathService.save(filePath);

        return new responseJson(responseCode.SUCCESS);
    }

    @PostMapping("/upload_slice_file")
    public responseJson uploadSliceFile(@RequestParam MultipartFile file, @RequestParam String fileName, @RequestParam int index) {
        //查看是否已经存在该文件上级目录,不存在就创建
        java.io.File realFile = new java.io.File(fileUtils.path + "/temp/" + fileName);
        if (!realFile.getParentFile().exists()) {
            realFile.getParentFile().mkdirs();
        }
        //开始文件整合
        RandomAccessFile raf = null;
        InputStream fis = null;
        try {
            //随意读取文件函数
            raf = new RandomAccessFile(realFile, "rw");
            raf.seek(index * 1024 * 1024L);
            fis = file.getInputStream();
            byte[] buffer = new byte[1024 * 512];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {//读取到字节数组
                raf.write(buffer, 0, len);//写入
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new responseJson(responseCode.FILE_FAIL);
        } finally {
            //释放内存
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return new responseJson(index);
    }

    @PostMapping("/file_exist")
    public responseJson fileExist(@RequestBody File file, HttpServletRequest request) {
        File sameFile = fileService.getOne(new QueryWrapper<File>().eq("md5", file.getMd5()));
        if (sameFile != null) {//是否存在MD5相同文件,达到秒传
            FilePath filePath = new FilePath();
            filePath.setFileId(sameFile.getId());
            filePath.setUserId(tokenUtils.requestGetId(request));
            filePath.setLevel(file.getPath());
            if(filePathService.list(new QueryWrapper<FilePath>().eq("name",file.getName())).size()>=1){
                filePath.setName("r"+new Random().nextInt(10)+"*"+file.getName());
            }else{
                filePath.setName(file.getName());
            }
            filePathService.save(filePath);
            return new responseJson(responseCode.SUCCESS);
        }
        return new responseJson(responseCode.FILE_NULL);
    }

    @PostMapping("/file_combined")
    public responseJson fileCombined(@RequestBody File file, HttpServletRequest request) {
        //文件上传服务器成功,同步数据库数据
        java.io.File realFile = new java.io.File(fileUtils.path + "/temp/" + file.getRealName());
        //先创建file
        File dataFile = new File();
        dataFile.setMd5(file.getMd5());
        dataFile.setName(file.getRealName());
        dataFile.setPath(file.getPath());
        dataFile.setType(file.getName().substring(file.getName().lastIndexOf(".")+1));
        dataFile.setCategoryId(1534448966809767937L);
        dataFile.setSize(realFile.length());
        dataFile.setLevel("/");
        fileService.save(dataFile);
        File newFile = fileService.getOne(new QueryWrapper<File>().eq("md5", file.getMd5()));

        FilePath filePath = new FilePath();
        filePath.setFileId(newFile.getId());
        filePath.setUserId(tokenUtils.requestGetId(request));
        filePath.setLevel(file.getPath());
        filePath.setName(file.getName());
        filePathService.save(filePath);
        return new responseJson(responseCode.SUCCESS);
    }

    @GetMapping("/upload_cancel")
    public responseJson uploadCancel(@RequestParam String fileName){
        java.io.File File = new java.io.File(fileUtils.path + "/temp/" + fileName);
        boolean delete = File.delete();
        return new responseJson(delete);
    }
}
