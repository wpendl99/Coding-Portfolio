#ifndef TOUCHSCREEN
#define TOUCHSCREEN

#include "display.h"
#include "stdio.h"

// Status of the touchscreen
typedef enum {
  TOUCHSCREEN_IDLE,    // Touchscreen is idle (not pressed)
  TOUCHSCREEN_PRESSED, // Touchscreen is actively being pressed
  TOUCHSCREEN_RELEASED // Touchscreen has been released, but not acknowledged
} touchscreen_status_t;

typedef enum { init_st, waiting_st, adc_settling_st, pressed_st } sm_state_t;

static sm_state_t currentState;

// All printed messages for states are provided here.
#define init_st_msg "Touchscreen Init\n"
#define waiting_st_msg "Touchscreen Waiting\n"
#define adc_settling_st_msg "ADC Settling\n"
#define pressed_st_msg "Touchscreen Pressed\n"
#define error_msg                                                              \
  "Touchscreen: There was an error with the state machine, please try again "  \
  "later\n"

// Touch Variables
static uint16_t x, y;
static uint8_t z;
static bool pressed;

// ADC Timer Variables
static uint16_t adc_timer;
static uint16_t adc_settle_ticks;
#define ADC_SETTLE_TIME_S .05
#define INITAL_VAL 0

// Declare Function
void debugTouchscreenStatePrint();

// Initialize the touchscreen driver state machine, with a given tick period
// (in seconds).
void touchscreen_init(double period_seconds) {
  currentState = init_st;
  adc_timer = INITAL_VAL;
  adc_settle_ticks = ADC_SETTLE_TIME_S / period_seconds;
  pressed = false;
}

// Tick the touchscreen driver state machine
void touchscreen_tick() {
  debugTouchscreenStatePrint();

  // Perform state update first
  switch (currentState) {
  case init_st:
    currentState = waiting_st;
    break;
  case waiting_st:
    // The the display is touched while it is waiting
    if (display_isTouched()) {
      display_clearOldTouchData();
      currentState = adc_settling_st;
    }
    break;
  case adc_settling_st:
    // If you stop touching the screen before the ADC can settle, or if the ADC
    // settles
    if (!display_isTouched()) {
      currentState = waiting_st;
    } else if (adc_timer == adc_settle_ticks) {
      display_getTouchedPoint(&x, &y, &z);
      currentState = pressed_st;
    }
    break;
  case pressed_st:
    // If you stop touching the screen after the ADC settles
    if (!display_isTouched()) {
      currentState = waiting_st;
    }
    break;
  default:
    // If an unexpected state happens
    printf(error_msg);
    break;
  }

  // Perform state update first
  switch (currentState) {
  case waiting_st:
    adc_timer = 0;
    break;
  case adc_settling_st:
    adc_timer++;
    break;
  case pressed_st:
    pressed = true;
    break;
  default:
    printf(error_msg);
    break;
  }
}

// Return the current status of the touchscreen
touchscreen_status_t touchscreen_get_status() {
  // Depending on the status of the touchscreen SM and the pressed variable,
  // return a touchscreen_status_t
  switch (currentState) {
  // If the touch screen is waiting for a touch
  case waiting_st:
    // If the pressed variable hasn't been acknowledge
    if (pressed) {
      return TOUCHSCREEN_RELEASED;
    } else {
      return TOUCHSCREEN_IDLE;
    }
    break;

  // If the touchscreen is currently being touch and waiting for ADC to settle
  case adc_settling_st:
    return TOUCHSCREEN_IDLE;
    break;

    // If the touchscreen is currently being touch
  case pressed_st:
    return TOUCHSCREEN_PRESSED;
    break;

    // If an unexpected state happens, throw error
  default:
    printf(error_msg);
    break;
  }
}

// Acknowledge the touchscreen touch.  This function will only have effect when
// the touchscreen is in the TOUCHSCREEN_RELEASED status, and will cause it to
// switch to the TOUCHSCREEN_IDLE status.
void touchscreen_ack_touch() { pressed = false; }

// Get the (x,y) location of the last touchscreen touch
display_point_t touchscreen_get_location() { return (display_point_t){x, y}; }

// This is a debug state print routine. It will print the names of the states
// each time tick() is called. It only prints states if they are different than
// the previous state.
void debugTouchscreenStatePrint() {
  static sm_state_t previousState;
  static bool firstPass = true;
  // Only print the message if:
  // 1. This the first pass and the value for previousState is unknown.
  // 2. previousState != currentState - this prevents reprinting the same state
  // name over and over.
  if (previousState != currentState || firstPass) {
    firstPass = false; // previousState will be defined, firstPass is false.
    previousState =
        currentState;       // keep track of the last state that you were in.
    switch (currentState) { // This prints messages based upon the state that
                            // you were in.
    case init_st:
      printf(init_st_msg);
      break;
    case waiting_st:
      printf(waiting_st_msg);
      break;
    case adc_settling_st:
      printf(adc_settling_st_msg);
      break;
    case pressed_st:
      printf(pressed_st_msg);
      break;
    }
  }
}

#endif /* TOUCHSCREEN */
