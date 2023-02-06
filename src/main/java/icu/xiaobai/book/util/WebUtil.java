package icu.xiaobai.book.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import icu.xiaobai.book.entity.response.CustomResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Component
public class WebUtil {
    ObjectMapper objectMapper;

    public WebUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Deprecated
    public static void okResponse(HttpServletResponse response, String result) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void okResponse(HttpServletResponse response, CustomResponse<T> result) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().println(objectMapper.writeValueAsString(result));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public String getIpAddr(HttpServletRequest request) throws UnknownHostException {
        String ipAddress;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                ipAddress = InetAddress.getLocalHost().getHostAddress();
            }
        }
        // 通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null) {
            if (ipAddress.contains(",")) {
                return ipAddress.split(",")[0];
            } else {
                return ipAddress;
            }
        } else {
            return null;
        }

    }
}
