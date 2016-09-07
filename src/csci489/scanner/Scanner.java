package csci489.scanner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.*;
import java.util.Map;

/**
 * Created by Alec Bruns on 9/6/2016.
 */
public class Scanner {

    private final int IDR = 1;
    private final int CONST = 2;
    private final int KWRD = 3;
    private final int KWWR = 4;
    private final int KWIF = 5;
    private final int KWTH = 6;
    private final int KWEL = 7;
    private final int KWFI = 8;
    private final int KWTO = 9;
    private final int KWDO = 10;
    private final int KWENDL = 11;
    private final int SEMI = 12;
    private final int COMMA = 13;
    private final int ASGN = 14;
    private final int PLUS = 15;
    private final int MINUS = 16;
    private final int STAR = 17;
    private final int DVD = 18;
    private final int EQR = 19;
    private final int GTR = 20;
    private final int LTR = 21;
    private final int LER = 22;
    private final int GER = 23;
    private final int NER = 24;
    private final int LPAR = 25;
    private final int RPAR = 26;

    private List charList = new ArrayList();

    private List symbolTable = new ArrayList();

    private static int column = 0;
    private static int currentLine = 0;
    private static ArrayList codeLines = recordCodeIntoLines();

    public static void main(String[] args){
        Scanner scanner = new Scanner();
        for(int i = 0; i < 25; i++)
            System.out.println(scanner.getChar());
    }

    private void readChar(){
        String currentChar = getChar();
        int tok = -1;
        if(currentChar.equals("+")){
            tok = PLUS;

        }
        else if(currentChar.equals(":")){
            
        }

    }




    private static String getChar(){
        String result;

        try {
            String currentChar = " ";
            while (currentChar.equals(" ")) {
                String tempLine = (String) codeLines.get(currentLine);
                currentChar = tempLine.substring(column, column + 1);
                column++;
                if (column >= tempLine.length()) {
                    column = 0;
                    currentLine++;
                }
                if (currentLine >= codeLines.size()) {
                    return "End";
                }
            }
            result = currentChar;
        }catch(IndexOutOfBoundsException e){
            return "End";
        }

        return result;
    }

    private static ArrayList recordCodeIntoLines(){
        String fileName = "test.txt";
        String line = null;
        ArrayList results = new ArrayList();

        try {
            FileReader fileReader = new FileReader(fileName);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                results.add(line);
            }


        }catch(FileNotFoundException e){

        }catch (IOException e){

        }
        return results;
    }
}
