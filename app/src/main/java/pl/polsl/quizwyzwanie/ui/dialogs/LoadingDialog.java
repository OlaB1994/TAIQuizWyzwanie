package pl.polsl.quizwyzwanie.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.Window;
import android.widget.ProgressBar;


import butterknife.BindView;
import butterknife.ButterKnife;
import pl.polsl.quizwyzwanie.R;

public class LoadingDialog extends Dialog {

    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.dialog_loading_loading_pb)
    ProgressBar loadingPb;

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog);
    }

    public void setupDialog() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        this.setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this);
        changeProgressBarColor();
    }

    private void changeProgressBarColor() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            loadingPb.getIndeterminateDrawable()
                    .setColorFilter(getProgressBarColor(), PorterDuff.Mode.MULTIPLY);
        }
    }

    private int getProgressBarColor() {
        return getContext().getResources().getColor(R.color.colorPrimaryDark);
    }
}
