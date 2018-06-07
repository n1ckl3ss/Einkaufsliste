package de.gbsschulen;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private List<ArticleEntry> articleEntries;

    public DAO() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/articles","root","mysql");
        this.preparedStatement = this.connection.prepareStatement("SELECT * FROM article WHERE designation LIKE ? ORDER BY designation");
        this.articleEntries = new ArrayList<>();
    }

    public void close() throws SQLException {
        if(this.connection != null && !this.connection.isClosed()){
            this.connection.close();
        }
    }

    public void findArticle(String designation) throws SQLException {
        this.preparedStatement.setString(1,designation);
        ResultSet resultSet = this.preparedStatement.executeQuery();

        while (resultSet.next()){
            ArticleEntry articleEntry = new ArticleEntry();
            articleEntry.setDesignation(resultSet.getString("designation"));
            articleEntry.setPrice(resultSet.getDouble("price"));

            this.articleEntries.add(articleEntry);
        }
    }

    public List<ArticleEntry> getArticleEntries() {
        return articleEntries;
    }
}
