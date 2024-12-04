import java.util.Scanner;

public class collatz {
        public static void main(String[] args) {
                
                int steps = 0;
                
                System.out.println("Enter a number:");
                Scanner scan = new Scanner(System.in);
                
                int n = scan.nextInt();
                int x = n;
                
                while(n > 1){
                        if(n % 2 == 0)
                                n /= 2;
                        else n = (n * 3) + 1;
                        steps++;    
                        System.out.println(n);
                        
                        if ((Math.log(n) / Math.log(2)) % 1 == 0) {
                            System.out.println("N ist eine Zweierpotenz.");
                        }
                }
                
                System.out.println("It takes \'" + steps + "\' steps to reach \'1\' using the Collatz conjecture on the number \'" + x +"\'.");
        }
  kssdsjdnsjdknsjkdnsdjnsjdndjkn; 

}