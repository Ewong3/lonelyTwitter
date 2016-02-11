package ca.ualberta.cs.lonelytwitter;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ViewAsserts;
import android.widget.TextView;

/**
 * Created by sajediba on 2/8/16.
 */
public class IntentReaderActivityTest extends ActivityInstrumentationTestCase2{

    public IntentReaderActivityTest() {
        super(IntentReaderActivity.class);
    }

    //
    //
    public void testSendText() {
        Intent intent = new Intent();
        intent.putExtra(IntentReaderActivity.TEXT_TO_TRANSFORM_KEY, "Message 2");
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        assertEquals("Did not get text", "Message 2", ira.getText());

    }

    public void testDisplayText() {
        Intent intent = new Intent();
        intent.putExtra(IntentReaderActivity.TEXT_TO_TRANSFORM_KEY, "Message 3");
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        TextView textView = (TextView) ira.findViewById(R.id.intentText);
        assertEquals(textView.getText().toString(), "Message 3");
    }

    public void testDoubleText() {
        Intent intent = new Intent();
        intent.putExtra(IntentReaderActivity.TEXT_TO_TRANSFORM_KEY, "Message 4");
        intent.putExtra(IntentReaderActivity.MODE_OF_TRANSFORM_KEY, IntentReaderActivity.DOUBLE);
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        assertEquals("Text should repeat", "Message 4Message 4", ira.getText());
    }
    //
    //

    //TODO: Add your code here ...
//-------------------------------------------------------------------------------
    public void testReverseText() {
        Intent intent = new Intent();
        intent.putExtra(IntentReaderActivity.TEXT_TO_TRANSFORM_KEY, "Message 5");
        intent.putExtra(IntentReaderActivity.MODE_OF_TRANSFORM_KEY, IntentReaderActivity.REVERSE);
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        assertEquals("Expected reverse", "5 egasseM", ira.getText().toString());
    }

    public void testDefaultText() {
        Intent intent = new Intent();
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        assertEquals("Should be Default", "Default", ira.getText().toString());
    }

    public void testViewOnScreen() {
        Intent intent = new Intent();
        intent.putExtra(IntentReaderActivity.TEXT_TO_TRANSFORM_KEY, "Message 5");
        setActivityIntent(intent);

        IntentReaderActivity ira = (IntentReaderActivity) getActivity();
        TextView tv = (TextView) ira.findViewById(R.id.intentText);

        ViewAsserts.assertOnScreen(ira.getWindow().getDecorView(), tv);
    }
//-------------------------------------------------------------------------------
}
