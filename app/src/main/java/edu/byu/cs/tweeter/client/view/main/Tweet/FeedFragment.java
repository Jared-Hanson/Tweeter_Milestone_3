package edu.byu.cs.tweeter.client.view.main.Tweet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Tweet;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.client.model.service.GetUserDataServiceProxy;
import edu.byu.cs.tweeter.model.service.request.GetUserDataRequest;
import edu.byu.cs.tweeter.model.service.request.StoryRequest;
import edu.byu.cs.tweeter.model.service.response.GetUserDataResponse;
import edu.byu.cs.tweeter.model.service.response.StoryResponse;
import edu.byu.cs.tweeter.client.presenter.FeedPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFeedTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetUserDataTask;
import edu.byu.cs.tweeter.client.view.main.UserActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

public class FeedFragment extends Fragment implements FeedPresenter.View, GetUserDataTask.Observer {
    private static final String LOG_TAG = "FeedFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;

    private static final int PAGE_SIZE = 10;

    private User user;
    private AuthToken authToken;
    private FeedPresenter presenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    public static FeedFragment newInstance(User user, AuthToken authToken) {
        FeedFragment fragment = new FeedFragment();

        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);

        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        //noinspection ConstantConditions
        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new FeedPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    @Override
    public void getUserDataSuccessful(GetUserDataResponse getUserDataResponse) {
        //put function here
        Log.d("count", "getUserDataSuccessful: " + getUserDataResponse.getUser().getAlias());
        User mentionedUser = getUserDataResponse.getUser();
        createUseActivity(mentionedUser);
    }

    public void createUseActivity(User mentionedUser) {
        Intent intent = new Intent(getContext(), UserActivity.class);

        intent.putExtra(UserActivity.CURRENT_USER_KEY, mentionedUser);
        //intent.putExtra(UserActivity.CURRENT_USER_KEY, user);
        intent.putExtra(UserActivity.AUTH_TOKEN_KEY, authToken);
        intent.putExtra(UserActivity.LOGGED_IN_USER, user);


        startActivity(intent);
    }

    @Override
    public void getUserDataUnsuccessful(GetUserDataResponse getUserDataResponse) {
        Toast.makeText(getActivity(), "Failed to get user data. " + getUserDataResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    /**
     * The ViewHolder for the RecyclerView that displays the Following data.
     */
    private class FeedHolder extends RecyclerView.ViewHolder {

        private final ImageView userImage;
        private final TextView userAlias;
        private final TextView userName;
        private final TextView date;
        private final TextView tweetBody;

        /**
         * Creates an instance and sets an OnClickListener for the user's row.
         *
         * @param itemView the view on which the user will be displayed.
         */
        FeedHolder(@NonNull View itemView, int viewType) {
            super(itemView);

            if(viewType == ITEM_VIEW) {
                userImage = itemView.findViewById(R.id.userImage);
                userAlias = itemView.findViewById(R.id.userAlias);
                userName = itemView.findViewById(R.id.userName);
                date = itemView.findViewById(R.id.Date);
                tweetBody = itemView.findViewById(R.id.TweetBody);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getContext(), "You selected '" + userName.getText() + "'.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                userImage = null;
                userAlias = null;
                userName = null;
                date = null;
                tweetBody = null;
            }
        }

        /**
         * Binds the user's data to the view.
         *
         * @param tweet the user.
         */
        void bindTweet(Tweet tweet) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(tweet.getAuthor().getImageBytes()));
            userAlias.setText(tweet.getAuthor().getAlias());
            userName.setText(tweet.getAuthor().getName());

            date.setText(tweet.getDate().toString());
            tweetBody.setMovementMethod(LinkMovementMethod.getInstance());

            String body = tweet.getBody();
            int start;
            int end;
            if(body.contains("https")) {
                SpannableString spannableString = new SpannableString(body);
                end = -1;
                while((start = body.indexOf("https", end + 1)) != -1) {
                    end = start + 1;
                    for(int i = end; i < body.length(); i++) {
                        if(Character.isWhitespace(body.charAt(i))) {
                            break;
                        }
                        end = i + 1;
                    }
                    String url = body.substring(start, end);
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Uri uri = Uri.parse(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tweetBody.setText(spannableString);
            } else if (body.contains("@")) {
                SpannableString spannableString = new SpannableString(body);
                end = -1;
                while((start = body.indexOf("@", end + 1)) != -1) {
                    end = start + 1;
                    for(int i = end; i < body.length(); i++) {
                        if(Character.isWhitespace(body.charAt(i))) {
                            break;
                        }
                        end = i + 1;
                    }
                    String mentionAlias = body.substring(start, end);
                    spannableString.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            GetUserDataRequest getUserDataRequest = new GetUserDataRequest(mentionAlias);
                            GetUserDataServiceProxy getUserDataService = new GetUserDataServiceProxy();
                            GetUserDataTask getUserDataTask = new GetUserDataTask(getUserDataService, FeedFragment.this);
                            getUserDataTask.execute(getUserDataRequest);

                        }
                    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                tweetBody.setText(spannableString);
            } else {
                tweetBody.setText(body);
            }
        }
    }

    /**
     * The adapter for the RecyclerView that displays the Following data.
     */
    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<FeedHolder> implements GetFeedTask.Observer {

        private final List<Tweet> tweets = new ArrayList<>();

        private Tweet lastTweet;

        private boolean hasMorePages;
        private boolean isLoading = false;

        /**
         * Creates an instance and loads the first page of following data.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        StoryRecyclerViewAdapter() {
            loadMoreItems();
        }

        /**
         * Adds new users to the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that items have been added.
         *
         * @param newTweets the users to add.
         */
        void addItems(List<Tweet> newTweets) {
            int startInsertPosition = tweets.size();
            tweets.addAll(newTweets);
            this.notifyItemRangeInserted(startInsertPosition, newTweets.size());
        }

        /**
         * Adds a single user to the list from which the RecyclerView retrieves the users it
         * displays and notifies the RecyclerView that an item has been added.
         *
         * @param tweet the user to add.
         */
        void addItem(Tweet tweet) {
            tweets.add(tweet);
            this.notifyItemInserted(tweets.size() - 1);
        }

        /**
         * Removes a user from the list from which the RecyclerView retrieves the users it displays
         * and notifies the RecyclerView that an item has been removed.
         *
         * @param tweet the user to remove.
         */
        void removeItem(Tweet tweet) {
            int position = tweets.indexOf(tweet);
            tweets.remove(position);
            this.notifyItemRemoved(position);
        }

        /**
         *  Creates a view holder for a followee to be displayed in the RecyclerView or for a message
         *  indicating that new rows are being loaded if we are waiting for rows to load.
         *
         * @param parent the parent view.
         * @param viewType the type of the view (ignored in the current implementation).
         * @return the view holder.
         */
        @NonNull
        @Override
        public FeedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(FeedFragment.this.getContext());
            View view;

            if(viewType == LOADING_DATA_VIEW) {
                view =layoutInflater.inflate(R.layout.loading_row, parent, false);

            } else {
                view = layoutInflater.inflate(R.layout.tweet_row, parent, false);
            }

            return new FeedHolder(view, viewType);
        }

        /**
         * Binds the followee at the specified position unless we are currently loading new data. If
         * we are loading new data, the display at that position will be the data loading footer.
         *
         *
         * @param position the position (in the list of followees) that contains the followee to be
         *                 bound.
         */
        @Override
        public void onBindViewHolder(@NonNull FeedHolder feedHolder, int position) {
            if(!isLoading) {
                feedHolder.bindTweet(tweets.get(position));
            }
        }

        /**
         * Returns the current number of followees available for display.
         * @return the number of followees available for display.
         */
        @Override
        public int getItemCount() {
            return tweets.size();
        }

        /**
         * Returns the type of the view that should be displayed for the item currently at the
         * specified position.
         *
         * @param position the position of the items whose view type is to be returned.
         * @return the view type.
         */
        @Override
        public int getItemViewType(int position) {
            return (position == tweets.size() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        /**
         * Causes the Adapter to display a loading footer and make a request to get more following
         * data.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetFeedTask getFeedTask = new GetFeedTask(presenter, this);
            StoryRequest request = new StoryRequest(user.getAlias(), PAGE_SIZE, (lastTweet == null ? null : lastTweet));
            getFeedTask.execute(request);
        }

        /**
         * A callback indicating more following data has been received. Loads the new followees
         * and removes the loading footer.
         *
         * @param storyResponse the asynchronous response to the request to load more items.
         */
        @Override
        public void tweetsRetrieved(StoryResponse storyResponse) {
            List<Tweet> tweets = storyResponse.getTweets();

            lastTweet = (tweets.size() > 0) ? tweets.get(tweets.size() -1) : null;
            hasMorePages = storyResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(tweets);
        }

        /**
         * A callback indicating that an exception was thrown by the presenter.
         *
         * @param exception the exception.
         */
        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * Adds a dummy user to the list of users so the RecyclerView will display a view (the
         * loading footer view) at the bottom of the list.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        private void addLoadingFooter() {
            LocalDate date = LocalDate.now();
            addItem(new Tweet(new User("Dummy", "User", ""), "User", date.toEpochDay()));
        }

        /**
         * Removes the dummy user from the list of users so the RecyclerView will stop displaying
         * the loading footer at the bottom of the list.
         */
        private void removeLoadingFooter() {
            removeItem(tweets.get(tweets.size() - 1));
        }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }

}
