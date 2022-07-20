#include <stdio.h>

#define MISSILE_COMMAND_PART2

#include "config.h"
#include "gameControl.h"
#include "interrupts.h"
#include "intervalTimer.h"

#define RUNTIME_S 20
#define RUNTIME_TICKS ((int)(RUNTIME_S / CONFIG_TIMER_PERIOD))

volatile bool interrupt_flag;

uint32_t isr_triggered_count;
uint32_t isr_handled_count;

void isr() {
  intervalTimer_ackInterrupt(INTERVAL_TIMER_0);
  interrupt_flag = true;
  isr_triggered_count++;
}

int main() {
  interrupt_flag = false;
  isr_triggered_count = 0;
  isr_handled_count = 0;

  gameControl_init();

  // Initialize timer interrupts
  interrupts_init();
  interrupts_register(INTERVAL_TIMER_0_INTERRUPT_IRQ, isr);
  interrupts_irq_enable(INTERVAL_TIMER_0_INTERRUPT_IRQ);
  interrupts_irq_enable(INTERVAL_TIMER_1_INTERRUPT_IRQ);

  intervalTimer_initCountDown(INTERVAL_TIMER_0, CONFIG_TIMER_PERIOD);
  intervalTimer_enableInterrupt(INTERVAL_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_0);

  // Main game loop
  while (isr_triggered_count < RUNTIME_TICKS) {
    while (!interrupt_flag)
      ;
    interrupt_flag = false;
    isr_handled_count++;

    gameControl_tick();
  }
  printf("Handled %d of %d interrupts\n", isr_handled_count,
         isr_triggered_count);
}