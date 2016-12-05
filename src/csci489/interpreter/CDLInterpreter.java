package csci489.interpreter;


import csci489.exceptions.CDLException;

import java.util.ArrayList;
import java.util.List;
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
                int total = temp1 / temp2;
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
                int total = temp1 - temp2;
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
                    symbolList.get(Integer.parseInt(holder)).set(1, temp);
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

        }

    }

    private int retreiveValue(String holder) throws CDLException{
        int temp1;
        if(tempStack.pop().equals("2"))
            temp1 = Integer.parseInt(holder);
        else if(tempStack.pop().equals("1"))
            temp1 = (Integer) symbolList.get(Integer.parseInt(holder)).get(1);
        else
            throw new CDLException("Error");

        return temp1;
    }
}
