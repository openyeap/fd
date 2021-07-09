
### create topic - streams-plaintext-input
```
bin/kafka-topics.sh --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-plaintext-input
```
### create topic - output topic
```
bin/kafka-topics.sh --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-wordcount-output \
    --config cleanup.policy=compact
```

### delete topic

```
bin/kafka-topics.sh --delete --zookeeper 192.168.2.38:2181 --topic 主题名
```

### list topic

```
bin/kafka-topics.sh --bootstrap-server localhost:9092 --describe
```


#### 接入源 source connector config 测试数据
```$xslt
{
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "incrementing.column.name": "id",
    "validate.non.null": "false",
    "connection.password": "postgres",
    "tasks.max": "1",
    "batch.max.rows": 100,
    "table.whitelist": "t_sample",
    "mode": "incrementing",
    "topic.prefix": "",
    "max.data.condition": "",
    "extends": "{\"cast($id$ as int4)\":\"id\",\"cast($brithday$ as timestamp)\":\"brithday\",\"cast($remarks$ as varchar)\":\"remarks\",\"cast($name$ as varchar)\":\"name\",\"cast($height$ as real)\":\"height\",\"cast($create_time$ as timestamp)\":\"create_time\",\"cast($gender$ as varchar)\":\"gender\",\"cast($weight$ as double precision)\":\"weight\",\"cast($age$ as int4)\":\"age\"}",
    "connection.user": "postgres",
    "connection.url": "jdbc:postgresql://192.168.2.38:5431/mti_source?user=postgres&password=postgres"
}
```
```$xslt

```

#### sink connector config 测试数据 

```$xslt
{
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.password": "postgres",
    "auto.evolve": "true",
    "tasks.max": "1",
    "topics": "20210409_t_sample",
    "connection.user": "postgres",
    "batch.size": 100,
    "auto.create": "true",
    "connection.url": "jdbc:postgresql://192.168.2.38:5431/mti_backup?user=postgres&password=postgres",
    "insert.mode": "insert",
    "tableName": "t_sample_20210408142622"
}
```

#### 落地源 source connector config 测试数据
````$xslt
{
    "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
    "incrementing.column.name": "id",
    "validate.non.null": "false",
    "connection.password": "postgres",
    "tasks.max": "1",
    "batch.max.rows": 100,
    "table.whitelist": "20210409_t_sample",
    "mode": "incrementing",
    "topic.prefix:": "",
    "max.data.condition": "",
    "extends": "{\"cast($id$ as int4)\":\"id\",\"cast($brithday$ as timestamp)\":\"brithday\",\"cast($remarks$ as varchar)\":\"remarks\",\"cast($name$ as varchar)\":\"name\",\"cast($height$ as real)\":\"height\",\"cast($create_time$ as timestamp)\":\"create_time\",\"cast($gender$ as varchar)\":\"gender\",\"cast($weight$ as double precision)\":\"weight\",\"cast($age$ as int4)\":\"age\"}",
    "connection.user": "postgres",
    "connection.url": "jdbc:postgresql://192.168.2.38:5431/mti_backup?user=postgres&password=postgres"
}
````

#### 落地源 sink connector config 测试数据 

```$xslt
{
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.password": "postgres",
    "auto.evolve": "false",
    "tasks.max": "1",
    "topics": "",
    "connection.user": "postgres",
    "batch.size": 100,
    "auto.create": "true",
    "connection.url": "jdbc:postgresql://192.168.2.38:5431/mti_backup?user=postgres&password=postgres",
    "insert.mode": "insert",
    "tableName": "ld_sample_sink"
}
```
