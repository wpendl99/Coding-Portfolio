package hangman;

import hangman.EmptyDictionaryException;
import hangman.GuessAlreadyMadeException;
import hangman.IEvilHangmanGame;
import org.junit.platform.commons.util.StringUtils;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.lang.String;

public class EvilHangmanGame implements IEvilHangmanGame {
    private List<Character> usedLetters;
    private SortedSet<Character> guessedLetters;
    private List<String> wordList;
    private HashMap<Integer, List<String>> map;
    private Integer selectedKey;
    private Integer wordLength;

    public List<Character> getUsedLetters() {
        return this.usedLetters;
    }

    public void setUsedLetters(List<Character> usedLetter) {
        this.usedLetters = usedLetter;
    }

    public void addUsedLetter(Character letter) {
        this.usedLetters.add(letter);
    }

    public void popUsedLetter(Character chr){
        this.usedLetters.remove(chr);
    }

    public void setGuessedLetters(SortedSet<Character> usedLetter) {
        this.guessedLetters = usedLetter;
    }

    public void addGuessedLetters(Character letter) {
        this.guessedLetters.add(letter);
    }

    public List<String> getWordList() {
        return wordList;
    }

    public void setWordList(List<String> wordList) {
        this.wordList = wordList;
    }

    public void addWordList(String word){
        this.wordList.add(word);
    }

    public HashMap<Integer, List<String>> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, List<String>> map) {
        this.map = map;
    }

    public void addMapPair(int key, String word){
        if(!this.map.containsKey(key)){
            this.map.put(key, new ArrayList<>());
        }
        this.map.get(key).add(word);
    }

    public List<String> getLargestSet(){
        int largesValue = -1;
        int largestKey = -1;
        for(int key: this.map.keySet()){
            if(map.get(key).size() > largesValue){
                largestKey = key;
                largesValue = map.get(key).size();
            } else if ((map.get(key).size() == largesValue) & !(largestKey == 0)){
                String tmpStr1 = this.getDecryptedKey(largestKey);
                int tmpCount1 = 0;
                String tmpStr2 = this.getDecryptedKey(key);
                int tmpCount2 = 0;

                for(int i = 0; i < this.getWordLength(); i++){
                    //System.out.println(tmpStr1.charAt(i));
                    if(tmpStr1.charAt(i) == this.getUsedLetters().get(this.getUsedLetters().size()-1)){
                        tmpCount1 ++;
                    }
                }

                for(int i = 0; i < this.getWordLength(); i++){
                    if(tmpStr2.charAt(i) == this.getUsedLetters().get(this.getUsedLetters().size()-1)){
                        tmpCount2 ++;
                    }
                }

                if(tmpCount2 < tmpCount1 || ((tmpCount2 == tmpCount1) & (key > largestKey))) {
                    largestKey = key;
                    largesValue = map.get(key).size();
                }
            }
        }
        this.setSelectedKey(largestKey);
        return map.get(largestKey);
    }

    public Integer getSelectedKey() {
        return this.selectedKey;
    }

    public void setSelectedKey(Integer selectedKey) {
        this.selectedKey = selectedKey;
    }

    public Integer getWordLength() {
        return this.wordLength;
    }

    public void setWordLength(Integer wordLength) {
        this.wordLength = wordLength;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        this.setUsedLetters(new ArrayList<>());
        this.setGuessedLetters(new TreeSet<>());
        this.setWordList(new ArrayList<>());
        this.setMap(new HashMap<>());
        this.setSelectedKey(selectedKey = 0);

        this.setWordLength(wordLength);


        try{
            Scanner scanner = new Scanner(dictionary);
            if(!scanner.hasNext()){
                throw new EmptyDictionaryException();
            }
            String tmp;
            while(scanner.hasNext()) {
                tmp = scanner.next();
                if (tmp.length() == this.getWordLength()) {
                    this.addWordList(tmp);
                }
            }

            if(this.getWordList().size() == 0){
                throw new EmptyDictionaryException();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        this.setMap(new HashMap<>());
        int tempKey = this.getSelectedKey();
        this.setSelectedKey(0);

        if(this.getGuessedLetters().contains(guess)){
            throw new GuessAlreadyMadeException();
        }

        this.addUsedLetter(guess);
        this.addGuessedLetters(guess);

        for(String word: this.getWordList()){
            this.addMapPair(this.getEncryptionKey(word), word);
        }

        this.setWordList(this.getLargestSet());

        if(!this.getDecryptedKey().contains(String.valueOf(guess))){
            this.popUsedLetter(guess);
            this.setSelectedKey(tempKey);
        }

        return new HashSet<>(this.getWordList());
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return this.guessedLetters;
    }

    public String getDecryptedKey() {
        String key = this.leftPad(this.baseConversion(this.selectedKey));
        StringBuilder str = new StringBuilder();

        int tmp;
        for(int i = 0; i < key.length(); i++){
            tmp = Integer.parseInt(String.valueOf(key.charAt(i))) - 1;

            if(tmp == -1){
                str.insert(0, '-');
            } else {
                str.insert(0, this.usedLetters.get(tmp));
            }
        }

        return str.toString();
    }

    public String getDecryptedKey(int inKey) {
        String key = this.leftPad(this.baseConversion(inKey));
        StringBuilder str = new StringBuilder();

        int tmp;
        for(int i = 0; i < key.length(); i++){
            tmp = Integer.parseInt(String.valueOf(key.charAt(i))) - 1;

            if(tmp == -1){
                str.insert(0, '-');
            } else {
                str.insert(0, this.usedLetters.get(tmp));
            }
        }

        return str.toString();
    }

    private String baseConversion(Integer key) {
        if(key == 0){
            return "0";
        }
        return Integer.toString(Integer.parseInt(String.valueOf(key), 10), this.getUsedLetters().size() + 1);
    }

    private int getEncryptionKey(String word) {
        int key = 0;
        for(int i = 0; i < this.getWordLength(); i++){
            for(int ii = 0; ii < this.getUsedLetters().size(); ii++){
                if( word.charAt(i) == this.getUsedLetters().get(ii)){
                    key += (ii + 1) * Math.pow(this.getUsedLetters().size() + 1, i);
                }
            }
        }

        return key;
    }

    private String leftPad(String str) {
        if (str.length() >= this.getWordLength()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();

        while(sb.length() < this.getWordLength() - str.length()){
            sb.append("0");
        }

        sb.append(str);

        return sb.toString();
    }
}
