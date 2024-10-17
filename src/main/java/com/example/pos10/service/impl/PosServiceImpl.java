package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.pos10.entity.Categories;
import com.example.pos10.vo.CreateOptionReq;
import com.example.pos10.vo.DeleteCgReq;
import com.example.pos10.vo.DeleteMenuReq;
import com.example.pos10.vo.DeleteOptionReq;
import com.example.pos10.vo.OptionContent;
import com.example.pos10.vo.UpdateCgReq;
import com.example.pos10.vo.UpdateMenuReq;
import com.example.pos10.vo.UpdateOptionPriceReq;
import com.example.pos10.vo.UpdateWorkstationReq;
import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.MenuItems;
import com.example.pos10.entity.Options;
import com.example.pos10.repository.CategoriesDao;
import com.example.pos10.repository.MenuItemsDao;
import com.example.pos10.repository.OptionsDao;
import com.example.pos10.repository.WorkstationDao;
import com.example.pos10.repository.OrdersHistoryDao;
import com.example.pos10.service.ifs.PosService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CreateCbReq;
import com.example.pos10.vo.CreateCgReq;
import com.example.pos10.vo.CreateReq;
import com.example.pos10.vo.JoinOrderHistoryVo;
import com.example.pos10.vo.PosStatisticsReq;
import com.example.pos10.vo.PosStatisticsRes;

@Service
public class PosServiceImpl implements PosService{
	
	@Autowired
	private OrdersHistoryDao orderDetailHistoryDao;
//	@Autowired
//	private OrderHistoryDao orderHistoryDao;
	
	@Autowired
	private MenuItemsDao menuItemsDao;

	@Autowired
	private CategoriesDao categoriesDao;

	@Autowired
	private OptionsDao optionsDao;
	
	@Autowired
	private WorkstationDao workstationDao;
	
	@Override
	public PosStatisticsRes statistics(PosStatisticsReq req) {
		
		
		LocalDate startDateInput = req.getStartDate();
		LocalDate endDateInput = req.getEndDate();
		

		if (endDateInput == null) {
			startDateInput = LocalDate.of(1900, 1, 1);
		}

		if (endDateInput == null) {
			endDateInput = LocalDate.of(2999, 1, 1);
		}
		

		LocalDateTime startDate = LocalDateTime.parse(startDateInput + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDate = LocalDateTime.parse(endDateInput + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		
		System.out.println(startDate);
		System.out.println(endDate);
		
		List<JoinOrderHistoryVo>searchRes = orderDetailHistoryDao.searchOrderDetailHistory(startDate, endDate);
		
		return new PosStatisticsRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), searchRes);
	}
	
	// 新增菜單
	@Transactional
	@Override
	public BasicRes create(CreateReq req) {
		List<MenuItems> menuItems = req.getMenuList();
		
		for(MenuItems item : menuItems) {
			String mealName = item.getMealName();
			int categoryId = item.getCategoryId();
			int workstationId = item.getWorkstationId();
			boolean available = item.isAvailable();
			String pictureName = item.getPictureName();
			
			// 若資料庫找到一樣的mealName數量大於0，表示mealName存在於資料庫
			if (menuItemsDao.existsByMealName(mealName) > 0) {
				// 返回菜單已存在的錯誤訊息
				return new BasicRes(ResMessage.MEAL_NAME_EXISTS.getCode(), ResMessage.MEAL_NAME_EXISTS.getMessage());
			}
			
			// 欲新增的categoryId必須已存在
			if (categoriesDao.optionExists(categoryId) == 0) {
				// 若不存在返回錯誤訊息
				return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(),
						ResMessage.CATEGORYID_NOT_FOUND.getMessage());
			}
			
			// price 不得小於0
			int price = item.getPrice();
			if (price < 0) {
				return new BasicRes(ResMessage.PRICE_CANNOT_BE_NEGATIVE.getCode(), //
						ResMessage.PRICE_CANNOT_BE_NEGATIVE.getMessage());
			}
			
			// workstationId必須已存在
//			if (workstationDao.countByWorkstationId(workstationId) == 0) {
//				return new BasicRes(ResMessage.WORKSTATION_ID_NOT_FOUND.getCode(), //
//						ResMessage.WORKSTATION_ID_NOT_FOUND.getMessage());
//			}
			
			// 存進資料庫
			menuItemsDao.insert(mealName, categoryId, workstationId, price, //
					available, pictureName);
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 新增菜單分類
	@Override
	public BasicRes createCategory(CreateCgReq req) {
		String categoryName = req.getCategory();

		// 新增餐點分類，新增的類別名稱不能已存在
		if (categoriesDao.cgNameExists(categoryName) > 0) {
			// 回傳錯誤訊息
			return new BasicRes(ResMessage.CATEGORY_ALREADY_EXISTS.getCode(), //
					ResMessage.CATEGORY_ALREADY_EXISTS.getMessage());
		}
		
		// 忽略空值
		if (categoryName == null || categoryName.trim().isEmpty()) {
			return new BasicRes(ResMessage.SUCCESS.getCode(), "已忽略空的欄位");
		}

		// 新增菜單分類
		categoriesDao.insert(categoryName);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 編輯菜單分類
	@Override
	public BasicRes updateCategory(UpdateCgReq req) {
		int categoryId = req.getCategoryId();
		String categoryName = req.getCategory();

		// 編輯餐點的id不能不存在
		if (categoriesDao.optionExists(categoryId) == 0) {
			return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(), //
					ResMessage.CATEGORYID_NOT_FOUND.getMessage());
		}

		// 輸入的名字不能存在於資料庫
		if (categoriesDao.cgNameExists(categoryName) > 0) {
			return new BasicRes(ResMessage.CATEGORY_ALREADY_EXISTS.getCode(), //
					ResMessage.CATEGORY_ALREADY_EXISTS.getMessage());
		}

		// 編輯菜單分類(update)
		categoriesDao.updateCg(categoryId, categoryName);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 刪除菜單分類
	@Transactional
	@Override
	public BasicRes deleteCategory(DeleteCgReq req) {
		// select categoryId，若不存在 count(1)等於零
		if (categoriesDao.optionExists(req.getCategoryId()) == 0) {
			// 回傳錯誤: 刪除的菜單不存在
			return new BasicRes(ResMessage.CATEGORY_NOT_FOUND.getCode(), //
					ResMessage.CATEGORY_NOT_FOUND.getMessage());
		}

		// 刪除菜單分類(delete)
		int categoryId = req.getCategoryId();
		categoriesDao.deleteCgById(categoryId);
		menuItemsDao.deleteMenuByCgId(categoryId);
		optionsDao.deleteOpByCgId(categoryId);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 編輯菜單
	@Transactional
	@Override
	public BasicRes updateMenu(UpdateMenuReq req) {

		// meal_name必須存在
		String mealName = req.getMealName();
		if (menuItemsDao.existsByMealName(mealName) == 0) {
			// 回傳錯誤訊息
			return new BasicRes(ResMessage.MEAL_NAME_NOT_FOUND.getCode(), //
					ResMessage.MEAL_NAME_NOT_FOUND.getMessage());
		}

		// 菜單分類Id必須存在
		int categoryId = req.getCategoryId();
		if (categoriesDao.optionExists(categoryId) == 0) {
			// 回傳錯誤訊息
			return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(), //
					ResMessage.CATEGORYID_NOT_FOUND.getMessage());
		}

		// workstation_id必須存在
		int workstationId = req.getWorkstationId();
		if(workstationDao.countByWorkstationId(workstationId) == 0) {
			return new BasicRes(ResMessage.WORKSTATION_ID_NOT_FOUND.getCode(), //
					ResMessage.WORKSTATION_ID_NOT_FOUND.getMessage());
		}

		// price不能小於0
		int price = req.getPrice();
		if (price < 0) {
			return new BasicRes(ResMessage.PRICE_CANNOT_BE_NEGATIVE.getCode(), //
					ResMessage.PRICE_CANNOT_BE_NEGATIVE.getMessage());
		}

		// 編輯SQL
		menuItemsDao.updateMenu(mealName, categoryId, workstationId, //
				price, req.isAvailable(), req.getPictureName());

		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 刪除菜單
	@Override
	public BasicRes deleteMenu(DeleteMenuReq req) {
		String mealName = req.getMealName();
		// 刪除的菜單名稱必須存在
		if (menuItemsDao.existsByMealName(mealName) == 0) {
			// 回傳錯誤訊息
			return new BasicRes(ResMessage.MEAL_NAME_NOT_FOUND.getCode(), //
					ResMessage.MEAL_NAME_NOT_FOUND.getMessage());
		}

		// 刪除菜單動作
		menuItemsDao.deleteMenuByMealName(mealName);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 編輯客製化選項價錢
	@Transactional
	@Override
	public BasicRes updateOption(List<UpdateOptionPriceReq> reqList) {
		for (UpdateOptionPriceReq item : reqList) {
			String optionTitle = item.getOptionTitle();
			int categoryId = item.getCategoryId();
			String optionType = item.getOptionType();
			List<OptionContent> optionContents = item.getOptionList();
			
			// categoryId、optionTitle 必須存在
			if (optionsDao.selectOption(optionTitle, categoryId) == 0) {
				return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(),
						ResMessage.CATEGORYID_NOT_FOUND.getMessage());
			}

	        // 遍歷送進來的 optionList
	        for (OptionContent conItem : optionContents) {
	            // 每個 extraPrice 必須大於零
	            if (conItem.getExtraPrice() < 0) {
	                return new BasicRes(ResMessage.EXTRA_PRICE_CANNOT_BE_NEGATIVE.getCode(),
	                        ResMessage.EXTRA_PRICE_CANNOT_BE_NEGATIVE.getMessage());
	            }
	            optionsDao.updateOptionPrice(optionTitle, categoryId, conItem.getOptionContent(), optionType, conItem.getExtraPrice());
	        }
		}
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 刪除客製化選項 (刪除optionTitle的所有資料)
	@Override
	public BasicRes deleteOption(DeleteOptionReq req) {

		String optionTitlte = req.getOptionTitle();
		int categoryId = req.getCategoryId();
		
		// option PK 組合 必須存在
		if(optionsDao.selectOption(optionTitlte, categoryId) == 0) {
			return new BasicRes(ResMessage.OPTION_COMBINATION_NOT_FOUND.getCode(), //
					ResMessage.OPTION_COMBINATION_NOT_FOUND.getMessage());
		}
		
		optionsDao.deleteOption(optionTitlte, categoryId);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	
	// 新增客製化選項
	@Transactional
	@Override
	public BasicRes createOption(List<CreateOptionReq> reqList) {
	    for (CreateOptionReq req : reqList) {
	        int categoryId = req.getCategoryId();
	        String optionTitle = req.getOptionTitle();
	        List<OptionContent> optionContents = req.getOptionList(); // 取得選項細節
	        String optionType = req.getOptionType();

	        // categoryId 必須存在
	        if (categoriesDao.optionExists(categoryId) == 0) {
	            return new BasicRes(ResMessage.CATEGORYID_NOT_FOUND.getCode(),
	                    ResMessage.CATEGORYID_NOT_FOUND.getMessage());
	        }

	        // 遍歷送進來的 optionList
	        for (OptionContent item : optionContents) {
	            // 每個 extraPrice 必須大於零
	            if (item.getExtraPrice() < 0) {
	                return new BasicRes(ResMessage.EXTRA_PRICE_CANNOT_BE_NEGATIVE.getCode(),
	                        ResMessage.EXTRA_PRICE_CANNOT_BE_NEGATIVE.getMessage());
	            }

	            // PK組合不能已存在
	            if (optionsDao.selectOptionThree(optionTitle, categoryId, item.getOptionContent()) > 0) {
	                return new BasicRes(ResMessage.OPTION_COMBINATION_ALREADY_EXISTS.getCode(),
	                        ResMessage.OPTION_COMBINATION_ALREADY_EXISTS.getMessage());
	            }

	            // 插入到資料庫
	            optionsDao.insert(optionTitle, categoryId, item.getOptionContent(), optionType, item.getExtraPrice());
	        }
	    }
		
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}

	// 回傳已存在的菜單分類
	@Override
	public List<Categories> selectCate() {
		return categoriesDao.selectAll();
	}

	// 回傳已存在的菜單
	@Override
	public List<MenuItems> selectMenu() {
		return menuItemsDao.selectAll();
	}

	// 回傳已存在的客製化選項
	@Override
	public List<Options> selectCust() {
		return optionsDao.selectAll();
	}

	@Transactional
	@Override
	public BasicRes updateMenuWorkStation(UpdateWorkstationReq req) {
		int categoryId = req.getCategoryId();
		int newWorkId = req.getWorkstationId();
		
		 // 記錄 categoryId 和 newWorkId 到日志
	    System.out.println("接收到的分類 ID: " + categoryId);
	    System.out.println("接收到的工作檯 ID: " + newWorkId);
		if (workstationDao.countByWorkstationId(newWorkId) == 0) {
			return new BasicRes(ResMessage.WORKSTATION_ID_NOT_FOUND.getCode(), //
					ResMessage.WORKSTATION_ID_NOT_FOUND.getMessage());
		}
		
		menuItemsDao.updateMenuWorkStation(newWorkId, categoryId);
		categoriesDao.updateWorkIdFromCg(categoryId, newWorkId);
		return new BasicRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
	}
	
		
	
	
	
	
	
	
	
	

}
