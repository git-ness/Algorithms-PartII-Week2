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

    /**
     * Picks the lowest of the three below the current position where they are located
     * at the bottom left, bottom or the bottom right.
     *
     * @param x row position within the 2d energyArray
     * @param y column position
     * @return int that represents the x position of the row below the current starting point.
     * Return -1 if the lower left and lowerleft below the lower left-location-energy is less than the others.
     * Return -1 if the lower right and lowerright below the lower right-location-energy is less than the others.
     */
    private int pick(int x, int y) {

        double lowerBelowLeft = Double.POSITIVE_INFINITY;
        double lowerBelowRight = Double.POSITIVE_INFINITY;
        double below = getValFromEnergyArray(x, y + 1);

        if (y == height() - 1) {
            return -1;
        }

        // ---------------------------------------------------------
        // Check to see if range for two rows below are valid. If not, only look one row below instead of two rows below.
        if (!(x - 2 < 0) || !(x + 2 > width() - 1)) {
            double lowestSubEnergyFromNegOneToZero = Double.POSITIVE_INFINITY;
            double lowestSubEnergyFromOneToTwo = Double.POSITIVE_INFINITY;
            int firstCandidate = -99;
            int secondCandidate = -100;

            // Using the for loop, check energy for below nodes x - 1 to x, adding energies for the three nodes below them.
            for (int i = -1; i < 1; i++) {
                for (int j = -2; j < 1; j++) {

                    double energyCandidate = getValFromEnergyArray(x + i, y + 1)
                            + getValFromEnergyArray(x + j, y + 2);

                    if (energyCandidate < lowestSubEnergyFromNegOneToZero) {
                        lowestSubEnergyFromNegOneToZero = energyCandidate;
                        firstCandidate = i;
                    } else {
                    }
                }
            }

            // Disconnect and perform this in two parts so all seams are connected,
            // otherwise energies that are not "connected" will be calculated
            for (int i = 1; i < 2; i++) {
                for (int j = -2; j < 3; j++) {

                    double energyCandidate = getValFromEnergyArray(x + i, y + 1)
                            + getValFromEnergyArray(x + j, y + 2);

                    if (energyCandidate < lowestSubEnergyFromOneToTwo) {
                        lowestSubEnergyFromOneToTwo = energyCandidate;
                        secondCandidate = i;
                    } else {
                    }
                } //TODO: Test to ensure that if x below is the lowest energy, that it'll be choosen appropriately
            }


            if (lowestSubEnergyFromNegOneToZero < lowestSubEnergyFromOneToTwo) {
                return x + firstCandidate;
            }

            if (lowestSubEnergyFromNegOneToZero > lowestSubEnergyFromOneToTwo) {
                return x + secondCandidate;
            }
        }

        if (x != 0) {
            lowerBelowLeft = getValFromEnergyArray(x - 1, y + 1);
        }

        if (x != width() - 1) {
            lowerBelowRight = getValFromEnergyArray(x + 1, y + 1);
        }

        // lowerBelowRight is the lowest
        if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
            // Add the value of x to seamArray[0+1]
            return x + 1;
        }

        // below is the lowest
        else if (below < lowerBelowLeft && below < lowerBelowRight) {
            return x;
        }

        // lowerBelowLeft is the lowest
        else {
            return x - 1;
        }
    }


    private SeamEnergy findVerticalSeamFrom(int x) {

        // Energy: 3443.1978197452986
        // 2  3  4  3  4  3  3  2  2  1

        int y = 0;
        int[] seam = new int[height()];
        seam[y] = x;

        for (y = 1; y < height(); y++) {
            x = pick(x, y);

            if (x == -2 || x == -1) {
                // Set x to far right or far left, then set x again with y increased by 1.

                // return;
            }

            seam[y] = x;
        }
        this.verticalSeamArray = seam;

        SeamEnergy se = new SeamEnergy(seam);
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

        // Energy: 3443.1978197452986
        // 2  3  4  3  4  3  3  2  2  1

        for (int i = 1; i < width(); i++) {
            double energyValue = findVerticalSeamFrom(i).energy;
            if (energyValue < lowestCandidate) {
                lowestCandidate = energyValue;
                arraySeam = findVerticalSeamFrom(i).seam;

            }
        }

        System.out.println("Lowest candidate: " + lowestCandidate);
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
                int placeholder = 0;
                energyValue += energyArray[verticalSeamArray[i]][i];


            }
            return energyValue;
        }

    }

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
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/7x10.png");
        SeamCarver seamCarver = new SeamCarver(picture);


//        SeamEnergy energy = seamCarver.findVerticalSeamFrom(2);
        int value = seamCarver.pick(2, 0);
        int placeholder = 0;

//        int[] horiz = seamCarver.findHorizontalSeam();


        // 6x5.jpg
//        assert (verticalSeem[0] == 3) || (verticalSeem[0] == 4) || (verticalSeem[0] == 5);
//        assert verticalSeem[1] == 4;
//        assert verticalSeem[2] == 3;
//        assert verticalSeem[3] == 2;
//        assert (verticalSeem[4] == 1) || (verticalSeem[4] == 2) || (verticalSeem[4] == 3);
//

        // 6x5.jpg - horizontal { 1||2||3, 2, 1, 2, 1, 2 }
//        assert (horizontalSeam[0] == 1) || (verticalSeem[0] == 2) || (verticalSeem[0] == 3);
//        assert horizontalSeam[1] == 2;
//        assert horizontalSeam[2] == 1;
//        assert horizontalSeam[3] == 2;
//        assert horizontalSeam[4] == 1;
//        assert (horizontalSeam[5] == 1) || (verticalSeem[4] == 2) || (verticalSeem[4] == 3);


        // 7x10.jpg
//        assert (verticalSeem[0] == 2) || (verticalSeem[0] == 3) || (verticalSeem[0] == 4);
//        assert verticalSeem[1] == 3;
//        assert verticalSeem[2] == 4;
//        assert verticalSeem[3] == 3;
//        assert verticalSeem[4] == 4;
//        assert verticalSeem[5] == 3;
//        assert verticalSeem[6] == 3;
//        assert verticalSeem[7] == 2;
//        assert verticalSeem[8] == 2;
//        assert (verticalSeem[9] == 1) || (verticalSeem[9] == 2) || (verticalSeem[9] == 3);

//        Energy: 3443.1978197452986
//        2  3  4  3  4  3  3  2  2  1


        //      8,6   8,7  7,8

//        seamCarver.removeVerticalSeam(verticalSeem);


        // Take three routes, see if any of those three have the lowest on either end. Calculate
        // the lowest energy of all of them as they go along. They will evaluated compared to each other.


    }
}
