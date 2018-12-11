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
	// TODO: Change this to get the queue URL in a static initialiser then keep it in a static property, and see if it runs each time or not.
	
	public static final String queueName = "LAMBDA_TO_LAMBDA_VIA_SQS_DEMO";
	
	@Data
	public static class Request {
		// TODO: make this private
		// TODO: change this to message
		String name;
	}
	
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
        final AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String theQueueUrl = "";
        
        // List all queues.
        log.info("Listing all queues in your account.");
        
        for (final String queueUrl : sqs.listQueues().getQueueUrls()) {
        	log.info("  QueueUrl: " + queueUrl);
        	
        	if (queueUrl.contains(queueName)) {
        		theQueueUrl = queueUrl;
        	}
        }
        
        sqs.sendMessage(new SendMessageRequest(theQueueUrl, req.getName()));

        return "Ok";
	}
}
