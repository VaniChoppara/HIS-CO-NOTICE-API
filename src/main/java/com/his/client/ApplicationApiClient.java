package com.his.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.his.dto.ApplicationRegDTO;

@FeignClient(name="AR-API")
public interface ApplicationApiClient {
	@GetMapping("/application/{appNumber}")
	public ApplicationRegDTO getApplication(@PathVariable Integer appNumber);
	
}
