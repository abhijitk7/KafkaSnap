package com.cognizant.corelogic.kafkasnap.api;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.service.TopicService;

import lombok.RequiredArgsConstructor;

/**
 * 
 * @author Arkit Das
 *
 */
@RestController
@RequiredArgsConstructor
public class TopicController {

	@Autowired
	private TopicService topicService;
	
	@GetMapping(value = "/{clusterName}/topic/all")
	public ResponseEntity<?> connect(@PathVariable(value="clusterName") String clusterName) {
		try {
			return ResponseEntity.ok(topicService.getTopicConfigs(clusterName));
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
