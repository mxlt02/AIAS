package top.aias.training.common.utils;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端设备信息
 *
 * @author Calvin
 * @mail 179209347@qq.com
 * @website www.aias.top
 */
@Slf4j
public class UserAgentUtil {
    private UserAgent userAgent;
    private String userAgentString;
    private HttpServletRequest request;

    public UserAgentUtil(HttpServletRequest request) {
        this.request = request;
        userAgentString = request.getHeader("User-Agent");
        userAgent = UserAgent.parseUserAgentString(userAgentString);
    }

    /**
     * 获取浏览器类型
     * Get broswer type
     */
    public String getBrowser() {
        if (null == userAgent) {
            return "";
        }
        return userAgent.getBrowser().getName();
    }

    /**
     * 获取操作系统
     * Get os info
     */
    public String getOS() {
        if (null == userAgent) {
            return "Unknown Device";
        }
        return userAgent.getOperatingSystem().getName();
    }
}
