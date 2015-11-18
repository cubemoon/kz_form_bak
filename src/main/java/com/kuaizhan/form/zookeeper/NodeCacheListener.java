package com.kuaizhan.form.zookeeper;

public interface NodeCacheListener {
    /**
     * Called when a change has occurred
     */
    public void nodeChanged() throws Exception;
}