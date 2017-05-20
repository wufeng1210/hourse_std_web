package com.hourse.web.sensitive;

/**
 * 功能说明: 自定义脱敏接口<br>
 * 系统版本: v1.0<br>
 * 开发人员: @author guanhui<br>
 * 开发时间: 2016年10月24日<br>
 */
public interface ISensitiveInfo {

    /**
     * 自定义脱敏
     * @param source
     * @return
     */
    Object sensitive(Object source);

    /**
     * 脱敏标识
     * @return
     */
    String getKey();
}
