package edu.berkeley.ground.api.models.cassandra;

import org.junit.Test;

import java.util.HashMap;

import edu.berkeley.ground.api.CassandraTest;
import edu.berkeley.ground.api.models.Graph;
import edu.berkeley.ground.exceptions.GroundException;

import static org.junit.Assert.*;

public class CassandraGraphFactoryTest extends CassandraTest {

  public CassandraGraphFactoryTest() throws GroundException {
    super();
  }

  @Test
  public void testGraphCreation() throws GroundException {
    String testName = "test";
    CassandraGraphFactory graphFactory = (CassandraGraphFactory) super.factories.getGraphFactory();
    graphFactory.create(testName, new HashMap<>());

    Graph graph = graphFactory.retrieveFromDatabase(testName);

    assertEquals(testName, graph.getName());
  }
}
