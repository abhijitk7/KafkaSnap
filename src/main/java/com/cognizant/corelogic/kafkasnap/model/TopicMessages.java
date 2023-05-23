package com.cognizant.corelogic.kafkasnap.model;

import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Arkit Das
 *
 */
@Builder
@Getter
public class TopicMessages {
	
	private Object key;
	private Object value;
	private Object headers;
	private int partition;
	private long offset;
	private long timestamp;

}
