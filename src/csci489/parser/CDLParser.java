package csci489.parser;


import csci489.exceptions.CDLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    private Stack<String> postfix = new Stack<>();

    private Stack<String> tempStack = new Stack<>();

    /*
    Constructor
     */
    public CDLParser(List symbolList, ArrayList tokenList, ArrayList constantTable) {
        this.symbolList = symbolList;
        this.tokenList = tokenList;
        this.constantTable = constantTable;
    }

    /*
    Method to run parser in main application
     */
    public void runParser() throws CDLException {
         program();
    }

    /*
    program non-terminal
     */
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
                System.out.println("Success");

            } else
                throw new CDLException("Missing end character");
        } else
            throw new CDLException("Missing end character or missing symbol");
    }

    /*
    declaration part non-terminal. Goes through declaration phase
     */
    private void declpart() throws CDLException {
        decllist();
        if (tok ==(KWEDE))
            tok = readChar();
        else
            throw new CDLException("Invalid declaration format");
    }

    /*
    Declare List non-terminal. Reads every declaration call
     */
    private void decllist() throws CDLException {
        decl();
        while (tok==(SEMI)) {
            tok = readChar();
            decl();
        }
    }

    /*
     Declare non-terminal. Checks integer is in token list then calls idlist
     */
    private void decl() throws CDLException {
        if (tok==(KWINT)) {
            tok = readChar();
            idlist();
        } else
            throw new CDLException("Invalid declaration. Missing integer call");
    }

    /*
    Statement group non-terminal. Goes through every statement non-terminal, calling them when separated by semi-colon
     */
    private void stgroup() throws CDLException {
        st();
        while (tok==(SEMI)) {
            tok = readChar();
            st();
        }

    }

    /*
    Statement non-terminal. Checks to be sure statement is valid.
     */
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
            throw new CDLException("Missing or Illegal Statement");

    }

    /*
    Read non-terminal
     */
    private void read() throws CDLException {
        idlist();
    }

    /*
    Write non-terminal
     */
    private void write() throws CDLException {
        outputlist();

    }

    /*
    Identifier-list non-terminal. Goes through all identifiers separated by a comma
     */
    private void idlist() throws CDLException {
        if (tok==(IDR)) {
            tok = readChar();
            identifier();
            postfix.push("Read");
            while (tok==(COMMA)) {
                tok = readChar();
                    identifier();
                postfix.push("Read");
            }
        }
        else
            throw new CDLException("Identifier not found. Please check to be sure constant or reserved words were not used.");
    }

    /*
    Output list non-terminal. Checks if output for write is a string constant, if not runs expression non-terminal method
     */
    private void outputlist() throws CDLException {
        if (tok==(QUOTE)) {
            tok = readChar();
            postfix.push("2");
            quote();
            postfix.push("Write");
            while (tok==(COMMA)) {
                tok = readChar();
                if (tok==(QUOTE)) {
                    tok = readChar();
                    quote();
                    postfix.push("Write");
                }
                else
                    throw new CDLException("error");
            }
        } else {
            expr();
            postfix.push("Write");

            while (tok==(COMMA)) {
                tok = readChar();
                expr();
                postfix.push("Write");
            }
        }

    }

    /*
    Quote non-terminal. Calls word non-terminal method after checking for quote and checks closing quote is used.
     */
    private void quote() throws CDLException {
        word();
        if (tok==(QUOTE))
            tok = readChar();
        else
            throw new CDLException("Error: missing end quote");


    }

    /*
    Word non-terminal. Checks string constant to verify only letters, spaces and numbers are used
     */
    private void word() throws CDLException {
        String constant = (String) constantTable.get(tok);
        postfix.push(constant);
        tok = readChar();
        for (int i = 0; i < constant.length(); i++) {
            String temp = constant.substring(i, i + 1);
            if (temp.matches("[a-zA-Z]+") || temp.matches("[0-9]+") || temp.equals(" ")) {

            }
            else{
                throw new CDLException("String Constant is not valid. Illegal charcters detected.");
            }


        }


    }

    /*
    Identifier non-terminal. Retrieves idr from symbol list and checks that it is a valid idr
     */
    private void identifier() throws CDLException {
        ArrayList symbol = (ArrayList) symbolList.get(tok);
        postfix.push("1");
        postfix.push(Integer.toString(tok));
        String id = (String) symbol.get(0);
        tok = readChar();
        if (!id.substring(0,1).matches("[a-zA-Z]+")) {
            throw new CDLException("Identifier does not start with a letter");
        }
        for (int i = 1; i < id.length(); i++) {
            String temp = id.substring(i, i + 1);
            if (temp.matches("[a-zA-Z]+") || temp.matches("[0-9]+")) {

            }
            else
                throw new CDLException("Illegal character found in identifier name. ");


        }
    }


    /*
    Constant non-terminal. Checks to make sure a constant is only made of numbers
     */
    private void constant() throws CDLException {
        postfix.push("2");
        postfix.push(Integer.toString(tok));
        String cons = Integer.toString(tok);
        for (int i = 0; i < cons.length(); i++) {
            String temp = cons.substring(i, i + 1);
            if (temp.matches("[0-9]+")) {

            }
            else
                throw new CDLException("Non number symbol found in constant");
        }
        tok = readChar();

    }

    /*
    asgn non-terminal. Calls identifer method and checks that the assignment token is next.
     */
    private void asgn() throws CDLException {
        identifier();
        if (tok==(ASGN)) {
            tok = readChar();
            expr();
            postfix.push(":=");
        } else
            throw new CDLException("Assignment symbol not found");

    }

    /*
    Expression non-terminal. Calls term and checks for plus and minus symbols
     */
    private void expr() throws CDLException {
        term();
        while ((tok==(PLUS)) || tok==(MINUS)) {
            String temp = "";
            if(tok ==PLUS )
                temp = "+";
            else
                temp= "-";
            tok = readChar();
            term();
            tempStack.push(temp);


        }

        for(int i = 0; i < tempStack.size(); i++){
            postfix.push(tempStack.pop());
        }


    }

    /*
    term non-terminal. Calls factor and checks for star and division symbols
     */
    private void term() throws CDLException {
        factor();
        while ((tok==(STAR)) || tok==(DVD)) {
            String temp = "";
            if(tok == STAR)
                temp = "*";
            else
                temp = "/";
            tok = readChar();
            factor();
            postfix.push(temp);

        }


    }

    /*
    factor. Checks for an optional negative then checks for an idr, constant or another expr in paras
     */
    private void factor() throws CDLException {
        if (tok==(MINUS))
            tok = readChar();
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
                throw new CDLException("Right Para not found");

        } else
            throw new CDLException("No valid symbols found. Only constants, identifiers and expressions are valid");
    }

    /*
    Conditional non-terminal. Calls necessary parts of conditional statement and checks for optional else
     */
    private void cond() throws CDLException {
            ifpart();
            stgroup();
            if(tok==KWEL)
            {
                tok = readChar();
                stgroup();
            }
            if (tok==(KWFI))
                tok = readChar();
            else
                throw new CDLException("Fi reserved word not found");
    }

    /*
    ifpart non-terminal. checks for a relation for the conditional
     */
    private void ifpart() throws CDLException {
        expr();
        rel();
        expr();

        if (tok==(KWTH))
            tok = readChar();
        else
            throw new CDLException("Then reserved word not found");

    }

    /*
    rel non-terminal. Checks for a rel symbol
     */
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
            throw new CDLException("No relation symbol found");


        }
    }

    /*
    loop non-terminal.
     */
    private void loop() throws CDLException {
            expr();
            if (tok==(KWLO)) {
                tok = readChar();
                stgroup();
                if (tok==(KWENDL))
                    tok = readChar();
                else
                    throw new CDLException("No endloop reserved word found");
            }
    }


    /*
    Reads next token
     */
    private int readChar() {
        int result = (Integer) tokenList.get(currentChar);
        currentChar++;
        return result;
    }
}
