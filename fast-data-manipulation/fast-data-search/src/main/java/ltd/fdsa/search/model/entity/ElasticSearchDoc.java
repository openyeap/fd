package ltd.fdsa.search.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ElasticSearchDoc {
  private String id;
  private String type;
  private String title;
  private String summary;
  private String content;
  private String url;
  private Date datetime;
  private String source;
}
