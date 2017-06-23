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

import javax.jcr.observation.EventListener;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tracks selected set of JCR observation EventListeners from AEM implementation.
 */
@Component(service = EventListenerTracker.class)
public class EventListenerTracker implements ServiceTrackerCustomizer<EventListener, Object> {

  static final String COMPONENT_CACHE_IMPL_CLASS = "com.day.cq.wcm.core.impl.components.ComponentCacheImpl";
  static final String HTML_LIBRARY_MANAGER_IMPL_CLASS = "com.adobe.granite.ui.clientlibs.impl.HtmlLibraryManagerImpl";

  private BundleContext bundleContext;
  private ServiceTracker serviceTracker;

  private volatile EventListener componentCache;
  private volatile EventListener htmlLibraryManager;

  private static final Logger log = LoggerFactory.getLogger(EventListenerTracker.class);

  @Activate
  private void activate(BundleContext context) {
    this.bundleContext = context;
    this.serviceTracker = new ServiceTracker<EventListener, Object>(context, EventListener.class, this);
    this.serviceTracker.open();
  }

  @Deactivate
  private void deactivate() {
    this.serviceTracker.close();
  }

  @Override
  public Object addingService(ServiceReference<EventListener> reference) {
    EventListener service = bundleContext.getService(reference);
    String className = service.getClass().getName();

    log.warn("Found EventListener: " + className);

    if (isComponentCache(className)) {
      componentCache = service;
    }
    else if (isHtmlLibraryManager(className)) {
      htmlLibraryManager = service;
    }

    return service;
  }

  @Override
  public void modifiedService(ServiceReference<EventListener> reference, Object service) {
    // nothing to do
  }

  @Override
  public void removedService(ServiceReference<EventListener> reference, Object service) {
    String className = service.getClass().getName();

    if (isComponentCache(className)) {
      componentCache = null;
    }
    else if (isHtmlLibraryManager(className)) {
      htmlLibraryManager = null;
    }

    bundleContext.ungetService(reference);
  }

  private boolean isComponentCache(String className) {
    return COMPONENT_CACHE_IMPL_CLASS.equals(className);
  }

  private boolean isHtmlLibraryManager(String className) {
    return HTML_LIBRARY_MANAGER_IMPL_CLASS.equals(className);
  }


  public EventListener getComponentCache() {
    return this.componentCache;
  }


  public void setComponentCache(EventListener componentCache) {
    this.componentCache = componentCache;
  }

  public EventListener getHtmlLibraryManager() {
    return this.htmlLibraryManager;
  }

  public void setHtmlLibraryManager(EventListener htmlLibraryManager) {
    this.htmlLibraryManager = htmlLibraryManager;
  }

}
