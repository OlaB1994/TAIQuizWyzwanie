package pl.polsl.quizwyzwanie.views.ui.game;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.RoundResult;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;

public class RoundsAdapter extends RecyclerView.Adapter<RoundsAdapter.ViewHolder> {

    private List<RoundResult> rounds;
    private MainActivity activity;

    public RoundsAdapter(MainActivity activity, List<RoundResult> rounds) {
        this.activity = activity;
        this.rounds = rounds;
    }

    @Override
    public RoundsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.entry_rounds_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoundsAdapter.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return rounds != null ? rounds.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        public ViewHolder(View rowView) {
            super(rowView);
            ButterKnife.bind(this, rowView);
        }
    }
}
