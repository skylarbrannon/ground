package edu.berkeley.ground.api.models.postgres;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.berkeley.ground.api.PostgresTest;
import edu.berkeley.ground.api.models.EdgeVersion;
import edu.berkeley.ground.api.models.Tag;
import edu.berkeley.ground.api.versions.GroundType;
import edu.berkeley.ground.exceptions.GroundException;

import static org.junit.Assert.*;

public class PostgresEdgeVersionFactoryTest extends PostgresTest {

  public PostgresEdgeVersionFactoryTest() throws GroundException {
    super();
  }

  @Test
  public void testEdgeVersionCreation() throws GroundException {
    String firstTestNode = "firstTestNode";
    long firstTestNodeId = super.factories.getNodeFactory().create(firstTestNode, new HashMap<>()).getId();
    long firstNodeVersionId = super.factories.getNodeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), firstTestNodeId, new ArrayList<>()).getId();

    String secondTestNode = "secondTestNode";
    long secondTestNodeId = super.factories.getNodeFactory().create(secondTestNode, new HashMap<>()).getId();
    long secondNodeVersionId = super.factories.getNodeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), secondTestNodeId, new ArrayList<>()).getId();

    String edgeName = "testEdge";
    long edgeId = super.factories.getEdgeFactory().create(edgeName, firstTestNodeId,
        secondTestNodeId, new HashMap<>()).getId();

    String structureName = "testStructure";
    long structureId = super.factories.getStructureFactory().create(structureName, new HashMap<>()).getId();

    Map<String, GroundType> structureVersionAttributes = new HashMap<>();
    structureVersionAttributes.put("intfield", GroundType.INTEGER);
    structureVersionAttributes.put("boolfield", GroundType.BOOLEAN);
    structureVersionAttributes.put("strfield", GroundType.STRING);

    long structureVersionId = super.factories.getStructureVersionFactory().create(
        structureId, structureVersionAttributes, new ArrayList<>()).getId();

    Map<String, Tag> tags = new HashMap<>();
    tags.put("intfield", new Tag(-1, "intfield", 1, GroundType.INTEGER));
    tags.put("strfield", new Tag(-1, "strfield", "1", GroundType.STRING));
    tags.put("boolfield", new Tag(-1, "boolfield", true, GroundType.BOOLEAN));

    String testReference = "http://www.google.com";
    Map<String, String> parameters = new HashMap<>();
    parameters.put("http", "GET");

    long edgeVersionId = super.factories.getEdgeVersionFactory().create(tags,
        structureVersionId, testReference, parameters, edgeId, firstNodeVersionId, -1,
        secondNodeVersionId, -1, new ArrayList<>()).getId();

    EdgeVersion retrieved = super.factories.getEdgeVersionFactory().retrieveFromDatabase(edgeVersionId);

    assertEquals(edgeId, retrieved.getEdgeId());
    assertEquals(structureVersionId, retrieved.getStructureVersionId());
    assertEquals(testReference, retrieved.getReference());
    assertEquals(retrieved.getFromNodeVersionStartId(), firstNodeVersionId);
    assertEquals(retrieved.getToNodeVersionStartId(), secondNodeVersionId);
    assertEquals(retrieved.getFromNodeVersionEndId(), -1);
    assertEquals(retrieved.getToNodeVersionEndId(), -1);

    assertEquals(parameters.size(), retrieved.getParameters().size());
    assertEquals(tags.size(), retrieved.getTags().size());

    Map<String, String> retrievedParameters = retrieved.getParameters();
    Map<String, Tag> retrievedTags = retrieved.getTags();

    for (String key : parameters.keySet()) {
      assert (retrievedParameters).containsKey(key);
      assertEquals(parameters.get(key), retrievedParameters.get(key));
    }

    for (String key : tags.keySet()) {
      assert (retrievedTags).containsKey(key);
      assertEquals(tags.get(key), retrievedTags.get(key));
    }
  }


  @Test
  public void testCorrectEndVersion() throws GroundException {
    String firstTestNode = "firstTestNode";
    long firstTestNodeId = super.factories.getNodeFactory().create(firstTestNode, new HashMap<>()).getId();
    long firstNodeVersionId = super.factories.getNodeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), firstTestNodeId, new ArrayList<>()).getId();

    String secondTestNode = "secondTestNode";
    long secondTestNodeId = super.factories.getNodeFactory().create(secondTestNode, new HashMap<>()).getId();
    long secondNodeVersionId = super.factories.getNodeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), secondTestNodeId, new ArrayList<>()).getId();

    String edgeName = "testEdge";
    long edgeId = super.factories.getEdgeFactory().create(edgeName, firstTestNodeId,
        secondTestNodeId, new HashMap<>()).getId();


    long edgeVersionId = super.factories.getEdgeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), edgeId, firstNodeVersionId, -1, secondNodeVersionId, -1,
        new ArrayList<>()).getId();

    EdgeVersion retrieved = super.factories.getEdgeVersionFactory()
        .retrieveFromDatabase(edgeVersionId);

    assertEquals(edgeId, retrieved.getEdgeId());
    assertEquals(-1, retrieved.getStructureVersionId());
    assertEquals(null, retrieved.getReference());
    assertEquals(firstNodeVersionId, retrieved.getFromNodeVersionStartId());
    assertEquals(secondNodeVersionId, retrieved.getToNodeVersionStartId());
    assertEquals(-1, retrieved.getFromNodeVersionEndId());
    assertEquals(-1, retrieved.getToNodeVersionEndId());

    // create two new node versions in each of the nodes
    List<Long> parents = new ArrayList<>();
    parents.add(firstNodeVersionId);
    long fromEndId = super.factories.getNodeVersionFactory().create(new HashMap<>(), -1,
        null, new HashMap<>(), firstTestNodeId, parents).getId();

    parents.clear();
    parents.add(fromEndId);
    long newFirstNodeVersionId = super.factories.getNodeVersionFactory().create(new
        HashMap<>(), -1, null, new HashMap<>(), firstTestNodeId, parents).getId();

    parents.clear();
    parents.add(secondNodeVersionId);
    long toEndId = super.factories.getNodeVersionFactory().create(new HashMap<>(), -1, null,
        new HashMap<>(), secondTestNodeId, parents).getId();

    parents.clear();
    parents.add(toEndId);
    long newSecondNodeVersionId = super.factories.getNodeVersionFactory().create(new
        HashMap<>(), -1, null, new HashMap<>(), secondTestNodeId, parents).getId();

    parents.clear();
    parents.add(edgeVersionId);
    long newEdgeVersionId = super.factories.getEdgeVersionFactory().create(new HashMap<>(),
        -1, null, new HashMap<>(), edgeId, newFirstNodeVersionId, -1, newSecondNodeVersionId, -1,
        parents).getId();

    EdgeVersion parent = super.factories.getEdgeVersionFactory()
        .retrieveFromDatabase(edgeVersionId);
    EdgeVersion child = super.factories.getEdgeVersionFactory()
        .retrieveFromDatabase(newEdgeVersionId);

    assertEquals(edgeId, child.getEdgeId());
    assertEquals(-1, child.getStructureVersionId());
    assertEquals(null, child.getReference());
    assertEquals(newFirstNodeVersionId, child.getFromNodeVersionStartId());
    assertEquals(newSecondNodeVersionId, child.getToNodeVersionStartId());
    assertEquals(-1, child.getFromNodeVersionEndId());
    assertEquals(-1, child.getToNodeVersionEndId());

    // Make sure that the end versions were set correctly
    assertEquals(firstNodeVersionId, parent.getFromNodeVersionStartId());
    assertEquals(secondNodeVersionId, parent.getToNodeVersionStartId());
    assertEquals(fromEndId, parent.getFromNodeVersionEndId());
    assertEquals(toEndId, parent.getToNodeVersionEndId());
  }
}
