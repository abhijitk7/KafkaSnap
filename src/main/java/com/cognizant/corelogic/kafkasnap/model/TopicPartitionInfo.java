package com.cognizant.corelogic.kafkasnap.model;

import java.util.List;

import org.apache.kafka.common.Node;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TopicPartitionInfo {

	private final int partition;
    private final Node leader;
    private final List<Node> replicas;
    private final List<Node> isr;
}
