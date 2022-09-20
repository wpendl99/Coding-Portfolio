/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#include <stdio.h>

#include "display.h"

// Define line struct
typedef struct {
  uint16_t x0;
  uint16_t y0;
  uint16_t x1;
  uint16_t y1;
  uint16_t color;
} Line;

// Define Triangle struct
typedef struct {
  uint16_t x0;
  uint16_t y0;
  uint16_t x1;
  uint16_t y1;
  uint16_t x2;
  uint16_t y2;
  uint16_t color;
} Triangle;

// Define Circle struct
typedef struct {
  uint16_t x0;
  uint16_t y0;
  uint16_t r;
  uint16_t color;
} Circle;

#define TEXT_SIZE 2
#define LINE1                                                                  \
  (Line) { 0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT, DISPLAY_GREEN }
#define LINE2                                                                  \
  (Line) { 0, DISPLAY_HEIGHT, DISPLAY_WIDTH, 0, DISPLAY_GREEN }
#define TRIANGLE1                                                              \
  (Triangle) {                                                                 \
    DISPLAY_WIDTH / 2, DISPLAY_HEIGHT * 3 / 8, DISPLAY_WIDTH * 3 / 8,          \
        DISPLAY_HEIGHT / 8, DISPLAY_WIDTH * 5 / 8, DISPLAY_HEIGHT / 8,         \
        DISPLAY_YELLOW                                                         \
  }
#define TRIANGLE2                                                              \
  (Triangle) {                                                                 \
    DISPLAY_WIDTH / 2, DISPLAY_HEIGHT * 5 / 8, DISPLAY_WIDTH * 3 / 8,          \
        DISPLAY_HEIGHT * 7 / 8, DISPLAY_WIDTH * 5 / 8, DISPLAY_HEIGHT * 7 / 8, \
        DISPLAY_YELLOW                                                         \
  }
#define CIRCLE1                                                                \
  (Circle) { DISPLAY_WIDTH / 4, DISPLAY_HEIGHT / 2, 30, DISPLAY_RED }
#define CIRCLE2                                                                \
  (Circle) { DISPLAY_WIDTH * 3 / 4, DISPLAY_HEIGHT / 2, 30, DISPLAY_RED }
#define CURSOR_X 10
#define CURSOR_Y (DISPLAY_HEIGHT / 2)

uint8_t mystery(uint8_t x) { return ~(1 << x); }

// Print out "hello world" on both the console and the LCD screen.
int main() {

  // Initialize display driver, and fill scren with black
  display_init();
  display_fillScreen(DISPLAY_BLACK); // Blank the screen.

  // Draw the Lines.
  display_drawLine(LINE1.x0, LINE1.y0, LINE1.x1, LINE1.y1, LINE1.color);
  display_drawLine(LINE2.x0, LINE2.y0, LINE2.x1, LINE2.y1, LINE2.color);

  // Draw the Triangles
  display_fillTriangle(TRIANGLE1.x0, TRIANGLE1.y0, TRIANGLE1.x1, TRIANGLE1.y1,
                       TRIANGLE1.x2, TRIANGLE1.y2, TRIANGLE1.color);
  display_drawTriangle(TRIANGLE2.x0, TRIANGLE2.y0, TRIANGLE2.x1, TRIANGLE2.y1,
                       TRIANGLE2.x2, TRIANGLE2.y2, TRIANGLE2.color);

  // Draw the Circles
  display_drawCircle(CIRCLE1.x0, CIRCLE1.y0, CIRCLE1.r, CIRCLE1.color);
  display_fillCircle(CIRCLE2.x0, CIRCLE2.y0, CIRCLE2.r, CIRCLE2.color);

  // Also print out 'hello world' on the terminal(stdout).

  return 0;
}
