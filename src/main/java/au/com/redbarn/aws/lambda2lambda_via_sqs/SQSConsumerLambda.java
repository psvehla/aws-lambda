/**
 * 
 */
package au.com.redbarn.aws.lambda2lambda_via_sqs;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;

import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class SQSConsumerLambda implements RequestHandler<SQSEvent, String> {
	
	/* (non-Javadoc)
	 * @see com.amazonaws.services.lambda.runtime.RequestHandler#handleRequest(java.lang.Object, com.amazonaws.services.lambda.runtime.Context)
	 */
	@Override
	public String handleRequest(SQSEvent input, Context context) {
		
		log.info("message received");
		
		List<SQSMessage> records = input.getRecords();
		
		for (SQSMessage record : records) {
			log.info(record.getBody());
		}
		
		return "Ok";
	}
}
