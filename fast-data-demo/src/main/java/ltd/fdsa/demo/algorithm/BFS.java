package ltd.fdsa.demo.algorithm;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.springframework.util.StringUtils;

/*
 *  Breadth-First Search 
 */
public class BFS {
	// 使用一个HashMap表示关系
	Map<String, String[]> graph = new HashMap<String, String[]>();
	// 使用一个队列表示搜索的广度。
	Queue<String> queue = new LinkedList<String>();
//使用一个HashMap记录已经处理过的人
	Map<String, Boolean> processed = new HashMap<String, Boolean>();

	public void demo(String[] args) {
		// 根据条件创建图
		graph.put("I", new String[] { "F1", "F2", "F3", "F4", "F5", "F6", "F7" });
		graph.put("F1", new String[] { "F1", "F12", "F3", "F7", "F8" });
		graph.put("F2", new String[] { "F11", "F12", "F3", "F4", "F5" });
		graph.put("F3", new String[] { "F1", "F12", "F3", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F4", new String[] { "F1", "F22", "F3", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F5", new String[] { "F3", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F6", new String[] { "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F7", new String[] { "F1", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F8", new String[] { "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8" });
		graph.put("F11", new String[] { "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8" });

		queue.offer("I");
		String result = do_computer();

		System.out.println("result: " + result);

	}

	String do_computer() {
		int level=0;
		String person = this.queue.poll();
		while (!StringUtils.isEmpty(person)) {
			level++;	
			if (this.is_satisfied(person)) {
				return String.format("get satisfied person:%s,at level:%d", person,level);
			}
			if (!this.processed.containsKey(person)) {
				this.processed.put(person, true);
				if (this.graph.containsKey(person)) {
					for (String friend : graph.get(person)) {
						this.queue.offer(friend);
					}
				}
			}

			person = this.queue.poll();
		}
		return "Not Found";
	}

	Boolean is_satisfied(String person) {
		return person.endsWith("8");
	}

}
