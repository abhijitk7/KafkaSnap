package com.cognizant.corelogic.kafkasnap.model;

import java.util.Properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * @author Arkit Das
 *
 */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KafkaCluster {

	private final String name;
	private final String version;
	private final String bootstrapServers;
	private final Properties properties;
	private final boolean readOnly;
}
