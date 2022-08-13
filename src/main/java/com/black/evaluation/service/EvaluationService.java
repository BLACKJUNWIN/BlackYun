package com.black.evaluation.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.evaluation.pojo.Evaluation;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
public interface EvaluationService extends IService<Evaluation> {
    Page<Evaluation> listByPage(Map<String,Object> evaluation);
}
