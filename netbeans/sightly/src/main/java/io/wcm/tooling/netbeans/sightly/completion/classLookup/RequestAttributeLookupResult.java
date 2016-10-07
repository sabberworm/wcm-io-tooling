/*
 * #%L
 * wcm.io
 * %%
 * Copyright (C) 2014 wcm.io
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
package io.wcm.tooling.netbeans.sightly.completion.classLookup;

import javax.lang.model.element.Element;

/**
 *
 * @author amuthmann
 */
public class RequestAttributeLookupResult {

  private final String requestAttributeName;
  private final Element element;

  /**
   *
   * @param requestAttributeName
   */
  public RequestAttributeLookupResult(String requestAttributeName) {
    this(requestAttributeName, null);
  }

  /**
   *
   * @param requestAttributeName
   * @param element
   */
  public RequestAttributeLookupResult(String requestAttributeName, Element element) {
    this.requestAttributeName = requestAttributeName;
    this.element = element;
  }

  /**
   *
   * @return
   */
  public String getRequestAttributeName() {
    return requestAttributeName;
  }

  /**
   *
   * @return
   */
  public Element getElement() {
    return element;
  }

}
