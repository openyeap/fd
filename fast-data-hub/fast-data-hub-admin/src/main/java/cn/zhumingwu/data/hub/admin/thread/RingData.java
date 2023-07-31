package cn.zhumingwu.data.hub.admin.thread;

import lombok.extern.slf4j.Slf4j;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RingData {
    private final Data[] data;

    private final int length;

    public RingData() {
        length = 256;
        this.data = new Data[length];
    }

    public Set<Long> getItem(long second) {
        var index = (int) second & this.length;
        return data[index].getItems();
    }

    public List<Long> fetchItem(long second) {
        var index = (int) second & this.length;
        var items = data[index].getItems().stream().map(m -> m).collect(Collectors.toList());
        data[index].getItems().clear();
        return items;
    }

    public List<Set<Long>> getAll() {
        var list = new ArrayList<Set<Long>>(this.length);
        for (var item : data) {
            list.add(item.getItems());
        }
        return list;
    }

    class Data {
        private final Set<Long> items = new HashSet<Long>();

        public Set<Long> getItems() {
            return items;
        }
    }
}
