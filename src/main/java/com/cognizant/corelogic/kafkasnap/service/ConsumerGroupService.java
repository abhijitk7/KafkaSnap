package com.cognizant.corelogic.kafkasnap.service;

import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.BytesDeserializer;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.config.ClusterProperties.Cluster;

import lombok.AllArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@Service
@AllArgsConstructor
public class ConsumerGroupService {

	public KafkaConsumer<Bytes, Bytes> createConsumer(Cluster cluster, Map<String, Object> properties) {
		Properties props = new Properties();
		if(!Objects.isNull(cluster.getProperties())) 
			props.putAll(cluster.getProperties());
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, "kafka-ui-consumer-" + System.currentTimeMillis());
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, cluster.getBootstrapServers());
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, BytesDeserializer.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
		if(!Objects.isNull(properties))
			props.putAll(properties);

		return new KafkaConsumer<>(props);
	}

}
