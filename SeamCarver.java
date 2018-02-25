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

    private int pick(int x, int y) {

        double lowerBelowLeft = Double.POSITIVE_INFINITY;
        double below = Double.POSITIVE_INFINITY;
        double lowerBelowRight = Double.POSITIVE_INFINITY;

        if (isNotOnBorderOfPicture(x)) {
            lowerBelowLeft = getValFromEnergyArray(x - 1, y);
            below = getValFromEnergyArray(x, y);
            lowerBelowRight = getValFromEnergyArray(x + 1, y);

        }

        // If on the left border of picture
        else if (x == 0 && y != height()-1) {
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
            return x + 1;
        }

        // below is the lowest
        else if (below < lowerBelowLeft && below < lowerBelowLeft) {
            return x;
        }

        // lowerBelowLeft is the lowest
        else {
            return x - 1;
        }
    }


    private SeamEnergy findVerticalSeamFrom(int x) {

        int y = 0;
        int[] seam = new int[height()];
        seam[y] = x;

        for (y = 1; y < height(); y++) {
            x = pick(x , y);
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

        for (int i = 1; i < width(); i++) {
            int placehold = 0;
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
                int placeholder = 0;
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

        int[] verticalSeem = seamCarver.findVerticalSeam();

        int placeholder = 0;

        // 6x5.jpg
//        assert (verticalSeem[0] == 3) || (verticalSeem[0] == 4) || (verticalSeem[0] == 5);
//        assert verticalSeem[1] == 4;
//        assert verticalSeem[2] == 3;
//        assert verticalSeem[3] == 2;
//        assert (verticalSeem[4] == 1) || (verticalSeem[4] == 2) || (verticalSeem[4] == 3);
//

        // 7x10.jpg
//        assert (verticalSeem[0] == 2) || (verticalSeem[0] == 3) || (verticalSeem[0] == 4);
//        assert verticalSeem[1] == 3;
//        assert verticalSeem[2] == 4;
//        assert verticalSeem[3] == 3;
//        assert verticalSeem[4] == 4;
//        assert verticalSeem[5] == 3;
//        assert verticalSeem[6] == 4;
//        assert verticalSeem[7] == 3;
//        assert verticalSeem[8] == 4;
//        assert (verticalSeem[9] == 3) || (verticalSeem[9] == 4) || (verticalSeem[9] == 5);





        seamCarver.removeVerticalSeam(verticalSeem);

    }
}
