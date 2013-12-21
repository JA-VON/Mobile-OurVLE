/**
 *
 */
package com.mits.mobile.ourvle.Classes.TransportLayer.RemoteAPIRequests.WebServiceFunctions;

import com.mits.mobile.ourvle.Classes.DataLayer.Authentication.Session.UserSession;

/**
 * @author Aston Hamilton
 */
public class GetUserCourses extends RemoteWebServiceFunction {

    public GetUserCourses(final UserSession session) {
        super("core_enrol_get_users_courses", session,
              "userid", session.getContext()
                               .getCurretnUser().getId().toString());
    }
}
