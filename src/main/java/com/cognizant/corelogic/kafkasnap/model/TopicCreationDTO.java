package com.cognizant.corelogic.kafkasnap.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Arkit Das
 *
 */

@Builder
@Getter
public class TopicCreationDTO {

  private String name;

  private Integer partitions;

  private Integer replicationFactor;

  private Map<String, String> configs = new HashMap<>();
}

