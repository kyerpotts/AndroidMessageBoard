package curtin.edu.assignment2a.postsrecyclerview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import curtin.edu.assignment2a.R;
import curtin.edu.assignment2a.model.Post;
import curtin.edu.assignment2a.model.User;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostItemViewHolder> {
    // List is initialised as empty to prevent null pointer exceptions if internet calls returns a null value
    List<Post> postList = Collections.emptyList();

    @NonNull
    @Override
    public PostItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostItemViewHolder holder, int position) {
        Post currentPost = postList.get(position);
        holder.tvTitle.setText(currentPost.getTitle());
        holder.tvBody.setText(currentPost.getBody());

        boolean isVisible = currentPost.isVisible();
        holder.expandedLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void setData(List<Post> postList) {
        this.postList = postList;
        notifyDataSetChanged();
    }

    public class PostItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvBody;

        ConstraintLayout expandedLayout;

        public PostItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvBody = itemView.findViewById(R.id.tvBody);
            expandedLayout = itemView.findViewById(R.id.expandedPostRow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Post post = postList.get(getAbsoluteAdapterPosition());
                    post.setVisible(!post.isVisible());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });
        }
    }
}
