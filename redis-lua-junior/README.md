# Lua for Redis

Lua是一种功能强大、高效、轻量级、可嵌入的脚本语言。
它是动态类型语言，通过使用基于寄存器的虚拟机解释字节码运行，并具有增量垃圾收集的自动内存管理，
是配置、脚本、快速原型设计的最佳选择。

Redis提供了丰富的指令集，但是仍然不能满足所有场景。
在一些特定场景下，需要自定义一些命令来完成某些功能。
因此，从2.6版本开始，Redis提供了Lua脚本支持，用户可以自己编写脚本来实现想要的功能。

# 例子
## eval
redis使用eval 执行脚本，脚本使用引号包起来。单引号和双引号都可以，必要匹配就行。

脚本后面先跟一个数字表示有几个参数，没有参数也要写0。每一个参数都是一个键值对：第一个是键，会被加入KEYS数组，
第二个是值，会被加入ARGV数组。
```bash
manxi@bogon  ~  redis-cli
127.0.0.1:6379> eval 'local val=KEYS[1] return val.." 999  "..ARGV[1]' 1 key1 value1
"key1 999  value1"
127.0.0.1:6379>
127.0.0.1:6379> eval 'local a = KEYS[2] return KEYS[1]..a..ARGV[1]..ARGV[2]' 2 KEY1 V1 K2 V2
"KEY1V1K2V2"
```
上面例子中的两个点（..）是lua语言是的字符串拼接。

如果要操作Redis，可以使用redis.call方法：
```bash
127.0.0.1:6379> set name JimGreen
OK
127.0.0.1:6379> get name
"JimGreen"
127.0.0.1:6379> eval 'return KEYS[1].." "..ARGV[1].." "..redis.call("get", "name")' 1 kkk1 vvv1
"kkk1 vvv1 JimGreen"
```
如果redis获取失败，会报错：
```bash
127.0.0.1:6379> del name
(integer) 1
127.0.0.1:6379> eval 'return KEYS[1].." "..ARGV[1].." "..redis.call("get", "name")' 1 kkk1 vvv1
(error) ERR Error running script (call to f_4d387d042bae7a82b70065cb64d202d3fe1c4791): @user_script:1: user_script:1: attempt to concatenate a boolean value
```

