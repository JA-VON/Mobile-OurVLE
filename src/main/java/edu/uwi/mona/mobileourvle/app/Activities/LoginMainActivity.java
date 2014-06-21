package edu.uwi.mona.mobileourvle.app.Activities;

import org.sourceforge.ah.android.utilities.Communication.EntitySyncroniser.EntitySyncronizer;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Widgets.Activities.ActivityBase;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import edu.uwi.mona.mobileourvle.app.R;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.ParcelKeys;
import edu.uwi.mona.mobileourvle.app.Classes.SharedConstants.SharedContract;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.UserSession;
import edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers.UserSessionParcel;
import edu.uwi.mona.mobileourvle.app.Fragments.LoginFragment;
import edu.uwi.mona.mobileourvle.app.Fragments.LoginFragment.DefaultLoginResponse;

/**
 * The Class MainActivity.
 */
public class LoginMainActivity extends ActivityBase implements
        LoginFragment.Listener {
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState
     * the saved instance state
     */

    private DefaultLoginResponse mDefaultResponse;
    private SharedPreferences preferences;
    private EditText mUsernameTextbox;
    private EditText mPasswordTextBox;
    private CheckBox mRememberBox;
    private CheckBox mSaveBox;
    public static boolean statusSaved;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        setTitle(R.string.title_login);

        EntitySyncronizer.setupEntitySyncronizer(
                getApplicationContext(),
                SharedContract.SYNCRONIZATION_PERMISSION,
                0, 5 * 60 * 1000);

        final LoginFragment fragment = LoginFragment.newInstance();

        final android.support.v4.app.FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this
        // fragment,
        transaction.replace(R.id.fragment, fragment);

        // Commit the transaction
        transaction.commit();

        mDefaultResponse = fragment.new DefaultLoginResponse();

    }

    @Override
    public void onLoginAuthenticationSuccess(final UserSession session,
                                             final ResponseObject response) {
        final Intent intent = new Intent(LoginMainActivity.this,
                                         CourseListActivity.class);

        intent.putExtra(ParcelKeys.USER_SESSION,
                        new UserSessionParcel(session));

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if(!statusSaved) {
            preferences = getPreferences(MODE_PRIVATE);

            mUsernameTextbox = (EditText) findViewById(R.id.id_number_field);

            mPasswordTextBox = (EditText) findViewById(R.id.password_field);
            mRememberBox = (CheckBox) findViewById(R.id.remember_box);

            mSaveBox = (CheckBox) findViewById(R.id.keep_login);

            if (mRememberBox.isChecked()) {
                final String user_name = mUsernameTextbox.getText().toString();
                preferences.edit()
                        .putString("UserName", user_name)
                        .commit();
            } else
                preferences.edit()
                        .putString("UserName", "")
                        .commit();

            if (mSaveBox.isChecked()) {
                final String user_name = mUsernameTextbox.getText().toString();
                final String password = mPasswordTextBox.getText().toString();

                preferences.edit()
                        .putString("UserName", user_name)
                        .putString("Password", password)
                        .commit();

                statusSaved = true;
            }
        }


        startActivity(intent);
    }

    @Override
    public void onLoginAuthenticationFailed() {
        mDefaultResponse.onLoginAuthenticationFailed();
    }

}