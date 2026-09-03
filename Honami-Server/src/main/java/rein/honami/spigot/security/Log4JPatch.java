package rein.honami.spigot.security;

public class Log4JPatch {

    public static void patch() {
        System.setProperty("log4j2.formatMsgNoLookups", "true");
        System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "false");
        System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase", "false");
    }
}
