package curtin.edu.assignment2a.userrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import curtin.edu.assignment2a.R;
import curtin.edu.assignment2a.UserListFragmentDirections;
import curtin.edu.assignment2a.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserItemViewHolder> {
    private List<User> userList = Collections.emptyList();

    @NonNull
    @Override
    public UserAdapter.UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserItemViewHolder holder, int position) {
        User currentUser = userList.get(position);
        holder.tvUsername.setText(currentUser.getUsername());
        holder.tvUsersName.setText(currentUser.getName());
        holder.tvEmail.setText(currentUser.getEmail());
        holder.tvPhone.setText(currentUser.getPhone());
        holder.tvWebsite.setText(currentUser.getWebsite());

        boolean isVisible = currentUser.isVisible();
        holder.expandedLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void setData(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    protected class UserItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername;
        TextView tvUsersName;
        TextView tvEmail;
        TextView tvPhone;
        TextView tvWebsite;
        Button btnViewPosts;

        ConstraintLayout expandedLayout;


        public UserItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsersName = itemView.findViewById(R.id.tvUsersName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvWebsite = itemView.findViewById(R.id.tvWebsite);
            btnViewPosts = itemView.findViewById(R.id.btnViewPosts);
            expandedLayout = itemView.findViewById(R.id.expandedUserRow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user = userList.get(getAbsoluteAdapterPosition());
                    user.setVisible(!user.isVisible());
                    notifyItemChanged(getAbsoluteAdapterPosition());
                }
            });

            btnViewPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavDirections action = UserListFragmentDirections.actionUserListFragmentToUserPostsFragment(userList.get(getAdapterPosition()).getId());
                    Navigation.findNavController(itemView).navigate(action);
                }
            });
        }
    }
}