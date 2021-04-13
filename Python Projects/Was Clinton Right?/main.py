"""
Author: William Pendleton

This project is used to fulfill requirements for a multi part project around discovering how random random really is.

In this project I learned about how to make a program do more than just 1 think but to fulfil multiple requirements
given input.
"""
from time import sleep
import matplotlib.pyplot as plt

BLSData = []
PresidentsData = []


def openBLS():
    """Opens BLS_corrected.csv file and sticks data into BLSData list"""
    global BLSData

    f = open('BLS_corrected.csv', 'r')

    for i in range(5):
        f.readline()

    temp = ""
    for line in f:
        BLSData.append(line[:-1].split(','))
        temp = line

    BLSData[-1][-1] = BLSData[-1][-1] + temp[-1:]


def openPresidents():
    """Opens Presidents.txt and appends data to PresidentsData list"""
    global PresidentsData

    f = open("Presidents.txt", "r")

    for line in f:
        PresidentsData.append(line[:-1].split(','))
        if line[-1:] != "\n":
            PresidentsData[-1] = line.split(',')


def printGraph():
    """Uses matplotlib to display results of computation"""
    cord, height, tickLabel, colour = getGraphInfo()

    plt.figure(figsize=(18, 8))

    # plotting a bar chart
    plt.bar(cord, height, tick_label=tickLabel,
            width=0.8,
            color=colour,
            edgecolor="black",
            linewidth=.5)

    # naming the x-axis
    plt.xlabel('Years and Presidents')
    # naming the y-axis
    plt.ylabel('Job Difference per Year')
    # plot title
    plt.title('Total Private Job Year-End Difference (Seasonally Adjusted) 1961 - 2012')

    fig = plt.gcf()
    ax = plt.gca()

    ax.tick_params(labelsize=8)

    ax.bar(0, 0, 0, label='Republican', color="red")
    ax.bar(0, 0, 0, label='Democratic', color="blue")

    ax.legend()

    fig.autofmt_xdate()
    fig.tight_layout()
    plt.rcParams.update({'font.size': 2})

    # function to show the plot
    plt.show()


def getGraphInfo():
    """Using Global BLSData and PresidentsData to return data labels for matplotlib"""
    global BLSData
    global PresidentsData

    cord = []
    height = []
    tickLabel = []
    colours = []
    tempName = ""
    tempNum = 0

    for i in range(1, len(BLSData)):
        cord.append(i)
        height.append(int(BLSData[i][12]) - tempNum)
        if i == 1:
            height[0] = 0
        tempNum = int(BLSData[i][12])
        if tempName == PresidentsData[i][1]:
            tickLabel.append(str(PresidentsData[i][0]))
        else:
            tickLabel.append(str(PresidentsData[i][1] + " - " + PresidentsData[i][0]))
            tempName = PresidentsData[i][1]
        colours.append(getColour(PresidentsData[i][2]))

    return cord, height, tickLabel, colours


def getColour(party):
    """Returns Party's corresponding color"""
    if party == "Democratic":
        return "blue"
    else:
        return "red"


def mainScript():
    """Displays the whole show on the commandline."""
    print("\033[1m\033[36m      ---------- Democratic Party Job Moto ----------")
    print("  We are committed to doing everything we can to build a \nfull-employment economy, where everyone has a job that pays\nenough to raise a family and live in dignity with a sense of\n                         purpose.\033[0m\n")
    waitDots(5, 1)
    print("\033[0mPresident Nixon once said:")
    wait(1)
    print("\033[33m'\033[34mSince 1961, for 52 years now, the Republicans have held the White House 28 years, the Democrats 24.")
    wait(1.5)
    print("In those 52 years, our private economy has produced 66 million private-sector jobs.")
    wait(1.5)
    print("So what's the jobs score? Republicans 24 million, Democrats 42 (million).\033[33m'\033[0m")
    wait(2)
    input("Do you think that he was telling the truth? \033[31m(Rhetorical, press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    wait(1)
    print("When it comes to finding out if he telling the truth,")
    wait(2)
    print("it is important to break it into segments:")
    wait(2)
    print("\033[31m1\033[0m) Was it really \033[33m52 years\033[0m since 1961?")
    wait(2)
    print("\033[31m2\033[0m) During those supposed 52 years were \033[33mDemocrats\033[0m in office for 24 years and \033[33mRepublicans\033[0m 28?")
    wait(3)
    print("\033[31m3\033[0m) Were \033[33m66 million jobs\033[0m made in those 52 years??")
    wait(2)
    input("\033[31m4\033[0m) Is the job scores correct? \033[33m(Republicans 24 million, Democrats 42 million)\033[0m \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    wait(1)
    print("In trying to prove if a politician is correct it is important to THINK like a politician")
    wait(1.5)
    print("(That means we might do a little bit of rounding.....)")
    wait(1)
    print("\033[31m1\033[0m) This one is easy because it's simple math,")
    wait(2)
    print("If the speech was given at the end of \033[33m2012\033[0m")
    wait(2)
    print("\033[34m2012 \033[33m- \033[34m1961 \033[33m= \033[35m51", end="")
    wait(2)
    print("  \033[0mround up 1 = 52  ")
    wait(1)
    print("\033[33m(\033[32mCORRECT\033[33m)")
    wait(2)
    print("\033[31m2\033[0m) This one is a bit longer at proving, but all you do is look at each party per year and add it up: ")
    wait(2)
    print("\033[31m(warning big text spam coming up)")
    wait(1.5)
    getPresList()
    wait(2)
    print("\033[33m(\033[32mCORRECT\033[33m)", end="")
    wait(1)
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("\033[31m3\033[0m) This one is easy, just look at how many jobs there were in \033[33m2012\033[0m subtracted by the amount in \033[33m1961\033[0m: ")
    wait(2)
    print("(Dec 2012) \033[34m113.201 \033[0mmil \033[33m- \033[0m(Dec 1961) \033[34m46.036 \033[0mmil \033[33m~= \033[35m67 mil", end="")
    wait(2)
    print("  \033[0mround down 1 = 66 mil  ")
    wait(1)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m")
    wait(2)
    print("In all honesty, up to this point has been simple math, but this next part gets a little trickier...")
    wait(2)
    print("\033[31m4\033[0m) To solve this one we have to take the year end difference of all of every years and then add them up into two categories, \033[31mRepublican\033[0m and \033[34mDemocratic\033[0m...: ")
    wait(2)
    print("\033[31m(again, warning about big text spam coming up)\033[0m")
    getYearEnd()
    wait(2)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m", end="")
    wait(1)
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("Let's review....")
    wait(1.5)
    print("\033[31m1\033[0m) Was it really \033[33m52 years\033[0m since 1961? ", end="")
    wait(2)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m")
    wait(1)
    print("\033[31m2\033[0m) During those supposed 52 years were \033[33mDemocrats\033[0m in office for 24 years and \033[33mRepublicans\033[0m 28? ", end="")
    wait(3)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m")
    wait(1)
    print("\033[31m3\033[0m) Were \033[33m66 million jobs\033[0m made in those 52 years?? ", end="")
    wait(2)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m")
    wait(1)
    print("\033[31m4\033[0m) Is the job scores correct? \033[33m(Republicans 24 million, Democrats 42 million)\033[0m ", end="")
    wait(2)
    print("\033[33m(\033[32mCORRECT\033[33m)\033[0m")
    wait(3)
    print(".\nIn Conclusion, I think that Bill Clinton was telling the truth.")
    wait(2)
    print("I mean, sure he did have to round a few numbers, but really who doesn't fib the truth a little just to get their point across.")
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("However", end="")
    waitDots(5, 1, .5)
    print("\033[0mMark Twain once said:")
    wait(1)
    print(
        "\033[33m'\033[34mThere are three kinds of lies:")
    wait(1.5)
    print("Lies,")
    wait(1.5)
    print("Damning Lies,")
    wait(1.5)
    print("and Statistics.\033[33m'\033[0m", end="")
    waitDots(4, 1, .5)
    input("\033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("In my very professional opinion,")
    wait(1)
    print("I think that although Bill Clinton was telling the truth,", end="")
    waitDots(4, 1, .5)
    print("He was still telling a Statistical Lie.", end="")
    wait(1.5)
    print(" Let Me Explain.", end="")
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("If we were to plot all of the data that we found today on a graph and put it into context,")
    wait(2)
    print("What do you think it would look like?", end="")
    wait(1)
    input("\033[31m(Rhetorical, press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("I'll tell you what it would look like,", end="")
    waitDots(4, 1, wait=.75)
    print("All of the Democrats would have a positive influence on the job count, where as the Republicans would have an unstable impact")
    wait(3)
    print("This is true for all of the data, EXCEPT for ONE person.")
    wait(3)
    print("That person is Barack Obama", end="")
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(4, 1, wait=.5)
    print("You see, Clinton was giving this speech to try and convince the people to vote for the Democratic Candidate Barack Obama")
    wait(3)
    print("But what most people don't know is that The first year Obama was Elected President, he was the only Democratic President to have EVER taken away from the job count,")
    wait(4)
    print("I think that this is misleading to the great citizens of America to ask them to put their trust in a democrat who is supposed to add jobs, but doesn't")
    wait(4)
    print("That constitutes as a Statistical lie because it's misleading people with statistics.")
    wait(3)
    print("Even is the stats are true, it's still lying")
    input(" \033[31m(press enter to continue)\033[0m")
    waitDots(2, 2, wait=.5)
    print("I would now like to finish off by showing to you a graph with all of the data that we have calculated together today,")
    wait(3)
    print("And even though we have to part ways, I hope that you know that I will always remember you and the times that we've had")
    wait(2)
    input("\033[31m(press enter to continue to the end)\033[0m")
    waitDots(2, 2, wait=.5)



def getYearEnd():
    """Using total graph data, to display results from calculations."""
    global BLSData
    global PresidentsData

    rTot = 0
    dTot = 0

    for i in range(1, len(PresidentsData) - 1):
        print("(\033[33mDec", PresidentsData[i + 1][0] + "\033[0m)\033[36m", BLSData[i + 1][12] + "000 \033[33m- \033[0m(\033[33mDec", PresidentsData[i][0] + "\033[0m)\033[36m", BLSData[i][12] + "000 \033[33m=", end="")
        dif = int(BLSData[i + 1][12]) * 1000 - int(BLSData[i][12]) * 1000
        if PresidentsData[i + 1][2] == "Democratic":
            print("\033[35m", str(dif), "\033[34m", PresidentsData[i + 1][1], "\033[0m")
            dTot += dif
        elif PresidentsData[i + 1][2] == "Republican":
            print("\033[35m", dif, "\033[31m", PresidentsData[i + 1][1], "\033[0m")
            rTot += dif

    print("------------------")
    wait(1)
    print("Total: \033[31mRepublicans\033[0m= \033[33m", str(rTot)[:2] + "." + str(rTot)[2:-3], "\033[0mmil \033[34mDemocratics\033[0m= \033[33m", str(dTot)[:2] + "." + str(dTot)[2:-3], "\033[0mmil")



def getPresList():
    """prints list of presidents in their respected parties"""
    global PresidentsData
    rCount = 0
    dCount = 0

    print("\033[31m", PresidentsData[0][0][:-1], "\033[0m:\033[31m", PresidentsData[0][1][:-1], "\033[0m:\033[31m ", PresidentsData[0][2][:-1], "\033[0m:")
    for i in range(1, len(PresidentsData)):
        print("\033[33m", PresidentsData[i][0], "\033[0m, \033[35m", PresidentsData[i][1], "\033[0m, ", end="")
        if PresidentsData[i][2] == "Democratic":
            print("\033[34mDemocratic\033[0m")
            dCount += 1
        elif PresidentsData[i][2] == "Republican":
            print("\033[31mRepublican\033[0m")
            rCount += 1
        else:
            print("\033[32mERROR\033[0m")

    print("------------------")
    wait(1)
    print("Total: \033[31mRepublicans\033[0m= \033[33m", rCount, "\033[34mDemocratics\033[0m= \033[33m", dCount)


def wait(x):
        sleep(x)


def waitDots(x, type, wait=1):
    """Prints dots on the screen at a slower pace"""
    if type == 1:
        for i in range(x):
            print(".", end="")
            sleep(wait)
        print(".")
    else:
        for i in range(x):
            print(".")
            sleep(wait)
        print(".")



def main():
    """main function for the program"""
    openBLS()
    openPresidents()
    mainScript()
    printGraph()

    print("All of the Data Used Courtesy of BLS_corrected.csv and Presidents.txt:")
    print(BLSData)
    print(PresidentsData)


if __name__ == '__main__':
    main()