package ca.ualberta.cs.lonelytwitter;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by esports on 2/17/16.
 */
public class ElasticsearchTweetController {

    private static JestDroidClient client;

    //TODO: A function that gets tweets
    public static ArrayList<Tweet> getTweets() {
        verifyConfig();
        return null;
    }

    public static class GetTweetsTask extends AsyncTask<String, Void, ArrayList<NormalTweet>> {
        @Override
        protected ArrayList<NormalTweet> doInBackground(String... strings) {
            String search_string = strings[0];
            ArrayList<NormalTweet> tweets = new ArrayList<NormalTweet>();

            Search search = new Search.Builder(search_string).addIndex("testing").addType("tweet").build();
            try {
                SearchResult execute = client.execute(search);
                if (execute.isSucceeded()) {
                    List<NormalTweet> found = execute.getSourceAsObjectList(NormalTweet.class);
                    tweets.addAll(found);
                } else {
                    Log.i("TODO", "Search failed");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tweets;
        }
    }
    //TODO: A function that adds a tweet
    /*
    public static void addTweet(Tweet tweet) {

        verifyConfig();

        Index index = new Index.Builder(tweet).index("testing").type("tweet").build();
        try {
            DocumentResult execute = client.execute(index);
            if (execute.isSucceeded()) {
                tweet.setId(execute.getId());
            } else {
                Log.e("TODO", "Insert failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    public static class AddTweetTask extends AsyncTask<NormalTweet, Void, Void> {

        @Override
        protected Void doInBackground(NormalTweet... tweets) {
            verifyConfig();
            for(NormalTweet tweet : tweets) {
                Index index = new Index.Builder(tweet).index("testing").type("tweet").build();
                try {
                    DocumentResult execute = client.execute(index);
                    if (execute.isSucceeded()) {
                        tweet.setId(execute.getId());
                    } else {
                        Log.e("TODO", "Insert failed");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static void verifyConfig() {
        if (client == null) {
            DroidClientConfig.Builder bconfig = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = bconfig.build();

            JestClientFactory fact = new JestClientFactory();
            fact.setDroidClientConfig(config);
            client = (JestDroidClient) fact.getObject();
        }
    }

}
