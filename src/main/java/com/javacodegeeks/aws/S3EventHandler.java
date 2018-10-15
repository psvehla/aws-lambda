/**
 * 
 */
package com.javacodegeeks.aws;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import lombok.extern.log4j.Log4j2;

/**
 * @author peter
 *
 */
@Log4j2
public class S3EventHandler implements RequestHandler<S3Event, String> {
	
	static final int THUMBNAIL_WIDTH = 512;
	static final int THUMBNAIL_HEIGHT = 512;

	/* (non-Javadoc)
	 * @see com.amazonaws.services.lambda.runtime.RequestHandler#handleRequest(java.lang.Object, com.amazonaws.services.lambda.runtime.Context)
	 */
	@Override
	public String handleRequest(S3Event s3Event, Context context) {
		log.info("Invoked " + S3EventHandler.class.getSimpleName() + " with " + (s3Event.getRecords() == null ? 0 : s3Event.getRecords().size()) + " records.");
		List<S3EventNotification.S3EventNotificationRecord> records = s3Event.getRecords();
		
		if (records != null) {
			for (S3EventNotification.S3EventNotificationRecord record : records) {
				String eventName = record.getEventName();
				
				if ("ObjectCreated:Put".equals(eventName)) {
					S3EventNotification.S3Entity s3 = record.getS3();
					
					if (s3 != null) {
						String bucketName = s3.getBucket().getName();
						String key = s3.getObject().getKey();
						
						if (key.endsWith(".jpg") || key.endsWith(".jpeg")) {
							AmazonS3 client = AmazonS3ClientBuilder.defaultClient();
							GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
							S3Object s3Object = client.getObject(getObjectRequest);
							S3ObjectInputStream inputStream = s3Object.getObjectContent();
							
							try {
								byte[] bytes = IOUtils.toByteArray(inputStream);
								byte[] scaledBytes = scaleImage(bytes);
								uploadThumbnailImage(bucketName, key, client, scaledBytes);
								log.info("Successfully created thumbnail image.");
							}
							catch (IOException e) {
								log.error("Failed to get content of S3 object (bucket = " + bucketName + ", key = " + key + "): " + e.getMessage(), e);
							}
						}
						else {
							log.debug("Key does not end with .jpg or .jpeg");
						}
					}
					else {
						log.debug("No S3 object in record.");
					}
				}
				else {
					log.debug("Ignoring record (not a put request).");
				}
			}
		}
		return "OK";
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException 
	 */
	private byte[] scaleImage(byte[] bytes) throws IOException {
		BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
		int width = img.getWidth();
		int height = img.getHeight();
		
		if (width > 0 && height > 0) {
			int newWidth = THUMBNAIL_WIDTH;
			int newHeight = THUMBNAIL_HEIGHT;
			
			if (width > THUMBNAIL_WIDTH || height > THUMBNAIL_HEIGHT) {
				if (width >= height) {
					newWidth = THUMBNAIL_WIDTH;
					newHeight = (int)((THUMBNAIL_WIDTH / (double)width) * (double)height);
				}
				else {
					newHeight = THUMBNAIL_HEIGHT;
					newWidth = (int)((THUMBNAIL_WIDTH / (double)height) * (double)width);
				}
			}
			Image scaledInstance = img.getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT);
			BufferedImage thumbnail = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_3BYTE_BGR);
			Graphics2D graphics = thumbnail.createGraphics();
			graphics.drawImage(scaledInstance, 0, 0, null);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ImageIO.write(thumbnail, "jpg", outputStream);
			return outputStream.toByteArray();
		}
		else {
			return bytes;
		}
	}


	/**
	 * @param bucketName
	 * @param key
	 * @param client
	 * @param scaledBytes
	 */
	private void uploadThumbnailImage(String bucketName, String key, AmazonS3 client, byte[] scaledBytes) {
		int lastIndexOfDot = key.lastIndexOf('.');
		String newKey = key.substring(0, lastIndexOfDot) + "_thumb" + key.substring(lastIndexOfDot + 1);
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(scaledBytes.length);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, newKey, new ByteArrayInputStream(scaledBytes), metadata);
		client.putObject(putObjectRequest);
	}
}
