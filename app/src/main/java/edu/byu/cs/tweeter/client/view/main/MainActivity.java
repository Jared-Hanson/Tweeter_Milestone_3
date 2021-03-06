package edu.byu.cs.tweeter.client.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

import edu.byu.cs.tweeter.R;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.service.request.FollowDataRequest;
import edu.byu.cs.tweeter.model.service.request.LogoutRequest;
import edu.byu.cs.tweeter.model.service.request.TweetRequest;
import edu.byu.cs.tweeter.model.service.response.FollowDataResponse;
import edu.byu.cs.tweeter.model.service.response.LogoutResponse;
import edu.byu.cs.tweeter.model.service.response.TweetResponse;
import edu.byu.cs.tweeter.client.presenter.FollowerDataPresenter;
import edu.byu.cs.tweeter.client.presenter.LogoutPresenter;
import edu.byu.cs.tweeter.client.presenter.TweetPresenter;
import edu.byu.cs.tweeter.client.view.asyncTasks.GetFollowerDataTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.LogoutTask;
import edu.byu.cs.tweeter.client.view.asyncTasks.PostTweetTask;
import edu.byu.cs.tweeter.client.view.main.Login.LoginActivity;
import edu.byu.cs.tweeter.client.view.util.ImageUtils;

import static edu.byu.cs.tweeter.R.layout.activity_main;
import static java.time.ZoneOffset.UTC;

/**
 * The main activity for the application. Contains tabs for feed, story, following, and followers.
 */
public class MainActivity extends AppCompatActivity implements LogoutTask.Observer {

    public static final String CURRENT_USER_KEY = "CurrentUser";
    public static final String AUTH_TOKEN_KEY = "AuthTokenKey";
    private static final String LOG_TAG = "MainActivity";

    private Toast logoutToast;
    private Toast tweetToast;
    private User user;
    private AuthToken authToken;
    private FollowerDataPresenter dataPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
        if(user == null) {
            throw new RuntimeException("User not passed to activity");
        }

        authToken = (AuthToken) getIntent().getSerializableExtra(AUTH_TOKEN_KEY);
        dataPresenter = new FollowerDataPresenter();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), user, authToken);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTweetPopUp();
                //createUserFeedTest();



            }
        });



        TextView userName = findViewById(R.id.userName);
        userName.setText(user.getName());

        TextView userAlias = findViewById(R.id.userAlias);
        userAlias.setText(user.getAlias());

        ImageView userImageView = findViewById(R.id.userImage);
        userImageView.setImageDrawable(ImageUtils.drawableFromByteArray(user.getImageBytes()));

        TextView followeeCount = findViewById(R.id.followeeCount);
        followeeCount.setText(getString(R.string.followeeCount, 42));

        TextView followerCount = findViewById(R.id.followerCount);
        followerCount.setText(getString(R.string.followerCount, 27));
        new getFollowerData().GetDataFunction(new FollowDataRequest(user));
    }

    // only needed for testing with specific users here
    private static byte[] readContentIntoByteArray(File file)
    {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try
        {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
            for (int i = 0; i < bFile.length; i++)
            {
                System.out.print((char) bFile[i]);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bFile;
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);


        return true;
    }


    public void logout(MenuItem item) {
        logoutToast = Toast.makeText(this, "logging out", Toast.LENGTH_LONG);
        logoutToast.show();

        LogoutPresenter logoutPresenter = new LogoutPresenter();

        LogoutRequest logoutRequest = new LogoutRequest(user, authToken);
        LogoutTask logoutTask = new LogoutTask(logoutPresenter, this);
        logoutTask.execute(logoutRequest);
    }

    @Override
    public void logoutSuccessful(LogoutResponse logoutResponse) {
        Intent resetIntent = new Intent(this, LoginActivity.class);

        startActivity(resetIntent);
    }

    @Override
    public void logoutUnsuccessful(LogoutResponse logoutResponse) {
        Toast.makeText(this, "Failed to login. " + logoutResponse.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleException(Exception ex) {
        Log.e(LOG_TAG, ex.getMessage(), ex);
        Toast.makeText(this, "Failed to login because of exception: " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void createTweetPopUp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Compose your Tweet");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);


        // Set up the buttons



        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String inText = input.getText().toString();
                User user = (User) getIntent().getSerializableExtra(CURRENT_USER_KEY);
                Long date = LocalDateTime.now().toEpochSecond(UTC);
                TweetRequest request = new TweetRequest(user.getAlias(), inText, date, authToken);
                new postTweetDialogBOX().postTweet(request);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }


private class postTweetDialogBOX implements PostTweetTask.Observer {

    void postTweet(TweetRequest request) {
        TweetPresenter presenter = new TweetPresenter();
        PostTweetTask tweet = new PostTweetTask(presenter, this);
        tweet.execute(request);
    }


    @Override
    public void postTweetResponse(TweetResponse tweetResponse) {
        tweetToast = Toast.makeText(getApplicationContext(), "Tweet Succesfully added to facade", Toast.LENGTH_LONG);
        if (tweetResponse.isSuccess()) {
            tweetToast.show();
        }

    }

    @Override
    public void handleException(Exception exception) {
        Log.e(LOG_TAG, exception.getMessage(), exception);
        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

    }
}

    private class getFollowerData implements GetFollowerDataTask.Observer {

        void GetDataFunction(FollowDataRequest request) {
            //FollowerDataPresenter presenter = new FollowerDataPresenter(dataPresenter);
            GetFollowerDataTask data = new GetFollowerDataTask(dataPresenter, this);
            data.execute(request);
        }


        @Override
        public void getData(FollowDataResponse res) {
            TextView followeeCount = findViewById(R.id.followeeCount);
            followeeCount.setText(getString(R.string.followeeCount, res.getFollowing()));

            TextView followerCount = findViewById(R.id.followerCount);
            followerCount.setText(getString(R.string.followerCount, res.getFollowers()));

        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

        }
    }



}
