package app.Entities.Comment;

import app.DB.PostgreConnector;
import org.eclipse.jetty.util.StringUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import static app.DB.Query.*;

public class CommentDao {
    public static ArrayList<Comment> getComments(String id) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Connection connection = PostgreConnector.createConnection();
        try
        {
            ResultSet resultSet = PostgreConnector.executeSQL(connection, SELECT_COMMENT_BY_ISSUE_ID(id));

            while (resultSet.next())
            {
                String user_id = resultSet.getString("user_id");
                String text = resultSet.getString("text");

                if(StringUtil.isEmpty(user_id) && StringUtil.isEmpty(text))
                    continue;

                comments.add(new Comment(user_id, text));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            connection.close();
        }

        return comments;
    }

    public static int addComment
    (
        String text,
        String userId,
        String issueId
    ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try
        {
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, INSERT_COMMENT_PARAMS);
            preparedStatement.setString(1, text);
            preparedStatement.setObject(2, UUID.fromString(issueId));
            preparedStatement.setObject(3, UUID.fromString(userId));
            return preparedStatement.executeUpdate();
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            connection.close();
        }
        return 0;
    }
}
