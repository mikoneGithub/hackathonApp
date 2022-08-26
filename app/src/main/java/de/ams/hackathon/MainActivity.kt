package de.ams.hackathon

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import de.ams.hackathon.databinding.ActivityMainBinding
import kotlin.math.max
import kotlin.math.min

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
        }

        binding.btnPause.setOnClickListener {
            binding.boardView.onPausePressed()
        }

        binding.btnBack.setOnClickListener {
            binding.boardView.onBackPressed()
        }

        binding.btnPlus.setOnClickListener {
            setLevel(binding, min(currentLevel + 1, items.size - 1))
        }

        binding.btnMinus.setOnClickListener {
            setLevel(binding, max(currentLevel - 1, 0))
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