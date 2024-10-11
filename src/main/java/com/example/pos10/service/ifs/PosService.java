package com.example.pos10.service.ifs;

import java.util.List;

import com.example.pos10.entity.Categories;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateOptionReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.DeleteCgReq;
import com.example.pos10.vo.DeleteMenuReq;
import com.example.pos10.vo.DeleteOptionReq;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;
import com.example.pos10.vo.UpdateCgReq;
import com.example.pos10.vo.UpdateMenuReq;
import com.example.pos10.vo.UpdateOptionPriceReq;
import com.example.pos10.vo.UpdateWorkstationReq;

public interface PosService {

	public PosStatisticsRes statistics(PosStatisticsReq req);

	public List<MenuItems> selectMenu();

	public BasicRes create(CreateReq req);

	public BasicRes updateMenu(UpdateMenuReq req);
	
	public BasicRes updateMenuWorkStation(UpdateWorkstationReq req);

	public BasicRes deleteMenu(DeleteMenuReq req);

	public List<Categories> selectCate();

	public BasicRes createCategory(CreateCgReq req);

	public BasicRes updateCategory(UpdateCgReq req);

	public BasicRes deleteCategory(DeleteCgReq req);

	public List<Options> selectCust();

	public BasicRes createOption(List<CreateOptionReq> reqList);

	public BasicRes updateOption(List<UpdateOptionPriceReq> reqList);

	public BasicRes deleteOption(DeleteOptionReq req);

}
