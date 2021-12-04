package top.chenzhimeng.hr_health_check.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author M
 * @date 2021/05/06
 **/
public class HttpUtil {
    private static HttpUtil instance;

    private HttpUtil() {
    }

    public synchronized static HttpUtil getInstance() {
        if (instance == null) instance = new HttpUtil();
        return instance;
    }

    public String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (invalidIp(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (invalidIp(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (invalidIp(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (invalidIp(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (invalidIp(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private boolean invalidIp(String ipAddresses) {
        return ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses);
    }
}
