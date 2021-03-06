package edu.berkeley.ground.api.models.cassandra;

import org.junit.Test;

import java.util.HashMap;

import edu.berkeley.ground.api.CassandraTest;
import edu.berkeley.ground.api.models.Edge;
import edu.berkeley.ground.exceptions.GroundException;

import static org.junit.Assert.*;

public class CassandraEdgeFactoryTest extends CassandraTest {

  public CassandraEdgeFactoryTest() throws GroundException {
    super();
  }

  @Test
  public void testEdgeCreation() throws GroundException {
    String testName = "test";

    long fromNodeId = 1;
    long toNodeId = 2;

    CassandraEdgeFactory edgeFactory = (CassandraEdgeFactory) super.factories.getEdgeFactory();
    edgeFactory.create(testName, fromNodeId, toNodeId, new HashMap<>());

    Edge edge = edgeFactory.retrieveFromDatabase(testName);

    assertEquals(testName, edge.getName());
    assertEquals(fromNodeId, edge.getFromNodeId());
    assertEquals(toNodeId, edge.getToNodeId());
  }
}
