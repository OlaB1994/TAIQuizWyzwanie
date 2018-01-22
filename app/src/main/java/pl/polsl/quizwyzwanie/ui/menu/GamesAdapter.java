package pl.polsl.quizwyzwanie.ui.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import pl.polsl.quizwyzwanie.domain.model.AppUser;
import pl.polsl.quizwyzwanie.domain.model.Game;
import pl.polsl.quizwyzwanie.ui.MainActivity;
import pl.polsl.quizwyzwanie.ui.game.GameFragment;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.ViewHolder> {

    private final List<Game> games;
    private final MainActivity activity;
    private final Bundle bundleData;
    private final AppUser user;

    public GamesAdapter(MainActivity activity, List<Game> games, Bundle bundleData) {
        this.activity = activity;
        this.games = games;
        this.bundleData = bundleData;
        user = (AppUser) bundleData.getSerializable("user");
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
            GameFragment gameFragment;
            Bundle arguments = bundleData;
            gameFragment = new GameFragment();
            arguments.putSerializable("game", games.get(position));

            gameFragment.setArguments(arguments);
            activity.switchToFragmentWithBackStack(gameFragment,
                    GameFragment.class.getName(), MenuFragment.class.getName());
        });
    }

    @SuppressLint("StringFormatInvalid")
    private void setText(Game game, TextView textTv) {
        if (game.getState() == Game.STATE_WAITING)
            textTv.setText(activity.getString(R.string.entry_games_state_waiting, game.getOpponentUsername(user)));
        else if (game.getState() == Game.STATE_FINISHED)
            textTv.setText(activity.getString(R.string.entry_games_state_finished, game.getOpponentUsername(user)));
        else if (game.getState() == Game.STATE_YOUR_TURN)
            textTv.setText(activity.getString(R.string.entry_games_state_your_turn, game.getOpponentUsername(user)));
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
