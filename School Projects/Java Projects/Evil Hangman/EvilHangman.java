package hangman;

import org.junit.platform.commons.util.StringUtils;

import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class EvilHangman{

    public static void main(String[] args) {

        String dictionaryFileName = args[0];
        int wordLength = Integer.parseInt(args[1]);
        int guesses = Integer.parseInt(args[2]);

        File file = new File(dictionaryFileName);
        List<String> results = new ArrayList<>();
        EvilHangmanGame game = new EvilHangmanGame();
        Scanner input = new Scanner(System.in);
        Character guess;

        try {
            game.startGame(file, wordLength);
        } catch (EmptyDictionaryException e) {
            System.out.println("I'm sorry, but that file is empty, please pick another one and try again");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            //System.out.println("I'm sorry, but I am not compatible with that file, please pick another one and try again...");
            return;
        }

        for (int i = 0; i < guesses; i++) {
            System.out.println("You have "+ (guesses - i) + " guesses left");
            System.out.print("Guessed letters: ");
            Iterator<Character> iter = game.getGuessedLetters().iterator();
            for(int ii = 0; ii < game.getGuessedLetters().size(); ii++){
                System.out.print(iter.next() + " ");

            }
            System.out.println();

            System.out.println("Word: " + game.getDecryptedKey());
            System.out.print("Enter Guess: ");

            guess = input.next().charAt(0);
            try {
                results = new ArrayList<String>(game.makeGuess(guess));
                //System.out.println(results);
                //System.out.println(game.getMap().toString());
                if(!game.getDecryptedKey().contains("-")){
                    System.out.println("You Won!!!");
                    System.out.println("The word is " + results.get(0));
                    return;
                }
                game.setWordList(results);
            } catch (GuessAlreadyMadeException e) {
                System.out.println("You already guessed that, try again");
                i--;
            }

            String key = game.getDecryptedKey();
            int count = 0;
            for(int ii = 0; ii < key.length(); ii++){
                if(key.charAt(ii) == guess){
                    count ++;
                }
            }

            if(count == 0){
                System.out.println("Sorry, there are no " + String.valueOf(guess) + "'s");
            } else {
                System.out.println("Yes, there are " + count + " " + String.valueOf(guess));
                i--;
            }

            System.out.println();
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println();

        }

        System.out.println("You Lose!!!");
        System.out.println("The word is " + results.get(0));

    }
}
