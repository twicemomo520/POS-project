package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.ReservationManagement;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.ReservationManagementDao;
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
            return new ReservationRes(ResMessage.INVALID_CUSTOMER_NAME.getCode(), ResMessage.INVALID_CUSTOMER_NAME.getMessage());
        }

        // 2. 驗證電話號碼格式
        String phoneNumber = reservationReq.getCustomerPhoneNumber();
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) { // 假設是10位數字
            return new ReservationRes (ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
        }

        // 3. 驗證電子郵件格式
        String email = reservationReq.getCustomerEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new ReservationRes(ResMessage.INVALID_EMAIL_FORMAT.getCode(), ResMessage.INVALID_EMAIL_FORMAT.getMessage());
        }

        // 4. 驗證性別
        if (reservationReq.getCustomerGender() == null) {
            return new ReservationRes(ResMessage.INVALID_CUSTOMER_GENDER.getCode(), ResMessage.INVALID_CUSTOMER_GENDER.getMessage());
        }

        // 5. 驗證訂位人數
        int people = reservationReq.getReservationPeople();
        if (people <= 0) {
            return new ReservationRes(ResMessage.INVALID_RESERVATION_PEOPLE.getCode(), ResMessage.INVALID_RESERVATION_PEOPLE.getMessage());
        }

        // 6. 根據 ReservationManagement 直接確認訂位時間段
        ReservationManagement reservationManagement = reservationReq.getReservationManagement();
        if (reservationManagement == null || reservationManagement.getIndexId() == 0) {
        	return new ReservationRes(ResMessage.NO_RESERVED_TIME_SLOTS.getCode(), ResMessage.NO_RESERVED_TIME_SLOTS.getMessage());
        }

        // 7. 檢查是否有重複訂位（根據同一電話號碼和相同的時間段）
        List<Reservation> existingReservations = reservationDao.findByCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        for (Reservation existing : existingReservations) {
            if (existing.getReservationManagement().equals(reservationManagement)) {
                return new ReservationRes(ResMessage.DUPLICATE_RESERVATION.getCode(), ResMessage.DUPLICATE_RESERVATION.getMessage());
            }
        }

        // 檢查是否有足夠的總容量可供分配
        List <TableManagement> availableTables = tableManagementDao.findAvailableTablesOrderedByCapacity(); // 查詢所有狀態為 "AVAILABLE" 的桌位
        int totalAvailableCapacity = availableTables.stream().mapToInt(TableManagement::getTableCapacity).sum();
        if (totalAvailableCapacity < people) {
            return new ReservationRes(ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getCode(), ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getMessage());
        }

        // 8. 自動分配桌位
        int totalCapacity = 0;
        List<TableManagement> selectedTables = new ArrayList<>();

        for (TableManagement table : availableTables) {
            selectedTables.add(table);
            totalCapacity += table.getTableCapacity();
            if (totalCapacity >= people) {
                break;  // 當桌位總容量滿足人數需求後，停止分配
            }
        }

        // 9. 更新已分配的桌位狀態為 "RESERVED"
        try {
            selectedTables.forEach(table -> {
                tableManagementDao.updateTableStatus(table.getTableNumber(), "RESERVED");
            });
        } catch (Exception e) {
            return new ReservationRes(500, "更新桌位狀態時發生錯誤");
        }

        // 10. 儲存訂位資訊
        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationReq.getCustomerName());
        reservation.setCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        reservation.setCustomerEmail(reservationReq.getCustomerEmail());
        reservation.setCustomerGender(reservationReq.getCustomerGender());
        reservation.setReservationPeople(reservationReq.getReservationPeople());
        reservation.setReservationManagement(reservationManagement);  // 設定已選擇的時間段
        reservation.setTables(selectedTables);

        reservationDao.save(reservation);

        // 11. 發送訂位確認信
        try {
            emailService.sendReservationConfirmationEmail(
                reservation.getCustomerEmail(),
                reservation.getCustomerName(),
                reservation.getReservationManagement().getReservationDate().toString(),
                reservation.getReservationManagement().getReservationStarttime().toString(),
                reservation.getReservationPeople()
            );
        } catch (Exception e) {
            return new ReservationRes(500, "訂位成功，但發送確認信時發生錯誤");
        }

        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 2. 根據顧客電話號碼查詢訂位
    @Override
    public ReservationRes findReservationsByPhoneNumber(String phoneNumber) {
        // 1. 驗證電話號碼格式
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            return new ReservationRes(ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
        }

        // 2. 查詢訂位
        List <Reservation> reservations = reservationDao.findByCustomerPhoneNumber(phoneNumber);

        // 3. 檢查查詢結果是否為空
        if (reservations.isEmpty()) {
            return new ReservationRes(ResMessage.NO_RESERVATIONS_FOUND.getCode(), ResMessage.NO_RESERVATIONS_FOUND.getMessage());
        }

        // 4. 將查詢結果轉換為 ReservationReq 格式
        List <ReservationReq> reservationReqs = reservations.stream()
            .map(reservation -> {
                if (reservation.getReservationManagement() == null) {
                    throw new IllegalStateException("ReservationManagement 不應為空");
                }
                return new ReservationReq(
                    reservation.getReservationId(),
                    reservation.getCustomerName(),
                    reservation.getCustomerPhoneNumber(),
                    reservation.getCustomerEmail(),
                    reservation.getCustomerGender(),
                    reservation.getReservationPeople(),
                    reservation.getReservationManagement(),
                    null
                );
            })
            .collect(Collectors.toList());

        // 5. 回傳成功結果與查詢到的資料
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), reservationReqs);
    }

    // 3. 查詢當天的所有訂位
    @Override
    public ReservationRes findReservationsByDate (LocalDate reservationDate) {
        // 1. 驗證日期是否為空
        if (reservationDate == null) {
            return new ReservationRes(ResMessage.NULL_OR_EMPTY_RESERVATION_DATE.getCode(), //
            		ResMessage.NULL_OR_EMPTY_RESERVATION_DATE.getMessage());
        }

        // 2. 驗證日期是否為過去的日期
        if (reservationDate.isBefore(LocalDate.now())) {
            return new ReservationRes(ResMessage.RESERVATION_DATE_CANNOT_BE_IN_PAST.getCode(), //
            		ResMessage.RESERVATION_DATE_CANNOT_BE_IN_PAST.getMessage());
        }

        // 3. 查詢當天的所有訂位
        List<Reservation> reservations = reservationDao.findAllByReservationDate(reservationDate);
        
        // 4. 檢查查詢結果是否為空
        if (reservations.isEmpty()) {
        	 return new ReservationRes(ResMessage.NO_RESERVATIONS_FOUND.getCode(), ResMessage.NO_RESERVATIONS_FOUND.getMessage());
        }

        // 5. 將查詢結果轉換為 ReservationReq 格式
        List<ReservationReq> reservationReqs = reservations.stream()
            .map(reservation -> new ReservationReq(
                reservation.getReservationId(),
                reservation.getCustomerName(),
                reservation.getCustomerPhoneNumber(),
                reservation.getCustomerEmail(),
                reservation.getCustomerGender(),
                reservation.getReservationPeople(),
                reservation.getReservationManagement(),
                null 
            ))
            .collect(Collectors.toList());

        // 6. 回傳成功結果與查詢到的資料
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), reservationReqs);
    }

    // 4. 取消訂位
    @Override
    public ReservationRes cancelReservation(String tableNumber) {
        // 1. 驗證桌號是否存在
        TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
        if (table == null) {
            return new ReservationRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
        }

        // 2. 檢查桌位當前狀態
        if (!table.getTableStatus().equals(TableManagement.TableStatus.RESERVED)) {
            return new ReservationRes(ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getCode(), ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getMessage());
        }

        // 3. 更新桌位狀態為 "AVAILABLE"
        tableManagementDao.updateTableStatus(tableNumber, "AVAILABLE");  

        // 4. 返回成功訊息
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 5. 自動更新桌位狀態（每10分鐘執行一次）
    @Override
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分鐘執行一次
    public void autoUpdateTableStatus(LocalDate currentDate, LocalTime currentTime, LocalTime cutOffTime) {
        // 1. 驗證日期和時間是否為空
        if (currentDate == null || currentTime == null || cutOffTime == null) {
            throw new IllegalArgumentException("日期和時間不能為空");
        }

        // 2. 更新桌位狀態，並獲取更新的桌位數量
        int updatedTablesCount = reservationDao.autoUpdateTableStatusToAvailable(currentDate, cutOffTime);

        // 3. 記錄更新操作
        System.out.println("自動更新完成，已更新的桌位數量: " + updatedTablesCount);
    }

    // 6. 訂位前一天發送提醒
    @Override
    @Scheduled(cron = "0 0 8 * * ?")  // 每天早上8點執行一次
    public void sendReservationReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);  // 計算明天的日期
        // 查詢明天的訂位
        List<Reservation> tomorrowReservations = reservationDao.findAllByReservationDate(tomorrow);

        // 發送提醒郵件
        tomorrowReservations.forEach(reservation -> {
            ReservationManagement reservationManagement = reservation.getReservationManagement();
            if (reservationManagement != null) {
                emailService.sendReminderEmail(
                    reservation.getCustomerEmail(),   // 顧客的電子郵件
                    reservation.getCustomerName(),    // 顧客的名字
                    reservationManagement.getReservationDate().toString(), // 訂位日期
                    reservationManagement.getReservationStarttime().toString(), // 訂位開始時間
                    reservation.getReservationPeople() // 訂位人數
                );
            }
        });


        System.out.println("訂位提醒郵件已發送");
    }
}