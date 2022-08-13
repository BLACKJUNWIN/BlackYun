package com.black.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.*;

@Data
@NoArgsConstructor
public class UnitDownloader implements Runnable {
    private int from;
    private int to;
    private File file;
    private OutputStream outputStream;
    private int id;
    public static long fileSize;

    public UnitDownloader(int from, int to, File file, OutputStream outputStream, int id) {
        this.from = from;
        this.to = to;
        this.file = file;
        this.outputStream = outputStream;
        this.id = id;
        if(fileSize==0){
            fileSize=file.length();
        }
    }

    @Override
    public void run() {
        try {
            long perThreadSize=to-from;//获取文件总大小
            long realSize=perThreadSize;
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(from);
            byte[] buffer = new byte[1024 * 1024];
            int readCount=0;
            while (perThreadSize > 0) {
                boolean isLast=to-from<buffer.length;
                if(isLast){
                    readCount = randomAccessFile.read(buffer,0,to-from);
                }else{
                    readCount = randomAccessFile.read(buffer, 0, buffer.length);
                }
                outputStream.write(buffer, 0, readCount);
                from+=readCount;
                perThreadSize -= readCount;
            }
            fileSize=fileSize-realSize;
            if(fileSize<=0){
                outputStream.close();
            }
            randomAccessFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
