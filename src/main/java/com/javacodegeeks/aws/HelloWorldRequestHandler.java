/**
 * 
 */
package com.javacodegeeks.aws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author peter
 *
 */
public class HelloWorldRequestHandler implements RequestHandler<String, String> {
	
	private static final Logger LOGGER = LogManager.getLogger(HelloWorldRequestHandler.class);

	@Override
	public String handleRequest(String s, Context context) {
		
		LOGGER.info("awsRequestId=" + context.getAwsRequestId());
		LOGGER.info("functionName=" + context.getFunctionName());
		LOGGER.info("functionVersion=" + context.getFunctionVersion());
		LOGGER.info("memoryLimitInMB=" + context.getMemoryLimitInMB());
		LOGGER.info("remainingTimeInMillis=" + context.getRemainingTimeInMillis());
		return "My first request handler:" + s;
	}
}
