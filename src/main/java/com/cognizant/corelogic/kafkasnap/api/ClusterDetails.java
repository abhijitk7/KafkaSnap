package com.cognizant.corelogic.kafkasnap.api;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper.ClusterDescription;
import com.cognizant.corelogic.kafkasnap.model.InternalClusterState;
import com.cognizant.corelogic.kafkasnap.service.ClusterService;
/**
 * 
 * @author Arkit Das
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ClusterDetails {
	
	@Autowired
	private ClusterService clusterService;
	
	@GetMapping(value = "/{clusterName}/cluster/all")
	public ResponseEntity<?> getClusterDetails(@PathVariable(value="clusterName") String clusterName) {
		InternalClusterState internalClusterState = null;
		try {
			internalClusterState = clusterService.getClusterDetails(clusterName);
		} catch (ClusterNotFoundException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(internalClusterState);
	}
	
	@GetMapping(value = "/{clusterName}/cluster/{brokerId}/config")
	public ResponseEntity<?> getClusterConfigs(@PathVariable(value="clusterName") String clusterName,
			@PathVariable(value="brokerId") int brokerId) {
		InternalClusterState internalClusterState = null;
		try {
			internalClusterState = clusterService.getClusterStats(clusterName);
		} catch (ClusterNotFoundException | InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(internalClusterState);
	}
}
