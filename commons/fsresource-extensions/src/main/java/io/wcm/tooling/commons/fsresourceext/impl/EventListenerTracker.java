/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2017 wcm.io
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package io.wcm.tooling.commons.fsresourceext.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.jcr.Session;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.EventListenerIterator;
import javax.jcr.observation.ObservationManager;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Tracks selected set of JCR observation EventListeners from AEM implementation.
 */
@Component(service = EventListenerTracker.class)
public class EventListenerTracker {

  static final String COMPONENT_CACHE_IMPL_CLASS = "com.day.cq.wcm.core.impl.components.ComponentCacheImpl";
  static final String HTML_LIBRARY_MANAGER_IMPL_CLASS = "com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl";

  private static final Logger log = LoggerFactory.getLogger(EventListenerTracker.class);

  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  private final static EventListener NOT_FOUND = new EventListener() {
    @Override
    public void onEvent(EventIterator events) {
      // ignore
    }
  };

  private LoadingCache<String, EventListener> lookupCache = CacheBuilder.newBuilder()
      .expireAfterWrite(5, TimeUnit.SECONDS)
      .weakValues()
      .build(new CacheLoader<String, EventListener>() {
        @Override
        public EventListener load(String key) throws Exception {
          try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(null)) {
            Session session = resourceResolver.adaptTo(Session.class);
            ObservationManager observationManager = session.getWorkspace().getObservationManager();
            EventListenerIterator eventListeners = observationManager.getRegisteredEventListeners();
            while (eventListeners.hasNext()) {
              EventListener item = eventListeners.nextEventListener();
              log.warn("Found event listener: " + item.getClass().getName());
              if (item.getClass().getName().equals(key)) {
                return item;
              }
            }
          }
          catch (Exception ex) {
            log.warn("Error looking up JCR event listener for " + key, ex);
          }
          return NOT_FOUND;
        }
  });

  public EventListener get(String className) {
    try {
      EventListener result = lookupCache.get(className);
      if (result == NOT_FOUND) {
        return null;
      }
      return result;
    }
    catch (ExecutionException ex) {
      log.warn("Error looking up JCR event listener for " + className, ex);
      return null;
    }
  }
}
