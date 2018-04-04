package com.fancy.util.对象工具;

import org.apache.commons.beanutils.*;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

/**
 *   <!-- commons-beanutils -->
     <dependency>
        <groupId>commons-beanutils</groupId>
        <artifactId>commons-beanutils</artifactId>
        <version>1.7.0</version>
        <exclusions>
            <exclusion>
                <groupId>commons-logging</groupId>
                <artifactId>commons-logging</artifactId>
            </exclusion>
        </exclusions>
     </dependency>
 */
public class MyBeanUtils {

    protected static PropertyUtilsBean getInstance() {
        return BeanUtilsBean.getInstance().getPropertyUtils();
    }

    /**
     * 将Map内的key与Bean中属性相同的内容复制到BEAN中
     *
     * @param bean       Object
     * @param properties Map
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void copyMap2Bean(Object bean, Map properties) throws
            IllegalAccessException, InvocationTargetException {
        // Do nothing unless both arguments have been specified
        if ((bean == null) || (properties == null)) {
            return;
        }
        // Loop through the property name/value pairs to be set
        Iterator names = properties.keySet().iterator();
        while (names.hasNext()) {
            String name = (String) names.next();
            // Identify the property name and value(s) to be assigned
            if (name == null) {
                continue;
            }
            Object value = properties.get(name);
            try {
                Class clazz = PropertyUtils.getPropertyType(bean, name);
                if (null == clazz) {
                    continue;
                }
                String className = clazz.getName();
                if (className.equalsIgnoreCase("java.sql.Timestamp")) {
                    if (value == null || value.equals("")) {
                        continue;
                    }
                }
                getInstance().setSimpleProperty(bean, name, value);
            } catch (NoSuchMethodException e) {
                continue;
            }
        }
    }


    public static void copyBean2Map(Map map, Object bean) {
        PropertyDescriptor[] pds = PropertyUtils.getPropertyDescriptors(bean);
        for (int i = 0; i < pds.length; i++) {
            PropertyDescriptor pd = pds[i];
            String propname = pd.getName();
            try {
                Object propvalue = PropertyUtils.getSimpleProperty(bean, propname);
                map.put(propname, propvalue);
            } catch (IllegalAccessException e) {
                //e.printStackTrace();
            } catch (InvocationTargetException e) {
                //e.printStackTrace();
            } catch (NoSuchMethodException e) {
                //e.printStackTrace();
            }
        }
    }

    public static void copyBean2Bean(Object dest, Object orig) throws Exception {
        convert(dest, orig);
    }

    private static void convert(Object dest, Object orig) throws
            IllegalAccessException, InvocationTargetException {

        // Validate existence of the specified beans
        if (dest == null) {
            throw new IllegalArgumentException
                    ("No destination bean specified");
        }
        if (orig == null) {
            throw new IllegalArgumentException("No origin bean specified");
        }

        // Copy the properties, converting as necessary
        if (orig instanceof DynaBean) {
            DynaProperty origDescriptors[] =
                    ((DynaBean) orig).getDynaClass().getDynaProperties();
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
                if (PropertyUtils.isWriteable(dest, name)) {
                    Object value = ((DynaBean) orig).get(name);
                    try {
                        getInstance().setSimpleProperty(dest, name, value);
                    } catch (Exception e) {
                        ; // Should not happen
                    }

                }
            }
        } else if (orig instanceof Map) {
            Iterator names = ((Map) orig).keySet().iterator();
            while (names.hasNext()) {
                String name = (String) names.next();
                if (PropertyUtils.isWriteable(dest, name)) {
                    Object value = ((Map) orig).get(name);
                    try {
                        getInstance().setSimpleProperty(dest, name, value);
                    } catch (Exception e) {
                        ; // Should not happen
                    }

                }
            }
        } else
      /* if (orig is a standard JavaBean) */ {
            PropertyDescriptor origDescriptors[] =
                    PropertyUtils.getPropertyDescriptors(orig);
            for (int i = 0; i < origDescriptors.length; i++) {
                String name = origDescriptors[i].getName();
//              String type = origDescriptors[i].getPropertyType().toString();
                if ("class".equals(name)) {
                    continue; // No point in trying to set an object's class
                }
                if (PropertyUtils.isReadable(orig, name) &&
                        PropertyUtils.isWriteable(dest, name)) {
                    try {
                        Object value = PropertyUtils.getSimpleProperty(orig, name);
                        getInstance().setSimpleProperty(dest, name, value);
                    } catch (IllegalArgumentException ie) {
                        ; // Should not happen
                    } catch (Exception e) {
                        ; // Should not happen
                    }

                }
            }
        }

    }

}