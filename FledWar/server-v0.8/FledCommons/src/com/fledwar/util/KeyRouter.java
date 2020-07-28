///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.util;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//
///**
// *
// * @author REx
// */
//public final class KeyRouter
//{
//    public static <T> Map<String, T> convertFromClasses(
//            Class<T> type,
//            Map<String, Class> classes)
//            throws Exception
//    {
//        Map<String, T> result = new HashMap<>();
//        
//        Iterator<Map.Entry<String, Class>> it = classes
//                .entrySet().iterator();
//        while(it.hasNext())
//        {
//            Map.Entry<String, Class> entry = it.next();
//            
//            T instance = (T) entry.getValue()
//                    .getConstructor()
//                    .newInstance();
//            result.put(entry.getKey(), instance);
//        }
//        
//        return Collections.unmodifiableMap(result);
//    }
//    
//    public static <T> Map<String, T> convertFromNames(
//            Map<String, String> classes)
//            throws Exception
//    {
//        Map<String, T> result = new HashMap<>();
//        
//        Iterator<Map.Entry<String, String>> it = classes
//                .entrySet().iterator();
//        while(it.hasNext())
//        {
//            Map.Entry<String, String> entry = it.next();
//            
//            T instance = (T) Class.forName(entry.getValue())
//                    .getConstructor()
//                    .newInstance();
//            result.put(entry.getKey(), instance);
//        }
//        
//        return Collections.unmodifiableMap(result);
//    }
//}
