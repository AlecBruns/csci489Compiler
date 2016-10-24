package csci489.parser;

import com.sun.xml.internal.bind.v2.model.core.ID;
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
    private ArrayList constantTable;


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
    private final int QUOTE = 32;


    private int tok;

    public CDLParser(List symbolList, ArrayList tokenList, ArrayList constantTable) {
        this.symbolList = symbolList;
        this.tokenList = tokenList;
        this.constantTable = constantTable;
    }

    public void runParser() throws CDLException {
        program();
    }


    private void program() throws CDLException {
        tok = readChar();
        if (tok==(KWDEC)) {
            tok = readChar();
            declpart();
        }
        stgroup();
        if (tok==(NER)) {
            tok = readChar();
            if (tok==(NER)) {

            } else
                throw new CDLException("error");
        } else
            throw new CDLException("error");
    }

    private void declpart() throws CDLException {
        decllist();
        if (tok !=(KWEDE))
            throw new CDLException("Invalid declaration format");
    }

    private void decllist() throws CDLException {
        decl();
        while (tok==(SEMI)) {
            tok = readChar();
            decl();
        }
    }

    private void decl() throws CDLException {
        if (tok==(KWINT)) {
            tok = readChar();
            idlist();
        } else
            throw new CDLException("error");
    }

    private void stgroup() throws CDLException {
        st();
        tok = readChar();
        while (tok==(SEMI)) {
            tok = readChar();
            st();
        }

    }

    private void st() throws CDLException {
        if (tok==(IDR)) {
            tok = readChar();
            asgn();
        } else if (tok==(KWRD)) {
            tok = readChar();
            read();
        } else if (tok==(KWWR)) {
            tok = readChar();
            write();
        } else if (tok==(KWIF)) {
            tok = readChar();
            cond();
        } else if (tok==(KWTO)) {
            tok = readChar();
            loop();
        } else
            throw new CDLException("error");

    }

    private void read() throws CDLException {
        idlist();
    }

    private void write() throws CDLException {
        outputlist();
    }

    private void idlist() throws CDLException {
        if (tok==(IDR)) {
            tok = readChar();
            identifier();
            while (tok==(COMMA)) {
                tok = readChar();
                identifier();
            }
        }
        else
            throw new CDLException("error");
    }

    private void outputlist() throws CDLException {
        if (tok==(QUOTE)) {
            tok = readChar();
            quote();
            while (tok==(COMMA)) {
                tok = readChar();
                if (tok==(QUOTE)) {
                    tok = readChar();
                    quote();
                } else
                    throw new CDLException("error");
            }
        } else {
            expr();
            while (tok==(COMMA)) {
                tok = readChar();
                expr();
            }
        }

    }

    private void quote() throws CDLException {
        word();
        if (tok==(QUOTE))
            tok = readChar();
        else
            throw new CDLException("error; missing end quote");


    }

    private void word() throws CDLException {
        String constant = (String) constantTable.get(tok);
        tok = readChar();
        for (int i = 0; i < constant.length(); i++) {
            String temp = constant.substring(i, i + 1);
            if (temp.matches("[a-zA-Z]+") || temp.matches("[0-9]+") || temp.equals(" ")) {

            }
            else{
                throw new CDLException("error");
            }


        }


    }

    private void identifier() throws CDLException {
        String id = (String) symbolList.get(tok);
        tok = readChar();
        if (!id.substring(0,1).matches("[a-zA-Z]+")) {
            throw new CDLException("error");
        }
        for (int i = 1; i < id.length(); i++) {
            String temp = id.substring(i, i + 1);
            if (temp.matches("[a-zA-Z]+") || temp.matches("[0-9]+")) {

            }
            else
                throw new CDLException("error");


        }
    }


    private void constant() throws CDLException {
        if (tok==(MINUS)) {
            tok = readChar();
        }
        String cons = Integer.toString(tok);
        for (int i = 0; i < cons.length(); i++) {
            String temp = cons.substring(i, i + 1);
            if (temp.matches("[0-9]+")) {
                tok = readChar();
            }
            else
                throw new CDLException("error");
        }

    }

    private void asgn() throws CDLException {
        identifier();
        if (tok==(ASGN)) {
            tok = readChar();
            expr();
        } else
            throw new CDLException("error");

    }

    private void expr() throws CDLException {
        term();
        while ((tok==(PLUS)) || tok==(MINUS)) {
            tok = readChar();
            term();
        }


    }


    private void term() throws CDLException {
        factor();
        while ((tok==(STAR)) || tok==(DVD)) {
            tok = readChar();
            factor();


        }


    }

    private void factor() throws CDLException {
        if (tok==(MINUS))
            tok = readChar();
        factor2();
    }

    private void factor2() throws CDLException {
        if (tok==(IDR)) {
            tok = readChar();
            identifier();
        } else if (tok==(CONST)) {
            tok = readChar();
            constant();

        } else if (tok==(LPAR)) {
            tok = readChar();
            expr();
            if (tok==(RPAR))
                tok = readChar();
            else
                throw new CDLException("Error");

        } else
            throw new CDLException("Error");


    }


    private void cond() throws CDLException {
        if (tok==(KWIF)) {
            tok = readChar();
            ifpart();
            stgroup();

            if (tok==(KWEL)) ;
            {
                tok = readChar();
                stgroup();
            }
            if (tok!=(KWFI))
                throw new CDLException("Error");
        }
    }


    private void ifpart() throws CDLException {
        expr();
        rel();
        expr();

        if (readChar()==(KWTH))
            tok = readChar();
        else
            throw new CDLException("Error");

    }

    private void rel() throws CDLException {
        if (tok==(EQR))
            tok = readChar();

        else if (tok==(GTR))
            tok = readChar();
        else if (tok==(PLUS))
            tok = readChar();

        else if (tok==(LTR))
            tok = readChar();
        else if (tok==(EQR))
            tok = readChar();

        else if (tok==(NER))
            tok = readChar();
        else if (tok==(LER))
            tok = readChar();
        else if (tok==(GER))
            tok = readChar();

        else {
            throw new CDLException("Error");


        }
    }


    private void loop() throws CDLException {
        if(tok==(KWTO)) {
            tok = readChar();
            loopPart();
            if (tok==(KWLO)) {
                tok = readChar();
                stgroup();
                if (tok==(KWENDL))
                    tok = readChar();
                else
                    throw new CDLException("error");
            }
        }

    }

    private void loopPart() throws CDLException {
        expr();
    }

    private int readChar() {
        int result = (Integer) tokenList.get(currentChar);
        currentChar++;
        return result;
    }
}
