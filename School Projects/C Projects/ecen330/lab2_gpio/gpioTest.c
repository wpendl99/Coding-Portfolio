#ifndef GPIOTEST
#define GPIOTEST

#include "display.h"
#include "drivers/buttons.h"
#include "drivers/switches.h"
#include "leds.h"

#include <math.h>
#include <stdio.h>
#include <unistd.h>

// Rectangle Type
typedef struct {
  uint16_t x;
  uint16_t y;
  uint16_t w;
  uint16_t h;
  uint16_t on_color;
  uint16_t text_color;
  uint16_t off_color;
} Rectangle;

// Colors array for rectangle fill color
uint16_t fill_colors[4] = {DISPLAY_RED, DISPLAY_BLUE, DISPLAY_GREEN,
                           DISPLAY_YELLOW};
uint16_t text_colors[4] = {DISPLAY_BLACK, DISPLAY_WHITE, DISPLAY_BLACK,
                           DISPLAY_BLACK};

// State Values
uint8_t prev_switches_state = 0x00;
uint8_t prev_buttons_state = 0x00;

// Constant Values
#define all_on 0x0F
#define all_off 0x00
#define BUTTONS_COUNT 4
#define SWITCHES_COUNT 4

// Array of all of the display rectangles associated to a button
Rectangle rectangles[BUTTONS_COUNT];

// Declare Functions
static uint8_t did_switches_toggle(uint8_t index, uint8_t current_state);
static uint8_t did_buttons_toggle(uint8_t index, uint8_t current_state);
static void toggle_led(uint8_t index);
static void toggle_rectangle(uint8_t index);
static void rectangles_init();
static void draw_text(uint8_t index);
static uint8_t index_to_hex(uint8_t index);

// Runs a test of the buttons. As you push the buttons, graphics and messages
// will be written to the LCD panel. The test will until all 4 pushbuttons are
// simultaneously pressed.
void gpioTest_buttons() {
  buttons_init();
  rectangles_init();
  display_init();
  display_fillScreen(DISPLAY_BLACK); // Blank the screen.

  // While loop that flashes rectangles on the screen while a button is turned
  // on Each button has an 0 based index id that corresponds to a 0 based index
  // id for an rectangle in rectangles
  while (prev_buttons_state != all_on) {
    uint8_t cur_buttons_state = buttons_read();

    uint8_t i;
    // Loop through all of the switches to see if they have changes states
    for (i = 0; i < BUTTONS_COUNT; ++i) {
      if (did_buttons_toggle(i, cur_buttons_state)) {
        toggle_rectangle(i);
      }
    }
  }

  display_fillScreen(DISPLAY_BLACK); // Blank the screen.
}

// Runs a test of the switches. As you slide the switches, LEDs directly above
// the switches will illuminate. The test will run until all switches are slid
// upwards. When all 4 slide switches are slid upward, this function will
// return.
void gpioTest_switches() {
  switches_init();
  leds_init();

  // While loop that turns LEDs on when a switch is turned on
  // Each switch has an 0 based index id that corresponds to a 0 based index id
  // for an LED
  while (prev_switches_state != all_on) {
    uint8_t cur_switches_state = switches_read();

    // Loop through all of the switches to see if they have toggled
    for (uint8_t i = 0; i < SWITCHES_COUNT; ++i) {
      if (did_switches_toggle(i, cur_switches_state)) {
        toggle_led(i);
      }
    }
  }

  leds_write(all_off);
}

// Used to check if a given switch number was toggled
static uint8_t did_switches_toggle(uint8_t index, uint8_t current_state) {
  return ((current_state & index_to_hex(index)) ^
          (prev_switches_state & index_to_hex(index)));
}

// Used to check if a given button number was toggled
static uint8_t did_buttons_toggle(uint8_t index, uint8_t current_state) {
  return ((current_state & index_to_hex(index)) ^
          (prev_buttons_state & index_to_hex(index)));
}

// Used to turn on and off leds based on the number of switch that was flipped
static void toggle_led(uint8_t index) {
  uint8_t index_mask = index_to_hex(index);

  // If the switch was already on
  if ((prev_switches_state & index_mask) && index_mask) {
    prev_switches_state = ~index_mask & prev_switches_state;
    leds_write(prev_switches_state);
  }
  // If the switch was already off
  else {
    prev_switches_state = index_mask | prev_switches_state;
    leds_write(prev_switches_state);
  }
}

// Used to draw and undraw rectangles on the display based on the button number
// that was pushed
static void toggle_rectangle(uint8_t index) {
  uint8_t index_mask = index_to_hex(index);
  Rectangle rectangle = rectangles[index];

  // If the button was already on, draw a black box
  if ((prev_buttons_state & index_mask) && index_mask) {
    prev_buttons_state = ~index_mask & prev_buttons_state;
    display_fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h,
                     rectangle.off_color);
  }
  // If the button was already off, draw the appropriate color box
  else {
    prev_buttons_state = index_mask | prev_buttons_state;
    display_fillRect(rectangle.x, rectangle.y, rectangle.w, rectangle.h,
                     rectangle.on_color);
    draw_text(index);
  }
}

// Used to initiate rectangles array and fill it with a Rectangle for every
// button
static void rectangles_init() {
  uint16_t rectangle_width = DISPLAY_WIDTH / BUTTONS_COUNT;
  uint16_t rectantle_height = DISPLAY_HEIGHT / 2;

  // Loop through all of the buttons and make an associate rectangle and store
  // it in rectangles
  for (uint8_t i = 0; i < BUTTONS_COUNT; i++) {
    rectangles[i] = (Rectangle){
        rectangle_width * (BUTTONS_COUNT - i - 1),
        0,
        rectangle_width,
        rectantle_height,
        fill_colors[i % (sizeof(fill_colors) / sizeof(fill_colors[0]))],
        text_colors[i % (sizeof(text_colors) / sizeof(text_colors[0]))],
        DISPLAY_BLACK};
  }
}

// Helper function used to set the cursor and right text inside of a given
// rectangle index in rectangles
static void draw_text(uint8_t index) {
  Rectangle rectangle = rectangles[index];

  display_setCursor(rectangle.x, rectangle.y + (rectangle.w / 3 * 2));
  display_setTextColor(rectangle.text_color);
  display_setTextSize(2);

  display_print(" BTN ");
  display_printDecimalInt(index);
}

// Helper function used to convert int index value to help convert int to hex,
// but honestly now that I think about it this is probably useless and I could
// just use the actual int values... XD I will fix this on future projects
static uint8_t index_to_hex(uint8_t index) { return pow(2, index); }

#endif /* GPIOTEST */