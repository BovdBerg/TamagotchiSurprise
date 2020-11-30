package com.example.semsurprise

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import pl.droidsonroids.gif.GifImageView


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var drinkBierieSet: AnimationSet
    private var isSleeping = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bierieIV = view.findViewById<View>(R.id.bierie) as ImageView
        val semIV = view.findViewById<View>(R.id.imageview_sem) as ImageView
        val zzzGIV = view.findViewById<View>(R.id.zzz_gif) as GifImageView

        val rotateHeen = RotateAnimation(bierieIV.rotation, bierieIV.rotation + 90, Animation.RELATIVE_TO_SELF, 1.2f, Animation.RELATIVE_TO_SELF, 0.3f)
        rotateHeen.duration = 1500
        rotateHeen.startOffset = 3700
        rotateHeen.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                bierieIV.visibility = View.VISIBLE
                MediaPlayer.create(context, R.raw.lekker_pilsje_drinken).start()
            }
            override fun onAnimationRepeat(p0: Animation?) {
                // empty
            }
            override fun onAnimationEnd(p0: Animation?) {
                // empty
                bierieIV.visibility = View.GONE
            }
        })

        val rotateBack = RotateAnimation(bierieIV.rotation, bierieIV.rotation - 90, Animation.RELATIVE_TO_SELF, 1.2f, Animation.RELATIVE_TO_SELF, 0.3f)
        rotateBack.duration = 1500
        rotateBack.startOffset = rotateHeen.startOffset + rotateHeen.duration + 800
        rotateBack.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                // empty
            }
            override fun onAnimationRepeat(p0: Animation?) {
                // empty
            }
            override fun onAnimationEnd(p0: Animation?) {
                MediaPlayer.create(context, R.raw.ah_das_lekker).start()
                bierieIV.visibility = View.GONE
            }
        })

        drinkBierieSet = AnimationSet(true)
        drinkBierieSet.addAnimation(rotateHeen)
        drinkBierieSet.addAnimation(rotateBack)
        drinkBierieSet.interpolator = LinearInterpolator()
        drinkBierieSet.isFillEnabled = true
        drinkBierieSet.fillAfter = true

        view.findViewById<Button>(R.id.button_gun_bierie).setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil geen bier tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                bierieIV.startAnimation(drinkBierieSet)
                // todo: bug dat biertje eerste keer verkeerd draait
                // todo: add slurp soundeffect
            }
        }

        var counter = 0
        val buttonTukkieDoen = view.findViewById<Button>(R.id.button_tukkie_doen)
        buttonTukkieDoen.setOnClickListener {
            when (counter) {
                0 -> { // Wakker
                    isSleeping = false
                    semIV.setImageResource(R.drawable.sem)
                    buttonTukkieDoen.text = getString(R.string.tukkie_doen)
                    zzzGIV.visibility = View.GONE
                    counter++
                }
                1 -> { // Slapen
                    isSleeping = true
                    bierieIV.visibility = View.GONE
                    semIV.setImageResource(R.drawable.sem_moe)
                    buttonTukkieDoen.text = getString(R.string.wakker_maken)
                    zzzGIV.visibility = View.VISIBLE
                    counter = 0
                }
            }
        }
//        view.findViewById<Button>(R.id.button_leer_japans).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }

    }

}
