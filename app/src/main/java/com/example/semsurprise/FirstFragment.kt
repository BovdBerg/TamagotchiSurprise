package com.example.semsurprise

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import pl.droidsonroids.gif.GifImageView


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private lateinit var buttonLeerJapans: Button
    private lateinit var buttonLekkerDansen: Button
    private lateinit var buttonGunBierie: Button
    private lateinit var buttonTukkieDoen: Button
    private lateinit var lekkerPilsjeAudio: MediaPlayer
    private lateinit var dasLekkerAudio: MediaPlayer
    private lateinit var slaapLiedjeAudio: MediaPlayer
    private lateinit var textCloudTV: TextView
    private lateinit var zzzGIV: GifImageView
    private lateinit var semIV: ImageView
    private lateinit var bierieIV: ImageView
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

        bierieIV = view.findViewById<View>(R.id.bierie) as ImageView
        semIV = view.findViewById<View>(R.id.imageview_sem) as ImageView
        zzzGIV = view.findViewById<View>(R.id.zzz_gif) as GifImageView
        textCloudTV = view.findViewById<View>(R.id.imageview_textcloud) as TextView
        slaapLiedjeAudio = MediaPlayer.create(context, R.raw.slaap_kindje_slaap)
        lekkerPilsjeAudio = MediaPlayer.create(context, R.raw.lekker_pilsje_drinken)
        dasLekkerAudio = MediaPlayer.create(context, R.raw.ah_das_lekker)

        val rotateHeen = RotateAnimation(bierieIV.rotation, bierieIV.rotation + 90, Animation.RELATIVE_TO_SELF, 1.2f, Animation.RELATIVE_TO_SELF, 0.3f)
        rotateHeen.duration = 1500
        rotateHeen.startOffset = 3700
        rotateHeen.isFillEnabled = true
        rotateHeen.fillBefore = true
        rotateHeen.fillAfter = true
        rotateHeen.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                bierieIV.rotation = 0f
                bierieIV.visibility = View.VISIBLE
                lekkerPilsjeAudio.stop()
                dasLekkerAudio.stop()
                lekkerPilsjeAudio = MediaPlayer.create(context, R.raw.lekker_pilsje_drinken)
                lekkerPilsjeAudio.start()
            }
            override fun onAnimationRepeat(p0: Animation?) {
                // empty
            }
            override fun onAnimationEnd(p0: Animation?) {
                // empty
            }
        })

        val rotateBack = RotateAnimation(bierieIV.rotation, bierieIV.rotation - 90, Animation.RELATIVE_TO_SELF, 1.2f, Animation.RELATIVE_TO_SELF, 0.3f)
        rotateBack.duration = 1500
        rotateBack.startOffset = rotateHeen.startOffset + rotateHeen.duration + 800
        rotateBack.isFillEnabled = true
        rotateBack.fillBefore = true
        rotateBack.fillAfter = true
        rotateBack.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                // empty
            }
            override fun onAnimationRepeat(p0: Animation?) {
                // empty
            }
            override fun onAnimationEnd(p0: Animation?) {
                dasLekkerAudio = MediaPlayer.create(context, R.raw.ah_das_lekker)
                dasLekkerAudio.start()
                bierieIV.visibility = View.GONE
            }
        })

        drinkBierieSet = AnimationSet(true)
        drinkBierieSet.addAnimation(rotateHeen)
        drinkBierieSet.addAnimation(rotateBack)
        drinkBierieSet.interpolator = LinearInterpolator()

        buttonGunBierie = view.findViewById<Button>(R.id.button_gun_bierie)
        buttonGunBierie.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil geen bier tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                bierieIV.startAnimation(drinkBierieSet)
                // todo: bug dat biertje eerste keer verkeerd draait
                // todo: add slurp soundeffect
            }
        }

        var counter = 0
        buttonTukkieDoen = view.findViewById<Button>(R.id.button_tukkie_doen)
        buttonTukkieDoen.setOnClickListener {
            when (counter) {
                0 -> { // Wakker
                    hideViews()
                    isSleeping = false
                    semIV.setImageResource(R.drawable.sem)
                    buttonTukkieDoen.text = getString(R.string.tukkie_doen)
                    slaapLiedjeAudio.stop()
                    counter++
                }
                1 -> { // Slapen
                    hideViews()
                    isSleeping = true
                    semIV.setImageResource(R.drawable.sem_moe)
                    buttonTukkieDoen.text = getString(R.string.wakker_maken)
                    zzzGIV.visibility = View.VISIBLE
                    slaapLiedjeAudio = MediaPlayer.create(context, R.raw.slaap_kindje_slaap)
                    slaapLiedjeAudio.start()
                    counter = 0
                }
            }
        }

        buttonLekkerDansen = view.findViewById<Button>(R.id.button_lekker_dansen)
        buttonLekkerDansen.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil niet dansen tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                lekkerPilsjeAudio.stop()
                dasLekkerAudio.stop()
                val elfDansUrl = when ((0..6).random()) {
                    0 -> "https://elfyourself.com?mId=49314"
                    1 -> "https://elfyourself.com?mId=50183"
                    2 -> "https://elfyourself.com?mId=50188"
                    3 -> "https://elfyourself.com?mId=50189"
                    4 -> "https://elfyourself.com?mId=50192"
                    5 -> "https://elfyourself.com?mId=50198"
                    else -> "https://elfyourself.com?mId=50201"
                }
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(elfDansUrl))
                startActivity(browserIntent)
            }
        }

        buttonLeerJapans = view.findViewById<Button>(R.id.button_leer_japans)
        buttonLeerJapans.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil niet Japans leren tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                textCloudTV.visibility = View.VISIBLE
                lekkerPilsjeAudio.stop()
                dasLekkerAudio.stop()
                // todo: hide after a while
                // todo: meer japanse woorden
                textCloudTV.text = when ((0..7).random()) {
                    0 -> "卓球"
                    1 -> "寿司"
                    2 -> "私は日本語がとても上手になります"
                    3 -> "これはduolingoよりも優れています"
                    4 -> "ching chong kawaii"
                    5 -> "私はアニメを楽しんでいます"
                    6 -> "私は経済学を研究しています"
                    7 -> "ガンビール"
                    else -> "プレースホルダー"
                }
            }
        }
        // todo: add app logo
        // todo: health bars
    }

    private fun hideViews() {
        textCloudTV.visibility = View.GONE
        zzzGIV.visibility = View.GONE
        bierieIV.visibility = View.GONE
    }
}
