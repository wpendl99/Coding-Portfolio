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

#include <stdint.h>

#define INTERRUPTS_TIMER_0_IRQ 0
#define INTERRUPTS_TIMER_0_MASK 0x01

#define INTERRUPTS_TIMER_1_IRQ 1
#define INTERRUPTS_TIMER_1_MASK 0x02

#define INTERRUPTS_TIMER_2_IRQ 2
#define INTERRUPTS_TIMER_2_MASK 0x04

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
void interrupts_init();

// Register a callback function (fcn) for a given interrupt input number (irq).
void interrupts_register(uint8_t irq, void (*fcn)());

// Enable interrupt line(s)
// irq_mask: Bitmask of interrupt lines to enable
// This function only enables interrupt lines, it does not disable them.
// (bits with '0' value in the mask will not disable the interrupt line)
// Hint: The AXI Intc has a register specifically to handle this behavior.
void interrupts_irq_enable(uint8_t irq_mask);

// Disable interrupt line(s)
// irq_mask: Bitmask of interrupt lines to disable
// This function only disables interrupt lines, it does not enable them.
// Hint: The AXI Intc has a register specifically to handle this behavior.
void interrupts_irq_disable(uint8_t irq_mask);

// Acknowledge interrupt line(s)
// irq_mask: Bitmask of interrupt lines to acknowledge and clear.
void interrupts_ack(uint8_t irq_mask);

#endif /* INTERRUPTS */
