/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#include <stdint.h>
#include <stdio.h>

#include "intervalTimer.h"
#include "utils.h"
#include "xil_io.h"
#include "xparameters.h"

#define MILESTONE_1 1
#define MILESTONE_2 2

////////////////////////////////////////////////////////////////////////////////
// Uncomment one of the following lines to run Milestone 1 or 2      ///////////
////////////////////////////////////////////////////////////////////////////////
// #define RUN_PROGRAM MILESTONE_1
// #define RUN_PROGRAM MILESTONE_2

#ifndef RUN_PROGRAM
#define RUN_PROGRAM MILESTONE_2
#endif

#define MILESTONE_1_MSG "Running milestone 1.\n"
#define MILESTONE_2_MSG "Running milestone 2.\n"

#define TCSR0_OFFSET 0x00
#define TCR0_OFFSET 0x08 // register offset for TCR0
#define TCR1_OFFSET 0x18 // register offset for TCR1

#define TCSR_INT_BIT 0x0100
#define TCSR_ENIT_BIT 0x0040

// Reads the timer1 registers based upon the offset.
uint32_t readTimer0Reg(uint32_t registerOffset) {
  uint32_t address = XPAR_AXI_TIMER_0_BASEADDR +
                     registerOffset; // Add the offset to the base address.
  return Xil_In32(address);          // Read the register at that address.
}
uint32_t readTimer1Reg(uint32_t registerOffset) {
  uint32_t address = XPAR_AXI_TIMER_1_BASEADDR +
                     registerOffset; // Add the offset to the base address.
  return Xil_In32(address);          // Read the register at that address.
}
uint32_t readTimer2Reg(uint32_t registerOffset) {
  uint32_t address = XPAR_AXI_TIMER_2_BASEADDR +
                     registerOffset; // Add the offset to the base address.
  return Xil_In32(address);          // Read the register at that address.
}

#define MS_PER_S 1000

#define M1_SHORT_DELAY_MS 10
#define M1_REPEAT 5
#define M1_MED_DELAY_SECONDS 5
#define M1_ROLLOVER_DELAY_S 40

#define M1_DOWN_PERIOD_S 5
#define M2_DOWN_PERIOD_S 5
#define DELAY_500MS 500
#define DELAY_1S 1000

#define M2_FIRST_DELAY_SECONDS 4
#define M2_SECOND_DELAY_SECONDS 2

// #define TEST_ITERATION_COUNT 4
// #define ONE_SECOND_DELAY 1000

// Test UP Counter for Timer 0
void testUpCounter() {
  printf("Calling `initCountUp` to initialize the timer.\n");
  intervalTimer_initCountUp(INTERVAL_TIMER_0);
  printf("timer_0 TCR0 should be 0: %u\n", readTimer0Reg(TCR0_OFFSET));

  // Start timer 0.
  printf("Calling `start` to start the timer.\n");
  intervalTimer_start(INTERVAL_TIMER_0);

  // Show that the timer is running.
  printf("Reading TCR0 with %d ms delays.\n", M1_SHORT_DELAY_MS);

  // Just checking multiple times to see if the timer is running.
  for (int32_t i = 0; i < M1_REPEAT; i++) {
    utils_msDelay(M1_SHORT_DELAY_MS);
    printf("timer_0 TCR0 (which should be increasing): %10u\n",
           readTimer0Reg(TCR0_OFFSET));
  }

  printf("Calling `reload` to set the timer back to 0.\n");
  intervalTimer_reload(INTERVAL_TIMER_0);

  // Show that the timer is running.
  printf("Reading TCR0 with %d ms delays.\n", M1_SHORT_DELAY_MS);

  // Just checking multiple times to see if the timer is running.
  for (int32_t i = 0; i < M1_REPEAT; i++) {
    utils_msDelay(M1_SHORT_DELAY_MS);
    printf("timer_0 TCR0 (which should be increasing): %10u\n",
           readTimer0Reg(TCR0_OFFSET));
  }

  printf("Testing conversion to seconds; wait until it has been ~%d "
         "seconds since timer was started.\n",
         M1_MED_DELAY_SECONDS);
  utils_msDelay((M1_MED_DELAY_SECONDS * MS_PER_S) -
                (M1_SHORT_DELAY_MS - M1_REPEAT));
  printf("`getTotalDurationInSeconds`: %f\n",
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));

  printf("Calling `stop` and waiting another %d seconds.  Make sure the timer "
         "hasn't changed much.\n",
         M1_MED_DELAY_SECONDS);
  intervalTimer_stop(INTERVAL_TIMER_0);
  utils_msDelay(M1_MED_DELAY_SECONDS * MS_PER_S);
  printf("`getTotalDurationInSeconds`: %f\n",
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
}

// Test rollover for up counter
void testUpRollover() {
  printf(
      "Testing cascade mode; call `start` and then wait another ~%d seconds.\n",
      M1_ROLLOVER_DELAY_S);
  intervalTimer_start(INTERVAL_TIMER_0);
  utils_msDelay(M1_ROLLOVER_DELAY_S * MS_PER_S);

  printf("timer_0 TCR0 value after wait:          %10u\n",
         readTimer0Reg(TCR0_OFFSET));

  // Check upper register.
  printf("timer_0 TCR1 should be 1 at this point: %10u\n",
         readTimer0Reg(TCR1_OFFSET));

  printf("Value returned by `getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M1_MED_DELAY_SECONDS + M1_ROLLOVER_DELAY_S,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
}

void testDownCounter() {
  printf("Calling `initCountDown` to initialize the timer.\n");
  intervalTimer_initCountDown(INTERVAL_TIMER_0, M1_DOWN_PERIOD_S);

  printf("Calling `start` to start the timer.\n");
  intervalTimer_start(INTERVAL_TIMER_0);

  utils_msDelay(DELAY_500MS);
  printf("timer_0 TCR0 should be decreasing at this point:    %10u\n",
         readTimer0Reg(TCR0_OFFSET));
  utils_msDelay(DELAY_1S);
  printf("timer_0 TCR0 should be decreasing at this point:    %10u\n",
         readTimer0Reg(TCR0_OFFSET));
  utils_msDelay(DELAY_1S);
  printf("timer_0 TCR0 should be decreasing at this point:    %10u\n",
         readTimer0Reg(TCR0_OFFSET));
  utils_msDelay(DELAY_1S);
  printf("timer_0 TCR0 should be decreasing at this point:    %10u\n",
         readTimer0Reg(TCR0_OFFSET));
  utils_msDelay(DELAY_1S);
  printf("timer_0 TCR0 should be decreasing at this point:    %10u\n",
         readTimer0Reg(TCR0_OFFSET));
  utils_msDelay(DELAY_1S);
  printf("timer_0 TCR0 should have rolled over at this point: %10u\n",
         readTimer0Reg(TCR0_OFFSET));
}

// Milestone 1 test
void milestone1() {
  printf("=============== Starting milestone 1 ===============\n");

  // Test UP Counter for Timer 0
  printf("******************************************\n");
  printf("***** Testing UP Counter for Timer 0 *****\n");
  printf("******************************************\n");
  testUpCounter();

  printf("******************************************\n");
  printf("***** Testing UP Counter Rollover ********\n");
  printf("******************************************\n");
  // Test rollover - you may want to comment this out once you have it working
  // as it takes a while.
  testUpRollover();

  printf("******************************************\n");
  printf("***** Testing DOWN Counter for Timer 0 ***\n");
  printf("******************************************\n");
  testDownCounter();

  printf("=============== Done milestone 1 ===============\n");
}

// Test UP Counter for Timer 0
void testUpCounterAll() {
  printf("Calling `initCountUp` to initialize all the timers.\n");
  intervalTimer_initCountUp(INTERVAL_TIMER_0);
  intervalTimer_initCountUp(INTERVAL_TIMER_1);
  intervalTimer_initCountUp(INTERVAL_TIMER_2);

  // Start timer 0.
  printf("Calling `start` to start all the timers.\n");
  intervalTimer_start(INTERVAL_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_1);
  intervalTimer_start(INTERVAL_TIMER_2);

  printf("Waiting for %d seconds.\n", M2_FIRST_DELAY_SECONDS);
  utils_msDelay(M2_FIRST_DELAY_SECONDS * MS_PER_S);

  printf("`getTotalDurationInSeconds` for timer 0 (should be ~%d): %f\n",
         M2_FIRST_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
  printf("`getTotalDurationInSeconds` for timer 1 (should be ~%d): %f\n",
         M2_FIRST_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_1));
  printf("`getTotalDurationInSeconds` for timer 2 (should be ~%d): %f\n",
         M2_FIRST_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_2));

  printf("Calling `reload` and waiting another %d seconds\n",
         M2_SECOND_DELAY_SECONDS);
  intervalTimer_reload(INTERVAL_TIMER_0);
  intervalTimer_reload(INTERVAL_TIMER_1);
  intervalTimer_reload(INTERVAL_TIMER_2);
  utils_msDelay(M2_SECOND_DELAY_SECONDS * MS_PER_S);
  printf("`getTotalDurationInSeconds` for timer 0 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
  printf("`getTotalDurationInSeconds` for timer 1 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_1));
  printf("`getTotalDurationInSeconds` for timer 2 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_2));

  printf("Calling `stop` and waiting another %d seconds.  Make sure the timer "
         "hasn't changed much.\n",
         M2_SECOND_DELAY_SECONDS);
  intervalTimer_stop(INTERVAL_TIMER_0);
  intervalTimer_stop(INTERVAL_TIMER_1);
  intervalTimer_stop(INTERVAL_TIMER_2);

  printf("`getTotalDurationInSeconds` for timer 0 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
  printf("`getTotalDurationInSeconds` for timer 1 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_1));
  printf("`getTotalDurationInSeconds` for timer 2 (should be ~%d): %f\n",
         M2_SECOND_DELAY_SECONDS,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_2));

  printf(
      "Testing cascade mode; call `start` and then wait another ~%d seconds.\n",
      M1_ROLLOVER_DELAY_S);
  intervalTimer_start(INTERVAL_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_1);
  intervalTimer_start(INTERVAL_TIMER_2);

  utils_msDelay(M1_ROLLOVER_DELAY_S * MS_PER_S);

  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_SECOND_DELAY_SECONDS + M1_ROLLOVER_DELAY_S,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_SECOND_DELAY_SECONDS + M1_ROLLOVER_DELAY_S,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_1));
  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_SECOND_DELAY_SECONDS + M1_ROLLOVER_DELAY_S,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_2));
}

void testDownCounterAll() {
  printf("Calling `initCountDown` to initialize all the timers with a %ds "
         "period.\n",
         M2_DOWN_PERIOD_S);
  intervalTimer_initCountDown(INTERVAL_TIMER_0, M2_DOWN_PERIOD_S);
  intervalTimer_initCountDown(INTERVAL_TIMER_1, M2_DOWN_PERIOD_S);
  intervalTimer_initCountDown(INTERVAL_TIMER_2, M2_DOWN_PERIOD_S);

  printf("Verifying interrupt enable (ENIT) bit is 0 after init on all "
         "timers:\n");
  printf("Timer 0 TCSR0 ENIT (should be 0): %d\n",
         (readTimer0Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 1 TCSR0 ENIT (should be 0): %d\n",
         (readTimer1Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 2 TCSR0 ENIT (should be 0): %d\n",
         (readTimer2Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);

  printf("Calling `enableInterrupt` on all timers.\n");
  intervalTimer_enableInterrupt(INTERVAL_TIMER_0);
  intervalTimer_enableInterrupt(INTERVAL_TIMER_1);
  intervalTimer_enableInterrupt(INTERVAL_TIMER_2);

  printf("Verifying ENIT bit is set on all timers:\n");
  printf("Timer 0 TCSR0 ENIT (should be 1): %d\n",
         (readTimer0Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 1 TCSR0 ENIT (should be 1): %d\n",
         (readTimer1Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 2 TCSR0 ENIT (should be 1): %d\n",
         (readTimer2Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);

  printf("Calling `disableInterrupt` on all timers:\n");
  intervalTimer_disableInterrupt(INTERVAL_TIMER_0);
  intervalTimer_disableInterrupt(INTERVAL_TIMER_1);
  intervalTimer_disableInterrupt(INTERVAL_TIMER_2);

  printf("Verifying interrupt enable (ENIT) bit has been cleared on all "
         "timers:\n");
  printf("Timer 0 TCSR0 ENIT (should be 0): %d\n",
         (readTimer0Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 1 TCSR0 ENIT (should be 0): %d\n",
         (readTimer1Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);
  printf("Timer 2 TCSR0 ENIT (should be 0): %d\n",
         (readTimer2Reg(TCSR0_OFFSET) & TCSR_ENIT_BIT) == TCSR_ENIT_BIT);

  printf("Calling `start` to start all the timers.\n");
  intervalTimer_start(INTERVAL_TIMER_0);
  intervalTimer_start(INTERVAL_TIMER_1);
  intervalTimer_start(INTERVAL_TIMER_2);

  printf("Waiting for %d seconds.\n", M2_DOWN_PERIOD_S + 1);
  utils_msDelay((M2_DOWN_PERIOD_S + 1) * MS_PER_S);

  printf("Verifying that timers restarted:\n");
  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_DOWN_PERIOD_S - 1,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_0));
  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_DOWN_PERIOD_S - 1,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_1));
  printf("`getTotalDurationInSeconds` (should be ~%d): "
         "%f\n",
         M2_DOWN_PERIOD_S - 1,
         intervalTimer_getTotalDurationInSeconds(INTERVAL_TIMER_2));

  printf("Verifying that rollover occurred:\n");
  printf("Timer 0 TCSR0 INT_BIT (should be 1): %d\n",
         (readTimer0Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);
  printf("Timer 1 TCSR0 INT_BIT (should be 1): %d\n",
         (readTimer1Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);
  printf("Timer 2 TCSR0 INT_BIT (should be 1): %d\n",
         (readTimer2Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);

  printf("Calling `ackInterrupt` on all timers.\n");
  intervalTimer_ackInterrupt(INTERVAL_TIMER_0);
  intervalTimer_ackInterrupt(INTERVAL_TIMER_1);
  intervalTimer_ackInterrupt(INTERVAL_TIMER_2);

  printf(
      "Verifying that rollover/interrupt output was successfully cleared:\n");
  printf("Timer 0 TCSR0 INT_BIT (should be 0): %d\n",
         (readTimer0Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);
  printf("Timer 1 TCSR0 INT_BIT (should be 0): %d\n",
         (readTimer1Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);
  printf("Timer 2 TCSR0 INT_BIT (should be 0): %d\n",
         (readTimer2Reg(TCSR0_OFFSET) & TCSR_INT_BIT) == TCSR_INT_BIT);
}

// Mileston 2 test function
void milestone2() {
  printf("=============== Starting milestone 2 ===============\n");

  printf("******************************************\n");
  printf("***** Testing UP Counter for All Timers **\n");
  printf("******************************************\n");
  testUpCounterAll();

  printf("******************************************\n");
  printf("***** Testing Down Counter & Interrupts for All Timers \n");
  printf("******************************************\n");
  testDownCounterAll();

  printf("=============== Done milestone 2 ===============\n");
}

// main executes both milestones.
int main() {

#if (RUN_PROGRAM == MILESTONE_1)
  printf(MILESTONE_1_MSG);
  milestone1(); // Execute milestone 1
#elif (RUN_PROGRAM == MILESTONE_2)
  printf(MILESTONE_2_MSG);
  milestone2(); // Execute milestone 2
#endif
  return 0;
}
