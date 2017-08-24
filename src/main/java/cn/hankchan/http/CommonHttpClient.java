package cn.hankchan.http;

/**
 * @author hankChan
 *         24/08/2017.
 */
public class CommonHttpClient extends AbstractHttpClient {

    public CommonHttpClient() {}

    public CommonHttpClient(String host, Integer port, Integer maxTotal,
            Integer maxPerRoute, Integer defaultMaxPerRoute) {
        super(host, port, maxTotal, maxPerRoute, defaultMaxPerRoute);
    }
}
