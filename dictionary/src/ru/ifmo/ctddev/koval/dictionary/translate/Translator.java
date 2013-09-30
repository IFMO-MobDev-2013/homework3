package ru.ifmo.ctddev.koval.dictionary.translate;

/**
 * Interface for translating words from English to Russian.
 *
 * @author ndkoval
 */
public interface Translator {

    /**
     * Translate word from english to russian.
     *
     * @param word word to be translated
     * @return translating word in english
     */
    public String translate(String word);

}
