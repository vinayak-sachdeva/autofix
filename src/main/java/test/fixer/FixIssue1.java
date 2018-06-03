package test.fixer;

import com.codenation.refactoring.basic.*;
import test.helper.AutoFixNeo4jKit;

import com.codenation.refactoring.basic.exception.RefactoringException;
import com.codenation.refactoring.basic.types.*;

import java.util.ArrayList;
import java.util.List;

public class FixIssue1 {

    List < List <Long> > nodeIdMappingList;
    Neo4jConnection neo4jConnection;
    NodeCreator nodeCreator;
    NodeFinder nodeFinder;
    AutoFixNeo4jKit autoFixNeo4jKit;
    String dataStoreName;

    List <VariableDeclarationStatement> variableDeclarationStatementList;

    public FixIssue1(Neo4jConnection neo4jConnection, List < List <Long> > nodeIdMappingList) {
        this.neo4jConnection = neo4jConnection;
        nodeCreator = new NodeCreator(neo4jConnection);
        nodeFinder = new NodeFinder(neo4jConnection);
        autoFixNeo4jKit = new AutoFixNeo4jKit(neo4jConnection, nodeCreator, nodeFinder);
        this.nodeIdMappingList = nodeIdMappingList;
    }

    public TypeDeclaration getTypeDeclarationForNewMethod() throws RefactoringException {
        List <Long> FirstNodeIdMapping = nodeIdMappingList.get(0);

        TypeDeclaration typeDeclaration = null;

        Boolean newClassRequiredForNewMethod = false;

        for(Long nodeId : FirstNodeIdMapping) {
            if (typeDeclaration == null) typeDeclaration = autoFixNeo4jKit.getTypeDeclarationFromStatementNodeId(nodeId);

            if (!autoFixNeo4jKit.getTypeDeclarationFromStatementNodeId(nodeId).equals(typeDeclaration)) {
                newClassRequiredForNewMethod = true;
                break;
            }
        }

        if (newClassRequiredForNewMethod == true) {
            // create a file and a class in it
            typeDeclaration = nodeCreator.createNewTypeDeclaration("AutoFixClass", null, null, null);
        }

        return typeDeclaration;
    }

    public Block getBlockForNewMethod() {
        return new Block();
    }

    public List<SingleVariableDeclaration> createParameterListForNewMethod() {
        return new ArrayList<>();
    }

    public Type getReturnType() throws RefactoringException {
        return nodeCreator.createNewSimpleType(dataStoreName);
    }

    public List<VariableDeclarationStatement> createVariableDeclarationList() {
        return new ArrayList<>();
    }


    public TypeDeclaration createTypeDeclarationForDataStore() throws RefactoringException {

        // field declaration list

        List <FieldDeclaration> fieldDeclarationList = new ArrayList<>();

        List <ModifierKeyword> modifierKeywordList = new ArrayList<>();
        modifierKeywordList.add(ModifierKeyword.PRIVATE);
        for(VariableDeclarationStatement variableDeclarationStatement : variableDeclarationStatementList) {
            List <BaseNode> variableDeclarationFragmentList =  variableDeclarationStatement.getChildren(RelationTypes.FRAGMENT);
            Type type = new Type(variableDeclarationStatement.getChildren(RelationTypes.TYPE).get(0).copySubtree().getNeo4jNode(), neo4jConnection);
            for(BaseNode variableDeclarationFragment : variableDeclarationFragmentList) {
                fieldDeclarationList.add(nodeCreator.createFieldDeclarationNode(new VariableDeclarationFragment(variableDeclarationFragment.copySubtree().getNeo4jNode(), neo4jConnection), type, modifierKeywordList, 1));
            }
        }

        // method declaration list

        List <Statement> statementList =  new ArrayList<>();

        for(VariableDeclarationStatement variableDeclarationStatement : variableDeclarationStatementList) {
            ThisExpression thisExpression = new ThisExpression(neo4jConnection);
            FieldAccess leftHandSide = nodeCreator.createFieldAccess("a", thisExpression);
            SimpleName rightHandSide = nodeCreator.createNewSimpleName("a");
            Assignment assignment = nodeCreator.createNewAssignment(Operator.EQUAL, leftHandSide, rightHandSide);
            ExpressionStatement expressionStatement = nodeCreator.createNewExpressionStatement(assignment, 1, 1);
            statementList.add(expressionStatement);
        }

        Block block = nodeCreator.createNewBlock(statementList);

        MethodDeclaration methodDeclaration = nodeCreator.createNewMethodDeclaration(dataStoreName, block, null);

        List <MethodDeclaration> methodDeclarationList = new ArrayList<>();
        methodDeclarationList.add(methodDeclaration);

        // modifier keyword list

        List <ModifierKeyword> modifierKeywordListForDataStore = new ArrayList<>();
        modifierKeywordListForDataStore.add(ModifierKeyword.PUBLIC);

        // creating class

        return nodeCreator.createNewTypeDeclaration(dataStoreName, fieldDeclarationList, methodDeclarationList, modifierKeywordListForDataStore);
    }

    public File createFileForDataStore(TypeDeclaration typeDeclaration) throws RefactoringException {
        ImportDeclaration importDeclaration = nodeCreator.createNewImportDeclaration("java.util", false, true);

        List <ImportDeclaration> importDeclarationList = new ArrayList<>();
        importDeclarationList.add(importDeclaration);

        return nodeCreator.createNewFile(dataStoreName, importDeclarationList, nodeCreator.createNewPackageDeclaration("package1"), typeDeclaration);
    }

    public ReturnStatement createReturnStatementForNewMethod() throws RefactoringException {
        Type returnType = nodeCreator.createNewSimpleType(dataStoreName);

        SimpleName simpleName = nodeCreator.createNewSimpleName("a");

        List <Argument> argumentList = new ArrayList<>();
        argumentList.add(simpleName);

        ClassInstanceCreation classInstanceCreation = nodeCreator.createNewClassInstanceCreation(returnType, argumentList);

        return nodeCreator.createNewReturnStatement(classInstanceCreation, 1);
    }

    public void fixIssue() {
        this.dataStoreName = "ReturnObj";
        try {
//            variableDeclarationStatementList = createVariableDeclarationList();
variableDeclarationStatementList = new ArrayList<>();
VariableDeclarationFragment variableDeclarationFragment = nodeCreator.createVariableDeclarationFragment("a");
Type type = nodeCreator.createNewPrimitiveType("int");
VariableDeclarationStatement variableDeclarationStatement = nodeCreator.createNewVariableDeclarationStatement(variableDeclarationFragment, type, 1);
variableDeclarationStatementList.add(variableDeclarationStatement);
//            Block block = getBlockForNewMethod();
//            List <Statement> insideStatementList = block.getStatements();

            ReturnStatement returnStatementForNewMethod = createReturnStatementForNewMethod();

            List <Statement> statementList = new ArrayList<>();

//            for(VariableDeclarationStatement variableDeclarationStatement : variableDeclarationStatementList) {
//                statementList.add(variableDeclarationStatement);
//            }

//            for(Statement insideStatement : insideStatementList) {
//                statementList.add(insideStatement);
//            }

            statementList.add(returnStatementForNewMethod);

            Block newMethodBlock = nodeCreator.createNewBlock(statementList);

            MethodDeclaration newMethodDeclaration = nodeCreator.createNewMethodDeclaration("AutoFixMethod", newMethodBlock, getReturnType());

//            newMethodDeclaration.addParameter(createParameterListForNewMethod());

            TypeDeclaration newMethodTypeDeclaration = getTypeDeclarationForNewMethod();

            newMethodTypeDeclaration.addMethodDeclaration(newMethodDeclaration);

            createFileForDataStore(createTypeDeclarationForDataStore());
        } catch (RefactoringException re) {
            re.printStackTrace();
        }
    }
}