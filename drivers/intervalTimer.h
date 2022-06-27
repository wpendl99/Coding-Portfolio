/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

// Provides an API for accessing the three hardware timers that are installed
// in the ZYNQ fabric.

#ifndef INTERVALTIMER
#define INTERVALTIMER

#include <stdbool.h>
#include <stdint.h>

// Used to indicate status that can be checked after invoking the function.
typedef uint32_t
    intervalTimer_status_t; // Use this type for the return type of a function.

#define INTERVAL_TIMER_STATUS_OK 1   // Return this status if successful.
#define INTERVAL_TIMER_STATUS_FAIL 0 // Return this status if failure.

#define INTERVAL_TIMER_TIMER_0 0
#define INTERVAL_TIMER_TIMER_1 1
#define INTERVAL_TIMER_TIMER_2 2

// You must configure the interval timer before you use it:
// 1. Set the Timer Control/Status Registers such that:
//  - The timer is in 64-bit cascade mode
//  - The timer counts up
// 2. Initialize both LOAD registers with zeros
// 3. Call the _reload function to move the LOAD values into the Counters
intervalTimer_status_t intervalTimer_initCountUp(uint32_t timerNumber);

// You must configure the interval timer before you use it:
// 1. Set the Timer Control/Status Registers such that:
//  - The timer is in 64-bit cascade mode
//  - The timer counts down
//  - The timer automatically reloads when reaching zero
// 2. Initialize LOAD registers with appropraite values, given the `period`.
// 3. Call the _reload function to move the LOAD values into the Counters
intervalTimer_status_t intervalTimer_initCountDown(uint32_t timerNumber,
                                                   double period);

// This function starts the interval timer running.
// If the interval timer is already running, this function does nothing.
// timerNumber indicates which timer should start running.
void intervalTimer_start(uint32_t timerNumber);

// This function stops a running interval timer.
// If the interval time is currently stopped, this function does nothing.
// timerNumber indicates which timer should stop running.
void intervalTimer_stop(uint32_t timerNumber);

// This function is called whenever you want to reload the Counter values
// from the load registers.  For a count-up timer, this will reset the
// timer to zero.  For a count-down timer, this will reset the timer to
// its initial count-down value.  The load registers should have already
// been set in the appropriate `init` function, so there is no need to set
// them here.
void intervalTimer_reload(uint32_t timerNumber);

// Use this function to ascertain how long a given timer has been running.
// Note that it should not be an error to call this function on a running timer
// though it usually makes more sense to call this after intervalTimer_stop()
// has been called. The timerNumber argument determines which timer is read.
double intervalTimer_getTotalDurationInSeconds(uint32_t timerNumber);

// Enable the interrupt output of the given timer.
void intervalTimer_enableInterrupt(uint8_t timerNumber);

// Disable the interrupt output of the given timer.
void intervalTimer_disableInterrupt(uint8_t timerNumber);

// Acknowledge the rollover to clear the interrupt output.
void intervalTimer_ackInterrupt(uint8_t timerNumber);

#endif /* INTERVALTIMER */
