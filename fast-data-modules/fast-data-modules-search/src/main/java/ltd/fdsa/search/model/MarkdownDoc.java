package ltd.fdsa.search.model;

import lombok.*;
import org.elasticsearch.common.Strings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MarkdownDoc {
    private String title;
    private Collection<String> categories;
    private String type;
    private String summary;
    private String content;
    private Date datetime;
    private String source;

    public String toUrl() {
        try {
            Collection<String> list = new ArrayList<String>();
            if (this.categories != null && this.categories.size() > 0) {
                list.addAll(this.categories);
            }
            if (Strings.isNullOrEmpty(this.title)) {
                this.title = "untitled";
            }
            list.add(this.title);
            return String.join("/", list) + ".html";
        } catch (Exception ex) {
            return "untiled.html";
        }
    }
}
