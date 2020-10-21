package com.hens.common.utils;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author henry
 * @date 2020/10/20
 */
public class BeanUtils {

    private static final DozerBeanMapper MAPPER = new DozerBeanMapper();

    static {
        List<String> files = new ArrayList<String>();
        files.add("bean.xml");
        MAPPER.setMappingFiles(files);
    }

    public static String getPropertyString(Object bean, String propName) {
        Object prop = getProperty(bean, propName);
        return (prop == null) ? null : prop.toString();
    }

    public static Object getProperty(Object bean, String propName) {
        try {
            return PropertyUtils.getProperty(bean, propName);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) {
        try {
            return PropertyUtils.getPropertyDescriptors(clazz);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static void copy(Object orig, Object dest) {
        if (orig == null) {
            return;
        }

        MAPPER.map(orig, dest);
    }

    public static <T> T convert(Object orig, Class<T> clazz) {
        if (orig == null) {
            return null;
        }

        return MAPPER.map(orig, clazz);
    }

    public static Object deepClone(Object orig) {
        if (null == orig) {
            return null;
        }
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bao);
            objectOutputStream.writeObject(orig);
            return new ObjectInputStream(new ByteArrayInputStream(bao.toByteArray())).readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> convertList(List<?> orig, Class<T> clazz) {
        List<T> dest = null;

        if (!CollectionUtils.isEmpty(orig)) {
            dest = new ArrayList<T>();
            for (Object each : orig) {
                T destEntry = convert(each, clazz);
                dest.add(destEntry);
            }

        } else if (orig != null) {
            dest = new ArrayList<T>();
        }
        return dest;
    }

    public static <T> T convert(String str, String split, String[] keyArray, Class<T> clazz) {
        String[] strArray = StringUtils.split(str, split);
        Map<String, String> strMap = new HashMap<String, String>();
        int i = 0;
        for (String key : keyArray) {
            strMap.put(key, strArray[i]);
            i++;
        }
        return convert(strMap, clazz);
    }

}
