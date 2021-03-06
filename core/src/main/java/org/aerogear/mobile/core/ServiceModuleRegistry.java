package org.aerogear.mobile.core;

import android.support.annotation.VisibleForTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Registry of service modules.  It is responsible for maintaining a mapping of service modules to
 * their types as well as the dependencies each module expects.
 */
public final class ServiceModuleRegistry {
    private static ServiceModuleRegistry sInstance;
    private final Map<String, Class<? extends ServiceModule>> serviceTypeMap = new HashMap<>();
    private final Map<String, List<String>> dependencyMap = new HashMap<>();


    @VisibleForTesting()
    public ServiceModuleRegistry() {}

    /**
     * Register the looking for a service module to a class.
     *
     * @param type the type of module to use
     * @param moduleClass the class that implements a service module type
     * @param dependencies the other serviceTypes this Service Depends on.
     */
    public void registerServiceModule(String type, Class<? extends ServiceModule> moduleClass, String... dependencies) {
        serviceTypeMap.put(type, moduleClass);
        for (String dependency : dependencies) {
            if (dependencyMap.get(type) == null) {
                dependencyMap.put(type, new ArrayList<>());
            }
            dependencyMap.get(type).add(dependency);
        }
    }

    public Map<String, Class<? extends ServiceModule>> services() {
        return Collections.unmodifiableMap(serviceTypeMap);
    }

    public List<String> getDependenciesFor(String serviceName) {
        List<String> list = dependencyMap.get(serviceName);
        if (list == null) {
            list = Collections.emptyList();
        }
        return Collections.unmodifiableList(list);
    }

    public Class<? extends ServiceModule> getServiceClass(String serviceType) {
        return serviceTypeMap.get(serviceType);
    }

    public synchronized static ServiceModuleRegistry getInstance() {
        if (sInstance == null) {
            sInstance = new ServiceModuleRegistry();
        }
        return sInstance;
    }
}
