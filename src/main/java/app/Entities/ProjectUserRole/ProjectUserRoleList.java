package app.Entities.ProjectUserRole;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;

@JsonDeserialize(using = ProjectUserRoleListDeserializer.class)
public class ProjectUserRoleList extends ArrayList<ProjectUserRole> {
    static String adminId = "2";
    static public ProjectUserRoleList intersection(ProjectUserRoleList list1, ProjectUserRoleList list2) {
        ProjectUserRoleList list = new ProjectUserRoleList();

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {
                if(list1.get(i).userId.equals(list2.get(j).userId))
                {
                    list.add(list1.get(i));
                }
            }
        }

        return list;
    }

    static public ProjectUserRoleList minus(ProjectUserRoleList list1, ProjectUserRoleList list2) {
        ProjectUserRoleList list = new ProjectUserRoleList();

        for (int i = 0; i < list2.size(); i++) {
            boolean add = false;
            for (int j = 0; j < list1.size(); j++) {
                if(list2.get(i).projectRoleId.equals(adminId) ||
                    list2.get(i).userId.equals(list1.get(j).userId))
                {
                    add = true;
                }
            }
            if(!add){
                list.add(list2.get(i));
            }
        }

        return list;
    }
}
