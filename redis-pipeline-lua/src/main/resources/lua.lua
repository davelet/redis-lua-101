for i=1,tonumber(KEYS[1]) do
    redis.call('set', 'lua_'..i, '1')
end
return 'done'
