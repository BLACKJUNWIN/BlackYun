package com.black.common.pojo;

import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class UnitUploader implements Runnable{
    private int from;
    private int to;
    private File file;
    private InputStream inputStream;
    private int id;
    public static long fileSize;

    public UnitUploader(int from, int to, File file, InputStream inputStream, int id) {
        this.from = from;
        this.to = to;
        this.file = file;
        this.inputStream = inputStream;
        this.id = id;
        if(fileSize==0){
            fileSize=file.length();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        byte[] buffer = new byte[2];
        int readCount=0;
        //读取文件
        readCount = this.inputStream.read(buffer, 0, (to - from));
        //断点写入
        RandomAccessFile randomAccessFile=new RandomAccessFile(this.file,"rw");
        randomAccessFile.seek(from);
        randomAccessFile.write(buffer,0,readCount);
        fileSize-=(to-from);
    }
}
