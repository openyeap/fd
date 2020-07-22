package ltd.fdsa.search.model.view;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ViewSource {
  /** 资源编号 */
  private String id;
  /** 资源类型 */
  private String type;
  /** 资源标题 */
  private String title;
  /** 资源摘要 */
  private String summary;
  /** 资源内容 */
  private String content;
  /** 资源链接 */
  private String url;
  /** 资源来源 */
  private String source;
}
