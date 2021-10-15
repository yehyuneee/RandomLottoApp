package com.example.randomlottoapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.size
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    // by lazy
    // 처음 초기화 된 직후부터 계속 read-only로만 쓰이는 변수일 때 쓰인다

    private val mResetButton: Button by lazy {
        findViewById<Button>(R.id.reset_btn)
    }

    private val mAddButton: Button by lazy {
        findViewById<Button>(R.id.add_btn)
    }

    private val mRunButton: Button by lazy {
        findViewById<Button>(R.id.run_btn)
    }

    private val mNumberPicker: NumberPicker by lazy {
        findViewById<NumberPicker>(R.id.number_picker)
    }

    // 생성된 번호가 노출될 text 리스트
    private val mTextList: List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.text_one),
            findViewById(R.id.text_two),
            findViewById(R.id.text_three),
            findViewById(R.id.text_four),
            findViewById(R.id.text_five),
            findViewById(R.id.text_six)
        )
    }

    // 자동생성이 이미 이루어진 상태인지 판단하는 플래그
    private var mDidRun = false

    // 자동생성 숫자와 추가한 숫자의 중복 여부 판단하는 Set
    private var mPickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mNumberPicker.minValue = 1
        mNumberPicker.maxValue = 45

        // 랜덤 번호 자동 생성
        initRunBtn()
        // 번호 추가
        addNumBtn()
        // 초기화
        resetInit()

    }

    /**
     * 랜덤 번호 생성
     */
    private fun initRunBtn() {
        mRunButton.setOnClickListener {
            mDidRun = true
            val list = getRandomList()

            list.forEachIndexed { index, number ->
                // forEach와는 다르게 Index값이 넘어와 명시할 수 있다.
                val textview = mTextList.get(index)
                textview.text = number.toString()
                textview.isVisible = true

                setNumberBackground(number, textview = textview)
            }
            println("random num : ${list}")
        }
    }

    /**
     * 랜덤 번호 가져오기
     */
    private fun getRandomList(): List<Int> {
        val resultlist = mutableListOf<Int>().apply {
            // apply 를 사용하면 그 앞에 함수를 this로 가져올 수 있다.
            for (i in 1..45) {
                if (mPickNumberSet.contains(i)) {
                    // 추가된 숫자는 랜덤 로직을 돌 필요가 없다!!
                    continue
                }
                this.add(i)
            }
        }

        resultlist.shuffle()

        // 이미 선택된 번호를 제외하여 sort한 리스트 반환
        return mPickNumberSet.toList() + resultlist.subList(0, 6 - mPickNumberSet.size)
    }

    /**
     * 실행 여부 및,, 초기화 처리
     */
    private fun resetInit() {
        mResetButton.setOnClickListener {
            mDidRun = false
            mPickNumberSet.clear()
            mTextList.forEach {
                // forEach : 리스트를 순차적으로 하나씩 꺼내준다
                it.isVisible = false
            }
        }
    }

    /**
     * 번호 추가하기
     */
    private fun addNumBtn() {
        mAddButton.setOnClickListener {
            if (mDidRun) {
                // 이미 자동생성 버튼을 눌렀을 때
                Toast.makeText(this, "초기화 버튼을 누른 후 시도해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (mPickNumberSet.size >= 5) {
                // 이미 숫자가 모두 생성되었을 경우
                Toast.makeText(this, "번호는 5개까지 지정할 수 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (mPickNumberSet.contains(mNumberPicker.value)) {
                // 이미 선택된 숫자라면
                Toast.makeText(this, "이미 선택한 번호입니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 이미 선택된 번호만큼 text 리스트가 초기화 된다.
            val textView = mTextList[mPickNumberSet.size]
            textView.isVisible = true
            textView.text = mNumberPicker.value.toString()
            textView.background = ContextCompat.getDrawable(this, R.drawable.circle_blue)

            setNumberBackground(mNumberPicker.value, textview = textView)

            mPickNumberSet.add(mNumberPicker.value)
        }
    }

    /**
     * 추가한 번호의 Background 설정
     */
    private fun setNumberBackground(number: Int, textview: TextView) {
        when (number) {
            in 1..10 -> textview.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 11..20 -> textview.background =
                ContextCompat.getDrawable(this, R.drawable.circle_yellow)
            in 21..30 -> textview.background =
                ContextCompat.getDrawable(this, R.drawable.circle_red)
            in 31..40 -> textview.background =
                ContextCompat.getDrawable(this, R.drawable.circle_gray)
            else -> textview.background =
                ContextCompat.getDrawable(this, R.drawable.circle_green)
        }
    }
}