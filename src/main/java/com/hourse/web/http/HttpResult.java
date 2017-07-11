package com.hourse.web.http;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class HttpResult {
	private static Pattern headerCharsetPattern = Pattern.compile("charset=((gb2312)|(gbk)|(utf-8))", Pattern.CASE_INSENSITIVE);
	private static Pattern pattern = Pattern.compile("<meta[^>]*content=(['\"])?[^>]*charset=((gb2312)|(gbk)|(utf-8))\\1[^>]*>",Pattern.CASE_INSENSITIVE);
	
	
	private String headerCharset;
	private String headerContentType;
	private String headerContentEncoding;
	private List<Header> headers;
	private String metaCharset;
	private byte[] response;
	private String responseUrl;
	private int statuCode = 999;
	private CookieStore cookieStore;
	private static final int BUFFER_SIZE = 4096;
	private HttpResult() {}

	public static HttpResult empty() {
		return new HttpResult();
	}

	public String getHeaderCharset() {
		return headerCharset;
	}

	public String getHeaderContentType() {
		return headerContentType;
	}

	public String getHeaderContentEncoding() {
		return headerContentEncoding;
	}
	
	public final List<Header> getHeaders() {
		return this.headers;
	}

	public String getMetaCharset() {
		return metaCharset;
	}

	public byte[] getResponse() {
		return Arrays.copyOf(this.response, this.response.length);
	}

	public String getResponseUrl() {
		return responseUrl;
	}

	public int getStatuCode() {
		return statuCode;
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}
	
	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public String getHtml() {
		try {
			return this.getText();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public String getText() throws UnsupportedEncodingException {
		return this.getText("");
	}

	public String getText(String encoding) throws UnsupportedEncodingException {
		if (this.response == null) {
			return "";
		}
		if (encoding == null || "".equals(encoding.trim()) == true) {
			encoding = this.metaCharset;
		}
		if (encoding == null || "".equals(encoding.trim()) == true) {
			encoding = this.headerCharset;
		}
		if (encoding == null || "".equals(encoding.trim()) == true) {
			encoding = "UTF-8";
		}
		if ("gb2312".equalsIgnoreCase(encoding) == true) {
			encoding = "GBK";
		}
		return new String(this.response, encoding);
	}

	private String getCharsetFromMeta() {
		StringBuilder builder = new StringBuilder();
		String charset = "";
		for (int i = 0; i < this.response.length&& ("".equals(charset)); i++) {
			char c = (char) this.response[i];
			switch (c) {
			case '<':
				builder.delete(0, builder.length());
				builder.append(c);
				break;
			case '>':
				if (builder.length() > 0) {
					builder.append(c);
				}
				String meta = builder.toString();

				if (meta.toLowerCase().startsWith("<meta")) {
					charset = this.getCharsetFromMeta(meta);
				}
				break;
			default:
				if (builder.length() > 0) {
					builder.append(c);
				}
				break;
			}
		}

		return charset;
	}

	private String getCharsetFromMeta(String meta) {
		if (meta == null || "".equals(meta) == true) {
			return "";
		}
		Matcher m = pattern.matcher(meta);
		if (m.find() == true) {
			return m.group(2);
		}
		return "";
	}

	private void parseHttpHeaders(HttpResponse httpResponse) {
		String headerName = "";
		String headerValue = "";
		int index = -1;

		Header[] rspHeaders = httpResponse.getAllHeaders();
		for (int i = 0; i < rspHeaders.length; i++) {
			Header header = rspHeaders[i];
			this.headers.add(header);
			headerName = header.getName();
			if ("Content-Type".equalsIgnoreCase(headerName)) {
				headerValue = header.getValue();
				index = headerValue.indexOf(";");
				if (index > 0) {
					this.headerContentType = headerValue.substring(0, index);
					Matcher m = headerCharsetPattern.matcher(headerValue);
					if (m.find() == true) {
						this.headerCharset = m.group(1);
					}
				} else {
					this.headerContentType = headerValue;
				}
			}
			if ("Content-Encoding".equalsIgnoreCase(headerName) == true) {
				this.headerContentEncoding = header.getValue();
			}
		}
	}

	private void parseResponseUrl(HttpContext httpContext) {
		HttpHost target = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
		HttpUriRequest req = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
		String url = req.getURI().toString();
		if (url.startsWith("/") == true) {
			url = target.toHostString() + url;
		}
		this.responseUrl = url;
	}

	public HttpResult(HttpContext httpContext, HttpResponse httpResponse)throws IOException {
		this.headers = new ArrayList<Header>();
		this.statuCode = httpResponse.getStatusLine().getStatusCode();

		if (httpContext != null) {
			this.parseResponseUrl(httpContext);
			this.cookieStore=(CookieStore)httpContext.getAttribute(ClientContext.COOKIE_STORE);
		}

		if (httpResponse != null) {
			this.parseHttpHeaders(httpResponse);
			try {
				if ("gzip".equalsIgnoreCase(this.headerContentEncoding) || "deflate".equalsIgnoreCase(this.headerContentEncoding)) {
					GZIPInputStream is = new GZIPInputStream(httpResponse.getEntity().getContent());
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					byte[] buffer = new byte[BUFFER_SIZE];
					int count = 0;
					while ((count = is.read(buffer)) > 0) {
						os.write(buffer, 0, count);
					}
					this.response = os.toByteArray();
					os.close();
					is.close();
				} else {
					this.response = EntityUtils.toByteArray(httpResponse.getEntity());
				}
			} catch (IOException e) {
				this.response=new byte[0];
				throw e;
			}
			if (this.response != null) {
				this.metaCharset = this.getCharsetFromMeta();
			}
		}
	}

}
