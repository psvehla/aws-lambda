/**
 * 
 */
package com.javacodegeeks.aws;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class HelloWorldRequestHandler implements RequestHandler<String, String> {

	@Override
	public String handleRequest(String s, Context context) {
		
		log.info("awsRequestId=" + context.getAwsRequestId());
		log.info("functionName=" + context.getFunctionName());
		log.info("functionVersion=" + context.getFunctionVersion());
		log.info("memoryLimitInMB=" + context.getMemoryLimitInMB());
		log.info("remainingTimeInMillis=" + context.getRemainingTimeInMillis());
		return "My first request handler:" + s;
	}
}
