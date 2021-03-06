//Charles Armstrong ID:20046148

import java.io.BufferedReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;


public class Assn2_16chra {
    final static int ROW=1000;
    final static int COL=8;

    public static String[] readIt(String FileIn){
        String[] data = new String[ROW];
        Path file = Paths.get(FileIn); //Specifying location of file specific to file inputted.
        if(!file.toFile().exists()){ //Case if file does not exist.
            System.err.println("The file" + FileIn + "cannot be found");
            return null;
        }
        try(BufferedReader reader = Files.newBufferedReader(file)){ //Read file in.
            for(int i = 0;i<ROW; i++) { //Counter to read in through amount of rows.
                data[i] = reader.readLine();
            }
        } catch (IOException err) {
            System.err.println(err.getMessage());
            return null;
        }
            return data;
    }
    //Next we need to convert the 1D array into a 2D array and cast all the ints to doubles.
    public static double[][] convertArray (String[] data){
        if(data==null){
            System.err.println("There is no data");
            return null;
        }
        double in;
        double[][] newArray = new double[ROW][COL];
        StringTokenizer splitUp;
        int r, c;
        for(r=0;r<ROW;r++){
            splitUp = new StringTokenizer(data[r],","); //Takes data in the row, splits it up where the commas are.
            for(c=0;c<COL;c++){
                in = Double.parseDouble(splitUp.nextToken());
                newArray[r][c] = in;

            }
        }
        return newArray;
    }

    public static String conditionOutput(int motorNumber, double[][] data) {
        int time;
        int r;
        int startTime = 0;
        int endTime = 0;
        double restCurrent = 1.0;
        double aveCurrent = 0;//Initializing all to zero
        double maxCurrent = 8.0;
        double current;
        String completedOutput = "";
        for (r = 0; r < ROW; r++) {
            current = data[r][motorNumber]; //get data using row and motor number
            time = (int) data[r][0]; //get time to see when things change
            if (current >= restCurrent) { //the motor is on, now we get the start time for this surge.
                if (startTime == 0)
                    startTime = time;
                aveCurrent = aveCurrent + current;
            } else if (startTime > 0) { //after current surge.
                endTime = time - 1;
                aveCurrent = aveCurrent / (endTime - startTime + 1);
                aveCurrent = Math.round(1000 * aveCurrent)/1000.0; //turns to 3 decimal place decimal.
                completedOutput = completedOutput + startTime +"," +endTime +","+ aveCurrent;
                if(aveCurrent>maxCurrent){
                    completedOutput += ",***CURRENT EXCEEDED";
                completedOutput += "\r\n";
                startTime = 0;
                aveCurrent = 0;
            }

        }

    }
        if (completedOutput.length() == 0)
            completedOutput += "The motor is not being used.\r\n";
                    return completedOutput;
        //Make a temp array checking for spikes in the current, record the times of spike.
        //Use to  calculate averages.
        //if a motor does not run over the entire logged interval then list at "not used"
        //Currents not recorded to more than three significant digits after decimal point

    }


    public static void createFiles(String outputLocation, String info){
        Path file = Paths.get(outputLocation);
        try(BufferedWriter writer = Files.newBufferedWriter(file)){
            writer.write(info);
        }
        catch(IOException err){
            System.err.println(err.getMessage());
        }
    }
   public static void main (String[] args){
        String[] startData;
        startData = readIt("Logger.csv");
       double [][] data = convertArray(startData);
       String assessment;
       int motor;
       for(motor=0; motor<7; motor++){
           assessment= "Start(sec), Finish(sec), Current(amps)\r\n";
           assessment= assessment +conditionOutput(motor,data);
           createFiles("Motor"+motor+".csv", assessment);

       }
  System.out.println("Assessment complete");
   }

}








