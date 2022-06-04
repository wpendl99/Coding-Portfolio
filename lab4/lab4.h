#ifndef LAB4
#define LAB4

/*
This function is a small test application of your interrupt controller.  The
goal is to use the three AXI Interval Timers to generate interrupts at different
rates (1Hz, 2Hz, 4Hz), and create interrupt handler functions that blink the
LEDs at this rate.

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
void lab4_main();

/*
In this interrupt handler for timer 0, you should:
    1. (Optionally) Print a message so you know the function is being called.
    2. Acknowledge both:
        a. The interrupt output of the Interval Timer
        b. The interrupt input of the Interrupt Controller
        (Think about which one you should do first -- the order matters!)
    3. Flip the value of LED 0.
*/
void isr_timer0();

/*
In this interrupt handler for timer 1, you should:
    1. (Optionally) Print a message so you know the function is being called.
    2. Acknowledge both:
        a. The interrupt output of the Interval Timer
        b. The interrupt input of the Interrupt Controller
        (Think about which one you should do first -- the order matters!)
    3. Flip the value of LED 1.
*/
void isr_timer1();

/*
In this interrupt handler for timer 2, you should:
    1. (Optionally) Print a message so you know the function is being called.
    2. Acknowledge both:
        a. The interrupt output of the Interval Timer
        b. The interrupt input of the Interrupt Controller
        (Think about which one you should do first -- the order matters!)
    3. Flip the value of LED 2.
*/
void isr_timer2();

#endif /* LAB4 */
