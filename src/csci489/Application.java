package csci489;

import csci489.exceptions.CDLException;
import csci489.scanner.Scanner489;

import java.util.Scanner;

/**
 * Created by Alec Bruns on 9/13/2016.
 */
public class Application {

    public static void main(String[] args)throws CDLException {
        System.out.println("Enter name of file to scan");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.next();
        Scanner489 scanner489 = new Scanner489(file);
        scanner489.runScanner();
    }
}

