package blueoptima;

import com.google.common.base.Joiner;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class FileIdentifier {
    private static String outputJSONFileName = "output.txt";

    public static void main(String[] args) throws IOException, WrongInputArgumentException {
        System.out.println(Joiner.on(" ").join(Arrays.asList(args)));
        if (args == null) {
            throw new NullPointerException("Must provide args");
        } else if (args.length == 0) {
            throw new WrongInputArgumentException("No input arguments provided");
        }
        if (args[0].equals("FILENAME") && args.length == 2) {
            new FileIdentifier(args[1]);
        } else if (args[0].equals("FILE")  && args.length == 2) {
            new FileIdentifier(fileReader(args[1]));
        } else if (args[0].equals("FILENAMES")  && args.length > 1) {
            List<String> fileNames = new ArrayList<>();
            fileNames.addAll(Arrays.asList(args));
            fileNames.remove(0);
            new FileIdentifier(fileNames);
        } else {
            throw new WrongInputArgumentException("Invalid input arguments");
        }
    }

    public FileIdentifier(String fileName) throws WrongInputArgumentException{
        if(fileName == null){
            throw new WrongInputArgumentException("Provide Filenames");
        }
        ArrayList<FileName> list = new ArrayList<>();
        list.add(new FileName(fileName));
        TaskExecutor taskExecutor = new TaskExecutor(list);
        taskExecutor.execute();
        System.out.println(taskExecutor.getResultAsJson());
        fileWriter(taskExecutor.getResultAsJson());
    }

    public FileIdentifier(List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("Input argument is empty");
        }
        List<FileName> fileNames = stringList.parallelStream().parallel().filter(new Predicate<String>() {
            @Override
            public boolean test(String s) {
                if (s == null) {
                    return false;
                }
                if (!s.contains(".")) {
                    return false;
                }
                return true;
            }
        }).map(new Function<String, FileName>() {
            @Override
            public FileName apply(String s) {
                return new FileName(s);
            }
        }).collect(Collectors.toList());
        TaskExecutor taskExecutor = new TaskExecutor(fileNames);
        taskExecutor.execute();
        System.out.println(taskExecutor.getResultAsJson());
        fileWriter(taskExecutor.getResultAsJson());


    }

    public static List<String> fileReader(String fileName) {
        try {
            BufferedReader inputFile = new BufferedReader(new FileReader(fileName));
            String line;
            List<String> stringList = new ArrayList<String>();
            while ((line = inputFile.readLine()) != null) {
                stringList.add(line.trim());
            }
            inputFile.close();
            return stringList;
        } catch (FileNotFoundException e) {
            System.out.println("File not found :" + e.getMessage());

        } catch (IOException e) {
            System.out.println("Error while file reading : " + e.getMessage());
        }
        return null;
    }

    public static void fileWriter(String resultJSON){
        try {
            BufferedWriter outputfile = new BufferedWriter(new FileWriter(outputJSONFileName));
            outputfile.write(resultJSON);
            outputfile.close();
        } catch (IOException e) {
            System.out.println("Error while file writing "+ e.getMessage());
        }
    }
}

class WrongInputArgumentException extends Exception {

    public WrongInputArgumentException(String msg) {
        super(msg);
    }

}
