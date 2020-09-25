package ru.rsue.android.droidquestkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class QuestActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "QuestActivity"
        private const val KEY_INDEX = "index"
        private const val KEY_INDEX_TWO = "index1"
        private const val KEY_INDEX_QUESTION = "index2"
        private const val REQUEST_CODE_DECEIT = 0
    }

    private lateinit var mTrueButton: Button
    private lateinit var mFalseButton: Button
    private lateinit var mNextButton: ImageButton
    private lateinit var mBackButton: ImageButton
    private lateinit var mDeceitButton: Button
    private lateinit var mQuestionTextView: TextView

    private val mQuestionBank = listOf(
        Question(R.string.question_android, true),
        Question(R.string.question_linear, false),
        Question(R.string.question_service, false),
        Question(R.string.question_res, true),
        Question(R.string.question_manifest, true),
    )
    private var mCurrentIndex = 0
    private var mIsDeceiter = false
    private var mUsedHint = BooleanArray(mQuestionBank.size)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "onCreate(Bundle) вызван")

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            mIsDeceiter = savedInstanceState.getBoolean(KEY_INDEX_TWO, false)
            mUsedHint = savedInstanceState.getBooleanArray(KEY_INDEX_QUESTION)!!
        }

        mTrueButton = findViewById(R.id.true_button)
        mTrueButton.setOnClickListener {
            checkAnswer(true)
        }

        mFalseButton = findViewById(R.id.false_button)
        mFalseButton.setOnClickListener {
            checkAnswer(false)
        }

        mNextButton = findViewById(R.id.next_button)
        mNextButton.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            mIsDeceiter = false
            updateQuestion()
        }

        mBackButton = findViewById(R.id.back_button)
        mBackButton.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 9) % mQuestionBank.size
            updateQuestion()
        }

        mQuestionTextView = findViewById(R.id.question_text_view)
        mQuestionTextView.setOnClickListener {
            mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.size
            updateQuestion()
        }

        mDeceitButton = findViewById(R.id.deceit_button)
        mDeceitButton.setOnClickListener {
            val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue
            val intent = DeceitActivity.newIntent(this, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_DECEIT)
        }
        updateQuestion()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK)
            return

        if (requestCode == REQUEST_CODE_DECEIT) {
            if (data == null) {
                return
            }
            mIsDeceiter = DeceitActivity.wasAnswerShown(data)
            mUsedHint[mCurrentIndex] = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt(KEY_INDEX, mCurrentIndex)
        outState.putBoolean(KEY_INDEX_TWO, mIsDeceiter)
        outState.putBooleanArray(KEY_INDEX_QUESTION, mUsedHint)
    }

    private fun updateQuestion() {
        val question = mQuestionBank[mCurrentIndex].textResId
        mQuestionTextView.setText(question)
    }

    private fun checkAnswer(userPressedTrue: Boolean) {
        val answerIsTrue = mQuestionBank[mCurrentIndex].answerTrue
        val messageResId = if (mIsDeceiter || mUsedHint[mCurrentIndex])
            R.string.judgment_toast
        else if (userPressedTrue == answerIsTrue)
                R.string.correct_toast
            else
                R.string.incorrect_toast

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() $mCurrentIndex")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() $mCurrentIndex")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() $mCurrentIndex")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() $mCurrentIndex")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() $mCurrentIndex")
    }
}
