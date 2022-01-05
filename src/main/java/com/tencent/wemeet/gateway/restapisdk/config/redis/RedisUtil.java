package com.tencent.wemeet.gateway.restapisdk.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author dongliang7
 * @projectName tenxun-meeting-api
 * @ClassName RedisUtil.java
 * @description: redis工具类  (这里只是列举常用的api) 不含通用方法  针对所有的hash 都是以h开头的方法
 *                                                           针对所有的Set  都是以s开头的方法
 *                                                           针对所有的List 都是以l开头的方法
 *                                                           针对所有的zSet 都是以zS开头的方法
 * @createTime 2021年11月29日 18:46:00
 */
@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /*********************************common***********************************/
    /**
     * 指定缓存失效时间
     *
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     *            键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     *            键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key
     *            可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    /*********************************String***********************************/

    /**
     * 普通缓存放入
     *
     * @param key
     *            键
     * @param value
     *            值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 普通缓存获取
     *
     * @param key
     *            键
     * @return 值
     */
    public Object get(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        }catch (Exception e) {
            log.error("[RedisUtil.get] [error] [key is {} ,e is {}]", key , e.getMessage());
            return null;
        }
    }

    /**
     * 下标索引缓存获取
     * 获取 {@code begin} 和 {@code end} 之间的 {@code key} 值的子串
     *
     * @param key
     *            键
     * @return 值
     */
    public Object getRange(String key ,long start, long end) {
        try {
            return redisTemplate.opsForValue().get(key, start, end);
        }catch (Exception e) {
            log.error("[RedisUtil.getRange] [error] [key is {}, [start is {},end is {},e is {}]", key , start , end , e.getMessage());
            return null;
        }
    }

    /**
     *
     * 将 value} 附加到 key
     *
     * @param key
     *         键
     * @param value
     *         值
     */
    public Integer append(String key ,String value){
        try {
            return redisTemplate.opsForValue().append(key, value);
        } catch (Exception e) {
            log.error("[RedisUtil.append] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return null;
        }
    }

    /**
     * 设置key的 value 并返回其旧值 , 如果key不存在 设置value，返回旧值为null
     * @param key
     *          键
     * @param value
     *          值
     */
    public Object getAndSet(String key ,Object value){
        try {
            return redisTemplate.opsForValue().getAndSet(key, value);
        } catch (Exception e) {
            log.error("[RedisUtil.getAndSet] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return null;
        }
    }

    /**
     * 如果不存在key，则设置 保存字符串 value
     * @param key
     *          键
     * @param value
     *          值
     */
    public Boolean setIfAbsent(String key ,Object value){
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value);
        } catch (Exception e) {
            log.error("[RedisUtil.setIfAbsent] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return false;
        }
    }

    /**
     * 如果key不存在 ，设置缓存和到期时间
     *
     * @param key
     *          键
     * @param value
     *          值
     * @param timeout
     *           过期时间
     * @param unit
     *           单位
     */
    public Boolean setIfAbsentExpiration(String key ,Object value ,long timeout, TimeUnit unit){
        try {
            return redisTemplate.opsForValue().setIfAbsent(key, value , timeout, unit);
        } catch (Exception e) {
            log.error("[RedisUtil.setIfAbsentExpiration] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return false;
        }
    }

    /**
     * 如果存在 key，则设置保存字符串 value
     * @param key
     *          键
     * @param value
     *          值
     */
    public Boolean setIfPresent(String key ,Object value){
        try {
            return redisTemplate.opsForValue().setIfPresent(key , value);
        } catch (Exception e) {
            log.error("[RedisUtil.setIfPresent] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return false;
        }
    }

    /**
     * 如果存在 key，则设置保存字符串和到期时间
     * @param key
     *           键
     * @param value
     *           值
     * @param timeout
     *           过期时间
     * @param unit
     *           单位
     */
    public Boolean setIfPresentExpiration(String key ,Object value ,long timeout, TimeUnit unit){
        try {
            return redisTemplate.opsForValue().setIfPresent(key, value , timeout, unit);
        } catch (Exception e) {
            log.error("[RedisUtil.setIfPresentExpiration] [error] [key is {},value is {},e is {}]", key,value , e.getMessage());
            return false;
        }
    }


    /**
     * 获取存储在key中的值的长度
     *
     * @param key   键
     */
    public Long size(String key) {
        try {
            return redisTemplate.opsForValue().size(key);
        } catch (Exception e) {
            log.error("[RedisUtil.size] [error] [key is {},e is {}]", key, e);
            return null;
        }
    }

    /**
     * 递增 适用场景： 高并发生成订单号，秒杀类的业务逻辑等。
     * 通过delta 递增 key 下存储为字符串值的整数值
     *
     * @param key
     *            键
     * @param delta
     *            要增加几(大于0)
     * @return
     */
    public Long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 将 key下存储为字符串值的整数值递减 delta
     *
     * @param key
     *            键
     * @param delta
     *            要减少几(小于0)
     * @return
     */
    public Long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().decrement(key, delta);
    }

    /*********************************Map***********************************/
    /**
     * HashGet
     *
     * @param key
     *            键 不能为null
     * @param item
     *            项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key
     *            键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     *
     * @param key
     *            键
     * @param map
     *            对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key
     *            键
     * @param map
     *            对应多个键值
     * @param time
     *            时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     *            键
     * @param item
     *            项
     * @param value
     *            值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key
     *            键
     * @param item
     *            项
     * @param value
     *            值
     * @param time
     *            时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key
     *            键 不能为null
     * @param item
     *            项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key
     *            键 不能为null
     * @param item
     *            项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key
     *            键
     * @param item
     *            项
     * @param by
     *            要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key
     *            键
     * @param item
     *            项
     * @param by
     *            要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /*********************************Set***********************************/
    /**
     * 根据key获取Set中的所有值
     *
     * @param key
     *            键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key
     *            键
     * @param value
     *            值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key
     *            键
     * @param values
     *            值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 将set数据放入缓存
     *
     * @param key
     *            键
     * @param time
     *            时间(秒)
     * @param values
     *            值 可以是多个
     * @return 成功个数
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key
     *            键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     *
     * @param key
     *            键
     * @param values
     *            值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /*********************************ZSet***********************************/

    /**
     * 增加有序集合
     *
     * @param key
     *            键
     * @param value
     *            值
     * @param seqNo
     *            序号（用来排序）
     * @return
     */
    public boolean zSSet(String key, Object value, double seqNo) {
        try {
            return redisTemplate.opsForZSet().add(key, value, seqNo);
        } catch (Exception e) {
            log.error("[RedisUtil.addZset] [error]", e);
            return false;
        }
    }

    /**
     * 获取zset指定范围内的集合
     *
     * @param key
     *           键
     * @param start
     *           范围开始
     * @param end
     *           范围结束
     * @return
     */
    public Set<Object> zSRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("[RedisUtil.rangeZset] [error] [key is {},start is {},end is {}]", key, start, end, e);
            return null;
        }
    }

    /**
     * 获取zset集合数量
     *
     * @param key
     *         键
     * @return
     */
    public Long zSSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("[RedisUtil.countZset] [error] [key is {}]", key, e);
            return 0L;
        }
    }

    /**
     * 根据key和value移除指定元素
     * 未查询到对应的key和value，返回0，否则返回1
     * @param key
     *          键
     * @param values
     *          值
     * @return
     */
    public Long zSRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取对应key和value的score
     *
     * @param key
     *          键
     * @param value
     *          值
     * @return
     */
    public Double zSScore(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 指定范围内元素排序
     * 从排序集中获取分数在 {@code min} 和 {@code max} 之间的元素
     *
     * @param key
     *          键
     * @param min
     *          排序最小值
     * @param max
     *          排序最大值
     * @return
     */
    public Set<Object> zSRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 指定元素增加指定分数
     * 用 {@code value} 在 sorted set by {@code increment} 中增加元素的分数。  如果value不存在 会自动添加
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Object zSIncrementScore(String key, Object value, double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    /**
     * 排名  确定排序集中具有 {@code value} 的元素的索引。
     *
     * @param key
     * @param value
     * @return
     */
    public Object zSRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /*********************************List***********************************/
    /**
     * 获取list缓存的内容
     *
     * @取出来的元素 总数 end-start+1
     *
     * @param key
     *            键
     * @param start
     *            开始 0 是第一个元素
     * @param end
     *            结束 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key
     *            键
     * @return
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key
     *            键
     * @param index
     *            索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key
     *            键
     * @param value
     *            值
     * @return
     */
    public boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    public boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key
     *            键
     * @param value
     *            值
     * @return
     */
    public boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key
     *            键
     * @param value
     *            值
     * @param time
     *            时间(秒)
     * @return
     */
    public boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0)
                expire(key, time);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key
     *            键
     * @param index
     *            索引
     * @param value
     *            值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(key, e);
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key
     *            键
     * @param count
     *            移除多少个
     * @param value
     *            值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }
}
