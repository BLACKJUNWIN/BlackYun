package com.black.evaluation.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.black.evaluation.pojo.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author BlackJun
 * @since 2022-06-08
 */
@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluation> {
    Page<Evaluation> selectByPage(Page<Evaluation> page, Map<String, Object> map);
}
