package hangman;
import java.io.*;
import java.util.*;

public class EvilHangmanGame
{
  @SuppressWarnings("serial")
  public static class GuessAlreadyMadeException extends Exception {
  }

  public ArrayList<Character> usedLetters = new ArrayList<Character>();
  public String baseWord = new String();
  public Set<String> dictionaryWords = new HashSet<String>();
  public String currentWordPattern = new String();

  public void startGame(File dictionary, int wordLength)
  {
    //Make the base word filled with dashes up to the word length
    StringBuilder sb = new StringBuilder();
    for(int i = 0; i < wordLength; i++)
    {
      sb.append('-');
    }
    baseWord = sb.toString();
    currentWordPattern = baseWord;

    try {
      Scanner scanner = new Scanner(dictionary);


      while(scanner.hasNext())
      {
        String nextWord = scanner.next();
        if(nextWord.length() == wordLength)
        {
          dictionaryWords.add(nextWord);
        }
      }
      if(dictionaryWords.size() == 0)
      {
        System.out.println("There are no words that size. Enter a different word length.");
        System.exit(0);
      }
      scanner.close();
    } catch(FileNotFoundException ex)
    {
      System.out.println("File not found.");
      System.exit(0);
    }


  }

  public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException
  {
    guess = Character.toLowerCase(guess);
    if(usedLetters.contains(guess))
    {
      throw new GuessAlreadyMadeException();
    }
    //Make the guess lower case and add to list of guessed letters
    else
    {
      usedLetters.add(guess);
    }
    //Create map to hold current patterns and words
    Map<String, Set<String>> patternedWords = new HashMap<String, Set<String>>();
    Map<String, Integer> freqGuessedChar = new HashMap<String, Integer>();

    for(String dictionaryWord : dictionaryWords)
    {
      String pattern = makePattern(dictionaryWord, guess);
      Set<String> words = new HashSet<String>();

      if(patternedWords.containsKey(pattern))
      {
        //Add the word to the pattern map
        words = patternedWords.get(pattern);
        words.add(dictionaryWord); //do we need map.put(pattern, words)?
      }
      else
      {
        //Add the new pattern and first word in the set to the pattern map
        words.clear();
        words.add(dictionaryWord);
        patternedWords.put(pattern, words);
        //Add the number of guessed letters in the pattern to the freq map
        int letterFreq = 0;
        for(int i = 0; i < pattern.length(); i++)
        {
          if(pattern.charAt(i) == guess)
          {
            letterFreq++;
          }
        }
        freqGuessedChar.put(pattern, letterFreq);
      }
      //Map now has all patterns and sets of words relating to patterns
    }

    //Return the largest set
    String biggestSetKey = new String();
    int bigSize = 0;
    for(String key : patternedWords.keySet())
    {
      if(patternedWords.get(key).size() > bigSize)
      {
        biggestSetKey = key;
        bigSize = patternedWords.get(key).size();
      }
      else if(patternedWords.get(key).size() == bigSize)
      {
        //Return the set that has none of the guessed letter
        if(key.equals(baseWord))
        {
          biggestSetKey = key;
          bigSize = patternedWords.get(key).size();
        }
        else if(biggestSetKey.equals(baseWord))
        {
          continue;
        }
        //If not an option, go through other compare options
        else
        {
          biggestSetKey = compareSets(key, biggestSetKey, freqGuessedChar, guess);
          bigSize = patternedWords.get(biggestSetKey).size();
        }
      }
    }
    dictionaryWords = patternedWords.get(biggestSetKey);
    currentWordPattern = biggestSetKey;
    return patternedWords.get(biggestSetKey);
  }

  public String compareSets(String compareKey, String biggestSetKey, Map<String, Integer> freqGuessedChar, char guess)
  {
    //Return the set with fewest of the guessed letter
    if(freqGuessedChar.get(compareKey) < freqGuessedChar.get(biggestSetKey))
    {
      return compareKey;
    }
    if(freqGuessedChar.get(biggestSetKey) < freqGuessedChar.get(compareKey))
    {
      return biggestSetKey;
    }
    //Return set with rightmost letter or rightmost letters
    for(int i = baseWord.length() - 1; i >= 0; i--)
    {
      if(compareKey.charAt(i) == guess && biggestSetKey.charAt(i) != guess)
      {
        return compareKey;
      }
      else if(biggestSetKey.charAt(i) == guess && compareKey.charAt(i) != guess)
      {
        return biggestSetKey;
      }
    }
    return biggestSetKey;
  }

  public String makePattern(String word, char letter)
  {
    StringBuilder sb = new StringBuilder();

    for(int i = 0; i < word.length(); i++)
    {
      if(word.charAt(i) != letter)
      {
        sb.append('-');
      }
      else
      {
        sb.append(letter);
      }
    }
    return sb.toString();
  }
}
