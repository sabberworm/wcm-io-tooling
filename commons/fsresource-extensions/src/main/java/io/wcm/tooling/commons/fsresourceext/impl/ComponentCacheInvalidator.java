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

import java.util.List;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
    ResourceChangeListener.PATHS + "=/apps",
    ResourceChangeListener.CHANGES + "=ADDED",
    ResourceChangeListener.CHANGES + "=CHANGED",
    ResourceChangeListener.CHANGES + "=REMOVED"
})
public class ComponentCacheInvalidator implements ResourceChangeListener {

  private static final Logger log = LoggerFactory.getLogger(ComponentCacheInvalidator.class);

  @Reference
  private ResourceResolverFactory resourceResolverFactory;
  @Reference
  private Scheduler scheduler;

  @Override
  public void onChange(List<ResourceChange> changes) {
    log.warn("Change detected...");

    for (ResourceChange change : changes) {
      if (change.getPath().startsWith("/apps/dummy/")) {
        return;
      }
    }

    ScheduleOptions options = scheduler.NOW();
    options.name("component-cache-invalidator");
    options.canRunConcurrently(false);
    scheduler.schedule(new Runnable() {

      @Override
      public void run() {
        try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(null)) {
          Resource dummyComponent = ResourceUtil.getOrCreateResource(resolver, "/apps/dummy/components/dummy",
              ImmutableMap.<String, Object>of("jcr:primaryType", "cq:Component"), null, true);
          resolver.delete(dummyComponent);
          resolver.commit();
          log.warn("Dummy component created and removed.");
        }
        catch (LoginException ex) {
          log.warn("Unable get resource resolver.", ex);
        }
        catch (PersistenceException ex) {
          log.warn("Unable to persist changes.", ex);
        }
      }

    }, options);


  }

}
