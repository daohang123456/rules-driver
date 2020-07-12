package org.syq.bean;

/**
 * Created by cage on 2020-07-12
 */

public class Tuple<K, V> {
    public K k;
    public V v;

    public Tuple(K k, V v) {
        this.k = k;
        this.v = v;
    }
}
