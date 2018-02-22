import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;

public class SeamCarver {
    private Picture pic;
    private final double[][] energyArray;
    private int[] verticalSeamArray;
    private ArrayList<int[]> verticalSeamsToRemoveList = new ArrayList<>();

    // mutable is a deep copy
    // imutable is a final var

    public SeamCarver(Picture picture) {                    // create a seam carver object based on the given picture

        if (picture == null) throw new IllegalArgumentException();

        pic = new Picture(picture);
        this.energyArray = new double[pic.width()][pic.height()];

        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                energyArray[x][y] = energy(x, y);
            }
        }
    }

    private double getValFromEnergyArray(int xSpot, int ySpot) {
        return energyArray[xSpot][ySpot];
    }

    private boolean isNotOnBorderOfPicture(int x) {
        return x != 0 && x != width() - 1;
    }


    private SeamEnergy findVerticalSeamFrom(int x) {
        /*
        refactor
        int y=0;
        seam[y] = x;

        for ( y=1; y<height(); y++ ) {
            x = pick();
            seam[y] = x;
        }
        */

        int y = 0;
        verticalSeamArray = new int[height()];

        verticalSeamArray[0] = x;
        double lowerBelowLeft = Double.POSITIVE_INFINITY;
        double below = Double.POSITIVE_INFINITY;
        double lowerBelowRight = Double.POSITIVE_INFINITY;

        if (isNotOnBorderOfPicture(x)) {
            lowerBelowLeft = getValFromEnergyArray(x - 1, y + 1);
            below = getValFromEnergyArray(x, y + 1);
            lowerBelowRight = getValFromEnergyArray(x + 1, y + 1);

        }

        // If on the left border of picture
        else if (x == 0) {
            below = getValFromEnergyArray(x, y + 1);
            lowerBelowRight = getValFromEnergyArray(x + 1, y + 1);
        }

        // If on the right border of picture.
        else {
            below = energyArray[x][y + 1];
            lowerBelowLeft = energyArray[x - 1][y + 1];
        }

        // ---------------------------------------------------------

        // lowerBelowRight is the lowest
        if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
            // Add the value of x to seamArray[0+1]
            verticalSeamArray[1] = x + 1;
            y++;
        }

        // below is the lowest
        else if (below < lowerBelowLeft && below < lowerBelowLeft) {
            verticalSeamArray[1] = x;
            y++;

        }

        // lowerBelowLeft is the lowest
        else {
            verticalSeamArray[1] = x - 1;
            y++;
        }

        // for loop and keep traversing the seam.
        // account for left/right cases


        for (int i = 2; i < verticalSeamArray.length; i++) {
            int newX = verticalSeamArray[i - 1];
            int newY = y;

            if (isNotOnBorderOfPicture(newX)) {
                lowerBelowLeft = energyArray[newX - 1][y + 1];
                below = energyArray[newX][y + 1];
                lowerBelowRight = energyArray[newX + 1][y + 1];
            } else if (newX == 0) {
                below = energyArray[newX][y + 1];
                lowerBelowRight = energyArray[newX + 1][y + 1];
            } else {
                below = energyArray[newX][y + 1];
                lowerBelowRight = energyArray[newX - 1][y + 1];
            }



            // lowerBelowRight is the lowest
            if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
                verticalSeamArray[i] = newX + 1;
                newY++;
                continue;
            }

            // below is lowest
            else if (below < lowerBelowLeft && below < lowerBelowRight) {
                verticalSeamArray[i] = newX;
                newY++;
                continue;
            }

            // lowerBelowLeft is the lowest
            else {
                verticalSeamArray[i] = newX - 1;
                newY++;
                continue;
            }
        }

        SeamEnergy se = new SeamEnergy(verticalSeamArray);

        // Calculates the total energy so we can find the lowest energy from the seams.

        return se;
    }

    public int width() {                                    // width of current picture
        return pic.width();
    }

    public int height() {                                   // height of current picture
        return pic.height();
    }


    public double energy(int x, int y) {               // energy of pixel at column x and row y

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        return Math.sqrt(deltaHorizontal(x, y) + deltaVertical(x, y));
    }


    public int[] findVerticalSeam() {                // sequence of indices for vertical seam
        // Find lowest energy on second row and use the row right above it as the first location

        double lowestCandidate = Double.MAX_VALUE;
        int[] arraySeam = null;

        for (int i = 1; i < width(); i++) {
            double energyValue = findVerticalSeamFrom(i).energy;
            if (energyValue < lowestCandidate) {
                lowestCandidate = energyValue;
                arraySeam = findVerticalSeamFrom(i).seam;

            }
        }
        verticalSeamsToRemoveList.add(arraySeam);
        return arraySeam;
    }

    private class SeamEnergy {
        double energy;
        int[] seam;

        public SeamEnergy(int[] seam) {
            this.seam = seam;
            this.energy = energySumOfSeam();
        }

        private double energySumOfSeam() {
            double energyValue = 0;

            for (int i = 0; i < seam.length; i++) {
                energyValue += energyArray[verticalSeamArray[i]][i];


            }
            return energyValue;
        }
    }

//    private void processVerticalSeams() {
//
//        // If a seam "crosses" a different seam, this will reorder the seam to ensure they no longer "cross" over each other.
//        for (int row = 0; row < height(); row++) {
//            for (int i = 0; i < verticalSeamsToRemoveList.size(); i++) {
//                if (verticalSeamsToRemoveList.get(0)[i] > verticalSeamsToRemoveList.get(verticalSeamsToRemoveList.size() - 1 - i)[i]) {
//                    int seamValue = verticalSeamsToRemoveList.get(0)[i];
//                    verticalSeamsToRemoveList.get(0)[i] = verticalSeamsToRemoveList.get(verticalSeamsToRemoveList.size() - 1 - i)[i];
//                    verticalSeamsToRemoveList.get(verticalSeamsToRemoveList.size() - 1 - i)[i] = seamValue;
//                }
//            }
//        }
//    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam


        return null;
    }

    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture

    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture

        Picture newPic = new Picture(width() - 1, height());


        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {

                if (x < seam[y]) {
                    newPic.set(x, y, pic.get(x, y));
                }

                if (x == seam[y]) {
                    // Skip
                }

                if (x > seam[y]) {
                    newPic.set(x - 1, y, pic.get(x, y));
                }
            }
        }
        this.pic = newPic;
    }


    //TODO: Since saving to a newpic doesn't work, try making a copy of a pic to get the same picture
    //TODO: This will help make sure the process of moving all pixels to a new pic is valid


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

    public Picture picture() {                              // current picture

        return new Picture(pic);
    }

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/6x5.png");

        SeamCarver seamCarver = new SeamCarver(picture);
//        assert seamCarver.energy(0, 0) == 1000; // 3x4.png
//        assert seamCarver.energy(1, 2) == Math.sqrt(52024);

//        seamCarver.findVerticalSeamFrom(3);

        int[] verticalSeem = seamCarver.findVerticalSeam();
        seamCarver.removeVerticalSeam(verticalSeem);

    }
}
