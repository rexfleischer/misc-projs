///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.rf.fled.presistance.fileio;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//
///**
// *
// * @author REx
// */
//public class FledFile_RandomAccessFile implements RecordFile
//{
//    
//    
//    private RandomAccessFile file;
//    
//    private int metaLength;
//    
//    private int blocksUsed;
//    
//    private int[] blockStarts;
//    
//    private int userMetaLength;
//    
//    private byte[] userMeta;
//    
//    public FledFile_RandomAccessFile(String filename)
//            throws IOException, FileNotFoundException
//    {
//        File _file = new File(filename);
//        this.file = new RandomAccessFile(_file, "rw");
//        
//        
//    }
//
//    @Override
//    public long fileSize()
//            throws IOException
//    {
//        return file.length();
//    }
//
//    @Override
//    public void insert(byte[] bytes, int index, boolean replace) 
//            throws IOException 
//    {
//        
//    }
//
//    @Override
//    public int numOfRecords() 
//            throws IOException
//    {
//        return blocksUsed;
//    }
//
//    @Override
//    public byte[] read(int index) throws IOException
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public long recordSize(int index) throws IOException 
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void remove(int index) throws IOException 
//    {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public byte[] getMeta() {
//        return userMeta;
//    }
//
//    @Override
//    public void setMeta(byte[] meta) {
//        userMeta = meta;
//    }
//    
//    
//    
//
////    @Override
////    public long size() 
////            throws IOException
////    {
////        return file.length();
////    }
////
////    @Override
////    public void write(byte[] bytes, int pos) 
////            throws IOException
////    {
////        file.write(bytes, pos, bytes.length);
////    }
////
////    @Override
////    public void insert(byte[] bytes, int pos)
////            throws IOException
////    {
////        byte[] read = new byte[(int)file.length() - pos];
////        file.read(read, pos, read.length);
////        byte[] putting = new byte[read.length + bytes.length];
////        System.arraycopy(bytes, 0, putting, 0, bytes.length);
////        System.arraycopy(putting, bytes.length, read, 0, read.length);
////        file.write(putting, pos, putting.length);   
////    }
////
////    @Override
////    public void insertSome(byte[] bytes, int pos, int amount) 
////            throws IOException
////    {
////        if (bytes.length == amount)
////        {
////            file.write(bytes, pos, bytes.length);
////            return;
////        }
////        long length = file.length() - amount;
////        byte[] read = new byte[(int)file.length() - pos - amount];
////        file.read(read, pos + amount, read.length);
////        byte[] putting = new byte[read.length + bytes.length];
////        System.arraycopy(bytes, 0, putting, 0, bytes.length);
////        System.arraycopy(putting, bytes.length, read, 0, read.length);
////        file.write(putting, pos, putting.length);
////        file.setLength(length);
////    }
////
////    @Override
////    public void read(byte[] dest, int pos) 
////            throws IOException
////    {
////        file.read(dest, pos, dest.length);
////    }
////
////    @Override
////    public void remove(int pos, int amount) 
////            throws IOException 
////    {
////        byte[] shifting = new byte[(int)file.length() - pos + amount];
////        file.read(shifting, pos + amount, shifting.length);
////        file.write(shifting, pos, shifting.length);
////        file.setLength(file.length() - amount);
////    }
////    
//}
