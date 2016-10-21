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
    private ArrayList tokenList;


    private static ArrayList tokens;
    private int currentChar = 0;

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
    private final int KWIN  = 30;
    private final int KWFOR = 31;
    private final int QUOTE = 32;


    private String tok;

    public CDLParser(List symbolList, ArrayList tokenList) {
        this.symbolList = symbolList;
        this.tokenList = tokenList;
    }

    public void runParser() throws CDLException {
        program();
    }


    public void program() throws CDLException{
            tok = readChar();
        if(tok.equals(KWDEC)){
            tok = readChar();
            declpart();
        }
        stgroup();
        if(tok.equals(NER)){
            tok = readChar();
            if(tok.equals(NER)){
                tok = readChar();
            }
            else
                throw new CDLException("error");
        }
        else
            throw new CDLException("error");
    }

    public void declpart() throws CDLException{
            decllist();
            if(!readChar().equals(KWEDE))
               throw new CDLException("Invalid declaration format");
        }

    public void decllist() throws CDLException{
        decl();
        tok = readChar();
        while(tok.equals(SEMI)){
            tok = readChar();
            decl();
        }
    }

    public void decl() throws CDLException{
        if(tok.equals(KWINT)) {
            tok = readChar();
            idlist();
        }
        else
            throw new CDLException("error");
    }

    public void stgroup() throws CDLException{
        st();
        tok = readChar();
        while(tok.equals(SEMI)){
            tok = readChar();
            st();
        }

    }

    public void st() throws CDLException{
        if(tok.equals(IDR)){
            tok = readChar();
            asgn();
        }
        else if(tok.equals(KWRD)){
            tok = readChar();
            read();
        }
        else if(tok.equals(KWWR)){
            tok = readChar();
            write();
        }
        else if(tok.equals(KWIF)){
            tok = readChar();
            cond();
        }
        else if(tok.equals(KWTO) || tok.equals(KWFOR)){
            tok = readChar();
            loop();
        }
        else
            throw new CDLException("error");

    }

    public void read() {
        idlist();
    }

    public void write() {
        outputlist();
    }

    public void idlist() {
        identifier();
        tok = readChar();
        while(tok.equals(COMMA)){
            tok = readChar();
            identifier();
        }
    }

    public void outputlist() {









    }

    public void qoute() throws CDLException{
        if(tok.equals(QUOTE)) {
            tok = readChar();
            word();
            if (tok.equals(QUOTE))
                tok = readChar();
            else
                throw new CDLException("error; missing end quote");
        }
        else
            throw new CDLException("error");









    }

    public void word() throws CDLException{
        if (tok.equals(IDR)) {
            tok = readChar();
            identifier();
        }
        else if (tok.equals(CONST)) {
            tok = readChar();
            digit();
        }
        else {
            tok = readChar();
            throw new CDLException("error; invalid word");
        }







    }

    public void identifier(){













    }









    public void letter() {









    }

    public void digit() {













    }

    public void constant() {












    }

    public void asgn() throws CDLException {
        if (tok.equals(ASGN))
            tok = readChar();
            if (tok.equals(EXPR))
                //return true
            else
                throw new CDLException("error");
        else
            throw new CDLException("error");










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
        String result = (String)tokenList.get(currentChar);
        currentChar++;
        return result;
    }
}
