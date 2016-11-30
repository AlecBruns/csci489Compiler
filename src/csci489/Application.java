package csci489;

import csci489.exceptions.CDLException;
import csci489.interpreter.CDLInterpreter;
import csci489.parser.CDLParser;
import csci489.scanner.CDLScanner;

import java.util.Scanner;

/**
 * Created by Alec Bruns on 9/13/2016.
 */
public class Application {

    public static void main(String[] args)throws CDLException {
        System.out.println("Enter name of file to scan");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.next();
        CDLScanner cdlScanner = new CDLScanner(file);
        cdlScanner.runScanner();
        CDLParser cdlParser = new CDLParser(cdlScanner.getSymbolTable(), cdlScanner.getTokenTable(), cdlScanner.getConstantTable());
        cdlParser.runParser();
        CDLInterpreter cdlInterpreter = new CDLInterpreter(cdlParser.getPostFix());
        cdlInterpreter.interpret();

    }
}

