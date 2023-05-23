package com.cognizant.corelogic.kafkasnap.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.service.AdminClientService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@RestController
public class KafkaConnectController {
	
	@Autowired
	private AdminClientService adminClientService;
	
	@GetMapping(value = "/connect/{clusterName}")
	public ResponseEntity<?> connect(@PathVariable(value="clusterName") String clusterName) {
		try {
			adminClientService.get(clusterName);
		} catch (ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}

}
