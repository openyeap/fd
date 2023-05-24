package cn.zhumingwu.database.model;

public class Triplet<K, V, T> {

    final K key;
    final V value;
    final T type;
    public Triplet(K key, V value, T type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }
}


