package cn.zhumingwu.dataswitch.core.model;

import cn.zhumingwu.base.config.Configuration;
import lombok.*;

import java.io.Serializable;
import java.util.Map;

@Data
@ToString
@Builder
public class NewService  implements Serializable {
    private static final long serialVersionUID = 42L;
    /*
    唯一
     */
    private String id;
    /*
     * 同类
     * */
    private String name;

    private String value;
    /*
     * 可处理的插件
     * */
    private Map<String, Configuration> handles;
    private String url;
    private Map<String, String> meta;

}
