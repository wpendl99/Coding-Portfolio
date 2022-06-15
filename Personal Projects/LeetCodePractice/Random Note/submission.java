import java.util.HashMap;

class Solution {
    public boolean canConstruct(String ransomNote, String magazine) {
        if (ransomNote.length() > magazine.length()) {
            return false;
        }

        // Generate a HashMap of every char in magazine with the # of occurances as the
        // value of it
        HashMap<Character, Integer> magMap = getMapFromString(magazine);

        // Loop through every letting inside of the ransom note,
        // if there are not enough letters to make of the ransom note then return false
        for (char ch : ransomNote.toCharArray()) {
            Integer value = magMap.get(ch);

            if (value == null || value == 0) {
                return false;
            }
            magMap.put(ch, value - 1);
        }

        return true;
    }

    // Function to convert a string into a HashMap with a char as the key and the #
    // of char occurances as values
    public HashMap<Character, Integer> getMapFromString(String string) {
        HashMap<Character, Integer> map = new HashMap<Character, Integer>();

        for (char ch : string.toCharArray()) {
            Integer value = map.get(ch);
            if (value != null) {
                map.put(ch, value + 1);
            } else {
                map.put(ch, 1);
            }
        }

        return map;
    }
}