package com.example.concurrency.threads;

import java.util.Random;

public class UserOrDaemonRunnable extends Random implements Runnable {

	private final String threadType;
	private final int MAX_ITERATIONS = 1000000000;

	public UserOrDaemonRunnable(String type) {
		threadType = type;
	}

	private int computeGCD(int number1, int number2) {
		if (number2 == 0) {
			return number1;
		}
		return computeGCD(number2, number1 % number2);
	}

	public void run() {
		final String threadString = "UserOrDaemonRunnable with " + threadType + " thread id "
				+ Thread.currentThread();
		System.out.println("Entering run()");
		try {
			for (int i = 0; i < MAX_ITERATIONS; i++) {
				int number1 = nextInt();
				int number2 = nextInt();
				if (i % MAX_ITERATIONS / 10 == 0) {
					System.out.println("In run()" + threadString
							+ " the GCD of " + number1 + " and " + number2
							+ " is " + computeGCD(number1, number2));
				}
			}
		} finally {
			System.out.println("UserOrDaemonRunnable Leaving run() " + threadString);
		}
	}

}
