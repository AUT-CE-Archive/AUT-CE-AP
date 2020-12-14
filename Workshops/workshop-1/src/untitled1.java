import java.util.Scanner;

public class untitled1 {

    private static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        int num1 = scanner.nextInt();
        int num2 = scanner.nextInt();

        if (num1 % num2 == 0 || num2 % num1 == 0)
            System.out.format("%d & %d are prime in relation.", num1, num2);
        else
            System.out.format("%d & %d are NOT prime in relation.", num1, num2);
    }
}
