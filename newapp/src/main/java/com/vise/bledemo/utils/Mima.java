package com.vise.bledemo.utils;



import java.util.Random;

public class Mima {
    public static String[] password = {"ac","t","ds","ds","0cs","ns","t","dmn","xs","0fds","fds"
            ,"tas3","12s","d3s","0cs","as","t","p","js","kj","ar","u","dis","0p","02s","25s"};
    public static Random random =new Random();

    public static String createMima(){
        return password[random.nextInt(password.length-1)];
    }
}
