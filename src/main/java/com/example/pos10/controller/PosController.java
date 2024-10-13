 package com.example.pos10.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.pos10.entity.Categories;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.vo.CreateOptionReq;
import com.example.pos10.vo.DeleteCgReq;
import com.example.pos10.vo.DeleteMenuReq;
import com.example.pos10.vo.DeleteOptionReq;
import com.example.pos10.vo.UpdateCgReq;
import com.example.pos10.vo.UpdateMenuReq;
import com.example.pos10.vo.UpdateOptionPriceReq;
import com.example.pos10.vo.UpdateWorkstationReq;
import com.example.pos10.service.ifs.PosService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;


@RestController
@CrossOrigin
public class PosController {
	
	@Autowired
	private PosService posService ;
	
	@PostMapping(value = "pos/statistics")
	public PosStatisticsRes statistics(@Valid @RequestBody  PosStatisticsReq req) {
		return posService.statistics(req);
	}
	
	@GetMapping(value = "menu/all")
	public List<MenuItems> selectMenu(){
		return posService.selectMenu();
	}
	
	@PostMapping(value = "menu/create")
	public BasicRes create(@Valid @RequestBody CreateReq req) {
		return posService.create(req);
	}

	@PostMapping(value = "menu/update")
	public BasicRes updateMenu(@Valid @RequestBody UpdateMenuReq req) {
		return posService.updateMenu(req);
	}
	
	@PostMapping(value = "menu/updateWorkId")
	public BasicRes updateMenuWorkStation(@Valid @RequestBody UpdateWorkstationReq req) {
		return posService.updateMenuWorkStation(req);
	}
	
	@PostMapping(value = "menu/delete")
	public BasicRes deleteMenu(@Valid @RequestBody DeleteMenuReq req) {
		return posService.deleteMenu(req);
	}
	
	@GetMapping(value = "category/all")
	public List<Categories> selectCate(){
		return posService.selectCate();
	}

	@PostMapping(value = "category/create")
	public BasicRes createCategory(@Valid @RequestBody CreateCgReq req) {
		return posService.createCategory(req);
	}
	
	@PostMapping(value = "category/update")
	public BasicRes updateCategory(@Valid @RequestBody UpdateCgReq req) {
		return posService.updateCategory(req);
	}
	
	@PostMapping(value = "category/delete")
	public BasicRes deleteCategory(@Valid @RequestBody DeleteCgReq req) {
		return posService.deleteCategory(req);
	}
	
	@GetMapping(value = "option/all")
	public List<Options> selectCust(){
		return posService.selectCust();
	}
	
	@PostMapping(value = "option/create")
	public BasicRes createOption(@Valid @RequestBody List<CreateOptionReq> reqList) {
		return posService.createOption(reqList);
	}
	
	@PostMapping(value = "option/update")
	public BasicRes updateOption(@Valid @RequestBody List<UpdateOptionPriceReq> reqList) {
		return posService.updateOption(reqList);
	}
	
	@PostMapping(value = "option/delete")
	public BasicRes deleteOption(@Valid @RequestBody DeleteOptionReq req) {
		return posService.deleteOption(req);
	}
	
}
