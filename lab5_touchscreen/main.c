#include <stdio.h>

#include "arm_interrupts.h"
#include "interrupts.h"
#include "intervalTimer.h"
#include "touchscreen.h"
#include "utils.h"

#define DELAY_MS 10

volatile bool press_detected = false;
volatile display_point_t point;

void isr() {
  // Acknowledge interrupt
  intervalTimer_ackInterrupt(INTERVAL_TIMER_TIMER_0);
  interrupts_ack(INTERRUPTS_IRQ_TIMER_0);

  // Repeatedly tick the touch screen state machine
  touchscreen_tick();

  // Get the current status of the touchscreen
  enum touchscreen_status_e status = touchscreen_get_status();

  if (!press_detected && status != TOUCHSCREEN_IDLE) {
    // New button press detected, draw filled circle
    point = touchscreen_get_location();
    press_detected = true;
    display_fillCircle(point.x, point.y, 40, DISPLAY_RED);
  }

  if (press_detected && status == TOUCHSCREEN_RELEASED) {
    // Press released, draw empty circle and acknowledge press
    press_detected = false;
    display_fillCircle(point.x, point.y, 40, DISPLAY_BLACK);
    display_drawCircle(point.x, point.y, 40, DISPLAY_RED);
    touchscreen_ack_touch();
  }
}

int main() {
  printf("========== Starting touchscreen test program ==========");

  // Initialize drivers
  display_init();
  interrupts_init();
  touchscreen_init(DELAY_MS);

  // Fill screen black
  display_fillScreen(DISPLAY_BLACK);

  // Set up interrupts
  interrupts_register(INTERRUPTS_IRQ_TIMER_0, isr);
  intervalTimer_initCountDown(INTERVAL_TIMER_TIMER_0, 0.01);

  while (1) {
    utils_msDelay(1);
  }
  return 0;
}