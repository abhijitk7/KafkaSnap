package com.cognizant.corelogic.kafkasnap.api;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cognizant.corelogic.kafkasnap.exception.ClusterNotFoundException;
import com.cognizant.corelogic.kafkasnap.model.TopicCreationDTO;
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
	public ResponseEntity<?> getTopicDetails(@PathVariable(value="clusterName") String clusterName) {
		try {
			return ResponseEntity.ok(topicService.getTopicDetails(clusterName));
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@GetMapping(value = "/{clusterName}/topic/config")
	public ResponseEntity<?> getTopicConfigs(@PathVariable(value="clusterName") String clusterName) {
		try {
			return ResponseEntity.ok(topicService.getTopicConfigs(clusterName));
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@PostMapping(value= "/{clusterName}/topic")
	public ResponseEntity<?> createTopic(@PathVariable(value="clusterName")  String clusterName, TopicCreationDTO topicCreation) {
		try {
			return ResponseEntity.ok(topicService.createTopic(clusterName, topicCreation));
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping(value= "/{clusterName}/topic/{topicName}")
	public ResponseEntity<?> deleteTopic(@PathVariable(value="clusterName")  String clusterName,@PathVariable(value="topicName") String topicName) {
		try {
			return ResponseEntity.ok(topicService.deleteTopic(clusterName, topicName));
		} catch (InterruptedException | ExecutionException | ClusterNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
