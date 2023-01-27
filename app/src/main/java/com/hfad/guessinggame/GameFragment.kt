package com.hfad.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hfad.guessinggame.databinding.FragmentGameBinding
import androidx.navigation.findNavController

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get () = _binding!!

    val words = listOf("Android", "activity", "lunar", "fragment")
    val secretWord = words.random().uppercase()
    var secretWordDisplay = ""
    var correctGuesses = ""
    var incorrectGuesses = ""
    var livesLeft = 9

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        secretWordDisplay = deriveSecretWordDisplay()
        updateScreen()

        binding.guessButton.setOnClickListener {
            makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
            updateScreen()
            if(isWon() || isLost()){
                val action = GameFragmentDirections.actionGameFragmentToResultFragment(wonLostMessage())
                binding.root.findNavController().navigate(action)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateScreen(){
        binding.apply {
            val mistakes = "Incorrect guesses: $incorrectGuesses"
            val livesText = "You have $livesLeft lives left"
            word.text = secretWordDisplay
            lives.text = livesText
            incorrect.text = mistakes
        }
    }

    fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkLetter(it.toString())
        }
        return display
    }

    fun checkLetter(str: String) = when (correctGuesses.contains(str)){
        true ->  str
        false -> "_"
    }

    fun makeGuess(guess: String) {
        if(guess.length == 1){
            if(secretWord.contains(guess)){
                correctGuesses += guess
                secretWordDisplay = deriveSecretWordDisplay()
            } else {
                incorrectGuesses += " $guess"
                livesLeft--
            }
        }
    }

    fun isWon() = secretWord.equals(secretWordDisplay, true)

    fun isLost() = livesLeft <= 0

    fun wonLostMessage(): String {
        var message = ""
        if (isWon()) message = "You won!"
        else if (isLost()) message = "You lost xD"
        message += " the word was $secretWord"
        return message
    }

}