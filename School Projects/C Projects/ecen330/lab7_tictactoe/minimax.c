/*
This software is provided for student assignment use in the Department of
Electrical and Computer Engineering, Brigham Young University, Utah, USA.

Users agree to not re-host, or redistribute the software, in source or binary
form, to other persons or other institutions. Users may modify and use the
source code for personal or educational use.

For questions, contact Brad Hutchings or Jeff Goeders, https://ece.byu.edu/
*/

#ifndef MINIMAX
#define MINIMAX

#include <stdbool.h>
#include <stdint.h>
#include <stdio.h>

#include "ticTacToe.h"

// Scoring for minimax.
#define MINIMAX_X_WINNING_SCORE 10  // This means that X will win.
#define MINIMAX_O_WINNING_SCORE -10 // This means that O will win.
#define MINIMAX_DRAW_SCORE 0        // Nobody wins.
#define MINIMAX_NOT_ENDGAME -1      // Not an end-game.

// Useful Constants
#define BOARD_SIZE 3
#define TOT_SQRZ 9
#define INIT_VAL 0
#define TOP 0
#define MID 1
#define BOT 2
#define LFT 0
#define RGT 2

// Global Variables
tictactoe_location_t next_move;

// Define a score type.
typedef int16_t minimax_score_t;

// Score struct for minimax
typedef struct {
  tictactoe_location_t location;
  minimax_score_t score;
  uint8_t depth;
} Score;

typedef struct {
  minimax_score_t score;
  uint8_t depth;
} Best_Score;

// Function Declarations
bool minimax_isGameDraw(tictactoe_board_t *board);
Best_Score minimax(tictactoe_board_t *board, bool is_Xs_turn, uint8_t depth);
void minimax_printBoard(tictactoe_board_t *board);

// This routine is not recursive but will invoke the recursive minimax function.
// You will call this function from the controlling state machine that you will
// implement in a later milestone. It computes the row and column of the next
// move based upon: the current board and player.
//
// When called from the controlling state machine, you will call this function
// as follows:
// 1. If the computer is playing as X, you will call this function with
// is_Xs_turn = true.
// 2. If the computer is playing as O, you will call this function with
// is_Xs_turn = false.
// This function directly passes the  is_Xs_turn argument into the minimax()
// (helper) function.
tictactoe_location_t minimax_computeNextMove(tictactoe_board_t *board,
                                             bool is_Xs_turn) {
  minimax(board, is_Xs_turn, INIT_VAL);
  return next_move;
}

// Returns the score of the board.
// This returns one of 4 values: MINIMAX_X_WINNING_SCORE,
// MINIMAX_O_WINNING_SCORE, MINIMAX_DRAW_SCORE, MINIMAX_NOT_ENDGAME
// Note: the is_Xs_turn argument indicates which player just took their
// turn and makes it possible to speed up this function.
// Assumptions:
// (1) if is_Xs_turn == true, the last thing played was an 'X'.
// (2) if is_Xs_turn == false, the last thing played was an 'O'.
// Hint: If you know the game was not over when an 'X' was played,
// you don't need to look for 'O's, and vice-versa.
minimax_score_t minimax_computeBoardScore(tictactoe_board_t *board,
                                          bool is_Xs_turn) {
  // If it is X's turn
  if (is_Xs_turn) {
    // Check Rows wins
    for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
      // See if the row is full of X's
      if (board->squares[i][LFT] == MINIMAX_X_SQUARE &&
          board->squares[i][MID] == MINIMAX_X_SQUARE &&
          board->squares[i][RGT] == MINIMAX_X_SQUARE) {
        return MINIMAX_X_WINNING_SCORE;
      }
    }

    // Check Columns win
    for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
      // See if the col is full of X's
      if (board->squares[TOP][i] == MINIMAX_X_SQUARE &&
          board->squares[MID][i] == MINIMAX_X_SQUARE &&
          board->squares[BOT][i] == MINIMAX_X_SQUARE) {
        return MINIMAX_X_WINNING_SCORE;
      }
    }

    // Check Diagnal win
    if (board->squares[TOP][LFT] == MINIMAX_X_SQUARE &&
        board->squares[MID][MID] == MINIMAX_X_SQUARE &&
        board->squares[BOT][RGT] == MINIMAX_X_SQUARE) {
      return MINIMAX_X_WINNING_SCORE;
    }

    // Check Anti-Diagnal
    if (board->squares[TOP][RGT] == MINIMAX_X_SQUARE &&
        board->squares[MID][MID] == MINIMAX_X_SQUARE &&
        board->squares[BOT][LFT] == MINIMAX_X_SQUARE) {
      return MINIMAX_X_WINNING_SCORE;
    }

    // See if the game is a draw
    if (minimax_isGameDraw(board)) {
      return MINIMAX_DRAW_SCORE;
    }
    return MINIMAX_NOT_ENDGAME;
    // If it is O's turn
  } else {
    // Check Rows win
    for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
      // See if the row is full of O's
      if (board->squares[i][LFT] == MINIMAX_O_SQUARE &&
          board->squares[i][MID] == MINIMAX_O_SQUARE &&
          board->squares[i][RGT] == MINIMAX_O_SQUARE) {
        return MINIMAX_O_WINNING_SCORE;
      }
    }

    // Check Columns win
    for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
      // See if the col is full of O's
      if (board->squares[TOP][i] == MINIMAX_O_SQUARE &&
          board->squares[MID][i] == MINIMAX_O_SQUARE &&
          board->squares[BOT][i] == MINIMAX_O_SQUARE) {
        return MINIMAX_O_WINNING_SCORE;
      }
    }

    // Check Diagnal win
    if (board->squares[TOP][LFT] == MINIMAX_O_SQUARE &&
        board->squares[MID][MID] == MINIMAX_O_SQUARE &&
        board->squares[BOT][RGT] == MINIMAX_O_SQUARE) {
      return MINIMAX_O_WINNING_SCORE;
    }

    // Check Anti-Diagnal win
    if (board->squares[TOP][RGT] == MINIMAX_O_SQUARE &&
        board->squares[MID][MID] == MINIMAX_O_SQUARE &&
        board->squares[BOT][LFT] == MINIMAX_O_SQUARE) {
      return MINIMAX_O_WINNING_SCORE;
    }

    // Check to see if it is a draw
    if (minimax_isGameDraw(board)) {
      return MINIMAX_DRAW_SCORE;
    }
    return MINIMAX_NOT_ENDGAME;
  }
}

// Init the board to all empty squares.
void minimax_initBoard(tictactoe_board_t *board) {
  // Iterate through the rows
  for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
    // Iterate through the columns
    for (uint8_t j = INIT_VAL; j < BOARD_SIZE; j++) {
      board->squares[i][j] = MINIMAX_EMPTY_SQUARE;
    }
  }
}

// Determine that the game is over by looking at the score.
bool minimax_isGameOver(minimax_score_t score) {
  if (score == MINIMAX_NOT_ENDGAME)
    return false;
  return true;
}

// Determine if the game is in a draw state, requires you do this after you
// checked all possible winning combinations
bool minimax_isGameDraw(tictactoe_board_t *board) {
  // Loop through rows
  for (uint8_t i = INIT_VAL; i < BOARD_SIZE; i++) {
    // Loop through cols
    for (uint8_t j = INIT_VAL; j < BOARD_SIZE; j++) {
      // See if the square is empty
      if (board->squares[i][j] == MINIMAX_EMPTY_SQUARE) {
        return false;
      }
    }
  }
  return true;
}

// Recursive minimax funciton that returns the Best_Score of the board given
// whos turn it is
Best_Score minimax(tictactoe_board_t *board, bool is_Xs_turn, uint8_t depth) {
  minimax_score_t board_score = minimax_computeBoardScore(board, !is_Xs_turn);
  // If game is over, return who own
  if (minimax_isGameOver(board_score)) {
    return (Best_Score){board_score, depth};
  }

  // Define array for tracking every moves best
  Score scores[TOT_SQRZ];
  uint8_t last_index = INIT_VAL;
  // Iterate through rows
  for (uint8_t row = INIT_VAL; row < BOARD_SIZE; row++) {
    // Iterate through columns
    for (uint8_t col = INIT_VAL; col < BOARD_SIZE; col++) {
      // See if the square is empty
      if (board->squares[row][col] == MINIMAX_EMPTY_SQUARE) {
        // Simulate playing at this location
        board->squares[row][col] =
            is_Xs_turn ? MINIMAX_X_SQUARE : MINIMAX_O_SQUARE;

        // Recursively call Minimax to get the best score playing with perfect
        // players
        Best_Score score = minimax(board, !is_Xs_turn, (depth + 1));

        // Add Score, move and depth to score table
        scores[last_index].location = (tictactoe_location_t){row, col};
        scores[last_index].score = score.score;
        scores[last_index].depth = score.depth;
        last_index++;

        // Undo playing at this location
        board->squares[row][col] = MINIMAX_EMPTY_SQUARE;
      }
    }
  }

  // Determine the best winning move
  minimax_score_t highest_score = scores[INIT_VAL].score;
  uint8_t longest_depth = scores[INIT_VAL].depth;
  next_move = scores[INIT_VAL].location;

  // DEBUGGING STUFF
  if (depth == INIT_VAL) {
    minimax_printBoard(board);
    // If X's turn
    if (is_Xs_turn) {
      printf("--X's turn, Higher Score is better--\n");
      // If O's turn
    } else {
      printf("--O's turn, Lower Score is better--\n");
    }
    // Loop through every possible turn and print that it's position is, the
    // score it gets and how long it takes to get that score
    for (uint8_t i = INIT_VAL; i < last_index; i++) {
      printf("Posible option %d: %d, %d => %d [%d turns]\n", i,
             scores[i].location.row, scores[i].location.column, scores[i].score,
             scores[i].depth - MID);
    }
  }

  // Loop through every possible option and find out the best outcome
  for (uint8_t i = INIT_VAL; i < last_index; i++) {
    // If X's turn
    if (is_Xs_turn) {
      // Check to see if it has a higher score than previous
      if (scores[i].score > highest_score) {
        highest_score = scores[i].score;
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
        // Check to see if X isn't winning, but can delay tie/lose
      } else if (highest_score != MINIMAX_X_WINNING_SCORE &&
                 scores[i].score == highest_score &&
                 scores[i].depth > longest_depth) {
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
        // Check to see if X is winning and can win faster
      } else if (highest_score == MINIMAX_X_WINNING_SCORE &&
                 scores[i].score == highest_score &&
                 scores[i].depth < longest_depth) {
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
      }
      // If O's turn
    } else {
      // Check to see if it has a higher score than previous
      if (scores[i].score < highest_score) {
        highest_score = scores[i].score;
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
        // Check to see if O isn't winning, but can delay tie/lose
      } else if (highest_score != MINIMAX_O_WINNING_SCORE &&
                 scores[i].score == highest_score &&
                 scores[i].depth > longest_depth) {
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
        // Check to see if O is winning and can win faster
      } else if (highest_score == MINIMAX_O_WINNING_SCORE &&
                 scores[i].score == highest_score &&
                 scores[i].depth < longest_depth) {
        longest_depth = scores[i].depth;
        next_move = scores[i].location;
      }
    }
  }

  // Return the highest score
  return (Best_Score){highest_score, longest_depth};
}

// Prints a board out on the console making it look pretty
void minimax_printBoard(tictactoe_board_t *board) {
  // Iterate through all of the rows
  for (uint8_t row = INIT_VAL; row < BOARD_SIZE; row++) {
    // Iterate through the columns
    printf("  ");
    for (uint8_t col = INIT_VAL; col < BOARD_SIZE; col++) {
      // Check to see what is in the current square and print that symbol
      switch (board->squares[row][col]) {
      case MINIMAX_EMPTY_SQUARE:
        printf(" ");
        break;
      case MINIMAX_O_SQUARE:
        printf("O");
        break;
      case MINIMAX_X_SQUARE:
        printf("X");
        break;
      default:
        break;
      }
      // If you are on the last column, print a new line
      if (col == RGT) {
        printf("\n");
        // Print a divider
      } else {
        printf("|");
      }
    }
    // If you are on the last row, print a new line
    if (row == BOT) {
      printf("\n");
      // Print a divider
    } else {
      printf("  -----\n");
    }
  }
}

#endif /* MINIMAX */
