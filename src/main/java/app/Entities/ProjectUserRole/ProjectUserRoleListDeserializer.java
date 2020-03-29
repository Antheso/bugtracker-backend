package app.Entities.ProjectUserRole;

import app.Entities.Project.Project;
import app.Util.MyLogger;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class ProjectUserRoleListDeserializer extends JsonDeserializer<ProjectUserRoleList> {
    private static MyLogger logger = MyLogger.getLogger(ProjectUserRoleListDeserializer.class);
    @Override
    public ProjectUserRoleList deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        JsonNode users = node.get("users");
        ProjectUserRoleList projectUserRoleList = new ProjectUserRoleList();

        int usersSize = users.size();
        for(int i = 0; i < usersSize; i++){
            String userId = "";
            String projectRoleId = "";
            try{
                userId = users.get(i).get("userId").asText();
                projectRoleId = users.get(i).get("projectRoleId").asText();

                if(projectRoleId.isEmpty() || userId.isEmpty()){
                    throw new Exception("user broken" + i);
                }
            } catch (Exception ex){
                logger.error(ex);
            }

            projectUserRoleList.add(new ProjectUserRole(projectRoleId, userId));
        }

        return projectUserRoleList;
    }
}
