package csci489.parser;

import csci489.exceptions.CDLException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alec Bruns on 9/14/2016.
 */
public class CDLParser {
    private List symbolList;
    private String tokenList;


    private static ArrayList tokens;
    private int currentChar = 0;

    public CDLParser(List symbolList, String tokenList) {
        this.symbolList = symbolList;
        this.tokenList = tokenList;
    }

    public void runParser() throws CDLException {

    }

    public void stgroup() {

    }

    public void st() {

    }

    public void read() {

    }

    public void write() {

    }

    public void idlist() {

    }

    public void outputlist() {

    }

    public void qoute() {

    }

    public void word() {

    }

    public void identifier() {

    }

    public void letter() {

    }

    public void digit() {

    }

    public void constant() {

    }

    public void asgn() {

    }

    public void expr() {

    }

    public void adding() {

    }

    public void term() {

    }

    public void multi() {

    }

    public void factor() {

    }

    public void factor2() {

    }

    public void cond() {

    }

    public void ifpart() {

    }

    public void rel() {

    }

    public void loop() {

    }

    public void loopPart() {

    }

    public String readChar() {
        String result = tokenList.substring(currentChar, currentChar+1);
        currentChar++;
        return result;
    }
}
