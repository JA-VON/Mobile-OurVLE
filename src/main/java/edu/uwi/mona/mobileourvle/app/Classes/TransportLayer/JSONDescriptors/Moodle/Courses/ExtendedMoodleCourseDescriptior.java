/**
 *
 */
package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Courses;

import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONDecoder;
import org.sourceforge.ah.android.utilities.Communication.JSONFactory.JSONObjectDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Courses.ExtendedMoodleCourse;
import edu.uwi.mona.mobileourvle.app.Classes.DataLayer.Moodle.Users.CourseManager;
import edu.uwi.mona.mobileourvle.app.Classes.TransportLayer.JSONDescriptors.Moodle.Users.CourseManagerDescriptor;

// TODO: Auto-generated Javadoc

/**
 * The Class UserSessionDescriptior.
 *
 * @author Aston Hamilton
 */
public class ExtendedMoodleCourseDescriptior extends
        JSONObjectDescriptor<ExtendedMoodleCourse> {

    /*
     * (non-Javadoc)
     * 
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getJsonElement(java.lang.Object)
     */
    @Override
    public JsonElement getJsonElement(final ExtendedMoodleCourse object) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see edu.uwi.mona.mobileourvle.app.Classes.CommunicationBase.JSONFactory.
     * JSONObjectDescriptor#getObjectFromJson(com.google.gson.JsonElement)
     */
    @Override
    public ExtendedMoodleCourse getObjectFromJson(final JsonElement json) {
        final JsonObject jsonObject = (JsonObject) json;

        final long courseId = jsonObject.get("id").getAsLong();
        final String fullName = jsonObject.get("fullname").getAsString();
        final String shortName = jsonObject.get("shortname").getAsString();

        // TODO - Official API Doesn't return the managers so this feature wll need to be removed in the rebuild.
        final JsonArray courseManagers = new JsonArray();

        final CourseManager[] managerList = new CourseManager[courseManagers
                .size()];

        for (int i = 0; i < courseManagers.size(); i++) {

            final CourseManager manager = JSONDecoder
                    .getObject(new CourseManagerDescriptor(),
                               courseManagers.get(i));

            managerList[i] = manager;
        }

        return new ExtendedMoodleCourse(courseId, fullName, shortName,managerList);
    }
}
