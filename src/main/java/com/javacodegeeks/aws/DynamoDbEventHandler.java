/**
 * 
 */
package com.javacodegeeks.aws;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class DynamoDbEventHandler implements RequestHandler<DynamodbEvent, String> {

	/* (non-Javadoc)
	 * @see com.amazonaws.services.lambda.runtime.RequestHandler#handleRequest(java.lang.Object, com.amazonaws.services.lambda.runtime.Context)
	 */
	@Override
	public String handleRequest(DynamodbEvent dynamodbEvent, Context context) {
		List<DynamodbEvent.DynamodbStreamRecord> records = dynamodbEvent.getRecords();
		
		if (records != null) {
			for (DynamodbEvent.DynamodbStreamRecord record : records) {
				StringBuilder sb = new StringBuilder();
				sb.append("eventName=").append(record.getEventName());
				StreamRecord dynamodb = record.getDynamodb();
				
				if (dynamodb != null) {
					sb.append(";keys=");
					appendMap(sb, dynamodb.getKeys());
					sb.append(";oldImage=");
					appendMap(sb, dynamodb.getOldImage());
					sb.append(";newImage=");
					appendMap(sb, dynamodb.getNewImage());
				}
				
				log.info("Record: " + sb.toString());
			}
		}
		return "OK";
	}

	/**
	 * @param sb
	 * @param keys
	 */
	private void appendMap(StringBuilder sb, Map<String, AttributeValue> map) {
		if (map != null) {
			int count = 0;
			
			for (Map.Entry<String, AttributeValue> entry : map.entrySet()) {
				if (count > 0) {
					sb.append(", ");
				}
				
				sb.append(entry.getKey()).append("/").append(entry.getValue());
				count++;
			}
		}
	}

}
