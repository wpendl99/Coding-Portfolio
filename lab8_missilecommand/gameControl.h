#ifndef GAMECONTROL
#define GAMECONTROL

#include <stdbool.h>

// Initialize the game control logic
// milestone3 - whether to include milestone 3 features (plane and stats)
//
// This function will initialize all missiles, stats, plane, etc.
void gameControl_init(bool milestone3);

// Tick the game control logic
//
// This function should tick the missiles, handle screen touches, collisions,
// and updating statistics.
void gameControl_tick();

#endif /* GAMECONTROL */
