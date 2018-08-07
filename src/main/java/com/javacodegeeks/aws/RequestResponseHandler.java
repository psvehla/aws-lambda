/**
 * 
 */
package com.javacodegeeks.aws;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author psveh
 *
 */
public class RequestResponseHandler {
	
	private static final Logger LOGGER = LogManager.getLogger(RequestResponseHandler.class);
	
	public static class Order {
		private String productId;
		private String amount;
		
		public String getProductId() {
			return productId;
		}
		
		public void setProductId(String productId) {
			this.productId = productId;
		}
		
		public String getAmount() {
			return amount;
		}
		
		public void setAmount(String amount) {
			this.amount = amount;
		}
	}
	
	public static class Response {
		private String timestamp;
		private String state;
		
		public String getTimestamp() {
			return timestamp;
		}
		
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		
		public String getState() {
			return state;
		}
		
		public void setState(String state) {
			this.state = state;
		}
	}
	
	public Response handleRequest(Order order) {
		LOGGER.info("Received order for product " + order.getProductId() + ": amount =" + order.getAmount());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Response response = new Response();
		response.setTimestamp(sdf.format(new Date()));
		response.setState("Shipped");
		
		return response;
	}
}
