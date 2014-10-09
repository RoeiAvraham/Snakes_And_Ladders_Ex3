package utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author blecherl
 */
public class SessionUtils {

    public static String getGameName(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.GAME_NAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getSoldierNum(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.SOLDIER_ID) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getDiceRes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.DICE_RES) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static void clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
    }
}
