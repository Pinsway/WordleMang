package com.eesti.wordle2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sõnad {
    private List<String> sõnad;
    private Random rand;

    public Sõnad(String failiNimi) throws Exception {
        sõnad = Files.readAllLines(Paths.get(failiNimi));
        sõnad.replaceAll(String::toUpperCase);
        rand = new Random();
    }

    public String laeSuvalineSõna() {
        return sõnad.get(rand.nextInt(sõnad.size()));
    }

    public boolean onKehtivSõna(String sõna) {
        return sõnad.contains(sõna);
    }
}
