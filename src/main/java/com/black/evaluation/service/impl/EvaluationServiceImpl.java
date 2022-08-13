package com.black.evaluation.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.common.utils.commonUtils;
import com.black.evaluation.pojo.Evaluation;
import com.black.evaluation.mapper.EvaluationMapper;
import com.black.evaluation.service.EvaluationService;
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
public class EvaluationServiceImpl extends ServiceImpl<EvaluationMapper, Evaluation> implements EvaluationService {
    @Autowired
    EvaluationMapper evaluationMapper;

    @Override
    public Page<Evaluation> listByPage(Map<String,Object> evaluation) {
        int page=1;
        if(!commonUtils.isNull(evaluation.get("page"))){
            page= (int) evaluation.get("page");
        }
        Page<Evaluation> pageParam = new Page<>(page,10);
        return evaluationMapper.selectByPage(pageParam,evaluation);
    }
}
