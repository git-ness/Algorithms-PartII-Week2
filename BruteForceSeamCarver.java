import com.sun.tools.javac.util.List;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.ArrayList;

public class BruteForceSeamCarver {

    private Picture pic;
    private final double[][] energyArray;
    ArrayList<SeamAndEnergy> seamAndEnergiesList = new ArrayList<>();

    public BruteForceSeamCarver(Picture picture) {                    // create a seam carver object based on the given picture

        if (picture == null) throw new IllegalArgumentException();

        pic = new Picture(picture);
        this.energyArray = new double[pic.width()][pic.height()];

        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {
                energyArray[x][y] = energy(x, y);
            }
        }

        // Add three new SeamAndEnergy instances for each new path.

        for (int i = 0; i < 4; i++) {
            seamAndEnergiesList.add(new SeamAndEnergy(Integer.toString(i)));
        }

        bruteForce();

    }

    private SeamAndEnergy createSeamAndEnergy(int x, int y) {

        if (x!= 0 && x != width()-1) {



            SeamAndEnergy seamAndEnergy = new SeamAndEnergy(); // String representation of seam and energy
            seamAndEnergiesList.add(new SeamAndEnergy(createSeamAndEnergy(x, y)));
        }

        else {seamAndEnergiesList.add(new SeamAndEnergy())}
    }


    /*

    stringArray.set(stringArray.indexOf("124"), "222");

     */

    private void bruteForce() {

        for (int y = 1; y < height() - 2; y++) {            //TODO: Double check value of 2
            for (int x = 1; x < width() - 2; x++) {         //TODO: Double check value of 2

                // 3x to create a new Seam for each unexplored seam.
                seamAndEnergiesList.add(new SeamAndEnergy(Integer.toString(x)));



            }
        } //TODO Explore if storing different "parts" of the tree in it, storing the lowest energy for that work for the original? Hmm
    }

    private double energy(int x, int y) {
        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        return Math.sqrt(deltaHorizontal(x, y) + deltaVertical(x, y));
    }

    private int deltaVertical(int x, int y) {
        Color above = pic.get(x, y - 1);
        Color below = pic.get(x, y + 1);

        int deltaRed = ((below.getRed() - above.getRed()) * (below.getRed() - above.getRed()));
        int deltaGreen = ((below.getGreen() - above.getGreen()) * (below.getGreen() - above.getGreen()));
        int deltaBlue = ((below.getBlue() - above.getBlue()) * (below.getBlue() - above.getBlue()));

        return deltaRed + deltaGreen + deltaBlue;
    }

    private int deltaHorizontal(int x, int y) {
        Color right = pic.get(x + 1, y);
        Color left = pic.get(x - 1, y);

        int deltaRed = ((right.getRed() - left.getRed())) * ((right.getRed() - left.getRed()));
        int deltaGreen = (right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen());
        int deltaBlue = (right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue());

        return deltaRed + deltaGreen + deltaBlue;
    }

    public int width() {
        return pic.width();
    }

    public int height() {
        return pic.height();
    }

    private class SeamAndEnergy {
        double energy;
        ArrayList<String> seam;
        private String seamValue;

        public SeamAndEnergy(ArrayList<String> seamList) {
            this.seam = seamList;
            addEnergy();
        }

        public SeamAndEnergy(String seamString) {
            this.seamValue = seamString;
            addStringEnergy();
        }

        private void addStringEnergy() {
            String seamString = seamValue;
            int y = 0;

            for (int x = 0; x < seamString.length(); x++) {

                String[] rowValues = seamString.split("");
                int seamInt = Integer.parseInt(rowValues[x]);
                this.energy += energyArray[seamInt][y];
                y++;
            }
        }

        private void addEnergy() {
            String seamString = seam.get(0);
            int y = 0;

            for (int x = 0; x < seamString.length(); x++) {

                String[] rowValues = seamString.split("");
                int seamInt = Integer.parseInt(rowValues[x]);
                this.energy += energyArray[seamInt][y];
                y++;

            }
        }
    }

    public static void main(String[] args) {
        Picture pic = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/4x6.png");
        BruteForceSeamCarver bruteForceSeamCarver = new BruteForceSeamCarver(pic);


    }

}
