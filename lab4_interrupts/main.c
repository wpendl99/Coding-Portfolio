#include <stdbool.h>
#include <stdio.h>

#include "arm_interrupts.h"
#include "interrupt_test.h"
#include "interrupts.h"

int main() {
  // Enable the interrupt input on the ARM processor
  // coming from the AXI Interrupt Controller
  arm_interrupts_init();
  arm_interrupts_register_intc_isr(interrupts_isr);
  arm_interrupts_enable();

  // Run the interrupt test to test the driver for the AXI Interrupt Controller
  printf("Running the interrupt test\n");
  interrupt_test_run();
}