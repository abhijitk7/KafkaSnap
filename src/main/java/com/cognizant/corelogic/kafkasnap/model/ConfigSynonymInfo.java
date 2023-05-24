package com.cognizant.corelogic.kafkasnap.model;

import com.cognizant.corelogic.kafkasnap.enums.ConfigSourceEnum;

import lombok.Builder;
import lombok.Getter;

/**
 * 
 * @author Arkit Das
 *
 */
@Builder
@Getter
public class ConfigSynonymInfo {

  private String name;

  private String value;

  private ConfigSourceEnum source;

  
}

