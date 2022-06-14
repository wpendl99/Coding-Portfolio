"""
Author: William Pendleton

This project uses turtle to draw a tessellation pattern on a canvas that looks neat and organized.

In this project I learned how to work with other modules and learned learned about all they tools that python has to 
offer.
"""
from math import sqrt
from turtle import *

l_hatchling = Turtle()
c_hatchling = Turtle()
d_hatchling = Turtle()
g_hatchling = Turtle()

turtles = [l_hatchling, c_hatchling, d_hatchling, g_hatchling]  # list of turtle
turtle_color = ["light gray", "dark gray", "black"]  # list of colors
l_hatchling.shape("triangle")  # change shapes of turtles
c_hatchling.shape("circle")
d_hatchling.shape("turtle")

for i in range(0, 4):  # adjusts the speed and puts all of the pens up
    turtles[i].speed(0)
    turtles[i].penup()

c_hatchling.color("cyan")  # sets the constant colors of turtles
c_hatchling.pencolor("navy")
d_hatchling.pencolor("black")
l_hatchling.pencolor("white")

columns = int(input("How many COLUMNS would you like to make?"))
rows = int(input("How many ROWS would you like to make?"))
length = int(input("What SIZE would you like to make the shapes?"))

sc = d_hatchling.getscreen()  # Gets the screen width and height which are used to center drawing
width = sc.window_width()
height = sc.window_height()

for i in range(0, 4):  # Setting the beginning position for all of the turtles
    turtles[i].setx(int(-(sqrt(3) * length / 2 * columns)))
    turtles[i].sety((length * rows))


c_radius = ((length / 2.0) * sqrt(3))  # Looks easy, but the amount of MATH that went into it was insane
gap = (length * 2 - c_radius * 2) / 2  # The gap length for g_hatchling
dir = 1  # Variable to keep track of what direction the turtles move


def draw_box(turtle):
    """Uses Global 'length' and 'dir' to draw a row of 3D looking boxes"""
    global dir
    turtles[turtle].setheading(get_heading())

    if dir == 1:  # determines what the starting color is for the background color turtle
        color = 0
    else:
        color = 2

    if turtle == 0 and dir == 1:  # determines the starting position of the background turtle if it is going Right
        turtles[turtle].left(30)
        turtles[turtle].forward(length)
        turtles[turtle].right(90)
    elif turtle == 0 and dir == -1:  # determines the starting position of the background turtle if it is going Left
        turtles[turtle].left(90)
        turtles[turtle].forward(length)
        turtles[turtle].right(90)

    for type in range(1, 5):  # toggles the different diamonds that need to be drawn to create the box
        sides = 4
        if type == 1:
            turtles[turtle].left(30 * dir)
            turtles[0].fillcolor(turtle_color[color])
        elif type == 2:
            turtles[turtle].right(30 * dir)
            turtles[0].fillcolor(turtle_color[color])
        elif turtle == 0 and type == 3:
            turtles[turtle].left(30)
            sides = 2
        elif type == 3:
            turtles[turtle].left(30 * dir)
            sides = 2
        elif type == 4:
            turtles[turtle].right(90 * dir)
            turtles[0].fillcolor(turtle_color[color])

        if sides == 4:  # Begins the fill for the background turtle
            turtles[0].begin_fill()

        for i in range (0, sides):  # draws the diamond
            turtles[turtle].pendown()
            turtles[turtle].forward(length)
            turtles[turtle].right((60 * (i % 2 + 1)) * dir)
            turtles[turtle].penup()

        if turtle == 2:  # If the outline turtle it puts it back into position
            turtles[turtle].setheading(get_heading())

        elif turtle == 0:  # IF the background turtle it have to do a bit more to put it back into position
            turtles[turtle].end_fill()
            if dir == -1 and type == 3:  # direction for drawing left on the 3 phase
                turtles[turtle].setheading(120)
            else:  # direction for drawing everything else XD
                turtles[turtle].setheading(-60 * dir)

        if type != 3:  # Toggles through the differnt colors in turtle_color list
            color += 1
        if color >= 3:
            color = 0

    if turtle == 0:  # moves turtle back into position
        turtles[turtle].left(150)
        turtles[turtle].forward(length)


def draw_circle():
    """Uses Global 'dir' and 'c_radius' to draw a row of 3D looking circles"""
    global dir
    global c_radius
    c_hatchling.setheading(get_heading())  # move into position
    c_hatchling.left(30 * dir)
    c_hatchling.forward(length / 2)

    c_hatchling.pendown()  # draw
    c_hatchling.begin_fill()
    c_hatchling.circle(-c_radius * dir)
    c_hatchling.end_fill()
    c_hatchling.penup()

    c_hatchling.forward(length / 2)  # move into next position
    c_hatchling.right(60 * dir)
    c_hatchling.forward(length)


def draw_gap():
    """Uses Global 'dir' to draw a row of gaps to add 3D affect"""
    global dir
    g_hatchling.setheading(get_heading())  # detects which way it should print
    for i in range(0, 4):  # Toggles through the different ways the turtles has to turn to get into position
        if i == 0:  # Follows the same basis and draw_box()
            g_hatchling.left(30 * dir)
            g_hatchling.forward(length)
            g_hatchling.right(120 * dir)

        elif i == 1 or i == 2:
            g_hatchling.left(60 * dir)
            g_hatchling.forward(length)
            g_hatchling.right(60 * dir)
            g_hatchling.forward(length)
            g_hatchling.right(120 * dir)

        elif i == 3:
            g_hatchling.left(60 * dir)
            g_hatchling.forward(length)
            g_hatchling.right(60 * dir)
            g_hatchling.forward(length)
            g_hatchling.right(60 * dir)
            g_hatchling.forward(length)
            g_hatchling.left(30 * dir)
            break

        g_hatchling.pendown()  # does the actual printing of the lines
        g_hatchling.forward(gap / 3)
        g_hatchling.penup()
        g_hatchling.forward(gap / 3)
        g_hatchling.pendown()
        g_hatchling.forward(gap / 3)
        g_hatchling.penup()
        g_hatchling.backward(gap)


def get_heading():
    """Used to detect which way the turtle should be facing when drawing a shape, returns turtle expected heading"""
    global dir
    if dir == 1:  # if turtle drawing left to right, set the heading to 0
        return 0
    elif dir == -1:  # if turtle drawing right to left, set the heading to 180
        return 180


def new_column():
    """Used to move all of the turtles to their new starting point on new column start"""
    global dir
    global turtles
    for i in range(0, 4):  # Toggles through all of the turtles moving them
        turtles[i].setheading(get_heading())
        turtles[i].right(90 * dir)
        turtles[i].forward(length)
        turtles[i].right(60 * dir)
        turtles[i].forward(length)
        turtles[i].left(120 * dir)
        turtles[i].forward(length)

    if(dir == 1):  # Changes the printing direction of the turtles
        dir = -1
        turtles[i].setheading(180)
    elif(dir == -1):
        dir = 1
        turtles[i].setheading(0)


def main():
    """Main function for project that draws the actual patterns"""
    for ii in range(0, columns):  # The actually printing of the shapes, this one desides how many columns
        for i in range(0, rows):  # This one desides how many rows
            draw_box(0)  # Draw the background colored diamonds
            draw_circle()
            draw_box(2)  # Draw the outline 3D Box
            draw_gap()  # Draw little dashed lines to add 3D effect
        new_column()

    for i in range(0, 4):  # Shows off the 4 different turtles used by having them spread out a little bot on the bottom
        turtles[i].setheading(get_heading())
        turtles[i].forward(125 / 4 * (i + 1))


sc.exitonclick()