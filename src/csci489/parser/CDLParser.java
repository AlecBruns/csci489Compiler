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

    public void read() throws CDLException {
        idlist();
    }

    public void write() throws CDLException{
        outputlist();
    }

    public void idlist() throws CDLException {
        identifier();
        while(tok.equals(COMMA)){
            tok = readChar();
            identifier();
        }
    }

    public void outputlist() throws CDLException{
        if(tok.equals(QUOTE)){
            tok = readChar();
            quote();
            while(tok.equals(COMMA)){
                tok = readChar();
                if(tok.equals(QUOTE)) {
                    tok = readChar();
                    quote();
                }
                else
                    throw new CDLException("error");
            }
        }
        else {
            expr();
            while(tok.equals(COMMA)){
                tok = readChar();
                expr();
            }
        }








    }

    public void quote() throws CDLException{
            word();
            if (tok.equals(QUOTE))
                tok = readChar();
            else
                throw new CDLException("error; missing end quote");









    }

    public void word() throws CDLException{
        if (tok.equals(IDR)) {
            tok = readChar();
            identifier();
        }
        else if (tok.equals(CONST)) {
            tok = readChar();
            constant();
        }
        else {
            tok = readChar();
            throw new CDLException("error; invalid word");
        }







    }

    public void identifier() throws CDLException{

        for(int i = 0; i < tok.length(); i++) {
            String temp = tok.substring(i, i + 1);
            if (!temp.matches("[a-zA-Z]+")) {
                throw new CDLException("error");
            }
        }
        while ((tok.matches("[a-zA-Z]+")) || tok.matches("[0-9]+")) {
            tok = readChar();
        }








    }









    /*public void letter() {









    }*/



    public void constant() throws CDLException {
        if(tok.equals(MINUS)){
            tok = readChar();
        }

        for(int i = 0; i < tok.length(); i++)
        {
            String temp = tok.substring(i, i+1);
            if (!temp.matches("[0-9]+"))
            {
                throw new CDLException("error");
            }
        }

    }

    public void asgn() throws CDLException {
        identifier();
        if (tok.equals(ASGN)) {
            tok = readChar();
            expr();
        }
        else
            throw new CDLException("error");







    }

    public void expr() throws CDLException{
        term();
        while((tok.equals(PLUS)) || tok.equals(MINUS)) {
            tok = readChar();
            term();
        }









    }

    public void adding() throws CDLException {
        if(tok.equals(PLUS)) {
            tok = readChar();

        }

        else if(tok.equals(MINUS)) {
            tok = readChar();

        }
        else
            throw new CDLException("Error");



}









    public void term() throws CDLException {
        factor();
        while((tok.equals(STAR)) || tok.equals(DVD)){
            tok=readChar();
            factor();


        }





    }

    public void multi() throws CDLException {

        if (tok.equals(STAR))
            tok = readChar();

        else if (tok.equals(DVD))
            tok = readChar();

        else
            throw new CDLException("Error");
    }










    public void factor() throws CDLException{
        if(tok.equals(MINUS)) {
            tok = readChar();
            factor2();
        }

        else
            factor2();


    }

    public void factor2() throws CDLException{
        if(tok.equals(IDR)){
            tok=readChar();
            identifier();
        }
        else if(tok.equals(CONST)){
            tok=readChar();

        }
        else if(tok.equals(LPAR)) {
            tok = readChar();
            expr();
            if(tok.equals(RPAR))
                tok = readChar();
            else
                throw new CDLException("Error");

        }
        else
            throw new CDLException("Error");


    }



    public void cond() throws CDLException {


        ifpart();
        stgroup();

        if(tok.equals(KWEL));
        {
            tok = readChar();
            if (tok.equals(KWFI))
                stgroup();
            else
                throw new CDLException("Error");
        }

        
        if(!readChar().equals(KWFI))
            throw new CDLException("Error");


}












    public void ifpart() throws CDLException {
        if(tok.equals(KWIF))
            tok=readChar();
            expr();
            rel();
            expr();

        if(!readChar().equals(KWTH))
            throw new CDLException("Error");











    }

    public void rel() throws CDLException {
        if (tok.equals(EQR))
            tok = readChar();

        else if (tok.equals(GTR)) {
            tok = readChar();
            if (tok.equals(PLUS))
                tok = readChar();

        } else if (tok.equals(LTR)) {
            tok = readChar();
            if (tok.equals(EQR)) {
                tok = readChar();
            }
        } else if (tok.equals(NER))
            tok = readChar();

        else if (tok.equals(KWIN))
            tok = readChar();

        else {
            throw new CDLException("Error");


        }
    }










    public void loop() throws CDLException{
        loopPart();
        stgroup();
        if(!readChar().equals(KWENDL))
            throw new CDLException("error");










    }

    public void loopPart() throws CDLException {
        if(tok.equals(KWTO)) {
            tok = readChar();
            expr();
        }

        else if(tok.equals(KWFOR)) {
            identifier();
            tok = readChar();

            if(tok.equals(KWIN)) {
                tok = readChar();
                expr();
            }
            else
                throw new CDLException("Error");
        }

        else
            throw new CDLException("Error");









    }

    public String readChar() {
        String result = (String)tokenList.get(currentChar);
        currentChar++;
        return result;
    }
}
