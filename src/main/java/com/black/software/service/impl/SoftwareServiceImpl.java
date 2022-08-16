package com.black.software.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.common.utils.commonUtils;
import com.black.software.pojo.Software;
import com.black.software.mapper.SoftwareMapper;
import com.black.software.service.SoftwareService;
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
public class SoftwareServiceImpl extends ServiceImpl<SoftwareMapper, Software> implements SoftwareService {
    @Autowired
    SoftwareMapper softwareMapper;
    @Override
    public Page<Software> listByPage(Software software) {
        int page=1;
        if(commonUtils.isNull(software.getPage())){
            page= software.getPage();
        }
        Page<Software> pageParam = new Page<>(page,10);
        return softwareMapper.selectByPage(pageParam,software);
    }

    @Override
    public List<Software> selectByCategory(Map<String, Object> map) {
        return softwareMapper.selectByCategory(map);
    }
}
