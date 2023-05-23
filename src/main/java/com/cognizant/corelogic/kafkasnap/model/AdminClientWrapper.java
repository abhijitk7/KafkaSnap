package com.cognizant.corelogic.kafkasnap.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.lang.Nullable;

import com.cognizant.corelogic.kafkasnap.config.ClusterProperties.Cluster;

import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Arkit Das
 *
 */
@Builder
@Getter
public class AdminClientWrapper {

	private final AdminClient client;
	private final Cluster cluster;

	public void createTopic(String name, int numPartitions, @Nullable Integer replicationFactor,
			Map<String, String> configs) {
		var newTopic = new NewTopic(name, Optional.of(numPartitions),
				Optional.ofNullable(replicationFactor).map(Integer::shortValue)).configs(configs);
		client.createTopics(List.of(newTopic)).all();
	}

	public Set<String> listTopics(boolean listInternal) throws InterruptedException, ExecutionException {
		return client.listTopics(new ListTopicsOptions().listInternal(listInternal)).names().get();
	}

	public void deleteTopic(String topicName) throws InterruptedException, ExecutionException {
		client.deleteTopics(List.of(topicName)).all().get();
	}
	
	public Map<String, KafkaFuture<TopicDescription>> getTopicDescription(boolean listInternal) throws InterruptedException, ExecutionException {
		return client.describeTopics(listTopics(listInternal)).topicNameValues();
	}

}
