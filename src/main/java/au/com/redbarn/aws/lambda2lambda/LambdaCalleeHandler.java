/**
 * 
 */
package au.com.redbarn.aws.lambda2lambda;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * @author peter
 *
 */
public class LambdaCalleeHandler {
	
	@Data
	public static class Request {
		String name;
	}
	
	@Data
	public static class Response {
		String message;
	}
	
	public Response handleRequest(Request req) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Response res = new Response();
		res.setMessage("Hello, " + req.getName() + " it's " + sdf.format(new Date()));
		return res;
	}
}
