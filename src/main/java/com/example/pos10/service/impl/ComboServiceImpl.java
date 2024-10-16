package com.example.pos10.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.ComboItems;
import com.example.pos10.repository.ComboDao;
import com.example.pos10.service.ifs.ComboService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.DeleteCbReq;
import com.example.pos10.vo.SearchCbReq;
import com.example.pos10.vo.SearchCbRes;
import com.example.pos10.vo.UpdateCbReq;



@Service
public class ComboServiceImpl implements ComboService{
	
	@Autowired 
	private ComboDao comboDao;
	

	@Override
	public BasicRes createCombo(CreateCbReq req) {
		String comboName =  req.getComboName();
		String comboDetail = req.getComboDetail();
		int discountAmount = req.getDiscountAmount();
		int categoryId = req.getCategoryId();
		System.out.println(comboDetail);
		
		long countByComboName = comboDao.countByComboName(comboName);
		if (countByComboName>0) {
			return new BasicRes(ResMessage.COMBO_NAME_EXISTS.getCode(), ResMessage.COMBO_NAME_EXISTS.getMessage());
		}
		
		comboDao.createCombo(comboName, comboDetail, discountAmount, categoryId);
		
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	
	
	@Override
	public BasicRes updateCombo(UpdateCbReq req) {
		String oldComboName = req.getOldComboName();
		String comboName =  req.getComboName();
		String comboDetail = req.getComboDetail();
		int discountAmount = req.getDiscountAmount();
//		int categoryId = req.getCategoryId();
		
		if (StringUtils.isEmpty(oldComboName) || StringUtils.isEmpty(comboName) || 
				StringUtils.isEmpty(comboDetail) || StringUtils.isEmpty(discountAmount)) {
			return new BasicRes(ResMessage.COMBO_INPUT_CANNOT_BE_NULL_OR_EMPTY.getCode(), 
					ResMessage.COMBO_INPUT_CANNOT_BE_NULL_OR_EMPTY.getMessage());
		}
		
		long countByComboName = comboDao.countByComboName(oldComboName);
		if (countByComboName<1) {
			return new BasicRes(ResMessage.COMBO_NAME_NOT_FOUND.getCode(), ResMessage.COMBO_NAME_NOT_FOUND.getMessage());
		}
		
		comboDao.updateCombo(oldComboName, comboName, comboDetail, discountAmount);
		
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	
	@Override
	public BasicRes deleteCombo(DeleteCbReq req) {
		
		String comboName = req.getComboName();
		
		long existsByComboName = comboDao.countByComboName(comboName);
		if (existsByComboName<1) {
			return new BasicRes(ResMessage.COMBO_NAME_NOT_FOUND.getCode(), ResMessage.COMBO_NAME_NOT_FOUND.getMessage());
		}
		
		comboDao.deleteCombo(comboName);
		
		return  new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	
	@Override
	public SearchCbRes searchCombo(SearchCbReq req) {
		String comboName = req.getComboName();
		List<ComboItems> comboList = comboDao.searchCombo(comboName);
		
		return new SearchCbRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), comboList);
	}



	@Override
	public int countData() {
		return comboDao.countData();
	}

}
