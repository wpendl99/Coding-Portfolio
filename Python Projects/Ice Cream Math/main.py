"""
Author: William Pendleton

A program used to print out ice cream information about The Pouncing Tiger Dairy Company.

In this project I learned how to use pythons print statement, how to use math functions, and how to make my own functions.
"""
import math


def getTubVol(di, ht):
    """calculates the volume of the tub using a Cylinder volume formula (Pi * radius^2 * height) given diameter and height"""
    r = di / 2  # calculate Radius
    return math.pi * (r ** 2) * ht  # returns calculated volume


def getSphereVol(di):
    """calculates the volume of the scoop using a sphere volume formula (4/3 * Pi * radius^3) given diameter"""
    r = di / 2   # calculate Radius
    return (4.0 / 3.0) * math.pi * (r ** 3)  # returns calculated volume


def getServings(tubVol, sphereVol):
    """calculates amount of servings from one tub given tub volume and scoop volume"""
    return tubVol / sphereVol  # returns calculated amount


def main():
    name = "Johnny Lingo"  # preassigned variables defined in problem statement
    flavour = "'Cow'conut Cream Pie"
    tubDi = 9.5
    tubHt = 11
    scoopDi = 2
    tubVol = getTubVol(tubDi, tubHt)  # calls function getTubVol to calculate the volume of the tub
    sphereVol = getSphereVol(scoopDi)  # calls function getSphereVol to calcuate the volume of the scoop
    # (For some reason in the problem statement they want the scoop called sphere)

    print("Name: ", name)  # printing of needed variables required in problem statement
    print("Flavour: ", flavour)
    print("Tub: ", tubVol)
    print("Sphere: ", sphereVol)
    print("Servings: ", getServings(tubVol, sphereVol))  # calls function getServings for print servings amount


if __name__ == '__main__':
    main()