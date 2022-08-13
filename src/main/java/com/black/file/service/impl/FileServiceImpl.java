package com.black.file.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.file.pojo.File;
import com.black.file.mapper.FileMapper;
import com.black.file.service.FileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {
    @Autowired
    FileMapper fileMapper;

    @Override
    public Page<File> listByPage(Map<String, Object> map) {
        int page=1;
        if(map.get("page")!=null){
            page= (int) map.get("page");
        }
        Page<File> pageParam = new Page<>(page,10);
        fileMapper.selectPage(pageParam,null);
        return pageParam;
    }
}
