package app.Entities.Comment;

import app.DB.PostgreConnector;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static app.DB.Query.*;

public class CommentDao {
    public static ArrayList<Comment> comments;

    public static ArrayList<Comment> getComments(String id)
    {
        comments = new ArrayList<Comment>();
        try {
            Statement stmt = PostgreConnector.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(SELECT_COMMENT_BY_ISSUE_ID(id));

            while (resultSet.next()) {
                Comment tempComment = new Comment();

                String user_id = resultSet.getString("user_id");
                String text = resultSet.getString("text");

                if(user_id == null && text == null)// bad way
                    continue;

                tempComment.setUserId(user_id);
                tempComment.setText(text);

                comments.add(tempComment);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }

    public static void addComment
            (
                String text,
                String userId,
                String issueId
            )
    {
        Statement stmt = null;
        try
        {
            stmt = PostgreConnector.connection.createStatement();
            stmt.executeUpdate(INSERT_COMMENT_PARAMS(text, issueId, userId));
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }
}
