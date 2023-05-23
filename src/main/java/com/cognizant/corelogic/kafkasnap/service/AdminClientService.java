package com.cognizant.corelogic.kafkasnap.service;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.config.ClusterProperties;
import com.cognizant.corelogic.kafkasnap.config.ClusterProperties.Cluster;
import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.AdminClientWrapper;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@Service
@AllArgsConstructor
public class AdminClientService {

	private static final int DEFAULT_CLIENT_TIMEOUT_MS = 30_000;
	private static final AtomicLong CLIENT_ID_SEQ = new AtomicLong();
	private final Map<String, AdminClientWrapper> adminClientCache = new ConcurrentHashMap<>();
	
	@Autowired
	private final ClusterProperties clusterProperties;

	public AdminClientWrapper get(String clusterName) throws ClusterNotFoundException {
		if(Objects.isNull(adminClientCache.get(clusterName))) {
			Cluster cluster = clusterProperties.getClusters()
					.stream()
					.filter(c -> clusterName.equals(c.getName()))
					.findFirst()
					.orElseThrow(() -> new ClusterNotFoundException("No cluster name found for [" + clusterName + "]"));
			adminClientCache.put(clusterName, createAdminClient(cluster));
		}
		
		return adminClientCache.get(clusterName);
	}
	private AdminClientWrapper createAdminClient(Cluster kafkaCluster) {
		Properties properties = new Properties();
		if(!Objects.isNull(kafkaCluster.getProperties()))
			properties.putAll(kafkaCluster.getProperties());
		properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster.getBootstrapServers());
		properties.putIfAbsent(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, DEFAULT_CLIENT_TIMEOUT_MS);
		properties.putIfAbsent(AdminClientConfig.CLIENT_ID_CONFIG,
				"kafka-ui-admin-" + Instant.now().getEpochSecond() + "-" + CLIENT_ID_SEQ.incrementAndGet());
		return AdminClientWrapper.builder().client(AdminClient.create(properties)).cluster(kafkaCluster).build();
	}
}
