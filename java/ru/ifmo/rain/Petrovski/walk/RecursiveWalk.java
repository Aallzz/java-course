package ru.ifmo.rain.Petrovski.walk;

public class RecursiveWalk {
    public static void main(String[] args) {
        try {
            if (args == null) {
                System.out.println("Error: Argument is null");
                return ;
            }
            if (args.length != 2) {
                System.out.println("Error: Expected two file names");
                return ;
            }
            RecursiveWalker walker = new RecursiveWalker(args[0], args[1]);
            walker.walk();
        } catch (RecursiveWalkerException e) {
            System.out.println(e.getMessage());
        }
    }
}



