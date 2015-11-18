package com.kuaizhan.form.zookeeper;

public interface NodeCacheDataBuilder<T> {
    
    T build(byte[] data);

}
