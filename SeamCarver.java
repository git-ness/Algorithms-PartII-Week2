import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture pic;
    private final double[][] energyArray;

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

    private SeamEnergy findVerticalSeamFrom(int x) {

        int y = 0;
        int[] seamArray = new int[height()];

        seamArray[0] = x;

        double lowerBelowLeft = energyArray[x - 1][y + 1];
        double below = energyArray[x][y + 1];
        double lowerBelowRight = energyArray[x + 1][y + 1];

        // lowerBelowRight is the lowest
        if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
            // Add the value of x to seamArray[0+1]
            seamArray[1] = x + 1;
            y++;
        }

        // below is the lowest
        else if (below < lowerBelowLeft && below < lowerBelowLeft) {
            seamArray[1] = x;
            y++;

        }

        // lowerBelowLeft is the lowest
        else {
            seamArray[1] = x - 1;
            y++;
        }

        // for loop and keep traversing the seam.
        // account for left/right cases

        for (int i = 2; i < seamArray.length; i++) {
            int newX = seamArray[i - 1];
            int newY = y;

            lowerBelowLeft = energyArray[newX - 1][newY + 1];
            below = energyArray[newX][newY + 1];
            lowerBelowRight = energyArray[newX + 1][newY + 1];

            // check to ensure energyArray is not on the left or right edges
            if (lowerBelowLeft == 1000 || lowerBelowRight == 1000) {

                // compare below and lowerBelowRight
                if (lowerBelowLeft == 1000) {

                    if (lowerBelowRight < below) {
                        seamArray[i] = newX - 1;
                        y++;
                        continue;
                    } else {
                        seamArray[i] = newX;
                        y++;
                        continue;
                    }

                } else {
                    if (lowerBelowLeft < below) { // lowerBelowRight is 1000, so we compare below and lowerBelowLeft
                        seamArray[i] = newX - 1;
                        y++;
                        continue;
                    } else {
                        seamArray[i] = newX;
                        y++;
                        continue;
                    }
                }
            }

            // lowerBelowRight is the lowest
            if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
                seamArray[i] = newX + 1;
                newY++;
                continue;
            }

            // below is lowest
            else if (below < lowerBelowLeft && below < lowerBelowRight) {
                seamArray[i] = newX;
                newY++;
                continue;
            }

            // lowerBelowLeft is the lowest
            else {
                seamArray[i] = newX - 1;
                newY++;
                continue;
            }
        }

        SeamEnergy se = new SeamEnergy();
        se.seam = seamArray;
        //TODO: Calculate to total energy so we can find the lowest energy from the seams.
        return se;
    }

    public Picture picture() {                              // current picture

        return new Picture(pic);
    }

    public int width() {                                    // width of current picture
        return pic.width();
    }

    public int height() {                                   // height of current picture
        return pic.height();
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

    public double energy(int x, int y) {               // energy of pixel at column x and row y

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        return Math.sqrt(deltaHorizontal(x, y) + deltaVertical(x, y));
    }


    public int[] findVerticalSeam() {                // sequence of indices for vertical seam
        // Find lowest energy on second row and use the row right above it as the first location

        double lowestCandidate = Double.MAX_VALUE;
        int arrayCandidateIndex = 0;

        // TODO: Upgrade functionality to include top as part of for loop
        for (int i = 0; i < width(); i++) {
            double energyValue = findVerticalSeamFrom(i).energy;
            if (energyValue < lowestCandidate) {
                lowestCandidate = energyValue;
                arraySeam = findVerticalSeamFrom(i).seam;

            }
        }
        return arraySeam;

    }

    public static class SeamEnergy {
        double energy;
        int[] seam;
    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam


        return null;
    }


    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture

    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture

        // Shift all pixels that is to the right of the seam and move them left.
        for (int col = seam[0]; col < energyArray.length- seam[0]; col++) {
            for (int row = 0; row < height(); row++) {

                double pixelSwap = energyArray[col + 1][row];
                energyArray[col][row] = pixelSwap;
            }
        } // TODO: The above ^ doesn't take into account the new deltas that need to be calculated after the seem is removed.
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
