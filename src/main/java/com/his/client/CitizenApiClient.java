package com.his.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.his.dto.CitizenDTO;

@FeignClient(name="CITIZEN-API")
public interface CitizenApiClient {

	@GetMapping("/citizen/{id}")
	public CitizenDTO getCitizen(@PathVariable("id") Long id);
	
	
}
