package cn.hankchan.http;

import cn.hankchan.util.UrlUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 公共抽象的Http连接客户端
 * <h>基于连接池实现</h>
 * @author hankChan
 *         2017/7/11 0011.
 */
public abstract class AbstractHttpClient {

    // TODO 保留这样实现方式，后面根据实际情况再进一步斟酌采用何种方式
    private static CloseableHttpClient httpClient;
    private static PoolingHttpClientConnectionManager clientConnectionManager;
    private static Integer maxTotal;
    private static Integer defaultMaxPerRoute;
    private static Integer port;
    private static String host;
    private static Integer maxPerRoute;

    static {
        clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(Optional.ofNullable(maxTotal).orElse(2048));
        clientConnectionManager.setDefaultMaxPerRoute(Optional.ofNullable(defaultMaxPerRoute).orElse(50));
        HttpHost httpHost = new HttpHost(Optional.ofNullable(host).orElse("localhost"),
                Optional.ofNullable(port).orElse(58888));
        clientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost),
                Optional.ofNullable(maxPerRoute).orElse(100));
        httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
    }

    public AbstractHttpClient() {}

    public AbstractHttpClient(Integer port) {
        this.port = port;
    }

    public AbstractHttpClient(String host, Integer port, Integer maxTotal, Integer maxPerRoute, Integer defaultMaxPerRoute) {
        this.host = host;
        this.port = port;
        this.maxTotal = maxTotal;
        this.maxPerRoute = maxPerRoute;
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    /**
     * 执行GET请求，获取响应结果
     * @param httpGet HttpGet请求对象
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String doGet(HttpGet httpGet) throws IOException {

        // 创建HTTP连接客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 返回JSON格式字符串
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity);
    }

    /**
     * 执行POST请求，获取响应结果
     * @param httpPost HttpPost请求对象
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String doPost(HttpPost httpPost) throws IOException {

        HttpResponse response = httpClient.execute(httpPost);

        // 返回结果JSON字符串
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity);
    }

    /**
     * 执行POST请求，获取响应结果
     * @param httpPost HttpPost请求对象
     * @param header 请求头内容
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String doPost(HttpPost httpPost, Map<String, String> header) throws IOException {
        if(!(Optional.ofNullable(header).orElse(new HashMap<>()).isEmpty())) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
//                httpPost.setHeader("Accept-Encoding", "gzip");
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        HttpResponse response = httpClient.execute(httpPost);
        // 返回结果JSON字符串
        HttpEntity responseEntity = response.getEntity();
        return EntityUtils.toString(responseEntity);
    }

    // ------------------------ POST RequestBody接收参数方式（application/json） -------------------------------

    /**
     * 发送JSON/RequestBody形式的POST请求
     * @param url 请求URL
     * @param requestJsonStirng JSON格式的请求字符串
     * @return JSON格式响应结果
     * @throws Exception exception
     */
    public final String postByRequestBody(String url, String requestJsonStirng) throws Exception {
        // 构建JSON格式的请求实体
        StringEntity stringEntity = new StringEntity(requestJsonStirng);
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");

        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(stringEntity);
        return doPost(httpPost);
    }


    // ------------------------ POST,GET URL拼接参数请求方式（application/x-www-form-urlencoded）--------------------

    /**
     * 发送UrlEncoded方式的POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByUrlEncoded(String url, Map<String, String> params) throws Exception {
        // post方式的urlEncoded应该是将key=value部分直接放在content中，如果直接拼接在url后面，长度可能会超出
        HttpPost httpPost = new HttpPost(url);
        // 拼接请求参数
        String keyValuePart = UrlUtils.appendKeyAndValues(params);
        StringEntity stringEntity = new StringEntity(keyValuePart);
        stringEntity.setContentEncoding("UTF-8");
        // 设定content格式
        stringEntity.setContentType("application/x-www-form-urlencoded");
        httpPost.setEntity(stringEntity);
        // 设定请求头格式
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        return doPost(httpPost);
    }

    /**
     * 发送URLEncoded方式的GET请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String getByUrlEncoded(String url, Map<String, String> params) throws IOException {
        // 拼接URL和请求参数
        String fullUrl = UrlUtils.appendUrlWithParams(url, params);
        // 发送请求
        HttpGet httpGet = new HttpGet(fullUrl);
        return doGet(httpGet);
    }

    // ------------------------ POST FormData表单请求方式（multipart/form-data） -------------------------------

    /**
     * 使用表单方式提交POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @param bytesKey 需要上传的二进制流的字段名
     * @param bytes 二进制流
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByFormData(String url, Map<String, String> params,
                                       String bytesKey, byte[] bytes) throws Exception {
        // 构建请求表单
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            multipartEntityBuilder.addPart(entry.getKey(),
                    new StringBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA));
        }
        // 加入需要的byte二进制流
        multipartEntityBuilder.addPart(bytesKey,
//                new ByteArrayBody(bytes, ContentType.APPLICATION_OCTET_STREAM, ""));
                new ByteArrayBody(bytes, ContentType.MULTIPART_FORM_DATA, ""));
        // 构建请求参数实体
        HttpEntity requestEntity = multipartEntityBuilder.build();
        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);
        // 执行POST
        return doPost(httpPost);
    }

    /**
     * 使用表单方式提交POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByFormData(String url, Map<String, String> params) throws Exception {
        // 构建请求表单
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            multipartEntityBuilder.addPart(entry.getKey(),
                    new StringBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA));
        }
        // 构建请求参数实体
        HttpEntity requestEntity = multipartEntityBuilder.build();
        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);
        // 执行POST
        return doPost(httpPost);
    }
}
