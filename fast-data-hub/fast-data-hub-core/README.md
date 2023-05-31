## 文件结构

0 - 基于内存 MemoryTable
1 - ./data/wal/{0-n}.wal
2 - ./data/lsm/{topic}/{0-n}.lsm
3 - ./data/idx/{topic}.ix|.ts

## lsm结构
version = byte;
status = byte;
offset = byte[8];
start = byte[8];
body = byte[n];
size =byte[4];
end =byte[4];


body.status = byte; -1 删除
body.size = vlen; 总长度
body.offset = vlen;
body.timestamp = vlen;
body.schema = vlen;
body.length = vlen; payload 长度
body.payload = vbyte;
body.crc = byte[4]

