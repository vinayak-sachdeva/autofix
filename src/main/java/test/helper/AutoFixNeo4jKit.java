package test.helper;

import com.codenation.refactoring.basic.*;
import com.codenation.refactoring.basic.exception.RefactoringException;
import com.codenation.refactoring.basic.types.TypeDeclaration;
import org.neo4j.driver.v1.Values;

import java.util.ArrayList;
import java.util.List;

public class AutoFixNeo4jKit {

    private Neo4jConnection neo4jConnection;
    private NodeCreator nodeCreator;
    private NodeFinder nodeFinder;
    private final String QUERY_TO_GET_TYPE_DECLARATION_FROM_NODE_ID = "match (statement)<-[:tree_edge*]-(class:TypeDeclaration) where id(statement)=$nodeId return class";

    public AutoFixNeo4jKit(Neo4jConnection neo4jConnection, NodeCreator nodeCreator, NodeFinder nodeFinder) {
        this.neo4jConnection = neo4jConnection;
        this.nodeCreator = nodeCreator;
        this.nodeFinder = nodeFinder;
    }

    public TypeDeclaration getTypeDeclarationFromStatementNodeId(Long nodeId) throws RefactoringException {
        QueryResult queryResult = neo4jConnection.runQuery(QUERY_TO_GET_TYPE_DECLARATION_FROM_NODE_ID, Values.parameters(new Object[]{"nodeId", nodeId}));
        return new TypeDeclaration(queryResult.asNeo4jNode(), neo4jConnection);
    }

    public List<ModifierKeyword> parseModifierStringToModifierKeywordList(String modifiers) {
        modifiers = modifiers.substring(1, modifiers.length() - 1);
        String modifierStrings[] = modifiers.split(",");
        List <ModifierKeyword> modifierKeywordList = new ArrayList<>();
        for(String modifier: modifierStrings) {
            modifierKeywordList.add(ModifierKeyword.valueOf(modifier.trim()));
        }
        return modifierKeywordList;
    }
}