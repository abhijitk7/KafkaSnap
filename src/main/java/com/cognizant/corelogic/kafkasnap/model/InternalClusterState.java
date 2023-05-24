package com.cognizant.corelogic.kafkasnap.model;

import java.util.Optional;

import org.apache.kafka.common.Node;

import lombok.Data;

@Data
public class InternalClusterState {
  private String name;
  private Integer topicCount;
  private Integer brokerCount;
  private Integer activeControllers;
  private Integer onlinePartitionCount;
  private Integer offlinePartitionCount;
  private Integer inSyncReplicasCount;
  private Integer outOfSyncReplicasCount;
  private Integer underReplicatedPartitionCount;
  private String version;
  private Boolean readOnly;

  public InternalClusterState(AdminClientWrapper client,String clusterName, Statistics statistics) {
    name = clusterName;
    topicCount = statistics.getTopicDescriptions().size();
    brokerCount = statistics.getClusterDescription().getNodes().size();
    activeControllers = Optional.ofNullable(statistics.getClusterDescription().getController())
        .map(Node::id)
        .orElse(null);
    version = statistics.getVersion();
    var partitionsStats = new PartitionsStats(statistics.getTopicDescriptions().values());
    onlinePartitionCount = partitionsStats.getOnlinePartitionCount();
    offlinePartitionCount = partitionsStats.getOfflinePartitionCount();
    inSyncReplicasCount = partitionsStats.getInSyncReplicasCount();
    outOfSyncReplicasCount = partitionsStats.getOutOfSyncReplicasCount();
    underReplicatedPartitionCount = partitionsStats.getUnderReplicatedPartitionCount();
  }

}
