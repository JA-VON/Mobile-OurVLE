/**
 * 
 */
package edu.uwi.mona.mobileourvle.app.Classes.ParcableWrappers;

import org.sourceforge.ah.android.utilities.Parcels.SimpleParcableWrapper;

import android.os.Parcel;
import android.os.Parcelable;

import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SessionContext;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Authentication.Session.SiteInfo;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.MoodleUser;

/**
 * @author Aston Hamilton
 * 
 */
public class SessionContextParcel extends
	SimpleParcableWrapper<SessionContext> {
    public SessionContextParcel(final SessionContext sessionContext) {
	super(sessionContext);
    }

    public SessionContextParcel(final Parcel in) {
	super(in);

    }

    @Override
    protected SessionContext getObjectFromStream(final Parcel in) {
	final MoodleUserParcel moodleUserParcel = in
		.readParcelable(MoodleUserParcel.class.getClassLoader());

	final SiteInfoParcel siteInfoParcel = in
		.readParcelable(SiteInfoParcel.class.getClassLoader());

	final MoodleUser moodleUser = moodleUserParcel.getWrappedObejct();
	final SiteInfo siteInfo = siteInfoParcel.getWrappedObejct();

	return new SessionContext(siteInfo, moodleUser);
    }

    @Override
    protected void writeObjectToParcel(final SessionContext wrappedObject,
	    final Parcel parcel) {
	parcel.writeParcelable(
		new MoodleUserParcel(wrappedObject.getCurrentUser()), 0);

	parcel.writeParcelable(
		new SiteInfoParcel(wrappedObject.getSiteInfo()), 0);

    }

    /** The Constant CREATOR. */
    public static final Parcelable.Creator<SessionContextParcel> CREATOR =
	    new SimpleParcableWrapperCreator<SessionContextParcel>(
		    SessionContextParcel.class);
}
