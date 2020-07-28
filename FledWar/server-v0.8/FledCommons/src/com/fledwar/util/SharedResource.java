///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.fledwar.util;
//
//import java.util.Arrays;
//import java.util.Objects;
//
///**
// *
// * @author REx
// */
//public class SharedResource<T>
//{
//    private final T[] resources;
//    
//    private final boolean[] locked;
//    
//    public SharedResource(T[] resources)
//    {
//        if (resources.length < 1)
//        {
//            throw new IllegalArgumentException("resource length cannot be 0");
//        }
//        
//        this.resources = Arrays.copyOf(resources, resources.length);
//        this.locked = new boolean[resources.length];
//        Arrays.fill(locked, false);
//        
//        for(int i = 0; i < this.resources.length; i++)
//        {
//            Objects.requireNonNull(this.resources[i]);
//        }
//    }
//    
//    public T lockResource()
//            throws SharedResourceException
//    {
//        synchronized(locked)
//        {
//            int size = locked.length;
//            for(int i = 0; i < size; i++)
//            {
//                if (!locked[i])
//                {
//                    locked[i] = true;
//                    return resources[i];
//                }
//            }
//        }
//        
//        // non of the resources were available
//        throw new SharedResourceException("no resources available");
//    }
//    
//    public void unlockResource(T resource)
//            throws SharedResourceException
//    {
//        Objects.requireNonNull(resource, "resource");
//        synchronized(locked)
//        {
//            int size = locked.length;
//            for(int i = 0; i < size; i++)
//            {
//                if (resources[i].equals(resource) && locked[i])
//                {
//                    locked[i] = false;
//                    return;
//                }
//            }
//        }
//        
//        throw new SharedResourceException("unknown resource in pool");
//    }
//}
