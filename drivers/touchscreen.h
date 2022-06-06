#ifndef TOUCHSCREEN
#define TOUCHSCREEN

#include "display.h"

enum touchscreen_status_e {
  TOUCHSCREEN_IDLE,
  TOUCHSCREEN_PRESSED,
  TOUCHSCREEN_RELEASED
};

void touchscreen_init(double period_seconds);

void touchscreen_tick();

enum touchscreen_status_e touchscreen_get_status();

void touchscreen_ack_touch();

display_point_t touchscreen_get_location();

#endif /* TOUCHSCREEN */
