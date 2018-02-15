import edu.princeton.cs.algs4.Picture;
import java.awt.*;

public class SeamCarver {
    private Picture pic;
    private double[][] energyArray;

    // mutable is a deep copy
    // imutable is a final var
    //TODO: Guard against null parameters

    public SeamCarver(Picture picture) {                    // create a seam carver object based on the given picture
        pic = new Picture(picture);
        this.energyArray = new double[pic.width()][pic.height()];

        createEnergyArray();


//        // Find the lowest pixel on the second row
//        int lowestVertIndex = 0;
//        int lowestVertIndexSecondRow = 0;
//        double lowest = 0.0;
//
//        for (int i = 0; i < energyArray.length; i++) {
//            double firstRowCandidate = energyArray[1][i];
//            double secondRowCandidate = energyArray[2][i];
//
//            if (lowest > firstRowCandidate) {
//                lowestVertIndex = i;
//            }
//            if (lowestVertIndexSecondRow > )
//
//        }

    }

    // Loop through each node at the top, go down three rows to determine the shorted past first. Once I know
    // which one it is, then I could then plug that into the Topological sort library and have it start from there.
    // Once it starts from there, I use the topological sort from each node...but hmm weights are different than the graph.




    private void createEnergyArray() {
        for (int x = 0; x < pic.width(); x++) {

            for (int y = 0; y < pic.height(); y++) {

                energyArray[x][y] = energy(x, y);


            }

        }
    }

    private int[] findVerticalSeamFrom(int x) {

        int y = 0;
        int[] seamArray = new int[height()];


        seamArray[0] = x;

        double lowerBelowLeft = energyArray[x-1][y+1];
        double below = energyArray[x][y+1];
        double lowerBelowRight = energyArray[x+1][y+1];

        // lowerBelowRight is the lowest
        if (lowerBelowRight < below && lowerBelowRight < lowerBelowLeft) {
            // Add the value of x to seamArray[0+1]
            seamArray[1] = x+1;
        }

        // below is the lowest
        else if (below < lowerBelowLeft && below < lowerBelowLeft) {
            seamArray[1] = x;

        }

        // lowerBelowLeft is the lowest
        else {
            seamArray[1] = x-1;
        }

        // for loop and keep traversing the seam.
        // account for left/right cases

        for (int i = 2; i < seamArray.length; i++) {
            lowerBelowLeft = energyArray[x-1][y+1];
            below = energyArray[x][y+1];
            lowerBelowRight = energyArray[x+1][y+1];



            seamArray[i] =
        }

        return seamArray;
    }

    public Picture picture() {                              // current picture

        return new Picture(pic);
        // a = 1, b = a, b = 2 what is value of a ... values are independent
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


        return null;
    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam


        return null;
    }


    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture

    }

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/6x5.png");

        SeamCarver seamCarver = new SeamCarver(picture);
//        assert seamCarver.energy(0, 0) == 1000;
//        assert seamCarver.energy(1, 2) == Math.sqrt(52024);

    }
}
