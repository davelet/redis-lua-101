# Lua for Redis

Lua是一种功能强大、高效、轻量级、可嵌入的脚本语言。
它是动态类型语言，通过使用基于寄存器的虚拟机解释字节码运行，并具有增量垃圾收集的自动内存管理，
是配置、脚本、快速原型设计的最佳选择。

Redis提供了丰富的指令集，但是仍然不能满足所有场景。
在一些特定场景下，需要自定义一些命令来完成某些功能。
因此，从2.6版本开始，Redis提供了Lua脚本支持，用户可以自己编写脚本来实现想要的功能。

# 例子

## lua文件
可以先写一个Lua文件，然后使用redis-cli命令来执行：
```
manxi@bogon  ~  cat a.lua
local aname=redis.call('get', KEYS[1])
local hi=ARGV[1]
local res=hi..' '..aname
return res
```
然后执行：
```
manxi@bogon  ~  redis-cli --eval a.lua name , hello
"hello JimGreen"
```
可以看到比直接执行lua代码少传了参数个数，并且参数之间使用逗号分隔。注意：逗号前后都要有空格！