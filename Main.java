package hangman;
import java.io.*;
import java.util.*;

public class Main
{
  public static void main(String args[]) throws IOException
  {
    String dictionary = args[0];
    int wordLength = Integer.parseInt(args[1]); //must be greater than or equal to 2
    int guesses = Integer.parseInt(args[2]); //must be greater than or equal to 1
    boolean badInput = false;

    EvilHangmanGame EG = new EvilHangmanGame();

    File dictionaryFile = new File(dictionary);

    EG.startGame(dictionaryFile, wordLength);

    String guessedWord = EG.baseWord;

    while(guesses > 0)
    {
      System.out.println("You have " + guesses + " guesses left");
      System.out.print("Used letters:");
      //If letters have already been guessed then print out guessed letters
      if(EG.usedLetters.size() > 0)
      {
        Collections.sort(EG.usedLetters);
        for(int i = 0; i < EG.usedLetters.size(); i++)
        {
          System.out.print(" " + EG.usedLetters.get(i));
        }
      }
      System.out.print("\n");
      System.out.println("Word: " + guessedWord);
      //Get the guess from the user
      Scanner userGuess = new Scanner(System.in);
      System.out.print("Enter guess: ");
      String guessString = userGuess.next();
      //Check to see if info is bad
      while(guessString.length() != 1 || !Character.isLetter(guessString.charAt(0)))
      {
        System.out.println("You have entered invalid input. Please enter a letter and try again.");
        System.out.print("Enter guess: ");
        guessString = userGuess.next();
      }
      char guess = guessString.charAt(0);
      //Run the guess function
      try {
        Set<String> winningWords = EG.makeGuess(guess);
        //Return the pattern for the current guessed word set
        String tempGuessWord = EG.currentWordPattern;

        //Only do if not end game
        if((guesses - 1) != 0)
        {
          //Incorrect guess
          if(tempGuessWord.equals(EG.baseWord))
          {
            System.out.println("Sorry, there are no " + guess + "'s\n");
            guesses--;
          }
          //Correct guess
          else
          {
            guessedWord = correctGuess(tempGuessWord, guessedWord, guess);
          }
        }
        //If it is the end game...
        else
        {
          if(tempGuessWord.equals(EG.baseWord))
          {
            //Wrong guess means a loss in the end game - too bad for you :(
            System.out.println("You lose!");
            System.out.println("The word was: " + winningWords.iterator().next());
            guesses--;
          }
          else
          {
            guessedWord = correctGuess(tempGuessWord, guessedWord, guess);
          }
        }
        if(!guessedWord.contains("-"))
        {
          guesses = 0;
        }
      } catch(EvilHangmanGame.GuessAlreadyMadeException ex)
      {
        System.out.println("You already guessed that. Enter a new guess.");
      }
    }
  }

  public static String correctGuess(String tempGuessWord, String guessedWord, char guess)
  {
    //Find out how many of the guessed letter there are in the word
    int numLetters = 0;
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < tempGuessWord.length(); i++)
    {
      if(tempGuessWord.charAt(i) == guess)
      {
        //Update current guessed word to reflect new letters
        sb.append(guess);
        numLetters++;
      }
      else
      {
        sb.append(guessedWord.charAt(i));
      }
    }
    guessedWord = sb.toString();
    if(guessedWord.contains("-"))
    {
      System.out.println("Yes, there is " + numLetters + " " + guess + "\n");
      return guessedWord;
    }
    else
    {
      System.out.println("You win!");
      System.out.println("The word was: " + guessedWord);
      return guessedWord;
    }
  }
}
