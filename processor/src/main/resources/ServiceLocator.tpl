package ${ package };

import ${ canonicalName };

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * A generic service locator.
 */
public class ${ simpleName }ServiceLocator {

    private ${ simpleName }ServiceLocator() {
    }

    public static ${ simpleName } locate() {
        final List services = locateAll();
        return services.isEmpty() ? (${ simpleName })null : (${ simpleName })services.get(0);
    }

    public static List< ${ simpleName } > locateAll() {

        final Iterator<${ simpleName }> iterator = ServiceLoader.load(${ simpleName }.class).iterator();
        final List<${ simpleName }> services = new ArrayList<${ simpleName }>();

        while (iterator.hasNext()) {
            try {
                services.add(iterator.next());
            }
            catch (Error e) {
                e.printStackTrace(System.err);
            }
        }

        return services;

    }

}
