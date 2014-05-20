/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Modules.Forum;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONEncoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.DiscussionParent;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Modules.Forum.ForumDiscussion;

// TODO: Auto-generated Javadoc

/**
 * The Class UserSessionDescriptior.
 *
 * @author Aston Hamilton
 */
public class FullForumDiscussionDescriptior extends
        JSONObjectDescriptor<ForumDiscussion> {

    final String mCourseId;

    public FullForumDiscussionDescriptior(final String pCourseId) {
        mCourseId = pCourseId;
    }

    @Override
    public JsonElement getJsonElement(final ForumDiscussion object) {
        final JsonElement encodedParent = JSONEncoder.getEncodedObjectElement(
                new DiscussionParentDescriptor(mCourseId), object.getParent());

        final JsonObject encodedDiscussion = (JsonObject) JSONEncoder
                .getEncodedObjectElement(
                        new ForumDiscussionDescriptior(object.getParent()),
                        object);

        encodedDiscussion.add("parent", encodedParent);

        return encodedDiscussion;
    }

    @Override
    public ForumDiscussion getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;

        final DiscussionParent parent = JSONDecoder.getObject(
                new DiscussionParentDescriptor(mCourseId), jsonObject.get("parent"));

        return JSONDecoder.getObject(new ForumDiscussionDescriptior(parent),
                                     jsonObject);
    }
}
