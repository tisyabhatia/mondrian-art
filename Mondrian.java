import java.util.*;
import java.awt.*;

/*
 * This class enables a user to make a Mondrian art piece. The user can make a simple Mondrian art,
 * which consists of multiple different rectangles with random ones being filled with red, yellow,
 * or blue. The user can also make a complex Mondrian art, which will have a center gradient (i.e.
 * further from the center is darker colored) 
 */
public class Mondrian {
    private final int MIN_CANVAS_SIZE = 0;
    private final double DIST_SCALE = 0.75;
    private final int ADD_SCALE = 190;

    private Random random;
    private Color[] colors;

    /* Behavior: this method creates a new Mondrian art */
    public Mondrian() {
        random = new Random();
        colors = new Color[]{Color.RED, Color.YELLOW, Color.CYAN, Color.WHITE};
        // colors.add(Color.RED);
        // colors.add(Color.YELLOW);
        // colors.add(Color.CYAN);
        // colors.add(Color.WHITE);
    }

    /* Behavior: This method paints complex art in the Mondrian style. In this style, there are 
     * many randomly size rectangles filled with color randomly. 
     * Exceptions: Throws an IllegalArgumentException if pixels list in null, or less than 300x300
     * Parameters: Color[][] pixels: 2D array of Colors for each individual pixels
     */
    public void paintBasicMondrian(Color[][] pixels) {
        if (pixels == null || pixels.length < MIN_CANVAS_SIZE || 
                                pixels[0].length < MIN_CANVAS_SIZE) {   
            throw new IllegalArgumentException("pixels array is null, or too small :(");
        }

        // start recursion
        paintMondrian(pixels, 0, 0, pixels[0].length, pixels.length, false);
    }

    /* Behavior: This method paints simple art in the Mondrian style. In this style, there are many
     * randomly size rectangles filled with color randomly. 
     * Exceptions: Throws an IllegalArgumentException if pixels list in null, or less than 300x300
     * Parameters: Color[][] pixels: 2D array of Colors for each individual pixels
     */
    public void paintComplexMondrian(Color[][] pixels) {
        // throw exceptions
        if (pixels == null || pixels.length < MIN_CANVAS_SIZE || 
                                pixels[0].length < MIN_CANVAS_SIZE) {
            throw new IllegalArgumentException("pixels array is null, or too small :(");
        }

        // starts recursion 
        paintMondrian(pixels, 0, 0, pixels[0].length, pixels.length, true);
    }

    /* Behavior: This method picks a random color for a specific region on the art piece depending
     * on how far the center of the rectangle is from the middle of the canvas. The further away
     * from the center, the more pigmented the color will be 
     * Parameters: 
     *   => Color[][] pixels: 2D array of Colors for each individual pixels
     *   => int x: the mid value of the x coordinate of the curent region
     *   => int y: the mid value of the y coordinate of the current region
     * Returns: a Color object which represents a certain random color to fill in the region with
     */
    private Color chooseColor(Color[][] pixels, int x, int y) {
        // the x and y distance from the center (not corrected for negative / positive)
        int xDisplacement = x - (pixels[0].length / 2);
        int yDisplacement = y - (pixels.length / 2);

        // finding the distance from the center
        int distanceFromCenter = (int) Math.sqrt(xDisplacement * xDisplacement 
                                 + yDisplacement * yDisplacement);

        // rbg scale
        double scaleFactor = DIST_SCALE * distanceFromCenter;
        
        // determine each of the rgp colors
        int red = (int) Math.min(Math.max(255 - scaleFactor + random.nextInt(ADD_SCALE), 0), 255);
        int green = (int) Math.min(Math.max(255 - scaleFactor, 0), 250);
        int blue = (int) Math.min(Math.max(255 - scaleFactor + random.nextInt(ADD_SCALE), 0), 255);

        return new Color(red, green, blue);
    }

    /* Behavior: This method paints both complex and simple art in the Mondrian style. In this
     * style, there are many randomly size rectangles filled with color randomly. 
     * Parameters: 
     *   => Color[][] pixels: 2D array of Colors for each individual pixels
     *   => xStart: the smaller row (top row)
     *   => yStart: the smaller column (left column)
     *   => xEnd: the larger row (bottom row)
     *   => yEnd: the larger column (right column)
     */
    private void paintMondrian(Color[][] pixels, int xStart, int yStart, int xEnd, int yEnd, 
                                                                        boolean complex) {
        // PRIVATE PAIR

        // base case is at the end after we check for all the cases in which we DO continue
        // recursing? recursion? continue calling the method itself again

        // CASE 1: if region is at least one fourth height and width of full canvas, split into 
        // four small regions by choosing the vertical and horizontal line at random
        if ((yEnd - yStart >= (pixels.length / 4)) && (xEnd - xStart >= (pixels[0].length / 4))
            && (xStart + 20 < xEnd) && (yEnd - 20 > yStart)) {
            // split into four sections

            // store the x split line and y split line (make sure there is 10 pixels of space)
            int xSplit = random.nextInt(xStart + 10, xEnd - 10);
            int ySplit = random.nextInt(yStart + 10, yEnd - 10);

            // top left
            paintMondrian(pixels, xStart, yStart, xSplit, ySplit, complex);

            // top right
            paintMondrian(pixels, xStart, ySplit, xSplit, yEnd, complex);

            // bottom left
            paintMondrian(pixels, xSplit, yStart, xEnd, ySplit, complex);

            // bottom right
            paintMondrian(pixels, xSplit, ySplit, xEnd, yEnd, complex);

        // CASE 2: if region is at least one fourth height of full canvas, split into two smaller
        // regions by choosing horizontal line divide
        } else if ((xEnd - xStart) >= (pixels[0].length / 4) && (xStart + 20 < xEnd)) {
            // store the x split line
            int xSplit = random.nextInt(xStart + 10, xEnd - 10);

            // top 
            paintMondrian(pixels, xStart, yStart, xSplit, yEnd, complex);

            // bottom
            paintMondrian(pixels, xSplit, yStart, xEnd, yEnd, complex);
        
        // CASE 3: if region is at least one fourth height of full canvas, split into two smaller
        // regions by choosing vertical line divide
        } else if ((yEnd - yStart) >= (pixels.length / 4) && (yStart + 20 < yEnd)) {
            // store the y split line
            int ySplit = random.nextInt(yStart + 10, yEnd - 10);

            // left
            paintMondrian(pixels, xStart, yStart, xEnd, ySplit, complex);

            // right
            paintMondrian(pixels, xStart, ySplit, xEnd, yEnd, complex);

        // CASE 4: NOTHING BC SIZE DOES NOT FIT REQS (vals not greater than one fourth of canvas)
        } else {
            Color regionColor;

            if (complex) {
                regionColor = chooseColor(pixels, (xStart + xEnd) / 2, (yStart + yEnd) / 2);
            } else {
                regionColor = colors[random.nextInt(colors.length)];
            }

            for (int i = xStart + 1; i < xEnd - 1; i++) {
                for (int j = yStart + 1; j < yEnd - 1; j++) {
                    pixels[j][i] = regionColor; // make sure j before i because of the index thing
                }
            }
        }
    }
}
