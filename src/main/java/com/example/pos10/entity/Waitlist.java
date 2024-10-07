package com.example.pos10.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "waitlist")
public class Waitlist {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name = "customer_name", nullable = false)
    @NotBlank (message = "Customer name cannot be null or empty !!!")
    private String customerName;

    @Column (name = "contact_info", nullable = false)
    @NotBlank (message = "Contact information cannot be null or empty !!!")
    @Pattern (regexp = "^09\\d{8}$", message = "Invalid Taiwan phone number format !!!")
    private String contactInfo;

    @Column (name = "joined_time", nullable = false)
    @NotNull (message = "Joined time cannot be null")
    @PastOrPresent (message = "Joined time must be in the past or present !!!")
    private LocalDateTime joinedTime;

    public Waitlist () {
    }

    public Waitlist (String customerName, String contactInfo, LocalDateTime joinedTime) {
        this.customerName = customerName;
        this.contactInfo = contactInfo;
        this.joinedTime = LocalDateTime.now();  // 默認設置為當前時間
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getCustomerName () {
        return customerName;
    }

    public void setCustomerName (String customerName) {
        this.customerName = customerName;
    }

    public String getContactInfo () {
        return contactInfo;
    }

    public void setContactInfo (String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public LocalDateTime getJoinedTime () {
        return joinedTime;
    }

    public void setJoinedTime (LocalDateTime joinedTime) {
        this.joinedTime = joinedTime;
    }
}