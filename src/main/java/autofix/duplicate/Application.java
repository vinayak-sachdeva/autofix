package autofix.duplicate;

import com.codenation.autofix.base.Sandbox;
import com.codenation.refactoring.basic.*;
import com.codenation.refactoring.basic.types.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        //SpringApplication.run(App.class, args);

        try {
            Neo4jConnection neo4jConnection = new Neo4jConnection(new Sandbox("bolt://localhost", "http://localhost", "neo4j", "eraserhead"), Integer.MAX_VALUE);


            String query1 = "match(n:Block) where n.file = \"src/systemTest/java/com/redknee/framework/xhome/ubean/TestUBeanAuthAdapter.java\" AND n.line < 560 AND n.endLine > 640 return n;";
            String query2 = "match(n) where n.file = \"src/systemTest/java/com/redknee/framework/xhome/ubean/TestUBeanAuthAdapter.java\" AND n.line < 560 AND n.endLine > 640 AND \"MethodDeclaration\" in labels(n) return n";
            QueryResult queryResult1 = neo4jConnection.runQuery(query1, null);
            QueryResult queryResult2 = neo4jConnection.runQuery(query2, null);

            //QueryResult queryResult2 = neo4jConnection.runQuery("match(n:TypeDeclaration) where n.simplename=\"Graph\" return n limit 50", null);

            BaseNode block = queryResult1.asBaseNode();
            BaseNode originalMethod = queryResult2.asBaseNode(); //originalMethodDeclaration


            //are present originally
            List <BaseNode> allChildren = block.getChildren(RelationTypes.TREE_EDGE);

            List <BaseNode> setByMethod = originalMethod.getChildren(RelationTypes.SETS);
            List <BaseNode> usedByMethod = originalMethod.getChildren(RelationTypes.USES);
            //List <BaseNode> definedByMethod = originalMethod.getChildren(RelationTypes.DEFINE);


            List <Statement> newMethodBlock = new ArrayList<>();


            NodeCreator nodeCreator = new NodeCreator(neo4jConnection);


            ////////METHOD BLOCK
            for(BaseNode child : allChildren) {
                if (child.getProperty("line").asInt() > 550 || child.getProperty("line").asInt() < 650) {
                    child.cutIncomingEdges();
                    //cut inside also (recursively)
                    newMethodBlock.add(new Statement(child.getNeo4jNode(), neo4jConnection));
                } else{

                }
            }


            ////////PARAMETER LIST

            MethodDeclaration method = nodeCreator.createNewMethodDeclaration("autofixMethod", nodeCreator.createNewBlock(newMethodBlock), null);

            List<SingleVariableDeclaration> parameterList = new ArrayList<>();
            List<VariableDeclarationStatement> variableDeclarationStatementList = new ArrayList<>();
            for (BaseNode param: usedByMethod)
            {
                BaseNode vdFragment = param.getParentNode(Labels.VariableDeclarationFragment);
                if(vdFragment.getProperty("line").asInt() > 550 && vdFragment.getProperty("line").asInt() < 650)
                {

                    BaseNode vdStatement  = vdFragment.getParentNode(Labels.VariableDeclarationStatement);
                    BaseNode fieldDecl  = vdFragment.getParentNode(Labels.FieldDeclaration);
                    BaseNode singleVd  = vdFragment.getParentNode(Labels.SingleVariableDeclaration);

                    ////// ANY OTHER ROUTE TO TYPE? (PRIMITIVE TYPES INCLUDED HERE?)
                    BaseNode type = null;
                    if(vdStatement!=null) {
                        type = vdStatement.getChildren(Labels.Type).get(0);
                    } else if (fieldDecl != null) {
                        type = fieldDecl.getChildren(Labels.Type).get(0);
                    }
                    else if(singleVd!=null){
                        type = singleVd.getChildren(Labels.Type).get(0);
                    }
                    SimpleType simpleType = new SimpleType(type.getNeo4jNode(), neo4jConnection);
                    SingleVariableDeclaration param1 = nodeCreator.createNewSingleVariableDeclaration(param.getProperty("simplename").toString() ,simpleType);
                    parameterList.add(param1);
                }



            }



            ////////VARIABLE DECLARATION STATEMENT LIST

            HashSet<String> uniqueVariableSet = new HashSet<>();
            List<BaseNode> uniqueVariableList = new ArrayList<>();
            for(BaseNode var: setByMethod) {
                if(!uniqueVariableSet.contains(var.getProperty("simplename").toString()))
                {
                    uniqueVariableList.add(var);
                }
            }

            for(BaseNode var: uniqueVariableList) {

                BaseNode vdFragment = var.getParentNode(Labels.VariableDeclarationFragment);
                if(vdFragment.getProperty("line").asInt() < 550) //outside scope declarations + fieldDeclarations
                {

                    BaseNode type=null;
                    BaseNode vdStat  = vdFragment.getParentNode(Labels.VariableDeclarationStatement);
                    BaseNode fieldDecl  = vdFragment.getParentNode(Labels.FieldDeclaration);
                    BaseNode singleVd  = vdFragment.getParentNode(Labels.SingleVariableDeclaration);

                    //CHECK IF ALL ROUTES TO TYPE COVERED (PRIMITIVE TYPE?)
                    if(vdStat!=null){
                        type = vdStat.getChildren(Labels.Type).get(0);
                    }
                    else if (fieldDecl!=null){
                        type = fieldDecl.getChildren(Labels.Type).get(0);
                    }
                    else if(singleVd!=null)
                    {
                        type = singleVd.getChildren(Labels.Type).get(0);
                    }

                    SimpleType simpleType = new SimpleType(type.getNeo4jNode(), neo4jConnection);

                    VariableDeclarationFragment variableDeclarationFragment = new VariableDeclarationFragment(vdFragment.getNeo4jNode(), neo4jConnection);
                    VariableDeclarationStatement newVariableDeclarationStatementdStatement = nodeCreator.createNewVariableDeclarationStatement(variableDeclarationFragment, simpleType, 1);
                    variableDeclarationStatementList.add(newVariableDeclarationStatementdStatement);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    }