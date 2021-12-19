# fql

一种基于类graphql完成多表关联查询语言实现。

## 基本格式

每个查询都是一个选择集合（Set），由若干个选择（Selection)组合而成，其中集合使得{}表示。每个选择的格式如下：

```
alias? name arguments? selectionSet?
```
格式说明：

| 英文 | 名称          | 备注                |
| ------------ | ------------ | ---------------------- |
| alias         | 别名  | 可选，用于重命名，默认为后面的名称 |
| name         | 表名  | 必选，需要查询的表名      |
| arguments         | 参数  | 可选，以key_operator: value方式组织，是查询中的操作 |
| selectionSet         | 选择集 | 可选，需要查询的内容，可以是子查询   |

## 别名alias

当别名是...时，表示将子选择的结果直接输出当前层级，省去后面的表名。

## 名称name

当名称是...时，表示将本层级所示字段都输出。

## 操作符operator

参数以key_operator: value方式组织，其中key通常为表的字段名称，operator为本语言定义的操作符，已经支持操作符定义如下：

| operator | 操作          | 备注             |
| ------------ | ------------ | ---------------------- |
| eq           | `=` or `is`  | key's equals value or key is null |
| gt           | `>`          | key's greater than value |
| gte          | `>=`         | key's greater than or equal value |
| lt           | `<`          | key's less than value  |
| lte          | `<=`         | key's less than or equal value |
| neq          | `<>` or `is` | key's not equal value or is not null |
| like         | `LIKE`       | key's like '%value%' |
| start        |  `LIKE`        | key's like 'value%' |
| end          | `LIKE`       | key's like '%value' |
| in           | `IN`         | key's in (value's) |
| order       | `order`         | order by key  asc or desc |
| limit           | `limit`         | limit value |
| offset           | `offset`         | offset value |
| distinct |  `distinct `         | distinct |

# 示例

## 单表查询

```fql
{
    data:user(id_eq:1){
        ...
    }
}
```

```sql
select * from user as data where id = 1
```

## 嵌套查询

```
{
    person(id_eq:1){
        ...
        firends : firend(person_id_eq: $id)
        {
            name
        }
    }
}
```

```sql
-- step 1 
select * from person where id = 1
-- step 2
select * from firend as firends where firends.person_id = person.id
```

注：$id需要在当前层的选择中。

## 关联查询

```
{
    staff {
        ... : staff(manager_id: $id)
        {
            manager_name:name
        }
    }
}
```

```sql
select a.*, b.name as manager_name from staff a left join staff b on a.manager_id = b.id
```

注：关联查询实际是使用嵌套查询并同级输出。实际使用中需要注意1+N的问题。