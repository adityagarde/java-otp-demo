package com.aditya.test;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import javax.crypto.KeyGenerator;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;

public class OTPTest {

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException {

		final TimeBasedOneTimePasswordGenerator totp = new TimeBasedOneTimePasswordGenerator();

		final Key key;
		{
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());

			// Key length should match the length of the HMAC output
			// 160 bits for SHA-1, 256 bits for SHA-256, and 512 bits for SHA-512
			keyGenerator.init(160);

			key = keyGenerator.generateKey();
		}

		System.out.println(key.getFormat() + " " + key.toString());
		
		// KEY + COUNTER or KEY + TIMESTAMP = 6 digit OTP
		
		// 1. Time based OTP generation
		final Instant now = Instant.now();
		final Instant later = now.plus(totp.getTimeStep());

		System.out.format("Current password (TOTP) : %06d\n", totp.generateOneTimePassword(key, now));

		System.out.format("Future password (TOTP) :  %06d\n", totp.generateOneTimePassword(key, later));

		// 2. Counter based OTP generation
		// Hard-coded counter for Demo purpose - will be dynamic in production.
		int counter = 123456;
		int counterBasedOTP = totp.generateOneTimePassword(key, counter);
		System.out.println("HOTP : " + counterBasedOTP);

	}

}
