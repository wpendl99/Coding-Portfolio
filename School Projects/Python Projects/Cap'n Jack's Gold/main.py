"""
Author: William Pendleton

A Program to help Cap'n Jack calculate how much Gold each Pirate gets.

In this project I learned about how python does math and how keeping track of variables is important in programming.
"""
import random
import sys


def getPolly(str):
    """Function that adds some colour to Polly string and then returns color back to normal, returns input string but coloured"""
    i = 0  # index tracker
    tog = 2  # color toggler tacker
    while str[i] != ' ':  # Test's for end of last word
        if (tog % 2) == 0:  # If color toggler is even, makes letter Cyan
            str = str[:i] + "\033[96m" + str[i:]
            tog += 1
        else:  # If color toggler is odd, makes letter Yellow
            str = str[:i] + "\033[93m" + str[i:]
            tog -= 1
        i += 6  # index bumper to account for added string (I can not explain why this number work, but it works)
    str = str[:len(str) - 3] + "\033[0m" + str[len(str) - 3:]
    return str


def getAmount(phrase):
    """Function that gets and test input Integer from User, returns int or throws error"""
    while True:
        try:
            val = int(input(phrase + "\n"))  # Inputer
            if (val < 0):  # Tests if inputer Value is positive
                raise (ValueError)
            return val
        except ValueError:  # Tests if inputed Value is an Integer
            print(getRandomResponse())


def getRandomResponse():
    """Function to randomly rebuke Cap'n, returns string response"""
    responses = {
        0: "You're drunk Cap'n...",
        1: "I don't think that is what you meant to put in",
        2: "Try again",
        3: "You're crazy, that can't be right"
    }
    return responses[random.randint(0, 3)]


def preDivvy(gold, pirates):
    """Gives every pirate 3 pieces of gold before hand (not including Cap and Gibbs)"""
    if pirates * 3 > gold:  # Tests if there is enough Gold for everyone
        sys.exit("Cap'n, you are just dumb. You didn't get nearly enough gold for all of you")
    return gold - ((pirates - 2)* 3)  #


def getCapsShare(gold):
    """Returns Captain's share (12%) given gold amount"""
    return gold // (100.0 / 12.0)


def getGibbsShare(capsShare, gold):
    """Returns Gibbs's share (8%) given gold amount"""
    return (gold - capsShare) // (100/8.0)


def getDivvy(gold, pirates):
    """Returns individual share given gold and pirates amount (including Cap and Gibbs)"""
    return gold // pirates


def main():
    """main function for this program"""
    print(getPolly("Polly 2.0"))
    totGold = getAmount("How much Gold did you get Cap'n")
    totPirates = getAmount("How many of you are there? (\033[3mIncluding You and Mr. Gibbs\033[0m)")

    gold = preDivvy(totGold, totPirates)  # Each Pirate (not including Cap and Gibb) get 3 coins before hand
    capsShare = getCapsShare(gold)  # Calculate Caps PreShare
    gibbsShare = getGibbsShare(capsShare, gold)  # Calculate Gibbs PreShare
    gold = gold - capsShare - gibbsShare  # Hide the Gold (Subtract Cap and Gibbs PreShare

    divvy = getDivvy(gold, totPirates)
    print(gold)
    print("\nCap'n Jack gets", int(capsShare + divvy), "\033[33mgold coins\033[0m")
    print("Mr. Gibbs gets", int(gibbsShare + divvy), "\033[33mgold coins\033[0m")
    print("The crew gets", int(divvy), "\033[33mgold coins\033[0m each")
    print("and the Pirate Bonevolent Fund gets", int(gold % divvy), "\033[33mgold coins\033[0m")


if __name__ == '__main__':
    main()
