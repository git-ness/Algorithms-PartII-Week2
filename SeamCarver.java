import edu.princeton.cs.algs4.*;
import java.awt.*;
import java.util.Iterator;

public class SeamCarver {
    private Picture pic;
    private double[][] energyArray;
    Digraph verticalDigraph;

    // mutable is a deep copy
    // imutable is a final var
    //TODO: Guard against null parameters

    public SeamCarver(Picture picture) {                    // create a seam carver object based on the given picture
        pic = new Picture(picture);
        this.energyArray = new double[pic.width()][pic.height()];
        this.verticalDigraph = new Digraph(height()*width());

        createVerticalDigraph();

        Topological topological = new Topological(verticalDigraph);
        Iterable<Integer> topOrder = topological.order();
        Iterator<Integer> iterator = topOrder.iterator();
        iterator.hasNext();

        


    }


    private void createVerticalDigraph() {
        int vertex = 0;

        for (int x = 0; x < pic.width(); x++) {
            for (int y = 0; y < pic.height(); y++) {

                energyArray[x][y] = energy(x, y);

                // Adds edges on
                if (vertex % width() == 0 && vertex + width() < width()*height()) {
                    verticalDigraph.addEdge(vertex, vertex + width()+1);
                    verticalDigraph.addEdge(vertex, vertex + width());
                    vertex++;
                }

                if ((vertex + 1) % width() == 0 && vertex + width() < width()*height()) {
                    verticalDigraph.addEdge(vertex, vertex + width()-1);
                    verticalDigraph.addEdge(vertex, vertex+width());

                    vertex++;
                }

                if ((y != 0 && y != width() - 1) && vertex + width() < width()*height()) {
                    verticalDigraph.addEdge(vertex, vertex + width()+1);
                    verticalDigraph.addEdge(vertex, vertex + width()-1);
                    verticalDigraph.addEdge(vertex, vertex + width());
                    vertex++;

                }
            }
        }
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

        return Math.sqrt(deltaHorizontal(x, y) + deltaVertical(x,y));
    }


    public int[] findVerticalSeam() {                // sequence of indices for vertical seam

        // Find the lowest pixel on the second row
        int lowestVertIndex = 0;
        for (int i = 0; i < energyArray.length; i++) {
            double lowerCandidate = energyArray[1][i];
            double lowest = 0.0;
            if (lowest > lowerCandidate ) {
                lowestVertIndex = i;
            }
        }


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
