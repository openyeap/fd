package cn.zhumingwu.starter.jdbc.sql;

import lombok.Data;

import java.util.LinkedList;

@Data
public class BatchSql {
    LinkedList<SingleSql> sqls;
}
