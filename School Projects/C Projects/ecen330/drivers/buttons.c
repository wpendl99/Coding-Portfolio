/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef BUTTONS
#define BUTTONS

#include "xil_io.h"
#include "xparameters.h"
#include <stdint.h>

#define BUTTONS_BTN0_MASK 0x1
#define BUTTONS_BTN1_MASK 0x2
#define BUTTONS_BTN2_MASK 0x4
#define BUTTONS_BTN3_MASK 0x8
#define BUTTONS_ALL_MASK 0xF

// Button input mode settings
#define BUTTONS_TRI_INPUT_MODE 0x0F

// Button address offsets
#define BUTTONS_DATA_ADDR_OFFSET 0x00
#define BUTTONS_TRI_ADDR_OFFSET 0x04

static uint32_t readRegister(uint32_t offset);
static void writeRegister(uint32_t offset, uint32_t value);

// Initializes the button driver software and hardware.
void buttons_init() {
  writeRegister(BUTTONS_TRI_ADDR_OFFSET, BUTTONS_TRI_INPUT_MODE);
}

// Returns the current value of all 4 buttons as the lower 4 bits of the
// returned value. bit3 = BTN3, bit2 = BTN2, bit1 = BTN1, bit0 = BTN0.
uint8_t buttons_read() {
  return (readRegister(BUTTONS_DATA_ADDR_OFFSET) & BUTTONS_ALL_MASK);
}

// Returns the current value of the of the data stored at the given offset
// from SLIDE_BUTTONS_BASEADDR.
// Used to read the values of the BUTTONS on the ZYBO board.
static uint32_t readRegister(uint32_t offset) {
  return Xil_In32(XPAR_PUSH_BUTTONS_BASEADDR + offset);
}

// Sets the current value of the of the data stored at the given offset
// from SLIDE_BUTTONS_BASEADDR.
// Used to set the values of the TRI Gate
static void writeRegister(uint32_t offset, uint32_t value) {
  Xil_Out32(XPAR_PUSH_BUTTONS_BASEADDR + offset, value);
}

#endif /* BUTTONS */
