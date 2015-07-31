package com.xavigil.uniquewords.model;

import java.util.Comparator;

public class UniqueWord {

    public String word;
    public int count;

    public UniqueWord(String word, int count){
        this.word = word;
        this.count = count;
    }

    public static Comparator<UniqueWord> getWordComparator() {
        return new Comparator<UniqueWord>() {
            @Override
            public int compare(UniqueWord uw1, UniqueWord uw2) {
                return uw1.word.compareTo(uw2.word);
            }
        };
    }

    public static Comparator<UniqueWord> getCountComparator() {
        return new Comparator<UniqueWord>() {
            @Override
            public int compare(UniqueWord uw1, UniqueWord uw2) {
                return uw2.count - uw1.count;
            }
        };
    }
}
