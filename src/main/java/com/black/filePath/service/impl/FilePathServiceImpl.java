package com.black.filePath.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.filePath.pojo.FilePath;
import com.black.filePath.mapper.FilePathMapper;
import com.black.filePath.service.FilePathService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class FilePathServiceImpl extends ServiceImpl<FilePathMapper, FilePath> implements FilePathService {
    @Autowired
    FilePathMapper filePathMapper;
    @Override
    public Page<FilePath> listByPage(Map<String, Object> map) {
        int page=1;
        if(map.get("page")!=null){
            page=(int)map.get("page");
        }
        Page<FilePath> pageParam = new Page<>();
        filePathMapper.selectPage(pageParam,null);
        return pageParam;
    }

    @Override
    public List<FilePath> showPack(Map<String, Object> map) {
        return filePathMapper.showPack(map);
    }
}
