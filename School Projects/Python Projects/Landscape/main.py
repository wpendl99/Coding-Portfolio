"""
Author: William Pendleton

This project uses turtle to draw a scene that can also be scaled.

In this project I learned how to better structure my functions to allow for better variability
"""
from math import sqrt, cos, sin, pi, acos, copysign
from turtle import *
from random import *
import time

turtles = []
x_Vars = []
rotations = []
counter = []
y_Pos = []
sizes = []
max_Snow = 40
max_Trees = 20
max_Stars = 60
max_Sun_Beams = 25

# Used to help get specs on the screen that is going to be used to draw
dummy = Turtle()
sc = dummy.getscreen()
dummy.ht()
del dummy

# This is used to get use input determining what scale we should paint the picture in
while True:
    try:
        size = int(input("One a scale from 1 to 4 what size do you want it to be? (1 is the smallest, 4 is the largest): "))
    except ValueError:
        print("Invalid input, try again")
    else:
        if 5 > size > 0:
            break
        else:
            print("Invalid input, try again")

# Used to get user input to determine what type of scene they want, Snowy, Sunny, Night
# It does have a type tester
while True:
    try:
        weather = input("What type of scene would you like? (Sunny, Snowy or Night): ").lower()
    except ValueError:
        print("Invalid input, try again")
    else:
        if weather == "sunny" or weather == "snowy" or weather == "night":
            break
        else:
            print("Invalid input, try again")

#Used to set the actual screen size and set a stable variable for it
sc_Height = int(900 * size / 4)
sc.setup(sc_Height / 3 * 4, sc_Height)
sc_Width = int(sc.window_width())

# Set the background colour of the window based on the type of scene
if weather == "snowy":
    sc.screensize(sc_Width - 20, sc_Height - 20, "light grey")
elif weather == "night":
    sc.screensize(sc_Width - 20, sc_Height - 20, "black")
elif weather == "sunny":
    sc.screensize(sc_Width - 20, sc_Height - 20, "light blue")
sun_Radius = int(sc_Width / 6 * size / 4)


def draw_Mountains():
    """This function is used to put together the mountains in the background and snow base on the bottom
    It doesn't require a turtle because it will make and delete it's own"""
    shades_Of_Grey = ['#696969', '#808080', '#A9A9A9', '#C0C0C0']
    mountain = Turtle()
    mountain.penup()
    mountain.speed(0)
    moun_Min = [-(sc_Width / 2), -(sc_Width / 4), 0 + 50, sc_Width / 4]
    moun_Max = [-(sc_Width / 4), 0, sc_Width / 4, (sc_Width / 2)]
    counter = [2, 4, 1, 3]

    for i in range(1, 5):
        x = counter[i-1]
        mountain.setpos(randrange(int(moun_Min[x - 1]), int(moun_Max[x - 1])), randrange(int(sc_Height / 9), int(sc_Height / 4)))
        draw_Mountain(mountain, shades_Of_Grey[i - 1])

    draw_Base(mountain)

    if weather == "snowy":
       for i in range(0, 10):
        draw_Clouds(mountain)
    elif weather == "sunny":
        i = 0
    elif weather == "night":
        i = 0

    mountain.ht()
    del mountain


def draw_Mountain(turtle, colour):
    """Called by draw_Mountains to actually randomly draw mountains in different positions with different colours to give it depth"""
    turtle.fillcolor(colour)
    turtle.pencolor("black")
    turtle.right(45)
    turtle.begin_fill()
    turtle.down()
    turtle.forward(700)
    turtle.right(135)
    turtle.forward(700 * sqrt(2))
    turtle.right(135)
    turtle.forward(700)
    turtle.up()
    turtle.end_fill()
    turtle.setheading(0)
    draw_Snow_Line(turtle)


def draw_Snow_Line(turtle, scale=None):
    """Used by both draw_Mountain AND draw_Tree to draw the little snow caps on the tops of the objects
    Requires the turtle from both to know what position to draw it
    It draws with a random scale for the mountains
    BUT requires scale=.1 when drawing tree to know that that's the scale for the tree"""
    if scale is None:
        scale = randrange(4, 15) / 10.0 * sc_Height / 1000
    turtle.pencolor("black")
    fullLength = 100 * scale
    smallLength = fullLength / 3
    turtle.fillcolor("white")
    turtle.down()
    turtle.begin_fill()
    turtle.right(45)
    turtle.forward(fullLength)
    turtle.right(90)
    for i in range(0,3):
        turtle.forward(smallLength)
        turtle.right(90)
        turtle.forward(smallLength)
        turtle.left(90)
    turtle.right(180)
    turtle.forward(fullLength)
    turtle.end_fill()
    turtle.up()
    turtle.setheading(0)


def draw_Base(turtle):
    """Used by draw_Mountains to draw the snow base on the bottom of the screen on top of the mountains,
    Requires a turtle to be passed in, but it doesn't matter it's location"""
    turtle.fillcolor("white")
    turtle.pencolor("black")
    turtle.setpos(-(sc_Width / 2), -(sc_Height / 4))
    turtle.begin_fill()
    turtle.down()
    for i in range(0, 2):
        turtle.forward(sc_Width)
        turtle.right(90)
        turtle.forward(sc_Height / 4)
        turtle.right(90)
    turtle.up()
    turtle.end_fill()


def draw_Clouds(turtle):
    """Used by draw_Mountains to put clouds in the sky if the scene type is snowy which will draw random clouds in the sky
    requires a turtle but it doesn't matter the position"""
    turtle.fillcolor("dark grey")
    turtle.pencolor("black")
    turtle.setpos(randrange(0, sc_Width) - int(sc_Width / 2), randrange(int(sc_Height / 2 - 60), int(sc_Height / 2 + 10)))
    lst = turtle.pos()
    for i in range(0, 7):
        turtle.begin_fill()
        turtle.down()
        turtle.circle(20 * size / 4)
        turtle.up()
        turtle.end_fill()
        turtle.setpos(randrange(lst[0] - 40 * size / 4, lst[0] + 40 * size / 4), randrange(lst[1] - 20 * size / 4, lst[1] + 20 * size / 4))


def draw_Trees():
    """Main Function called after draw_Mountains which is used to draw trees and snow caps on top of the snow base"""
    tree_Turtle = Turtle()
    tree_Turtle.speed(0)
    tree_Turtle.pencolor("black")
    tree_Turtle.up()
    ys = []
    for i in range(0, max_Trees):
        ys.append(randrange(- int(sc_Height / 2), int(sc_Height / 4 - sc_Height / 2)))
    ys.sort(reverse=True)
    for i in range(0, max_Trees):
        tree_Turtle.setpos(randrange(-(sc_Width / 2), sc_Width / 2), ys[i])
        draw_Tree(tree_Turtle)
    tree_Turtle.ht()
    del tree_Turtle


def draw_Tree(turtle):
    """Called by draw_Trees to draw the individual tree in a random position determined by draw_trees
    requires a turtle already set in position. This is to make sure that depth is still correct in the picture"""
    tree_Colours = ["#006400", "#556b2f", "#8fbc8f", "#2e8b57", "#3cb371", "#20b2aa", "#32cd32"]
    turtle.fillcolor("brown")
    turtle.begin_fill()
    turtle.down()
    for i in range(0, 2):
        turtle.forward(5 * size / 4)
        turtle.right(90)
        turtle.forward(10 * size / 4)
        turtle.right(90)
    turtle.up()
    turtle.end_fill()
    turtle.forward(2.5 * size / 4)
    turtle.setpos(turtle.xcor(), turtle.ycor() + 10 * size / 4)
    turtle.fillcolor(choice(tree_Colours))
    for i in range(0, 2):
        turtle.right(45)
        turtle.begin_fill()
        turtle.down()
        turtle.forward(20 * size / 4),
        turtle.right(135),
        turtle.forward(20 * sqrt(2) * size / 4)
        turtle.right(135)
        turtle.forward(20 * size / 4)
        turtle.end_fill()
        turtle.up()
        turtle.setheading(0)
        if i == 0:
            turtle.setpos(turtle.xcor(), turtle.ycor() + 10 * size / 4)
    draw_Snow_Line(turtle, .1 * size / 4)


def make_It_Snow():
    """Once draw_Mountains and draw_Trees have been called and executed this one should be called is the scene type is snowy
    This will put the turtle window in a True loop while it creates a set amount of turtles determined by max_Snow that fall to look like snowflakes falling from the sky"""
    global turtles
    global x_Vars
    global rotations
    global counter
    global y_Pos
    global sizes

    while True:

        while len(turtles) < max_Snow:
            create_Snow_Turtle()

        sc.tracer(False)

        for i in range(0, len(turtles)):
            turtle = turtles[i]

            if turtle.ycor() >= y_Pos[i] and counter[i] != 15:
                turtle.sety(turtle.ycor() - 2)

                turtle._rotate(rotations[i])

                x_Vars[i] += (randrange(-100, 100) / 400)
                if x_Vars[i] > 1:
                    x_Vars[i] = 1
                if x_Vars[i] < -1:
                    x_Vars[i] = -1
                turtle.setx(turtle.xcor() + x_Vars[i])

                if sizes[i] == -1:
                    turtle.shapesize(turtle.shapesize()[0] - .003)
                    if turtle.shapesize()[0] < .2:
                        sizes[i] = 1
                elif sizes[i] == 1:
                    turtle.shapesize(turtle.shapesize()[0] + .003)
                    if turtle.shapesize()[0] > .3:
                        sizes[i] = -1

            elif counter[i] == 12:
                turtles[i].ht()
                del turtles[i]
                x_Vars.pop(i)
                rotations.pop(i)
                counter.pop(i)
                sizes.pop(i)
                create_Snow_Turtle()
            else:
                counter[i] += 1

        sc.update()


def create_Snow_Turtle():
    """Called by make_It_Snow to, both, at the beginning of execution and when a snowflake expires to then make another one in it's place"""
    global turtles
    global x_Vars
    global rotations
    global sc
    global counter
    global y_Pos
    global sizes

    turtles.append(Turtle())
    turtles[-1].penup()
    turtles[-1].speed(0)
    x_Vars.append(0)
    rotations.append(randint(-5, 5))
    counter.append(0)
    y_Pos.append(randint(-(int(sc.window_height() / 2) - int(sc.window_height() / 18)), -(int(sc.window_height() / 2) - int(sc.window_height() / 4))))
    turtles[-1].sety(sc.window_height() / 2 + randint(0, 1000))
    turtles[-1].setx(randint(0, sc.window_width()) - sc.window_width() / 2)
    turtles[-1].shape("turtle")
    sizes.append(choice([-1, 1]))
    turtles[-1].shapesize(randrange(10, 30) / 100)
    turtles[-1].color("White")
    turtles[-1].pencolor("Black")


def paint_Some_Stars():
    """Once draw_Mountains and draw_Trees have been called and executed this one should be called is the scene type is night
    This will put the turtle window in a True loop while it creates a set amount of turtles determined by max_stars that will look like twinkling stars"""
    global turtles
    global rotations

    canvas = sc.getcanvas()
    counter = 1

    for i in range(0, max_Stars):
        while True:
            x = randrange(-int(sc_Width / 2), int(sc_Width / 2))
            y = randrange(-int(sc_Height / 4), int(sc_Height / 2))
            if not canvas.find_overlapping(x - 2, -y - 2, x + 2, -y + 2):
                break
        create_Star_Turtle(x, y)
        counter += 1

    while True:
        timer = randint(0, 20)
        time.sleep(timer / 10)
        tur = randint(0, len(turtles) - 1)
        for i in range(0, 10):
            turtles[tur].shapesize(turtles[tur].shapesize()[1] + .1)
        for i in range(0, 10):
            turtles[tur].shapesize(turtles[tur].shapesize()[1] - .1)


def create_Star_Turtle(x, y):
    """Called by paint_Some_Stars to actually create the turtle GIVEN an x and y cord that has been determined by paint_Some_Stars as a location where there is not a mountain behind it
    **REQUIRES x and y cord**"""
    global turtles
    global x_Vars
    global rotations
    global sc
    global counter
    global y_Pos
    global sizes

    turtles.append(Turtle())
    turtles[-1].penup()
    turtles[-1].speed(0)
    turtles[-1].setpos(x, y)
    turtles[-1].color("white")
    turtles[-1].shape("turtle")
    turtles[-1].shapesize(randrange(10, 30) / 100)


def turn_Up_The_Light():
    """Once draw_Mountains and draw_Trees have been called and executed this one should be called is the scene type is Sunny
    This will put the turtle window in a True loop while it creates a sun out of turtles.
    This function will draw the base of the sun, and then call other functions to draw the sun beams and move them"""
    global sun_Radius

    testie = Turtle()
    testie.shape("turtle")
    testie.up()
    testie.fillcolor("yellow")
    testie.pencolor("orange")
    testie.shapesize(sun_Radius / 20, sun_Radius / 20, 5)
    testie.speed(0)
    testie.setpos(sc_Width / 2, sc_Height / 2)
    testie.right(135)
    testie.speed(4)

    draw_Sun_Beam()
    offset = .5
    while True:
        let_It_Shine(offset=offset, radius=sun_Radius)

        sun_Radius += 1

        if sun_Radius > sc_Width / 6 * 1.5:
            sun_Radius = -sun_Radius
        elif 0 > sun_Radius > -sc_Width / 6:
            sun_Radius = abs(sun_Radius)
        else:
            offset += .5


def draw_Sun_Beam(offset=0.0):
    """Called by turn_Up_The_Light which is used at the beginning to initially draw the turtle sunbeams"""
    global turtles
    sc.tracer(False)
    for i in range(int(max_Sun_Beams * size / 4)):
        radius = 300
        x = radius * cos((i * 360 / 25 * size / 4 + offset) * pi / 180)
        y = radius * sin((i * 360 / 25 * size / 4 + offset) * pi / 180)
        x_Vars.append(-copysign(1.0, y))
        y_Pos.append(copysign(1.0, x))
        turtles.append(Turtle())
        turtles[-1].shape("turtle")
        turtles[-1].up()
        turtles[-1].color("orange")
        turtles[-1].shapesize(1.5 * size / 4)
        turtles[-1].speed(0)
        turtles[-1].goto(x + sc_Width / 2, y + sc_Height / 2)
        turtles[-1].left(360 / max_Sun_Beams * i)
    sc.update()


def let_It_Shine(offset=0.0, radius=sun_Radius):
    """Called by turn_Up_The_Light which is used to move the sun beams according to the offset and the radius of the which is given as arguments"""
    global turtles

    sc.tracer(False)

    for i in range(int(max_Sun_Beams * size / 4)):
        turtle = turtles[i]

        x = radius * cos((i * 360 / 25 / size * 4 + offset) * pi / 180)
        y = radius * sin((i * 360 / 25 / size * 4 + offset) * pi / 180)

        turtles[i].goto(x + sc_Width / 2, y + sc_Height / 2)
        turtle.right(.005 * 180 / pi)
        turtle.setheading(i * 360 / 25 / size * 4 + offset)

    sc.update()


def main():
    """The main function that calls main functions draw_Mountains and draw_Trees and then the corresponding main function based on scene type"""
    draw_Mountains()
    draw_Trees()
    if weather == "snowy":
        make_It_Snow()
    elif weather == "night":
        paint_Some_Stars()
    elif weather == "sunny":
        turn_Up_The_Light()


if __name__ == '__main__':
    main()
