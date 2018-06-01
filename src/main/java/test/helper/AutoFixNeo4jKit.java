package test.helper;

import com.amazonaws.handlers.StackedRequestHandler;
import com.codenation.refactoring.basic.Neo4jConnection;
import com.codenation.refactoring.basic.NodeCreator;
import com.codenation.refactoring.basic.NodeFinder;
import com.codenation.refactoring.basic.QueryResult;
import com.codenation.refactoring.basic.exception.RefactoringException;
import com.codenation.refactoring.basic.types.BaseNode;
import com.codenation.refactoring.basic.types.TypeDeclaration;
import org.hibernate.loader.plan.spi.QuerySpaceUidNotRegisteredException;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;

public class AutoFixNeo4jKit {

    private Neo4jConnection neo4jConnection;
    private NodeCreator nodeCreator;
    private NodeFinder nodeFinder;
    private final String QUERY_TO_GET_TYPE_DECLARATION_FROM_NODE_ID = "match (statement)<-[:tree_edge*]-(class:TypeDeclaration) where id(statement)=1000220 return x";

    public AutoFixNeo4jKit(Neo4jConnection neo4jConnection, NodeCreator nodeCreator, NodeFinder nodeFinder) {
        this.neo4jConnection = neo4jConnection;
        this.nodeCreator = nodeCreator;
        this.nodeFinder = nodeFinder;
    }

    public TypeDeclaration getTypeDeclarationFromStatementNodeId(Long nodeId) throws RefactoringException {
        QueryResult queryResult = neo4jConnection.runQuery(QUERY_TO_GET_TYPE_DECLARATION_FROM_NODE_ID, null);
        return (TypeDeclaration) queryResult.asNeo4jNode();
    }
}
