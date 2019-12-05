# Lua for Redis

Lua是一种功能强大、高效、轻量级、可嵌入的脚本语言。
它是动态类型语言，通过使用基于寄存器的虚拟机解释字节码运行，并具有增量垃圾收集的自动内存管理，
是配置、脚本、快速原型设计的最佳选择。

Redis提供了丰富的指令集，但是仍然不能满足所有场景。
在一些特定场景下，需要自定义一些命令来完成某些功能。
因此，从2.6版本开始，Redis提供了Lua脚本支持，用户可以自己编写脚本来实现想要的功能。

# 例子

## lua文件重试使用
我们在Redis交互模式中，如果想要执行脚本文件，每次都退出来，执行完再连接一次也太麻烦了。

Redis提供了EVALSHA命令，使我们可以在交互模式执行脚本文件。

先将lua文件上传到服务器：
```
manxi@bogon  ~ redis-cli script load "$(cat a.lua)"
"aafcf8dc0a5d873bff3ee3e774f603ac754fa572"
```
然后用返回的sha串执行：
```
127.0.0.1:6379> evalsha aafcf8dc0a5d873bff3ee3e774f603ac754fa572 1 name hello
"hello JimGreen"
```