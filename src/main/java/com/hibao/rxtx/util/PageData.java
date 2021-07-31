package com.hibao.rxtx.util;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 封装map
 *
 * @author WANG
 * @create 2018-04-24 9:10
 **/
public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 6200890717274467507L;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                value = "";
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = value + values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }

        returnMap.put("token", request.getHeader("X-Token"));

        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key) {
        return (String) get(key);
    }

    @Override
    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return map.containsValue(value);
    }

    @Override
    public Set entrySet() {
        // TODO Auto-generated method stub
        return map.entrySet();
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return map.isEmpty();
    }

    @Override
    public Set keySet() {
        // TODO Auto-generated method stub
        return map.keySet();
    }

    @Override
    public void putAll(Map t) {
        // TODO Auto-generated method stub
        map.putAll(t);
    }

    @Override
    public int size() {
        // TODO Auto-generated method stub
        return map.size();
    }

    @Override
    public Collection values() {
        // TODO Auto-generated method stub
        return map.values();
    }


}
