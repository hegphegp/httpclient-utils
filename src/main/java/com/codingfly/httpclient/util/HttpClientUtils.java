package com.codingfly.httpclient.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import com.codingfly.httpclient.model.HttpConfig;
import com.codingfly.httpclient.model.HttpMethod;
import com.codingfly.httpclient.model.HttpResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	/**
	new HttpConfig(client, HttpMethod.GET, url)
		.setUrlParams(urlParams)
		.setBodyParams(bodyParams)
		.setHeaders(headers)
		.setInenc(inenc)
		.setOutenc(outenc)
		.setConnectionRequestTimeout(10000)
		.setConnectTimeout(10000)
		.setSocketTimeout(24*3600*1000);
	*/
	public static HttpResult get(HttpConfig config) {
		config.setMethod(HttpMethod.GET);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.POST, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult post(HttpConfig config) {
		config.setMethod(HttpMethod.POST);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.PUT, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult put(HttpConfig config) {
		config.setMethod(HttpMethod.PUT);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.DELETE, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult delete(HttpConfig config) {
		config.setMethod(HttpMethod.DELETE);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.PATCH, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult patch(HttpConfig config) {
		config.setMethod(HttpMethod.PATCH);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.HEAD, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult head(HttpConfig config) {
		config.setMethod(HttpMethod.HEAD);
		return sendAndGetResp(config);
	}


	/**
	 new HttpConfig(client, HttpMethod.TRACE, url)
		 .setUrlParams(urlParams)
		 .setBodyParams(bodyParams)
		 .setHeaders(headers)
		 .setInenc(inenc)
		 .setOutenc(outenc)
		 .setConnectionRequestTimeout(10000)
		 .setConnectTimeout(10000)
		 .setSocketTimeout(24*3600*1000);
	 */
	public static HttpResult trace(HttpConfig config) {
		config.setMethod(HttpMethod.TRACE);
		return sendAndGetResp(config);
	}

	public static boolean isBinaryResp(HttpResponse resp) {
		if (resp.getEntity().getContentType() == null) return false;
		String contentType = resp.getEntity().getContentType().getValue();
		String[] arr = contentType.split(";");
		for (int i = 0; i < arr.length; i++) {
			String type = arr[i].toLowerCase();
			if (type.startsWith("text/") || type.equals("application/x-javascript") || type.equals("application/json")) {
				return false;
			}
		}
		return true;
	}

	public static void deleteDir(File file) {
		if (file.isDirectory()) { //????????????????????????
			//????????????????????????????????????
			File[] files = file.listFiles();
			//??????????????????????????????delete??????
			for (int i = 0; i < files.length; i++) {
				deleteDir(files[i]);
			}
		}
		file.delete(); //???????????????????????????????????????File??????????????????
	}

	public static boolean download(HttpConfig config, String filePath, String tempDirPath) throws IOException {
		File tempDir = new File(tempDirPath);
		if (new File(filePath).exists()) {
			deleteDir(tempDir);
			return true;
		}

		Object rangeResult = checkCanRange(config, filePath);
		if (rangeResult instanceof HttpResult) {
			deleteDir(tempDir);
			return false;
		}
		tempDir.mkdirs();
		Long range = (Long) rangeResult;
		File startIndexFile = new File(tempDir+File.separator+"startIndex.txt");
		Long start = 0l;
		tempDir.mkdirs();
		if (startIndexFile.exists()) {
			String startIndexStr = FileUtils.readFileToString(startIndexFile, "utf-8");
			start = startIndexStr!=null && startIndexStr.length()>0? Long.valueOf(startIndexStr):0l;
		} else {
			startIndexFile.createNewFile();
		}
		String pathPrefix = tempDirPath + File.separator + Md5Utils.md5(tempDirPath) + File.separator;
		for (; start < range; ) {
			String rangeFilePath = pathPrefix + "range-" + start;
			setRangeHeader(config, start, start + 1024 * 1024);
			HttpResponse rangeResp = execute(config);
			checkDownload(rangeResp, config);
			binaryResp2File(config, rangeResp, rangeFilePath);
			FileUtils.writeStringToFile(startIndexFile,start+"","UTF-8",false);
			start = start + 1024 * 1024;
		}
		// ???tempDir?????????????????????????????????????????????
		merge(pathPrefix, filePath);
		deleteDir(tempDir);
		return true;
	}

	/** ???????????? */
	public static void merge(String tempDirPath, String filePath) throws FileNotFoundException {
		Vector<FileInputStream> vector = new Vector();
		File tempDir = new File(tempDirPath);
		File[] files = tempDir.listFiles();
		if (files==null || files.length==0) return;
		TreeSet<String> treeSet = new TreeSet();
		for (File file:files) {
			treeSet.add(file.getAbsolutePath());
		}
		for (String filename:treeSet) {
			vector.add(new FileInputStream(filename));
		}
		SequenceInputStream sis = null;
		FileOutputStream fos = null;
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		try {
			file.createNewFile();
			Enumeration<FileInputStream> elements = vector.elements();
			sis = new SequenceInputStream(elements);
			fos = new FileOutputStream(file);
			byte[] b = new byte[1024 * 1024];
			int len = 0;
			while ((len = sis.read(b)) != -1) {
				fos.write(b, 0, len);
			}
		} catch (Exception e) {
			file.delete();
			throw new RuntimeException(e);
		} finally {
			if (sis!=null) {
				try {
					sis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos!=null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void fmt2Stream(HttpResponse resp, OutputStream out) {
		try {
			resp.getEntity().writeTo(out);
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			close(out);
			close(resp);
		}
	}

	/**
	 * ??????????????????????????????HttpResult??????
	 * @param config    ??????????????????
	 * @return        ??????HttpResult????????????
	 */
	public static HttpResult sendAndGetResp(HttpConfig config) {
		HttpResponse resp = execute(config);  // ????????????
		HttpResult result = new HttpResult(resp);
		boolean isBinaryResp = isBinaryResp(resp);
		if (isBinaryResp) {
			result.setBinary(true);
			result.setBody(fmt2Byte(resp));
		} else {
			checkStatusCode(resp, config);
			result.setResult(fmt2String(resp, config.getOutenc()));
		}
		result.setReqHeaders(config.getHeaders());
		result.setRespHeaders(resp.getAllHeaders());
		result.setStatusCode(resp.getStatusLine().getStatusCode());
		return result;
	}

	/**
	 * ??????????????????????????????????????????????????????????????????????????????????????????
	 * ??????Long????????????????????????
	 * ??????HttpResult????????????????????????????????????????????????
	 */
	public static Object checkCanRange(HttpConfig config, String filePath) throws IOException {
		setRangeHeader(config, 0L, 1L);
		HttpResponse resp = execute(config);
		checkDownload(resp, config);
		// Content-Range      ????????????????????????????????????????????????
		// httpStatusCode=206 ???????????????????????????
		Header header = resp.getFirstHeader("Content-Range");
		if (header==null) { // ??????????????????
			return binaryResp2File(config, resp, filePath);
		} else { // ??????????????????
			try {
				String value = header.getValue();
				EntityUtils.consume(resp.getEntity());
				String[] arr = value.split("/");
				return Long.valueOf(arr[1]);
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				close(resp);
			}
		}
	}

	public static void setRangeHeader(HttpConfig config, Long start, Long end) {
		List<Header> headers = config.getHeaders()==null? new ArrayList():config.getHeaders();
		List<Header> newHeaders = new ArrayList();
		for (Header header:headers) {
			if (header.getName().toLowerCase().equals("range")==false) {
				newHeaders.add(new BasicHeader(header.getName(), header.getValue()));
			}
		}
		newHeaders.add(new BasicHeader("Range", "bytes="+start+"-"+end));
		config.setHeaders(newHeaders);
	}

	public static HttpResult binaryResp2File(HttpConfig config, HttpResponse resp, String filePath) throws IOException {
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		if (file.exists()==false) {
			file.createNewFile();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			fmt2Stream(resp, out);
		} catch (Exception e) {
			file.delete();
			throw new RuntimeException(e);
		}

		HttpResult result = new HttpResult(resp);
		result.setReqHeaders(config.getHeaders());
		result.setRespHeaders(resp.getAllHeaders());
		result.setStatusCode(resp.getStatusLine().getStatusCode());
		return result;
	}

	public static void checkDownload(HttpResponse resp, HttpConfig config) throws IOException {
		checkIsBinaryResp(resp, config);
		checkDownloadStatusCode(resp, config);
	}

	public static void checkIsBinaryResp(HttpResponse resp, HttpConfig config) {
		boolean isBinaryResp = isBinaryResp(resp);
		if (isBinaryResp==false) {
			int statusCode = resp.getStatusLine().getStatusCode();
			String errMsg = "\n"+config.getUrl()+" ??????????????????????????????????????????????????????"+statusCode+"\n\n"+fmt2String(resp, config.getOutenc());
			throw new RuntimeException(errMsg);
		}
	}

	public static void checkStatusCode(HttpResponse resp, HttpConfig config) {
		int statusCode = resp.getStatusLine().getStatusCode();
		if (statusCode>=400) {
			String errMsg = "\n"+config.getMethod().name()+" "+config.getUrl()+" ???httpStatusCode??? "+statusCode+"?????????????????? "+fmt2String(resp, config.getOutenc())+ "\n\n";
			logger.error(errMsg);
			throw new RuntimeException(errMsg);
		}
	}

	public static void checkDownloadStatusCode(HttpResponse resp, HttpConfig config) {
		int statusCode = resp.getStatusLine().getStatusCode();
		if (statusCode!=200 && statusCode!=206) {
			String errMsg = "\n"+config.getMethod().name()+" "+config.getUrl()+" ????????????"+statusCode+"\n\n"+fmt2String(resp, config.getOutenc());
			throw new RuntimeException(errMsg);
		}
	}

	/** ??????????????????????????????HttpResponse?????? */
	public static HttpResponse execute(HttpConfig config) {
		try {
			String inenc = config.getInenc()==null? "utf-8":config.getInenc();
			if (config.getUrlParams()!=null && config.getUrlParams().size()>0) {
				config.setUrl(checkHasParas(config.getUrl(), new ArrayList(), inenc));
			}
			HttpRequestBase request = getRequest(config.getUrl(), config.getMethod()); //??????????????????
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(config.getConnectionRequestTimeout())
					.setConnectTimeout(config.getConnectTimeout())
					.setSocketTimeout(config.getSocketTimeout())
					.build();

			request.setConfig(requestConfig);        // ????????????
			if (config.getHeaders()!=null && config.getHeaders().size()>0) {
				request.setHeaders(config.getHeaders().toArray(new Header[]{})); // ??????header??????
			}
			
			//????????????????????????entity(???HttpPost???HttpPut???HttpPatch??????)
			if (HttpEntityEnclosingRequestBase.class.isAssignableFrom(request.getClass())) {
				List<NameValuePair> nvps = new ArrayList();
				if (config.getMethod()!=HttpMethod.GET && config.getBodyParams()!=null && config.getBodyParams().size()>0) {
					//????????????
					HttpEntity entity = map2HttpEntity(nvps, config.getBodyParams(), inenc);
					//??????????????????????????????
					((HttpEntityEnclosingRequestBase) request).setEntity(entity);
				}
			}
//			String host = UrlUtils.getHost(config.getUrl());
//			HttpClientContext httpContext = httpContextMap.get(host);
//			if (httpContext==null) {
//				httpContext = new HttpClientContext();
//				httpContextMap.put(host, httpContext);
//			}
			// ??????????????????????????????????????????????????????
			return config.getClient().execute(request);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * ??????????????????
	 * @param resp			????????????
	 * @return				??????????????????
	 */
	private static byte[] fmt2Byte(HttpResponse resp) {
		byte[] bytes = null;
		try {
			if (resp.getEntity() != null) { // ????????????????????????????????????String??????
				bytes = IOUtils.toByteArray(resp.getEntity().getContent());
			}
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(resp);
		}
		return bytes;
	}

	/**
	 * ??????????????????
	 * @param resp			????????????
	 * @param encoding		??????
	 * @return				??????????????????
	 */
	public static String fmt2String(HttpResponse resp, String encoding) {
		String body = "";
		try {
			if (resp.getEntity() != null) { // ????????????????????????????????????String??????
				if (encoding!=null) {
					body = EntityUtils.toString(resp.getEntity(), encoding);
				} else {
					byte[] bytes = IOUtils.toByteArray(resp.getEntity().getContent());
					String contentType = resp.getEntity().getContentType() == null ? "" : resp.getEntity().getContentType().getValue();
					encoding = getHtmlCharset(contentType, bytes);
					body = new String(bytes, encoding);
				}
			} else { //????????????head??????
				body =resp.getStatusLine().toString();
			}
			EntityUtils.consume(resp.getEntity());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(resp);
		}
		return body;
	}

	private static String getHtmlCharset(String contentType, byte[] contentBytes) throws IOException {
		String charset = CharsetUtils.detectCharset(contentType, contentBytes);
		if (charset == null) {
			charset = Charset.defaultCharset().name();
		}
		return charset;
	}

	private static HttpRequestBase getRequest(String url, HttpMethod method) {
		switch (method) {
			case GET:
				return new HttpGet(url);
			case POST:
				return new HttpPost(url);
			case HEAD:
				return new HttpHead(url);
			case PUT:
				return new HttpPut(url);
			case DELETE:
				return new HttpDelete(url);
			case TRACE:
				return new HttpTrace(url);
			case PATCH:
				return new HttpPatch(url);
			default:
				return null;
		}
	}

	private static void close(OutputStream out) {
		try {
			if(out == null) return;
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void close(HttpResponse resp) {
		try {
			if(resp == null) return;
			//??????CloseableHttpResponse ???resp???????????????????????????
			if (CloseableHttpResponse.class.isAssignableFrom(resp.getClass())) {
				((CloseableHttpResponse)resp).close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//????????????????????????
	public static final String ENTITY_STRING="$ENTITY_STRING$";
	public static final String ENTITY_JSON="$ENTITY_JSON$";
	public static final String ENTITY_FILE="$ENTITY_FILEE$";
	public static final String ENTITY_BYTES="$ENTITY_BYTES$";
	public static final String ENTITY_INPUTSTREAM="$ENTITY_INPUTSTREAM$";
	public static final String ENTITY_SERIALIZABLE="$ENTITY_SERIALIZABLE$";
	public static final String ENTITY_MULTIPART="$ENTITY_MULTIPART$";
	private static final List<String> SPECIAL_ENTITY = Arrays.asList(ENTITY_STRING, ENTITY_JSON, ENTITY_BYTES, ENTITY_FILE, ENTITY_INPUTSTREAM, ENTITY_SERIALIZABLE, ENTITY_MULTIPART);

	/**
	 * ??????url??????????????????????????????????????????????????????????????????
	 * @param url	????????????
	 * @param nvps	????????????
	 * @param encoding	??????
	 * @return	?????????????????????url
	 * @throws UnsupportedEncodingException ????????????????????????
	 */
	public static String checkHasParas(String url, List<NameValuePair> nvps, String encoding) throws UnsupportedEncodingException {
		if (url.contains("?") && url.indexOf("?") < url.indexOf("=")) { // ??????url?????????????????????
			Map<String, Object> map = buildParas(url.substring(url.indexOf("?") + 1));
			map2HttpEntity(nvps, map, encoding);
			url = url.substring(0, url.indexOf("?"));
		}
		return url;
	}

	/**
	 * ??????????????????map????????????????????????????????????
	 * @param nvps				????????????
	 * @param map				???????????????map???
	 * @param encoding			??????
	 * @return					??????HttpEntity
	 * @throws UnsupportedEncodingException  ????????????????????????
	 */
	public static HttpEntity map2HttpEntity(List<NameValuePair> nvps, Map<String, Object> map, String encoding) throws UnsupportedEncodingException {
		HttpEntity entity = null;
		if(map!=null && map.size()>0){
			boolean isSpecial = false;
			// ????????????
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				if(SPECIAL_ENTITY.contains(entry.getKey())){ //?????????????????????
					isSpecial = true;
					if (ENTITY_STRING.equals(entry.getKey())) {//string
						entity = new StringEntity(String.valueOf(entry.getValue()), encoding);
						break;
					} else if (ENTITY_JSON.equals(entry.getKey())) {//json
						entity = new StringEntity(String.valueOf(entry.getValue()), encoding);
						String contentType = "application/json";
						if (encoding != null) {
							contentType += ";charset=" + encoding;
						}
						((StringEntity) entity).setContentType(contentType);
						break;
					} else if (ENTITY_BYTES.equals(entry.getKey())) {//file
						entity = new ByteArrayEntity((byte[])entry.getValue());
						break;
					} else if (ENTITY_FILE.equals(entry.getKey())) {//file
						if (File.class.isAssignableFrom(entry.getValue().getClass())){
							entity = new FileEntity((File)entry.getValue(), ContentType.APPLICATION_OCTET_STREAM);
						} else if (entry.getValue().getClass()==String.class){
							entity = new FileEntity(new File((String) entry.getValue()), ContentType.create("text/plain", "UTF-8"));
						}
						break;
					} else if (ENTITY_INPUTSTREAM.equals(entry.getKey())) { //inputstream
						break;
					} else if (ENTITY_SERIALIZABLE.equals(entry.getKey())){ //serializeable
						break;
					} else if (ENTITY_MULTIPART.equals(entry.getKey())){ //MultipartEntityBuilder
						break;
					} else {
						nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
					}
				} else {
					nvps.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
			}
			if (!isSpecial) {
				entity = new UrlEncodedFormEntity(nvps, encoding);
			}
		}
		return entity;
	}

	/**
	 * ????????????
	 * ???????????????k1=v1&amp;k2=v2
	 *
	 * @param paras				????????????
	 * @return					?????????????????????map???
	 */
	public static Map<String,Object> buildParas(String paras){
		String[] p = paras.split("&");
		String[][] ps = new String[p.length][2];
		int pos = 0;
		for (int i = 0; i < p.length; i++) {
			pos = p[i].indexOf("=");
			ps[i][0]=p[i].substring(0, pos);
			ps[i][1]=p[i].substring(pos+1);
			pos = 0;
		}
		return buildParas(ps);
	}

	/**
	 * ????????????
	 * ???????????????{{"k1","v1"},{"k2","v2"}}
	 * @param paras 				????????????
	 * @return						?????????????????????map???
	 */
	public static Map<String,Object> buildParas(String[][] paras){
		Map<String,Object> map = new HashMap();
		for (String[] para: paras) {
			map.put(para[0], para[1]);
		}
		return map;
	}

	public static Header[] toHeaderArr(Map<String, String> map) {
		List<Header> headers = toHeaderList(map);
		return headers.toArray(new Header[]{});
	}

	public static List<Header> toHeaderList(Map<String, String> map) {
		List<Header> headers = new ArrayList<>();
		for (String key:map.keySet()) {
			if (key!=null && key.equals("")==false) {
				headers.add(new BasicHeader(key, map.get(key)));
			}
		}
		return headers;
	}

}