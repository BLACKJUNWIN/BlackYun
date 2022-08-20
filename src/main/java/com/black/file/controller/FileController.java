package com.black.file.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.black.common.pojo.responseJson;
import com.black.common.utils.fileUtils;
import com.black.common.utils.tokenUtils;
import com.black.filePath.pojo.FilePath;
import com.black.filePath.service.FilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import com.black.file.pojo.File;
import com.black.file.service.FileService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	FileService fileService;

	@Autowired
	FilePathService filePathService;

	@PostMapping("/")
	public responseJson add(@RequestBody File file, HttpServletRequest request) {
		//保存文件
		fileService.save(file);
		//保存文件路径
		FilePath filePath = new FilePath();
		filePath.setUserId(tokenUtils.requestGetId(request));
		filePath.setFileId(fileService.getOne(new QueryWrapper<File>().eq("md5",file.getMd5())).getId());
		filePath.setLevel(file.getLevel());
		filePathService.save(filePath);
	return new responseJson(fileService.listByPage(new HashMap<>()));
	}

	@DeleteMapping("/{id}")
	public responseJson delCategory(@PathVariable Long id){
	fileService.removeById(id);
		return new responseJson(fileService.listByPage(new HashMap<>()));
	}

	@PutMapping("/")
	public responseJson upd(@RequestBody File file) {
	fileService.updateById(file);
		return new responseJson(fileService.listByPage(new HashMap<>()));
	}

	@PatchMapping("/")
	public responseJson select(@RequestBody Map<String,Object> map){
		return new responseJson(fileService.listByPage(map));
	}

	@PostMapping("/uploadImage")
	public responseJson uploadImage(@RequestParam MultipartFile file,@RequestParam String category){
		return fileUtils.uploadFile(file, category);

	}
}
