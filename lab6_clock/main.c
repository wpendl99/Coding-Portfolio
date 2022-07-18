/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>

#include "armInterrupts.h"
#include "clockControl.h"
#include "clockDisplay.h"
#include "config.h"
#include "display.h"
#include "interrupts.h"
#include "intervalTimer.h"
#include "leds.h"
#include "touchscreen.h"
#include "utils.h"

#define MILESTONE_1 1
#define MILESTONE_2 2

////////////////////////////////////////////////////////////////////////////////
// Uncomment one of the following lines to run Milestone 1 or 2      ///////////
////////////////////////////////////////////////////////////////////////////////
// #define RUN_PROGRAM MILESTONE_1
// #define RUN_PROGRAM MILESTONE_2

// If nothing is uncommented above, run milestone 2
#ifndef RUN_PROGRAM
#define RUN_PROGRAM MILESTONE_2
#endif

#define RUN_DISPLAY_TEST_MSG "========== Running Milestone 1 ==========\n"
#define RUN_MILESTONE_2_MSG "Running Milestone 2: full clock lab\n"

// Keep track of how many times isr_function() is called.
uint32_t isr_functionCallCount = 0;

extern volatile int interrupt_occurred;

void isr() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_0);
  clockControl_tick();
  touchscreen_tick();
}

void isr_1s() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_1);

  touchscreen_status_t ts_status = touchscreen_get_status();
  if (ts_status != TOUCHSCREEN_PRESSED) {
    clockDisplay_advanceTimeOneSecond();
  }
}

// This main uses isr_function() to invoked clockControl_tick().
int main() {
#if (RUN_PROGRAM == MILESTONE_1)

  printf(RUN_DISPLAY_TEST_MSG);

  printf("========== Milestone done ==========\n");

#elif (RUN_PROGRAM == MILESTONE_2)
  // This main() uses the flag method to invoke clockControl_tick().

  printf(RUN_MILESTONE_2_MSG);
  leds_init();

  clockDisplay_init();
  clockControl_init(CONFIG_TIMER_PERIOD);
  touchscreen_init(CONFIG_TIMER_PERIOD);

  interrupts_init();
  interrupts_register(INTERVAL_TIMER_0_INTERRUPT_IRQ, isr);
  interrupts_register(INTERVAL_TIMER_1_INTERRUPT_IRQ, isr_1s);
  interrupts_irq_enable(INTERVAL_TIMER_0_INTERRUPT_MASK |
                        INTERVAL_TIMER_1_INTERRUPT_MASK);

  intervalTimer_initCountDown(INTERVAL_TIMER_0, CONFIG_TIMER_PERIOD);
  intervalTimer_initCountDown(INTERVAL_TIMER_1, 1.0);

  intervalTimer_enableInterrupt(INTERVAL_TIMER_0);
  intervalTimer_enableInterrupt(INTERVAL_TIMER_1);

  intervalTimer_start(INTERVAL_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_1);

  while (1)
    ;
#endif
  return 0;
}
