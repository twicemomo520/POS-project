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
        // 設定清潔時間
        int cleaningTime = 5; // 清潔時間 5 分鐘

        // 1. 查詢當天的營業時段
        String dayOfWeek = reservationDate.getDayOfWeek().toString();
        List<BusinessHours> businessHoursList = businessHoursDao.findBusinessHoursByDayAndStore(1, dayOfWeek);

        if (businessHoursList.isEmpty()) {
            return new ReservationRes(ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getCode(),
                                      ResMessage.NO_BUSINESS_HOURS_FOUND_FOR_DAY.getMessage());
        }

        List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();

        // 2. 遍歷每個營業時段，計算出可用的時間段
        for (BusinessHours businessHours : businessHoursList) {
            LocalTime openingTime = businessHours.getOpeningTime();
            LocalTime closingTime = businessHours.getClosingTime();
            LocalTime currentTime = openingTime;

            while (currentTime.isBefore(closingTime)) {
                LocalTime slotEndTime = currentTime.plusMinutes(diningDuration); // 計算用餐結束時間
                
                // 確保結束時間加上清潔時間不超過營業結束時間
                if (slotEndTime.plusMinutes(cleaningTime).isAfter(closingTime)) {
                    break;
                }

                // 查詢該時間段內的可用桌位
                List<TableManagement> availableTables = reservationDao.findAvailableTables(reservationDate, currentTime, slotEndTime);

                // 加入日誌輸出可用桌位的數量
                System.out.println("當前檢查的時間段：" + currentTime + " 到 " + slotEndTime);
                System.out.println("可用桌位數量：" + availableTables.size());

                int totalCapacity = 0;
                boolean timeSlotAvailable = true;

                // 檢查桌位的預約狀態
                for (TableManagement table : availableTables) {
                    List<Reservation> overlappingReservations = reservationDao.findReservationsForTable(table.getTableNumber(), reservationDate);
                    for (Reservation reservation : overlappingReservations) {
                        LocalTime reservedStart = reservation.getReservationStartTime();
                        LocalTime reservedEnd = reservation.getReservationEndingTime();

                        // 如果新預約時間與已存在的預約時間重疊，則標記為不可用
                        if (currentTime.isBefore(reservedEnd) && slotEndTime.isAfter(reservedStart)) {
                            timeSlotAvailable = false;
                            break;
                        }
                    }

                    // 計算可用桌位的容量
                    if (timeSlotAvailable) {
                        totalCapacity += table.getTableCapacity(); // 如果沒有重疊，增加可用容量
                    }
                }

                // 加入日誌輸出當前可用桌位容量和此時間段可用狀態
                System.out.println("當前可用桌位容量：" + totalCapacity);
                System.out.println("此時間段可用狀態：" + timeSlotAvailable);

                // 檢查是否有足夠的桌位可供分配
                boolean isAvailable = totalCapacity >= reservationPeople;

                // 設定可用時間段的狀態
                AvailableTimeSlot timeSlot = new AvailableTimeSlot();
                timeSlot.setStartTime(currentTime);
                timeSlot.setEndTime(slotEndTime);
                timeSlot.setAvailable(isAvailable && timeSlotAvailable);  // 如果有足夠的容量且時間不重疊則設定為可用

                availableTimeSlots.add(timeSlot);

                // 更新當前時間段，加上用餐時間和清潔時間
                currentTime = slotEndTime.plusMinutes(cleaningTime);
            }
        }

        // 3. 如果沒有可用的時間段
        if (availableTimeSlots.isEmpty()) {
            return new ReservationRes(ResMessage.NO_RESERVED_TIME_SLOTS.getCode(),
                                      ResMessage.NO_RESERVED_TIME_SLOTS.getMessage());
        }

        // 4. 返回可用時間段的結果
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
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
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
        LocalDate reservationDate = reservationReq.getReservationDate();
        LocalTime reservationStartTime = reservationReq.getReservationStartTime();
        LocalTime reservationEndTime = reservationReq.getReservationEndingTime();

        // 7. 查詢當天所有預約
        List<Reservation> existingReservations = reservationDao.findAllByReservationDate(reservationDate);

        // 8. 查詢可用桌位
        List<TableManagement> availableTables = tableManagementDao.findAvailableTablesOrderedByCapacity();
        List<TableManagement> selectedTables = new ArrayList<>();
        List<TableManagement> mergeableTables = new ArrayList<>();
        int totalCapacity = 0;

        System.out.println("開始分配桌位，預約人數：" + reservationReq.getReservationPeople());
        System.out.println("查詢到的可用桌位：");

        for (TableManagement table : availableTables) {
            System.out.println("桌位：" + table.getTableNumber() + " 容量：" + table.getTableCapacity());

            // 檢查此桌位是否有其他預約與新預約時間重疊
            boolean hasOverlap = existingReservations.stream()
            	    .filter(existing -> existing.getTables().contains(table))
            	    .anyMatch(existing ->
            	        (existing.getReservationStartTime().isBefore(reservationEndTime) && 
            	         existing.getReservationEndingTime().isAfter(reservationStartTime)) ||
            	        (existing.getReservationEndingTime().equals(reservationStartTime) || 
            	         existing.getReservationStartTime().equals(reservationEndTime))
            	    );

            System.out.println("正在檢查桌位：" + table.getTableNumber() + 
                    " 預約時間：" + reservationStartTime + " 到 " + reservationEndTime + 
                    " 是否重疊：" + hasOverlap);

            // 8-1. 如果沒有重疊，並且此桌位容量 >= 訂位人數
            if (!hasOverlap) {
                // 8-1-1. 確保只選擇最小符合條件的桌位
                if (table.getTableCapacity() == reservationReq.getReservationPeople()) {
                    selectedTables.add(table);
                    totalCapacity += table.getTableCapacity();
                    System.out.println("找到完全匹配的桌位：" + table.getTableNumber());
                    break; // 完全匹配，直接退出
                } else if (table.getTableCapacity() > reservationReq.getReservationPeople()) {
                    selectedTables.add(table); // 大於則加入選擇
                    totalCapacity += table.getTableCapacity();
                    System.out.println("找到大於預約人數的桌位：" + table.getTableNumber());
                    break; // 找到合適的桌位後停止查找
                } else {
                    // 如果桌位容量小於預約人數，則考慮併桌的邏輯
                    mergeableTables.add(table);
                    System.out.println("桌位 " + table.getTableNumber() + " 容量不足，加入併桌列表");
                }
            }
        }

        // 8-2. 若未找到完全匹配或大於的桌位，則考慮併桌
        if (totalCapacity < reservationReq.getReservationPeople()) {
            System.out.println("進行併桌分配...");
            for (TableManagement table : mergeableTables) {
                totalCapacity += table.getTableCapacity();
                selectedTables.add(table); // 將併桌加入選擇
                System.out.println("併桌：" + table.getTableNumber() + " 當前總容量：" + totalCapacity);

                // 如果已經達到或超過預約人數，則停止查找
                if (totalCapacity >= reservationReq.getReservationPeople()) {
                    break;
                }
            }
        }

        // 9. 檢查是否有足夠的桌位可供分配
        if (totalCapacity < reservationReq.getReservationPeople()) {
            System.out.println("桌位容量不足，無法滿足預約需求。");
            return new ReservationRes(ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getCode(),
                                      ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getMessage());
        }

        System.out.println("分配成功的桌位：" + selectedTables.stream().map(TableManagement::getTableNumber).collect(Collectors.joining(", ")));
        
        // 10. 更新已分配的桌位狀態為 "RESERVED"
        try {
            selectedTables.forEach(table -> {
                tableManagementDao.updateTableStatus(table.getTableNumber(), "RESERVED");
            });
        } catch (Exception e) {
            return new ReservationRes(500, "更新桌位狀態時發生錯誤");
        }

        // 儲存訂位資訊
        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationReq.getCustomerName());
        reservation.setCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        reservation.setCustomerEmail(reservationReq.getCustomerEmail());
        reservation.setCustomerGender(reservationReq.getCustomerGender()); // 加入性別資料
        reservation.setReservationPeople(reservationReq.getReservationPeople());
        reservation.setReservationDate(reservationDate);
        reservation.setReservationStartTime(reservationStartTime);
        reservation.setReservationEndingTime(reservationEndTime);
        reservation.setTables(selectedTables); // 儲存選擇的桌位

        // 儲存到資料庫
        reservationDao.save(reservation);

        // 發送訂位確認信
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