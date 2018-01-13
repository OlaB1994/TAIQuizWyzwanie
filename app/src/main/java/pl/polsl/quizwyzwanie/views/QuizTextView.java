package pl.polsl.quizwyzwanie.views;

import android.content.Context;
import android.util.AttributeSet;

public class QuizTextView extends android.support.v7.widget.AppCompatTextView {

    public QuizTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
      //  setTypeface(TypeFace.get(getContext(), FONT_NORMAL));
    }

}