#ifndef TICTACTOECONTROL
#define TICTACTOECONTROL

#include "drivers/buttons.h"
#include "minimax.h"
#include "stdio.h"
#include "ticTacToeDisplay.h"
#include "touchscreen.h"

#include <time.h>

// TicTacToe State Machine States
typedef enum {
  init_st,
  setup_st,
  intro_st,
  redo_st,
  player_waiting_st,
  comp_move_st,
  game_over_st
} sm_state_t;

// Global Variables
static sm_state_t currentState;
static bool is_Xs_turn;
static tictactoe_board_t game_board;
static bool first_move;

// TicTacToe Constants
static uint16_t intro_cnt;
static uint16_t intro_num_ticks;
#define INTRO_TIME_S 3
#define HALF_INT 2
#define INITIAL_START 1
#define INITIAL_VAL 0
#define BTN_0_MSK 0x01
#define BOARD_SIZE 3
#define RANDOM_SEED 48321234

// Color/Text Constants
#define BACKGROUND_COLOR DISPLAY_DARK_BLUE
#define TEXT_COLOR DISPLAY_WHITE
#define TEXT_SIZE 2

// TicTacToe State Messages for Debugging
#define error_msg                                                              \
  "TicTacToe: There was an error with the clock state machine, please try "    \
  "again later\n"

#define intro_msg                                                              \
  "Touch board to start\n(PLAY X)\n--or--\nWait for computer\n(PLAY O)"

// Function Declarations
void draw_intro_text(uint16_t color);

// Tick the tic-tac-toe controller state machine
void ticTacToeControl_tick() {
  tictactoe_location_t next_move;
  // Perform state update first
  switch (currentState) {
  case init_st:
    currentState = setup_st;
    break;
  case setup_st:
    draw_intro_text(TEXT_COLOR);
    currentState = intro_st;
    break;
  case intro_st:
    // If the player wants to start
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      first_move = INITIAL_VAL;
      draw_intro_text(BACKGROUND_COLOR);
      touchscreen_ack_touch();
      ticTacToeDisplay_init();
      currentState = player_waiting_st;
      // If the player wants the computer to start
    } else if (intro_cnt == intro_num_ticks) {
      draw_intro_text(BACKGROUND_COLOR);
      ticTacToeDisplay_init();
      intro_cnt = INITIAL_VAL;
      currentState = comp_move_st;
    }
    break;
  case redo_st:
    // If the player wants to go first
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      touchscreen_ack_touch();
      intro_cnt = INITIAL_VAL;
      currentState = player_waiting_st;
      // If the player wants the computer to go first
    } else if (intro_cnt == intro_num_ticks) {
      intro_cnt = INITIAL_VAL;
      currentState = comp_move_st;
    }
    break;
  case comp_move_st:
    // Check to see if the game is over
    if (minimax_isGameOver(
            minimax_computeBoardScore(&game_board, !is_Xs_turn))) {
      currentState = game_over_st;
      // If the game isn't over, let computer play
    } else {
      currentState = player_waiting_st;
    }
    break;
  case player_waiting_st:
    // Check to see if the game is over
    if (minimax_isGameOver(
            minimax_computeBoardScore(&game_board, !is_Xs_turn))) {
      currentState = game_over_st;
      // If the game isn't over, check if the player went
    } else if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      touchscreen_ack_touch();

      // Get the players touched location
      next_move =
          ticTacToeDisplay_getLocationFromPoint(touchscreen_get_location());

      // Update the board
      // Draw an X if player is X
      if (is_Xs_turn) {
        game_board.squares[next_move.row][next_move.column] = MINIMAX_X_SQUARE;
        ticTacToeDisplay_drawX(next_move, false);
        // Draw a O if player is O
      } else {
        game_board.squares[next_move.row][next_move.column] = MINIMAX_O_SQUARE;
        ticTacToeDisplay_drawO(next_move, false);
      }

      // Switch to comps turn
      currentState = comp_move_st;
      is_Xs_turn = !is_Xs_turn;
    }
    break;
  case game_over_st:
    // Check to see if the player is touching the screen when the game is over
    if (touchscreen_get_status() == TOUCHSCREEN_RELEASED) {
      touchscreen_ack_touch();
    }
    // If the button pushed the restart button
    if ((buttons_read() & BTN_0_MSK) == BTN_0_MSK) {
      // Iterate through all of the rows
      for (uint8_t row = INITIAL_VAL; row < BOARD_SIZE; row++) {
        // Iterate through columns
        for (uint8_t col = INITIAL_VAL; col < BOARD_SIZE; col++) {
          // If square has a O in it, erase it
          if (game_board.squares[row][col] == MINIMAX_O_SQUARE) {
            ticTacToeDisplay_drawO((tictactoe_location_t){row, col}, true);
            // If square has X in it, erase it
          } else if (game_board.squares[row][col] == MINIMAX_X_SQUARE) {
            ticTacToeDisplay_drawX((tictactoe_location_t){row, col}, true);
          }
        }
      }

      // Restart the board
      minimax_initBoard(&game_board);
      is_Xs_turn = true;

      first_move = INITIAL_START;
      currentState = redo_st;
    }
    break;
  default:
    // If an unexpected state happens
    printf(error_msg);
    break;
  }

  // Perform state action second
  switch (currentState) {
  case setup_st:
    minimax_initBoard(&game_board);
    is_Xs_turn = true;
    break;
  case intro_st:
    intro_cnt++;
    break;
  case comp_move_st:
    // Calculate the next move for the computer
    if (!first_move) {
      next_move = minimax_computeNextMove(&game_board, is_Xs_turn);
      // If this is the computer is going first, give it a random square
    } else {
      first_move = !first_move;
      next_move =
          (tictactoe_location_t){rand() % BOARD_SIZE, rand() % BOARD_SIZE};
    }

    // Update the board
    // Draw an X if comp is X
    if (is_Xs_turn) {
      game_board.squares[next_move.row][next_move.column] = MINIMAX_X_SQUARE;
      ticTacToeDisplay_drawX(next_move, false);
      // Draw an O if comp is O
    } else {
      game_board.squares[next_move.row][next_move.column] = MINIMAX_O_SQUARE;
      ticTacToeDisplay_drawO(next_move, false);
    }

    // Update to Players turn
    is_Xs_turn = !is_Xs_turn;
    break;
  case redo_st:
    intro_cnt++;
    break;
  }
}

// Initialize the tic-tac-toe controller state machine,
// providing the tick period, in seconds.
void ticTacToeControl_init(double period_s) {
  buttons_init();
  display_init();
  display_fillScreen(BACKGROUND_COLOR);
  intro_cnt = INITIAL_VAL;
  intro_num_ticks = INTRO_TIME_S / period_s;
  first_move = INITIAL_START;
  srand(RANDOM_SEED);
}

// Helper function to draw the intial intro text on the screeen
void draw_intro_text(uint16_t color) {
  display_setCursor(INITIAL_VAL, DISPLAY_HEIGHT / HALF_INT);
  display_setTextColor(color);
  display_setTextSize(TEXT_SIZE);

  display_print(intro_msg);
  display_setTextColor(BACKGROUND_COLOR);
}

#endif /* TICTACTOECONTROL */
