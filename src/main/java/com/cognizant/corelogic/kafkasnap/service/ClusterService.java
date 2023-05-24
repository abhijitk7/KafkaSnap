package com.cognizant.corelogic.kafkasnap.service;

import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper.ClusterDescription;
import com.cognizant.corelogic.kafkasnap.model.InternalClusterState;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@Service
@AllArgsConstructor
public class ClusterService {

	private final AdminClientService adminClientService;
	private final StatisticsService statisticsService;

	public ClusterDescription getClusterDescription(String clusterName) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		return adminClientService.get(clusterName).getClusterDescription();
	}
	
	public InternalClusterState getClusterDetails(String clusterName)
			throws InterruptedException, ExecutionException, ClusterNotFoundException {
		return new InternalClusterState(adminClientService.get(clusterName), clusterName,
				statisticsService.get(clusterName));
	}

	public InternalClusterState getClusterStats(String clusterName)
			throws ClusterNotFoundException, InterruptedException, ExecutionException {
		return new InternalClusterState(adminClientService.get(clusterName), clusterName,
				statisticsService.get(clusterName));
	}

}
