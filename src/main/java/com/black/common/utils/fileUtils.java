package com.black.common.utils;

import com.black.common.pojo.UnitDownloader;
import com.black.common.pojo.responseCode;
import com.black.common.pojo.responseJson;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class fileUtils {
    public static final String path="D:\\file\\desk\\BlackYun\\";
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
     * 单点下载(内存会溢出,淘汰)
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
     * 文件下载(单点传输,会内存溢出,淘汰)
     * @param filePath
     * @param outputStream
     * @return
     */
    public static boolean down(String filePath, OutputStream outputStream){
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] fileByte;
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = fileInputStream.read(bytes, 0, bytes.length)) != -1) {
                byteArrayOutputStream.write(bytes, 0, len);
            }
            byteArrayOutputStream.close();
            fileByte = byteArrayOutputStream.toByteArray();
            outputStream.write(fileByte);
            System.out.println("图片:"+filePath+",下载成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("图片:"+filePath+",下载失败");
            return false;
        }
    }

    /**
     * 断点传输
     * @param file
     * @param outputStream
     */
    public static boolean subFile(File file,OutputStream outputStream){
        try {
            System.out.println("文件名:" + file.getName());
            //获取文件总大小
            long totalSize = file.length();
            System.out.println("文件总大小:" + totalSize/1024 + "kb,"+((float)totalSize/1048576)+"MB");
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
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取递归文件夹大小
     * @param pack
     * @return
     */
    public static long packSize(File pack){
        long totalSize=0;
        for (File item : pack.listFiles()) {
            if (item.isFile()){
                totalSize+=item.length();
            }else{
                totalSize+=packSize(item);
            }
        }
        return totalSize;
    }
}
