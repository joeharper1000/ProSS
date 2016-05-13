package sample;

/**
 * Created by cf913 on 11/05/16.
 */
import javafx.scene.control.Alert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class MainReconstruct {

    public static String reconstruct(int numshares, String shares, BigInteger prime) {

        String result;

        ArrayList<Pair<Integer, BigInteger>> newShares = new ArrayList<>();
        String[] list = shares.split("\\n");

        if (numshares != list.length) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Conflicting number of shares");
            alert.showAndWait();
            return "";
        }

        for (int i = 0; i < list.length; i++) {


            String nobracket = list[i].replaceAll("[\\[\\](){}]","");
            String[] s = nobracket.split(",");

            int k = Integer.parseInt(s[0]);
            BigInteger v = new BigInteger(String.valueOf(s[1]));
            newShares.add(new Pair<>(k,v));
        }

        BigInteger lesecret = reconstruct_shares(newShares,prime);
        result = new String(lesecret.toByteArray());
        return result;
    }


    public static void reconstruct2(String[] shares, BigInteger prime) {

        ArrayList<Pair<Integer, BigInteger>> newShares = new ArrayList<>();

        for (int i = 0; i < shares.length; i++) {

            //clean share format
            String nobracket = shares[i].replaceAll("[\\[\\](){}]","");
            String[] s = nobracket.split(",");

            int k = Integer.parseInt(s[0]);
            BigInteger v = new BigInteger(String.valueOf(s[1]));
            newShares.add(new Pair<>(k,v));
        }

        //TODO: implement output shares to file
        BigInteger lesecret = reconstruct_shares(newShares,prime);
        System.out.println("The secret is: " + lesecret);
        System.out.println("The secret is: " + new String(lesecret.toByteArray()));
    }

    private static BigInteger reconstruct_shares(List<Pair<Integer, BigInteger>> newShares, BigInteger prime) {

        BigInteger s = BigInteger.ZERO;

        //Lagrange interpolation to find coef[0]
        for (int i = 0; i < newShares.size(); i++) {
            BigInteger num = BigInteger.ONE;
            BigInteger denum = BigInteger.ONE;
            for (int j = 0; j < newShares.size(); j++) {

                if (i != j) {
                    num = num.multiply(BigInteger.valueOf(-newShares.get(j).getL())).mod(prime);
                    denum = denum.multiply(BigInteger.valueOf(newShares.get(i).getL()-newShares.get(j).getL())).mod(prime);

                }
            }
            BigInteger value = newShares.get(i).getR();

            s = s.add(value.multiply(num).multiply(denum.modInverse(prime))).mod(prime);

        }

        return s;
    }
}
