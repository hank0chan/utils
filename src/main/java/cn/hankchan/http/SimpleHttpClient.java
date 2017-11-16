package cn.hankchan.http;

import cn.hankchan.util.UrlUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 简单的HttpClient客户端
 * @author hankChan
 *         27/09/2017.
 */
public class SimpleHttpClient {

    private CloseableHttpClient httpClient;

    public SimpleHttpClient() {
        httpClient = HttpClients.createDefault();
    }

    /**
     * 执行GET请求，获取响应结果
     * @param httpGet HttpGet请求对象
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String doGet(HttpGet httpGet) throws IOException {
        CloseableHttpResponse response = null;
        try {
            // 发送请求
            response = httpClient.execute(httpGet);
            // 返回JSON格式字符串
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            // 需要关闭资源
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行GET请求，获取响应结果
     * @param httpGet HttpGet请求对象
     * @param header 请求头内容
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String doGet(HttpGet httpGet, Map<String, String> header) throws IOException {
        // 处理请求头信息
        if(!(Optional.ofNullable(header).orElse(new HashMap<>()).isEmpty())) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return doGet(httpGet);
    }

    /**
     * 执行POST请求，获取响应结果
     * @param httpPost HttpPost请求对象
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String doPost(HttpPost httpPost) throws IOException {
        HttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            // 返回结果JSON字符串
            HttpEntity responseEntity = response.getEntity();
            return EntityUtils.toString(responseEntity);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 执行POST请求，获取响应结果
     * @param httpPost HttpPost请求对象
     * @param header 请求头内容
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String doPost(HttpPost httpPost, Map<String, String> header) throws IOException {
        // 处理请求头信息
        if(!(Optional.ofNullable(header).orElse(new HashMap<>()).isEmpty())) {
            for(Map.Entry<String, String> entry : header.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        return doPost(httpPost);
    }

    // ------------------------ POST RequestBody接收参数方式（application/json） -------------------------------

    /**
     * 发送JSON/RequestBody形式的POST请求
     * @param url 请求URL
     * @param requestJsonString JSON格式的请求字符串
     * @return JSON格式响应结果
     * @param header 请求头内容
     * @throws Exception exception
     */
    public final String postByRequestBody(String url, String requestJsonString,
            Map<String, String> header) throws Exception {
        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        // 构建JSON格式的请求实体
        StringEntity stringEntity = new StringEntity(requestJsonString, Charset.forName("UTF-8"));
        stringEntity.setContentEncoding("UTF-8");
        stringEntity.setContentType("application/json");
        httpPost.setEntity(stringEntity);
        return doPost(httpPost, header);
    }

    /**
     * 发送JSON/RequestBody形式的POST请求
     * @param url 请求URL
     * @param requestJsonString JSON格式的请求字符串
     * @return JSON格式响应结果
     * @throws Exception exception
     */
    public final String postByRequestBody(String url, String requestJsonString) throws Exception {
        return postByRequestBody(url, requestJsonString, null);
    }


    // ------------------------ POST,GET URL拼接参数请求方式（application/x-www-form-urlencoded）--------------------

    /**
     * 发送UrlEncoded方式的POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @param header 请求头内容
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByUrlEncoded(String url, Map<String, String> params,
            Map<String, String> header) throws Exception {
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
        return doPost(httpPost, header);
    }

    /**
     * 发送UrlEncoded方式的POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByUrlEncoded(String url, Map<String, String> params) throws Exception {
        return postByUrlEncoded(url, params, null);
    }

    /**
     * 发送URLEncoded方式的GET请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws IOException exception
     */
    public final String getByUrlEncoded(String url, Map<String, String> params) throws IOException {
        return getByUrlEncoded(url, params, null);
    }

    public final String getByUrlEncoded(String url, Map<String, String> params,
            Map<String, String> header) throws IOException {
        // 拼接URL和请求参数
        String fullUrl = UrlUtils.appendUrlWithParams(url, params);
        // 发送请求
        HttpGet httpGet = new HttpGet(fullUrl);
        return doGet(httpGet, header);
    }

    // ------------------------ POST FormData表单请求方式（multipart/form-data） -------------------------------

    /**
     * 使用表单方式提交POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @param header 请求头内容
     * @param bytesKey 需要上传的二进制流的字段名
     * @param bytes 二进制流
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByFormData(String url, Map<String, String> params,
            Map<String, String> header, String bytesKey, byte[] bytes) throws Exception {
        // 构建请求表单
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for(Map.Entry<String, String> entry : params.entrySet()) {
            multipartEntityBuilder.addPart(entry.getKey(),
                    // 修复表单请求时，中文编码问题导致的请求参数无法被服务器端解析的问题  --hankchan
                    new StringBody(entry.getValue(), ContentType.create(
                            "multipart/form-data", "UTF-8")));
        }
        // 加入需要的byte二进制流
        multipartEntityBuilder.addPart(bytesKey,
                // 修复表单请求时，中文编码问题导致的请求参数无法被服务器端解析的问题  --hankchan
                new ByteArrayBody(bytes, ContentType.create(
                        "multipart/form-data", "UTF-8"), ""));
        // 构建请求参数实体
        HttpEntity requestEntity = multipartEntityBuilder.build();
        // 构建POST请求
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(requestEntity);
        // 执行POST
        return doPost(httpPost, header);
    }

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
        return postByFormData(url, params, null, bytesKey, bytes);
    }

    /**
     * 使用表单方式提交POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @param header 请求头内容
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByFormData(String url, Map<String, String> params,
            Map<String, String> header) throws Exception {
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
        return doPost(httpPost, header);
    }

    /**
     * 使用表单方式提交POST请求
     * @param url 请求URL
     * @param params 请求参数集合
     * @return JSON字符串响应结果
     * @throws Exception exception
     */
    public final String postByFormData(String url, Map<String, String> params) throws Exception {
        return postByFormData(url, params, null);
    }

}
