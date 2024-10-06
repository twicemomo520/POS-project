package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pos10.entity.Member;
import com.example.pos10.repository.MemberDao;
import com.example.pos10.service.ifs.MemberService;
import com.example.pos10.vo.BasicRes;
import com.example.pos10.vo.CheckLoginRes;
import com.example.pos10.vo.ForgotPasswordReq;
import com.example.pos10.vo.ForgotPasswordRes;
import com.example.pos10.vo.LoginMemberReq;
import com.example.pos10.vo.LoginMemberRes;
import com.example.pos10.vo.MemberInfoRes;
import com.example.pos10.vo.RegisterMemberReq;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	@Autowired
	private EmailService emailService;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// 註冊會員
	@Override
	public BasicRes registerMember(RegisterMemberReq req) {

		// 1. 防呆
		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "註冊失敗：姓名格式不正確");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "註冊失敗：電話格式不正確");
		}

		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "註冊失敗：密碼格式不正確");
		}

		if (req.getEmail() == null || req.getEmail().trim().isEmpty()) {
			return new BasicRes(400, "註冊失敗：E-mail格式不正確");
		}

		// 2. 檢查會員是否已存在
		if (memberDao.phoneExists(req.getPhone()) > 0) {
			return new BasicRes(400, "註冊失敗：該電話號碼已被註冊");
		}

		// 3. 新會員資料
		String name = req.getName().trim();
		String phone = req.getPhone().trim();
		LocalDate birthday = req.getBirthday();
		String email = req.getEmail();
		// 預設0
		int totalSpendingAmount = 0;
		// 暫時寫1
		String memberLevel = "1";

		// 密碼加密
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		req.setPwd(encryptedPassword);

		String pwd = encryptedPassword;

		// 4. 新增會員
		try {
			int result = memberDao.insertMember(pwd, name, phone, birthday, email, totalSpendingAmount, memberLevel);
			if (result > 0) {
				return new BasicRes(200, "註冊成功");
			} else {
				return new BasicRes(500, "註冊失敗：無法註冊");
			}
		} catch (Exception e) {
			return new BasicRes(500, "註冊失敗：" + e.getMessage());
		}
	}

	// 登入會員
	@Override
	public BasicRes loginMember(LoginMemberReq req) {

		// 1.防呆
		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "登入失敗：電話格式不正確");
		}

		// 2. 檢查會員是否已存在
		if (memberDao.phoneExists(req.getPhone()) > 0) {

			// 3.確認密碼是否正確
			// CheckLoginRes checkLoginRes
			// result[0] = member_id
			// result[1] = pwd
			List<Object[]> results = memberDao.CheckLogin(req.getPhone());
			if (results.isEmpty()) {
				return new BasicRes(400, "登入失敗：此電話號碼沒有註冊過");
			}
			Object[] result = results.get(0); // 獲取第一行結果

			int memberId = ((Number) result[0]).intValue(); // 轉換 member_id
			String pwd = (String) result[1]; // 轉換密碼

			CheckLoginRes checkLoginRes = new CheckLoginRes(memberId, pwd);

			// 比對密碼
			if (passwordEncoder.matches(req.getPwd(), checkLoginRes.getPwd())) {
				// 登入成功
				return new LoginMemberRes(200, "登入成功", checkLoginRes.getMemberId());
			} else {
				// 密碼錯誤
				return new BasicRes(400, "登入失敗：密碼錯誤");
			}

		} else {
			return new BasicRes(400, "登入失敗：此電話號碼沒有註冊過");
		}

	}

	// 抓會員資料
	@Override
	public BasicRes getMemberInfo(int memberId) {

		Optional<Member> memberOpt = memberDao.findByMemberId(memberId);

		if (memberOpt.isEmpty()) {
			return new BasicRes(400, "查詢失敗：該會員ID不存在");
		}

		// 取得會員資料
		Member member = memberOpt.get();
		// 因為密碼也會回傳 所以設定他成不顯示
		member.setPwd("********");

		// 回傳會員詳細資訊
		return new MemberInfoRes(200, "查詢成功", member);
	}

	// 忘記密碼 (輸入手機之後會產生6位數驗證碼 驗證成功 可以修改密碼)
	@Override
	public BasicRes forgotPassword(ForgotPasswordReq req) {

		// 1.防呆
		if (req.getPhone() == null || req.getPhone().isEmpty()) {
			return new BasicRes(400, "驗證碼發送失敗：電話格式不正確");
		}
		
		if (req.getEmail() == null || req.getEmail().isEmpty()) {
			return new BasicRes(400, "驗證碼發送失敗：E-mail格式不正確");
		}

		// 2. 檢查會員是否已存在
		if (memberDao.phoneExists(req.getPhone()) > 0) {

			// 3. 產生驗證碼和時間
			Random random = new Random();
			int code = random.nextInt(999999) + 1; // 生成範圍是1到999999
			String verificationCode = String.format("%06d", code); // 確保補足6位，前面不足的補0

			// 5分鐘之後
			LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

			memberDao.updateVerificationCode(verificationCode, expiry, req.getPhone());

			// 4. 發送電子郵件

			// 確認手機和信箱是同一個人
			if (memberDao.checkEmail(req.getPhone(), req.getEmail()) > 0) {

				try {
					emailService.sendVerificationEmail(req.getEmail(), verificationCode);
				} catch (Exception e) {
					return new BasicRes(500, "驗證碼發送失敗：郵件發送失敗");
				}
			}else {
				return new BasicRes(400, "驗證碼發送失敗：查無該電話和E-mail對應的帳號");
			}

			return new ForgotPasswordRes(200, "驗證碼發送成功",verificationCode,expiry);
		} else {
			return new BasicRes(400, "驗證碼發送失敗：該電話號碼尚未註冊");
		}

	}

	@Override
	public BasicRes resetPassword(LoginMemberReq req) {

		// 1.防呆
		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "失敗：密碼格式不正確");
		}
		
		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "失敗：手機格式不正確");
		}

		if (memberDao.phoneExists(req.getPhone()) > 0) {
			
			// 密碼加密
			String encryptedPassword = passwordEncoder.encode(req.getPwd());
			String newPwd = encryptedPassword;
			memberDao.retsetPassword(newPwd, req.getPhone());

			return new BasicRes(200, "密碼修改成功");
			
		}else {
			return new BasicRes(400, "密碼修改失敗");
		}
		
		
	}

}
