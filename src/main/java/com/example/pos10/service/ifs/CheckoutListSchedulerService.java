package com.example.pos10.service.ifs;

import com.example.pos10.vo.BasicRes;

public interface CheckoutListSchedulerService {

	public BasicRes scheduledUpdateHistory();
	
	public BasicRes manualUpdateHistory();
}
