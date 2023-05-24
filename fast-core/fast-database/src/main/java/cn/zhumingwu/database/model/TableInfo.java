package cn.zhumingwu.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TableInfo {
    String name;
    String alias;
    String code;
}
