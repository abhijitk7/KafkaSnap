package com.cognizant.corelogic.kafkasnap.model;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ConfigEntry;
import org.apache.kafka.clients.admin.DescribeClusterOptions;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.clients.admin.DescribeConfigsOptions;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.acl.AclOperation;
import org.apache.kafka.common.config.ConfigResource;
import org.apache.kafka.common.errors.TopicAuthorizationException;
import org.apache.kafka.common.errors.UnknownTopicOrPartitionException;
import org.springframework.lang.Nullable;

import com.cognizant.corelogic.kafkasnap.config.ClusterProperties.Cluster;

import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import reactor.core.publisher.Mono;

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
	private String version = "1.0-UNKNOWN";
	private boolean topicDeletionEnabled = true;

	public void createTopic(String name, int numPartitions, @Nullable Integer replicationFactor,
			Map<String, String> configs) {
		var newTopic = new NewTopic(name, Optional.of(numPartitions),
				Optional.ofNullable(replicationFactor).map(Integer::shortValue)).configs(configs);
		client.createTopics(List.of(newTopic)).all();
	}

	public Set<String> listTopics(boolean listInternal) throws InterruptedException, ExecutionException {
		return client.listTopics(new ListTopicsOptions().listInternal(listInternal)).names().get();
	}


	public Map<String, KafkaFuture<TopicDescription>> getTopicDescription(boolean listInternal)
			throws InterruptedException, ExecutionException {
		return client.describeTopics(listTopics(listInternal)).topicNameValues();
	}

	public ClusterDescription getClusterDescription() throws InterruptedException, ExecutionException {
		DescribeClusterResult result = client
				.describeCluster(new DescribeClusterOptions().includeAuthorizedOperations(true));
		return new ClusterDescription(result.controller().get(), result.clusterId().get(), result.nodes().get(),
				result.authorizedOperations().get());

	}

	public Map<Integer, List<ConfigEntry>> loadBrokersConfig(List<Integer> brokerIds)
			throws InterruptedException, ExecutionException {
		List<ConfigResource> resources = brokerIds.stream()
				.map(brokerId -> new ConfigResource(ConfigResource.Type.BROKER, Integer.toString(brokerId)))
				.collect(toList());
		return client.describeConfigs(resources).all().get().entrySet().stream().collect(Collectors.toMap(
				c -> Integer.valueOf(c.getKey().name()), c -> new ArrayList<ConfigEntry>(c.getValue().entries())));

	}

	public void populateVersionAndTopicDeletionEnabled() throws InterruptedException, ExecutionException {
		int controllerId = getClusterDescription().getController().id();
		Map<Integer, List<ConfigEntry>> configMaps = loadBrokersConfig(List.of(controllerId));
		if (!configMaps.isEmpty()) {

			for (ConfigEntry entry : configMaps.get(controllerId)) {
				if (entry.name().contains("inter.broker.protocol.version")) {
					version = entry.value();
				}
				if (entry.name().equals("delete.topic.enable")) {
					topicDeletionEnabled = Boolean.parseBoolean(entry.value());
				}
			}
		}

	}

	public Map<String, List<ConfigEntry>> getTopicsConfig() throws InterruptedException, ExecutionException {
		return getTopicsConfig(listTopics(true), false);
	}

	public Map<String, List<ConfigEntry>> getTopicsConfig(Collection<String> topicNames, boolean includeDoc) {
		Map<String, List<ConfigEntry>> configMap = new HashMap<String, List<ConfigEntry>>();
		List<ConfigResource> resources = topicNames.stream()
				.map(topicName -> new ConfigResource(ConfigResource.Type.TOPIC, topicName)).collect(toList());

		var values = client.describeConfigs(resources,
				new DescribeConfigsOptions().includeSynonyms(true).includeDocumentation(includeDoc)).values();
		values.entrySet().stream().forEach(value -> {
			try {
				configMap.put(value.getKey().name(), List.copyOf(value.getValue().get().entries()));
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return configMap;
	}
	
	public Map<String, TopicDescription> describeTopics() throws InterruptedException, ExecutionException {
		Map<String, TopicDescription> configMap = new HashMap<String, TopicDescription>();
		client.describeTopics(listTopics(true)).topicNameValues().entrySet().stream().forEach(value -> {
			try {
				configMap.put(value.getKey(), value.getValue().get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		return configMap;
	}
	
	public void creatTopic(String clusterName, TopicCreationDTO topicCreation) throws InterruptedException, ExecutionException {
		var newTopic = new NewTopic(topicCreation.getName(), Optional.of(topicCreation.getPartitions()),
				Optional.ofNullable(topicCreation.getReplicationFactor()).map(Integer::shortValue))
						.configs(topicCreation.getConfigs());
		client.createTopics(List.of(newTopic)).all().get();
	}
	
	public void deleteTopic(String topicName) throws InterruptedException, ExecutionException {
		client.deleteTopics(List.of(topicName)).all().get();
	}
		

	@Value
	public static class ClusterDescription {
		@Nullable
		Node controller;
		String clusterId;
		Collection<Node> nodes;
		@Nullable // null, if ACL is disabled
		Set<AclOperation> authorizedOperations;
	}


	

}
