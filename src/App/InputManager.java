package App;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;

public class InputManager
{
    private static Scanner _scanner = new Scanner(System.in);

//    protected int getSeqLen() {
//        var len = getIntPrompt("Ile liczb ma sie znalezc w ciagu? (1-20)", Optional.of(x -> x > 0 && x <= 20));
//
//        return len;
//    }

//    protected int[] generateSequence() {
//        var sequenceLen = getSeqLen();
//
//        return generateSequence(sequenceLen);
//    }
//
//    protected int[] generateSequence(int sequenceLen) {
//        var min = getIntPrompt("Podaj minimalną wartość w generowanej sekwencji");
//        var max = getIntPrompt("Podaj maksymalną wartość w generowanej sekwencji (nie mniejsza niż " + min + ")", Optional.of(x -> x >= min));
//        System.out.println();
//
//        return generateSequence(sequenceLen, min, max);
//    }

//    protected int[] generateSequence(int sequenceLen, int min, int max){
//        var random = new Random();
//        var sequence = new int[sequenceLen];
//
//        System.out.println("Wygenerowane liczby to:");
//        for (int i = 0; i < sequenceLen; i++)
//        {
//            sequence[i] = random.nextInt(min, max+1);
//        }
//        showSequence(sequence);
//        System.out.println();
//
//        return sequence;
//    }
//
//    protected void showSequence(int[] sequence) {
//        for (var value : sequence)
//            System.out.print(value + " ");
//
//        System.out.println();
//    }
//
//    protected int[] getSequence() {
//
//        return getSequence(Optional.ofNullable(null));
//    }

//    protected int[] getSequence(Optional<Predicate<Integer>> predicate) {
//        var sequenceLen = getSeqLen();
//
//        return getSequence(sequenceLen, predicate);
//    }
//
//    protected int[] getSequence(int sequenceLen)
//    {
//        return getSequence(sequenceLen, Optional.ofNullable(null));
//    }

//    protected int[] getSequence(int sequenceLen, Optional<Predicate<Integer>> predicate)
//    {
//        var sequence = new int[sequenceLen];
//        for (int i = 0; i < sequenceLen; i++)
//            sequence[i] = getIntPrompt("Podaj " + (i + 1) + " liczbe z sekwencji", predicate);
//
//        return sequence;
//    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.print("Wciśnij enter aby kontynuować...");
        try
        {
            System.in.read();
            _scanner.nextLine();
        }
        catch(Exception e)
        {}
    }

    public static void clearConsole() {
        try
        {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows"))
            {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            e.printStackTrace();
        }
    }

    public static int getIntPrompt(String prompt) {
        return getIntPrompt(prompt, Optional.ofNullable(null));
    }

    public static int getIntPrompt(String prompt, Optional<Predicate<Integer>> predicate){
        Double result = null;

        do {
            if (result != null)
                System.out.println("Podaj prosze liczbe calkowita.");

            result = getDoublePrompt(prompt);

            if (result % 1 != 0)
                continue;

            if (!predicate.isPresent())
                break;

            if(predicate.get().test((int)(double)result))
                return (int)(double)result;
            else
                System.out.println("Podana wartość nie jest poprawna w obecnym kontekście.");

            result = null;
        } while (true);

        return (int)(double)result;
    }

    public static double getDoublePrompt(String prompt){
        return getDoublePrompt(prompt, Optional.ofNullable(null));
    }

    public static double getDoublePrompt(String prompt, Optional<Predicate<Double>> predicate){
        do {
            try
            {
                System.out.print(prompt + ": ");
                double response = _scanner.nextDouble();
                _scanner.nextLine();

                if (!predicate.isPresent())
                    return response;

                if (predicate.get().test(response))
                    return response;

                System.out.println("Podana wartość nie jest poprawna w obecnym kontekście.");
            }
            catch (InputMismatchException e)
            {
                System.out.println("Podany ciag znakow nie zostal rozpoznany jako liczba!");

                if (_scanner.hasNext())
                    _scanner.nextLine();
            }
        } while (true);
    }

    public static boolean getBooleanPrompt(String prompt){
        String response = null;
        do {
            if (response != null)
                System.out.println("Udzielona odpowiedz nie jest prawidlowa.");

            response =  getStringPrompt(prompt + "? (T/N)");
        } while (!response.equals("T") && !response.equals("N"));

        return response.equals("T");
    }

    public static String getStringPrompt(String prompt) {
        return getStringPrompt(prompt, Optional.ofNullable(null));
    }

    public static String getStringPrompt(String prompt, Optional<Predicate<String>> predicate) {
        do
        {
            System.out.print(prompt + ": ");
            var response = _scanner.nextLine();

            if (!predicate.isPresent())
                return response;

            if (predicate.get().test(response))
                return response;

            System.out.println("Podany ciąg znaków jest niepoprawny w obecnym kontekście.");
        } while (true);
    }
}
