package com.example.pos10.service.impl;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pos10.repository.StaffDao;
import com.example.pos10.service.ifs.StaffService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.RegisterStaffReq;
import com.example.pos10.vo.UpdateStaffReq;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffDao staffDao;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private String generateStaffNumber() {
		// 查詢目前最高的員工編號 要抓數字往下+
		String lastStaffNumber = staffDao.findLastStaffNumber();
		if (lastStaffNumber == null) {
			// 如果沒有員工，從 "TEST001" 開始 之後可以+設定檔讓公司設定
			return "TEST001";
		}

		// 取得數字部分，遞增1
		String numberPart = lastStaffNumber.substring(lastStaffNumber.length() - 3);
		int num = Integer.parseInt(numberPart) + 1;

		// 格式化為三位數，並加上 "TEST" 目前寫死 因為我們沒有設定檔
		return String.format("TEST%03d", num);
	}

	// 新增員工
	@Override
	public BasicRes registerStaff(RegisterStaffReq req) {

		// 1. 防呆
		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：密碼格式不正確");
		}

		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：姓名格式不正確");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：電話格式不正確");
		}

		// 3. 新會員資料

		// 生成員工編號
		// 透過上面的方法 產生指定的員工編號
		String staffNumber = generateStaffNumber();

		String name = req.getName().trim();
		String phone = req.getPhone().trim();

		// 預設0 最小權限
		String authorization = "0";

		// 密碼加密
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		String pwd = encryptedPassword;

		// 4. 新增會員
		try {
			int result = staffDao.insertStaff(staffNumber, pwd, name, phone, authorization);
			if (result > 0) {
				return new BasicRes(200, "註冊成功");
			} else {
				return new BasicRes(500, "註冊失敗：無法註冊");
			}
		} catch (Exception e) {
			return new BasicRes(500, "註冊失敗：" + e.getMessage());
		}

	}

	// 更新員工資料
	@Override
	public BasicRes updateStaff(UpdateStaffReq req) {

		// 1. 防呆

		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "修改員工資料失敗：姓名格式不正確");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "修改員工資料失敗：電話格式不正確");
		}
		
		if (req.getAuthorization() == null || req.getAuthorization().trim().isEmpty()) {
            return new BasicRes(400, "更新失敗：權限不正確");
        }
		
		//2.執行sql
		int result = staffDao.updateStaff( req.getName(), req.getPhone(), req.getAuthorization(),req.getStaffNumber());
        if (result > 0) {
            return new BasicRes(200, "更新成功");
        } else {
            return new BasicRes(500, "更新失敗：未找到對應的員工");
        }
		

	}

	
	
}
