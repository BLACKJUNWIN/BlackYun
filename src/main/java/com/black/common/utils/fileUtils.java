package com.black.common.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.common.pojo.UnitDownloader;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class fileUtils {
    public static final String path="D:/file/desk/BlackYun/";
    public static final String pathMapping="http://localhost:8088/upload/";

    public static responseJson removeFile(String filePath){
        String fileName = filePath.replace(pathMapping,"");
        String realPath = path+fileName;
        File file = new File(realPath);
        if(!file.exists()){
            return new responseJson(responseCode.FILE_NULL);
        }
        if(file.delete()){
            return new responseJson("文件删除成功");
        }
        return new responseJson(responseCode.FILE_FAIL);
    }

    /**
     * 单点上传(只支持1MB一下内容上传)
     * @param file
     * @param category
     * @return
     */
    public static responseJson uploadFile(MultipartFile file,String category){
        String fileName = file.getOriginalFilename();
        String realName;
        String type = null;
        if(!commonUtils.isNull(fileName)){
            type=fileName.substring(fileName.lastIndexOf(".")+1);
        }
        realName= "BlackYun"+ UUID.randomUUID()+"."+type;
        try {
            File saveFile = new File(path + category + "\\" + realName);
            if(!saveFile.exists()){
               saveFile.getParentFile().mkdirs();
            }
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            new responseJson(responseCode.FILE_FAIL);
        }
        String filePath=pathMapping+category + "/"+realName;
        Map<String, Object> map=new HashMap<>();
        map.put("fileName",fileName);
        map.put("realName",realName);
        map.put("type",type);
        map.put("path",filePath);
        return new responseJson(map);
    }

    /**
     *
     * @param file 文件数据
     * @param category 分类名
     * @param fileName 文件名
     * @param index 文件分段上传的坐标/位置
     * @return 是否成功
     */
    public static boolean subUpload(MultipartFile file,String category,String fileName,int index){
        //查看是否已经存在该文件上级目录,不存在就创建
        java.io.File realFile = new java.io.File(path + category+"/" + fileName);
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
            return false;
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
        return true;
    }

    /**
     * 文件下载(单点传输,只支持1MB一下内容下载)//淘汰
     * @param filePath 文件路径
     * @param outputStream 输出流
     * @return 是否成功
     */
//    private static boolean down(String filePath, OutputStream outputStream){
//        try {
//            File file = new File(filePath);
//            FileInputStream fileInputStream = new FileInputStream(file);
//            byte[] fileByte;
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            byte[] bytes = new byte[1024];
//            int len;
//            while ((len = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
//                byteArrayOutputStream.write(bytes, 0, len);
//            }
//            byteArrayOutputStream.close();
//            fileByte = byteArrayOutputStream.toByteArray();
//            outputStream.write(fileByte);
//            System.out.println("图片:"+filePath+",下载成功");
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println("图片:"+filePath+",下载失败");
//            return false;
//        }
//    }

    /**
     * 断点传输
     * @param file 文件数据
     * @param outputStream 输出流
     */
    private static void subFile(File file, OutputStream outputStream){
        try {
//            System.out.println("文件名:" + file.getName());
            //获取文件总大小
            long totalSize = file.length();
//            System.out.println("文件总大小:" + totalSize/1024 + "kb,"+((float)totalSize/1048576)+"MB");
            //将文件分片并分开下载
            long threadCount = totalSize/(1024 * 1024 * 15)+1;
            int perThreadSize = (int) (totalSize / threadCount);//每一个线程分到的任务下载量
            int id = 0;
            int from = 0, to = 0;
            while (totalSize > 0) {
                id++;
                //计算分片
                if (totalSize < perThreadSize) {
                    from = 0;
                    to = (int) totalSize;
                    totalSize=0;
                } else {
                    from = to;
                    to = from + perThreadSize;
                    totalSize-=perThreadSize;
                }
                //开始下载
                UnitDownloader downloader = new UnitDownloader(from, to, file, outputStream, id);
                Thread thread = new Thread(downloader);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取递归文件夹大小
     * @param pack
     * @return
     */
    public static long packSize(File pack){
        long totalSize=0;
        for (File item : Objects.requireNonNull(pack.listFiles())) {
            if (item.isFile()){
                totalSize+=item.length();
            }else{
                totalSize+=packSize(item);
            }
        }
        return totalSize;
    }

    public static void downFile(com.black.file.pojo.File blackFile, HttpServletResponse response, HttpServletRequest request){
        String fileName = blackFile.getName();
        String filePath = path+"temp/"+fileName;
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
            }else if(blackFile.getType().equals("pack")||file.length()>1024*1024*50){
                String tempPath=path+"/tmp";
                File tempPack=new File(tempPath);
                if(!tempPack.exists()){
                    tempPack.mkdir();
                }
                String tempFilePath=path+"/tmp/"+fileName;
                File tempFile=new File(tempFilePath);
                if(!tempFile.exists()){
                    tempFile.createNewFile();
                }
                OutputStream tempStream=new FileOutputStream(tempFilePath);
                zipUtils.toZip(filePath,tempStream,true);
                size= tempFile.length();
                response.setContentLengthLong(size);
                subFile(tempFile,outputStream);
                if(tempFile.exists()){
                    tempFile.delete();
                }
            }else{
                response.setContentLengthLong(size);
                subFile(new File(filePath),outputStream);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
