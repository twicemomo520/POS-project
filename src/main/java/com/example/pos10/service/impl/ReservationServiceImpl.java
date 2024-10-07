package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pos10.entity.Reservation;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private EmailService emailService;

    // 1. 儲存訂位
    @Override
    public ReservationRes saveReservation(ReservationReq reservationReq) {
    	 // 1. 驗證顧客姓名
        if (reservationReq.getCustomerName() == null || reservationReq.getCustomerName().isEmpty()) {
            return new ReservationRes<>(400, "顧客姓名不能為空");
        }

        // 2. 驗證電話號碼格式
        String phoneNumber = reservationReq.getCustomerPhoneNumber();
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) { // 假設是10位數字
            return new ReservationRes<>(400, "無效的電話號碼格式");
        }

        // 3. 驗證電子郵件格式
        String email = reservationReq.getCustomerEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new ReservationRes<>(400, "無效的電子郵件地址");
        }

        // 4. 驗證性別
        if (reservationReq.getCustomerGender() == null) {
            return new ReservationRes<>(400, "顧客性別不能為空");
        }

        // 5. 驗證訂位人數
        int people = reservationReq.getReservationPeople();
        if (people <= 0) {
            return new ReservationRes<>(400, "訂位人數必須大於 0");
        }

        // 6. 驗證訂位時間
        if (reservationReq.getReservationTime().isBefore(LocalTime.now())) {
            return new ReservationRes<>(400, "訂位時間不能是過去的時間");
        }

        // 7. 檢查桌位狀態與容納量
        if (!reservationReq.getReservationManagement().getTableManagement().getTableStatus().equals("AVAILABLE")) {
            return new ReservationRes<>(400, "選定的桌位目前不可用");
        }
        if (reservationReq.getReservationManagement().getTableManagement().getTableCapacity() < people) {
            return new ReservationRes<>(400, "訂位人數超過桌位最大容納量");
        }

        // 8. 檢查是否有重複訂位
        List<Reservation> existingReservations = reservationDao.findByCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        for (Reservation existing : existingReservations) {
            if (existing.getReservationTime().equals(reservationReq.getReservationTime())) {
                return new ReservationRes<>(400, "您已經有相同時間的訂位");
            }
        }

        // 如果所有驗證都通過，儲存訂位
        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationReq.getCustomerName());
        reservation.setCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        reservation.setCustomerEmail(reservationReq.getCustomerEmail());
        reservation.setCustomerGender(reservationReq.getCustomerGender());
        reservation.setReservationPeople(reservationReq.getReservationPeople());
        reservation.setReservationTime(reservationReq.getReservationTime());
        reservation.setReservationManagement(reservationReq.getReservationManagement());

        reservationDao.save(reservation);

        return new ReservationRes<>(200, "訂位已成功儲存");
    }

    // 2. 根據顧客電話號碼查詢訂位
    @Override
    public ReservationRes findReservationsByPhoneNumber(String phoneNumber) {
        List<Reservation> reservations = reservationDao.findByCustomerPhoneNumber(phoneNumber);
        List<ReservationReq> reservationReqs = reservations.stream()
            .map(reservation -> new ReservationReq(
                reservation.getReservationId(),
                reservation.getCustomerName(),
                reservation.getCustomerPhoneNumber(),
                reservation.getCustomerEmail(),
                reservation.getCustomerGender(),
                reservation.getReservationPeople(),
                reservation.getReservationTime(),
                reservation.getReservationManagement()
            ))
            .collect(Collectors.toList());

        return new ReservationRes(200, "查詢成功");
    }

    // 3. 查詢當天的所有訂位
    @Override
    public ReservationRes findReservationsByDate(LocalDate reservationDate) {
        List<Reservation> reservations = reservationDao.findAllByReservationDate(reservationDate);
        List<ReservationReq> reservationReqs = reservations.stream()
            .map(reservation -> new ReservationReq(
                reservation.getReservationId(),
                reservation.getCustomerName(),
                reservation.getCustomerPhoneNumber(),
                reservation.getCustomerEmail(),
                reservation.getCustomerGender(),
                reservation.getReservationPeople(),
                reservation.getReservationTime(),
                reservation.getReservationManagement()
            ))
            .collect(Collectors.toList());

        return new ReservationRes(200, "查詢成功");
    }

    // 4. 取消訂位
    @Override
    public ReservationRes cancelReservation(String tableNumber) {
        tableManagementDao.updateTableStatus(tableNumber, "AVAILABLE");  // 使用 updateTableStatus 方法更新桌位狀態為 "可使用"
        return new ReservationRes(200, "訂位已取消，桌位已釋放");
    }

    // 5. 自動更新桌位狀態（每10分鐘執行一次）
    @Override
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分鐘執行一次
    public void autoUpdateTableStatus(LocalDate currentDate, LocalTime currentTime, LocalTime cutOffTime) {
        reservationDao.autoUpdateTableStatusToAvailable(currentDate, cutOffTime);
    }
    
    // 6. 訂位時間前 30 分鐘發送提醒
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分鐘檢查一次
    public void sendReservationReminders() {
        LocalDate currentDate = LocalDate.now();
        LocalTime reminderTime = LocalTime.now().plusMinutes(30);

        // 查找30分鐘內的訂位
        List<Reservation> reservations = reservationDao.findReservationsStartingSoon(currentDate, reminderTime);

        reservations.forEach(reservation -> {
            emailService.sendReminder(reservation.getCustomerEmail(), reservation);
        });
        
    }
}