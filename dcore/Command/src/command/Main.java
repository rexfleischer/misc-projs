/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package command;

import com.rf.dcore.operation.KeySetOperations;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author REx
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
//        RandomAccessFile randomAccessFile = null;
//        try {
//
//            //Declare variables that we're going to write
//            Long line1 = 1234567890l;
//            Long line2 = 9876543120l;
//
//            //Create RandomAccessFile instance with read / write permissions
//            randomAccessFile = new RandomAccessFile("C:/Users/REx/Desktop/fundir/myFile.txt", "rw");
//
//            //Write two lines to the file
//            randomAccessFile.writeLong(line1);
//            randomAccessFile.writeLong(line2);
//
//            //Place the file pointer at the end of the first line
//            randomAccessFile.seek(8);
//
//            //Read data from the file
//            Long read = randomAccessFile.readLong();
//
//            //Print out the buffer contents
//            System.out.println(read);
//            System.out.println(Long.SIZE / 8);
//
//        } catch (FileNotFoundException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//
//                if (randomAccessFile != null)
//                    randomAccessFile.close();
//
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
        ByteBuffer buffer = ByteBuffer.allocate(20);
        //buffer.
        try
        {
            System.out.println(MD5("poopy poop da-de poop"));
            System.out.println(MD5("poopy poop da-de poop1"));
            System.out.println(MD5("poopy poop da-de poop2"));
            System.out.println(MD5("poopy poop da-de poop34"));

            System.out.println(Character.SIZE);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }



    }

    public static Long MD5(String text)
            throws NoSuchAlgorithmException,
            UnsupportedEncodingException
    {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();

        Long result = 0L;
        for (int i = 0; i < md5hash.length; i++)
        {
            result = (result << 2);
            result += ((md5hash[i] & 0x0F) + ( (md5hash[i] >>> 4) & 0x0F));
        }
        return result;
    }

}
