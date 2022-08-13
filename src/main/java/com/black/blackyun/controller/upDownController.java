package com.black.blackyun.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.common.utils.fileUtils;
import com.black.common.pojo.responseJson;
import com.black.common.utils.zipUtils;
import com.black.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1")
public class upDownController {

//    云盘根路径
//    public String mainPath="/usr/blackjun/blackYun";
    public String mainPath="D:\\file\\desk\\BlackYun";

    @Autowired
    FileService fileService;
    /**
     * 获取对应包目录文件
     * @param map
     * @return
     */
    @PostMapping("/showPack")
    public responseJson showPack(@RequestBody Map<String, Object> map){
        try {
            String path= map.get("path").toString();
            if (path.equals("null")){
                path= mainPath;
            }
            File file = new File(path);
            File[] tempList = file.listFiles();
            if(tempList==null||tempList.length==0){
                return new responseJson(null);
            }
            List<blackFile> blackFiles=new ArrayList<>();
            for (File item : tempList) {
                String name = item.getName();
                blackFile blackFile;
                if(item.isFile()){
                    int middle = name.lastIndexOf(".");
                    blackFile =new blackFile(mainPath,item.toString(),name.substring(0,middle),name.substring(++middle),item.length());
                }else{
                    blackFile =new blackFile(mainPath,item.toString(),name,"pack",fileUtils.packSize(item));
                }
                blackFiles.add(blackFile);
            }
            return new responseJson(blackFiles);
        } catch (Exception e) {
            e.printStackTrace();
            return new responseJson(500);
        }
    }

    /**
     * 下载文件
     * @param response
     * @param request
     */
    @RequestMapping("/down")
    public void down(@RequestParam String md5, HttpServletResponse response, HttpServletRequest request){
        com.black.file.pojo.File blackFile = fileService.getOne(new QueryWrapper<com.black.file.pojo.File>().eq("md5", md5));
        String fileName = blackFile.getName();
        String filePath = mainPath+"\\temp\\"+fileName;
        File file=new File(filePath);
        // 获取浏览器的信息
        try {
            String agent = request.getHeader("USER-AGENT");
            response.setHeader("content-type", "application/octet-stream");
            response.setContentType("application/octet-stream");
            if (agent != null && agent.toLowerCase().indexOf("FIRE_FOX") > 0) {
                //火狐浏览器自己会对URL进行一次URL转码所以区别处理
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String(fileName.getBytes("GB2312"), StandardCharsets.ISO_8859_1));
            } else if (agent.toLowerCase().indexOf("SAFARI") > 0) {
                //苹果浏览器需要用ISO 而且文件名得用UTF-8
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            } else {
                //其他的浏览器
                response.setHeader("Content-Disposition",
                        "attachment;filename="  + URLEncoder.encode(fileName, "UTF-8"));
            }
            OutputStream outputStream = null;
            outputStream = response.getOutputStream();
            long size=file.length();
            if(blackFile.getType().equals("zip")||blackFile.getType().equals("7z")||blackFile.getType().equals("rar")){
                response.setContentLengthLong(size);
                fileUtils.subFile(new File(filePath),outputStream);
//                response.setHeader("content-length",size+"");
            }else if(blackFile.getType().equals("pack")||file.length()>1024*1024*50){
                String tempPath=mainPath+"/临时文件夹";
                File tempPack=new File(tempPath);
                if(!tempPack.exists()){
                    tempPack.mkdir();
                }
                String tempFilePath=mainPath+"/临时文件夹/"+fileName;
                File tempFile=new File(tempFilePath);
                if(!tempFile.exists()){
                    tempFile.createNewFile();
                }
                OutputStream tempStream=new FileOutputStream(tempFilePath);
                zipUtils.toZip(filePath,tempStream,true);
                size= tempFile.length();
                response.setContentLengthLong(size);
                fileUtils.subFile(tempFile,outputStream);
                if(tempFile.exists()){
                    tempFile.delete();
                }
            }else{
                response.setContentLengthLong(size);
                fileUtils.subFile(new File(filePath),outputStream);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 文件上传
//     * @param file 前端文件
//     * @param filePath 储存路径
//     * @return
//     */
//    @PostMapping("/upload")
//    public responseJson upload(@RequestParam MultipartFile file,@RequestParam String filePath){
//        try {
//            if(filePath.equals("null")){
//                filePath=mainPath+"/临时文件夹";
//                File tempFile=new File(filePath);
//                if(!tempFile.exists()){
//                    tempFile.mkdir();
//                }
//            }
//            if(fileUtils.uploadFile(file,filePath,file.getOriginalFilename())){
//                return new responseJson("成功");
//            }
//            return new responseJson(423);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new responseJson(500);
//        }
//    }

    /**
     * 新建文件夹
     * @param path 创建路径
     * @param fileName 文件名
     * @return
     */
    @GetMapping("/newMkdir")
    public responseJson newMkdir(String path,String fileName){
        try {
            System.out.println(path);
            System.out.println(fileName);
            String filePath;
            if(path.equals("null")){
                filePath =mainPath+"/"+fileName;
            }else{
                filePath =path+"/"+fileName;
            }
            File tempFile=new File(filePath);
            if(!tempFile.exists()){
                tempFile.mkdir();
                System.out.println("文件:"+fileName+"创建成功!");
                return new responseJson("操作成功");
            }
            System.out.println("文件:"+filePath+"已经存在!");
            return new responseJson(424);
        } catch (Exception e) {
            e.printStackTrace();
            return new responseJson(500);
        }
    }

    /**
     * 删除文件
     * @return
     */
    @PostMapping("/delete")
    public responseJson delete(@RequestBody String md5){
        try {
            com.black.file.pojo.File blackFile = fileService.getOne(new QueryWrapper<com.black.file.pojo.File>().eq("md5", md5));
            File file=new File(mainPath+blackFile.getPath()+blackFile.getRealName());
            if(file.delete()){
                return new responseJson("文件删除成功!");
            }else{
                return new responseJson(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new responseJson(500);
        }
    }

    /**
     * 获取信息
     * @return
     */
    @PostMapping("/info")
    public responseJson info(){
        try {
            File file = new File(mainPath);
            long length = file.length();
            return new responseJson(length);
        } catch (Exception e) {
            e.printStackTrace();
            return new responseJson(500);
        }
    }

}
