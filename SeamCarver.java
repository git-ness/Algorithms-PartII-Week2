import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.util.ArrayList;

public class SeamCarver {
    private Picture pic;
    private final double[][] energyArray;
    private int[] verticalSeamArray;
    ArrayList<int[]> verticalSeamsToRemoveList = new ArrayList<>();

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

    private CalculateSeamEnergy findVerticalSeamFrom(int x) {

        int y = 0;
        verticalSeamArray = new int[height()];

        verticalSeamArray[0] = x;
        double lowerBelowLeft = Double.POSITIVE_INFINITY;
        double below = Double.POSITIVE_INFINITY;
        double lowerBelowRight = Double.POSITIVE_INFINITY;

        if (x != 0 && x != width() - 1) {
            lowerBelowLeft = energyArray[x - 1][y + 1];
            below = energyArray[x][y + 1];
            lowerBelowRight = energyArray[x + 1][y + 1];
        } else if (x == 0) {
            below = energyArray[x][y + 1];
            lowerBelowRight = energyArray[x + 1][y + 1];
        } else {
            below = energyArray[x][y + 1];
            lowerBelowLeft = energyArray[x - 1][y + 1];
        }

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

            if (x != 0 && x != width() - 1) {
                lowerBelowLeft = energyArray[x - 1][y + 1];
                below = energyArray[x][y + 1];
                lowerBelowRight = energyArray[x + 1][y + 1];
            } else if (x == 0) {
                below = energyArray[x][y + 1];
                lowerBelowRight = energyArray[x + 1][y + 1];
            } else {
                below = energyArray[x][y + 1];
                lowerBelowRight = energyArray[x - 1][y + 1];
            }

            // check to ensure energyArray is not on the left or right edges
            if (lowerBelowLeft == 1000 || lowerBelowRight == 1000) {

                // compare below and lowerBelowRight
                if (lowerBelowLeft == 1000) {

                    if (lowerBelowRight < below) {
                        verticalSeamArray[i] = newX - 1;
                        y++;
                        continue;
                    } else {
                        verticalSeamArray[i] = newX;
                        y++;
                        continue;
                    }

                } else {
                    if (lowerBelowLeft < below) { // lowerBelowRight is 1000, so we compare below and lowerBelowLeft
                        verticalSeamArray[i] = newX - 1;
                        y++;
                        continue;
                    } else {
                        verticalSeamArray[i] = newX;
                        y++;
                        continue;
                    }
                }
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

        CalculateSeamEnergy se = new CalculateSeamEnergy(verticalSeamArray);

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

    public class CalculateSeamEnergy {
        double energy;
        int[] seam;

        public CalculateSeamEnergy(int[] seam) {
            this.seam = seam;
            this.energy = energySumOfSeam();
        }

        private double energySumOfSeam() {
            double energyValue = 0;

            for (int i = 0; i < seam.length; i++) {
                // 3, 0 --> location of first vertex
                energyValue += energyArray[verticalSeamArray[i]][i];
                // 2, 1 --> location of second vertex


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
        

    }



    /*---------- vertical seam removal map ---------------
                    Goal to accomplish next (What's this things job?):
                        To remove the seam
                         Find the seam, add the seam to the ArrayList until removeSeam is called
                         if (arrayList.size == 1)
                            Create a new picture and move all pixels to the left of their current pos.
                         else
                            order the seams to ensure every spot is increasing.
                            for length of arrayList
                                Find the range of the pixels to be "saved" and moved to the left

                                // Determine the last seam to the end of the picture using the below
                                use arraylist.size() in (width() - arrayList.size())





                    Process - pseudo code (What we'll need):
                        ArrayList<int[]> verticalSeamsToRemoveList = new ArrayList<>();

                          removeVerticalSeam(int[] seam) {
                            if (verticalSeamsToRemoveList.size() == 1) {
                                new Picture(width - 1, height())
                                for (width - seamIndex) {
                                    color = pic.getRGB(row+1,col)
                                    pic.set(row,col,color)
                                }

                            } else {

                                // Order the seams so they are always increasing at each column
                                for (int i = 0; i < verticalSeamsToRemoveList.size(); i++) {
                                        int[] newSeam0;
                                        int[] newSeam1;

                                        vertSeam1 = verticalSeamsToRemoveList.index(i);
                                        // verticalSeamsToRemoveList.index(0);

                                        vertSeam2 = verticalSeamsToRemoveList.index(i+1);
                                        // verticalSeamsToRemoveList.index(1);

                                        if (vertSeam1.index(i) > vertSeam2(i))
                                }
                            }
                            pic.save();
                          }




                    Test Result Validation:

     -------------------------------------

      // Original idea
                          Remove the seam
                          Move RGB from the right of the seam into the space of the removed seam until the end
                          Recalculate the energies along the seam
                          ?When do we finish minimizing the picture via the seam? Perhaps the tests stop at a certain point.
                          Save picture

                          // Original idea
                          for i of 0 till length take the seam and convert to picture location
                             location on x axis = verticalSeamArray[i]
                             colorValue = pic.getRGB(col,row)
                             pic.set(row, col, colorValue) <-- dependent on if we can set value from getRGB here
                               ^ Set where seem is
                             pic.set(row, col + 1, colorValueFromRight)
                             -- continue until end, set last pixel to null --
                               (keep a counter to keep track of how many pixels to the right to go)

                           for every element in the seamArray represented as i
                              energyArray[verticalSeam[i]][i] = energy(verticalSeam[i],i)

    */

    //TODO: How do we remove the empty spaces on the right when done removing the seams?
    //TODO: Only way I can think of is create a new array, resize it and add everything one by
    //TODO: one but that doesn't seem right. https://tinyurl.com/seamremoval suggests it however.


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
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/3x4.png");

        SeamCarver seamCarver = new SeamCarver(picture);
//        assert seamCarver.energy(0, 0) == 1000; // 3x4.png
//        assert seamCarver.energy(1, 2) == Math.sqrt(52024);

//        seamCarver.findVerticalSeamFrom(3);

        int[] verticalSeem = seamCarver.findVerticalSeam();
        seamCarver.removeVerticalSeam(verticalSeem);


    }
}
