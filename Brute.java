import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;

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
        return energyArray[x][y];
    }

    public void bruteForce(int x, int y, int[] seam) {

        seam[y] = x;
        if (y == height() - 1) {
            finishSeam.add(seam);
            int placeholder = 0;
            return;
        }

        if (x != 0) {
            seam[y + 1] = x - 1;
            bruteForce(x - 1, y + 1, seam);
        }

        if (x != width() - 1) {
            seam[y + 1] = x + 1;
            bruteForce(x + 1, y + 1, seam);
        }

        seam[y + 1] = x;
        bruteForce(x, y + 1, seam);


    }

    public static void main(String[] args) {
        Picture pic = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/4x6.png");
        Brute brute = new Brute(pic);

        for (int x = 0; x < pic.width(); x++) {
            brute.bruteForce(x, 0, new int[brute.height()]);
        }

        int placeholder = 0;

    }
}



