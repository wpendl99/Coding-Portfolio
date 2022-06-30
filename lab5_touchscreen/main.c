#include <stdio.h>

#include "armInterrupts.h"
#include "interrupts.h"
#include "intervalTimer.h"
#include "touchscreen.h"
#include "utils.h"

#define PERIOD_MS 10
#define CIRCLE_RADIUS 40
#define MS_PER_S 1000

volatile bool press_detected = false;
volatile display_point_t point;

void isr() {
  // Acknowledge interrupt
  intervalTimer_ackInterrupt(INTERVAL_TIMER_TIMER_0);
  interrupts_ack(1 << INTERRUPTS_TIMER_0_IRQ);

  // Repeatedly tick the touch screen state machine
  touchscreen_tick();

  // Get the current status of the touchscreen
  touchscreen_status_t status = touchscreen_get_status();

  if (!press_detected && (status != TOUCHSCREEN_IDLE)) {
    // New button press detected, draw filled circle
    point = touchscreen_get_location();
    press_detected = true;
    printf("x: %hu y: %hu\n", point.x, point.y);
    display_fillCircle(point.x, point.y, CIRCLE_RADIUS, DISPLAY_RED);
  }

  if (press_detected && (status == TOUCHSCREEN_RELEASED)) {
    // Press released, draw empty circle and acknowledge press
    press_detected = false;
    display_fillCircle(point.x, point.y, CIRCLE_RADIUS, DISPLAY_BLACK);
    display_drawCircle(point.x, point.y, CIRCLE_RADIUS, DISPLAY_RED);
    touchscreen_ack_touch();
  }
}

int main() {
  printf("========== Starting touchscreen test program ==========\n");

  // Initialize drivers
  display_init();
  interrupts_init();
  touchscreen_init(PERIOD_MS);

  // Fill screen black
  display_fillScreen(DISPLAY_BLACK);

  // Set up interrupts
  interrupts_register(INTERRUPTS_TIMER_0_IRQ, isr);

  intervalTimer_initCountDown(INTERVAL_TIMER_TIMER_0,
                              PERIOD_MS / (double)MS_PER_S);
  intervalTimer_enableInterrupt(INTERVAL_TIMER_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_TIMER_0);
  interrupts_irq_enable(1 << INTERRUPTS_TIMER_0_IRQ);

  while (1) {
  }
  return 0;
}