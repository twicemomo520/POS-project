package com.example.pos10.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;
import com.example.pos10.entity.TableManagement.TableStatus;
import com.example.pos10.repository.ReservationDao;
import com.example.pos10.repository.TableManagementDao;
import com.example.pos10.service.ifs.ReservationService;
import com.example.pos10.service.ifs.TableManagementService;
import com.example.pos10.vo.AvailableTimeSlot;
import com.example.pos10.vo.ReservationReq;
import com.example.pos10.vo.ReservationRes;
import com.example.pos10.vo.TimeSlotWithTableStatusRes;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	private TableManagementService tableManagementService;

    @Autowired
    private ReservationDao reservationDao;

    @Autowired
    private TableManagementDao tableManagementDao;
    
    @PersistenceContext
    private EntityManager entityManager;

    
    @Autowired
    private EmailService emailService;

    @Override
    public ReservationRes findAvailableTimeSlots(LocalDate reservationDate, int reservationPeople) {
        List<AvailableTimeSlot> availableTimeSlots = new ArrayList<>();

        // 查詢當天可使用桌位的狀態
        List<TimeSlotWithTableStatusRes> availableTableStatuses = tableManagementService.getAvailableTableStatusesByDate(reservationDate);

        for (TimeSlotWithTableStatusRes timeSlot : availableTableStatuses) {
            boolean hasAvailableTable = false;

            // 檢查每個時間段中的桌位狀態
            for (TableManagement table : timeSlot.getTableStatuses()) {
                // 檢查桌位是否滿足條件（例如容量和狀態）
                if (table.getTableCapacity() >= reservationPeople && table.getTableStatus().equals(TableManagement.TableStatus.可使用)) {
                    hasAvailableTable = true;
                    break;
                }
            }

            // 如果有可用桌位，則設置 available 為 true
            String[] times = timeSlot.getTimeSlot().split(" - ");
            String startTime = times[0];
            String endTime = times[1];

            // 將開始和結束時間存入 AvailableTimeSlot，並根據可用性設置 available
            availableTimeSlots.add(new AvailableTimeSlot(startTime, endTime, hasAvailableTable));
        }

        // 返回 ReservationRes，包含可用的開始和結束時間段
        return new ReservationRes(ResMessage.SUCCESS.getCode(), ResMessage.SUCCESS.getMessage(), availableTimeSlots, null);
    }
    
 // 2. 儲存訂位
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

        // 7. 使用 TableManagementService 來獲取可用桌位狀態
        List<TimeSlotWithTableStatusRes> availableTableStatuses = tableManagementService.getAvailableTableStatuses(
            reservationDate, reservationStartTime, reservationEndTime);

        List<TableManagement> selectedTables = new ArrayList<>();
        int totalCapacity = 0;

        System.out.println("開始分配桌位，預約人數：" + reservationReq.getReservationPeople());

        List<TableManagement> exactMatchTables = new ArrayList<>();
        List<TableManagement> largerTables = new ArrayList<>();
        List<TableManagement> mergeableTables = new ArrayList<>();

        // 8. 遍歷可用時間段
        for (TimeSlotWithTableStatusRes timeSlotStatus : availableTableStatuses) {
            // 遍歷可用桌位，進行分配
            for (TableManagement table : timeSlotStatus.getTableStatuses()) {
                System.out.println("桌位：" + table.getTableNumber() + " 容量：" + table.getTableCapacity());

                // 完全匹配桌位
                if (table.getTableCapacity() == reservationReq.getReservationPeople()) {
                    exactMatchTables.add(table);
                }
                // 容量大於訂位人數的桌位
                else if (table.getTableCapacity() > reservationReq.getReservationPeople()) {
                    largerTables.add(table);
                }
                // 併桌邏輯，儲存需要合併的小桌位
                else {
                    mergeableTables.add(table);
                }
            }

            // 優先分配完全匹配的桌位
            if (!exactMatchTables.isEmpty()) {
                TableManagement matchedTable = exactMatchTables.stream()
                        .min(Comparator.comparingInt(TableManagement::getTableCapacity)).get(); // 使用最小的匹配桌位
                selectedTables.add(matchedTable);
                totalCapacity += matchedTable.getTableCapacity();
                System.out.println("完全匹配桌位：" + matchedTable.getTableNumber());
            }

            // 如果沒有完全匹配桌位，分配容量大於訂位人數的桌位
            if (totalCapacity < reservationReq.getReservationPeople() && !largerTables.isEmpty()) {
                TableManagement largerTable = largerTables.stream()
                        .min(Comparator.comparingInt(TableManagement::getTableCapacity)).get(); // 使用最小大於的桌位
                selectedTables.add(largerTable);
                totalCapacity += largerTable.getTableCapacity();
                System.out.println("容量大於訂位人數的桌位：" + largerTable.getTableNumber());
            }

            // 併桌邏輯：首先將併桌根據容量從小到大排序
            if (totalCapacity < reservationReq.getReservationPeople()) {
                mergeableTables.sort(Comparator.comparingInt(TableManagement::getTableCapacity));

                System.out.println("進行併桌分配...");
                for (TableManagement table : mergeableTables) {
                    totalCapacity += table.getTableCapacity();
                    selectedTables.add(table);
                    System.out.println("併桌：" + table.getTableNumber() + " 當前總容量：" + totalCapacity);

                    // 如果總容量已經滿足或超過訂位人數，停止分配
                    if (totalCapacity >= reservationReq.getReservationPeople()) {
                        break; // 成功分配完桌位，退出併桌邏輯
                    }
                }
            }
        }
        
        // 9. 如果容量不足
        if (totalCapacity < reservationReq.getReservationPeople()) {
            System.out.println("桌位容量不足，無法滿足預約需求。");
            return new ReservationRes(ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getCode(),
                                      ResMessage.NOT_ENOUGH_TABLE_CAPACITY.getMessage());
        }

        System.out.println("分配成功的桌位：" + selectedTables.stream().map(TableManagement::getTableNumber).collect(Collectors.joining(", ")));

        // 10. 更新已分配的桌位狀態為 "訂位中"
        try {
            selectedTables.forEach(table -> {
                tableManagementDao.updateTableStatus(table.getTableNumber(), TableManagement.TableStatus.訂位中);
            });
        } catch (Exception e) {
            return new ReservationRes(500, "更新桌位狀態時發生錯誤");
        }

        // 儲存訂位資訊
        Reservation reservation = new Reservation();
        reservation.setCustomerName(reservationReq.getCustomerName());
        reservation.setCustomerPhoneNumber(reservationReq.getCustomerPhoneNumber());
        reservation.setCustomerEmail(reservationReq.getCustomerEmail());
        reservation.setCustomerGender(reservationReq.getCustomerGender());
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

    // 3. 根據顧客電話號碼查詢訂位
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

    // 4. 查詢當天的所有訂位
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

        // 3. 逐一檢查桌位狀態，並更新為 "可使用"
        for (TableManagement table : tables) {
            // 檢查桌位是否處於 "訂位中" 狀態
            if (!table.getTableStatus().equals(TableManagement.TableStatus.訂位中)) {
                return new ReservationRes(ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getCode(), ResMessage.INVALID_TABLE_STATUS_FOR_CANCELLATION.getMessage());
            }

            // 更新桌位狀態為 "可使用"
            table.setTableStatus(TableManagement.TableStatus.可使用);
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

        // 4. 更新這些桌號的狀態為 "可使用"
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
    public ReservationRes manualCheckIn(String tableNumber, int reservationId) {
        // 1. 檢查桌號是否存在
      TableManagement table = tableManagementDao.findById(tableNumber).orElse(null);
      if (table == null) {
          return new ReservationRes(ResMessage.TABLE_NUMBER_NOT_FOUND.getCode(), ResMessage.TABLE_NUMBER_NOT_FOUND.getMessage());
      }

        // 2. 確保桌位處於 訂位中 狀態
        if (!table.getTableStatus().equals(TableManagement.TableStatus.訂位中)) {
         return new ReservationRes(ResMessage.INVALID_STATUS_TRANSITION.getCode(), ResMessage.INVALID_STATUS_TRANSITION.getMessage());
        }
       
        // 3. 更新桌位狀態為 用餐中
        int updatedCount = reservationDao.manualCheckIn(tableNumber, TableStatus.用餐中, TableStatus.訂位中);
        if (updatedCount == 0) {
          return new ReservationRes(ResMessage.FAILED_TO_UPDATE_TABLE_STATUS.getCode(), //
               ResMessage.FAILED_TO_UPDATE_TABLE_STATUS.getMessage());
        }
        
        // 4. 刪除對應的訂位資料
        reservationDao.deleteById(reservationId);

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