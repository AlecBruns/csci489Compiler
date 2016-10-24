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


    private String tok;

    public CDLParser(List symbolList, ArrayList tokenList) {
        this.symbolList = symbolList;
        this.tokenList = tokenList;
    }

    public void runParser() throws CDLException {
        program();
    }


    public void program() throws CDLException {
        tok = readChar();
        if (tok.equals(KWDEC)) {
            tok = readChar();
            declpart();
        }
        stgroup();
        if (tok.equals(NER)) {
            tok = readChar();
            if (tok.equals(NER)) {
                tok = readChar();
            } else
                throw new CDLException("error");
        } else
            throw new CDLException("error");
    }

    public void declpart() throws CDLException {
        decllist();
        if (!readChar().equals(KWEDE))
            throw new CDLException("Invalid declaration format");
    }

    public void decllist() throws CDLException {
        decl();
        tok = readChar();
        while (tok.equals(SEMI)) {
            tok = readChar();
            decl();
        }
    }

    public void decl() throws CDLException {
        if (tok.equals(KWINT)) {
            tok = readChar();
            idlist();
        } else
            throw new CDLException("error");
    }

    public void stgroup() throws CDLException {
        st();
        tok = readChar();
        while (tok.equals(SEMI)) {
            tok = readChar();
            st();
        }

    }

    public void st() throws CDLException {
        if (tok.equals(IDR)) {
            tok = readChar();
            asgn();
        } else if (tok.equals(KWRD)) {
            tok = readChar();
            read();
        } else if (tok.equals(KWWR)) {
            tok = readChar();
            write();
        } else if (tok.equals(KWIF)) {
            tok = readChar();
            cond();
        } else if (tok.equals(KWTO)) {
            tok = readChar();
            loop();
        } else
            throw new CDLException("error");

    }

    public void read() throws CDLException {
        idlist();
    }

    public void write() throws CDLException {
        outputlist();
    }

    public void idlist() throws CDLException {
        if (tok.equals(IDR)) {
            tok = readChar();
            identifier();
            while (tok.equals(COMMA)) {
                tok = readChar();
                identifier();
            }
        }
        else
            throw new CDLException("error");
    }

    public void outputlist() throws CDLException {
        if (tok.equals(QUOTE)) {
            tok = readChar();
            quote();
            while (tok.equals(COMMA)) {
                tok = readChar();
                if (tok.equals(QUOTE)) {
                    tok = readChar();
                    quote();
                } else
                    throw new CDLException("error");
            }
        } else {
            expr();
            while (tok.equals(COMMA)) {
                tok = readChar();
                expr();
            }
        }

    }

    public void quote() throws CDLException {
        word();
        if (tok.equals(QUOTE))
            tok = readChar();
        else
            throw new CDLException("error; missing end quote");


    }

    public void word() throws CDLException {

    }

    public void identifier() throws CDLException {
        String id = (String) symbolList.get(Integer.parseInt(tok));
        tok = readChar();
        if (!id.substring(0,1).matches("[a-zA-Z]+")) {
            throw new CDLException("error");
        }
        for (int i = 1; i < id.length(); i++) {
            String temp = id.substring(i, i + 1);
            if (temp.matches("[a-zA-Z]+") || !temp.matches("[0-9]+")) {
                tok = readChar();
            }
            else
                throw new CDLException("error");
        }
    }


    public void constant() throws CDLException {
        if (tok.equals(MINUS)) {
            tok = readChar();
        }

        for (int i = 0; i < tok.length(); i++) {
            String temp = tok.substring(i, i + 1);
            if (temp.matches("[0-9]+")) {
                tok = readChar();
            }
            else
                throw new CDLException("error");
        }

    }

    public void asgn() throws CDLException {
        identifier();
        if (tok.equals(ASGN)) {
            tok = readChar();
            expr();
        } else
            throw new CDLException("error");

    }

    public void expr() throws CDLException {
        term();
        while ((tok.equals(PLUS)) || tok.equals(MINUS)) {
            tok = readChar();
            term();
        }


    }


    public void term() throws CDLException {
        factor();
        while ((tok.equals(STAR)) || tok.equals(DVD)) {
            tok = readChar();
            factor();


        }


    }

    public void factor() throws CDLException {
        if (tok.equals(MINUS))
            tok = readChar();
        factor2();
    }

    public void factor2() throws CDLException {
        if (tok.equals(IDR)) {
            tok = readChar();
            identifier();
        } else if (tok.equals(CONST)) {
            tok = readChar();
            constant();

        } else if (tok.equals(LPAR)) {
            tok = readChar();
            expr();
            if (tok.equals(RPAR))
                tok = readChar();
            else
                throw new CDLException("Error");

        } else
            throw new CDLException("Error");


    }


    public void cond() throws CDLException {
        if (tok.equals(KWIF)) {
            tok = readChar();
            ifpart();
            stgroup();

            if (tok.equals(KWEL)) ;
            {
                tok = readChar();
                stgroup();
            }
            if (!readChar().equals(KWFI))
                throw new CDLException("Error");
        }
    }


    public void ifpart() throws CDLException {
        expr();
        rel();
        expr();

        if (readChar().equals(KWTH))
            tok = readChar();
        else
            throw new CDLException("Error");

    }

    public void rel() throws CDLException {
        if (tok.equals(EQR))
            tok = readChar();

        else if (tok.equals(GTR))
            tok = readChar();
        else if (tok.equals(PLUS))
            tok = readChar();

        else if (tok.equals(LTR))
            tok = readChar();
        else if (tok.equals(EQR))
            tok = readChar();

        else if (tok.equals(NER))
            tok = readChar();
        else if (tok.equals(LER))
            tok = readChar();
        else if (tok.equals(GER))
            tok = readChar();

        else {
            throw new CDLException("Error");


        }
    }


    public void loop() throws CDLException {
        if(tok.equals(KWTO)) {
            tok = readChar();
            loopPart();
            if (tok.equals(KWLO)) {
                tok = readChar();
                stgroup();
                if (tok.equals(KWENDL))
                    tok = readChar();
                else
                    throw new CDLException("error");
            }
        }

    }

    public void loopPart() throws CDLException {
        expr();
    }

    public String readChar() {
        String result = Integer.toString((Integer) tokenList.get(currentChar));
        currentChar++;
        return result;
    }
}
