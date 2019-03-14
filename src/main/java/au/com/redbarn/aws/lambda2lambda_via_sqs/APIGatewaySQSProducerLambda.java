/**
 * 
 */
package au.com.redbarn.aws.lambda2lambda_via_sqs;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class APIGatewaySQSProducerLambda {

	private static final String queueName = "LAMBDA_TO_LAMBDA_VIA_SQS_DEMO";
	private static final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
	private static String theQueueUrl;
	
	static {
        // List all queues.
        log.info("Listing all queues in your account.");
        
        // select the queue we're after
        for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
        	log.info("  QueueUrl: " + queueUrl);
        	
        	if (queueUrl.contains(queueName)) {
        		theQueueUrl = queueUrl;
        	}
        }		
	}
	
	@Data
	public static class Request {
		private String message;
	}
	
	// TODO: either remove this or get handleRequest() to return a response object
	@Data
	public static class Response {
		String message;
	}
	
	public String handleRequest(Request req) {
        /*
         * Create a new instance of the builder with all defaults (credentials
         * and region) set automatically. For more information, see
         * Creating Service Clients in the AWS SDK for Java Developer Guide.
         */
        log.info("message = " + req.getMessage());
        sqs.sendMessage(new SendMessageRequest(theQueueUrl, req.getMessage()));
        return "Ok";
	}
}
