package ltd.fdsa.database.model;

import lombok.Data;

@Data
public class Pair<K, V> {
    final K key;
    final V value;

    public Pair(K first, V second) {
        this.key = first;
        this.value = second;
    }
}
