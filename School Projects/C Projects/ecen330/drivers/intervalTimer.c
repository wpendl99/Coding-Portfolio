#ifndef INTERVALTIMER
#define INTERVALTIMER

#include "xil_io.h"
#include "xparameters.h"
#include <stdbool.h>
#include <stdint.h>

#define INTERVAL_TIMER_0 0
#define INTERVAL_TIMER_1 1
#define INTERVAL_TIMER_2 2

#define INTERVAL_TIMER_0_INTERRUPT_IRQ 0
#define INTERVAL_TIMER_1_INTERRUPT_IRQ 1
#define INTERVAL_TIMER_2_INTERRUPT_IRQ 2

// Bit Masks
#define CASC_MSK 0x0800 // Cascade mode (TLR0 is lower 32, TLR1 is upper 32-bit)
#define TINT_MSK 0x0100 // Timer Interrupt (To clear, write a 1)
#define ENT_MSK 0x0080  // Enable Timer
#define ENIT_MSK 0x0040 // Enable Interrupt
#define LOAD_MSK 0x0020 // Load Timer (While 1, loads timer with TLR)
#define ARHT_MSK 0x0010 // Auto Reload/Hold Timer
#define UDT_MSK 0x0002  // Up/Down Count Timer (0 is up, 1 is down)
#define MDT_MSK 0x0001  // Timer Mode (Set to 0 for generate mode)

// Const Interval Timer Values
const uint32_t INTERVAL_TIMER_BASE_ADDRESS[] = {XPAR_AXI_TIMER_0_BASEADDR,
                                                XPAR_AXI_TIMER_1_BASEADDR,
                                                XPAR_AXI_TIMER_2_BASEADDR};
const uint32_t INTERVAL_TIMER_CLOCK_FREQ_HZ[] = {
    XPAR_AXI_TIMER_0_CLOCK_FREQ_HZ, XPAR_AXI_TIMER_1_CLOCK_FREQ_HZ,
    XPAR_AXI_TIMER_2_CLOCK_FREQ_HZ};

// Hardware Register Offset
#define TCSR0 0x00 // Control/Status Register 0
#define TLR0 0x04  // Load Register 0
#define TCR0 0x08  // Timer/Counter Register 0
#define TCSR1 0x10 // Control/Status Register 1
#define TLR1 0x14  // Load Register 1
#define TCR1 0x18  // Timer/Coutner Register 1

#define ALL_OFF 0x00
#define UPPER_BIT_SHIFT 32

static uint32_t readRegister(uint32_t address);
static void writeRegister(uint32_t address, uint32_t value);
void intervalTimer_reload(uint32_t timerNumber);

// You must configure the interval timer before you use it:
void intervalTimer_initCountUp(uint32_t timerNumber) {
  // 1. Configure Timer Control/Status Registers
  // Configuration for TCSR0:
  // - Cascade Mode on
  // - Auto Reload/Hold timer on
  // - Count Up Mode
  uint32_t tcsr0_config_msk = CASC_MSK | ARHT_MSK;
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0, ALL_OFF);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                tcsr0_config_msk);

  // Configuration for TCSR1:
  // - Cascade Mode on
  uint32_t tcsr1_config_msk = CASC_MSK;
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR1, ALL_OFF);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR1,
                tcsr1_config_msk);

  // 2. Initialize both LOAD registers with zeros
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TLR0, ALL_OFF);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TLR1, ALL_OFF);

  // 3. Call the _reload function to move the LOAD values into the Counters
  intervalTimer_reload(timerNumber);
}

// You must configure the interval timer before you use it:
void intervalTimer_initCountDown(uint32_t timerNumber, double period) {
  // 1. Configure Timer Control/Status Registers
  // Configuration for TCSR0:
  // - Cascade Mode on
  // - Auto Reload/Hold timer on
  // - Count Down Mode
  uint32_t tcsr0_config_msk = CASC_MSK | ARHT_MSK | UDT_MSK;
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0, ALL_OFF);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                tcsr0_config_msk);

  // Configuration for TCSR1:
  // - Cascade Mode on
  uint32_t tcsr1_config_msk = CASC_MSK;
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR1, ALL_OFF);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR1,
                tcsr1_config_msk);

  // 2. Initialize both LOAD registers with period
  uint64_t seconds = INTERVAL_TIMER_CLOCK_FREQ_HZ[timerNumber] * period;
  uint32_t upper_bits = seconds >> UPPER_BIT_SHIFT;
  uint32_t lower_bits = seconds;
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TLR0, lower_bits);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TLR1, upper_bits);

  // 3. Call the _reload function to move the LOAD values into the Counters
  intervalTimer_reload(timerNumber);
}

// This function starts the interval timer running.
// If the interval timer is already running, this function does nothing.
// timerNumber indicates which timer should start running.
// Make sure to only change the Enable Timer bit of the register and not modify
// the other bits.
void intervalTimer_start(uint32_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value | ENT_MSK);
}

// This function stops a running interval timer.
// If the interval time is currently stopped, this function does nothing.
// timerNumber indicates which timer should stop running.
// Make sure to only change the Enable Timer bit of the register and not modify
// the other bits.
void intervalTimer_stop(uint32_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value & ~ENT_MSK);
}

// This function is called whenever you want to reload the Counter values
// from the load registers.  For a count-up timer, this will reset the
// timer to zero.  For a count-down timer, this will reset the timer to
// its initial count-down value.  The load registers should have already
// been set in the appropriate `init` function, so there is no need to set
// them here.  You just need to enable the load (and remember to disable it
// immediately after otherwise you will be loading indefinitely).
void intervalTimer_reload(uint32_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  // Turn on load
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value | LOAD_MSK);
  // Turn off load
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value & ~LOAD_MSK);
}

// Use this function to ascertain how long a given timer has been running.
// Note that it should not be an error to call this function on a running timer
// though it usually makes more sense to call this after intervalTimer_stop()
// has been called. The timerNumber argument determines which timer is read.
// In cascade mode you will need to read the upper and lower 32-bit registers,
// concatenate them into a 64-bit counter value, and then perform the conversion
// to a double seconds value.
double intervalTimer_getTotalDurationInSeconds(uint32_t timerNumber) {
  // Load upper half, shift to proper spot, then load lower
  uint64_t total_time =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCR1);
  total_time <<= UPPER_BIT_SHIFT;
  total_time |= readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCR0);
  return (double)total_time / INTERVAL_TIMER_CLOCK_FREQ_HZ[timerNumber];
}

// Enable the interrupt output of the given timer.
void intervalTimer_enableInterrupt(uint8_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value | ENIT_MSK);
}

// Disable the interrupt output of the given timer.
void intervalTimer_disableInterrupt(uint8_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value & ~ENIT_MSK);
}

// Acknowledge the rollover to clear the interrupt output.
void intervalTimer_ackInterrupt(uint8_t timerNumber) {
  uint32_t cur_register_value =
      readRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0);
  writeRegister(INTERVAL_TIMER_BASE_ADDRESS[timerNumber] + TCSR0,
                cur_register_value | TINT_MSK);
}

// Returns the current value of the of the data stored at the given address
static uint32_t readRegister(uint32_t address) { return Xil_In32(address); }

// Sets the current value of the of the data stored at the given address
static void writeRegister(uint32_t address, uint32_t value) {
  Xil_Out32(address, value);
}

#endif /* INTERVALTIMER */
