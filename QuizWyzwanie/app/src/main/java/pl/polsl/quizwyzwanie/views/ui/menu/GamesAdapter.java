package pl.polsl.quizwyzwanie.views.ui.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.polsl.quizwyzwanie.R;
import pl.polsl.quizwyzwanie.views.domain.model.Game;
import pl.polsl.quizwyzwanie.views.ui.MainActivity;
import pl.polsl.quizwyzwanie.views.ui.game.GameFragment;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private List<Game> games;
    private MainActivity activity;

    public GamesAdapter(MainActivity activity,List<Game> games) {
        this.activity = activity;
        this.games = games;
    }

    @Override
    public GamesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.entry_games_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GamesAdapter.ViewHolder holder, final int position) {
        holder.mainLl.setOnClickListener(v -> {
            activity.switchToFragment(new GameFragment(),
                    GameFragment.class.getName(), MenuFragment.class.getName());
        });
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.entry_games_main_ll)
        LinearLayout mainLl;

        public ViewHolder(View rowView) {
            super(rowView);
            ButterKnife.bind(this, rowView);
        }
    }
}
