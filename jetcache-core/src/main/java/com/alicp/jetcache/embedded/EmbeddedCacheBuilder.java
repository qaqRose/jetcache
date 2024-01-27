package com.alicp.jetcache.embedded;

import com.alicp.jetcache.AbstractCacheBuilder;

/**
 * 内嵌的缓存构造器
 * 有 Caffine 和 LinkedHashMapCache
 * Created on 16/9/7.
 *
 * @author huangli
 */
public class EmbeddedCacheBuilder<T extends EmbeddedCacheBuilder<T>> extends AbstractCacheBuilder<T> {

    public EmbeddedCacheBuilder(){
    }

    public static class EmbeddedCacheBuilderImpl extends EmbeddedCacheBuilder<EmbeddedCacheBuilderImpl> {
    }

    public static EmbeddedCacheBuilderImpl createEmbeddedCacheBuilder(){
        return new EmbeddedCacheBuilderImpl();
    }

    @Override
    public EmbeddedCacheConfig getConfig() {
        if (config == null) {
            config = new EmbeddedCacheConfig();
        }
        return (EmbeddedCacheConfig) config;
    }

    public T limit(int limit){
        getConfig().setLimit(limit);
        return self();
    }

    public void setLimit(int limit){
        getConfig().setLimit(limit);
    }

}
