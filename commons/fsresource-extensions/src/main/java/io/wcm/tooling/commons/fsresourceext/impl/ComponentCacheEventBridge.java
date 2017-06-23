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

import org.apache.sling.api.resource.observation.ResourceChange;
import org.apache.sling.api.resource.observation.ResourceChangeListener;
import org.osgi.service.component.annotations.Component;

@Component(service = ResourceChangeListener.class, immediate = true, property = {
    ResourceChangeListener.PATHS + "=/apps",
    ResourceChangeListener.CHANGES + "=ADDED",
    ResourceChangeListener.CHANGES + "=CHANGED",
    ResourceChangeListener.CHANGES + "=REMOVED"
})
public class ComponentCacheEventBridge implements ResourceChangeListener {

  @Override
  public void onChange(List<ResourceChange> changes) {
    // TODO: Auto-generated method stub

  }

}
