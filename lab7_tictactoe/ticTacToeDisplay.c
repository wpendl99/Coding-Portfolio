/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/
#include "ticTacToeDisplay.h"

#include "display.h"

#define PADY 5
#define XO_WIDTH (DISPLAY_HEIGHT / 3 - 2 * PADY)
#define PADX ((DISPLAY_WIDTH - 3 * XO_WIDTH) / 6)

#define COLOR_GRID DISPLAY_WHITE
#define COLOR_X DISPLAY_RED
#define COLOR_O DISPLAY_WHITE

#define BACKGROUND_COLOR DISPLAY_DARK_BLUE

///////////////////////////////////////////////////////////////////////////////
///////////////////////////// Globals /////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
///////////////////////////// Local Functions /////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////
///////////////////////////// Fuction Definitions /////////////////////////////
///////////////////////////////////////////////////////////////////////////////

// Inits the tic-tac-toe display, draws the lines that form the board.
void ticTacToeDisplay_init() {
  display_drawFastHLine(0, DISPLAY_HEIGHT / 3, DISPLAY_WIDTH, COLOR_GRID);
  display_drawFastHLine(0, DISPLAY_HEIGHT * 2 / 3, DISPLAY_WIDTH, COLOR_GRID);
  display_drawFastVLine(DISPLAY_WIDTH / 3, 0, DISPLAY_HEIGHT, COLOR_GRID);
  display_drawFastVLine(DISPLAY_WIDTH * 2 / 3, 0, DISPLAY_HEIGHT, COLOR_GRID);
}

// Draws an X at the specified location
// erase == true means to erase the X by redrawing it as background.
column.void ticTacToeDisplay_drawX(tictactoe_location_t location, bool erase) {
  uint16_t x1 = DISPLAY_WIDTH * location.column / 3 + PADX;
  uint16_t x2 = x1 + XO_WIDTH;
  uint16_t y1 = DISPLAY_HEIGHT * location.row / 3 + PADY;
  uint16_t y2 = y1 + XO_WIDTH;

  display_drawLine(x1, y1, x2, y2, erase ? BACKGROUND_COLOR : COLOR_X);
  display_drawLine(x1, y2, x2, y1, erase ? BACKGROUND_COLOR : COLOR_X);
}

// Draws an O at the specified row and column.
// erase == true means to erase the X by redrawing it as background.
void ticTacToeDisplay_drawO(tictactoe_location_t location, bool erase) {
  uint16_t x = DISPLAY_WIDTH * location.column / 3 + PADX + XO_WIDTH / 2;
  uint16_t y = DISPLAY_HEIGHT * location.row / 3 + PADY + XO_WIDTH / 2;

  display_drawCircle(x, y, XO_WIDTH / 2, erase ? BACKGROUND_COLOR : COLOR_O);
}

// For a given touch location on the touchscreen, this function returns the
// corresponding tictactoe board location.
tictactoe_location_t
ticTacToeDisplay_getLocationFromPoint(display_point_t point) {
  tictactoe_location_t location;

  int16_t x = point.x;
  int16_t y = point.y;

  uint16_t x1 = DISPLAY_WIDTH / 3;
  uint16_t x2 = DISPLAY_WIDTH * 2 / 3;
  uint16_t y1 = DISPLAY_HEIGHT / 3;
  uint16_t y2 = DISPLAY_HEIGHT * 2 / 3;

  if (x < x1)
    location.column = 0;
  else if (x < x2)
    location.column = 1;
  else
    location.column = 2;

  if (y < y1)
    location.row = 0;
  else if (y < y2)
    location.row = 1;
  else
    location.row = 2;

  return location;
}
