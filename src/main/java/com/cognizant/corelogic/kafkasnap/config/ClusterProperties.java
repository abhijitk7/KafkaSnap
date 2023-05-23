package com.cognizant.corelogic.kafkasnap.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 
 * @author Arkit Das
 *
 */
@Configuration
@ConfigurationProperties("kafka")
@Data
public class ClusterProperties {

  List<Cluster> clusters = new ArrayList<>();

  String internalTopicPrefix;

  Integer adminClientTimeout;


  @Data
  public static class Cluster {
    String name;
    String bootstrapServers;
    String schemaRegistry;
    String ksqldbServer;
    String zookeeper;
    Map<String, Object> properties;
    boolean readOnly = false;
  }


}
