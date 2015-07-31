package com.xavigil.uniquewords.model;

import java.util.Comparator;

public class UniqueWord {

    public String word;
    public int appearances;

    public UniqueWord(String word, int appearances){
        this.word = word;
        this.appearances = appearances;
    }

    static Comparator<UniqueWord> getWordComparator() {
        return new Comparator<UniqueWord>() {
            @Override
            public int compare(UniqueWord uw1, UniqueWord uw2) {
                return uw1.word.compareTo(uw2.word);
            }
        };
    }

    static Comparator<UniqueWord> getAppearancesComparator() {
        return new Comparator<UniqueWord>() {
            @Override
            public int compare(UniqueWord uw1, UniqueWord uw2) {
                return uw2.appearances - uw1.appearances;
            }
        };
    }
}
