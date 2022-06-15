class Solution:
    def romanToInt(self, s: str) -> int:

        # Hashmap of Roman Numerals to Numberic Values
        map = {"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}

        sum = 0

        # Because Roman Numerals go from biggest to smallest, if the Roman Numeral is followed by one of a larger value, then it is supposed to have a negative value
        for i in range(len(s)):
            if i + 1 < len(s) and map[s[i]] < map[s[i]]:
                sum -= map[s[i]]
            else:
                sum += map[s[i]]

        return sum
