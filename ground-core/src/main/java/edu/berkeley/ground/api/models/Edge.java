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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

import edu.berkeley.ground.api.versions.Item;

public class Edge extends Item<EdgeVersion> {
  // the name of this Edge
  private String name;

  // the id of the Node that this EdgeVersion originates from
  private long fromNodeId;

  // the id of the Node that this EdgeVersion points to
  private long toNodeId;


  @JsonCreator
  public Edge(@JsonProperty("id") long id,
              @JsonProperty("name") String name,
              @JsonProperty("fromNodeId") long fromNodeId,
              @JsonProperty("toNodeId") long toNodeId,
              @JsonProperty("tags") Map<String, Tag> tags) {
    super(id, tags);

    this.name = name;
    this.fromNodeId = fromNodeId;
    this.toNodeId = toNodeId;
  }

  @JsonProperty
  public String getName() {
    return this.name;
  }

  @JsonProperty
  public long getFromNodeId() {
    return this.fromNodeId;
  }

  @JsonProperty
  public long getToNodeId() {
    return this.toNodeId;
  }


  @Override
  public boolean equals(Object other) {
    if (!(other instanceof Edge)) {
      return false;
    }

    Edge otherEdge = (Edge) other;

    return this.name.equals(otherEdge.name) &&
        this.getId() == otherEdge.getId() &&
        this.fromNodeId == otherEdge.fromNodeId &&
        this.toNodeId == otherEdge.toNodeId;
  }
}
