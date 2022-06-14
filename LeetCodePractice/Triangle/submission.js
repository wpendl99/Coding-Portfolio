/**
 * Uses algorithm Top Down with Dynamic Programming
 * The idea is basically starting from the buttom of the triangle/tree and working your way up seeing which positions make the shortest path up
 * @param {number[][]} triangle
 * @return {number}
 */
var minimumTotal = function(triangle) {
    let arr = triangle[triangle.length-1]; // Set the initial array to the last row of triangle
    
    // Loop through every row
    for(let i = triangle.length - 2; i >= 0; i--){
        let row = arr
        // Loop through every index (j) in current row (i) and see whether (j) or (j+1) of the following row (i+1) are lower
        for(let j = 0; j < i + 1; j++){
            if(row[j] < row[j+1]){
                arr[j] = row[j] + triangle[i][j]
            } else {
                arr[j] = row[j+1] + triangle[i][j]
        }
    }
    return arr[0]
};
