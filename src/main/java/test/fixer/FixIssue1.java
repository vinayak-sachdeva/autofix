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

            if (autoFixNeo4jKit.getTypeDeclarationFromStatementNodeId(nodeId) != typeDeclaration) {
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
        return nodeCreator.createNewSimpleType("ReturnObj");
    }

    public List<VariableDeclarationStatement> createVariableDeclarationList() {
        return new ArrayList<>();
    }

    public File createFileForDataStore() throws RefactoringException {
        ImportDeclaration importDeclaration = nodeCreator.createNewImportDeclaration("java.util", false, true);

        List <ImportDeclaration> importDeclarationList = new ArrayList<>();
        importDeclarationList.add(importDeclaration);

        return nodeCreator.createNewFile("ReturnObj", importDeclarationList, nodeCreator.createNewPackageDeclaration("package1"), null);
    }

    public TypeDeclaration createTypeDeclarationForDataStore() throws RefactoringException {
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

        return new TypeDeclaration();
    }

    public ReturnStatement createReturnStatementForNewMethod() throws RefactoringException {
        Type returnType = nodeCreator.createNewSimpleType("ReturnObj");

        SimpleName simpleName = nodeCreator.createNewSimpleName("a");

        List <Argument> argumentList = new ArrayList<>();
        argumentList.add(simpleName);

        ClassInstanceCreation classInstanceCreation = nodeCreator.createNewClassInstanceCreation(returnType, argumentList);

        return nodeCreator.createNewReturnStatement(classInstanceCreation, 1);
    }

    public void fixIssue() {
        try {
            List <VariableDeclarationStatement> variableDeclarationStatementList = createVariableDeclarationList();

            Block block = getBlockForNewMethod();
            List <Statement> insideStatementList = block.getStatements();

            ReturnStatement returnStatementForNewMethod = createReturnStatementForNewMethod();

            List <Statement> statementList = new ArrayList<>();

            for(VariableDeclarationStatement variableDeclarationStatement : variableDeclarationStatementList) {
                statementList.add(variableDeclarationStatement);
            }

            for(Statement insideStatement : insideStatementList) {
                statementList.add(insideStatement);
            }

            statementList.add(returnStatementForNewMethod);

            Block newMethodBlock = nodeCreator.createNewBlock(statementList);

            MethodDeclaration newMethodDeclaration = nodeCreator.createNewMethodDeclaration("AutoFixMethod", newMethodBlock, getReturnType());

            newMethodDeclaration.addParameter(createParameterListForNewMethod());

            TypeDeclaration newMethodTypeDeclaration = getTypeDeclarationForNewMethod();

            newMethodTypeDeclaration.addMethodDeclaration(newMethodDeclaration);

            createFileForDataStore().addTypeDeclaration(createTypeDeclarationForDataStore());
        } catch (RefactoringException re) {
            re.printStackTrace();
        }
    }
}
