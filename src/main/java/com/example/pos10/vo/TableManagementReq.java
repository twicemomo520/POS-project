package com.example.pos10.vo;

import com.example.pos10.constants.ResMessage;
import com.example.pos10.entity.Reservation;
import com.example.pos10.entity.TableManagement;

public class TableManagementReq extends TableManagement {

    public TableManagementReq () {
        super ();
    }

    // 帶參數的構造函數，前端傳入的 tableStatus 是 String 型態，通過轉換方法將其轉換為 Enum 型態
    public TableManagementReq (String tableNumber, int tableCapacity, String tableStatus, Reservation reservation) {
        super (tableNumber, tableCapacity, convertStringToTableStatus(tableStatus), reservation); 
    }
    
	// 將字串類型的 tableStatus 轉換為 TableStatus 枚舉
    // 因為在 Dao 介面中 TableStatus 是 Enum 型態，資料庫需求是大寫的枚舉值: AVAILABLE, RESERVED, ACTIVE
    // 但前端輸入可能是小寫或不規範的大小寫 (如: available, RESERVED)
    // 因此這個方法將字串轉換為大寫，並匹配對應的枚舉類型
    // 如果 tableStatus 為 null 或是空字串，會跳出錯誤訊息 
    private static TableStatus convertStringToTableStatus (String tableStatus) {
        if (tableStatus == null || tableStatus.isEmpty()) {
            // 當 tableStatus 為 null 或空字串時，拋出自定義錯誤訊息 NULL_OR_EMPTY_TABLE_STATUS
            throw new IllegalArgumentException (ResMessage.NULL_OR_EMPTY_TABLE_STATUS.getMessage());
        }
        try {
            // 將字串轉換為大寫，並映射到 TableStatus 枚舉
            return TableStatus.valueOf (tableStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            // 如果傳入的字串不是有效的枚舉值，拋出自定義的錯誤訊息
        	throw new IllegalArgumentException ("Invalid table status: " + tableStatus + ". " + ResMessage.INVALID_TABLE_STATUS.getMessage());
        }
    }
    
    // 如果 tableStatus 為 null 或是空字串，會返回默認狀態 AVAILABLE
//    private static TableStatus convertStringToTableStatus(String tableStatus) {
//        if (tableStatus != null && !tableStatus.isEmpty()) {
//            try {
//                // 將字串轉換為大寫，並映射到 TableStatus 枚舉
//                return TableStatus.valueOf(tableStatus.toUpperCase());
//            } catch (IllegalArgumentException e) {
//                // 如果傳入的字串不是有效的枚舉值，拋出 IllegalArgumentException
//                throw new IllegalArgumentException(ResMessage.INVALID_TABLE_STATUS.getMessage());
//            }
//        }
//        
//        return TableStatus.AVAILABLE;
//    }
}