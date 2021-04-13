"""
Author: William Pendleton

This project is used to fulfill requirements for a multi part project around discovering how random random really is.

In this project I learned about how to make a program do more than just 1 think but to fulfil multiple requirements
given input.
"""
import statistics as stat
from random import seed, randint, choice
from math import hypot
from turtle import *


def set_seed(value):
    """ This function is used to set the seed for testing.
    When testing, this function needs to be called BEFORE main is called
    or any of function that uses random numbers.
    """
    seed(value)


def walk(walker, num_steps):
    """ Simulate one walk of length num_steps.
    Return the final location after num_steps for given walker.
    str, int -> tuple (x, y)
    """
    x = 0
    y = 0
    for i in range(num_steps):
        rand = choice((0, 1, 2, 3))
        if walker == "reg":
            if rand % 2 == 0:
                x += 1
            else:
                x -= 1
        else:
            if rand == 0:
                x += 1
            elif rand == 1:
                y += 1
            elif rand == 2:
                x -= 1
            elif rand == 3:
                y -= 1
                if walker == "mi-ma":
                    y -= 1

    return x, y


def sim_walks(num_steps, num_trials, walker):
    """ Simulates num_trials walks of num_steps by a walker.
    int, int, string -> list of (x,y) positions
    Returns a list of the final positions from each trial.
    """
    lst = []
    seed(20190101)
    for i in range(num_trials):
        lst.append(walk(walker, num_steps))

    return lst


def trial(data):
    """ Given a walker, for each number of steps in walk_lengths, runs sim_walks with num_trials walks and print results.
    return list of final locations for plotting if wanted
    (tuple, int, tuple) -> list
    """
    walk_lengths, num_trials, walker = data

    finals = sim_walks(walk_lengths, num_trials, walker)
    dis = get_distances(finals)

    print(" \033[31m", get_print_name(walker), "\033[0mrandom walk of\033[33m", walk_lengths, "\033[0msteps")
    print("Mean = \033[34m{0:.1f}\033[0m StDev = \033[34m{1:.1f}\033[0m".format(stat.mean(dis), stat.stdev(dis)))
    dis.sort()
    print("Max = \033[34m{0:.1f}\033[0m Min = \033[34m{1:.1f}\033[0m\n".format(dis[-1], dis[0]))

    return(finals)


def plot_locations(walker, finals):
    """ Plot final locations in a turtle window.
    """
    turtle = Turtle()
    turtle.penup()
    turtle.shapesize(1, 1, 2)
    turtle.speed(0)

    sc = turtle.getscreen()

    if walker == "pa":
        turtle.shape("circle")
        turtle.color("black")

    elif walker == "mi-ma":
        turtle.shape("square")
        turtle.color("green")

    elif walker == "reg":
        turtle.shape("triangle")
        turtle.color("red")

    sc.tracer(False)
    for pos in finals:
        x, y = pos
        turtle.setpos(x * 10, y * 10)
        turtle.stamp()
    sc.update()

    turtle.ht()
    del turtle
    pass


def get_distances(finals):
    """ Given a list of final positions it returns a list of the distance those points are from 0,0
    list -> list
    """
    dis = []
    for pos in finals:
        dis.append(hypot(pos[0], pos[1]))

    return dis


def get_print_name(walker):
    """returns walker name capitalized"""
    if walker == "pa":
        return "Pa"
    elif walker == "mi-ma":
        return "Mi-Ma"
    elif walker == "reg":
        return "Reg"


def getPart():
    """Prompts the user what part of the assignment they would list to test.
    Based on the four given parts in the project description.
    """

    print("\033[33mFor a list of what each part of the assignment does, type '\033[31mhelp\033[33m'")
    print("To exit the program type '\033[31mexit\033[33m'\033[0m\n")
    while True:
        try:
            part = input("\033[0mWhat Part of the assignment would you like to test? (\033[34m1\033[0m, \033[34m2\033[0m, \033[34m3\033[0m, or \033[34m4\033[0m): ").lower()
        except ValueError:
            print("\033[31mInvalid input, try again\033[0m")
        else:
            if part == "1" or part == "2" or part == "3" or part == "4" or part == "exit":
                return part
            elif part == "help":
                print("\033[32m\nAssignment Part List:\n-----------------------")
                print("\033[34mPart 1\033[32m: We will ask you to specify 3 different variables and then print and plot the results based on that")
                print("\033[34mPart 2\033[32m: Will just print the required data to the console as specified in the assignment requirements for part 2")
                print("\033[34mPart 3\033[32m: Using Turtle, it will plot the data for Ma, Pa and Reg using 50 trials of 100 walk lengths")
                print("\033[34mPart 4\033[32m: Will print the Doc String")
                print("-----------------------\n\033[0m")
            else:
                print("\033[31mInvalid input, try again\033[0m")


def getPart1():
    """ Prompts the user for the a given test suite, asking:
    1) Walk Length
    2) Amount of Trials
    3) Who to test
    return(int, int, str)
    """

    while True:
        try:
            length = int(input("\nWhat should be the \033[34mlength\033[0m of the walk be?"))
        except ValueError:
            print("\033[31mInvalid input, try again\033[0m")
        else:
            break

    while True:
        try:
            trials = int(input("\nHow many \033[34mtrials\033[0m should we do?"))
        except ValueError:
            print("\033[31mInvalid input, try again\033[0m")
        else:
            break

    while True:
        try:
            testee = input("\nWho's gonna be the testee? (\033[34mPa\033[0m, \033[34mMi-ma\033[0m, \033[34mReg\033[0m)").lower()
        except ValueError:
            print("\033[31mInvalid input, try again\033[0m")
        else:
            if testee == "pa" or testee == "mi-ma" or testee == "reg":
                break
            else:
                print("\033[31mInvalid input, try again\033[0m")

    return length, trials, testee

def main():
    while True:
        part = getPart()
        if part == "exit":
            break
        elif part == "1":
            data = getPart1()
            finals = trial(data)
            plot_locations(data[2], finals)
        elif part == "2":
            data = (100, 50, "pa")
            trial(data)
            data = (1000, 50, "pa")
            trial(data)
            data = (100, 50, "mi-ma")
            trial(data)
            data = (1000, 50, "mi-ma")
            trial(data)
            data = (100, 50, "reg")
            trial(data)
            data = (1000, 50, "reg")
            trial(data)
        elif part == "3":
            finals = sim_walks(100, 50, "pa")
            plot_locations("pa", finals)
            finals = sim_walks(100, 50, "mi-ma")
            plot_locations("mi-ma", finals)
            finals = sim_walks(100, 50, "reg")
            plot_locations("reg", finals)
        elif part == "4":
            print("The higher the amount of steps taken, the higher the changes of going away from the orgin")
        print("\033[35mROUND DONE!!!!!!!!\033[0m")
        print("--------------------------------------------")


if __name__ == "__main__":
    seed(20190101)
    main()
