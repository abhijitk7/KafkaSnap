package com.cognizant.corelogic.kafkasnap.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.enums.SeekDirectionEnum;
import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.service.MessagesService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@RestController
@RequiredArgsConstructor
public class MessageController {
	
	@Autowired
	private MessagesService messageService;
	
	
	@GetMapping(value = "/{clusterName}/topic/{topicName}")
	public ResponseEntity<?> loadMessage(@PathVariable(value="clusterName") String clusterName,
			@PathVariable(value="topicName") String topicName,
			@RequestParam(value="limit") int limit) {
		try {
			return ResponseEntity.ok(messageService.loadTopicMessage(clusterName, topicName, SeekDirectionEnum.FORWARD, limit));
		} catch (ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
