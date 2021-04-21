package com.windvalley.guli.common.base.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {
    private String url;
    private Map<String, String> paramaters;
    private int statusCode;
    private String content;
    private String xmlParamater;
    private boolean isHttps;

    public HttpClientUtils(String url) {
        this.url = url;
    }

    public HttpClientUtils(String url, Map<String, String> paramaters) {
        this.url = url;
        this.paramaters = paramaters;
    }

    public boolean isHttps() {
        return isHttps;
    }

    public void setHttp(boolean isHttps) {
        this.isHttps = isHttps;
    }

    public String getXmlParamater() {
        return xmlParamater;
    }

    public void setXmlParamater(String xmlParamater) {
        this.xmlParamater = xmlParamater;
    }

    public void setParamaters(Map<String, String> paramaters) {
        this.paramaters = paramaters;
    }

    public void addParamater(String key, String value) {
        if (paramaters == null){
            paramaters = new HashMap<String, String>();
        }
        paramaters.put(key, value);
    }

    public void post() throws ClientProtocolException, IOException {
        HttpPost http = new HttpPost(url);
        setEntity(http);
        execute(http);
    }

    public void put() throws ClientProtocolException, IOException {
        HttpPut http = new HttpPut(url);
        setEntity(http);
        execute(http);
    }

    public void get() throws ClientProtocolException, IOException {
        if (paramaters != null) {
            StringBuilder url = new StringBuilder(this.url);
            boolean isFirst = true;
            for (String key : paramaters.keySet()) {
                if (isFirst) {
                    url.append("?");
                    isFirst = false;
                }else {
                    url.append("&");
                }
                url.append(key).append("=").append(paramaters.get(key));
            }
            this.url = url.toString();
        }
        HttpGet http = new HttpGet(url);
        execute(http);
    }

    /**
     * set http post,put param
     */
    private void setEntity(HttpEntityEnclosingRequestBase http) {
        if (paramaters != null) {
            List<NameValuePair> nvps = new LinkedList<NameValuePair>();
            for (String key : paramaters.keySet())
                nvps.add(new BasicNameValuePair(key, paramaters.get(key))); // 参数
            http.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8)); // 设置参数
        }
        if (xmlParamater != null) {
            http.setEntity(new StringEntity(xmlParamater, Consts.UTF_8));
        }
    }

    private void execute(HttpUriRequest http) throws ClientProtocolException, IOException {
        CloseableHttpClient httpClient = null;
        try {
            if (isHttps) {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                            // 信任所有
                            public boolean isTrusted(X509Certificate[] chain,
                                                     String authType)
                                    throws CertificateException {
                                return true;
                            }
                }).build();
                SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                        sslContext);
                httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                        .build();
            } else {
                httpClient = HttpClients.createDefault();
            }
            CloseableHttpResponse response = httpClient.execute(http);
            try {
                if (response != null) {
                    if (response.getStatusLine() != null) {
                        statusCode = response.getStatusLine().getStatusCode();
                    }
                    HttpEntity entity = response.getEntity();
                    // 响应内容
                    content = EntityUtils.toString(entity, Consts.UTF_8);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getContent() throws ParseException, IOException {
        return content;
    }
}
