package spell;

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {
    private Trie trie;

    public SpellCorrector() {
        setTrie(new Trie());
    }

    private void setTrie(Trie trie){
        this.trie = trie;
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        try {
            File file = new File(dictionaryFileName);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()) {
                String str = scanner.next();
                trie.add(str);
                //System.out.println(str);
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: That file does not exist");
            System.exit(-1);
        }

        /*
        System.out.println("Word Count = " + trie.getWordCount());
        System.out.println("Node Count = " + trie.getNodeCount());
        System.out.println(trie.find("hello"));
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(trie.toString());
         */

    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();

        if(trie.find(inputWord) != null && trie.find(inputWord).getValue() > 0){
            return inputWord;
        }

        ArrayList<String> list = new ArrayList<>();
        getEdits(inputWord, list);
        ArrayList<String> suggestions = new ArrayList<>();


        getSuggestions(list, suggestions);

        if(suggestions.size() == 0){
            ArrayList<String> newList = new ArrayList<>();

            for(int i = 0; i < list.size(); i++){
                String temp = list.get(i);
                getEdits(temp, newList);
            }
            
            list = newList;

            getSuggestions(list, suggestions);
        }
        if(suggestions.size() == 0){
            return null;
        }
        if(suggestions.size() == 1){
            return suggestions.get(0);
        }

        ArrayList<String> top = new ArrayList<>();
        int topNum = 0;
        for(String word: suggestions){
            if(trie.find(word).getValue() > topNum){
                topNum = trie.find(word).getValue();
                top.clear();
                top.add(word);
            } else if(trie.find(word).getValue() == topNum){
                top.add(word);
            }
        }
        Collections.sort(top);
        return top.get(0);
    }

    private void getEdits(String word, ArrayList<String> list){
        list.addAll(getDeletionList(word));
        list.addAll(getTranspositionList(word));
        list.addAll(getAlterationList(word));
        list.addAll(getInsertionList(word));
    }

    private void getSuggestions(ArrayList<String> list, ArrayList<String> suggestions){
        for (String temp : list) {
            INode tempNode = trie.find(temp);
            if (tempNode != null && tempNode.getValue() > 0) {
                if (!suggestions.contains(temp)) {
                    suggestions.add(temp);
                }
            }
        }
    }

    private ArrayList<String> getDeletionList(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length(); i++){
            list.add(word.substring(0, i) + word.substring((i + 1)));
        }

        return list;
    }

    private ArrayList<String> getTranspositionList(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length() - 1; i++){
            list.add(word.substring(0, i) + word.charAt(i+1) + word.charAt(i)  + word.substring((i+2)));
        }

        return list;
    }

    private ArrayList<String> getAlterationList(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length(); i++){
            for(int ii = 0; ii < 26; ii++){
                if(word.charAt(i) != Node.getLetter(ii)) {
                    list.add(word.substring(0, i) + Node.getLetter(ii) + word.substring((i + 1)));
                }
            }
        }

        return list;
    }

    private ArrayList<String> getInsertionList(String word){
        ArrayList<String> list = new ArrayList<String>();
        for(int i = 0; i < word.length() + 1; i++){
            for(int ii = 0; ii < 26; ii++){
                list.add(word.substring(0, i) + Node.getLetter(ii) + word.substring((i)));
            }
        }

        return list;
    }
}
