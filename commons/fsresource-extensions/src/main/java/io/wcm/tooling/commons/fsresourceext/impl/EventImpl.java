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

import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.observation.Event;

class EventImpl implements Event {

  private final int type;
  private final String path;

  EventImpl(int type, String path) {
    this.type = type;
    this.path = path;
  }

  @Override
  public int getType() {
    return type;
  }

  @Override
  public String getPath() throws RepositoryException {
    return path;
  }

  @Override
  public String getUserID() {
    return null;
  }

  @Override
  public String getIdentifier() throws RepositoryException {
    return null;
  }

  @Override
  public Map getInfo() throws RepositoryException {
    return null;
  }

  @Override
  public String getUserData() throws RepositoryException {
    return null;
  }

  @Override
  public long getDate() throws RepositoryException {
    return 0;
  }

}
