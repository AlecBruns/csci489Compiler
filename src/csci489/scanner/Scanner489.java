package csci489.scanner;

import com.sun.xml.internal.bind.v2.model.core.ID;
import csci489.exceptions.CDLException;

import java.io.*;
import java.util.HashMap;
import java.util.*;
import java.util.Map;

/**
 * Created by Alec Bruns on 9/6/2016.
 */
public class Scanner489 {

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

   //private List charList = new ArrayList();

    private List symbolTable = new ArrayList();

    private static int column = 0;
    private static int currentLine = 0;
    private static ArrayList codeLines;

    private String currentChar;


    private FileWriter writer;

    /*
    * Constructor for scanner. Creates writer for writing tokens and sets first Char.
     */
    public Scanner489(String file) throws CDLException{
        try{
            writer = new FileWriter("outputToken.txt");

        }catch(IOException e){

        }
        codeLines = recordCodeIntoLines(file);
        currentChar = getChar();
    }



    /*
    *
     */
    public void runScanner() throws CDLException{
        while(currentLine < codeLines.size()){
            charToToken();
        }
        finishWritting();
    }
    private void charToToken() throws CDLException{
        int tok = -1;
        if(currentChar.equals("+")){
            tok = PLUS;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals("-")){
            tok = MINUS;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals(";")){
            tok =SEMI;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals(",")){
            tok = COMMA;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals("(")){
            tok = LPAR;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals(")")){
            tok = RPAR;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals("*")) {
            tok = STAR;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals("/")) {
            tok= DVD;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals(">")){
            String temptok = getChar();
            if(temptok.equals("=")){
                tok = GER;
                writeToken(tok);
                currentChar = getChar();
            }
            else{
                tok = GTR;
                writeToken(tok);
            }
        }

        else if(currentChar.equals("<")){
            currentChar = getChar();
            if(currentChar.equals("=")){
                tok = LER;
                writeToken(tok);
                currentChar = getChar();
            }
            else{
                tok = LTR;
                writeToken(tok);
            }
        }

        else if (currentChar.equals(":")){
            currentChar = getChar();
            if(currentChar.equals("=")){
                tok = ASGN;
                writeToken(tok);
                currentChar =getChar();
            }
            else{
               throw new CDLException("Illegal Character");
            }
        }
        else if(currentChar.equals("#")) {
            tok = NER;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.equals("=")) {
            tok = EQR;
            writeToken(tok);
            currentChar = getChar();
        }
        else if(currentChar.matches("[a-zA-Z]+")){
            String temp = currentChar;
            currentChar = getChar();
            while(currentChar.matches("[a-zA-Z]+")){
                temp += currentChar;
                currentChar = getChar();
            }
            if(temp.equals("read")){
                tok = KWRD;
                writeToken(tok);
            }
            else if(temp.equals("write")){
                tok =KWWR;
                writeToken(tok);
            }
            else if(temp.equals("if")){
                tok = KWIF;
                writeToken(tok);
            }
            else if(temp.equals("then")) {
                tok = KWTH;
                writeToken(tok);
            }
            else if(temp.equals("else")) {
                tok = KWEL;
                writeToken(tok);
            }
            else if(temp.equals("fi")) {
                tok = KWFI;
                writeToken(tok);
            }
            else if(temp.equals("to")) {
                tok = KWTO;
                writeToken(tok);
            }
            else if(temp.equals("do")) {
                tok = KWDO;
                writeToken(tok);
            }
            else if(temp.equals("endloop")){
                tok = KWENDL;
                writeToken(tok);
            }
            else {
                tok = IDR;
                if(symbolTable.contains(temp)) {
                    throw new CDLException("Identifier already exists");
                }
                symbolTable.add(temp);
                writeToken(tok);
                writeToken(symbolTable.indexOf(temp));

            }
        }
        else if(currentChar.matches("[0-9]+")){
            String temp = currentChar;
            currentChar = getChar();
            while(currentChar.matches("[0-9]+")) {
               temp += currentChar;
                currentChar = getChar();
            }
            tok = CONST;
            writeToken(tok);
            int constant = Integer.parseInt(temp);
            writeToken(constant);
            currentChar = getChar();
        }else if(currentChar.equals("~")){


        }
        else {
            throw new CDLException("Illegal symbol at " + currentLine +":" +column);
        }
    }

    private void writeToken(int tok){
        try {
            writer.write(tok + " ");

        }catch(IOException e){

        }
    }

    private void finishWritting(){
        try {
            writer.close();
        }catch(IOException e){

        }
    }





    private static String getChar() throws CDLException{
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
                if (currentLine > codeLines.size()) {
                    return "~";
                }
            }
            result = currentChar;
        }catch(IndexOutOfBoundsException e){
            //throw new CDLException();
            return "~";
        }

        return result;
    }

    private static ArrayList recordCodeIntoLines(String file){
        String fileName =file ;
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
