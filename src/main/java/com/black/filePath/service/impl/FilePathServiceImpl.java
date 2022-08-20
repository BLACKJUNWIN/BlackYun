package com.black.filePath.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.common.utils.tokenUtils;
import com.black.file.pojo.File;
import com.black.filePath.pojo.FilePath;
import com.black.filePath.mapper.FilePathMapper;
import com.black.filePath.service.FilePathService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;

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

    @Override
    public boolean saveFilePath(File file, Long fileId,Long userId) {
        try {
            FilePath filePath = new FilePath();
            filePath.setFileId(fileId);
            filePath.setUserId(userId);
            filePath.setLevel(file.getPath());
            int index = file.getName().lastIndexOf(".");
            String fileName="";
            if(index!=-1){
                fileName = file.getName().substring(0,index);
            }else{
                fileName=file.getName();
            }
            if(filePathMapper.selectList(new QueryWrapper<FilePath>().eq("name",fileName)).size()>=1){
                filePath.setName("r"+new Random().nextInt(10)+"*"+fileName);
            }else{
                filePath.setName(fileName);
            }
            filePathMapper.insert(filePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
