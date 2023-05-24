package com.cognizant.corelogic.kafkasnap.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.exception.NotFoundException;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper.ClusterDescription;
import com.cognizant.corelogic.kafkasnap.model.InternalBroker;
import com.cognizant.corelogic.kafkasnap.model.InternalBrokerConfig;
import com.cognizant.corelogic.kafkasnap.model.PartitionDistributionStats;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@Service
@AllArgsConstructor
public class BrokerService {

	private ClusterService clusterService;
	private AdminClientService adminClientService;
	private StatisticsService statisticsService;
	
	public List<InternalBroker> getBrokers(String clusterName) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		ClusterDescription clusterDescription = clusterService.getClusterDescription(clusterName);
		var stats = statisticsService.get(clusterName);
	    var partitionsDistribution = PartitionDistributionStats.create(stats);
	    
	    return clusterDescription.getNodes().stream()
	            .map(node -> new InternalBroker(node, partitionsDistribution, stats))
	            .collect(Collectors.toList());
	}
	
	public List<InternalBrokerConfig> getBrokerConfigs(String clusterName, int brokerId) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		if(statisticsService.get(clusterName).getClusterDescription().getNodes()
        .stream().noneMatch(node -> node.id() == brokerId)) {
			new NotFoundException(String.format("Broker with id %s not found", brokerId));
		}
		return adminClientService.get(clusterName).loadBrokersConfig(Collections.singletonList(brokerId))
				.values().stream().findFirst().orElse(List.of()).stream()
				.map(InternalBrokerConfig::from).collect(Collectors.toList()); 
	}
}
