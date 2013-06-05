/**
 * 
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.joda.time.DateTime;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;
import org.sourceforge.ah.android.utilities.Formatters.DateFormatter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Modules.Forum.DiscussionPost;
import com.mits.mobile.ourvle.Classes.DataLayer.Moodle.Users.MoodleUser;
import com.mits.mobile.ourvle.Classes.TransportLayer.JSONDescriptors.Moodle.Users.MoodleUserDescriptor;

// TODO: Auto-generated Javadoc
/**
 * The Class UserSessionDescriptior.
 * 
 * @author Aston Hamilton
 */
public class DiscussionPostDescriptior extends
	JSONObjectDescriptor<DiscussionPost> {

    @Override
    public JsonElement getJsonElement(final DiscussionPost object) {
	return null;
    }

    @Override
    public DiscussionPost getObjectFromJson(final JsonElement json) {
	final JsonObject jsonObject = (JsonObject) json;

	final long id = jsonObject.get("id").getAsLong();
	final String subject = jsonObject.get("subject").getAsString();
	final String message = jsonObject.get("message").getAsString();
	final long parent = jsonObject.get("parent").getAsLong();

	final JsonObject posterJsonObjject = jsonObject.get("poster")
		.getAsJsonObject();

	final MoodleUser poster = JSONDecoder.getObject(
		new MoodleUserDescriptor(), posterJsonObjject);

	final String createddDateTimeString = jsonObject.get("created")
		.getAsString();

	final DateTime createdDate = DateFormatter
		.getDateTimeFromISOString(createddDateTimeString);

	final String lastModifiedDateTimeString = jsonObject.get("modified")
		.getAsString();

	final DateTime lastModified = DateFormatter
		.getDateTimeFromISOString(lastModifiedDateTimeString);

	final boolean hasAttachment = jsonObject.get("has_attachment")
		.getAsInt() == 1;

	return new DiscussionPost(id, subject, message, parent, createdDate,
		lastModified, hasAttachment, poster);
    }
}
