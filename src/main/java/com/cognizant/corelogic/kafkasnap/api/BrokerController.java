package com.cognizant.corelogic.kafkasnap.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.InternalBroker;
import com.cognizant.corelogic.kafkasnap.model.InternalBrokerConfig;
import com.cognizant.corelogic.kafkasnap.service.BrokerService;

/**
 * 
 * @author Arkit Das
 *
 */
@RestController
public class BrokerController {
	
	@Autowired
	private BrokerService brokerService;
	
	@GetMapping(value = "/{clusterName}/broker/all")
	public ResponseEntity<?> getBrokers(@PathVariable(value="clusterName") String clusterName) {
		List<InternalBroker> brokerDetails = null;
		try {
			brokerDetails = brokerService.getBrokers(clusterName);
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(brokerDetails);
	}
	
	@GetMapping(value = "/{clusterName}/broker/{brokerId}/config")
	public ResponseEntity<?> getBrokerConfigs(@PathVariable(value="clusterName") String clusterName,
			@PathVariable(value="brokerId") int brokerId) {
		List<InternalBrokerConfig> brokerConfigDetails = null;
		try {
			brokerConfigDetails = brokerService.getBrokerConfigs(clusterName, brokerId);
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(brokerConfigDetails);
	}
}
