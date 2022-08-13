package com.black.evaluation.controller;
import com.black.common.pojo.responseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.black.evaluation.pojo.Evaluation;
import com.black.evaluation.service.EvaluationService;
@RestController
@RequestMapping("/evaluation")
public class EvaluationController {

	@Autowired
	EvaluationService evaluationService;

	@PostMapping("/")
	public responseJson add(@RequestBody Evaluation evaluation) {
	evaluationService.save(evaluation);
	return new responseJson(evaluationService.listByPage(new HashMap<>()));
	}

	@DeleteMapping("/{id}")
	public responseJson delCategory(@PathVariable Long id){
	evaluationService.removeById(id);
	return new responseJson(evaluationService.listByPage(new HashMap<>()));
	}

	@PutMapping("/")
	public responseJson upd(@RequestBody Evaluation evaluation) {
	evaluationService.updateById(evaluation);
		return new responseJson(evaluationService.listByPage(new HashMap<>()));
	}

	@PatchMapping("/")
	public responseJson select(@RequestBody Map<String,Object> evaluation){
		System.out.println(evaluation.toString());
		return new responseJson(evaluationService.listByPage(evaluation));
	}
}
