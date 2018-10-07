# EvilHangmanGame

Simulates a game of hangman where the user (almost) never wins.

Input arguments: Text file with a list of words to be used by the game as its dictionary, number of letters in the word you want to guess, number of guess allowed

Output: Each time a guess is made, the terminal will update the currently guessed word. Until a correct letter is guessed, the word will appear as all dashes. When a correct letter is guessed, the dashes will be replaced by that letter in the appropriate places. A list of all letters guessed is also outputted each time before the player guesses again. If a letter is guessed twice or an invalid guess is made (two letters, a number, question mark, etc.), the player is prompted to enter a valid guess. If the player successfully guesses the word then "You win!" is outputted followed by the winning word. Similiarly, if the player uses up all of their guesses and does not guess the word then "You lose!" is outputted followed by the word.

The game works to force the player to almost never win. Once the length of word is chosen by the initial arguments, the game removes any words longer or shorter than that length from its dictionary. When each guess is made, the game partitions its dictionary into different sets based on the location of the guessed letter in the word. For example, if the guessed letter was 'a' then one set would consist of all the letters in the dictionary that has 'a' as their second letter only (-a---). After creating all of the possible sets, the game chooses the one with the most words to be its new dictionary. If there is a tie for the most words, the game uses the following tiebreakers: choose the set where none of the guessed letter appears, choose the set where the fewest of the guessed letter appears, choose the set where the guessed letter appears in the rightmost position. The game continues to partition its dictionary into sets and selecting one of those to be its new dictionary until the player guesses correctly or runs out of guesses. In the case that the player runs out of guesses, the game selects one of the words from its final set (at random) and tells the player that was the word.

**All files in the project were written by Elizabeth Van Patten.
**Project was coded for a university class.
