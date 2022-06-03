/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef DRIVERS_INTERRUPTS
#define DRIVERS_INTERRUPTS

#include <stdint.h>

#define INTERRUPTS_IRQ_TIMER_0 0
#define INTERRUPTS_IRQ_TIMER_1 1
#define INTERRUPTS_IRQ_TIMER_2 2

// Initialize interrupt hardware
int32_t interrupts_init();

// Enable interrupt line(s)
// irq_mask: Bitmask of lines to enable
// This function only enables interrupt lines, ie, a 0 bit in irq_mask
//	will not disable the interrupt line
void intc_irq_enable(uint8_t irq_mask);

void intc_register(uint8_t irq, void (*fcn)());

void intc_isr();

#endif /* DRIVERS_INTERRUPTS */
