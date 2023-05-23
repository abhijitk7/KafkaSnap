package com.cognizant.corelogic.kafkasnap.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TopicDetailsInfo {
	private String name;
	private List<TopicPartitionInfo> partitions;
}
