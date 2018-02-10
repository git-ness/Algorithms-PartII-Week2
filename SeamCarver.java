import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture pic;
    // mutable is a deep copy
    // imutable is a final var

    public SeamCarver(Picture picture) {                    // create a seam carver object based on the given picture
        pic = new Picture(picture);

        pic.get(2,2).getRed();
        pic.get(2,2).getGreen();
        pic.get(2,2).getBlue();
    }

    public Picture picture() {                              // current picture
        //TODO: Is current picture the modified picture or the original picture?

        return new Picture(pic);
        // a = 1, b = a, b = 2 what is value of a ... values are independent
    }

    public int width() {                                    // width of current picture
        return pic.width();
    }

    public int height() {                                   // height of current picture
        return pic.height();
    }

    public double energy(int x, int y) {               // energy of pixel at column x and row y

        if (x == 0 || y == 0 || x == width()-1 || y == height()-1) {
            return 1000;
        }

        Color right = pic.get(x + 1, y);
        Color left = pic.get(x - 1, y);
        Color up = pic.get(x, y - 1);
        Color below = pic.get(x, y + 1);



        return 0.0;

    }

    public int[] findHorizontalSeam() {              // sequence of indices for horizontal seam
        return null;
    }


    public int[] findVerticalSeam() {                // sequence of indices for vertical seam

        return null;
    }

    public void removeHorizontalSeam(int[] seam) {  // remove horizontal seam from current picture
    }

    public void removeVerticalSeam(int[] seam) {    // remove vertical seam from current picture

    }

    public static void main(String[] args) {
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/3x4.png");

        SeamCarver seamCarver = new SeamCarver(picture);
        assert seamCarver.energy(0, 0) == 1000;

    }
}
