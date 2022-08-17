package com.black.common.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

public class commonUtils {
    /**
     * 获得文件编码
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String codeString(String fileName) throws Exception {
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
        int p = (bin.read() << 8) + bin.read();
        bin.close();
        String code = null;

        switch (p) {
            case 0xefbb:
                code = "UTF-8";
                break;
            case 0xfffe:
                code = "Unicode";
                break;
            case 0xfeff:
                code = "UTF-16BE";
                break;
            default:
                code = "GBK";
        }
        return code;
    }


    public static boolean isNull(String text){
        return text == null || text.isEmpty();
    }
    public static boolean isNull(Long text){
        return text == null || text==0L;
    }
    public static boolean isNull(Object text){
        return text == null || text.toString().isEmpty();
    }
}
