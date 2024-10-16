package com.example.pos10.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pos10.entity.Staff;
import com.example.pos10.repository.StaffDao;
import com.example.pos10.service.ifs.StaffService;
import com.example.pos10.vo.AllStaffInfoRes;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.ForgotPasswordRes;
import com.example.pos10.vo.LoginStaffReq;
import com.example.pos10.vo.LoginStaffRes;
import com.example.pos10.vo.RegisterStaffReq;
import com.example.pos10.vo.StaffForgotPasswordReq;
import com.example.pos10.vo.StaffInfoRes;
import com.example.pos10.vo.UpdateStaffReq;

@Service
public class StaffServiceImpl implements StaffService {

	@Autowired
	private StaffDao staffDao;

	@Autowired
	private EmailService emailService;

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

		if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：E-mail不正確");
		}

		// 3. 新會員資料

		// 生成員工編號
		// 透過上面的方法 產生指定的員工編號
		String staffNumber = generateStaffNumber();

		String name = req.getName().trim();
		String phone = req.getPhone().trim();

		// 預設0 最小權限
		String authorization = req.getAuthorization();
		if (authorization == null || authorization.trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：授權設定不正確");
		}

		String email = req.getEmail();

		// 密碼加密
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		String pwd = encryptedPassword;

		// 4. 新增會員
		try {
			int result = staffDao.insertStaff(staffNumber, pwd, name, phone, authorization, email);
			if (result > 0) {
				return new BasicRes(200, "新增員工成功");
			} else {
				return new BasicRes(500, "新增員工失敗：無法註冊");
			}
		} catch (Exception e) {
			return new BasicRes(500, "新增員工失敗：" + e.getMessage());
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

		if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
			return new BasicRes(400, "新增員工失敗：E-mail不正確");
		}

		// 2.執行sql
		int result = staffDao.updateStaff(req.getName(), req.getPhone(), req.getAuthorization(), req.getEmail(),
				req.getStaffNumber());
		if (result > 0) {
			return new BasicRes(200, "更新成功");
		} else {
			return new BasicRes(500, "更新失敗：未找到對應的員工");
		}

	}

	// 回傳所有員工資料
	@Override
	public AllStaffInfoRes searchAllstaff() {

		try {
			// 從資料庫中獲取所有員工
			List<Staff> staffData = staffDao.allStaffInfo();
			return new AllStaffInfoRes(200, "搜尋成功", staffData);
		} catch (Exception e) {
			// 在出錯時返回失敗消息
			return new AllStaffInfoRes(500, "搜尋失敗：" + e.getMessage(), null);
		}
	}

	// 刪除員工
	@Override
	public BasicRes deleteStaff(String staffNumber) {

		if (staffNumber == null || staffNumber.trim().isEmpty()) {
			return new BasicRes(400, "刪除員工失敗：員工編號格式不正確");
		}

		try {
			int result = staffDao.deleteStaff(staffNumber);
			if (result > 0) {
				return new BasicRes(200, "刪除員工成功");
			} else {
				return new BasicRes(400, "刪除員工失敗：未找到對應的員工");
			}
		} catch (Exception e) {
			return new BasicRes(500, "刪除員工失敗：" + e.getMessage());
		}
	}

	// 員工登入
	@Override
	public BasicRes loginStaff(LoginStaffReq req) {

		// 1. 防呆
		if (req.getStaffNumber() == null || req.getStaffNumber().trim().isEmpty()) {
			return new BasicRes(400, "登入失敗：帳號格式不正確");
		}

		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "登入失敗：密碼格式不正確");
		}

		// 2. 檢查員工是否已存在
		if (staffDao.staffNumberExists(req.getStaffNumber()) > 0) {

			Optional<Staff> staffOptional = staffDao.findById(req.getStaffNumber());

			Staff staffData = staffOptional.get();

			// 錯誤次數
			Integer errorCount = staffData.getErrorCount();
			LocalDateTime blockTime = staffData.getBlockTime();

			// 檢查封鎖時間
			if (blockTime != null && blockTime.plusMinutes(15).isAfter(LocalDateTime.now())) {
				return new BasicRes(400, "登入失敗：帳號已被鎖定，請稍後再試");
			}

			// 3. 確認密碼是否正確
			String pwd = staffDao.CheckLogin(req.getStaffNumber());

			if (pwd == null || pwd.isEmpty()) {
				return new BasicRes(400, "登入失敗：沒有此員工編號");
			}

			// 4. 比對密碼
			if (passwordEncoder.matches(req.getPwd(), pwd)) {

				// 重置錯誤次數與封鎖時間
				staffDao.updateErrorCount(0, req.getStaffNumber());
				staffDao.updateBlockTime(null, req.getStaffNumber());

				// 登入成功
				return new LoginStaffRes(200, "登入成功", req.getStaffNumber());
			} else {
				// 更新錯誤次數
				errorCount++;
				staffDao.updateErrorCount(errorCount, req.getStaffNumber());

				// 當錯誤次數達到 5 次時，更新封鎖時間
				if (errorCount >= 5) {
					staffDao.updateBlockTime(LocalDateTime.now(), req.getStaffNumber());
					return new BasicRes(400, "登入失敗：帳號已被鎖定，請稍後再試");
				}

				// 密碼錯誤
				return new BasicRes(400, "登入失敗：密碼錯誤");
			}

		} else {
			return new BasicRes(400, "登入失敗：此員工編號尚未註冊");
		}
	}

	// 抓員工資料
	@Override
	public BasicRes getStaffInfo(String staffNumber) {

		Optional<Staff> staffOpt = staffDao.findByStaffNumber(staffNumber);

		if (staffOpt.isEmpty()) {
			return new BasicRes(400, "查詢失敗：該員工編號不存在");
		}

		// 取得員工資料
		Staff staff = staffOpt.get();
		// 因為密碼也會回傳 所以設定他成不顯示
		staff.setPwd("********");

		// 回傳會員詳細資訊
		return new StaffInfoRes(200, "查詢成功", staff);

	}

	// 更新密碼
	@Override
	public BasicRes resetPassword(LoginStaffReq req) {

		// 1.防呆
		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "失敗：密碼格式不正確");
		}

		if (staffDao.staffNumberExists(req.getStaffNumber()) > 0) {

			// 密碼加密
			String encryptedPassword = passwordEncoder.encode(req.getPwd());
			String newPwd = encryptedPassword;
			staffDao.resetPassword(newPwd, req.getStaffNumber());

			staffDao.updateErrorCount(0, req.getStaffNumber());
			staffDao.updateBlockTime(null, req.getStaffNumber());

			return new BasicRes(200, "密碼修改成功");

		} else {
			return new BasicRes(400, "密碼修改失敗");
		}

	}

	// 忘記密碼
	@Override
	public BasicRes forgotPassword(StaffForgotPasswordReq req) {

		// 1.防呆
		if (req.getStaffNumber() == null || req.getStaffNumber().isEmpty()) {
			return new BasicRes(400, "驗證碼發送失敗：帳號格式不正確");
		}

		if (req.getEmail() == null || req.getEmail().isEmpty()) {
			return new BasicRes(400, "驗證碼發送失敗：E-mail格式不正確");
		}

		// 2. 檢查員工是否已存在

		if (staffDao.staffNumberExists(req.getStaffNumber()) > 0) {

			// 確認員工編號和信箱是同一個人
			if (staffDao.checkEmail(req.getStaffNumber(), req.getEmail()) > 0) {

				// 3. 產生驗證碼和時間
				Random random = new Random();
				int code = random.nextInt(999999) + 1; // 生成範圍是1到999999
				String verificationCode = String.format("%06d", code); // 確保補足6位，前面不足的補0

				// 30秒之後
				LocalDateTime expiry = LocalDateTime.now().plusSeconds(30);

				staffDao.updateVerificationCode(verificationCode, expiry, req.getStaffNumber());

				try {
					// 發送電子郵件
					emailService.sendVerificationEmail(req.getEmail(), verificationCode);

					return new ForgotPasswordRes(200, "驗證碼發送成功", verificationCode, expiry);
				} catch (Exception e) {
					return new BasicRes(500, "驗證碼發送失敗：郵件發送失敗");
				}
			} else {
				return new BasicRes(400, "驗證碼發送失敗：查無該員工編號和E-mail對應的帳號");
			}

		} else {
			return new BasicRes(400, "驗證碼發送失敗：查無此帳號");
		}

	}

	
	
	@Override
	public BasicRes updateFirstLogin(String staffNumber) {
		
		staffDao.updateFirstLogin(staffNumber);
		
		return new BasicRes(200,"更新成功");
	}

}
