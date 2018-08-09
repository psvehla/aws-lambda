/**
 * 
 */
package com.javacodegeeks.aws;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class RequestResponseHandler {

	@Data
	public static class Order {
		private String productId;
		private String amount;
	}

	@Data
	public static class Response {
		private String timestamp;
		private String state;
	}
	
	public Response handleRequest(Order order) {
		log.info("Received order for product " + order.getProductId() + ": amount =" + order.getAmount());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Response response = new Response();
		response.setTimestamp(sdf.format(new Date()));
		response.setState("Shipped");
		
		return response;
	}
}
