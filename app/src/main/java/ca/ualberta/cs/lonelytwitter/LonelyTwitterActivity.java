package ca.ualberta.cs.lonelytwitter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class LonelyTwitterActivity extends Activity {

    private EditText bodyText;
    private ListView oldTweetsList;
    // Moved the ArrayList<Tweet> tweets to be local, it
    // is not used anywhere and can accidentally be changed
    private TweetList myTweets;
    private ArrayAdapter<Tweet> adapter;

    // Save button changed to be local.
    public ArrayAdapter<Tweet> getAdapter() {
        return adapter;
    }


    private ImageButton pictureButton;
    private Bitmap thumbnail;

    int numImportant;

    static final int REQUEST_CAPTURING_IMAGE = 1234;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        bodyText = (EditText) findViewById(R.id.tweetMessage);
        oldTweetsList = (ListView) findViewById(R.id.tweetsList);

	// http://developer.android.com/training/camera/photobasics.html
        pictureButton = (ImageButton) findViewById(R.id.pictureButton);
        pictureButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_CAPTURING_IMAGE);
                }
            }
        });

        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                String text = bodyText.getText().toString();
                NormalTweet latestTweet = new NormalTweet(text);

                myTweets.addStuff(latestTweet);

                latestTweet.addThumbnail(thumbnail);
                adapter.insert(latestTweet, 0);
                adapter.notifyDataSetChanged();

                // Add the tweet to Elasticsearch
                ElasticsearchTweetController.AddTweetTask addTweetTask = new ElasticsearchTweetController.AddTweetTask();
                addTweetTask.execute(latestTweet);


                // http://stackoverflow.com/questions/11835251/remove-image-resource-of-imagebutton

                bodyText.setText("");
                pictureButton.setImageResource(android.R.color.transparent);
                thumbnail = null;

                setResult(RESULT_OK);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Get the latest tweets from Elasticsearch
        ElasticsearchTweetController.GetTweetsTask getTweetsTask = new ElasticsearchTweetController.GetTweetsTask();
//        getTweetsTask.execute("test");
        getTweetsTask.execute("");
        try {
            ArrayList<Tweet> tweets = new ArrayList<Tweet>();
            tweets.addAll(getTweetsTask.get());
            myTweets = new TweetList(tweets);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //Count important tweets
        countthing();

//        adapter = new ArrayAdapter<Tweet>(this, R.layout.list_item, tweets);
        // Binds tweet list with view, so when our array updates, the view updates with it
        //adapter = new TweetAdapter(this, tweets); /* NEW! */
        adapter = new TweetAdapter(this, myTweets.getTweets());
        oldTweetsList.setAdapter(adapter);
    }

    private void countthing() {
        numImportant = 0;
        for ( Tweet aTweet: myTweets.getTweets() ){
            if (aTweet.isImportant() == Boolean.TRUE){
                numImportant++;
            }
        }
    }

    // http://developer.android.com/training/camera/photobasics.html
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == REQUEST_CAPTURING_IMAGE && resultCode == RESULT_OK){
            Bundle extras = intent.getExtras();
            thumbnail = (Bitmap) extras.get("data");
            pictureButton.setImageBitmap(thumbnail);
        }
    }
}
