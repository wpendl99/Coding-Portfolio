#ifndef ARMTIMER
#define ARMTIMER

#include <stdint.h>

#ifdef __cplusplus
extern "C" {
#endif

int armTimer_start();
int armTimer_stop();
void armTimer_init();

void armTimer_setPeriod(double period_seconds);
void armTimer_ackInterrupt();

void armTimer_enableInterrupts();
void armTimer_disableInterrupts();

// uint32_t interrupts_getPrivateTimerCounterValue(void);
// void interrupts_setPrivateTimerLoadValue(u32 loadValue);
// void interrupts_setPrivateTimerPrescalerValue(u32 prescalerValue);

// // Returns the number of private timer ticks that occur in 1 second.
// uint32_t interrupts_getPrivateTimerTicksPerSecond();

#ifdef __cplusplus
}
#endif

#endif /* ARMTIMER */
