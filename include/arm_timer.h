#ifndef ARM_TIMER
#define ARM_TIMER
int interrupts_startArmPrivateTimer();
int interrupts_stopArmPrivateTimer();

u32 interrupts_getPrivateTimerCounterValue(void);
void interrupts_setPrivateTimerLoadValue(u32 loadValue);
void interrupts_setPrivateTimerPrescalerValue(u32 prescalerValue);

// Returns the number of private timer ticks that occur in 1 second.
u32 interrupts_getPrivateTimerTicksPerSecond();

#endif /* ARM_TIMER */
