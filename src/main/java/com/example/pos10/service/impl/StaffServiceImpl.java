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
		// �d�ߥثe�̰������u�s�� �n��Ʀr���U+
		String lastStaffNumber = staffDao.findLastStaffNumber();
		if (lastStaffNumber == null) {
			// �p�G�S�����u�A�q "TEST001" �}�l ����i�H+�]�w�������q�]�w
			return "TEST001";
		}

		// ���o�Ʀr�����A���W1
		String numberPart = lastStaffNumber.substring(lastStaffNumber.length() - 3);
		int num = Integer.parseInt(numberPart) + 1;

		// �榡�Ƭ��T��ơA�å[�W "TEST" �ثe�g�� �]���ڭ̨S���]�w��
		return String.format("TEST%03d", num);
	}

	// �s�W���u
	@Override
	public BasicRes registerStaff(RegisterStaffReq req) {

		// 1. ���b
		if (req.getPwd() == null || req.getPwd().trim().isEmpty()) {
			return new BasicRes(400, "�s�W���u���ѡG�K�X�榡�����T");
		}

		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "�s�W���u���ѡG�m�W�榡�����T");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "�s�W���u���ѡG�q�ܮ榡�����T");
		}

		// 3. �s�|�����

		// �ͦ����u�s��
		// �z�L�W������k ���ͫ��w�����u�s��
		String staffNumber = generateStaffNumber();

		String name = req.getName().trim();
		String phone = req.getPhone().trim();

		// �w�]0 �̤p�v��
		String authorization = "0";

		// �K�X�[�K
		String encryptedPassword = passwordEncoder.encode(req.getPwd());
		String pwd = encryptedPassword;

		// 4. �s�W�|��
		try {
			int result = staffDao.insertStaff(staffNumber, pwd, name, phone, authorization);
			if (result > 0) {
				return new BasicRes(200, "���U���\");
			} else {
				return new BasicRes(500, "���U���ѡG�L�k���U");
			}
		} catch (Exception e) {
			return new BasicRes(500, "���U���ѡG" + e.getMessage());
		}

	}

	// ��s���u���
	@Override
	public BasicRes updateStaff(UpdateStaffReq req) {

		// 1. ���b

		if (req.getName() == null || req.getName().trim().isEmpty()) {
			return new BasicRes(400, "�ק���u��ƥ��ѡG�m�W�榡�����T");
		}

		if (req.getPhone() == null || req.getPhone().trim().isEmpty()) {
			return new BasicRes(400, "�ק���u��ƥ��ѡG�q�ܮ榡�����T");
		}
		
		if (req.getAuthorization() == null || req.getAuthorization().trim().isEmpty()) {
            return new BasicRes(400, "��s���ѡG�v�������T");
        }
		
		//2.����sql
		int result = staffDao.updateStaff( req.getName(), req.getPhone(), req.getAuthorization(),req.getStaffNumber());
        if (result > 0) {
            return new BasicRes(200, "��s���\");
        } else {
            return new BasicRes(500, "��s���ѡG�������������u");
        }
		

	}

	
	
}
