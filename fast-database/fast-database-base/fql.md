# FQL

快捷查询语句，一种基于antlr4实现类graphql的资源级联查询语言。可以广泛应用于前后端频繁交互的场景，将多个后端接口调用合并到一个请求中，并将多个接口返回的结果组合一个新的返回结果。

比如：我们需要获取一个用户（id=1)的角色信息，通常后端需要调用三个表：`t_user`、`t_role` 和关联表 `t_user_role`，使用FQL如下：


```
{
  user : t_user(user_id_eq:1) {
    name
    user_id
    roles : t_user_role(user_id_eq:$user_id) {
      role_id
      role_name : t_role(role_id_eq: $role_id) {
         name
      }
    }
  }
}
```
等效于执行以下SQL：
```sql
SELECT `t_user`.`user_id` AS `user_id`, `t_user`.`name` AS `name` FROM `t_user` AS `t_user` WHERE `t_user`.`user_id` = 1
SELECT `t_user_role`.`role_id` AS `role_id` FROM `t_user_role` AS `t_user_role` WHERE `t_user_role`.`user_id` = 1
SELECT `t_role`.`name` AS `name` FROM `t_role` AS `t_role` WHERE `t_role`.`role_id` = 1
SELECT `t_role`.`name` AS `name` FROM `t_role` AS `t_role` WHERE `t_role`.`role_id` = 0
```

输出的结果如下：
```json
{
  "user": {
    "name": "zhumingwu",
    "roles": [
      {
        "role_id": 1,
        "role_name": {
          "name": "support"
        }
      },
      {
        "role_id": 0,
        "role_name": {
          "name": "admin"
        }
      }
    ],
    "user_id": 1
  }
}
```

注：本说明以数据库查询为例，但FQL不局限于数据库查询。

## 基本语法

在FQL中，每个查询语句都是一个或多个选择（Selection）组成，即选择集合（SelectionSet），一个基本的选择（Selection）语法如下：

```
alias? name arguments* selectionSet?
```
格式说明：

| 英文 | 名称          | 备注                |
| ------------ | ------------ | ---------------------- |
| alias         | 别名  | 可选，用于重命名，默认为后面的资源名称 |
| name         | 资源名称 | 必选，需要查询的资源名称 |
| arguments         | 参数  | 零或多个参数组合，一组参数以key_operator: value方式组织，是查询中的操作 |
| selectionSet         | 选择集 | 可选，默认资源所有属性，可以是资源属性或另一个资源（子查询） |

在FQL中，用{}表示集合，所以一般一个完整的查询语句可以表示为：`{ selection1 selection2 ...}`;

## 别名alias

别名是对查询资源名的重命名，资源的查询结果会以别名表现在返回结果中。默认是资源名称。

## 名称name

资源名称，可以表示后端接口、表名或资源等，由后端服务提供和决定。

## 参数arguments

参数是若干个key_operator: value组合。在FQL中，使用()表示组合。组合内部为`与`操作，组合之间为`或`操作。比如：`
(age_gte:18, age_lt:25)(age_eq:35) `表示年龄(age)大于等于18且小于25或年龄等于35。

## 操作符operator

操作可以分为两大类，一类是对查询的过滤，比如：`eq` 、`in`等。另一类是对结果的操作，比如：`limit` 、`order`等

以下是本语言支持操作（可以根据实际应用进行扩展或更新）：

| 操作 | 含义          | 备注             |
| ------------ | ------------ | ---------------------- |
| eq           | `=` or `IS`  | key's equals value or key is null |
| gt           | `>`          | key's greater than value |
| gte          | `>=`         | key's greater than or equal value |
| lt           | `<`          | key's less than value  |
| lte          | `<=`         | key's less than or equal value |
| neq          | `<>` or `IS` | key's not equal value or is not null |
| like         | `LIKE`       | key's like '%value%' |
| start        |  `LIKE`        | key's like 'value%' |
| end          | `LIKE`       | key's like '%value' |
| in           | `IN`         | key's in (value's) |
| nin           | `NOT IN`         | key's in (value's) |
| order       | `order`         | order by key value |
| limit           | `limit`         | limit value |
| offset           | `offset`         | offset value |
| distinct |  `distinct`         | distinct |

注： 

1. 操作为 `eq` 或 `neq` 时，如果 value 为 `null` 过滤操作将自动转成 `is null` or `is not null`。
2. 操作为 `in` 或 `nin`时，如果value 不为组合`()`时，操作将自动转成 `eq` or `neq`。
3. 操作为`order`时，value为 `false`或 `"desc"`时，表示降序，其它情况都表现升序。
4. 如果参数名称中不包含下划线时，_eq可以省略 比如： `{user(id_eq:1)}` 等效于 `{user(id:1)}`  




# 示例

## 单表查询

```fql
{
    data:user(id_eq:1){
        ...  //此处...为扩展列出资源所有属性，类似 sql 中的 select *
    }
}
```
等效于：
```sql
select * from user as data where id = 1
```

## 嵌套查询（子查询）

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
等效于：
```sql
-- step 1 
select * from person where id = 1
-- step 2
select * from firend as firends where firends.person_id = $person.id
```

注：

1. 子查询必须有参数，子查询中的参数值可以使用$引用父查询的信息，比如这里的$id就是person中id信息。
2. 在实际使用中主查询为多条数据时，需要注意1+N的问题。

