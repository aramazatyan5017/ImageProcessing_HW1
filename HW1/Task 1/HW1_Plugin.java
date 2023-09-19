import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.*;
import ij.plugin.frame.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HW1_Plugin implements PlugIn {

    @Override
    public void run(String s) {
	//!!!!! unzip the Dataset.zip file and extract the Dataset directory in the same place
        String courseDataFile = "Dataset\\car-f-92.crs";
        String studentDataFile = "Dataset\\car-f-92.stu";

        int numCourses = getNumberOfCourses(courseDataFile);

        ImageProcessor imageProcessor = new BinaryProcessor(new ByteProcessor(numCourses, numCourses));

        int color = 255 * 255 * 255;
        imageProcessor.setColor(color);
        imageProcessor.drawRect(0, 0, numCourses, numCourses);
        imageProcessor.fill();

        List<int[]> clashingPoints = getClashingPoints(studentDataFile);

        for (int[] point : clashingPoints) {
            imageProcessor.putPixel(point[0], point[1], 0);
        }

        ImagePlus imagePlus = new ImagePlus("car-f-92", imageProcessor);
        IJ.save(imagePlus, "car-f-92.png");
    }

    public int getNumberOfCourses(String fileName) {
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentLine = null;
            String visitedRow = null;
            while ((currentLine = bufferedReader.readLine()) != null) {
                visitedRow = currentLine.trim();
            }
            return Integer.parseInt(visitedRow.split(" ")[0]);
        } catch (Exception ex) {
            return -1;
        }
    }

    public List<int[]> getClashingPoints(String fileName) {
        List<int[]> clashingPoints = new ArrayList<>();
        try (FileReader fileReader = new FileReader(fileName);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String currentLine = null;
            while ((currentLine = bufferedReader.readLine()) != null) {
                String[] tokens = currentLine.split(" ");
                if (tokens.length > 1) {
                    for (int i = 0; i < tokens.length - 1; i++) {
                        for (int j = i + 1; j < tokens.length; j++) {
                            //if, for some case, the same course has been registered for the same student more than once
                            if (Integer.parseInt(tokens[i]) != Integer.parseInt(tokens[j])) {
                                clashingPoints.add(new int[] {Integer.parseInt(tokens[i]), Integer.parseInt(tokens[j])});
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            return null;
        }

        return clashingPoints;
    }
}