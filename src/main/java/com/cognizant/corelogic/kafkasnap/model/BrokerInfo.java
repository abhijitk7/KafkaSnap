package com.cognizant.corelogic.kafkasnap.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
/**
 * 
 * @author Arkit Das
 *
 */
@Builder
@Getter
public class BrokerInfo {
	private Integer id;
	private String host;
	private Integer port;
	private BigDecimal bytesInPerSec;
	private BigDecimal bytesOutPerSec;
	private Integer partitionsLeader;
	private Integer partitions;
	private Integer inSyncPartitions;
	private BigDecimal partitionsSkew;
	private BigDecimal leadersSkew;
}
