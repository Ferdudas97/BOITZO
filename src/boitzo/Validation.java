package boitzo;

import java.util.Scanner;

public class Validation {
    static Double getDouble(){
        Double result=2.0;
        Scanner scanner=new Scanner(System.in);
        String answer;
        Boolean valid=false;
        try {


            result = scanner.nextDouble();
        }catch (Exception e) {
            while (!valid) {
                answer = scanner.next();
                if (answer.matches("[0-9]+/[1-9]+")) {
                    String first = answer.substring(0, answer.indexOf('/'));
                    String second = answer.substring(answer.indexOf('/') + 1, answer.length());
                    System.out.println(first + "     " + second);
                    result = Double.valueOf(first) / Double.valueOf(second);
                    valid = true;
                    break;
                }
                if (answer.matches("[0-9]+.[0-9]+")) {

                    result = Double.valueOf(answer);
                    valid = true;

                }


            }
        }
        return result;
    }
    public static int getInteger(){
        int result=0;
        String answer;
        Boolean valid=false;
        Scanner scanner=new Scanner(System.in);

        try {


            result = scanner.nextInt();
        }catch (Exception e) {
            while (!valid){
                answer = scanner.next();
                if (answer.matches("[0-9]+")){
                    result=Integer.valueOf(answer);
                    valid=true;
                }

            }
        }
        return result;

    }
    public static void main(String [] arg){
        System.out.println(getDouble());
        System.out.println(getInteger());
        }
}
