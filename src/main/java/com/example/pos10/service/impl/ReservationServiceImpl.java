package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.BusinessHours;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.repository.BusinessHoursDao;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.vo.AvailableTimeSlot;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @Autowired
    private BusinessHoursDao businessHoursDao;
    
    @Autowired
    private EmailService emailService;
    
    // 1. 查詢可用的桌位並儲存
    @Override
    public ReservationRes generateAndFindAvailableTables(LocalDate reservationDate, int diningDuration, int reservationPeople) {
        // 查詢當天的營業時段
        String dayOfWeek = reservationDate.getDayOfWeek().toString();
        List<BusinessHours> businessHoursList = businessHoursDao.findBusinessHoursByDayAndStore(1, dayOfWeek);

        if (businessHoursList.isEmpty()) {
            return new ReservationRes(ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getCode(),
                                      ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getMessage());
        }

        List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();

        // 遍歷每個營業時段，計算出可用的時間段
        for (BusinessHours businessHours : businessHoursList) {
            LocalTime openingTime = businessHours.getOpeningTime();
            LocalTime closingTime = businessHours.getClosingTime();
            LocalTime currentTime = openingTime;

            while (currentTime.isBefore(closingTime)) {
                LocalTime slotEndTime = currentTime.plusMinutes(diningDuration);

                // 確保結束時間不超過營業結束時間
                if (slotEndTime.isAfter(closingTime)) {
                    break;
                }

                // 查詢該時間段內的可用桌位
                List<TableManagement> availableTables = reservationDao.findAvailableTables(reservationDate, currentTime, slotEndTime);

                // 計算可用桌位的總容量
                int totalCapacity = availableTables.stream()
                                                   .filter(table -> table.getTableStatus() == TableManagement.TableStatus.AVAILABLE)
                                                   .mapToInt(TableManagement::getTableCapacity)
                                                   .sum();

                // 檢查是否有足夠的桌位可供分配
                boolean isAvailable = totalCapacity >= reservationPeople;

                // 設定可用時間段的狀態
                AvailableTimeSlot timeSlot = new AvailableTimeSlot();
                timeSlot.setStartTime(currentTime);
                timeSlot.setEndTime(slotEndTime);
                timeSlot.setAvailable(isAvailable);  // 如果有足夠的容量則設定為可用

                availableTimeSlots.add(timeSlot);

                // 更新當前時間段
                currentTime = currentTime.plusMinutes(diningDuration);
            }
        }

        // 如果沒有可用的時間段
        if (availableTimeSlots.isEmpty()) {
            return new ReservationRes(ResMessage.NO_RESERVED_TIME_SLOTS.getCode(),
                                      ResMessage.NO_RESERVED_TIME_SLOTS.getMessage());
        }

        // 返回可用時間段的結果
        return new ReservationRes(ResMessage.SUCCESS.getCode(),
                                  ResMessage.SUCCESS.getMessage(), availableTimeSlots, null);
    }

    // 2. 計算可用的開始時間段 (設定階段需要查看）
    @Override
    public List <LocalTime> calculateAvailableStartTimes (LocalTime openingTime, LocalTime closingTime, int diningDuration) {
        if (openingTime.isAfter(closingTime)) {
            throw new IllegalArgumentException("營業開始時間不能晚於結束時間！");
        }

        if (diningDuration > ChronoUnit.MINUTES.between(openingTime, closingTime)) {
            throw new IllegalArgumentException("用餐時長超過了營業時間！");
        }

        List <LocalTime> availableStartTimes = new ArrayList<>();
        LocalTime currentTime = openingTime;

        // 根據營業時間和用餐時間計算可用時間段
        while (currentTime.isBefore(closingTime)) {
            LocalTime slotEndTime = currentTime.plusMinutes(diningDuration);

            // 確保結束時間不超過營業時間
            if (slotEndTime.isAfter(closingTime)) {
                break;
            }

            availableStartTimes.add(currentTime);
            currentTime = currentTime.plusMinutes(diningDuration);
        }

        return availableStartTimes;
    }

    // 3. 儲存訂位
    @Override
    @Transactional
    public ReservationRes saveReservation(ReservationReq reservationReq) {
        // 1. 驗證顧客姓名
        if (reservationReq.getCustomerName() == null || reservationReq.getCustomerName().isEmpty()) {
            return new ReservationRes(ResMessage.INVALID_CUSTOMER_NAME.getCode(), ResMessage.INVALID_CUSTOMER_NAME.getMessage());
        }

        // 2. 驗證電話號碼格式
        String phoneNumber = reservationReq.getCustomerPhoneNumber();
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) { // 假設是10位數字
            return new ReservationRes(ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
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

        // 6. 確認選擇的訂位時間段
        if (reservationReq.getReservationDate() == null || reservationReq.getReservationStartTime() == null || reservationReq.getReservationEndingTime() == null) {
            return new ReservationRes(ResMessage.NO_RESERVED_TIME_SLOTS.getCode(), ResMessage.NO_RESERVED_TIME_SLOTS.getMessage());
        }

        // 7. 檢查是否有重複訂位（根據同一電話號碼和相同的時間段）
        List<Reservation> existingReservations = reservationDao.findByCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        for (Reservation existing : existingReservations) {
            if (existing.getReservationDate().equals(reservationReq.getReservationDate())
                    && existing.getReservationStartTime().equals(reservationReq.getReservationStartTime())) {
                return new ReservationRes(ResMessage.DUPLICATE_RESERVATION.getCode(), ResMessage.DUPLICATE_RESERVATION.getMessage());
            }
        }

        // 檢查是否有足夠的總容量可供分配
        List<TableManagement> availableTables = tableManagementDao.findAvailableTablesOrderedByCapacity(); // 查詢所有狀態為 "AVAILABLE" 的桌位
        int totalAvailableCapacity = availableTables.stream().mapToInt(TableManagement::getTableCapacity).sum();
        if (totalAvailableCapacity < people) {
            return new ReservationRes(ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getCode(), ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getMessage());
        }

        // 8. 自動分配桌位
        List<TableManagement> selectedTables = new ArrayList<>();
        int totalCapacity = 0;

        // 8.1 優先選擇符合人數的桌位
        for (TableManagement table : availableTables) {
            if (table.getTableCapacity() == people) {
                selectedTables.add(table);
                totalCapacity += table.getTableCapacity();
                break; // 找到符合人數的桌位後停止
            }
        }

        // 8.2 如果沒有符合的桌位，則選擇大於人數的桌位
        if (totalCapacity < people) {
            for (TableManagement table : availableTables) {
                if (table.getTableCapacity() > people) {
                    selectedTables.add(table);
                    totalCapacity += table.getTableCapacity();
                    if (totalCapacity >= people) {
                        break; // 當總容量滿足人數需求後停止分配
                    }
                }
            }
        }

        // 8.3 如果仍然沒有滿足需求，則選擇剩餘的桌位
        if (totalCapacity < people) {
            for (TableManagement table : availableTables) {
                selectedTables.add(table);
                totalCapacity += table.getTableCapacity();
                if (totalCapacity >= people) {
                    break; // 當總容量滿足人數需求後停止分配
                }
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
        reservation.setReservationDate(reservationReq.getReservationDate());  // 設定已選擇的日期
        reservation.setReservationStartTime(reservationReq.getReservationStartTime());  // 設定已選擇的開始時間
        reservation.setReservationEndingTime(reservationReq.getReservationEndingTime());  // 設定已選擇的結束時間
        reservation.setTables(selectedTables); // 儲存選擇的桌位

        reservationDao.save(reservation);

        // 11. 發送訂位確認信
        try {
            emailService.sendReservationConfirmationEmail(
                reservation.getCustomerEmail(),
                reservation.getCustomerName(),
                reservation.getReservationDate().toString(),
                reservation.getReservationStartTime().toString(),
                reservation.getReservationPeople()
            );
        } catch (Exception e) {
            return new ReservationRes(500, "訂位成功，但發送確認信時發生錯誤");
        }

        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 4. 根據顧客電話號碼查詢訂位
    @Override
    public ReservationRes findReservationsByPhoneNumber(String phoneNumber) {
        // 1. 驗證電話號碼格式
        if (phoneNumber == null || (!phoneNumber.matches("\\d{10}") && !phoneNumber.matches("\\d{3}"))) {
            return new ReservationRes(ResMessage.INVALID_PHONE_NUMBER_FORMAT.getCode(), ResMessage.INVALID_PHONE_NUMBER_FORMAT.getMessage());
        }

        List<Reservation> reservations;

        // 2. 如果輸入的是三碼，則查詢結尾為這三碼的所有電話號碼
        if (phoneNumber.length() == 3) {
            reservations = reservationDao.findByCustomerPhoneNumberEndingWith(phoneNumber);
        } else {
            // 3. 否則查詢完整的電話號碼
            reservations = reservationDao.findByCustomerPhoneNumber(phoneNumber);
        }

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
                reservation.getReservationDate(),
                reservation.getReservationStartTime(),
                reservation.getReservationEndingTime(),
                reservation.getTables()
            ))
            .collect(Collectors.toList());

        // 6. 回傳成功結果與查詢到的資料
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), reservationReqs);
    }

    // 5. 查詢當天的所有訂位
    @Override
    public ReservationRes findReservationsByDate (LocalDate reservationDate) {
    	// 1. 驗證日期是否為空
        if (reservationDate == null) {
            return new ReservationRes(ResMessage.NULL_OR_EMPTY_RESERVATION_DATE.getCode(), 
            		ResMessage.NULL_OR_EMPTY_RESERVATION_DATE.getMessage());
        }

        // 2. 驗證日期是否為過去的日期
        if (reservationDate.isBefore(LocalDate.now())) {
            return new ReservationRes(ResMessage.RESERVATION_DATE_CANNOT_BE_IN_PAST.getCode(), 
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
                reservation.getReservationDate(),
                reservation.getReservationStartTime(),
                reservation.getReservationEndingTime(),
                reservation.getTables() // 這裡要確保取到桌位資料
            ))
            .collect(Collectors.toList());
        
        // 6. 回傳成功結果與查詢到的資料
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), reservationReqs);
    }

    // 6. 取消訂位並更新桌位狀態
    @Override
    @Transactional
    public ReservationRes cancelReservation(int reservationId) {
        // 1. 透過 reservationId 查詢對應的 Reservation
        Reservation reservation = reservationDao.findById(reservationId).orElse(null);
        if (reservation == null) {
            return new ReservationRes(ResMessage.NO_RESERVATIONS_FOUND.getCode(), ResMessage.NO_RESERVATIONS_FOUND.getMessage());
        }

        // 2. 從 Reservation 中獲取所有對應的桌位
        List <TableManagement> tables = reservation.getTables(); // 假設 Reservation 有 tables 屬性

        if (tables == null || tables.isEmpty()) {
            return new ReservationRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
        }

        // 3. 逐一檢查桌位狀態，並更新為 "AVAILABLE"
        for (TableManagement table : tables) {
            // 檢查桌位是否處於 "RESERVED" 狀態
            if (!table.getTableStatus().equals(TableManagement.TableStatus.RESERVED)) {
                return new ReservationRes(ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getCode(), ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getMessage());
            }

            // 更新桌位狀態為 "AVAILABLE"
            table.setTableStatus(TableManagement.TableStatus.AVAILABLE);
            tableManagementDao.save(table); // 儲存更新後的桌位狀態
        }

        // 4. 刪除對應的訂位資料
        reservationDao.deleteById(reservationId);

        // 5. 返回成功訊息
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 7. 自動更新桌位狀態（每10分鐘執行一次）
    @Override
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分鐘執行一次
    @Transactional
    public ReservationRes autoUpdateTableStatus(LocalDate currentDate, LocalTime currentTime) {
        // 1. 獲取當前日期和時間
        currentDate = LocalDate.now();
        currentTime = LocalTime.now();
        
        // 2. 計算cutOffTime（設定為當前時間的 10 分鐘前）
        LocalTime cutOffTime = currentTime.minusMinutes(10);

        System.out.println("自動更新啟動，當前時間：" + currentDate + " " + currentTime + "，cutOffTime：" + cutOffTime);
        
        // 3. 使用 Dao 查找已預訂但過期的桌號
        List<String> reservedTables = reservationDao.findReservedTables(currentDate, cutOffTime);
        System.out.println("過期桌號列表: " + reservedTables);
        
        if (reservedTables.isEmpty()) {
            return new ReservationRes(ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getCode(), 
                                      ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getMessage());
        }

        // 4. 更新這些桌號的狀態為 "AVAILABLE"
        int updatedTablesCount = reservationDao.updateTableStatusToAvailable(reservedTables);
        
        // 查找並刪除與這些桌位關聯的過期訂位
        for (String tableNumber : reservedTables) {
        	List<Integer> reservationIds = reservationDao.findReservationIdsByTableNumber(tableNumber);
            reservationDao.deleteByReservationIds(reservationIds);
        }

        // 6. 返回更新結果
        if (updatedTablesCount > 0) {
            return new ReservationRes(ResMessage.SUCCESS.getCode(), 
                                      ResMessage.SUCCESS.getMessage() + " 已更新的桌位數量: " + updatedTablesCount);
        } else {
            return new ReservationRes(ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getCode(), 
                                      ResMessage.TABLE_STATUS_IS_NOT_AVAILABLE.getMessage());
        }
    }
    
    // 8. 手動報到更新桌位狀態
    @Override
    @Transactional // 確保資料庫的更新操作在事務中執行
    public ReservationRes manualCheckIn(String tableNumber) {
        // 1. 檢查桌號是否存在
    	 TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
 	    if (table == null) {
 	        return new ReservationRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
 	    }

        // 2. 確保桌位處於 RESERVED 狀態
        if (!table.getTableStatus().equals(TableManagement.TableStatus.RESERVED)) {
        	return new ReservationRes(ResMessage.INVALID_STATUS_TRANSITION.getCode(), ResMessage.INVALID_STATUS_TRANSITION.getMessage());
        }
       
        // 3. 更新桌位狀態為 ACTIVE
        int updatedCount = reservationDao.manualCheckIn(tableNumber);
        if (updatedCount == 0) {
        	 return new ReservationRes(ResMessage.FAILED_TO_UPDATE_TABLE_STATUS.getCode(), //
             		ResMessage.FAILED_TO_UPDATE_TABLE_STATUS.getMessage());
        }

        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage());
    }

    // 9. 訂位前一天發送提醒
    @Override
    @Scheduled(cron = "0 0 * * * ?")  // 每小時整點執行一次
    public void sendReservationReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);  // 計算明天的日期
        
        // 查詢明天的訂位
        List<Reservation> tomorrowReservations = reservationDao.findAllByReservationDate(tomorrow);

        // 發送提醒郵件
        tomorrowReservations.forEach(reservation -> {
            // 直接從 Reservation 中獲取資料，無需再透過 ReservationManagement
            emailService.sendReminderEmail(
                reservation.getCustomerEmail(),   // 顧客的電子郵件
                reservation.getCustomerName(),    // 顧客的名字
                reservation.getReservationDate().toString(), // 訂位日期
                reservation.getReservationStartTime().toString(), // 訂位開始時間
                reservation.getReservationPeople() // 訂位人數
            );
        });

        System.out.println("訂位提醒郵件已發送");
    }
}