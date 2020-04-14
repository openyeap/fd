package ltd.fdsa.demo.model.view;

import lombok.Data;

import java.util.List;

@Data
public class ViewMenu {

	String id;
	String name;
	String displayName;
	String description;
	Integer type;
	String event;
	String path;
	String imageUrl;
	String rate;
	String isShow;
	List<ViewMenu> children;

	public ViewMenu(String id, String name, String displayName, String description, Integer type, String event, String path, String url, String rate, String isShow) {
	}
}
