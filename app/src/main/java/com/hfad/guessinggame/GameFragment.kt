package com.hfad.guessinggame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModelProvider
import com.hfad.guessinggame.databinding.FragmentGameBinding
import androidx.navigation.findNavController
import androidx.lifecycle.Observer

class GameFragment : Fragment() {
    private var _binding: FragmentGameBinding? = null
    private val binding get () = _binding!!
    lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        _binding = FragmentGameBinding.inflate(inflater, container, false)

        viewModel.secretWordDisplay.observe(viewLifecycleOwner, Observer {
            newValue -> binding.word.text = newValue
        })

        viewModel.incorrectGuesses.observe(viewLifecycleOwner, Observer { //observe change and display it
            newValue -> binding.incorrect.text = "Incorrect guesses: $newValue"
        })

        viewModel.livesLeft.observe(viewLifecycleOwner, Observer {
            newValue -> binding.lives.text = "You have $newValue lives left"
        })

        viewModel.gameOver.observe(viewLifecycleOwner, Observer {
            newValue ->
                if (newValue) {
                    val action = GameFragmentDirections.actionGameFragmentToResultFragment(viewModel.wonLostMessage())
                    binding.root.findNavController().navigate(action)
                }
        })

        binding.guessButton.setOnClickListener {
            viewModel.makeGuess(binding.guess.text.toString().uppercase())
            binding.guess.text = null
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}