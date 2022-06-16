import java.util.Arrays;
import java.util.HashMap;
import java.util.*;

// To solve this I use a HashMap to create a chain of all of the words that are connected. 
// The Key is the word and the value is the distance from the furtherest node
class Solution {
    public int longestStrChain(String[] words) {
        Arrays.sort(words, (a, b) -> a.length() - b.length());
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (String word : words) {
            map.put(word, 1);
            // Loop through every character, delete it, and see if that new word exists in
            // given words, if so, record chain distance value
            for (int i = 0; i < word.length(); i++) {

                StringBuilder builder = new StringBuilder(word);
                String testWord = builder.deleteCharAt(i).toString();

                if (map.containsKey(testWord)) {
                    map.put(word, Math.max(map.get(word), map.get(testWord) + 1));
                }
            }
        }

        return Collections.max(map.values());
    }
}