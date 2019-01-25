package com.andoresu.themoviedb.core.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andoresu.themoviedb.R;
import com.andoresu.themoviedb.core.movies.data.Actor;
import com.andoresu.themoviedb.core.movies.data.Casts;
import com.andoresu.themoviedb.core.movies.data.Movie;
import com.andoresu.themoviedb.core.movies.data.Movies;
import com.andoresu.themoviedb.utils.BaseRecyclerViewAdapter;
import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.ArcProgress;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andoresu.themoviedb.utils.MyUtils.glideRequestOptions;

public class ActorAdapter extends BaseRecyclerViewAdapter<Actor> {

    public Casts casts;

    public ActorAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_actor;
    }

    @NonNull
    @Override
    public BaseViewHolder<Actor> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<Actor> holder, int position) {
        super.onBindViewHolder(holder, position);
        ActorViewHolder viewHolder = (ActorViewHolder) holder;
        Actor actor = getItem(position);

        viewHolder.actorCharacterNameTextView.setText(actor.character);
        viewHolder.actorNameTextView.setText(actor.name);

        Glide.with(context)
                .load(actor.getProfilePath())
                .apply(glideRequestOptions(context))
                .into(viewHolder.actorProfileImageView);

    }

    public void setCasts(Casts casts){
        this.casts = casts;
        addAll(casts.cast);
    }

    public static class ActorViewHolder extends BaseViewHolder<Actor> {

        @BindView(R.id.actorProfileImageView)
        ImageView actorProfileImageView;

        @BindView(R.id.actorCharacterNameTextView)
        TextView actorCharacterNameTextView;

        @BindView(R.id.actorNameTextView)
        TextView actorNameTextView;

        public ActorViewHolder(View v){
            super(v);
            ButterKnife.bind(this, v);
        }

    }
}
