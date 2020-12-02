package com.example.semsurprise

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
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

    private lateinit var japans_check: ImageView
    private lateinit var moeheid_check: ImageView
    private lateinit var dorst_check: ImageView
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
    private var dorst_bool = false
        set(value) {
            if (value) {
                dorst_check.setImageResource(R.drawable.checkmark)
                Handler().postDelayed({
                    dorst_check.setImageResource(R.drawable.cross)
                }, 2*60*60*1000)
            }
            field = value
        }
    private var moeheid_bool = false
        set(value) {
            if (value) {
                moeheid_check.setImageResource(R.drawable.checkmark)
            } else {
                Handler().postDelayed({
                    moeheid_check.setImageResource(R.drawable.cross)
                }, 16*60*60*1000)
            }
            field = value
        }
    private var japans_bool = false
        set(value) {
            if (value) {
                japans_check.setImageResource(R.drawable.checkmark)
                Handler().postDelayed({
                    japans_check.setImageResource(R.drawable.cross)
                }, 6*60*60*1000)
            }
            field = value
        }

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
        dorst_check = view.findViewById<View>(R.id.check_dorst) as ImageView
        moeheid_check = view.findViewById<View>(R.id.check_moeheid) as ImageView
        japans_check = view.findViewById<View>(R.id.check_japans) as ImageView
        slaapLiedjeAudio = MediaPlayer.create(context, R.raw.slaap_kindje_slaap)
        lekkerPilsjeAudio = MediaPlayer.create(context, R.raw.lekker_pilsje_drinken)
        dasLekkerAudio = MediaPlayer.create(context, R.raw.ah_das_lekker)

        dorst_bool = true
        moeheid_bool = false
        japans_bool = true

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
                dorst_check.setImageResource(R.drawable.checkmark)
            }
        })

        drinkBierieSet = AnimationSet(true)
        drinkBierieSet.addAnimation(rotateHeen)
        drinkBierieSet.addAnimation(rotateBack)
        drinkBierieSet.interpolator = LinearInterpolator()

        buttonGunBierie = view.findViewById(R.id.button_gun_bierie)
        buttonGunBierie.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil geen bier tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                dorst_bool = true
                bierieIV.startAnimation(drinkBierieSet)
            }
        }

        var counter = 0
        buttonTukkieDoen = view.findViewById(R.id.button_tukkie_doen)
        buttonTukkieDoen.setOnClickListener {
            when (counter) {
                0 -> { // Wakker
                    hideViews()
                    isSleeping = false
                    moeheid_bool = false
                    semIV.setImageResource(R.drawable.sem_kawaii)
                    buttonTukkieDoen.text = getString(R.string.tukkie_doen)
                    slaapLiedjeAudio.stop()
                    counter++
                }
                1 -> { // Slapen
                    hideViews()
                    moeheid_bool = true
                    isSleeping = true
                    semIV.setImageResource(R.drawable.sem_moe_kawaii)
                    buttonTukkieDoen.text = getString(R.string.wakker_maken)
                    zzzGIV.visibility = View.VISIBLE
                    slaapLiedjeAudio = MediaPlayer.create(context, R.raw.slaap_kindje_slaap)
                    slaapLiedjeAudio.start()
                    counter = 0
                }
            }
        }

        buttonLekkerDansen = view.findViewById(R.id.button_lekker_dansen)
        buttonLekkerDansen.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil niet dansen tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                lekkerPilsjeAudio.stop()
                dasLekkerAudio.stop()
                val elfUrlList = listOf(
                    "https://elfyourself.com?mId=49314",
                    "https://elfyourself.com?mId=50183",
                    "https://elfyourself.com?mId=50188",
                    "https://elfyourself.com?mId=50189",
                    "https://elfyourself.com?mId=50192",
                    "https://elfyourself.com?mId=50198",
                    "https://elfyourself.com?mId=50201"
                )
                val elfDansUrl = elfUrlList[(elfUrlList.indices).random()]
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(elfDansUrl))
                startActivity(browserIntent)
            }
        }

        buttonLeerJapans = view.findViewById(R.id.button_leer_japans)
        buttonLeerJapans.setOnClickListener {
            if (isSleeping) {
                Toast.makeText(context, "Sem wil niet Japans leren tijdens het slapen", Toast.LENGTH_SHORT).show()
            } else {
                hideViews()
                japans_bool = true
                lekkerPilsjeAudio.stop()
                dasLekkerAudio.stop()
                textCloudTV.visibility = View.VISIBLE
                val japaneseWords = listOf(
                        "卓球",
                        "寿司",
                        "私は日本語がとても上手になります",
                        "これはduolingoよりも優れています",
                        "ching chong kawaii",
                        "私はアニメを楽しんでいます",
                        "私は経済学を研究しています",
                        "ガンビール",
                        "私の目は他の人と異なって見えます",
                        "広島",
                        "誰が春のロールを購入したいですか？",
                        "ラタタは私のお気に入りのポケモンです"
                )
                textCloudTV.text = japaneseWords[(japaneseWords.indices).random()]
                japans_check.setImageResource(R.drawable.checkmark)
            }
        }
    }

    private fun hideViews() {
        textCloudTV.visibility = View.GONE
        zzzGIV.visibility = View.GONE
        bierieIV.visibility = View.GONE
    }
}
