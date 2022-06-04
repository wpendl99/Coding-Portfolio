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

#ifndef DRIVERS_INTERVALTIMER
#define DRIVERS_INTERVALTIMER

#include <stdint.h>

// Used to indicate status that can be checked after invoking the function.
typedef uint32_t
    intervalTimer_status_t; // Use this type for the return type of a function.

#define INTERVAL_TIMER_STATUS_OK 1   // Return this status if successful.
#define INTERVAL_TIMER_STATUS_FAIL 0 // Return this status if failure.

#define INTERVAL_TIMER_TIMER_0 0
#define INTERVAL_TIMER_TIMER_1 1
#define INTERVAL_TIMER_TIMER_2 2

// You must initialize the timers before you use them the first time.
// It is generally only called once but should not cause an error if it
// is called multiple times.
// timerNumber indicates which timer should be initialized.
// returns INTERVAL_TIMER_STATUS_OK if successful, some other value otherwise.
intervalTimer_status_t intervalTimer_initCountUp(uint32_t timerNumber);
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

// This function is called whenever you want to reuse an interval timer.
// For example, say the interval timer has been used in the past, the user
// will call intervalTimer_reset() prior to calling intervalTimer_start().
// timerNumber indicates which timer should reset.
void intervalTimer_reload(uint32_t timerNumber);

// Use this function to ascertain how long a given timer has been running.
// Note that it should not be an error to call this function on a running timer
// though it usually makes more sense to call this after intervalTimer_stop()
// has been called. The timerNumber argument determines which timer is read.
double intervalTimer_getTotalDurationInSeconds(uint32_t timerNumber);

void intervalTimer_enableInterrupt(uint8_t timerNumber);

void intervalTimer_disableInterrupt(uint8_t timerNumber);

void intervalTimer_setCountUp(uint8_t timerNumber);

void intervalTimer_setCountDown(uint8_t timerNumber);

#endif /* DRIVERS_INTERVALTIMER */
