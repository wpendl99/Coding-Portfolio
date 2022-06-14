"""
Author: William Pendleton

This project is used to calculate how long it would take to fill a given amount of cages with bunnies with the bunnies
producing 1 bunny for every pair every month and having a set limit of bunnies per cage.

In this project I learned how to work with txt files (opening, writing and closing) and using them to store variables.
"""

def main():
    """Main function for project"""
    txt = open("rabbits.csv", "w+")  # writes to file bunnies.csv or creates it if not there

    pairs = 1  # The data for project
    months = 1
    babies = 0

    txt.write("# Table of rabbit pairs\n" + "Months, Adults, Babies, Total\n")  # writes header to file

    while True:
        total = pairs + babies  # calculates how many bunnies there are in total
        print("Months", months, "Adults", pairs, "Babies", babies, "Total", total)  # only for debugging reason to help user and grader
        txt.write(str(months) + "," + str(pairs) + "," + str(babies) + "," + str(total) + "\n")  # Writes data to file
        babies = pairs  # makes babies
        pairs = total  # turns babies into adults
        if total >= 500:  # tests if out of cages
            break
        months += 1  # adds a month

    txt.write("# Cages will run out in month " + str(months) + "\n")  # writes when we'd run out of cages
    txt.close()  # closes file

if __name__ == '__main__':
    main()