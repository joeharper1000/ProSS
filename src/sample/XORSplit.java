package sample;

import javafx.scene.control.Alert;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by cf913 on 14/05/16.
 */
public class XORSplit {


    public static String split(String message, int shares) {

        //final long start = System.nanoTime();
        String result = "";
        BigInteger bitsize = new BigInteger(message.getBytes());
        //String binary = new BigInteger(message.getBytes()).toString(2);


        String[] list = newshares(bitsize, shares);
        BigInteger px = computeXor(bitsize, list);
        list[shares-1] = String.valueOf(px);



        //BigInteger secret = computeXor(list);

        //System.out.println("Secret : " + new String(secret.toByteArray()));
        for (String s: list) {
            result = result.concat(s+"\n");
        }
        //System.out.println("Runtime:" + (System.nanoTime() - start));
        return result;
    }

    //for VSS only
    public static String split(int message, int shares, int xor) {

        //final long start = System.nanoTime();
        String result = "";
        BigInteger bitsize = (new BigInteger(String.valueOf(message)));
        //String binary = new BigInteger(message.getBytes()).toString(2);


        String[] list = newshares(bitsize, shares);
        BigInteger px = computeXor(bitsize, list);
        list[shares-1] = String.valueOf(px);



        //BigInteger secret = computeXor(list);

        //System.out.println("Secret : " + new String(secret.toByteArray()));
        for (String s: list) {
            result = result.concat(s+"\n");
        }
        //System.out.println("Runtime:" + (System.nanoTime() - start));
        return result;
    }

    public static String combine(int num, String shares) {
        final long start = System.nanoTime();
        String[] list = shares.split("\\n");
        if (list.length != num) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
            return "";
        }
        BigInteger result = computeXor(list);
        System.out.println("Runtime:" + (System.nanoTime() - start));
        return new String(result.toByteArray());

    }
    //only for VSS
    public static String combine(String shares) {
        //final long start = System.nanoTime();
        String[] list = shares.split("\\n");
        BigInteger result = computeXor(list);
        //System.out.println("Runtime:" + (System.nanoTime() - start));
        return String.valueOf(result);

    }

    private static BigInteger computeXor(BigInteger bitsize, String[] list) {
        BigInteger result = bitsize;
        for (int i = 0; i < list.length-1; i++) {
            result = result.xor(new BigInteger(list[i]));
        }
        return result;
    }

    private static BigInteger computeXor(String[] list) {
        BigInteger result = new BigInteger(list[0]);
        for (int i = 1; i < list.length; i++) {
            result = result.xor(new BigInteger(list[i]));
        }
        return result;
    }



    public static String[] newshares(BigInteger bitsize, int shares) {
        String[] list = new String[shares];

        for (int i = 0; i < list.length-1; i++) {
            //for VSS only
            BigInteger b = SSSSplit.getRandomBigInteger(new BigInteger("225"));


            //else uncomment//
            //BigInteger b = getRandomBigIntegerSameBitLength(bitsize);


            list[i] = String.valueOf(b);

            //System.out.println("Sh" + i + ": " + list[i]);
        }

        return list;

    }

    private static BigInteger getRandomBigIntegerSameBitLength(BigInteger max) {

        Random rand = new Random();
        BigInteger upperLimit = max;
        BigInteger result;

        int i = 0;
        do {
            //System.out.println("Loop: " + i);
            result = new BigInteger(upperLimit.bitLength(), rand);

            //System.out.println("result.bitLength(): " + result.bitLength());
            //System.out.println("max.bitLength(): " + max.bitLength());
            i++;
        } while(result.bitLength() != max.bitLength());

        return result;
    }
}
