package ru.rsue.android.droidquestkt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class DeceitActivity : AppCompatActivity(){
    companion object {
        var mIsDeceiter = false
        const val KEY_INDEX = "index"
        const val EXTRA_ANSWER_IS_TRUE = "ru.rsue.android.droidquestkt.answer_is_true"
        const val EXTRA_ANSWER_SHOWN = "ru.rsue.android.droidquestkt.answer_shown"

        fun newIntent(packageContext: Context?, answerIsTrue: Boolean):
                Intent? {
            val intent = Intent(packageContext, DeceitActivity::class.java)
            return intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
        }

        fun wasAnswerShown(result: Intent) = result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false)
    }

    private var mAnswerTextView: TextView? = null
    private lateinit var mShowAnswer: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deceit)
        val mAnswerIsTrue = intent.getBooleanExtra(
            EXTRA_ANSWER_IS_TRUE, false
        )

        if (savedInstanceState != null) mIsDeceiter = savedInstanceState.getBoolean(KEY_INDEX)
        if (mIsDeceiter)
            mAnswerTextView = findViewById(R.id.answer_text_view)
            mAnswerTextView?.setText(
                if (mAnswerIsTrue) R.string.true_button
                else R.string.false_button)
            )
        else mAnswerTextView?.setText("")

        setAnswerShownResult(true)

        mAnswerTextView = findViewById(R.id.answer_text_view)
        mShowAnswer = findViewById(R.id.show_answer_button)
        mShowAnswer.setOnClickListener {
            mAnswerTextView.setText(
                if (mAnswerIsTrue) R.string.true_button
                else R.string.false_button)
            setAnswerShownResult(true)
            mIsDeceiter = true
        }
    }

    override fun onSaveInstanceState(saveInstanceState: Bundle) {
        super.onSaveInstanceState(saveInstanceState)
        saveInstanceState.putBoolean(KEY_INDEX, mIsDeceiter)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent()
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        setResult(RESULT_OK, data)
    }

}
