/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef SWITCHES
#define SWITCHES

#include "xil_io.h"
#include "xparameters.h"
#include <stdint.h>

#define SWITCHES_SW0_MASK 0x1
#define SWITCHES_SW1_MASK 0x2
#define SWITCHES_SW2_MASK 0x4
#define SWITCHES_SW3_MASK 0x8
#define SWITCHES_ALL_MASK 0xF

// Switch input mode settings
#define SWITCHES_TRI_INPUT_MODE 0x0F

// Switch address offsets
#define SWITCHES_DATA_ADDR_OFFSET 0x00
#define SWITCHES_TRI_ADDR_OFFSET 0x04

static uint32_t readRegister(uint32_t offset);
static void writeRegister(uint32_t offset, uint32_t value);

// Initializes the SWITCHES driver software and hardware.
void switches_init() {
  writeRegister(SWITCHES_TRI_ADDR_OFFSET, SWITCHES_TRI_INPUT_MODE);
}

// Returns the current value of all 4 switches as the lower 4 bits of the
// returned value. bit3 = SW3, bit2 = SW2, bit1 = SW1, bit0 = SW0.
uint8_t switches_read() {
  return (readRegister(SWITCHES_DATA_ADDR_OFFSET) & SWITCHES_ALL_MASK);
}

// Returns the current value of the of the data stored at the given offset
// from SLIDE_SWITCHES_BASEADDR.
// Used to read the values of the switches on the ZYBO board.
static uint32_t readRegister(uint32_t offset) {
  return Xil_In32(XPAR_SLIDE_SWITCHES_BASEADDR + offset);
}

// Sets the current value of the of the data stored at the given offset
// from SLIDE_SWITCHES_BASEADDR.
// Used to set the values of the TRI Gate
static void writeRegister(uint32_t offset, uint32_t value) {
  Xil_Out32(XPAR_SLIDE_SWITCHES_BASEADDR + offset, value);
}

#endif /* SWITCHES */
