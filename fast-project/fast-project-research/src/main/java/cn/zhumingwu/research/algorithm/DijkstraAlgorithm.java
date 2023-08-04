package cn.zhumingwu.research.algorithm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/*
 * 最短路径
 * (1) 找出“最便宜”的节点，即可在最短时间内到达的节点。
 * (2) 更新该节点的邻居的开销，其含义将稍后介绍。
 * (3) 重复这个过程，直到对图中的每个节点都这样做了。
 * (4) 计算最终路径。
 */
@Slf4j
public class DijkstraAlgorithm {
    // 使用一个HashMap表示图
    Map<String, Integer> graph = new HashMap<String, Integer>();
    // 使用一个HaspMap记录从起点到各个节点的开销，一开始可能不是最优方案，或未知（使用较大值表示），然后逐步更新。
    Map<String, Integer> costs = new HashMap<String, Integer>();
    // 使用一个HashMap记录结果的路径
    Map<String, String> path = new HashMap<String, String>();
    // 使用一个HashMap记录已经处理过的节点
    Map<String, Boolean> processed = new HashMap<String, Boolean>();

    public void demo(String[] args) {
        // 根据条件创建图
        graph.put("s-a", 6);
        graph.put("s-b", 2);
        graph.put("a-t", 1);
        graph.put("b-a", 3);
        graph.put("b-t", 5);
        // 记录从起点到各个节点的开销
        costs.put("a", 6);
        costs.put("b", 2);
        costs.put("t", 999); // 未知
        // 记录路径
        path.put("a", "s");
        path.put("b", "s");
        path.put("t", ""); // 未知

        do_computer();
    }

    void do_computer() {
        Entry<String, Integer> node = find_lowest_cost_node();
        while (node != null) {
            Integer cost = this.costs.get(node.getKey());
            HashMap<String, Integer> neighbors = get_neighbors(node.getKey());
            for (Entry<String, Integer> entry : neighbors.entrySet()) {
                Integer costNew = cost + entry.getValue();
                if (this.costs.get(entry.getKey()) > costNew) {
                    this.costs.put(entry.getKey(), costNew);
                    this.path.put(entry.getKey(), node.getKey());
                }
            }
            this.processed.put(node.getKey(), true);
            node = find_lowest_cost_node();
        }
        log.info("Costs:");
        for (String name : this.costs.keySet()) {
            String key = name;
            String value = this.costs.get(name).toString();
            log.info(key + " " + value);
        }
        log.info("Path:");
        String pathString = this.path.get("t");

        String printString = "t";
        while (!Strings.isNullOrEmpty(pathString)) {
            printString = pathString + " > " + printString;
            pathString = this.path.get(pathString);
        }
        log.info(printString);
    }

    /*
     * 得到所有节点中没有处理过的成本最低的节点
     */
    Entry<String, Integer> find_lowest_cost_node() {
        Integer lowestCost = Integer.MAX_VALUE;
        Entry<String, Integer> result = null;
        for (Entry<String, Integer> entry : this.costs.entrySet()) {
            String key = entry.getKey();
            if (this.processed.containsKey(key)) {
                continue;
            }
            if (entry.getValue() < lowestCost) {
                lowestCost = entry.getValue();
                result = entry;
            }
        }
        return result;
    }

    /*
     * 得到所有邻居
     */
    HashMap<String, Integer> get_neighbors(String start) {
        HashMap<String, Integer> list = new HashMap<String, Integer>();
        for (Entry<String, Integer> entry : this.graph.entrySet()) {
            String key = entry.getKey();
            if (key.startsWith(start)) {
                list.put(entry.getKey().replace(start + "-", ""), entry.getValue());
            }
        }
        return list;
    }
}
