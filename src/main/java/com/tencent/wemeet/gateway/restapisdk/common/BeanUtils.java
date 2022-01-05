package com.tencent.wemeet.gateway.restapisdk.common;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author D-L
 * @program:
 * @description: 实体类之间转换
 * @date 2020-11-10 16:43:58
 */

public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 持有Dozer单例, 避免重复创建DozerMapper消耗资源.
     */
    private static DozerBeanMapper dozer = new DozerBeanMapper();

    /**
     * 通过无参数实例化目标对象和复制属性，将POJO对象转换成相应的对象
     *
     * @param source 原对象
     * @param type   目标类型
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> type) {
        T target = instantiateClass(type);
        if (source == null) {
            return target;
        }
        copyProperties(source, target);
        return target;
    }

    /**
     * 通过无参数实例化目标对象和复制属性，将两个POJO对象按照顺序转换成相应的对象
     * 如果原对象有相同的属性 会出现属性值覆盖
     *
     * @param source1 原对象1
     * @param source2 原对象2
     * @param type    目标类型
     * @param <T>
     * @return 返回转换后的对象
     */
    public static <T> T converts(Object source1, Object source2, Class<T> type) {
        T target = instantiateClass(type);
        if (source1 == null && source2 == null) {
            return target;
        }
        copyProperties(source1, target);
        copyProperties(source2, target);
        return target;
    }

    /**
     * 通过无参数实例化目标对象和复制属性，将POJO对象转换成相应的对象，可忽略部分属性
     *
     * @param source           原对象
     * @param type             目标类型
     * @param ignoreProperties 忽略的属性列表
     * @return 转换后的对象
     */
    public static <T> T convert(Object source, Class<T> type, String[] ignoreProperties) {
        T target = instantiateClass(type);
        copyProperties(source, target, ignoreProperties);
        return target;
    }

    /**
     * 通过无参数实例化目标对象和复制属性，将POJO对象批量转换成指定类型的对象
     *
     * @param sources List 原对象列表
     * @param type    目标类型
     * @return List 目标类型对象列表
     */
    public static <T, E> List<T> convert(List<E> sources, Class<T> type) {
        List<T> items = new ArrayList<T>();
        if (sources == null) {
            return items;
        }
        Iterator<E> iter = sources.iterator();
        while (iter.hasNext()) {
            items.add(convert(iter.next(), type));
        }
        return items;
    }

    /**
     * 通过无参数实例化目标对象和复制属性，将POJO对象批量转换成指定类型的对象
     *
     * @param sources          List 原对象列表
     * @param type             目标类型
     * @param ignoreProperties 忽略的属性列表
     * @return List 目标类型对象列表
     */
    public static <T, E> List<T> convert(List<E> sources, Class<T> type,
                                         Class<?> editable, String[] ignoreProperties) {
        List<T> items = new ArrayList<T>();
        if (sources == null) {
            return items;
        }
        Iterator<E> iter = sources.iterator();
        while (iter.hasNext()) {
            items.add(convert(iter.next(), type, ignoreProperties));
        }
        return items;
    }

    /**
     * 将Map的属性设置到bean中
     *
     * @param bean
     * @param properties
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void populate(Object bean, Map<String, ?> properties)
            throws IllegalAccessException, InvocationTargetException {
        org.apache.commons.beanutils.BeanUtils.populate(bean, properties);
    }


    /**
     * 将Bean转化为Map对象
     *
     * @param bean
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Map<?, ?> describe(Object bean)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return org.apache.commons.beanutils.BeanUtils.describe(bean);
    }

    /**
     * 对bean的特定属性赋值
     *
     * @param bean
     * @param name
     * @param value
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyProperty(Object bean, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        org.apache.commons.beanutils.BeanUtils.copyProperty(bean, name, value);
    }

    /**
     * 获取bean的特定属性
     *
     * @param bean
     * @param name
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String getProperty(Object bean, String name)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        return org.apache.commons.beanutils.BeanUtils.getProperty(bean, name);
    }

    /**
     * 基于Dozer转换对象的类型.
     *
     * @param source
     * @param destinationClass
     * @return
     */
    public static <T> T map(Object source, Class<T> destinationClass) {
        return dozer.map(source, destinationClass);
    }

    /**
     * 基于Dozer转换List中对象的类型.
     *
     * @param sourceList
     * @param destinationClass
     * @return
     */
    public static <T, E> List<T> map(List<E> sourceList,
                                     Class<T> destinationClass) {
        List<T> destinationList = new ArrayList<T>();
        for (Object sourceObject : sourceList) {
            T destinationObject = dozer.map(sourceObject, destinationClass);
            destinationList.add(destinationObject);
        }
        return destinationList;
    }

    /**
     * 复制对象到一个已经存在的对象
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public static void mapProperties(Object source, Object target) {
        dozer.map(source, target);
    }

    /**
     * 复制对象的指定属性
     *
     * @param source
     * @param target
     * @param names
     * @throws BeansException
     */
    public static void copyPropertiesByNames(Object source, Object target,
                                             String names) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");
        Assert.hasText(names, "names must not be empty");
        String[] keys = names.split(",");
        for (String name : keys) {
            name = name.trim();
            PropertyDescriptor targetPd = getPropertyDescriptor(
                    target.getClass(), name);
            PropertyDescriptor sourcePd = getPropertyDescriptor(
                    source.getClass(), name);
            if (sourcePd != null && sourcePd.getReadMethod() != null
                    && targetPd != null && targetPd.getWriteMethod() != null) {
                try {
                    Method readMethod = sourcePd.getReadMethod();
                    if (!Modifier.isPublic(readMethod.getDeclaringClass()
                            .getModifiers())) {
                        readMethod.setAccessible(true);
                    }
                    Object value = readMethod.invoke(source);
                    Method writeMethod = targetPd.getWriteMethod();
                    if (!Modifier.isPublic(writeMethod.getDeclaringClass()
                            .getModifiers())) {
                        writeMethod.setAccessible(true);
                    }
                    writeMethod.invoke(target, value);
                } catch (Throwable ex) {
                    throw new FatalBeanException(
                            "Could not copy properties from source to target",
                            ex);
                }
            }
        }
    }

    /**
     * 将Bean的指定属性集转化为Map对象
     *
     * @param bean
     * @param names
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> describe(Object bean, String names)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        String[] attrs = StringUtils.split(names, ",");
        for (int i = 0; i < attrs.length; i++) {
            attrs[i] = attrs[i].trim();
        }
        return describe(bean, attrs);
    }

    /**
     * 将Bean的指定属性集转化为Map对象
     *
     * @param bean
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    public static Map<String, Object> describe(Object bean, String[] names)
            throws IllegalAccessException, InvocationTargetException,
            NoSuchMethodException {
        Assert.notNull(bean, "Source must not be null");
        Assert.notEmpty(names, "names must not be empty");
        Map<String, Object> map = new HashMap<String, Object>();
        for (String name : names) {
            map.put(name, getProperty(bean, name));
        }
        return map;
    }
}