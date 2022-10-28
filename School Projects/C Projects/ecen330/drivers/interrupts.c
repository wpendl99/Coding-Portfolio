/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef INTERRUPTS
#define INTERRUPTS

#include "armInterrupts.h"
#include "intervalTimer.h"
#include "xil_io.h"
#include <stdint.h>
#include <stdio.h>
#include <xparameters.h>

#define INTERRUPT_CONTROLLER_BASE_ADDR XPAR_AXI_INTC_0_BASEADDR
#define INTERRUPT_INPUT_COUNT 3

static void (*isrFcnPtrs[3])() = {NULL};

// Bit Masks
#define MER_HIE_MSK 0x02  // MER Hardware Inteerrupt Enable Mask
#define MER_ME_MSK 0x01   // MER Master IRQ Enable Mask
#define IRQ_BASE_MSK 0x01 // Used along side with uint8_t irq to shift bits

// Hardware Register Offset
#define IPR 0x04 // Interrupt Pending Register
#define IER 0x08 // Interrup Enable Register
#define IAR 0x0C // Interrupt Acknowledge Register
#define SIE 0x10 // SET Interrupt Enables
#define CIE 0x14 // Clear Interrupt Enables
#define MER 0x1C // Master Enable Register

// Function Declarations
static void interrupts_isr();
void interrupts_irq_disable(uint8_t irq);
static uint32_t readRegister(uint32_t address);
static void writeRegister(uint32_t address, uint32_t value);

// Initialize interrupt hardware
// This function should:
// 1. Configure AXI INTC registers to:
//  - Enable interrupt output (see Master Enable Register)
//  - Disable all interrupt input lines.
// 2. Enable the Interrupt system on the ARM processor, and register an ISR
// handler function. This is done by calling:
//  - armInterrupts_init()
//  - armInterrupts_setupIntc(isr_fcn_ptr)
//  - armInterrupts_enable()
void interrupts_init() {
  // Step 1
  writeRegister(INTERRUPT_CONTROLLER_BASE_ADDR + MER, MER_HIE_MSK | MER_ME_MSK);
  // Disable all of the Interrupt input lines
  for (uint8_t i = 0; i < INTERRUPT_INPUT_COUNT; i++) {
    interrupts_irq_disable(i);
  }

  // Step 2
  armInterrupts_init();
  armInterrupts_setupIntc(interrupts_isr);
  armInterrupts_enable();
}

// Register a callback function (fcn is a function pointer to this callback
// function) for a given interrupt input number (irq).  When this interrupt
// input is active, fcn will be called.
void interrupts_register(uint8_t irq, void (*fcn)()) { isrFcnPtrs[irq] = fcn; }

// Enable single input interrupt line, given by irq number.
void interrupts_irq_enable(uint8_t irq) {
  writeRegister(INTERRUPT_CONTROLLER_BASE_ADDR + SIE, IRQ_BASE_MSK << irq);
}

// Disable single input interrupt line, given by irq number.
void interrupts_irq_disable(uint8_t irq) {
  writeRegister(INTERRUPT_CONTROLLER_BASE_ADDR + CIE, IRQ_BASE_MSK << irq);
}

// Inturrupt Service Routine Function that gets passed to the ARM setupIntc
static void interrupts_isr() {
  // Loop through every interrupt timer and see if it has a pending interupt
  for (uint8_t i = 0; i < INTERRUPT_INPUT_COUNT; i++) {
    uint32_t curr_ints_pending =
        readRegister(INTERRUPT_CONTROLLER_BASE_ADDR + IPR);

    // Check to see if the coresponding interupt timer has a pending interupt,
    // if so, run the coresponding isr
    if (curr_ints_pending & (IRQ_BASE_MSK << i)) {
      // If there is a ISR function, run it
      if (isrFcnPtrs[i]) {
        isrFcnPtrs[i]();
      }

      // Acknowledge interrupt
      writeRegister(INTERRUPT_CONTROLLER_BASE_ADDR + IAR, (IRQ_BASE_MSK << i));
    }
  }
}

// Returns the current value of the of the data stored at the given address
static uint32_t readRegister(uint32_t address) { return Xil_In32(address); }

// Sets the current value of the of the data stored at the given address
static void writeRegister(uint32_t address, uint32_t value) {
  Xil_Out32(address, value);
}

#endif /* INTERRUPTS */
