package csci489.interpreter;


import csci489.exceptions.CDLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Alec Bruns on 11/30/2016.
 */
public class CDLInterpreter {

    private List<String> postfix;
    private List<ArrayList> symbolList;
    private Stack<String> tempStack = new Stack<String>();

    public CDLInterpreter(List<String> postfix, List<ArrayList> symbolList){
        this.postfix = postfix;
        this.symbolList = symbolList;
    }

    public void interpret() throws CDLException{
        for(int i = 0; i < postfix.size(); i++){
            tempStack.push(postfix.get(i));
            if(postfix.get(i).equals("+")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp1;
                int temp2;
                temp1 = retreiveValue(holder);
                holder = tempStack.pop();
                temp2 = retreiveValue(holder);
                int total = temp1 + temp2;
                tempStack.push("2");
                tempStack.push(Integer.toString(total));

            }
            if(postfix.get(i).equals("/")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp1;
                int temp2;
                temp1 = retreiveValue(holder);
                holder = tempStack.pop();
                temp2 = retreiveValue(holder);
                int total = temp2 / temp1;
                tempStack.push("2");
                tempStack.push(Integer.toString(total));

            }
            else if(postfix.get(i).equals("-")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp1;
                int temp2;
                temp1 = retreiveValue(holder);
                holder = tempStack.pop();
                temp2 = retreiveValue(holder);
                int total = temp2 - temp1;
                tempStack.push("2");
                tempStack.push(Integer.toString(total));

            }
            else if(postfix.get(i).equals("--")){
                tempStack.pop();
                String holder = tempStack.pop();
                int temp;
                temp = retreiveValue(holder);
                int total = temp * -1;
                tempStack.push("2");
                tempStack.push(Integer.toString(total));
            }
            else if(postfix.get(i).equals("*")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp1;
                int temp2;
                temp1 = retreiveValue(holder);
                holder = tempStack.pop();
                temp2 = retreiveValue(holder);
                int total = temp1 * temp2;
                tempStack.push("2");
                tempStack.push(Integer.toString(total));

            }

            else if(postfix.get(i).equals(":=")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp;
                temp = retreiveValue(holder);
                holder = tempStack.pop();
                if(tempStack.pop().equals("1")){
                    symbolList.get(Integer.parseInt(holder)).set(1, Integer.toString(temp));
                }
                else
                    throw new CDLException("Error");


            }
            else if(postfix.get(i).equals("Write")){
                tempStack.pop();
                String holder = tempStack.pop();

                int temp;
                if(holder.matches("[a-zA-Z]+")){
                    tempStack.pop();
                    System.out.println(holder);
                }
                else {
                    temp = retreiveValue(holder);
                    System.out.println(temp);
                }


            }
            else if(postfix.get(i).equals("Read")){
                tempStack.pop();
                Scanner scanner = new Scanner(System.in);
                int result = scanner.nextInt();
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("1")){
                    throw new CDLException("Error");
                }
                symbolList.get(Integer.parseInt(holder)).set(1,Integer.toString(result));
            } else if (postfix.get(i).equals("BR")) {
                tempStack.pop();
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                if(Integer.parseInt(holder) < postfix.size())
                    i = Integer.parseInt(holder) - 1;


            }
            else if (postfix.get(i).equals("BZ")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp == 0)
                    i = Integer.parseInt(holder) - 1;

            }
            else if (postfix.get(i).equals("BNZ")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp != 0)
                    i = Integer.parseInt(holder) - 1;

            } else if (postfix.get(i).equals("BMZ")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp <= 0)
                    i = Integer.parseInt(holder) - 1;


            } else if (postfix.get(i).equals("BM")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp < 0)
                    i = Integer.parseInt(holder) - 1;


            } else if (postfix.get(i).equals("BP")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp > 0)
                    i = Integer.parseInt(holder) - 1;

            } else if (postfix.get(i).equals("BPZ")) {
                tempStack.pop();
                int temp;
                String holder = tempStack.pop();
                if(!tempStack.pop().equals("2")){
                    throw new CDLException("error");
                }
                String holder2 = tempStack.pop();
                temp = retreiveValue(holder2);

                if(Integer.parseInt(holder) < postfix.size() && temp >= 0)
                    i = Integer.parseInt(holder) - 1;

            }
            else if (postfix.get(i).equals("BS")){
                tempStack.pop();
                String temp = tempStack.peek();
                if(Integer.parseInt(temp) >= 0) {
                    tempStack.push("2");
                    tempStack.push(temp);
                }
            }

        }

    }

    private int retreiveValue(String holder) throws CDLException{
        int temp1;
        String value = tempStack.pop();
        if(value.equals("2"))
            temp1 = Integer.parseInt(holder);
        else if(value.equals("1"))
            temp1 = Integer.parseInt((String)symbolList.get(Integer.parseInt(holder)).get(1));
        else
            throw new CDLException("Error");

        return temp1;
    }
}
