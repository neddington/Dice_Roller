package com.zybooks.diceroller


import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.zybooks.diceroller.databinding.ActivityMainBinding


const val MAX_DICE = 5


class MainActivity : AppCompatActivity(),
    RollLengthDialogFragment.OnRollLengthSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private var timerLength = 2000L
    private var timer: CountDownTimer? = null
    private lateinit var optionsMenu: Menu
    private var numVisibleDice = MAX_DICE
    private lateinit var diceList: MutableList<Dice>
    private lateinit var diceImageViewList: MutableList<ImageView>
    private var selectedDie = 0
    private var total = 0
    private var initTouchX = 0
    private var initTouchY = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      //  val dialog = WarningDialogFragment()
        //
        // dialog.show(supportFragmentManager, "warningDialog")

        // Create list of Dice
        diceList = mutableListOf()
        for (i in 0 until MAX_DICE) {
            diceList.add(Dice(i + 1))
        }


        // Create list of ImageViews
        diceImageViewList = mutableListOf(
            binding.dice1, binding.dice2, binding.dice3, binding.dice4, binding.dice5
        )

        showDice()
        // Register context menus for all dice and tag each die
        for (i in 0 until diceImageViewList.size) {
            registerForContextMenu(diceImageViewList[i])
            diceImageViewList[i].tag = i
        }
        // Moving finger left or right changes dice number
        diceImageViewList[0].setOnTouchListener { _, event ->
            var returnVal = true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initTouchX = event.x.toInt()
                    initTouchY=  event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    val x = event.x.toInt()
                    val y = event.y.toInt()

                    // See if movement is at least 20 pixels
                    if (kotlin.math.abs(x - initTouchX) >= 20) {
                        if (x > initTouchX) {
                            diceList[0].number++
                        } else {
                            diceList[0].number--
                        }
                        showDice()
                        initTouchX = x
                    }
                    else if (kotlin.math.abs(y - initTouchY) >= 20) {
                        if (y > initTouchY) {
                            diceList[0].number++
                        } else {
                            diceList[0].number--
                        }
                        showDice()
                        initTouchY = y
                    }
                }
                else -> returnVal = false
            }
            returnVal
        }

    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        // Save which die is selected
        selectedDie = v?.tag as Int

        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_one -> {
                diceList[selectedDie].number++
                showDice()
                true
            }
            R.id.subtract_one -> {
                diceList[selectedDie].number--
                showDice()
                true
            }
            R.id.roll -> {
                rollDice()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        optionsMenu = menu!!
        return super.onCreateOptionsMenu(menu)
    }

    private fun showDice() {

        // Show visible dice
        for (i in 0 until numVisibleDice) {
            val diceDrawable = ContextCompat.getDrawable(this, diceList[i].imageId)
            diceImageViewList[i].setImageDrawable(diceDrawable)
            diceImageViewList[i].contentDescription = diceList[i].imageId.toString()
        }

    }
    override fun onRollLengthClick(which: Int) {
        // Convert to milliseconds
        timerLength = 1000L * (which + 1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // Determine which menu option was chosen
        return when (item.itemId) {
            R.id.action_one -> {
                changeDiceVisibility(1)
                showDice()
                true
            }
            R.id.action_two -> {
                changeDiceVisibility(2)
                showDice()
                true
            }
            R.id.action_three -> {
                changeDiceVisibility(3)
                showDice()
                true
            }
            R.id.action_four -> {
                changeDiceVisibility(4)
                showDice()
                true
            }
            R.id.action_five -> {
                changeDiceVisibility(5)
                showDice()
                true
            }
            R.id.action_stop -> {
                timer?.cancel()
                item.isVisible = false
                true
            }
            R.id.action_roll -> {
                rollDice()
                true
            }
            R.id.action_roll_length -> {
                val dialog = RollLengthDialogFragment()
                dialog.show(supportFragmentManager, "rollLengthDialog")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun rollDice() {
        optionsMenu.findItem(R.id.action_stop).isVisible = true
        timer?.cancel()

        // Start a timer that periodically changes each visible dice
        timer = object : CountDownTimer(timerLength, 100) {
            override fun onTick(millisUntilFinished: Long) {

                for (i in 0 until numVisibleDice) {
                    diceList[i].roll()


                }
                showDice()
            }

            override fun onFinish() {

                optionsMenu.findItem(R.id.action_stop).isVisible = false
                for (i in 0 until numVisibleDice) {
                   total += diceList[i].number
                }
                Toast.makeText(applicationContext,"Total Dice: " + total, Toast.LENGTH_LONG).show()
                total = 0
            }
        }.start()


    }

    private fun changeDiceVisibility(numVisible: Int) {
        numVisibleDice = numVisible

        // Make dice visible
        for (i in 0 until numVisible) {
            diceImageViewList[i].visibility = View.VISIBLE
        }

        // Hide remaining dice
        for (i in numVisible until MAX_DICE) {
            diceImageViewList[i].visibility = View.GONE
        }
    }

}