package csci489.scanner;

import csci489.exceptions.CDLException;

import java.io.*;
import java.util.*;

/**
 * Created by Alec Bruns on 9/6/2016.
 */
public class CDLScanner {

    private final int IDR = 1;
    private final int CONST = 2;
    private final int KWRD = 3;
    private final int KWWR = 4;
    private final int KWIF = 5;
    private final int KWTH = 6;
    private final int KWEL = 7;
    private final int KWFI = 8;
    private final int KWTO = 9;
    private final int KWLO = 10;
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
    private final int KWDEC = 27;
    private final int KWEDE = 28;
    private final int KWINT = 29;
    private final int QUOTE = 32;


    private List symbolTable = new ArrayList();
    private ArrayList tokenTable = new ArrayList();
    private ArrayList constantTable = new ArrayList();

    private static int column = 0;
    private static int currentLine = 0;
    private static ArrayList codeLines;

    private String currentChar;

    private boolean declaration = false;
    private static boolean currentLineUnchanged = false;

    private FileWriter writer;

    /*
    * Constructor for scanner. Creates writer for writing tokens and sets first Char.
     */
    public CDLScanner(String file) throws CDLException {
        try {
            writer = new FileWriter("outputToken.txt");

        } catch (IOException e) {
            throw new CDLException("IO Exception");
        }
        codeLines = recordCodeIntoLines(file);
        currentChar = getChar();
    }


    /*
    *   Runs scanner across all characters in code file
     */
    public void runScanner() throws CDLException {
        while (currentLine < codeLines.size()) {
            charToToken();
        }
        finishWritting();
        writeSymbolTable();
        writeTokenTable();
    }

    /*
    Checks Current character with Token listing and writes results to output file
     */
    private void charToToken() throws CDLException {
        int tok = -1;
        if (currentChar.equals("+")) {
            tok = PLUS;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals("-")) {
            tok = MINUS;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals(";")) {
            tok = SEMI;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals(",")) {
            tok = COMMA;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals("(")) {
            tok = LPAR;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals(")")) {
            tok = RPAR;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals("*")) {
            tok = STAR;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals("'")) {
            tok = QUOTE;
            writeToken(tok);
            currentChar = getChar();
            String temp = "";
            while(!currentChar.equals("'")){
                temp += currentChar;
                currentChar = getChar();
            }
            if(currentChar.equals("'")){
                constantTable.add(temp);
                tok = constantTable.indexOf(temp);
                writeToken(tok);
            }
            else
                throw new CDLException("End qoute not found");
        } else if (currentChar.equals("/")) {
            tok = DVD;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals(">")) {
            String temptok = getChar();
            if (temptok.equals("=")) {
                tok = GER;
                writeToken(tok);
                currentChar = getChar();
            } else {
                tok = GTR;
                writeToken(tok);
            }
        } else if (currentChar.equals("<")) {
            currentChar = getChar();
            if (currentChar.equals("=")) {
                tok = LER;
                writeToken(tok);
                currentChar = getChar();
            } else {
                tok = LTR;
                writeToken(tok);
            }
        } else if (currentChar.equals(":")) {
            currentChar = getChar();
            if (currentChar.equals("=")) {
                tok = ASGN;
                writeToken(tok);
                currentChar = getChar();
            } else {
                throw new CDLException("Illegal Character at " + currentLine + ":" + column);
            }
        } else if (currentChar.equals("#")) {
            tok = NER;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.equals("=")) {
            tok = EQR;
            writeToken(tok);
            currentChar = getChar();
        } else if (currentChar.matches("[a-zA-Z]+")) {
            String temp = currentChar;
            currentChar = getChar();
            while (currentChar.matches("[a-zA-Z]+") && !currentLineUnchanged) {
                temp += currentChar;
                currentChar = getChar();


            }
            if (currentChar.matches("[a-zA-Z]+") && currentLineUnchanged) {
                temp += currentChar;
                currentChar = getChar();
                currentLineUnchanged = true;
            }
            if (temp.equals("read")) {
                tok = KWRD;
                writeToken(tok);
            } else if (temp.equals("write")) {
                tok = KWWR;
                writeToken(tok);
            } else if (temp.equals("if")) {
                tok = KWIF;
                writeToken(tok);
            } else if (temp.equals("then")) {
                tok = KWTH;
                writeToken(tok);
            } else if (temp.equals("else")) {
                tok = KWEL;
                writeToken(tok);
            } else if (temp.equals("fi")) {
                tok = KWFI;
                writeToken(tok);
            } else if (temp.equals("to")) {
                tok = KWTO;
                writeToken(tok);
            } else if (temp.equals("loop")) {
                tok = KWLO;
                writeToken(tok);
            } else if (temp.equals("endloop")) {
                tok = KWENDL;
                writeToken(tok);

            } else if (temp.equals("declare")) {
                tok = KWDEC;
                writeToken(tok);
            } else if (temp.equals("enddeclare")) {
                tok = KWEDE;
                writeToken(tok);
                declaration = true;
            } else if (temp.equals("integer")) {
                tok = KWINT;
                writeToken(tok);
            } else {
                tok = IDR;
                while (currentChar.matches("[0-9]+")) {
                    temp += currentChar;
                    currentChar = getChar();
                }
                if (symbolTable.contains(temp) && !declaration) {
                    throw new CDLException("Identifier already exists at " + currentLine + ":" + column);
                } else if (declaration && !symbolTable.contains(temp)) {
                    throw new CDLException("Identifier not declared at " + currentLine + ":" + column);
                }
                if (!symbolTable.contains(temp))
                    symbolTable.add(temp);
                writeToken(tok);
                writeToken(symbolTable.indexOf(temp));

            }
        } else if (currentChar.matches("[0-9]+")) {
            String temp = currentChar;
            currentChar = getChar();
            while (currentChar.matches("[0-9]+")) {
                temp += currentChar;
                currentChar = getChar();
            }
            tok = CONST;
            writeToken(tok);
            int constant = Integer.parseInt(temp);
            writeToken(constant);
        } else {
            throw new CDLException("Illegal symbol at " + currentLine + ":" + column);
        }
    }

    /*
    Writes Token to output file
     */
    private void writeToken(int tok) throws CDLException {
        tokenTable.add(tok);
    }

    /*
    Close writer object after all writes are complete.
    Must be called to have the writes actually be present on output file.
     */
    private void finishWritting() throws CDLException {
        try {
            writer.close();
        } catch (IOException e) {
            throw new CDLException("IO Exception");
        }
    }

    /*
    Finds the next token in the array holding all of the code
     */
    private static String getChar() throws CDLException {
        String result = "";
        currentLineUnchanged = false;

        try {
            String currentChar = " ";
            while (currentChar.equals(" ")) {
                String tempLine = (String) codeLines.get(currentLine);
                currentChar = tempLine.substring(column, column + 1);
                column++;
                if (column >= tempLine.length()) {
                    column = 0;
                    currentLine++;
                    currentLineUnchanged = true;
                }
            }
            result = currentChar;
        } catch (IndexOutOfBoundsException e) {
            //throw new CDLException("Out of bound Exception");
        }

        return result;
    }

    /*
    Scans a text file and writes each line as one position in an ArrayList.
     */
    private static ArrayList recordCodeIntoLines(String file) throws CDLException {
        String line;
        ArrayList results = new ArrayList();

        try {
            FileReader fileReader = new FileReader(file);

            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                results.add(line);
            }


        } catch (FileNotFoundException e) {
            throw new CDLException("File not Found");
        } catch (IOException e) {
            throw new CDLException("IOException");
        }
        return results;
    }

    /*
    Writes symbol table to console
     */
    private void writeSymbolTable() {
        System.out.println();
        System.out.println();
        System.out.println("Symbol Table");
        System.out.println();
        for (int i = 0; i < symbolTable.size(); i++) {
            System.out.print(symbolTable.indexOf(symbolTable.get(i)) + " " + symbolTable.get(i));
            System.out.println();
        }
    }

    /*
    Writes Character to Token table
     */

    private void writeTokenTable() {
        System.out.println();
        System.out.println("Token Table");
        System.out.println();
        System.out.println("1  identifier");
        System.out.println("2  constant");
        System.out.println("3  read");
        System.out.println("4  write");
        System.out.println("5  if");
        System.out.println("6  then");
        System.out.println("7  else");
        System.out.println("8  fi");
        System.out.println("9  to");
        System.out.println("10  do");
        System.out.println("11  endloop");
        System.out.println("12  ;");
        System.out.println("13  ,");
        System.out.println("14  :=");
        System.out.println("15  +");
        System.out.println("16  -");
        System.out.println("17  *");
        System.out.println("18  /");
        System.out.println("19  =");
        System.out.println("20  >");
        System.out.println("21  <");
        System.out.println("22  <=");
        System.out.println("23  >=");
        System.out.println("24  #");
        System.out.println("25  (");
        System.out.println("26  )");
        System.out.println("27  declare");
        System.out.println("28  enddeclare");
        System.out.println("29  integer");
        System.out.println("30  in");
        System.out.println("31 for");


    }

    public List getSymbolTable() {
        return symbolTable;
    }

    public ArrayList getTokenTable() {
        return tokenTable;
    }

    public ArrayList getConstantTable(){ return  constantTable; }
}
