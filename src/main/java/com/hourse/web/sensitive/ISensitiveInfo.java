package com.hourse.web.sensitive;

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
