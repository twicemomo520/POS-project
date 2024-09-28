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
import com.example.pos10.vo.LoginMemberReq;
import com.example.pos10.vo.LoginMemberRes;
import com.example.pos10.vo.MemberInfoRes;
import com.example.pos10.vo.RegisterMemberReq;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberDao memberDao;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	// ���U�|��
	@Override
	public BasicRes registerMember(RegisterMemberReq req) {

		// 1. ���b
		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "���U���ѡG�m�W�榡�����T");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "���U���ѡG�q�ܮ榡�����T");
		}

		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "���U���ѡG�K�X�榡�����T");
		}

		// 2. �ˬd�|���O�_�w�s�b
		if (memberDao.phoneExists(req.getPhone()) > 0) {
			return new BasicRes(400, "���U���ѡG�ӹq�ܸ��X�w�Q���U");
		}

		// 3. �s�|�����
		String name = req.getName().trim();
		String phone = req.getPhone().trim();
		LocalDate birthday = req.getBirthday();
		// �w�]0
		int totalSpendingAmount = 0;
		// �Ȯɼg1
		String memberLevel = "1";

		// �K�X�[�K
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		req.setPwd(encryptedPassword);

		String pwd = encryptedPassword;

		// 4. �s�W�|��
		try {
			int result = memberDao.insertMember(pwd, name, phone, birthday, totalSpendingAmount, memberLevel);
			if (result > 0) {
				return new BasicRes(200, "���U���\");
			} else {
				return new BasicRes(500, "���U���ѡG�L�k���U");
			}
		} catch (Exception e) {
			return new BasicRes(500, "���U���ѡG" + e.getMessage());
		}
	}

	// �n�J�|��
	@Override
	public BasicRes loginMember(LoginMemberReq req) {

		// 1.���b
		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "�n�J���ѡG�q�ܮ榡�����T");
		}

		// 2. �ˬd�|���O�_�w�s�b
		if (memberDao.phoneExists(req.getPhone()) > 0) {

			// 3.�T�{�K�X�O�_���T
			// CheckLoginRes checkLoginRes
			// result[0] = member_id
			// result[1] = pwd
			List<Object[]> results = memberDao.CheckLogin(req.getPhone());
			if (results.isEmpty()) {
				return new BasicRes(400, "�n�J���ѡG���q�ܸ��X�S�����U�L");
			}
			Object[] result = results.get(0); // ����Ĥ@�浲�G

			int memberId = ((Number) result[0]).intValue(); // �ഫ member_id
			String pwd = (String) result[1]; // �ഫ�K�X

			CheckLoginRes checkLoginRes = new CheckLoginRes(memberId, pwd);

			// ���K�X
			if (passwordEncoder.matches(req.getPwd(), checkLoginRes.getPwd())) {
				// �n�J���\
				return new LoginMemberRes(200, "�n�J���\", checkLoginRes.getMemberId());
			} else {
				// �K�X���~
				return new BasicRes(400, "�n�J���ѡG�K�X���~");
			}

		} else {
			return new BasicRes(400, "�n�J���ѡG���q�ܸ��X�S�����U�L");
		}

	}

	// ��|�����
	@Override
	public BasicRes getMemberInfo(int memberId) {

		Optional<Member> memberOpt = memberDao.findByMemberId(memberId);

		if (memberOpt.isEmpty()) {
			return new BasicRes(400, "�d�ߥ��ѡG�ӷ|��ID���s�b");
		}

		// ���o�|�����
		Member member = memberOpt.get();
		// �]���K�X�]�|�^�� �ҥH�]�w�L�������(�]���i�o�Ф@�ӷs���S���K�X��class)
		member.setPwd("********");

		// �^�Ƿ|���ԲӸ�T
		return new MemberInfoRes(200, "�d�ߦ��\", member);
	}

	// �ѰO�K�X (��J�������|����6������ҽX ���Ҧ��\ �i�H�ק�K�X)
	@Override
	public BasicRes forgotPassword(String phone) {

		// 1.���b
		if (phone == null || phone.trim().isEmpty()) {
			return new BasicRes(400, "���ҽX�o�e���ѡG�q�ܮ榡�����T");
		}

		System.out.println("�q�ܦb�o: " + phone);
		// 2. �ˬd�|���O�_�w�s�b
		if (memberDao.phoneExists(phone) > 0) {

			// 3. �������ҽX�M�ɶ�(�N���ͭn�Ϊ��ܥi�H�� ���ڨS��)
			Random random = new Random();
			int code = random.nextInt(999999) + 1; // �ͦ��d��O1��999999
			String verificationCode = String.format("%06d", code); // �T�O�ɨ�6��A�e����������0

			// 5��������
			LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);

			memberDao.updateVerificationCode(verificationCode, expiry, phone);

			return new BasicRes(200, "���ҽX�o�e���\");
		} else {
			return new BasicRes(400, "���ҽX�o�e���ѡG�ӹq�ܸ��X�|�����U");
		}

	}

	@Override
	public BasicRes resetPassword(LoginMemberReq req) {

		// 1.���b
		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "���ѡG�K�X�榡�����T");
		}

		// �K�X�[�K
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		String newPwd = encryptedPassword;
		memberDao.retsetPassword(newPwd, req.getPhone());
		

		return new BasicRes(200, "�K�X�ק令�\");
	}

}
