/**
 * 
 */
package au.com.redbarn.aws.lambda2lambda_via_sqs;

import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;

import lombok.Data;

/**
 * @author peter
 *
 */
public class APIGatewaySQSProducerLambda {
	
	@Data
	public static class Request {
		String name;
	}
	
	@Data
	public static class Response {
		String message;
	}
	
	public interface LambdaCalleeService {
		@LambdaFunction(functionName="lambdaCalleeHandler")
		Response getPersonalisedDate(Request req);
	}
		
	public String handleRequest() {
		final LambdaCalleeService lambdaCalleeService = LambdaInvokerFactory.builder().lambdaClient(AWSLambdaClientBuilder.defaultClient()).build(LambdaCalleeService.class);
		
		Request req = new Request();
		req.setName("Peter");
		
		return lambdaCalleeService.getPersonalisedDate(req).getMessage();
	}
}
