package com.zerogravity.myapp.common.security;

import org.springframework.stereotype.Component;

/**
 * Snowflake ID Generator Service
 * Generates unique, sortable, non-sequential IDs suitable for distributed systems
 *
 * Structure (64-bit):
 * - 41 bits: timestamp (millisecond precision)
 * - 10 bits: machine/worker ID
 * - 12 bits: sequence number
 *
 * Total: 64 bits (fits in BIGINT)
 * Max sequence per millisecond: 4096
 *
 * No external dependencies required
 */
@Component
public class SnowflakeIdService {
	private static final long EPOCH = 1609459200000L; // 2021-01-01 00:00:00
	private static final long MACHINE_ID_BITS = 10;
	private static final long SEQUENCE_BITS = 12;

	private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
	private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;

	private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1; // 4095
	private static final long MACHINE_ID_MASK = (1L << MACHINE_ID_BITS) - 1; // 1023

	private final long machineId;
	private long lastTimestamp = -1L;
	private long sequence = 0L;

	public SnowflakeIdService() {
		// Get machine ID from environment or use default 1
		String machineIdStr = System.getenv().getOrDefault("MACHINE_ID", "1");
		try {
			this.machineId = Long.parseLong(machineIdStr) & MACHINE_ID_MASK;
		} catch (NumberFormatException e) {
			throw new RuntimeException("Invalid MACHINE_ID environment variable: " + machineIdStr, e);
		}
	}

	/**
	 * Generate a unique Snowflake ID
	 * Used for users, emotion records, AI analysis, and other entities
	 *
	 * @return unique Snowflake ID as BIGINT
	 */
	public synchronized long generateId() {
		long timestamp = System.currentTimeMillis();

		if (timestamp < lastTimestamp) {
			throw new RuntimeException(
				"Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds"
			);
		}

		if (timestamp == lastTimestamp) {
			sequence = (sequence + 1) & SEQUENCE_MASK;
			if (sequence == 0) {
				// Sequence overflow, wait for next millisecond
				timestamp = waitUntilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}

		lastTimestamp = timestamp;

		// Combine: timestamp (41 bits) | machineId (10 bits) | sequence (12 bits)
		return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
			| (machineId << MACHINE_ID_SHIFT)
			| sequence;
	}

	private long waitUntilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}
}
