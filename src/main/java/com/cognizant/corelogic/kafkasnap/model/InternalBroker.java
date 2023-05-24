package com.cognizant.corelogic.kafkasnap.model;

import java.math.BigDecimal;

import org.apache.kafka.common.Node;
import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class InternalBroker {

  private final Integer id;
  private final String host;
  private final Integer port;
  private final @Nullable Integer partitionsLeader;
  private final @Nullable Integer partitions;
  private final @Nullable Integer inSyncPartitions;
  private final @Nullable BigDecimal leadersSkew;
  private final @Nullable BigDecimal partitionsSkew;

  public InternalBroker(Node node,
                        PartitionDistributionStats partitionDistribution,
                        Statistics statistics) {
    this.id = node.id();
    this.host = node.host();
    this.port = node.port();
    this.partitionsLeader = partitionDistribution.getPartitionLeaders().get(node);
    this.partitions = partitionDistribution.getPartitionsCount().get(node);
    this.inSyncPartitions = partitionDistribution.getInSyncPartitions().get(node);
    this.leadersSkew = partitionDistribution.leadersSkew(node);
    this.partitionsSkew = partitionDistribution.partitionsSkew(node);
  }

}
