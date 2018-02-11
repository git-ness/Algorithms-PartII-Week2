import edu.princeton.cs.algs4.*;

import java.awt.*;

public class SeamCarver {
    private Picture pic;

    // mutable is a deep copy
    // imutable is a final var
    //TODO: Guard against null parameters

    public SeamCarver(Picture picture) {                    // create a seam carver object based on the given picture
        pic = new Picture(picture);
        double[][] energyArray = new double[pic.width()][pic.height()];

        // Start with the lowest energy of the second... index 1 array.
        // Square the amounts only when needed
        //TODO: How is getRGB supposed to be used when it returns a neg int?

        for (int i = 0; i < pic.width(); i++) {
            for (int j = 0; j < pic.height(); j++) {

                energyArray[i][j] = energy(i,j);

            }
        }
        

//        Topological topologicalWidth = new Topological(digraphWidth);


        /*---------- Memory Map ---------------
                    Goal to accomplish next (What's this things job?):
                          Transform/encode pixels into v with a weight directed at w
                          Grab the energy function of each vertex and store it in an Array
                          Traverse downward with pixel connecting to bottom three and see which is less


                    Process - pseudo code (What we'll need):
                          HashSet<Integer> nounIdSet = nounToSynsetIdMap.get(noun);
                          nounIdSet.add(synsetId);

                          HashSet<Integer> nounsInSynset = synsetIdToNounMap.get(noun);
                          nounsInSynset.add(noun);

                    Test Result Validation:
                          nounToSynsetIdMap.get("appearsTwice") returns 1,3
                          nounIdSet.get("one") returns 1,2
*/


        // Exercise says not to use EdgeWeightedDigraph or DirectedEdge. AcyclicSP takes an EdgeWeightedDigraph param.
//        AcyclicSP acyclicSP = new AcyclicSP();
//        acyclicSP.pathTo(4);


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

    public double energy(int x, int y) {               // energy of pixel at column x and row y

        if (x == 0 || y == 0 || x == width() - 1 || y == height() - 1) {
            return 1000;
        }

        //TODO: Switch to using getRGB
        Color right = pic.get(x + 1, y);
        Color left = pic.get(x - 1, y);
        Color up = pic.get(x, y - 1);
        Color below = pic.get(x, y + 1);

        int[] leftRight = new int[3];

        //TODO: Switch to be able to apply this to all graphs, not just 3x4 pixels.
        leftRight[0] = (right.getRed() - left.getRed()) * (right.getRed() - left.getRed());
        leftRight[1] = (right.getGreen() - left.getGreen()) * (right.getGreen() - left.getGreen());
        leftRight[2] = (right.getBlue() - left.getBlue()) * (right.getBlue() - left.getBlue());

        int[] upDown = new int[3];
        upDown[0] = ((below.getRed() - up.getRed())) * ((below.getRed() - up.getRed()));
        upDown[1] = ((below.getGreen() - up.getGreen())) * ((below.getGreen() - up.getGreen()));
        upDown[2] = ((below.getBlue() - up.getBlue())) * ((below.getBlue() - up.getBlue()));

        int sumLeftRight = 0;
        for (int i = 0; i < leftRight.length; i++) {
            sumLeftRight += leftRight[i];
        }

        int sumUpDown = 0;
        for (int i = 0; i < upDown.length; i++) {
            sumUpDown += upDown[i];
        }

        return sumLeftRight + sumUpDown;
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
        Picture picture = new Picture("/Users/elsa/learning/Algorithms-Part2-seamcarving/seam/3x4.png");

        SeamCarver seamCarver = new SeamCarver(picture);
        assert seamCarver.energy(0, 0) == 1000;
        assert seamCarver.energy(1, 2) == Math.sqrt(52024);

    }
}
