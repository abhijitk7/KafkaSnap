package com.cognizant.corelogic.kafkasnap.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 
 * @author Arkit Das
 *
 */
public enum SeekDirectionEnum {

	FORWARD("FORWARD"),

	BACKWARD("BACKWARD"),

	TAILING("TAILING");

	private String value;

	SeekDirectionEnum(String value) {
		this.value = value;
	}

	@JsonValue
	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return String.valueOf(value);
	}

	@JsonCreator
	public static SeekDirectionEnum fromValue(String value) {
		for (SeekDirectionEnum b : SeekDirectionEnum.values()) {
			if (b.value.equals(value)) {
				return b;
			}
		}
		throw new IllegalArgumentException("Unexpected value '" + value + "'");
	}
}
