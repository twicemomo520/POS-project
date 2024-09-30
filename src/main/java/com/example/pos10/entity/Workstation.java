package com.example.pos10.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


@Entity
@Table (name = "workstation")
public class Workstation {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "workstation_id")
    private int workstationId;

    @NotBlank (message = "Workstation name cannot be null or empty !!!")
    @Column (name = "workstation_name", nullable = false)
    private String workstationName;

    public Workstation () {
		super ();
	}

	public Workstation (int workstationId, String workstationName) {
		super ();
		this.workstationId = workstationId;
		this.workstationName = workstationName;
	}
	

	public Workstation(@NotBlank(message = "Workstation name cannot be null or empty !!!") String workstationName) {
		super();
		this.workstationName = workstationName;
	}

	public int getWorkstationId () {
        return workstationId;
    }

    public void setWorkstationId (int workstationId) {
        this.workstationId = workstationId;
    }

    public String getWorkstationName () {
        return workstationName;
    }

    public void setWorkstationName (String workstationName) {
        this.workstationName = workstationName;
        
        
    }
}