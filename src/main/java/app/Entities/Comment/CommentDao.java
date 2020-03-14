package app.Entities.Comment;

import app.DB.PostgreConnector;
import app.Entities.User.User;
import app.Util.MyLogger;
import org.eclipse.jetty.util.StringUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

import static app.DB.Query.*;

public class CommentDao {

    private static MyLogger logger = MyLogger.getLogger(CommentDao.class);

    public static ArrayList<Comment> getComments(String id) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        Connection connection = PostgreConnector.createConnection();
        try {
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, SELECT_COMMENT_BY_ISSUE_ID);
            preparedStatement.setString(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String comment_id = resultSet.getString("comment_id");
                String userId = resultSet.getString("user_id");
                String userName = resultSet.getString("name");
                String text = resultSet.getString("text");
                long timestamp = Long.parseLong(resultSet.getString("timestamp"));
                User tempUser = new User(userId, userName);

                if (!StringUtil.isEmpty(userName) && !StringUtil.isEmpty(text) && !StringUtil.isEmpty(comment_id)
                        && timestamp > 0 ){
                    comments.add(new Comment(id, comment_id, text, tempUser, timestamp));
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }

        return comments;
    }

    public static int addComment
            (
                    String text,
                    String userId,
                    String issueId,
                    long timestamp
            ) throws SQLException {
        Connection connection = PostgreConnector.createConnection();
        try {
            PreparedStatement preparedStatement = PostgreConnector.createStatement(connection, INSERT_COMMENT_PARAMS);
            preparedStatement.setString(1, text);
            preparedStatement.setObject(2, UUID.fromString(issueId));
            preparedStatement.setObject(3, UUID.fromString(userId));
            preparedStatement.setLong(4, timestamp);
            return preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            logger.error(ex);
        } finally {
            connection.close();
        }
        return 0;
    }
}
