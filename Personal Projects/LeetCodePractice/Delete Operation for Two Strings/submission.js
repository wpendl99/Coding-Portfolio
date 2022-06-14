/**
 * Uses algorithm LCR (Longest Common Subsequence)
 * @param {string} word1
 * @param {string} word2
 * @return {number}
 */
var minDistance = function (word1, word2) {
	// Initalize 2-Dim Array with word1.length as x and word2.length as y
	let arr = new Array(word1.length + 1)
		.fill(0)
		.map(() => new Array(word2.length + 1).fill(0));

	// Loop through each letter and plot the appropriate subsequence value
	for (let i = 0; i < word1.length; i++) {
		for (let j = 0; j < word2.length; j++) {
			if (word1.charAt(i) == word2.charAt(j)) {
				arr[i + 1][j + 1] = arr[i][j] + 1;
			} else {
				arr[i + 1][j + 1] = Math.max(arr[i][j + 1], arr[i + 1][j]);
			}
		}
	}

	// LCR Array print-out
	// for (const line of arr) {
	// 	console.log(line);
	// }

	// Return how many 'excess' characters there are in each string
	return word1.length + word2.length - 2 * arr[word1.length][word2.length];
};
