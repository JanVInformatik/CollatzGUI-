import java.util.Scanner;

public class collatz {
  public static void main(String[] args) {

    long schritte = 0;

    System.out.println("Enter a number:");
    Scanner sc = new Scanner(System.in);

    long n = sc.nextLong();
    long x = n;

    while (n > 1) {
      if (n % 2 == 0)
        n /= 2;
      else
        n = (n * 3) + 1;
      schritte++;
      System.out.println(n);

      if ((Math.log(n) / Math.log(2)) % 1 == 0) {
        System.out.println("N ist eine Zweierpotenz.");
      }
    }

    System.out.println(
        "Man braucht  \'" + schritte
            + "\' Schritte, um  \'1\' zu erreichen. Ein Algorithmus fuer das Collatz Problem wurde genutzt. Input:   \'"
            + x
            + "\'.");
    sc.close();
  }
}
