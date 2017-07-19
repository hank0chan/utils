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
import java.util.Map;

/**
 * 公共抽象的Http连接客户端
 * @author hankChan
 *         2017/7/11 0011.
 */
public abstract class AbstractHttpClient {

    private CloseableHttpClient httpClient;
    private PoolingHttpClientConnectionManager clientConnectionManager;

    /**
     * 默认初始化，通常情况下，推荐使用这种实现即可
     */
    protected void init() {
        init(200, 20);
    }

    /**
     * 默认初始化
     */
    protected void init(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * 默认初始化
     */
    protected void init(PoolingHttpClientConnectionManager clientConnectionManager) {
        httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
    }

    /**
     * 自定义初始化
     */
    protected void init(int maxTotal, int maxPerRoute) {
        clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(maxTotal);
        clientConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
        //HttpHost httpHost = new HttpHost("localhost", 58888);
        //clientConnectionManager.setMaxPerRoute(new HttpRoute(httpHost), 50);
        httpClient = HttpClients.custom().setConnectionManager(clientConnectionManager).build();
    }

    /**
     * 资源回收
     */
    protected void shutdown() {
        if(httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

        // 创建HTTP连接客户端
        //CloseableHttpClient httpClient = HttpClients.createDefault();

        // 发送请求
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


    // ------------------------ POST URL拼接参数请求方式（application/x-www-form-urlencoded）--------------------

    /**
     * 发送UrlEncoded方式的POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByUrlEncoded(String url, Map<String, String> params) throws Exception {
        // 拼接URL和请求参数
        String fullUrl = UrlUtils.appendUrlWithParams(url, params);
        // 发送请求
        HttpPost httpPost = new HttpPost(fullUrl);
        return doPost(httpPost);
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

}
