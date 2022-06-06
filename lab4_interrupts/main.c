#include <stdbool.h>
#include <stdio.h>

#include "arm_interrupts.h"
#include "interrupt_test.h"
#include "interrupts.h"

int main() {
  arm_interrupts_init();
  arm_interrupts_register_intc_isr(interrupts_isr);
  arm_interrupts_enable();

  // Run the interrupt test
  printf("Running the interrupt test\n");
  interrupt_test_run();
}