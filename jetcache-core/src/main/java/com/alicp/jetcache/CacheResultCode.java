/**
 * Created on  13-09-09 18:16
 */
package com.alicp.jetcache;

/**
 * @author huangli
 */
public enum CacheResultCode {
    /**
     * 请求成功
     */
    SUCCESS,
    PART_SUCCESS,
    FAIL,
    /**
     * 数据不存在
     */
    NOT_EXISTS,
    EXISTS,
    EXPIRED
}
