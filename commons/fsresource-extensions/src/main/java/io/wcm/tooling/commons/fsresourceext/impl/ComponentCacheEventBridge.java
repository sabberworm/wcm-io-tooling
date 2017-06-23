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

import java.util.ArrayList;
import java.util.List;

import javax.jcr.observation.Event;
import javax.jcr.observation.EventListener;

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
    ResourceChangeListener.PATHS + "=/apps",
    ResourceChangeListener.CHANGES + "=ADDED",
    ResourceChangeListener.CHANGES + "=CHANGED",
    ResourceChangeListener.CHANGES + "=REMOVED"
})
public class ComponentCacheEventBridge implements ResourceChangeListener {

  @Reference
  private EventListenerTracker eventListenerTracker;

  private static final Logger log = LoggerFactory.getLogger(ComponentCacheEventBridge.class);

  @Override
  public void onChange(List<ResourceChange> changes) {
    EventListener eventListener = eventListenerTracker.get(EventListenerTracker.COMPONENT_CACHE_IMPL_CLASS);
    if (eventListener == null) {
      log.warn("No event Listener found for " + EventListenerTracker.COMPONENT_CACHE_IMPL_CLASS);
      return;
    }
    log.warn("Handling " + changes.size() + " events...");
    List<Event> events = new ArrayList<>();
    for (ResourceChange change : changes) {
      Event event = toEvent(change);
      if (event != null) {
        events.add(event);
      }
    }
    eventListener.onEvent(new EventIteratorImpl(events));
  }

  private Event toEvent(ResourceChange change) {
    int type = 0;
    switch (change.getType()) {
      case ADDED:
        type = Event.NODE_ADDED;
        break;
      case CHANGED:
        type = Event.PROPERTY_CHANGED;
        break;
      case REMOVED:
        type = Event.NODE_REMOVED;
        break;
      default:
        return null;
    }
    log.warn("Event " + change.getType() + " on " + change.getPath());
    return new EventImpl(type, change.getPath());
  }

}
