package org.example;

import java.io.File;

public class Main {

    static File startPage;

    public static File getPath(String s) //get root directory from argument
    {
        File file = new File(s);

        if(!file.exists()) {
            System.err.println("Error: Given path does not exist");
            System.exit(1);
        }

        return file;
    }



    public static void main(String[] args)
    {
        if(args.length != 1){
            System.err.println("Error: Provide directory path as an argument");
            System.exit(1);
        }
        File currentDir = getPath(args[0]);

        startPage = currentDir;
        new Directory(currentDir,0);

        System.out.println();
    }
}