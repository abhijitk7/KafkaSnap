package com.cognizant.corelogic.kafkasnap.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.InternalBrokerConfig;
import com.cognizant.corelogic.kafkasnap.model.InternalTopicConfig;
import com.cognizant.corelogic.kafkasnap.model.TopicDetailsInfo;
import com.cognizant.corelogic.kafkasnap.model.TopicPartitionInfo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TopicService {

	private final AdminClientService adminClientService;
	
	public List<TopicDetailsInfo> getTopicDetails( String clusterName ) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		List<TopicDetailsInfo> topicDetailsInfos = new ArrayList<>();
		Map<String, KafkaFuture<TopicDescription>> topicDescriptionMap = adminClientService.get(clusterName).getTopicDescription(true);
		TopicDescription topicDescription = null;
		for(String key: topicDescriptionMap.keySet()) {
			topicDescription = topicDescriptionMap.get(key).get();
			topicDetailsInfos.add(TopicDetailsInfo.builder()
			.name(key)
			.partitions(topicDescription.partitions().stream()
					.map(partition -> {
						return TopicPartitionInfo.builder()
						.partition(partition.partition())
						.isr(partition.isr())
						.leader(partition.leader())
						.replicas(partition.replicas())
						.build();
					}).collect(Collectors.toList()))
			.build());
		}
		
		return topicDetailsInfos;
	}
	
	public List<InternalTopicConfig> getTopicConfigs( String clusterName ) throws InterruptedException, ExecutionException, ClusterNotFoundException {
		return adminClientService.get(clusterName).getTopicsConfig().values().stream().findFirst().orElse(List.of()).stream()
				.map(InternalTopicConfig::from).collect(Collectors.toList()); 
		
	}
}
