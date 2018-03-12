import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Brute {

    Picture pic;
    double[][] energyArray;
    ArrayList<int[]> finishSeam = new ArrayList<>();

    public Brute(Picture picture) {                     // create a seam carver object based on the given picture

        if (picture == null) throw new IllegalArgumentException();

        pic = new Picture(picture);
        this.energyArray = new double[pic.width()][pic.height()];

        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                energyArray[x][y] = energy(x, y);
            }
        }
    }

    public Picture picture() {                          // current picture
        return new Picture(pic);
    }

    public int width() {                                // width of current picture
        return pic.width();
    }

    public int height() {                               // height of current picture
        return pic.height();
    }

    public double energy(int x, int y) {                // energy of pixel at column x and row y
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        return Math.sqrt(deltaHorizontal(x, y) + deltaVertical(x, y));
    }

    private int deltaHorizontal(int x, int y) {
        Color right = pic.get(x + 1, y);
        Color left = pic.get(x - 1, y);

        int deltaRed = ((right.getRed() - left.getRed())) * ((right.getRed() - left.getRed()));
        int deltaGreen = (right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen());
        int deltaBlue = (right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue());

        return deltaRed + deltaGreen + deltaBlue;
    }

    private int deltaVertical(int x, int y) {
        Color above = pic.get(x, y - 1);
        Color below = pic.get(x, y + 1);

        int deltaRed = ((below.getRed() - above.getRed()) * (below.getRed() - above.getRed()));
        int deltaGreen = ((below.getGreen() - above.getGreen()) * (below.getGreen() - above.getGreen()));
        int deltaBlue = ((below.getBlue() - above.getBlue()) * (below.getBlue() - above.getBlue()));

        return deltaRed + deltaGreen + deltaBlue;
    }

    public void bruteForce(int x, int y, int[] seam) {

        seam[y] = x;
        if (y == height() - 1) {

            int[] newArray = new int[height()];

            System.arraycopy(seam, 0, newArray, 0, height());

            finishSeam.add(newArray);
            int placeholder = 0;
            return;
        }

        if (x != 0) {
            bruteForce(x - 1, y + 1, seam);
        }

        if (x != width() - 1) {
            bruteForce(x + 1, y + 1, seam);
        }

        bruteForce(x, y + 1, seam);


    }

    public int[] renameLater() {
        // If the return is null when calling bruteForce, do nothing...otherwise add the seam to the finishSeam list.
        return null;
    }

    public static void main(String[] args) {
        Picture pic = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/7x10.png");
        Brute brute = new Brute(pic);

        for (int x = 0; x < pic.width(); x++) {
            brute.bruteForce(x, 0, new int[brute.height()]);
        }


//        ArrayList<Double> energiesList = new ArrayList<>();
        double lowestEnergy = Double.POSITIVE_INFINITY;
        int[] lowestSeam = new int[brute.height()];

        for (int[] seam : brute.finishSeam) {

            double energyOfSeam = 0;

            for (int i = 0; i < seam.length; i++) {
                energyOfSeam += brute.energy(seam[i], i);
            }

//            energiesList.add(energyOfSeam);
            if (lowestEnergy > energyOfSeam) {
                lowestEnergy = energyOfSeam;
                lowestSeam = seam;
            }

        }
        System.out.println("Energy: " + lowestEnergy);

        for (int i = 0; i < lowestSeam.length; i++) {
              System.out.print(" " + lowestSeam[i] +" ");
        }

    }


//        Collections.sort(energiesList);
//
//        for (Double energy : energiesList) {
//            System.out.println(energy);
//        }

}


//        brute.bruteForce(0, brute.height()-2, new int[brute.height()]);



                /*
        for (int[] seam : brute.finishSeam) {

            double energyCalculation = 0;

            for (int i = 0; i < seam.length; i++ ) {
                energyCalculation += brute.energy(seam[i], i);
            }

            System.out.println(energyCalculation);
        }
        */






