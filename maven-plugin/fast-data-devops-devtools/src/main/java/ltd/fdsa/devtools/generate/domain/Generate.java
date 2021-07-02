package ltd.fdsa.devtools.generate.domain;

import lombok.Data;

import java.util.List;

/**
 * 封装生成数据
 *
 * @date 2018/10/23
 */
@Data
public class Generate {
    private Basic basic = new Basic();
    private List<Field> fields;
    private Template template;
}
