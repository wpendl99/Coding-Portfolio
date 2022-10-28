#ifndef CLOCKCONTROL
#define CLOCKCONTROL

#include "clockDisplay.h"
#include "stdio.h"
#include "touchscreen.h"

// Clock State Machine States
typedef enum {
  init_st,
  waiting_st,
  inc_dec_st,
  long_press_delay_st,
  fast_update_st
} sm_state_t;

static sm_state_t currentState;

// Clock State Variables

// Clock Constants
static uint16_t delay_cnt;
static uint16_t delay_num_ticks;
static uint16_t update_cnt;
static uint16_t update_num_ticks;
#define DELAY_TIME_S .5
#define UPDATE_TIME_S .1
#define INITAL_VAL 0

// Clock State Messages for Debugging
#define init_st_msg "Clock Init\n"
#define waiting_st_msg "Clock Waiting\n"
#define inc_dec_st_msg "Inc Dec\n"
#define long_press_delay_st_msg "Long Press Delay\n"
#define fast_update_st_msg "Fast Update State\n"
#define error_msg                                                              \
  "Clock: There was an error with the clock state machine, please try again "  \
  "later\n"

// Declare Function
void debugClockStatePrint();

// Initialize the clock control state machine, with a given period in seconds.
void clockControl_init(double period_s) {
  currentState = init_st;
  delay_cnt = INITAL_VAL;
  delay_num_ticks = DELAY_TIME_S / period_s;
  update_cnt = INITAL_VAL;
  update_num_ticks = UPDATE_TIME_S / period_s;
}

// Tick the clock control state machine
void clockControl_tick() {
  debugClockStatePrint();

  // Perform state update first
  switch (currentState) {
  case init_st:
    currentState = waiting_st;
    break;
  case waiting_st:
    // The the display is touched while it is waiting
    if (touchscreen_get_status() == TOUCHSCREEN_PRESSED) {
      delay_cnt = INITAL_VAL;
      currentState = long_press_delay_st;
      // If the display is somehow in the released state
    } else if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      currentState = inc_dec_st;
    }
    break;
  case long_press_delay_st:
    // While the user is touching the screen, wait for the release or transition
    // to fast update if delay_cnt == delay_num_ticks
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      currentState = inc_dec_st;
      // If the delay count threshold is hit, change to fast update state
    } else if (delay_cnt == delay_num_ticks) {
      update_cnt = INITAL_VAL;
      currentState = fast_update_st;
    }
    break;
  case inc_dec_st:
    // If the user stops touching the screen before delay_cnt ==
    // delay_num_ticks, perform a single increment
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      touchscreen_ack_touch();
      currentState = waiting_st;
    }
    break;
  case fast_update_st:
    // If the user holds the screen for a long period of time, enter fast_update
    // mode which inc/dec the clock a lot faster
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      touchscreen_ack_touch();
      currentState = waiting_st;
      // If the update count threshold is hit, update the clock and reset timer
    } else if (update_cnt == update_num_ticks) {
      update_cnt = INITAL_VAL;
      clockDisplay_performIncDec(touchscreen_get_location());
    }
    break;
  default:
    // If an unexpected state happens
    printf(error_msg);
    break;
  }

  // Perform state action second
  switch (currentState) {
  case inc_dec_st:
    clockDisplay_performIncDec(touchscreen_get_location());
    break;
  case long_press_delay_st:
    delay_cnt++;
    break;
  case fast_update_st:
    update_cnt++;
    break;
  }
}

// This is a debug state print routine. It will print the names of the states
// each time tick() is called. It only prints states if they are different than
// the previous state.
void debugClockStatePrint() {
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
    case inc_dec_st:
      printf(inc_dec_st_msg);
      break;
    case long_press_delay_st:
      printf(long_press_delay_st_msg);
      break;
    case fast_update_st:
      printf(fast_update_st_msg);
      break;
    }
  }
}

#endif /* CLOCKCONTROL */
