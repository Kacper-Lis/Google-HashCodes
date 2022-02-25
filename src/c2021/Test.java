package c2021;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) throws FileNotFoundException {
        Parser[] parsers = getParsers();
        for(Parser p : parsers){
            System.out.println(p.name);
        }
    }

    public static Parser[] getParsers(){
        File[] folder = new File("src\\c2021").listFiles((dir, name) -> name.endsWith(".txt"));
        List<Parser> parsers = Arrays.stream(folder).map(o -> new Parser(o)).collect(Collectors.toList());
        return parsers.toArray(new Parser[0]);
    }
}
