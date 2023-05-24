package com.cognizant.corelogic.kafkasnap.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.stereotype.Service;

import com.cognizant.corelogic.kafkasnap.enums.SeekDirectionEnum;
import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.TopicMessages;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author Arkit Das
 *
 */

@Slf4j
@Service
@AllArgsConstructor
public class MessagesService {

	private final AdminClientService adminClientService;
	private final ConsumerGroupService consumerGroupService;
	
	public List<TopicMessages> loadTopicMessage(String clusterName, String topicName, SeekDirectionEnum seekDirection,
			int limit) throws ClusterNotFoundException {
		List<TopicMessages> messages = new ArrayList<TopicMessages>();
		KafkaConsumer<Bytes, Bytes> consumer = null;
		try {
			consumer = consumerGroupService.createConsumer(adminClientService.get(clusterName).getCluster(), null);
			List<TopicPartition> partitions = consumer.partitionsFor(topicName).stream().map(p -> new TopicPartition(topicName, p.partition()))
				    .collect(Collectors.toList());
			consumer.assign(partitions);
			
			// seek
	        consumer.seekToBeginning(partitions);
	        

	        int numberOfMessagesReadSoFar = 0;
	        
	        // poll for new data
	        ConsumerRecords<Bytes, Bytes> records = consumer.poll(Duration.ofMillis(200));
	        
	        System.out.println(records.count());

	        for (ConsumerRecord<Bytes, Bytes> record : records){
	            numberOfMessagesReadSoFar += 1;
	            log.info("Key: " + record.key() + ", Value: " + record.value());
	            log.info("Partition: " + record.partition() + ", Offset:" + record.offset());
	            messages.add(TopicMessages.builder()
	            		.key(Objects.isNull(record.key())?null:new String(record.key().get(), StandardCharsets.UTF_8))
	            		.value(Objects.isNull(record.value())?null:new String(record.value().get(), StandardCharsets.UTF_8))
	            		.headers(record.headers())
	            		.partition(record.partition())
	            		.offset(record.offset())
	            		.timestamp(record.timestamp())
	            		.build());
	            if (limit >0 && numberOfMessagesReadSoFar >= limit){
	                break; // to exit the for loop
	            }
	        }
		}catch(Exception e) {
			log.error("Error occured while consumeing messgae due to ",e);
		}finally {
			if(!Objects.isNull(consumer))
				consumer.close();
		}
        log.info("Exiting the application");
        return messages;
	}

}
