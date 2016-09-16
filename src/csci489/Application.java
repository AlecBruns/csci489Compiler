package csci489;

import csci489.exceptions.CDLException;
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
        CDLParser CDLParser = new CDLParser();
        CDLScanner CDLScanner = new CDLScanner(file);
        CDLScanner.runScanner();
        CDLParser.runParser();

    }
}

