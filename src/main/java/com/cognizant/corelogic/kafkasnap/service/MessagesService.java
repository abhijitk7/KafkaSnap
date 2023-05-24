package com.cognizant.corelogic.kafkasnap.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
			/*
			 * List<TopicPartition> partitions =
			 * consumer.partitionsFor(topicName).stream().map(p -> new
			 * TopicPartition(topicName, p.partition())) .collect(Collectors.toList());
			 * consumer.assign(partitions);
			 * 
			 * // seek consumer.seekToBeginning(partitions);
			 * 
			 * 
			 * int numberOfMessagesReadSoFar = 0;
			 * 
			 * // poll for new data ConsumerRecords<Bytes, Bytes> records =
			 * consumer.poll(Duration.ofMillis(200));
			 */
			for(int i=0;i<consumer.assignment().size();i++) {
				ConsumerRecords<Bytes, Bytes> records =seekAndPoll(consumer, topicName, i, 0);
				int numberOfMessagesReadSoFar = 0;
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
	
	
	private ConsumerRecords<Bytes, Bytes> seekAndPoll(KafkaConsumer<Bytes, Bytes> consumer, String topic, int partition, long offset) {
	    
		TopicPartition tp = new TopicPartition(topic, partition);
	    consumer.assign(Collections.singleton(tp));
	    System.out.println("assignment:" + consumer.assignment()); // 这里是有分配到分区的
	    
	    // endOffset: the offset of the last successfully replicated message plus one
	    // if there has 5 messages, valid offsets are [0,1,2,3,4], endOffset is 4+1=5
	    Long endOffset = consumer.endOffsets(Collections.singleton(tp)).get(tp); 
	    if (offset < 0 || offset >= endOffset) {
	        System.out.println("offset is illegal");
	        return null;
	    } else {
	        consumer.seek(tp, offset);
	        ConsumerRecords<Bytes, Bytes> records = consumer.poll(Duration.ofMillis(100));
	        if(records.isEmpty()){
	            System.out.println("Not Found");
	            return null;
	        } else {
	            System.out.println("Found");
	            return records;
	        }
	    }
	}

}
