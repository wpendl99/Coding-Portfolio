#ifndef INTERRUPT_TEST
#define INTERRUPT_TEST

#include "interrupts.h"
#include "intervalTimer.h"
#include "leds.h"
#include <stdint.h>
#include <stdio.h>
#include <unistd.h>

#define TIMER_COUNT 3

#define LED_OFF 0
#define LED_ON 1

#define TIMER_FREQ_1 .1
#define TIMER_FREQ_2 1
#define TIMER_FREQ_3 10

// Bit Masks
#define IRQ_BASE_MSK 0x01 // Used along side with uint8_t irq to shift bits

// Function Declarations
static void toggle_led(uint8_t irq);
static void timer0_isr();
static void timer1_isr();
static void timer2_isr();

// Timer Frequencies
const uint32_t INTERVAL_TIMER_IRQ[] = {INTERVAL_TIMER_0_INTERRUPT_IRQ,
                                       INTERVAL_TIMER_1_INTERRUPT_IRQ,
                                       INTERVAL_TIMER_2_INTERRUPT_IRQ};
const double INTERVAL_TIMER_FREQ[] = {TIMER_FREQ_1, TIMER_FREQ_2, TIMER_FREQ_3};
static void (*INTERVAL_TIMER_INTERRUPT_ISR[3])() = {timer0_isr, timer1_isr,
                                                    timer2_isr};

/*
This function is a small test application of your interrupt controller.  The
goal is to use the three AXI Interval Timers to generate interrupts at different
rates (10Hz, 1Hz, 0.1Hz), and create interrupt handler functions that change the
LEDs at this rate.  For example, the 1Hz interrupt will flip an LED value each
second, resulting in LED that turns on for 1 second, off for 1 second,
repeatedly.

For each interval timer:
    1. Initialize it as a count down timer with appropriate period (1s, 0.5s,
0.25s)
    2. Enable the timer's interrupt output
    3. Enable the associated interrupt input on the interrupt controller.
    4. Register an appropriate interrupt handler function (isr_timer0,
isr_timer1, isr_timer2)
    5. Start the timer

Make sure you call `interrupts_init()` first!
*/
void interrupt_test_run() {
  leds_init();
  interrupts_init();

  // Loop through all of the timers:
  // 1. InitCountDown
  // 2. Enable the Timer Interrupt
  // 3. Register the interupt function with the interupt
  // 4. enable the irq interrupt on the controller
  for (uint8_t i = 0; i < TIMER_COUNT; i++) {
    intervalTimer_initCountDown(INTERVAL_TIMER_IRQ[i], INTERVAL_TIMER_FREQ[i]);
    intervalTimer_enableInterrupt(INTERVAL_TIMER_IRQ[i]);
    interrupts_register(INTERVAL_TIMER_IRQ[i], INTERVAL_TIMER_INTERRUPT_ISR[i]);
    interrupts_irq_enable(INTERVAL_TIMER_IRQ[i]);
  }

  // Start all of the timers
  intervalTimer_start(INTERVAL_TIMER_0_INTERRUPT_IRQ);
  intervalTimer_start(INTERVAL_TIMER_1_INTERRUPT_IRQ);
  intervalTimer_start(INTERVAL_TIMER_2_INTERRUPT_IRQ);

  // Run the Program indefinietly
  while (1)
    ;
}

// Blink led_0 every 0.1HZ
static void timer0_isr() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_0_INTERRUPT_IRQ);
  toggle_led(INTERVAL_TIMER_0_INTERRUPT_IRQ);
}

// Blink led_1 every 1HZ
static void timer1_isr() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_1_INTERRUPT_IRQ);
  toggle_led(INTERVAL_TIMER_1_INTERRUPT_IRQ);
}

// Blink led_2 every 10HZ
static void timer2_isr() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_2_INTERRUPT_IRQ);
  toggle_led(INTERVAL_TIMER_2_INTERRUPT_IRQ);
}

// Helper function used by timer_isr to toggle it's corresponding LED
static void toggle_led(uint8_t irq) {
  uint8_t curr_led_val = leds_read();
  // If the irq LED is already on, turn it off; or vise versa
  if (curr_led_val & (IRQ_BASE_MSK << irq)) {
    leds_write(curr_led_val & ~(IRQ_BASE_MSK << irq));
  } else {
    leds_write(curr_led_val | (IRQ_BASE_MSK << irq));
  }
}

#endif /* INTERRUPT_TEST */
