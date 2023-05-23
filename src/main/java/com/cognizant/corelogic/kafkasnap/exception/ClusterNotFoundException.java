package com.cognizant.corelogic.kafkasnap.exception;

/**
 * 
 * @author Arkit Das
 *
 */
public class ClusterNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5699320312705482623L;

	public ClusterNotFoundException(String message) {
        super(message);
    }

}
