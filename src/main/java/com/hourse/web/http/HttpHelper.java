package com.hourse.web.http;

import com.hourse.web.util.PropertiesUtils;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import javax.net.ssl.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class HttpHelper {
	public static int DEFAULT_CONNECTION_TIMEOUT = PropertiesUtils.get("sisap.webservice.connection.timeout", 60);
	
	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding) throws Exception {
		return HttpHelper.get(url, encoding, 90, null, null, null, null);
	}

	public static HttpResult get(String url, String encoding, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, 90, null, null, null, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 String localAddress) throws Exception {
		return HttpHelper
				.get(url, encoding, 90, null, null, localAddress, null);
	}

	public static HttpResult get(String url, String encoding,
                                 String localAddress, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, 90, null, null, localAddress,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut)
			throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, null, null, null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, null, null, proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 String localAddress) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, null, localAddress,
				null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 String localAddress, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, null, localAddress,
				proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies) throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, null, null, null);
	}

	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, null, null, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            页面编码方式，如果是null，程序将自动识别页面编码
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, String localAddress) throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, null, localAddress,
				null);
	}

	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, String localAddress, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, null, localAddress,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @return
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies) throws Exception {
		return HttpHelper
				.get(url, encoding, timeOut, cookies, null, null, null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, cookies, null, null,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, String localAddress) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, cookies, null,
				localAddress, null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, String localAddress, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, timeOut, cookies, null,
				localAddress, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param headers
	 *            设定的Http头信息
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 List<Header> headers) throws Exception {
		return HttpHelper.get(url, encoding, 90, null, headers, null, null);
	}

	public static HttpResult get(String url, String encoding,
                                 List<Header> headers, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, 90, null, headers, null, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param headers
	 *            设定的Http头信息
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 List<Header> headers, String localAddress) throws Exception {
		return HttpHelper.get(url, encoding, 90, null, headers, localAddress,
				null);
	}

	public static HttpResult get(String url, String encoding,
                                 List<Header> headers, String localAddress, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, 90, null, headers, localAddress,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param headers
	 *            设定的Http头信息
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 List<Header> headers) throws Exception {
		return HttpHelper
				.get(url, encoding, timeOut, null, headers, null, null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 List<Header> headers, HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, headers, null,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param headers
	 *            设定的Http头信息
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 List<Header> headers, String localAddress) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, headers,
				localAddress, null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 List<Header> headers, String localAddress, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, timeOut, null, headers,
				localAddress, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param headers
	 *            设定的Http头信息
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, List<Header> headers) throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, headers, null, null);
	}

	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, List<Header> headers, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, headers, null, proxy);
	}

	/**
	 * 用Get方式请求一个url，默认超时时间90秒
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param headers
	 *            设定的Http头信息
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, List<Header> headers, String localAddress)
			throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, headers,
				localAddress, null);
	}

	public static HttpResult get(String url, String encoding,
                                 CookieStore cookies, List<Header> headers, String localAddress,
                                 HttpHost proxy) throws Exception {
		return HttpHelper.get(url, encoding, 90, cookies, headers,
				localAddress, proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param headers
	 *            设定的Http头信息
	 * @return HttpResult
	 * @throws Exception
	 */
	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, List<Header> headers) throws Exception {
		return HttpHelper.get(url, encoding, timeOut, cookies, headers, null,
				null);
	}

	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, List<Header> headers, HttpHost proxy)
			throws Exception {
		return HttpHelper.get(url, encoding, timeOut, cookies, headers, null,
				proxy);
	}

	/**
	 * 用Get方式请求一个url
	 * 
	 * @param url
	 *            链接地址
	 * @param encoding
	 *            面编码方式，如果是null，程序将自动识别页面编码
	 * @param timeOut
	 *            请求超时时间，单位是秒
	 * @param cookies
	 *            传入这个链接地址的cookies
	 * @param headers
	 *            设定的Http头信息
	 * @param localAddress
	 *            指定本地的ip地址
	 * @return HttpResult
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public static HttpResult get(String url, String encoding, int timeOut,
                                 CookieStore cookies, List<Header> headers, String localAddress,
                                 HttpHost proxy) throws Exception {

		HttpClient httpclient = useTrustingTrustManager(new DefaultHttpClient(), url);
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeOut * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT * 1000);
		// httpclient.getParams().setIntParameter(HttpConnectionParams.SO_LINGER,0);
		if (proxy != null) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}

		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS, localBinding);
		}

		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}

		HttpGet httpget = new HttpGet(url);
		if (headers != null) {
			int size = headers.size();
			for (int i=0; i<size; i++) {
				httpget.addHeader(headers.get(i));
			}
		}
		httpget.addHeader("Connection", "close");
		((AbstractHttpClient) httpclient).setRedirectHandler(new RedirectHandler() {
					public URI getLocationURI(HttpResponse arg0,
							HttpContext arg1) throws ProtocolException {
						return null;
					}

					public boolean isRedirectRequested(HttpResponse arg0,
							HttpContext arg1) {
						return false;
					}
		});

		HttpResult httpResult = HttpResult.empty();
		HttpResponse response;
		try {
			response = httpclient.execute(httpget, localContext);
			httpResult = new HttpResult(localContext, response);
		} catch (Exception e) {
			httpget.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}

		return httpResult;
	}

	public static HttpResult post(String url, String data, String encoding)
			throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, null, null, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, null, null, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  String localAddress) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, null,localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  String localAddress, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, null,localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, null, null,null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, null, null,proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, String localAddress) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, null,localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, String localAddress, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, null,localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, null, null,null);
	}
	
	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, int timeout) throws Exception {
		return HttpHelper.post(url, data, encoding, timeout, cookies, null, null, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, null, null,proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, String localAddress) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, null,localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, String localAddress, HttpHost proxy)
			throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, null,localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, null,null, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, null,null, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, String localAddress)
			throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, null,localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, String localAddress,
                                  HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, null,localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, List<Header> headers) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, headers,null, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, List<Header> headers, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, headers,
				null, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  List<Header> headers) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, headers, null,
				null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  List<Header> headers, HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, null, headers, null,
				proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, List<Header> headers, String localAddress)
			throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, headers,
				localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, List<Header> headers, String localAddress,
                                  HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, null, headers,
				localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, List<Header> headers) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, headers, null,
				null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, List<Header> headers, HttpHost proxy)
			throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, headers, null,
				proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, List<Header> headers, String localAddress)
			throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, headers,
				localAddress, null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, List<Header> headers, String localAddress,
                                  HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, 90, cookies, headers,
				localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers)
			throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, headers,
				null);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  HttpHost proxy) throws Exception {
		return HttpHelper.post(url, data, encoding, timeOut, cookies, headers,
				proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  String localAddress, HttpHost proxy) throws Exception {
		byte[] postData = null;
		if (data != null) {
			postData = data.getBytes(encoding);
		}

		return HttpHelper.post(url, postData, encoding, timeOut, cookies,
				headers, localAddress, proxy);
	}

	public static HttpResult post(String url, String data, String encoding,
                                  CookieStore cookies, int timeout, boolean isJson) throws Exception {
		return HttpHelper.post(url, data, encoding, timeout, cookies, null, null,isJson, null);
	}
	public static HttpResult post(String url, String data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  String localAddress, boolean isJson, HttpHost proxy) throws Exception {
		byte[] postData = null;
		if (data != null) {
			postData = data.getBytes(encoding);
		}

		return HttpHelper.post(url, postData, encoding, timeOut, cookies,
				headers, localAddress,isJson, proxy);
	}

	public static HttpResult post(String url, byte[] data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  String localAddress, boolean isJson, HttpHost proxy) throws Exception {

		DefaultHttpClient httpclient = useTrustingTrustManager(
				new DefaultHttpClient(), url);
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeOut * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT * 1000);
		httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);//设置编码格式
		// httpclient.getParams().setIntParameter(HttpConnectionParams.SO_LINGER,0);
		if (proxy != null) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS,
					localBinding);
		}

		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}

		if(headers == null){
			headers = new ArrayList<Header>();
		}
//		Header header = new BasicHeader("User-Agent", UserAgent.getRandom());
//		headers.add(header);

		HttpPost httppost = new HttpPost(url);
		String contentType = null;
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				Header h = headers.get(i);
				if (h.getName().startsWith("$x-param") == false) {
					httppost.addHeader(h);
				}
				if ("Content-Type".equalsIgnoreCase(h.getName()) == true) {
					contentType = h.getValue();
				}
			}
		}

		//httppost.addHeader("Connection", "close");
		if(isJson){
			httppost.setHeader("Content-Type",
					"application/json");
		}else{
			if (contentType != null) {
				httppost.setHeader("Content-Type", contentType);
			} else {
				if (data != null && data.length > 0) {
					httppost.setHeader("Content-Type",
							"application/x-www-form-urlencoded");
				}
			}
		}


		try {
			httppost.setEntity(new ByteArrayEntity(data));

		} catch (Exception e) {
			throw e;
		}

		HttpResult httpResult = HttpResult.empty();
		HttpResponse response;
		try {
			response = httpclient.execute(httppost, localContext);
			httpResult = new HttpResult(localContext, response);
		} catch (Exception e) {
			httppost.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}

		httpclient.getConnectionManager().shutdown();
		return httpResult;
	}


	public static HttpResult post(String url, byte[] data, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  String localAddress, HttpHost proxy) throws Exception {

		DefaultHttpClient httpclient = useTrustingTrustManager(
				new DefaultHttpClient(), url);
		httpclient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, timeOut * 1000);
		httpclient.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT * 1000);
		httpclient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);//设置编码格式  
		// httpclient.getParams().setIntParameter(HttpConnectionParams.SO_LINGER,0);
		if (proxy != null) {
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
					proxy);
		}

		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS,
					localBinding);
		}

		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}
		
		if(headers == null){
			headers = new ArrayList<Header>();
		}
//		Header header = new BasicHeader("User-Agent", UserAgent.getRandom());
//		headers.add(header);

		HttpPost httppost = new HttpPost(url);
		String contentType = null;
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				Header h = headers.get(i);
				if (h.getName().startsWith("$x-param") == false) {
					httppost.addHeader(h);
				}
				if ("Content-Type".equalsIgnoreCase(h.getName()) == true) {
					contentType = h.getValue();
				}
			}
		}

		//httppost.addHeader("Connection", "close");

		if (contentType != null) {
			httppost.setHeader("Content-Type", contentType);
		} else {
			if (data != null && data.length > 0) {
				httppost.setHeader("Content-Type",
						"application/x-www-form-urlencoded");
			}
		}

		try {
			httppost.setEntity(new ByteArrayEntity(data));
			
		} catch (Exception e) {
			throw e;
		}

		HttpResult httpResult = HttpResult.empty();
		HttpResponse response;
		try {
			response = httpclient.execute(httppost, localContext);
			httpResult = new HttpResult(localContext, response);
		} catch (Exception e) {
			httppost.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}

		httpclient.getConnectionManager().shutdown();
		return httpResult;
	}
	
	public static HttpResult post(String url, String name, File file, String conType, String fileName, String encoding,
                                  int timeOut, CookieStore cookies, List<Header> headers,
                                  String localAddress) throws Exception {

		DefaultHttpClient httpclient = useTrustingTrustManager(
				new DefaultHttpClient(), url);
		httpclient.getParams().setIntParameter("http.socket.timeout",
				timeOut * 1000);

		if (null != localAddress && "".equals(localAddress) == false) {
			InetAddress localBinding = Inet4Address.getByName(localAddress);
			httpclient.getParams().setParameter(ConnRouteParams.LOCAL_ADDRESS,
					localBinding);
		}

		HttpContext localContext = new BasicHttpContext();
		if (cookies != null) {
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookies);
		}

		HttpPost httppost = new HttpPost(url);
		String contentType = null;
		if (headers != null) {
			int size = headers.size();
			for (int i = 0; i < size; i++) {
				Header h = headers.get(i);
				if (h.getName().startsWith("$x-param") == false) {
					httppost.addHeader(h);
				}
				if ("Content-Type".equalsIgnoreCase(h.getName()) == true) {
					contentType = h.getValue();
				}
			}
		}

		if (contentType != null) {
			httppost.setHeader("Content-Type", contentType);
		} else {
//			if (data != null && data.length > 0) {
//				httppost.setHeader("Content-Type",
//						"application/x-www-form-urlencoded");
//			}
			if (file != null) {
				httppost.setHeader("Content-Type",
						"multipart/form-data");
			}
		}
		
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart(name, new FileBody(file, conType, fileName));

		try {
			httppost.setEntity(reqEntity);
		} catch (Exception e) {
			throw e;
		}

		HttpResult httpResult = HttpResult.empty();
		HttpResponse response;
		try {
			response = httpclient.execute(httppost, localContext);
			httpResult = new HttpResult(localContext, response);
		} catch (Exception e) {
			httppost.abort();
			throw e;
		} finally {
			if (httpclient != null && httpclient.getConnectionManager() != null) {
				httpclient.getConnectionManager().shutdown();
			}
		}

		httpclient.getConnectionManager().shutdown();
		return httpResult;
	}

	public static List<NameValuePair> parsePostParams(byte[] data) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if (data != null && data.length > 0) {
			int ix = 0;
			int ox = 0;
			String key = null;
			String value = null;
			while (ix < data.length) {
				byte c = data[ix++];
				switch ((char) c) {
				case '&':
					value = new String(data, 0, ox);
					if (key != null) {
						params.add(new BasicNameValuePair(key, value));
						key = null;
					}
					ox = 0;
					break;
				case '=':
					if (key == null) {
						key = new String(data, 0, ox);
						ox = 0;
					} else {
						data[ox++] = c;
					}
					break;
				default:
					data[ox++] = c;
				}
			}
			if (key != null) {
				value = new String(data, 0, ox);
				params.add(new BasicNameValuePair(key, value));
			}
		}

		return params;
	}

	public static List<Header> getBasicRequestHeader(int headerTypes) {
		List<Header> headers = new ArrayList<Header>();
		if ((headerTypes & HttpConstant.ACCEPT) == HttpConstant.ACCEPT) {
			headers
					.add(new BasicHeader(
							"Accept",
							"image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, application/x-ms-application, application/x-ms-xbap, application/vnd.ms-xpsdocument, application/xaml+xml, */*"));
		}
		if ((headerTypes & HttpConstant.ACCEPT_ENCODING) == HttpConstant.ACCEPT_ENCODING) {
			headers.add(new BasicHeader("Accept-Encoding", "gzip, deflate"));
		}
		if ((headerTypes & HttpConstant.ACCEPT_LANGUAGE) == HttpConstant.ACCEPT_LANGUAGE) {
			headers.add(new BasicHeader("Accept-Language", "zh-cn"));
		}
		if ((headerTypes & HttpConstant.USER_AGENT) == HttpConstant.USER_AGENT) {
			headers.add(new BasicHeader("User-Agent", UserAgent.DEFAULT));
		}
		return headers;
	}

	static DefaultHttpClient useTrustingTrustManager(
            DefaultHttpClient httpClient, String url) {
		if (url.toLowerCase().startsWith("https://") == false) {
			return httpClient;
		}

		try {
			X509TrustManager trustManager = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};

			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(new KeyManager[]{},
					new TrustManager[] { trustManager }, null);

			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			// sf
			// .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

			ClientConnectionManager ccm = httpClient.getConnectionManager();
			SchemeRegistry schemeRegistry = ccm.getSchemeRegistry();

			schemeRegistry.register(new Scheme("https", sf, 443));

			return new DefaultHttpClient(ccm, httpClient.getParams());
		} catch (Throwable t) {
			t.printStackTrace();
			return null;
		}
	}

	private static KeyManager[] createKeyManagers(final KeyStore keystore,
			final String password) throws KeyStoreException,
			NoSuchAlgorithmException, UnrecoverableKeyException {
		if (keystore == null) {
			throw new IllegalArgumentException("Keystore may not be null");
		}
		KeyManagerFactory kmfactory = KeyManagerFactory
				.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmfactory.init(keystore, password != null ? password.toCharArray()
				: null);
		return kmfactory.getKeyManagers();
	}

	private static KeyStore createKeyStore(final URL url, final String password)
			throws KeyStoreException, NoSuchAlgorithmException,
			CertificateException, IOException {
		if (url == null) {
			throw new IllegalArgumentException("Keystore url may not be null");
		}
		KeyStore keystore = KeyStore.getInstance("jks");
		InputStream is = null;
		try {
			is = url.openStream();
			keystore.load(is, password != null ? password.toCharArray() : null);
		} finally {
			if (is != null)
				is.close();
		}
		return keystore;
	}
}
