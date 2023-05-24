package com.cognizant.corelogic.kafkasnap.model;

import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.TopicDescription;

import com.cognizant.corelogic.kafkasnap.enums.ServerStatusEnum;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Statistics {
	ServerStatusEnum status;
	Throwable lastKafkaException;
	String version;
	AdminClientWrapper.ClusterDescription clusterDescription;
	Map<String, TopicDescription> topicDescriptions;
	Map<String, List<ConfigEntry>> topicConfigs;
}
