/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.berkeley.ground.api.models;

import edu.berkeley.ground.exceptions.GroundException;

import java.util.List;
import java.util.Map;

public abstract class TagFactory {
  public abstract Map<String, Tag> retrieveFromDatabaseByVersionId(long id) throws GroundException;

  public abstract Map<String, Tag> retrieveFromDatabaseByItemId(long id) throws GroundException;

  public abstract List<Long> getVersionIdsByTag(String tag) throws GroundException;

  public abstract List<Long> getItemIdsByTag(String tag) throws GroundException;
}
