package com.mits.mobile.ourvle.Fragments;

import org.apache.http.HttpStatus;
import org.sourceforge.ah.android.utilities.Communication.CommuncationModule;
import org.sourceforge.ah.android.utilities.Communication.Interfaces.OnCommunicationResponseListener;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseError;
import org.sourceforge.ah.android.utilities.Communication.Response.ResponseObject;
import org.sourceforge.ah.android.utilities.Dialog.DialogCreator;
import org.sourceforge.ah.android.utilities.Dialog.DialogManager;
import org.sourceforge.ah.android.utilities.Plugins.DefaultCommunicationModulePlugin;
import org.sourceforge.ah.android.utilities.Plugins.BaseClass.PluggableFragment;
import org.sourceforge.ah.android.utilities.Widgets.Fragments.DialogFragmentBase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.CourseForum;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum.CourseForumDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetCourseDiscussions;
import com.mits.mobile.ourvle.R;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.SessionContext;
import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Authentication.SessionContextDescriptor;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Authentication.UserSessionDescriptior;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.LoginRemoteFunction;
import com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions.GetSessionContext;
import com.mits.mobile.ourvle.Classes.Util.ApplicationDataManager;
import com.mits.mobile.ourvle.Fragments.Shared.Dialogs.ProgressDialogFragment;

import java.util.List;

public class LoginFragment extends PluggableFragment implements
        OnCommunicationResponseListener, DialogCreator {

    private EditText mUsernameTextbox;
    private EditText mPasswordTextBox;
    private Button mLoginButton;

    private Listener mListener;

    private UserSession mUserSession;

    private DefaultCommunicationModulePlugin mCommunicationModulePlugin;

    private Activity mActivity;
    private SessionContext mSessionContext;

    public static LoginFragment newInstance() {
        final LoginFragment f = new LoginFragment();

        return f;
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mListener = (Listener) activity;
        } catch (final ClassCastException e) {
            mListener = new DefaultLoginResponse();
        }
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        mCommunicationModulePlugin = new DefaultCommunicationModulePlugin();
        registerPlugin(mCommunicationModulePlugin);

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentView = inflater.inflate(
                R.layout.fragment_login_main, container, false);

        mLoginButton = (Button) fragmentView.findViewById(R.id.login_btn);

        mUsernameTextbox = (EditText) fragmentView
                .findViewById(R.id.id_number_field);

        mPasswordTextBox = (EditText) fragmentView
                .findViewById(R.id.password_field);

        // Attach Login button
        mLoginButton.setOnClickListener(new LoginButtonListener());

        return fragmentView;
    }

    @Override
    public void onCommunicationError(final ResponseError response) {
        dismissDialog(LoginFragment.Dialogs.LOGIN_PROGRESS);
        if (response.getStatus() == HttpStatus.SC_UNAUTHORIZED)
            mListener.onLoginAuthenticationFailed();
        else
            mCommunicationModulePlugin.defaultResponseError(response);
    }

    @Override
    public void onCommunicationResponse(final int requestId,
                                        final ResponseObject response) {
        switch (requestId) {
            case Requests.LOGIN:
                mUserSession = JSONDecoder
                        .getObject(new UserSessionDescriptior(),
                                   response.getResponseText());

                CommuncationModule.sendAsyncRequest(
                        mActivity,
                        new GetSessionContext(
                                mUserSession),
                        Requests.GET_CONTEXT, this);
                break;
            case Requests.GET_CONTEXT:
                mSessionContext = JSONDecoder
                        .getObject(new SessionContextDescriptor(),
                                   response.getResponseText());

                CommuncationModule.sendAsyncRequest(
                        mActivity,
                        new GetCourseDiscussions(
                                String.valueOf(mSessionContext.getSiteInfo().getFrontPageId()),
                                mUserSession),
                        Requests.GET_SITE_NEWS_FORUM, this);
                break;
            case Requests.GET_SITE_NEWS_FORUM:
                dismissDialog(Dialogs.LOGIN_PROGRESS);
                mCommunicationModulePlugin.turnOffLoadingIcon();
                final List<CourseForum> newsForumForumList = JSONDecoder
                        .getObjectList(new CourseForumDescriptior(),
                                       response.getResponseText());

                if (newsForumForumList.size() > 0) {
                    // I'm expecting there to be only on Forum called 'Site News'
                    mSessionContext.getSiteInfo().setNewsForum(newsForumForumList.get(0));
                }

                mUserSession.setContext(mSessionContext);

                if (!ApplicationDataManager.saveLastUserSession(
                        getApplicationContext(),
                        mUserSession))
                    Log.i(getTag(), "Last Session Not Saved.");
                else
                    Log.i(getTag(), "Last session saved.");

                mListener.onLoginAuthenticationSuccess(mUserSession, response);
                break;
        }
    }

    @Override
    public DialogFragmentBase createDialog(final int id) {
        switch (id) {
            case Dialogs.LOGIN_PROGRESS:
                return new ProgressDialogFragment();
            default:
                return null;
        }
    }

    @Override
    public void onDestroy() {
        CommuncationModule.cancelAllRunningAsyncRequests(this);
        super.onDestroy();
    }

    private void dismissDialog(final int dialogId) {
        DialogManager.dismissDialog(dialogId, getFragmentManager());
    }

    private void showDialog(final int dialogId) {
        DialogManager.showDialog(dialogId, this, getFragmentManager());
    }

    /* ===================== Button Listeners ================ */
    private class LoginButtonListener implements OnClickListener {
        @Override
        public void onClick(final View v) {
            final String enteredUsername = mUsernameTextbox.getText()
                                                           .toString();
            final String enteredPassword = mPasswordTextBox.getText()
                                                           .toString();

            showDialog(Dialogs.LOGIN_PROGRESS);
            mCommunicationModulePlugin.turnOnLoadingIcon();

            CommuncationModule.sendAsyncRequest(
                    mActivity,
                    new LoginRemoteFunction(
                            enteredUsername, enteredPassword),
                    Requests.LOGIN, LoginFragment.this);
        }
    }

    /* ====================== Private classes ============== */
    public class DefaultLoginResponse implements Listener {
        @Override
        public void onLoginAuthenticationSuccess(final UserSession session,
                                                 final ResponseObject response) {
            Toast.makeText(mActivity
                                   .getApplicationContext(),
                           "Login Successfull", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onLoginAuthenticationFailed() {
            Toast.makeText(mActivity
                                   .getApplicationContext(),
                           "Invalid Login. Please try again", Toast.LENGTH_SHORT)
                 .show();
        }

    }

    /* ===================== Interfaces ====================== */

    public interface Listener {

        public void onLoginAuthenticationSuccess(UserSession session,
                                                 ResponseObject response);

        public void onLoginAuthenticationFailed();
    }

    private interface Dialogs {
        int LOGIN_PROGRESS = 0;
    }

    private interface Requests {
        int LOGIN = 0;
        int GET_CONTEXT = 1;
        int GET_SITE_NEWS_FORUM = 2;
    }

}
