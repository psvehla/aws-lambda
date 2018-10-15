/**
 * 
 */
package au.com.redbarn.aws.lambda2lambda;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author peter
 *
 */
public class APIGatewayCallingLambdaHandler {
	
	public String handleRequest() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return sdf.format(new Date());
	}
}
