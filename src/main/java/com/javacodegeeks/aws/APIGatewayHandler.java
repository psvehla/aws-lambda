/**
 * 
 */
package com.javacodegeeks.aws;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author psveh
 *
 */
public class APIGatewayHandler {
	
	public String handleRequest() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return sdf.format(new Date());
	}
}
