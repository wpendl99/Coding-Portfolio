/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef ARMINTERRUPTS
#define ARMINTERRUPTS

#include <stdbool.h>
#include <stdint.h>

#include "xil_types.h"

//#include "../src/laserTag/queue.h"

// These values can be used with interrupts_getAdcInputMode() to determine the
// current mode for the ADC input.
#define INTERRUPTS_ADC_UNIPOLAR_MODE                                           \
  true // Positive input voltage 0V:1V -> 0:4095
#define INTERRUPTS_ADC_BIPOLAR_MODE                                            \
  false // Bipolar input voltage -0.5V:+0.5V -> -2048:+2047 (two's complement).
#define INTERRUPTS_ADC_DEFAULT_INPUT_MODE                                      \
  INTERRUPTS_ADC_UNIPOLAR_MODE // Change the default here.

#ifdef ZYBO_BOARD
#endif

#define INTERRUPT_CUMULATIVE_ISR_INTERVAL_TIMER_NUMBER 0

// The ZYBO board routes for inputs of the XADC to the XADC PMOD.
// Set SELECTED_XADC_CHANNEL to the desired channel.
// Channel 14 is the channel that is used for the laser-tag system.
#define XADC_AUX_CHANNEL_15                                                    \
  XSM_CH_AUX_MAX // pins JA3 (P) and JA9 (N) on the ZYBO board.
#define XADC_AUX_CHANNEL_14                                                    \
  XSM_CH_AUX_MAX - 1 // pins JA1 (P) and JA7 (N) on the ZYBO board.
#define XADC_AUX_CHANNEL_7                                                     \
  XSM_CH_AUX_MAX - 8 // pins JA2 (P) and JA8 (N) on the ZYBO board.
#define XADC_AUX_CHANNEL_6                                                     \
  XSM_CH_AUX_MAX - 9 // pins JA4 (P) and JA10 (N) on the ZYBO board.

// This line selects the correct ADC input channel for the 330 baseboard.
#define SELECTED_XADC_CHANNEL XADC_AUX_CHANNEL_14
// This line selects some other ADC input channel for the 330 baseboard.
//#define SELECTED_XADC_CHANNEL XADC_AUX_CHANNEL_15

// Uses interval timer 0 to measure time spent in ISR.
#define ENABLE_INTERVAL_TIMER_0_IN_TIMER_ISR 1

enum armInterrupts_e {
  ARM_INTERRUPTS_IRQ_AXI_INTC,
  ARM_INTERRUPTS_IRQ_ARM_TIMER,
  ARM_INTERRUPTS_IRQ_SYSMON
};

#ifdef __cplusplus
extern "C" {
#endif

// queue_data_t interrupts_popAdcQueueData();
// Inits all interrupts, which means:
// 1. Sets up the interrupt routine for ARM (GIC ISR) and does all necessary
// initialization.
// 2. Initializes all supported interrupts and connects their ISRs to the GIC
// ISR.
// 3. Enables the interrupts at the GIC, but not at the device itself.
// 4. Pretty much does everything but it does not enable the ARM interrupts or
// any of the device global interrupts. if printFailedStatusFlag is true, it
// prints out diagnostic messages if something goes awry.
int armInterrupts_init();

// Global interrupt enable/disable
void armInterrupts_enable();
void armInterrupts_disable();

// Register different interrupt handlers
int32_t armInterrupts_setupTimer(void (*isr)(), double period_seconds);
void armInterrupts_enableTimer();
void armInterrupts_disableTimer();

int32_t armInterrupts_setupIntc(void (*isr)());
void armInterrupts_enableIntc();
void armInterrupts_disableIntc();

uint32_t armInterrupts_getTimerIsrCount();

// **********************************************************
// These have not been tested since major changes
// **********************************************************

// Globally enable/disable SysMon interrupts.
int armInterrupts_enableSysMonGlobalInts();
int armInterrupts_disableSysMonGlobalInts();

// Enable End-Of-Conversion interrupts. You can use this to count how often an
// ADC conversion occurs.
int armInterrupts_enableSysMonEocInts();
int armInterrupts_disableSysMonEocInts();

// Used to determine the input mode for the ADC.
bool armInterrupts_getAdcInputMode();

// Use this to read the latest ADC conversion.
uint32_t armInterrupts_getAdcData();

// u32 interrupts_getTotalXadcSampleCount();
u32 armInterrupts_getTotalEocCount();

// Init/Enable/disable interrupts for the bluetooth radio (RDYN line).
uint32_t armInterrupts_initBluetoothInterrupts();
void armInterrupts_enableBluetoothInterrupts();
void armInterrupts_disableBluetoothInterrupts();
void armInterrupts_ackBluetoothInterrupts();

#ifdef __cplusplus
} // extern "C'
#endif

#endif /* ARMINTERRUPTS */
