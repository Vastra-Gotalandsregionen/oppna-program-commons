/**
 * Copyright 2009 V�stra G�talandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 */
package se.vgr.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * Utilities for dealing with urls.
 * 
 * @author Jon Stevens
 */
public class HTTPUtils {
	/**
	 * Hides the irritating declared exception.
	 */
	public static String encode(String value) {
		try {
			return URLEncoder.encode(value, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Hides the irritating declared exception.
	 * 
	 * @return null if there is an IllegalArgumentException
	 * @throws RuntimeException
	 *             if there is an UnsupportedEncodingException
	 */
	public static String decode(String value) {
		try {
			return URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	/**
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static HttpResponse basicAuthRequest(String url, String username,
			String password, DefaultHttpClient client)
			throws HttpUtilsException {
		HttpGet get = new HttpGet(url);

		client.getCredentialsProvider().setCredentials(
				new AuthScope(null, 443),
				new UsernamePasswordCredentials(username, password));

		BasicHttpContext localcontext = new BasicHttpContext();

		// Generate BASIC scheme object and stick it to the local
		// execution context
		BasicScheme basicAuth = new BasicScheme();
		localcontext.setAttribute("preemptive-auth", basicAuth);

		// Add as the first request interceptor
		client.addRequestInterceptor(new PreemptiveAuth(), 0);
		HttpResponse response;
		try {
			response = client.execute(get, localcontext);
		} catch (ClientProtocolException e) {
			throw new HttpUtilsException("Invalid http protocol", e);
		} catch (IOException e) {
			throw new HttpUtilsException(e.getMessage(), e);
		}
		return response;
	}

	/** */
	public static HttpResponse makeRequest(String url, String token,
			DefaultHttpClient client) throws Exception {
		HttpGet get = new HttpGet(url);
		get.addHeader("X-TrackerToken", token);
		return client.execute(get);
	}

	/** */
	public static HttpResponse makePostXML(String url, String token,
			DefaultHttpClient client, String xml) throws Exception {
		HttpPost post = new HttpPost(url);

		StringEntity e = new StringEntity(xml, "utf-8");
		e.setContentType("application/xml");
		post.addHeader("X-TrackerToken", token);
		post.addHeader("Content-type", "application/xml");
		post.setEntity(e);

		return client.execute(post);
	}

	/** */
	/**
	 * @param url
	 * @param token
	 * @param httpclient
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse makePostAttachments(String url, String token,
			DefaultHttpClient httpclient, List<File> list) throws Exception {
		
        
        //httpclient.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, true);
		
		httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost(url);
		
		httppost.addHeader("X-TrackerToken", token);
		
		MultipartEntity mpEntity = new MultipartEntity();//HttpMultipartMode.BROWSER_COMPATIBLE);
		
		//FileEntity mPEntity = new FileEntity(list.get(i), "binary/octet-stream"); 
		for (int i = 0; i < list.size(); i++) {
			System.out.println("Adding file:"+list.get(i).getName());
			
			ContentBody cbFile = new FileBody(list.get(i),
					"binary/octet-stream");
			mpEntity.addPart(list.get(i).getName(), cbFile);
			

		}
		httppost.setEntity(mpEntity);
		HttpResponse response = httpclient.execute(httppost);
		//System.out.println(response.getStatusLine());
//		HttpEntity resEntity = response.getEntity();
//
//		
//		if (resEntity != null) {
//			System.out.println(EntityUtils.toString(resEntity));
//		}
//		if (resEntity != null) {
//			resEntity.consumeContent();
//		}
		return response;
	}

	/** */
	static class PreemptiveAuth implements HttpRequestInterceptor {
		public void process(final HttpRequest request, final HttpContext context)
				throws HttpException, IOException {

			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);

			// If no auth scheme avaialble yet, try to initialize it
			// preemptively
			if (authState.getAuthScheme() == null) {
				AuthScheme authScheme = (AuthScheme) context
						.getAttribute("preemptive-auth");
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
				if (authScheme != null) {
					Credentials creds = credsProvider
							.getCredentials(new AuthScope(targetHost
									.getHostName(), targetHost.getPort()));
					if (creds == null) {
						throw new HttpException(
								"No credentials for preemptive authentication");
					}
					authState.setAuthScheme(authScheme);
					authState.setCredentials(creds);
				}
			}
		}
	}
}
