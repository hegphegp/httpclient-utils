package com.codingfly.httpclient.model;

import java.io.Serializable;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/** 请求结果 */
public class HttpResult implements Serializable {
	private static final long serialVersionUID = -6368281080581808792L;
	
	/** 执行结果-body */
	private String result;

	private byte[] body;

	private boolean isBinary=false;

	/** 状态码-statusCode */
	private int statusCode;
	
	/** 状态行-StatusLine */
    private StatusLine statusLine;
    
    /** 请求头信息 */
    private List<Header> reqHeaders;
    
    /** 响应头信息 */
    private Header[] respHeaders;
    
    /** 协议版本 */
    private ProtocolVersion protocolVersion;
    
    /** HttpResponse结果对象 */
    private HttpResponse resp;

	public HttpResult(HttpResponse resp) {
		this.statusLine = resp.getStatusLine();
		this.respHeaders = resp.getAllHeaders();
		this.protocolVersion = resp.getProtocolVersion();
		this.statusCode = resp.getStatusLine().getStatusCode();
		this.resp = resp;
	}
	
	/**
	 * 从返回的头信息中查询指定头信息
	 * @param name	头信息名称
	 */
    public Header getHeaders(final String name) {
    	Header[] headers = this.resp.getHeaders(name);
        return headers!=null && headers.length>0?headers[0]:null;
    }

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public boolean isBinary() {
		return isBinary;
	}

	public void setBinary(boolean binary) {
		isBinary = binary;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public StatusLine getStatusLine() {
		return statusLine;
	}

	public void setStatusLine(StatusLine statusLine) {
		this.statusLine = statusLine;
	}

	public List<Header> getReqHeaders() {
		return reqHeaders;
	}

	public void setReqHeaders(List<Header> reqHeaders) {
		this.reqHeaders = reqHeaders;
	}

	public Header[] getRespHeaders() {
		return respHeaders;
	}

	public void setRespHeaders(Header[] respHeaders) {
		this.respHeaders = respHeaders;
	}

	public ProtocolVersion getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(ProtocolVersion protocolVersion) {
		this.protocolVersion = protocolVersion;
	}

	public HttpResponse getResp() {
		return resp;
	}

	public void setResp(HttpResponse resp) {
		this.resp = resp;
	}

}