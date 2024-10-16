package com.example.pos10.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.pos10.entity.Authorization;
import com.example.pos10.repository.AuthorizationDao;
import com.example.pos10.repository.StaffDao;
import com.example.pos10.service.ifs.AuthorizationService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.InsertAuthorizationReq;
import com.example.pos10.vo.UpdateAuthorizationReq;

@Service
public class AuthorizationImpl implements AuthorizationService {

	@Autowired
	private AuthorizationDao authorizationDao;
	
	@Autowired
	private StaffDao staffDao;

	@Override
	public BasicRes insertAuthorization(InsertAuthorizationReq req) {

		// 1. 防呆
		if (req.getAuthorizationName() == null || req.getAuthorizationName().trim().isEmpty()) {
			return new BasicRes(400, "新增權限失敗：權限格式不正確");
		}

		if (req.getAuthorizationItem() == null || req.getAuthorizationItem().trim().isEmpty()) {
			return new BasicRes(400, "新增權限失敗：管理範圍格式不正確");
		}

		// 2. 檢查是否已存在相同名稱的權限
		int count = authorizationDao.countByAuthorizationName(req.getAuthorizationName());
		if (count > 0) {
			return new BasicRes(400, "新增權限失敗：該權限名稱已存在");
		}

		// 3. 新增
		int result = authorizationDao.insertAuthorization(req.getAuthorizationName(), req.getAuthorizationItem());
		if (result > 0) {
			return new BasicRes(200, "新增權限成功");
		} else {
			return new BasicRes(500, "新增權限失敗：無法新增");
		}
	}

	@Override
	public List<Authorization> findAllAuthorizations() {
		return authorizationDao.findAll();
	}

	@Override
	public BasicRes updateAuthorization(UpdateAuthorizationReq req) {
		// 1. 防呆
		if (req.getAuthorizationName() == null || req.getAuthorizationName().trim().isEmpty()) {
			return new BasicRes(400, "更新權限失敗：權限格式不正確");
		}

		if (req.getAuthorizationItem() == null || req.getAuthorizationItem().trim().isEmpty()) {
			return new BasicRes(400, "更新權限失敗：管理範圍格式不正確");
		}
		
		//2.確認名稱是否已存在
		int count = authorizationDao.countByAuthorizationNameAndIdNot(req.getAuthorizationName(),req.getAuthorizationId());
		if (count > 0) {
			return new BasicRes(400, "更新權限失敗：該權限名稱已存在");
		}

		// 3. 更新
		int result = authorizationDao.updateAuthorization(req.getAuthorizationId(), req.getAuthorizationName(),
				req.getAuthorizationItem());
		if (result > 0) {
			return new BasicRes(200, "更新權限成功");
		} else {
			return new BasicRes(500, "更新權限失敗：無法更新");
		}
	}

	@Override
	public BasicRes deleteAuthorization(int id) {
		
		// 1. 刪除
		
		int count = staffDao.countAuthorization(id);
		if (count > 0) {
			return new BasicRes(400, "刪除權限失敗：該權限有使用者");
		}
		
		int result = authorizationDao.deleteAuthorization(id);
		if (result > 0) {
			return new BasicRes(200, "刪除權限成功");
		} else {
			return new BasicRes(500, "刪除權限失敗：無法刪除");
		}
	}

}
