package test;

import com.codenation.autofix.base.Sandbox;
import com.codenation.refactoring.basic.*;
import com.codenation.refactoring.basic.types.*;
import org.neo4j.driver.v1.types.Node;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.codenation.refactoring.basic.Neo4jConnection;
import test.fixer.FixIssue1;
import test.helper.Constants;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@SpringBootApplication
public class App {

    /*public static void refactorMethod() {
        try {
            Neo4jConnection neo4jConnection = new Neo4jConnection(new Sandbox("bolt://codegraph-staging-sandbox.devfactory.com:21991/browser/", "http://codegraph-staging-sandbox.devfactory.com:21990/browser/", "neo4j", Constants.NEO4J_PASSWORD), Integer.MAX_VALUE);

            QueryResult queryResult = neo4jConnection.runQuery("match(n:Block) where n.line <= 100 and n.endLine >= 105 and n.file=\"src/main/java/package1/Graph.java\" return n limit 50", null);
            QueryResult queryResult2 = neo4jConnection.runQuery("match(n:TypeDeclaration) where n.simplename=\"Graph\" return n limit 50", null);

            BaseNode node = queryResult.asBaseNode();
            BaseNode classNode = queryResult2.asBaseNode();

            List <BaseNode> children = node.getChildren(RelationTypes.TREE_EDGE);
            List <Statement> methodBlock = new ArrayList<>();

            for(BaseNode child : children) {
                if (child.getProperty("line").asInt() < 100 || child.getProperty("line").asInt() > 105) {
                    ;
                } else {
                    child.cutIncomingEdges();
                    methodBlock.add(new Statement(child.getNeo4jNode(), neo4jConnection));
                }
            }

            NodeCreator nodeCreator = new NodeCreator(neo4jConnection);

            MethodDeclaration method = nodeCreator.createNewMethodDeclaration("autofixMethod", nodeCreator.createNewBlock(methodBlock), null);

            System.out.println(method.id());

            classNode.addChildRelation(method, RelationTypes.MEMBER);
            classNode.addChildRelation(method, RelationTypes.TREE_EDGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void returnObjFileCreate(Neo4jConnection neo4jConnection, NodeCreator nodeCreator) {
        try {
            PackageDeclaration packageDeclaration = nodeCreator.createNewPackageDeclaration("test");

            VariableDeclarationFragment variableDeclarationFragment = nodeCreator.createVariableDeclarationFragment("a");

            PrimitiveType primitiveTypeInteger = nodeCreator.createNewPrimitiveType("int");

            List <ModifierKeyword> modifierKeywordListForFields = new ArrayList<>();
            modifierKeywordListForFields.add(ModifierKeyword.PRIVATE);

            FieldDeclaration fieldDeclaration = nodeCreator.createFieldDeclarationNode(variableDeclarationFragment, primitiveTypeInteger, modifierKeywordListForFields, 1);

            List <FieldDeclaration> fieldDeclarationList = new ArrayList<>();
            fieldDeclarationList.add(fieldDeclaration);

            Statement returnStatement = nodeCreator.createNewReturnStatement(variableDeclarationFragment, 1);

            List <Statement> statementList =  new ArrayList<>();
            statementList.add(returnStatement);

            Block block = nodeCreator.createNewBlock(statementList);

            MethodDeclaration methodDeclaration = nodeCreator.createNewMethodDeclaration("getA", block, primitiveTypeInteger);

            List <MethodDeclaration> methodDeclarationList = new ArrayList<>();
            methodDeclarationList.add(methodDeclaration);

            List <ModifierKeyword> modifierKeywordListForMethod = new ArrayList<>();
            modifierKeywordListForMethod.add(ModifierKeyword.PUBLIC);

            ClassDeclaration classDeclaration = nodeCreator.createNewTypeDeclaration("ReturnObj", fieldDeclarationList, methodDeclarationList, modifierKeywordListForMethod);

            ImportDeclaration importDeclaration = nodeCreator.createNewImportDeclaration("java.util", false, true);

            List <ImportDeclaration> importDeclarationList = new ArrayList<>();
            importDeclarationList.add(importDeclaration);

            nodeCreator.createNewFile("ReturnObj", importDeclarationList, nodeCreator.createNewPackageDeclaration("test"), classDeclaration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addReturnStatement(Neo4jConnection neo4jConnection, NodeCreator nodeCreator, NodeFinder nodeFinder) throws Exception {
        Type returnType = nodeCreator.createNewSimpleType("ReturnObj");

        List <Argument> argumentList = new ArrayList<>();

        SimpleName simpleName = nodeCreator.createNewSimpleName("a");

        argumentList.add(simpleName);

        ClassInstanceCreation classInstanceCreation = nodeCreator.createNewClassInstanceCreation(returnType, argumentList);

        ReturnStatement returnStatement = nodeCreator.createNewReturnStatement(classInstanceCreation, 1);

        Block methodBlock = (Block) nodeFinder.findListOfNodes(Labels.Block, "src/main/java/package1/Graph.java", 97, 108).get(0);

        methodBlock.addStatement(returnStatement);
    }

    public static void setFieldsFromReturnObj(Neo4jConnection neo4jConnection, NodeCreator nodeCreator, NodeFinder nodeFinder) throws Exception {
        SimpleName leftSide = nodeCreator.createNewSimpleName("a");

        SimpleType simpleType = nodeCreator.createNewSimpleType("ReturnObj");

        SimpleName simpleName = nodeCreator.createNewSimpleName("a");

        List<Argument> argumentList = new ArrayList<>();
        argumentList.add(simpleName);

        ClassInstanceCreation classInstanceCreation = nodeCreator.createNewClassInstanceCreation(simpleType, argumentList);

        FieldAccess rightSide = nodeCreator.createFieldAccess("a", classInstanceCreation);

        Assignment assignment = nodeCreator.createNewAssignment(Operator.EQUAL, leftSide, rightSide);

        ExpressionStatement expressionStatement = nodeCreator.createNewExpressionStatement(assignment, 1, 1);

        Block methodBlock = (Block) nodeFinder.findListOfNodes(Labels.Block, "src/main/java/package1/Graph.java", 97, 108).get(0);

        methodBlock.addStatement(expressionStatement);
    }*/

    public static void main(String[] args) {
        //SpringApplication.run(App.class, args);
//        try {
//            refactorMethod();

            Neo4jConnection neo4jConnection = new Neo4jConnection(new Sandbox("bolt://localhost/", "http://localhost/", "neo4j", Constants.NEO4J_PASSWORD), Integer.MAX_VALUE);

//            NodeCreator nodeCreator = new NodeCreator(neo4jConnection);

//            NodeFinder nodeFinder = new NodeFinder(neo4jConnection);

//            returnObjFileCreate(neo4jConnection, nodeCreator);

//            addReturnStatement(neo4jConnection, nodeCreator, nodeFinder);

//            setFieldsFromReturnObj(neo4jConnection, nodeCreator, nodeFinder);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Long [][] nodeIdsArray = {
            {1000334l, 1000988l},
            {1000342l, 1000996l},
            {1000354l, 1001008l},
            {1000361l, 1001015l},
            {1000369l, 1001023l},
            {1000375l, 1001029l},
            {1000381l, 1001035l},
            {1000392l, 1001046l}
        };

        List <List <Long> > nodeIdMappingList = new ArrayList<>();

        for(Long [] row : nodeIdsArray) {
            nodeIdMappingList.add(Arrays.asList(row));
        }

        new FixIssue1(neo4jConnection, nodeIdMappingList).fixIssue();
    }

}
