package com.cognizant.corelogic.kafkasnap.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.TopicDescription;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.enums.ServerStatusEnum;
import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper.ClusterDescription;
import com.cognizant.corelogic.kafkasnap.model.Statistics;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@Service
@AllArgsConstructor
public class StatisticsService {

	 private final AdminClientService adminClientService;
	 private final Map<String, Statistics> cache = new ConcurrentHashMap<>();
	 
	 public Statistics get(String clusterName) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		 if(Objects.isNull(cache.get(clusterName))) {
			 cache.put(clusterName, getStatistics(clusterName));
		 }
		 
		 return cache.get(clusterName);
	 }
	 public Statistics getStatistics(String clusterName) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		 AdminClientWrapper client = adminClientService.get(clusterName);   
		 ClusterDescription clusterDescription = client.getClusterDescription();
		 client.populateVersionAndTopicDeletionEnabled(); 
		    
		    return Statistics.builder()
                    .status(ServerStatusEnum.ONLINE)
                    .clusterDescription(clusterDescription)
                    .version(client.getVersion())
                    .topicConfigs(client.getTopicsConfig())
                    .topicDescriptions(client.describeTopics())
                    .build();
	 }
}
