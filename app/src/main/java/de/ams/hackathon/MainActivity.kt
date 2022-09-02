package de.ams.hackathon

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageButton
import de.ams.hackathon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var currentLevel = 0
    private lateinit var items: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        items = resources.getStringArray(R.array.levels)

        initDropDown(binding)
        initClickListeners(binding)

        setContentView(binding.root)
    }

    private fun initDropDown(binding: ActivityMainBinding) {

        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.dropdownLevel.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.dropdownLevel.editText as? AutoCompleteTextView)?.setText(items[0], false)
    }

    private fun initClickListeners(binding: ActivityMainBinding) {
        binding.btnPlay.setOnClickListener {
            binding.boardView.onPlayPressed()

            // toggle icon
            val name = if (binding.boardView.running) "de.ams.hackathon:drawable/ic_baseline_pause_circle_outline_24" else "de.ams.hackathon:drawable/ic_baseline_play_arrow_24"
            val button = binding.btnPlay as AppCompatImageButton
            val res = resources.getIdentifier(name, null, null)
            button.setImageResource(res)
        }

        binding.btnPause.setOnClickListener {
            binding.boardView.onToggleRouter()

            val name = if (binding.boardView.engine.basic) "de.ams.hackathon:drawable/alpha_a_circle" else "de.ams.hackathon:drawable/alpha_b_circle"
            val button = binding.btnPause as AppCompatImageButton
            val res = resources.getIdentifier(name, null, null)
            button.setImageResource(res)
        }

        binding.btnBack.setOnClickListener {
            binding.boardView.onBackPressed()
        }

        binding.btnPlus.setOnClickListener {
            binding.boardView.onPlusPressed()
        }

        binding.btnMinus.setOnClickListener {
            binding.boardView.onMinusPressed()
        }

        (binding.dropdownLevel.editText as? AutoCompleteTextView)?.setOnItemClickListener { _, _, i, _ ->
            setLevel(binding, i)
        }
    }

    private fun setLevel(binding: ActivityMainBinding, level: Int) {
        if (level != currentLevel) {
            currentLevel = level
            binding.boardView.onLevelSelected(level)
            (binding.dropdownLevel.editText as? AutoCompleteTextView)?.setText(
                items[currentLevel],
                false
            )
        }
    }
}