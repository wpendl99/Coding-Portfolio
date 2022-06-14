"""
Author: William Pendleton

This is a project that takes sample stock data from "stocks_data.csv" and returns calculated min, max and average
for every company in the file.
"""
from math import sqrt

#  List Format: [Max Date, Max Price, Min Date, Min Price, Average (used to add numbers as we go), Average count (used to count numbers added to average)]
aapl_data = [0.000, "Max Date", 0.000, "Min Date", 0.0, 0.0]
ibm_data = [0.000, "Max Date", 0.000, "Min Date", 0.0, 0.0]
msft_data = [0.000, "Max Date", 0.000, "Min Date", 0.0, 0.0]

main_lists = []


def load_data(f):
    """Takes a file name and returns a list of lists made up of the data"""
    data = [[], [], []]  # data = [[(AAPL)[12.9999, 1/2/19],[13.29384, 2/2/19]],[(IBM)[1.9999, 3/2/19],[1.29384, 4/2/19]],[(AAPL)[72.9999, 2/2/19][19.29384, 3/2/19]]]

    f.readline()  # Used to skip the first line in the file
    for line in f:  # Cycles through each line putting it into a list to make it easier to analyze the data
        line_list = line.split(",")

        adj_val = line_list[2]  # Checks for a newline character at the end of the Adj Close
        if line_list[2].find("\n") > -1:
            adj_val = float(line_list[2][:line_list[2].find("\n")])

        if line_list[0] == "AAPL":  # Puts the line of data into it's respective place in the list
            data[0].append([adj_val, line_list[1]])
        elif line_list[0] == "IBM":
            data[1].append([adj_val, line_list[1]])
        elif line_list[0] == "MSFT":
            data[2].append([adj_val, line_list[1]])

    return data


def stock_data(company, full_list):
    """Function used to alter the data in the Company Lists"""
    if company == "AAPL":
        lst = full_list[0]
    elif company == "IBM":
        lst = full_list[1]
    elif company == "MSFT":
        lst = full_list[2]
    else:
        return None

    lst.sort()
    min = lst[0][0]
    min_date = lst[0][1]
    max = lst[-1][0]
    max_date = lst[-1][1]
    ave = calculate_averages(lst)
    dev = get_deviation(lst, ave)

    return [max, max_date, min, min_date, ave, dev]


def calculate_averages(lst):
    """Used after all numbers have been calculated to calculate the average"""
    ave = 0
    for i in range(0, len(lst)):
        ave += float(lst[i][0])
    return ave / len(lst)


def get_deviation(lst, ave):
    """Used to get Standard Deviation for each Stock Company"""
    tot = 0
    for (val, date) in lst:
        tot += (float(val) - ave) ** 2

    return sqrt(tot / (len(lst) - 1))


def write_info():
    """Used to Print data to Console"""
    aapl_data.append("AAPL")
    ibm_data.append("IBM")
    msft_data.append("MSFT")

    stock_file = open("stock_summary.txt", "w+")
    for i in main_lists:
        stock_file.write(i[6] + "\n")
        stock_file.write("----\n")
        stock_file.write("Max: " + str(i[0]) + " " + i[1] + "\n")
        stock_file.write("Min: " + str(i[2]) + " " + i[3] + "\n")
        stock_file.write("Ave: " + str(i[4]) + "\n")
        stock_file.write("Dev: " + str(i[5]) + "\n\n")

    stock_file.write("Highest: " + max_price([aapl_data, ibm_data, msft_data]) + "\n")
    stock_file.write("Lowest: " + min_price([aapl_data, ibm_data, msft_data]))

    stock_file.close()

    for i in main_lists:
        print(i[6])
        print("----")
        print("Max: ", str(i[0]), i[1])
        print("Min: ", str(i[2]), i[3])
        print("Ave: ", str(i[4]))
        print("Dev: ", str(i[5]), "\n")

    print("Highest: " + max_price([aapl_data, ibm_data, msft_data]))
    print("Lowest: " + min_price([aapl_data, ibm_data, msft_data]))


def max_price(dataList):
    """Return max stock of every company given dataList"""
    maxPrice = max(aapl_data[0], ibm_data[0], msft_data[0])
    if maxPrice == aapl_data[0]:
        return "AAPL " + str(aapl_data[0]) + " " + aapl_data[1]
    elif maxPrice == ibm_data[0]:
        return "IBM " + str(ibm_data[0]) + " " + ibm_data[1]
    elif maxPrice == msft_data[0]:
        return "MSFT " + str(msft_data[0]) + " " + msft_data[1]
    else:
        return None


def min_price(dataList):
    """Return min stock of every company given dataList"""
    minPrice = min(aapl_data[2], ibm_data[2], msft_data[2])
    if minPrice == aapl_data[2]:
        return "AAPL " + str(aapl_data[2]) + " " + aapl_data[3]
    elif minPrice == ibm_data[2]:
        return "IBM " + str(ibm_data[2]) + " " + ibm_data[3]
    elif minPrice == msft_data[2]:
        return "MSFT " + str(msft_data[2]) + " " + msft_data[3]
    else:
        return None


def main():
    """Main function for project"""

    #  Generate main_list of all companies
    main_lists = load_data(open("stocks_data.csv"))
    #  Generate Lists for stock data
    aapl_data = stock_data("AAPL", main_lists)
    ibm_data = stock_data("IBM", main_lists)
    msft_data = stock_data("MSFT", main_lists)

    #  Seperate lists again
    main_lists = [aapl_data, ibm_data, msft_data]

    write_info()
