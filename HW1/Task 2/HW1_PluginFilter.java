import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.PlugInFilter;
import ij.process.ImageProcessor;

public class HW1_PluginFilter implements PlugInFilter {

    ImagePlus imagePlus;

    public int setup(String arg, ImagePlus imagePlus) {
        this.imagePlus = imagePlus;
        return DOES_ALL;
    }

    public void run(ImageProcessor imageProcessor) {
        int numPixels = imageProcessor.getWidth();
        shift(imageProcessor, numPixels / 2, true);
        shift(imageProcessor, numPixels / 2, false);

        IJ.save(imagePlus, "copy.png");
    }

    private void swapInRow(ImageProcessor imageProcessor, int row, int left, int right) {
        int temp = imageProcessor.getPixel(left, row);
        imageProcessor.putPixel(left, row, imageProcessor.getPixel(right, row));
        imageProcessor.putPixel(right, row, temp);
    }

    private void swapInColumn(ImageProcessor imageProcessor, int column, int top, int bottom) {
        int temp = imageProcessor.getPixel(column, top);
        imageProcessor.putPixel(column, top, imageProcessor.getPixel(column, bottom));
        imageProcessor.putPixel(column, bottom, temp);
    }

    private void flip(ImageProcessor imageProcessor, int rowOrColumn, int start, int end, boolean isHorizontal) {
        for (; start < end; start++, end--) {
            if (isHorizontal) {
                swapInRow(imageProcessor, rowOrColumn, start, end);
            } else {
                swapInColumn(imageProcessor, rowOrColumn, start, end);
            }
        }
    }

    private void shift(ImageProcessor imageProcessor, int offset, boolean isHorizontal) {
        int width = imageProcessor.getWidth(), height = imageProcessor.getHeight();
        int loopValue = isHorizontal ? height : width;
        int parameterValue = isHorizontal ? width : height;
        for (int rowOrColumn = 0; rowOrColumn < loopValue; rowOrColumn++){
            flip(imageProcessor, rowOrColumn, 0, offset - 1, isHorizontal);
            flip(imageProcessor, rowOrColumn, offset, parameterValue - 1, isHorizontal);
            flip(imageProcessor, rowOrColumn, 0, parameterValue - 1, isHorizontal);
        }
    }
}