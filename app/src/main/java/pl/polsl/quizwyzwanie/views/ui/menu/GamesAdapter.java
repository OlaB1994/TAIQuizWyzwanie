package pl.polsl.quizwyzwanie.views.ui.menu;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    public GamesAdapter(MainActivity activity, List<Game> games) {
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
        setIcon(games.get(position), holder.iconIv);
        setText(games.get(position), holder.textTv);
        holder.mainLl.setOnClickListener(v -> {
            if (games.get(position).getState() == Game.STATE_YOUR_TURN)
                activity.switchToFragment(new GameFragment(),
                        GameFragment.class.getName(), MenuFragment.class.getName());
        });
    }

    private void setText(Game game, TextView textTv) {
        if (game.getState() == Game.STATE_WAITING)
            textTv.setText(activity.getString(R.string.entry_games_state_waiting, game.getOpponentUsername()));
        else if (game.getState() == Game.STATE_FINISHED)
            textTv.setText(activity.getString(R.string.entry_games_state_finished, game.getOpponentUsername()));
        else if (game.getState() == Game.STATE_YOUR_TURN)
            textTv.setText(activity.getString(R.string.entry_games_state_your_turn, game.getOpponentUsername()));
    }

    private void setIcon(Game game, ImageView iconIv) {
        if (game.getState() == Game.STATE_WAITING)
            iconIv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_waiting));
        else if (game.getState() == Game.STATE_FINISHED)
            iconIv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_finish));
        else if (game.getState() == Game.STATE_YOUR_TURN)
            iconIv.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_your_turn));
    }

    @Override
    public int getItemCount() {
        return games != null ? games.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.entry_games_main_ll)
        LinearLayout mainLl;
        @BindView(R.id.entry_games_list_icon_iv)
        ImageView iconIv;
        @BindView(R.id.entry_games_text_tv)
        TextView textTv;

        public ViewHolder(View rowView) {
            super(rowView);
            ButterKnife.bind(this, rowView);
        }
    }
}
